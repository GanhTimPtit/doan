package com.ptit.edu.store.admin.controller;

import com.ptit.edu.store.admin.dao.ApplicationVersionRepository;
import com.ptit.edu.store.admin.dao.StoreBranchRepository;
import com.ptit.edu.store.admin.models.body.*;
import com.ptit.edu.store.admin.models.data.StoreBranch;
import com.ptit.edu.store.admin.models.view.StoreBranchViewModel;
import com.ptit.edu.store.constants.Constant;
import com.ptit.edu.store.customer.dao.CustomerRepository;
import com.ptit.edu.store.customer.dao.ItemRepository;
import com.ptit.edu.store.customer.dao.OrderRepository;
import com.ptit.edu.store.customer.models.data.OrderCustomer;
import com.ptit.edu.store.customer.models.view.ConfirmOrderPreview;
import com.ptit.edu.store.customer.models.view.CustomerStatictisPreView;
import com.ptit.edu.store.customer.models.view.ItemPreview;
import com.ptit.edu.store.product.dao.CategoryRepository;
import com.ptit.edu.store.product.dao.ClothesRepository;
import com.ptit.edu.store.product.dao.RateClothesRepository;
import com.ptit.edu.store.product.models.body.ClothesBody;
import com.ptit.edu.store.product.models.data.Category;
import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.response_model.NotFoundResponse;
import com.ptit.edu.store.response_model.OkResponse;
import com.ptit.edu.store.response_model.Response;
import com.ptit.edu.store.response_model.ServerErrorResponse;
import com.ptit.edu.store.utils.PageAndSortRequestBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.constraints.Null;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/admins")
@Api(value = "Admin-api", description = "Nhóm API Admin, Yêu cầu access token của Admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    StoreBranchRepository storeBranchRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ClothesRepository clothesRepository;
    @Autowired
    RateClothesRepository rateClothesRepository;
    @Autowired
    ApplicationVersionRepository applicationVersionRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FirebaseMessagingService firebaseMessagingService;

    /**********************xac nhan order********************/
    @ApiOperation(value = "Lay tat ca cac don dat hang")
    @GetMapping("/orders/{status}")
    public Response getAllOrder(@PathVariable("status") int status,
                                @ApiParam(name = "pageIndex", value = "index trang, mặc định là 0")
                                @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                @ApiParam(name = "pageSize", value = "Kích thước trang, mặc định và tối đa là " + Constant.MAX_PAGE_SIZE)
                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là : " + OrderCustomer.CREATED_DATE)
                                    @RequestParam(value = "sortBy", defaultValue = OrderCustomer.CREATED_DATE) String sortBy,
                                @ApiParam(name = "sortType", value = "Nhận asc|desc, mặc đính là desc")
                                @RequestParam(value = "sortType", required = false, defaultValue = "desc") String sortType) {
        Response response;
        try {
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<ConfirmOrderPreview> orderPreviews = orderRepository.getAllConfirmOrderPreview(status, pageable);

            response = new OkResponse(orderPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Lay tat ca cac san pham don dat hang")
    @GetMapping("/orders/{oid}/closthes")
    public Response getAllOrderProduct(@PathVariable("oid") String orderID) {
        Response response;
        try {
            List<ItemPreview> itemPreviews = itemRepository.getItemPreview(orderID);
            response = new OkResponse(itemPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "xac nhan don dat hang")
    @PutMapping("/orders/confirm")
    public Response ConfirmOrder(@RequestBody OrderConfirmBody body) {
        Response response;
        try {
            String msg = "Đơn đặt hàng của bạn đã giao dịch thành công. Mời bạn click xem lịch sử mua hàng của mình. " +
                    "Mọi thắc mắc vui lòng liên hệ: 01685990771. \n" +
                    "Cám ơn!";
            int statusWhere = 4;
            if (body.getStatus() == 4) {
                msg = "Đơn đặt hàng của bạn đã được phê duyệt thành công. Chúng tôi đã gửi hàng đi, bạn vui lòng chờ nhận hàng." +
                        "Mọi thắc mắc vui lòng liên hệ: 01685990771. \n" +
                        "Cám ơn!";
                statusWhere = 0;
            }
            orderRepository.updateBillStatus(body.getStatus(), statusWhere, body.getOrderIDs());
            // List<String> userList = orderRepository.getAllCustomerID(setOrderID);
            for (String customerID : body.getCustomerIDs()) {
                firebaseMessagingService.sendNotification(FirebaseMessagingService
                        .createOrderSuccessMessage(customerID, msg,statusWhere, new Date()));
            }
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "huy don dat hang")
    @PutMapping("/orders/delete")
    public Response notifi(@RequestBody OrderDeleteBody body) {
        Response response;
        try {
            int statusWhere = 4;
            if (body.getStatus() == 2) {
                statusWhere = 0;
            }
            orderRepository.deleteBillStatus(body.getStatus(), statusWhere, body.getOrderID());
            firebaseMessagingService.sendNotification(FirebaseMessagingService
                    .createOrderSuccessMessage(body.getCustomerID(), body.getMsg(),3, new Date()));
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /********************************************** Quản lý sản phẩm *****************************************************/

    @ApiOperation(value = "thêm mới 1 danh muc")
    @PostMapping("/addCategory")
    public Response insertClothes(@RequestBody CategoryBody body) {
        Response response;
        try {
            Category category = new Category();
            category.setTitle(body.getName());
            category.setGender(body.getGender());
            if(body.getLogoUrl()!=null){
                category.setLogoUrl(body.getLogoUrl());
            }
            categoryRepository.save(category);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "thêm mới 1 sản phẩm")
    @PostMapping("/clothes")
    public Response insertClothes(@RequestBody ClothesBody clothesBody) {
        Response response;
        try {
            Category category = categoryRepository.findOne(clothesBody.getCategoryID());
            if (category == null) {
                return new NotFoundResponse("Category not Exist");
            }
            Clothes clothes = new Clothes(clothesBody);
            clothes.setCategory(category);
            clothesRepository.save(clothes);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "thêm mới 1 sản phẩm")
    @PutMapping("/clothes/{id}")
    public Response updateClothes(@PathVariable("id") String clothesID,
                                  @RequestBody ClothesBody clothesBody) {
        Response response;
        try {
            clothesRepository.updateClothes(clothesID, clothesBody.getName(), clothesBody.getLogoUrl(), clothesBody.getCost(), clothesBody.getDescription());
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Xóa 1 sản phẩm")
    @DeleteMapping("/clothes/{id}")
    public Response deleteClothes(@PathVariable("id") String clothesID) {
        Response response;
        try {
            clothesRepository.delete(clothesID);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************************************Doanh thu*****************************************************/
    @ApiOperation(value = "Thống kê doanh thu theo tháng")
    @PostMapping("/bill/statictis/{year}")
    public Response statictisBill(@PathVariable("year") String year) {
        Response response;
        try {
            List<String> dateList = new ArrayList<>();
            dateList.add(year + "-01-01");
            dateList.add(year + "-02-01");
            dateList.add(year + "-03-01");
            dateList.add(year + "-04-01");
            dateList.add(year + "-05-01");
            dateList.add(year + "-06-01");
            dateList.add(year + "-07-01");
            dateList.add(year + "-08-01");
            dateList.add(year + "-09-01");
            dateList.add(year + "-10-01");
            dateList.add(year + "-11-01");
            dateList.add(year + "-12-01");
            dateList.add((Integer.parseInt(year) + 1) + "-01-01");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<String> data = new ArrayList<>();
            for (int i = 0; i < dateList.size() - 1; i++) {
                Date parsedDate1 = dateFormat.parse(dateList.get(i));
                Date parsedDate2 = dateFormat.parse(dateList.get(i + 1));
                Timestamp timestamp1 = new java.sql.Timestamp(parsedDate1.getTime());
                Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
                Long result = orderRepository.sumCostOrderPreview(timestamp1, timestamp2);
                data.add(result != null ? result + "" : "0");
            }
            response = new OkResponse(data);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Thống kê doanh thu theo tháng")
    @PostMapping("/customer/statictis/{year}")
    public Response statictisCustomerYear(@PathVariable("year") String year) {
        Response response;
        try {
            List<String> dateList = new ArrayList<>();
            dateList.add(year + "-01-01");
            dateList.add(year + "-02-01");
            dateList.add(year + "-03-01");
            dateList.add(year + "-04-01");
            dateList.add(year + "-05-01");
            dateList.add(year + "-06-01");
            dateList.add(year + "-07-01");
            dateList.add(year + "-08-01");
            dateList.add(year + "-09-01");
            dateList.add(year + "-10-01");
            dateList.add(year + "-11-01");
            dateList.add(year + "-12-01");
            dateList.add((Integer.parseInt(year) + 1) + "-01-01");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<String> data = new ArrayList<>();
            for (int i = 0; i < dateList.size() - 1; i++) {
                Date parsedDate1 = dateFormat.parse(dateList.get(i));
                Date parsedDate2 = dateFormat.parse(dateList.get(i + 1));
                Timestamp timestamp1 = new java.sql.Timestamp(parsedDate1.getTime());
                Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
                Long result = orderRepository.sumCostOrderPreview(timestamp1, timestamp2);
                data.add(result != null ? result + "" : "0");
            }
            response = new OkResponse(data);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Thống kê sản phẩm bán chạy")
    @PostMapping("/product/statictis/hot")
    public Response statictisProductHot(@RequestBody List<String> dates,
                                        @ApiParam(name = "pageIndex", value = "Index trang, mặc định là 0")
                                        @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                        @ApiParam(name = "pageSize", value = "Kích thước trang, mặc đinh và tối đa là " + 20)
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Response response;
        try {
            String date1 = dates.get(0);
            String date2 = dates.get(1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate1 = dateFormat.parse(date1);
            Date parsedDate2 = dateFormat.parse(date2);
            Timestamp timestamp1 = new java.sql.Timestamp(parsedDate1.getTime());
            Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
            Pageable pageable = PageAndSortRequestBuilder
                    .createPageRequest(pageIndex, pageSize, null, "desc", 20);
            response = new OkResponse(clothesRepository.getAllClothesStatictisHotPreviews(pageable, timestamp1, timestamp2));
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Thống kê sản phẩm tồn kho")
    @PostMapping("/product/statictis/nothot")
    public Response statictisProductNotHot(@RequestBody List<String> dates,
                                           @ApiParam(name = "pageIndex", value = "Index trang, mặc định là 0")
                                           @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                           @ApiParam(name = "pageSize", value = "Kích thước trang, mặc đinh và tối đa là " + 20)
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Response response;
        try {
            String date1 = dates.get(0);
            String date2 = dates.get(1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate1 = dateFormat.parse(date1);
            Date parsedDate2 = dateFormat.parse(date2);
            Timestamp timestamp1 = new java.sql.Timestamp(parsedDate1.getTime());
            Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
            Pageable pageable = PageAndSortRequestBuilder
                    .createPageRequest(pageIndex, pageSize, null, "desc", 20);
            response = new OkResponse(clothesRepository.getAllClothesStatictisNotHotPreviews(pageable, clothesRepository.getAllClothesIDStatictisHot(timestamp1, timestamp2)));
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Thống kê Khách hàng")
    @GetMapping("/customer/statictis")
    public Response statictisCustomer() {
        Response response;
        try {
            List<CustomerStatictisPreView> customerStatictisPreViews = customerRepository.getCustomerStatictis();
            for (int i = 0; i < customerStatictisPreViews.size(); i++) {
                Long countRate = rateClothesRepository.countRate(customerStatictisPreViews.get(i).getId());
                if (countRate == null) {
                    customerStatictisPreViews.get(i).setTotalRate(0);
                } else {
                    customerStatictisPreViews.get(i).setTotalRate(Math.toIntExact(countRate));
                }
            }
            response = new OkResponse(customerStatictisPreViews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************************************Store Branch*****************************************************/
    @ApiOperation(value = "thêm một chi nhánh cho công ty", response = Iterable.class)
    @PostMapping("/store_branch")
    Response InsertBranch(@RequestBody StoreBranchBody storeBranchBody) {
        Response response;
        try {
            StoreBranch storeBranch = new StoreBranch(storeBranchBody);
            storeBranchRepository.save(storeBranch);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "insert data jsoup", response = Iterable.class)
    @GetMapping("/jsoup/{id}")
    Response demo(@Param("path") String path, @PathVariable("id") String categoryID) {
        Response response;
        try {

            Document document = Jsoup.connect(path).get();
            Elements listSp = document.select("div.category-products").get(0).getElementsByTag("li");
            for (Element e : listSp) {
                String url = e.select("div.category-product-list-item-view").get(0).getElementsByTag("a").attr("href");
                Document documentCon = Jsoup.connect(url).get();
                Element elementCon1 = documentCon.select("div.product-essential").get(0);
                String name = elementCon1.select("div.product-name").get(0).getElementsByTag("h1").text();
                String price = elementCon1.select("div.product-name").get(0).getElementsByTag("span").text();
                String src = elementCon1.select("div.product-image-gallery").get(0).getElementsByTag("img").attr("src");
                String description = elementCon1.select("div.product-detail-tabs").get(0).getElementsByTag("p").text();
                Clothes clothes = new Clothes();
                clothes.setName(name);
                clothes.setLogoUrl(src);
                clothes.setPrice(Integer.valueOf(price));
                clothes.setDescription(description);
                clothes.setCategory(categoryRepository.getOne(categoryID));
                clothesRepository.save(clothes);
            }

            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Lấy tất cả chi nhánh công ty", response = Iterable.class)
    @GetMapping("/store_branch")
    Response getAllBranch() {
        Response response;
        try {
            Sort sort = PageAndSortRequestBuilder.createSortRequest(StoreBranch.CREATED_DATE, "desc");
            List<StoreBranchViewModel> lsBranch = storeBranchRepository.getStoreBranchViewModel(sort);
            response = new OkResponse(lsBranch);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }

        return response;
    }

    @ApiOperation(value = "Lấy version app mobile", response = Integer.class)
    @GetMapping("/mobile/version")
    Response getVersionApp() {
        Response response;
        try {
            int version = applicationVersionRepository.findAll().get(0).getVersion();
            response = new OkResponse(version);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "xuất thông tin sản phẩm quần áo", response = Iterable.class)
    @PutMapping("import/clothes")
    Response importClothes() {
        Response response;

        try {
            InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\TuanAnhKid\\Desktop\\springboot-api-store-service\\demo.xlsx");
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            List<Clothes> clothes = new ArrayList<>();
            Iterator rows = sheet.rowIterator();

            rows.next();
            while (rows.hasNext()) {
                Clothes clothes1 = new Clothes();
                row = (XSSFRow) rows.next();
                Iterator cellIterator = row.cellIterator();
                cellIterator.next();
                while (cellIterator.hasNext()) {
                    cell = (XSSFCell) cellIterator.next();
//                    System.out.println(cell.getColumnIndex());
                    switch (cell.getColumnIndex()) {
                        case 0: {
                            System.out.print("0");
//                            clothes1.setPrice((int) getCellValue(cell));
//                            System.out.println(getCellValue(cell).toString());
                            break;
                        }
                        case 1: {
                            System.out.print("1");

//                            clothes1.setDescription((String) getCellValue(cell));
                            break;
                        }
                        case 2: {
                            System.out.print("2");

                            clothes1.setLogoUrl((String) getCellValue(cell));
                            break;
                        }

                        case 3: {
                            System.out.print("3");

                            clothes1.setName((String) getCellValue(cell));
                            break;
                        }
                        case 4: {
                            System.out.print("4");

                            clothes1.setCategory(categoryRepository.findOne((String) getCellValue(cell)));
                            break;
                        }
                        case 5: {
                            System.out.println("5");
                            clothes1.setPrice((int) ((double) getCellValue(cell)));
                            break;
                        }
                    }
                }
                clothes1.setCreatedDate(new Date());
                clothesRepository.save(clothes1);
                clothes.add(clothes1);
            }

            response = new OkResponse(clothes);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();

            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_BLANK: {
                return null;
            }
        }

        return null;
    }

    @ApiOperation(value = "khoảng cách các chi nhánh của công ty", response = Iterable.class)
    @PostMapping("/store_branch_distance")
    Response getAllBranchViewModel(@Null @RequestBody LatLngBody latLngBody,
                                   @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là : " + StoreBranch.CREATED_DATE)
                                   @RequestParam(value = "pageIndex", defaultValue = StoreBranch.CREATED_DATE) String sortBy,
                                   @ApiParam(name = "sortType", value = "Nhận asc|desc, mặc đính là desc")
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "desc") String sortType) {
        Response response;
        try {
            Sort sort = PageAndSortRequestBuilder.createSortRequest(sortBy, sortType);
            List<StoreBranchViewModel> lsBranch = storeBranchRepository.getStoreBranchViewModel(sort);
            for (StoreBranchViewModel storeBranchViewModel : lsBranch) {
                if (latLngBody != null && latLngBody.getLat() != -1 && latLngBody.getLng() != -1) {
                    storeBranchViewModel.setDistance(distance(storeBranchViewModel.getLat(), storeBranchViewModel.getLng(),
                            latLngBody.getLat(), latLngBody.getLng()));
                } else {
                    storeBranchViewModel.setDistance(-1);
                }

            }
            Collections.sort(lsBranch, new Comparator<StoreBranchViewModel>() {
                @Override
                public int compare(StoreBranchViewModel storeBranchViewModel, StoreBranchViewModel t1) {
                    if (storeBranchViewModel.getDistance() > t1.getDistance())
                        return -1;
                    return 1;
                }
            });
            response = new OkResponse(lsBranch);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }

        return response;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
