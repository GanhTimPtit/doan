package com.ptit.edu.store.customer.controller;

import com.ptit.edu.store.auth.dao.UserRepository;
import com.ptit.edu.store.constants.Constant;
import com.ptit.edu.store.customer.dao.*;
import com.ptit.edu.store.customer.models.body.OrderBody;
import com.ptit.edu.store.customer.models.body.ProfileBody;
import com.ptit.edu.store.customer.models.body.RateBody;
import com.ptit.edu.store.customer.models.body.ItemBody;
import com.ptit.edu.store.customer.models.data.*;
import com.ptit.edu.store.customer.models.view.*;
import com.ptit.edu.store.product.dao.ClothesRepository;
import com.ptit.edu.store.customer.dao.OrderRepository;
import com.ptit.edu.store.product.dao.RateClothesRepository;
import com.ptit.edu.store.product.models.body.RateClothesBody;
import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.product.models.data.ClothesSave;
import com.ptit.edu.store.product.models.data.Rating;
import com.ptit.edu.store.product.models.view.ClothesViewModel;
import com.ptit.edu.store.response_model.*;
import com.ptit.edu.store.utils.PageAndSortRequestBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/customers")
@Api(value = "customer-api", description = "Nhóm API Customer, Yêu cầu access token của Khách hàng")
@CrossOrigin(origins = "*")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RateRepository rateRepository;
    @Autowired
    private ClothesRepository clothesRepository;
    @Autowired
    SaveClothesRepository saveClothesRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RateClothesRepository rateClothesRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private LocationRepository locationRepository;

    @ApiOperation(value = "Lấy Lấy avatar + email + tên Khách hàng", response = Iterable.class)
    @GetMapping("/headerProfiles")
    public Response getHeaderProfile() {
        Response response;
        try {
            HeaderProfile headerProfile = customerRepository.getHeaderProfile(getAuthenticatedCustomerID());
            response = new OkResponse(headerProfile);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @GetMapping("/profiles")
    public Response getProfile() {
        Response response;
        try {
            Profile profile = customerRepository.getProfile(getAuthenticatedCustomerID());
            response = new OkResponse(profile);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Lấy Lấy avatar + email + tên Khách hàng", response = Iterable.class)
    @PutMapping("/profiles")
    public Response updateProfile(@RequestBody ProfileBody profileBody) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not exist!");
            }
            customer.update(profileBody);
            customerRepository.save(customer);
            Profile profile = new Profile(customer);
            response = new OkResponse(profile);
        } catch (Exception e) {
            e.printStackTrace();
            ;
            response = new ServerErrorResponse();
        }
        return response;
    }


    //Gui phan hoi
    @ApiOperation(value = "Gưi phản hồi từ khách hàng", response = Iterable.class)
    @PostMapping("/feedback")
    public Response feedback(@RequestBody String content) {
        Response response;

        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not existed!");
            }
            Feedback feedback = new Feedback(customer, content);
            feedbackRepository.save(feedback);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    //Danh gia shop
    @ApiOperation(value = "Đánh giá cửa hàng", response = Iterable.class)
    @PutMapping("/rate")
    public Response rateShop(@RequestBody RateBody rateBody) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not found");
            }
            Rate rate = new Rate(customer, rateBody);
            rateRepository.save(rate);
            response = new OkResponse("Success");
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    //Cap nhat mo ta
    @ApiOperation(value = "api Cập nhật mô tả khách hàng", response = Iterable.class)
    @PostMapping("/description")
    public Response updateDescription(@RequestBody String description) {
        Response response;
        try {
            if (customerRepository.findOne(getAuthenticatedCustomerID()) == null) {
                return new NotFoundResponse("Customer not exist");
            }
            customerRepository.updateDescription(getAuthenticatedCustomerID(), description);
            response = new OkResponse(description);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************ClothesSave********************/
    @ApiOperation(value = "Api lấy toàn bộ sản phẩm đã lưu", response = Iterable.class)
    @GetMapping("/save_clothes")
    public Response saveClothes(@ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là " + ClothesSave.SAVED_DATE)
                                @RequestParam(value = "sortBy", defaultValue = ClothesSave.SAVED_DATE) String sortBy,
                                @ApiParam(name = "sortType", value = "Nhận (asc | desc), mặc định là desc")
                                @RequestParam(value = "sortType", defaultValue = "desc") String sortType,
                                @ApiParam(name = "pageIndex", value = "Index trang, mặc định là 0")
                                @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                @ApiParam(name = "pageSize", value = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_PAGE_SIZE)
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }

            Pageable pageable = PageAndSortRequestBuilder
                    .createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<SaveClothesPreview> saveClothesPreviews = saveClothesRepository.getAllSavedClothes(getAuthenticatedCustomerID(), pageable);

            response = new OkResponse(saveClothesPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************detailClothes********************/
    @ApiOperation(value = "Lấy chi tiết sản phẩm", response = Iterable.class)
    @GetMapping("/clothes/{id}")
    public Response getDetailClothes(@PathVariable("id") String clothesID) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            Clothes clothes = clothesRepository.findById(clothesID);
            if (clothes == null) {
                return new NotFoundResponse("Clothes not Exist");
            }
            ClothesViewModel clothesViewModel = clothesRepository.getClothesViewModel(clothesID);
            Sort sort = PageAndSortRequestBuilder.createSortRequest(Rating.RATE_DATE, "desc");

            clothesViewModel.setRateClothesViewModels(rateClothesRepository.getAllRate(clothesID, sort));
            clothesViewModel.setIsSaved(saveClothesRepository.existsByCustomer_IdAndClothes_Id(getAuthenticatedCustomerID(), clothesID));
            response = new OkResponse(clothesViewModel);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Api Lưu quần áo", response = Iterable.class)
    @PostMapping("/save_clothes/{id}")
    public Response saveClothes(@PathVariable("id") String clothesID) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            Clothes clothes = clothesRepository.findById(clothesID);
            if (clothes == null) {
                return new NotFoundResponse("Clothes not Exist");
            }

            clothes.addSave();
            clothesRepository.save(clothes);

            saveClothesRepository.save(new ClothesSave(clothes, customer));
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************rateClothes********************/
    @ApiOperation(value = "Api đánh giá sản phẩm", response = Iterable.class)
    @PutMapping("/rateClothes/{id}")
    public Response rateClothes(@PathVariable("id") String clothesID,
                                @RequestBody RateClothesBody body) {
        Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
        if (customer == null) {
            return new NotFoundResponse("Customer not Exist");
        }
        Clothes clothes = clothesRepository.findById(clothesID);
        if (clothes == null) {
            return new NotFoundResponse("Clothes not Exist");
        }

        if (rateClothesRepository.existsByCustomerIdAndClothesId(getAuthenticatedCustomerID(), clothesID)) {
            Rating rating = rateClothesRepository.findByClothes_IdAndCustomer_Id(clothesID, getAuthenticatedCustomerID());
            rating.update(body);
            rateClothesRepository.save(rating);
        } else {
            Rating rating = new Rating(body);
            rating.setCustomer(customer);
            rating.setClothes(clothes);
            rateClothesRepository.save(rating);
        }
        return new OkResponse();
    }

    @ApiOperation(value = "Api hủy lưu sản phẩm", response = Iterable.class)
    @DeleteMapping("/save_clothes/{id}")
    public Response deleteSaveClothes(@PathVariable("id") String clothesID,
                                      @ApiParam(name = "pageIndex", value = "index trang, mặc định là 0")
                                      @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                      @ApiParam(name = "pageSize", value = "Kích thước trang, mặc định và tối đa là " + Constant.MAX_PAGE_SIZE)
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                      @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là : " + ClothesSave.SAVED_DATE)
                                      @RequestParam(value = "pageIndex", defaultValue = ClothesSave.SAVED_DATE) String sortBy,
                                      @ApiParam(name = "sortType", value = "Nhận asc|desc, mặc đính là desc")
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "desc") String sortType) {
        Response response;
        try {

            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            Clothes clothes = clothesRepository.findById(clothesID);
            if (clothes == null) {
                return new NotFoundResponse("Clothes not Exist");
            }

            clothes.subSave();
            clothesRepository.save(clothes);
            saveClothesRepository.deleteByCustomer_idAndAndClothes_Id(customer.getId(), clothesID);

            Pageable pageable = PageAndSortRequestBuilder
                    .createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<SaveClothesPreview> saveClothesPreviews = saveClothesRepository.getAllSavedClothes(getAuthenticatedCustomerID(), pageable);

            response = new OkResponse(saveClothesPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /***********************************************OrderCustomer*********************************/
    @ApiOperation(value = "Api tạo order cho khách hàng", response = Iterable.class)
    @PutMapping("/orders")
    public Response insertOrder(@RequestBody OrderBody orderBody) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            Location location = new Location(orderBody.getLocation(), orderBody.getLat(), orderBody.getLog());
            location = locationRepository.save(location);
            OrderCustomer orderCustomer = new OrderCustomer(orderBody.getTotalCost(), orderBody.getPayments(), orderBody.getNameCustomer(), orderBody.getPhone(), location, customer);
            Set<Item> listItems = new HashSet<>();
            for (ItemBody itemBody : orderBody.getItemBodySet()) {
                Clothes clothes = clothesRepository.findOne(itemBody.getClothesID());
                Item item = new Item(orderCustomer, clothes, itemBody.getColor(), itemBody.getSize(), itemBody.getAmount(), itemBody.getPrice());
                listItems.add(item);
            }
            if (listItems.size() > 0) {
                orderCustomer.setItems(listItems);
                orderRepository.save(orderCustomer);
            }
            response = new OkResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Lay tat ca cac don dat hang cua khach hang với trạng thái xác nhận, đang giao")
    @PostMapping("/orders/{status}")
    public Response getPageOrder(@PathVariable("status") int status,
                                 @ApiParam(name = "pageIndex", value = "index trang, mặc định là 0")
                                 @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                 @ApiParam(name = "pageSize", value = "Kích thước trang, mặc định và tối đa là " + Constant.MAX_PAGE_SIZE)
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là : " + OrderCustomer.CREATED_DATE)
                                 @RequestParam(value = "sortBy", defaultValue = OrderCustomer.CREATED_DATE) String sortBy,
                                 @ApiParam(name = "sortType", value = "Nhận asc|desc, mặc đính là desc")
                                 @RequestParam(value = "sortType", required = false, defaultValue = "desc") String sortType
    ) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<OrderPreview> orderPreviews = orderRepository.getPageOrderPreview(customer.getId(), status, pageable);
            response = new OkResponse(orderPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Lay tat ca cac don dat hang cua khach hang với trạng thái thành công, thất bại")
    @GetMapping("/orders/complete")
    public Response getPageOrderComplete(@ApiParam(name = "pageIndex", value = "index trang, mặc định là 0")
                                         @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                         @ApiParam(name = "pageSize", value = "Kích thước trang, mặc định và tối đa là " + Constant.MAX_PAGE_SIZE)
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là : " + OrderCustomer.CREATED_DATE)
                                         @RequestParam(value = "sortBy", defaultValue = OrderCustomer.CREATED_DATE) String sortBy,
                                         @ApiParam(name = "sortType", value = "Nhận asc|desc, mặc đính là desc")
                                         @RequestParam(value = "sortType", required = false, defaultValue = "desc") String sortType) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<OrderPreview> orderPreviews = orderRepository.getPageOrderPreview(customer.getId(), pageable);
            response = new OkResponse(orderPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Lay chi tiết don dat hang cua khach hang")
    @GetMapping("/orders/{oid}")
    public Response getAllDetailOrderProduct(@PathVariable("oid") String orderID) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            List<ItemPreview> itemPreviews = itemRepository.getItemPreview(orderID, customer.getId());
            response = new OkResponse(itemPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Hủy don dat hang cua khach hang")
    @DeleteMapping("/orders/{oid}")
    public Response deleteOrderProduct(@PathVariable("oid") String orderID) {
        Response response;
        try {
            Customer customer = customerRepository.findOne(getAuthenticatedCustomerID());
            if (customer == null) {
                return new NotFoundResponse("Customer not Exist");
            }
            if (!orderRepository.existsByIdAndStatus(orderID, 0)) {
                return new ForbiddenResponse("Đơn hàng này đã được phê duyệt, bạn không thể xóa");
            }
            orderRepository.delete(orderID);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /*********************************************************************************************************/
    private String getAuthenticatedCustomerID() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.getDataIDWithUsername(userEmail);
    }
}
