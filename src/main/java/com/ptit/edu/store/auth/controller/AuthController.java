package com.ptit.edu.store.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptit.edu.store.admin.dao.AdminRepository;
import com.ptit.edu.store.admin.models.data.Admin;
import com.ptit.edu.store.admin.models.body.AdminRegisterBody;
import com.ptit.edu.store.customer.dao.CustomerRepository;
import com.ptit.edu.store.product.dao.RecommendClothesRepository;
import com.ptit.edu.store.auth.dao.UserRepository;
import com.ptit.edu.store.auth.models.body.CustomerRegisterBody;
import com.ptit.edu.store.auth.models.body.FacebookLoginBody;
import com.ptit.edu.store.auth.models.data.*;
import com.ptit.edu.store.auth.models.body.NewPassword;
import com.ptit.edu.store.auth.models.view.FacebookUserInfo;
import com.ptit.edu.store.auth.models.view.LoginResult;
import com.ptit.edu.store.constants.ApplicationConstant;
import com.ptit.edu.store.constants.Constant;
import com.ptit.edu.store.constants.DataValidator;
import com.ptit.edu.store.customer.models.data.Customer;
import com.ptit.edu.store.product.dao.ClothesRepository;
import com.ptit.edu.store.product.dao.RateClothesRepository;
import com.ptit.edu.store.product.models.body.RateClothesBody;
import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.product.models.data.Rating;
import com.ptit.edu.store.product.models.data.ClothesRecommend;
import com.ptit.edu.store.response_model.*;
import com.ptit.edu.store.utils.*;
import io.swagger.annotations.*;
import net.bytebuddy.utility.RandomString;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;

import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/auths")
@Api(value = "auth-api", description = "Nhóm API đăng nhập và cấp access token, Không yêu cầu access token")
public class AuthController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    RateClothesRepository rateClothesRepository;
    @Autowired
    ClothesRepository clothesRepository;


    @Autowired
    RecommendClothesRepository recommendClothesRepository;

    @ApiOperation(value = "Đăng ký tài khoản khách hàng", response = Iterable.class)
    @PostMapping("/customer/register")
    public Response register(@ApiParam(name = HeaderConstant.AUTHORIZATION, value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả", required = true)
                             @RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString, @ApiParam(name = "customerRegisterBody", value = "Tên đầy đủ KH", required = true)
                             @Valid @RequestBody CustomerRegisterBody customerRegisterBody) {
        Response response;
        try {
            User u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!PasswordValidate.isPasswordValidate(u.getPassword())) {
                return new ForbiddenResponse(ResponseConstant.ErrorMessage.PASSWORD_TOO_SHORT);
            }

            User user = userRepository.findByUsername(u.getUsername());
            if (user != null) {
                return new ResourceExistResponse("Tai khoan da ton tai!");
            } else {
                u.setRole(RoleConstants.CUSTOMER);
                u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
                u.setActived(false);
                Customer customer = new Customer();
                customer.updatecontruct(u, customerRegisterBody);
                customer = customerRepository.save(customer);
                u = customer.getUser();
                u.setDataID(customer.getId());

                userRepository.save(u);

                SendEmailUtils.sendEmailActiveAccount(u.getUsername());
                response = new OkResponse(u.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
    @ApiOperation(value = "api đăng nhập cho khách hàng", response = Iterable.class)
    @PostMapping("/customer/login")
    public Response CustomerLogin(@ApiParam(name = "encodedString", value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả")
                                  @RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString,
                                  @RequestBody(required = false) String gcmToken) {
        Response response;
        try {
            User user = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if(userRepository.findByUsername(user.getUsername()) == null){
                return new NotFoundResponse("Account not exist");
            }
            //check xem tai khoan da duoc kich hoat chua
            if (!userRepository.isAccountActivated(user.getUsername(), RoleConstants.CUSTOMER)) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                response = login(user.getUsername(), user.getPassword(), gcmToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(HttpStatus.UNAUTHORIZED, ResponseConstant.Vi.WRONG_EMAIL_OR_PASSWORD);
        }

        return response;
    }


    private Response login(String username, String password,String gcmToken) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        JWTToken token = new HttpPostRequestBuilder(restTemplate)
                .withUrl(ApplicationConstant.LOCAL_HOST + "/oauth/token")
                .setContentType(MediaType.APPLICATION_FORM_URLENCODED)
                .addToHeader(HeaderConstant.AUTHORIZATION, HeaderConstant.AUTHORIZATION_VALUE_PREFIX + Base64Utils.encode("trusted-app:secret"))
                .setFormDataBody(body)
                .execute(JWTToken.class);

        LoginResult loginResult = new LoginResult(userRepository.getDataIDWithUsername(username),
                token.getAccess_token(),
                token.getRefresh_token(),
                token.getExpires_in());
        return new OkResponse(loginResult);
    }

    @ApiOperation(value = "Đăng nhập bằng facebook cho customer", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Đăng nhập thành công")
    })
    @PostMapping("/customer/facebook")
    public Response candidateFacebookLogin(@RequestBody FacebookLoginBody facebookLoginBody) {
        try {
            return facebookLogin(facebookLoginBody.getAccessToken(), RoleConstants.CUSTOMER,facebookLoginBody.getFcmToken());
        } catch (Exception e) {
            return new ServerErrorResponse();
        }
    }
    private Response facebookLogin(String accessToken, String role,String fcmToken) {
        FacebookUserID facebookUserID = new HttpGetRequestBuilder(restTemplate)
                .withParam(Constant.FIELDS, FacebookUser.ID)
                .withParam(Constant.ACCESS_TOKEN, accessToken)
                .withProtocol(HttpGetRequestBuilder.HTTPS)
                .withUrl("graph.facebook.com/v2.11/me")
                .execute(FacebookUserID.class);

        String userID = facebookUserID.getId();
        User user = userRepository.findByUsername(userID);
        if (user == null) {
            user = registerFacebookUser(userID, accessToken, role);
        }
        if (!user.getActived()) {
            Customer customer = customerRepository.findOne(user.getDataID());
            FacebookUserInfo facebookUserInfo = new FacebookUserInfo(customer);
            return new UnAuthorizationResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED, facebookUserInfo);
        } else {
            return login(userID, userID,fcmToken);
        }
    }

    private User registerFacebookUser(String username, String accessToken, String role) {
        FacebookUser facebookUser = new HttpGetRequestBuilder(restTemplate)
                .withParam(Constant.FIELDS, FacebookUser.ID,
                        FacebookUser.EMAIL,
                        FacebookUser.BIRTHDAY,
                        FacebookUser.GENDER,
                        FacebookUser.FULL_NAME,
                        FacebookUser.FIRST_NAME,
                        FacebookUser.LAST_NAME,
                        FacebookUser.COVER)
                .withParam(Constant.ACCESS_TOKEN, accessToken)
                .withProtocol(HttpGetRequestBuilder.HTTPS)
                .withUrl("graph.facebook.com/v2.11/me")
                .execute(FacebookUser.class);

        FacebookAvatar facebookAvatar = new HttpGetRequestBuilder(restTemplate)
                .withProtocol(HttpGetRequestBuilder.HTTPS)
                .withParam(FacebookAvatar.TYPE, FacebookAvatar.LARGE)
                .withParam(FacebookAvatar.REDIRECT, false)
                .withParam(Constant.ACCESS_TOKEN, accessToken)
                .withUrl("graph.facebook.com/v2.11/me/picture")
                .execute(FacebookAvatar.class);

        User user = new User();
        user.setAccountType(Constant.FACEBOOK);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(username));
        user.setRole(role);
        user.setActived(true);
        user = userRepository.save(user);

        switch (role) {
            case RoleConstants.CUSTOMER: {
                Customer customer = new Customer();
                customer.setUser(user);
                customer.setLastName(facebookUser.getLast_name());
                customer.setFirstName(facebookUser.getFirst_name());
                customer.setBirthday(facebookUser.getBirthday());
                customer.setEmail(facebookUser.getEmail());
                customer.setGender(DataValidator.getGender(facebookUser.getGender()));
                if (facebookAvatar != null) {
                    customer.setAvatarUrl(facebookAvatar.getData().getUrl());
                }
                FacebookCover facebookCover = facebookUser.getCover();
                if (facebookCover != null) {
                    customer.setCoverUrl(facebookCover.getSource());
                }
                customer = customerRepository.save(customer);
                user.setDataID(customer.getId());
                user = userRepository.save(user);
            }
            break;


            default: {
                break;
            }
        }
        return user;
    }
//    @ApiOperation(value = "Cập nhật thông tin cho tài khoản đăng ký bằng facebook", response = Iterable.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Đăng nhập thành công", response = TokenOkResponseModel.class),
//            @ApiResponse(code = 401, message = "Tài khoản cần đăng ký đủ thông tin", response = FacebookAccountNotVerifiedResponseModel.class)
//    })
//    @PostMapping("/candidates/facebook/confirm")
//    public Response confirmFacebookLogin(@Valid @RequestBody FacebookUserRegisterBody facebookUserRegisterBody) {
//        try {
//            FacebookUserID facebookUserID = new HttpGetRequestBuilder(restTemplate)
//                    .withParam(Constant.FIELDS, FacebookUser.ID)
//                    .withParam(Constant.ACCESS_TOKEN, facebookUserRegisterBody.getAccessToken())
//                    .withProtocol(HttpGetRequestBuilder.HTTPS)
//                    .withUrl("graph.facebook.com/v2.11/me")
//                    .execute(FacebookUserID.class);
//
//            User user = userRepository.findByUsername(facebookUserID.getId());
//            Candidate candidate = candidateRepository.findById(user.getDataID());
//            candidate.update(facebookUserRegisterBody);
//
//            if (!GoogleLocationService.updateRegionAndAddressFor(candidate, regionRepository, restTemplate)) {
//                return new NotFoundResponse(ResponseConstant.En.REGION_NOT_SUPPORTED);
//            }
//
//            candidateRepository.save(candidate);
//            user.setIsActivated(true);
//            userRepository.save(user);
//
//            return login(user.getUsername(), user.getUsername(),facebookUserRegisterBody.getGcmToken());
//        } catch (Exception e) {
//            return new ServerErrorResponse();
//        }
//    }
    @ApiOperation(value = "Api đăng nhập cho admin", response = Iterable.class)
    @PostMapping("/admin/login")
    public Response AdminLogin(@ApiParam(name = "encodedString", value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả")
                               @RequestHeader(HeaderConstant.AUTHORIZATION) String encodedString) {
        Response response;
        try {
            User user = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!userRepository.isAccountActivated(user.getUsername(), RoleConstants.ADMIN)) {
                response = new ForbiddenResponse(ResponseConstant.ErrorMessage.ACCOUNT_NOT_VERIFIED);
            } else {
                User login = userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());

                if (login != null) {
                    Admin admin= adminRepository.findOne(login.getDataID());
                    response = new OkResponse(admin.getFullName());
                } else {
                    response = new Response(HttpStatus.UNAUTHORIZED, ResponseConstant.Vi.WRONG_EMAIL_OR_PASSWORD);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }


    @ApiOperation(value = "Đăng ký tài khoản admin", response = Iterable.class)
    @PostMapping("/admin/register")
    public Response adminRegister(@ApiParam(name = HeaderConstant.AUTHORIZATION, value = "username+\":\"+password, lấy kết quả encode theo Base64, sau đó thêm \"Basic \" + kết quả", required = true)
                                  @RequestHeader(value = HeaderConstant.AUTHORIZATION) String encodedString, @ApiParam(name = "adminRegisterBody", value = "Tên đầy đủ KH", required = true)
                                  @Valid @RequestBody AdminRegisterBody adminRegisterBody) {
        Response response;
        try {
            User u = UserDecodeUtils.decodeFromAuthorizationHeader(encodedString);
            if (!PasswordValidate.isPasswordValidate(u.getPassword())) {
                return new ForbiddenResponse(ResponseConstant.ErrorMessage.PASSWORD_TOO_SHORT);
            }

            User user = userRepository.findByUsername(u.getUsername());
            if (user != null) {
                return new ResourceExistResponse("Tai khoan da ton tai!");
            } else {
                user = new User();
                user.setPassword(u.getPassword());
                user.setUsername(u.getUsername());
                user.setRole(RoleConstants.ADMIN);
                user.setActived(true);
                Admin admin = new Admin();
                admin.setFullName(adminRegisterBody.getFullName());
                admin.setPosition(adminRegisterBody.getPosition());
                admin.setUser(user);
                adminRepository.save(admin);
                admin = adminRepository.save(admin);
                u = admin.getUser();
                u.setDataID(admin.getId());

                userRepository.save(u);


//                SendEmailUtils.sendEmailrequest(u.getUsername());
                response = new OkResponse(user.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Xac nhan email", response = Iterable.class)
    @GetMapping("/registration/confirm/{username}")
    public Response confirm_email(@PathVariable("username") String username) {
        Response response;
        try {
            username += ".com";
            if (userRepository.findByUsername(username) == null) {
                return new NotFoundResponse("Email khong ton tai !");
            }
            userRepository.activeAccount(true, username);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Xac nhan email", response = Iterable.class)
    @PostMapping("/resend/registration/confirm/{username}")
    public Response resend_confirm_email(@PathVariable("username") String username) {
        Response response;
        try {
            username += ".com";
            SendEmailUtils.sendEmailActiveAccount(username);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Đổi mật khẩu", response = Iterable.class)
    @PostMapping("/customer/{customerID}/newPassword")
    public Response changePassword(@PathVariable("customerID") String customerID,
                                   @Valid @RequestBody NewPassword password) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(customerID);
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            User u = customer.getUser();
            if (u.getPassword().matches(password.getOldPassword())) {
                u.setPassword(password.getNewPassword());
                userRepository.save(u);
                response = new OkResponse();
            } else {
                response = new Response(HttpStatus.CONFLICT, ResponseConstant.Vi.OLD_PASSWORD_MISMATCH);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Quên mật khẩu (Gửi lại email reset mật khẩu)", response = Iterable.class)
    @PostMapping("/customer/{id}/reset_password")
    public Response sendEmailToRessetPassword(
            @PathVariable("id") String customerID,
            @Valid @RequestBody String email) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(customerID);
            if(customer==null){
                return new NotFoundResponse("Customer not Exist");
            }
            if(!EmailValidate.validate(email)){
                return new Response(HttpStatus.GONE,ResponseConstant.ErrorMessage.INVALID_EMAIL);
            }

            User u = customer.getUser();
            RandomString randomString = new RandomString();
            String resetPassword = randomString.nextString();
            u.setPassword(resetPassword);
            userRepository.save(u);
            SendEmailUtils.sendEmailResetPassword(email,resetPassword);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Quên mật khẩu (Gửi lại email reset mật khẩu)", response = Iterable.class)
    @PostMapping("/jsonrecommend")
    public Response pullJsonRecommend() {
        Response response;
        try {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        Customer test = objectMapper.readValue(con.getInputStream(),Customer.class);
        System.out.println(test.toString());
        response= new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Xuất excel ratting" , response = Iterable.class)
    @GetMapping("/exportCSVRatting")
    public Response createCSVRatting() throws IOException {
        List<String> customerIDs = customerRepository.findAllID();
        List<String> listclotheIDs = clothesRepository.findAllID();
        listclotheIDs.add(0, "user");
        List<List<Object>> rowData= new ArrayList<>();
        for (int i = 0; i < customerIDs.size() ; i++) {
            List<Object> data= new ArrayList<>();
            data.add(i+1);
            for (int j = 1; j < listclotheIDs.size(); j++) {
                Rating rating = rateClothesRepository.findByClothes_IdAndCustomer_Id(listclotheIDs.get(j),customerIDs.get(i));
                if(rating ==null){
                    data.add(0);
                }else {
                    data.add(rating.getValue());
                }
            }
            rowData.add(data);

        }
        ICsvListWriter listWriter = null;
        try {
            listWriter = new CsvListWriter(new FileWriter("recommenditem/dataratting.csv"),
                    CsvPreference.STANDARD_PREFERENCE);

           // final CellProcessor[] processors = getProcessors();
           // final String[] header = new String[] { "customerNo", "firstName", "lastName", "birthDate",
            //        "mailingAddress", "married", "numberOfKids", "favouriteQuote", "email", "loyaltyPoints" };
            String[] header= new String[listclotheIDs.size()];
            header= listclotheIDs.toArray(header);
            // write the header
            listWriter.writeHeader(header);

            // write the customer lists
            for (int i = 0; i < rowData.size() ; i++) {
                listWriter.write(rowData.get(i));
            }

        }
        finally {
            if( listWriter != null ) {
                listWriter.close();
            }
        }
        return new OkResponse();
    }
    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // firstName
                new NotNull(), // lastName
                new NotNull(), // mailingAddress
                new NotNull(), // favouriteQuote
                new NotNull(), // favouriteQuote
                new NotNull()
        };

        return processors;
    }
    @ApiOperation(value = "Xuất excel ratting" , response = Iterable.class)
    @GetMapping("/importCSVRatting")
    public Response importCSVRecommend() throws IOException {
        Response response;

        ICsvListReader listReader = null;
        try {
            listReader = new CsvListReader(new FileReader("recommenditem/dataresult.csv"), CsvPreference.STANDARD_PREFERENCE);

            listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
            final CellProcessor[] processors = getProcessors();

            List<Object> customerList;
            while( (customerList = listReader.read(processors)) != null ) {
                for (int i = 2; i < customerList.size(); i++) {
                    ClothesRecommend clothesRecommend = new ClothesRecommend(clothesRepository.findOne(customerList.get(0).toString()),clothesRepository.findOne(customerList.get(i).toString()),(i-1));
                    recommendClothesRepository.save(clothesRecommend);
                }

//                RecommendItem recommendItem= new RecommendItem(customerList.get(0).toString(), customerList.get(2).toString(), customerList.get(3).toString(), customerList.get(4).toString(), customerList.get(5).toString());
//                recommendItemRepository.save(recommendItem);
            }
        }
        finally {
            if( listReader != null ) {
                listReader.close();
            }
        }
        return new OkResponse();
    }
    @ApiOperation(value = "Xuất excel ratting" , response = Iterable.class)
    @GetMapping("/exportRatting")
    public Response createExcelShareBill() throws IOException {
        List<Customer> customers = customerRepository.findAll();
        List<Clothes> listclothes = clothesRepository.findAll();
       // List<Rating> rates = rateClothesRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Ratting sheet");
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rownum);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("user");
        cell.setCellStyle(style);
        for (int i = 0; i < listclothes.size(); i++) {
            cell = row.createCell(i+1, CellType.STRING);
            cell.setCellValue(listclothes.get(i).getId());
            cell.setCellStyle(style);
        }
        for (Customer customer : customers) {
            rownum++;
            row = sheet.createRow(rownum);
            row.createCell(0, CellType.STRING).setCellValue(customer.getId());
            for (int i = 0; i < listclothes.size(); i++) {
                Rating rating = rateClothesRepository.findByClothes_IdAndCustomer_Id(listclothes.get(i).getId(), customer.getId());
                if(rating ==null){
                    row.createCell(i+1, CellType.NUMERIC).setCellValue(0);
                }else {
                    row.createCell(i + 1, CellType.NUMERIC).setCellValue(rating.getValue());
                }
            }
        }
        FileOutputStream fileOutputStream= new FileOutputStream(new File("data.xlsx"));
        workbook.write(fileOutputStream);
        return new OkResponse();
    }
    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
    @ApiOperation(value = "random ratting", response = Iterable.class)
    @GetMapping("/customer/ratting/{gender}")
    public Response ratting(@PathVariable("gender") int gender) {
        Response response;
        try {

            List<Customer> customers = customerRepository.findAll();

            List<Clothes> listclothes = clothesRepository.findAll();

            for (int i = 0; i < 1000; i++) {
                Random randomCustomer= new Random();
                Random randomClothes= new Random();
                Random ramdomPoint= new Random();
                Customer customer= customers.get(randomCustomer.nextInt(customers.size()));
                Clothes clothes= listclothes.get(randomClothes.nextInt(listclothes.size()));
                int diem= ramdomPoint.nextInt(5)+1;
                RateClothesBody rateClothesBody=null;
                switch (diem){
                    case 1:{
                        rateClothesBody= new RateClothesBody("Sản phẩm này kém chất lượng",1);
                        break;
                    }
                    case 2:{
                        rateClothesBody= new RateClothesBody("Sản phẩm chưa thực sự tốt",2);
                        break;
                    }
                    case 3:{
                        rateClothesBody= new RateClothesBody("Sản phẩm này tạm được",3);
                        break;
                    }case 4:{
                        rateClothesBody= new RateClothesBody("Sản phẩm khá tuyệt",4);
                        break;
                    }
                    case 5:{
                        rateClothesBody= new RateClothesBody("Sản phẩm quá tuyệt vời!",5);
                        break;
                    }

                }


                if(rateClothesRepository.existsByCustomerIdAndClothesId(customer.getId(),clothes.getId())){
                    Rating rating = rateClothesRepository.findByClothes_IdAndCustomer_Id(clothes.getId(),customer.getId());
                    rating.update(rateClothesBody);
                    rateClothesRepository.save(rating);
                }else {
                    Rating rating = new Rating(rateClothesBody);
                    rating.setCustomer(customer);
                    rating.setClothes(clothes);
                    rateClothesRepository.save(rating);
                }
            }

            response= new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Làm mới access token", response = Iterable.class,
            notes = "Nếu access token hết hạn - cấp access token mới dựa vào refresh token\nNếu refresh token hết hạn, trả về 401")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Làm mới access token thành công"),
            @ApiResponse(code = 400, message = "Trường không hợp lệ"),
            @ApiResponse(code = 401, message = "Refresh token hết hạn")
    }
    )
    @PostMapping("/accessToken/refresh")
    public ResponseEntity refreshAccessToken(@Valid @RequestBody TokenGroup tokenGroup) throws IOException {
        ResponseEntity response;
        JWTTokenPayload jwtAccessTokenPayload = AccessTokenUtil
                .decodeJWTAccessTokenPayload(tokenGroup.getAccessToken());
        Date now = new Date();
        long nowInMillis = now.getTime();
        if (nowInMillis > jwtAccessTokenPayload.getExp() * 1000) {
            String refreshToken = tokenGroup.getRefreshToken();
            JWTTokenPayload jwtRefreshTokenPayload = AccessTokenUtil
                    .decodeJWTAccessTokenPayload(tokenGroup.getRefreshToken());
            if (nowInMillis > jwtRefreshTokenPayload.getExp() * 1000) {
                response = new ResponseEntity<>(new TokenExpirationDto(refreshToken), Response.prepareHeader(), HttpStatus.UNAUTHORIZED);
            } else {
                TokenData tokenData = generateNewAccessToken(refreshToken);
                tokenGroup.setAccessToken(tokenData.getAccessToken());
                tokenGroup.setRefreshToken(tokenData.getRefreshToken());
                response = new OkResponse(tokenGroup);
            }
        } else {
            response = new OkResponse(tokenGroup);
        }
        return response;
    }
    public TokenData generateNewAccessToken(String refreshToken) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("username", "trusted-app");
        body.add("refresh_token", refreshToken);

        JWTToken token = new HttpPostRequestBuilder(restTemplate)
                .withUrl(ApplicationConstant.LOCAL_HOST + "/oauth/token")
                .withProtocol(HttpPostRequestBuilder.HTTP)
                .addToHeader(HeaderConstant.AUTHORIZATION, HeaderConstant.AUTHORIZATION_VALUE_PREFIX + Base64Utils.encode("trusted-app:secret"))
                .setFormDataBody(body)
                .execute(JWTToken.class);

        return new TokenData(token.getAccess_token(),
                token.getRefresh_token(),
                token.getExpires_in());
    }

}
