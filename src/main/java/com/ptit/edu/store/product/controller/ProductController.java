package com.ptit.edu.store.product.controller;

import com.ptit.edu.store.auth.dao.UserRepository;
import com.ptit.edu.store.constants.Constant;
import com.ptit.edu.store.customer.dao.CustomerRepository;
import com.ptit.edu.store.customer.dao.SaveClothesRepository;
import com.ptit.edu.store.product.dao.*;
import com.ptit.edu.store.product.models.body.ClothesBody;
import com.ptit.edu.store.product.models.data.*;
import com.ptit.edu.store.product.models.view.ClothesPreview;
import com.ptit.edu.store.product.models.view.ClothesViewModel;
import com.ptit.edu.store.product.models.view.RateClothesViewModel;
import com.ptit.edu.store.response_model.*;
import com.ptit.edu.store.utils.PageAndSortRequestBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Api(value = "candidate-api", description = "Nhóm API Customer, Yêu cầu access token của Khách hàng")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ClothesRepository clothesRepository;
    @Autowired
    private SaveClothesRepository saveClothesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RateClothesRepository rateClothesRepository;

    @Autowired
    private RecommendClothesRepository recommendClothesRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;


    @ApiOperation(value = "api Thêm mới sản phẩm quần áo", response = Iterable.class)
    @PutMapping("/clothes/{id}")
    public Response insertClothes(@PathVariable("id") String categoryID,
                                  @RequestBody ClothesBody clothesBody) {
        Category category = categoryRepository.findOne(categoryID);
        if (category == null) {
            return new NotFoundResponse("Category not Exist");
        }
        Clothes clothes = new Clothes(clothesBody);
        clothes.setCategory(category);
        clothesRepository.save(clothes);
        return new OkResponse();
    }


    /**********************ClothesSave********************/

//    @ApiOperation(value = "api Lưu sản phẩm", response = Iterable.class)
//    @PostMapping("/{customerID}/saveClothes/{id}")
//    public Response saveClothes(@PathVariable("customerID") String customerID, @PathVariable("id") String clothesID) {
//        Response response;
//        try {
//            Customer customer = customerRepository.findOne(customerID);
//            if (customer == null) {
//                return new NotFoundResponse();
//            }
//            Clothes clothes = clothesRepository.findById(clothesID);
//            if (clothes == null) {
//                return new NotFoundResponse("Clothes not Exist");
//            }
//            if (saveClothesRepository.existsByCustomer_IdAndClothes_Id(customerID, clothesID)) {
//                return new ResourceExistResponse();
//            }
//            clothes.addSave();
//            saveClothesRepository.save(new ClothesSave(clothes, customer));
//            response = new OkResponse();
//        } catch (Exception e) {
//            e.printStackTrace();
//            response = new ServerErrorResponse();
//        }
//        return response;
//    }
//    @ApiOperation(value = "Api hủy lưu sản phẩm", response = Iterable.class)
//    @DeleteMapping("/{customerID}/saveClothes/{id}/")
//    public Response deleteSaveClothes(@PathVariable("customerID") String customerID, @PathVariable("id") String clothesID) {
//        Response response;
//        try {
//
//            Customer customer = customerRepository.findOne(customerID);
//            if (customer == null) {
//                return new NotFoundResponse();
//            }
//            Clothes clothes = clothesRepository.findOne(clothesID);
//            if (clothes == null) {
//                return new NotFoundResponse("Clothes not Exist");
//            }
//            if (!saveClothesRepository.existsByCustomer_IdAndClothes_Id(customerID, clothesID)) {
//                return new ResourceExistResponse();
//            }
//
//            clothes.subSave();
//            clothesRepository.save(clothes);
//            saveClothesRepository.deleteByCustomer_idAndAndClothes_Id(clothesID, customer.getId());
//            response = new OkResponse();
//        } catch (Exception e) {
//            e.printStackTrace();
//            response = new ServerErrorResponse();
//        }
//        return response;
//
//    }
    @ApiOperation(value = "Api lấy toàn bộ đánh giá sản phẩm", response = Iterable.class)
    @GetMapping("/rateClothes/{id}")
    public Response getAllRateClothes(@PathVariable("id") String clothesID,
                                      @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là " + Rating.RATE_DATE)
                                      @RequestParam(value = "sortBy", defaultValue = Rating.RATE_DATE) String sortBy,
                                      @ApiParam(name = "sortType", value = "Nhận (asc | desc), mặc định là desc")
                                      @RequestParam(value = "sortType", defaultValue = "desc") String sortType) {

        Clothes clothes = clothesRepository.findById(clothesID);
        if (clothes == null) {
            return new NotFoundResponse("Clothes not Exist");
        }
        Sort sort =
                PageAndSortRequestBuilder.createSortRequest(sortBy, sortType);
        List<RateClothesViewModel> rateClothesViewModels = rateClothesRepository.getAllRate(clothesID, sort);
        return new OkResponse(rateClothesViewModels);
    }

    /**********************Clothes********************/
    @ApiOperation(value = "api thêm 1 danh mục sản phẩm", response = Iterable.class)
    @PutMapping("/categorys")
    public Response insertCategory(@RequestBody String body) {
        Response response;
        try {
            Category category = new Category();
            category.setTitle(body);
            categoryRepository.save(category);
            response = new OkResponse();
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }

        return response;
    }

    @ApiOperation(value = "Lấy tất cả danh mục sản phẩm", response = Iterable.class)
    @GetMapping("/category")
    public Response getAllCategory() {
        return new OkResponse(categoryRepository.findAll());
    }


    /**********************Clothes********************/
    @ApiOperation(value = "Lấy toàn bộ sản phẩm quần áo", response = Iterable.class)
    @GetMapping("/clothes")
    public Response getAllClothes(
            @ApiParam(name = "pageIndex", value = "Index trang, mặc định là 0")
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @ApiParam(name = "pageSize", value = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_PAGE_SIZE)
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là " + Clothes.CREATED_DATE)
            @RequestParam(value = "sortBy", defaultValue = Clothes.CREATED_DATE) String sortBy,
            @ApiParam(name = "sortType", value = "Nhận (asc | desc), mặc định là desc")
            @RequestParam(value = "sortType", defaultValue = "desc") String sortType) {
        Response response;

        try {
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<ClothesPreview> clothesPreviews = clothesRepository.getAllClothesPreviews(pageable);
            response = new OkResponse(clothesPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    @ApiOperation(value = "Lấy toàn bộ tên sản phẩm quần áo", response = Iterable.class)
    @GetMapping("/search/clothes")
    public Response getAllClothesName() {
        Response response;
        try {
            response = new OkResponse(clothesRepository.getAllClothesSearchPreviews());
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
    /**********************Clothes********************/
    @ApiOperation(value = "Lấy toàn bộ sản phẩm quần áo theo danh mục", response = Iterable.class)
    @GetMapping("/clothes/{id}/category")
    public Response getAllClothesCategory(@PathVariable("id") String categoryID,
                                          @ApiParam(name = "pageIndex", value = "Index trang, mặc định là 0")
                                          @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                          @ApiParam(name = "pageSize", value = "Kích thước trang, mặc đinh và tối đa là " + Constant.MAX_PAGE_SIZE)
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                          @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là " + Clothes.CREATED_DATE)
                                          @RequestParam(value = "sortBy", defaultValue = Clothes.CREATED_DATE) String sortBy,
                                          @ApiParam(name = "sortType", value = "Nhận (asc | desc), mặc định là desc")
                                          @RequestParam(value = "sortType", defaultValue = "desc") String sortType) {
        Response response;

        try {
            Category category = categoryRepository.findOne(categoryID);
            if (category == null) {
                return new NotFoundResponse("Category not Exist");
            }
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<ClothesPreview> clothesPreviews = clothesRepository.getAllClothesPreviewsByCategory(pageable, categoryID);
            response = new OkResponse(clothesPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /***************** Lay chi tiet clothes********************/
    @ApiOperation(value = "Lấy chi tiết quần áo", response = Iterable.class)
    @GetMapping("/clothes/{id}")
    public Response getDetailClothes(@PathVariable("id") String clothesID) {
        Response response;
        try {
            Clothes clothes = clothesRepository.findById(clothesID);
            if (clothes == null) {
                return new NotFoundResponse("Clothes not Exist");
            }
            ClothesViewModel clothesViewModel = clothesRepository.getClothesViewModel(clothesID);
            Sort sort =
                    PageAndSortRequestBuilder.createSortRequest(Rating.RATE_DATE, "desc");
            clothesViewModel.setRateClothesViewModels(rateClothesRepository.getAllRate(clothesID, sort));
            response = new OkResponse(clothesViewModel);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************similarClothes********************/
    @ApiOperation(value = "Lấy danh sách quần áo tưởng đương", response = Iterable.class)
    @GetMapping("/similarClothes/{id}")
    public Response getSimilarClothes(@PathVariable("id") String clothesID,
                                      @ApiParam(name = "pageIndex", value = "index trang, mặc định là 0")
                                      @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                      @ApiParam(name = "pageSize", value = "Kích thước trang, mặc định và tối đa là " + Constant.MAX_PAGE_SIZE)
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                      @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là " + Clothes.CREATED_DATE)
                                      @RequestParam(value = "sortBy", defaultValue = Clothes.CREATED_DATE) String sortBy,
                                      @ApiParam(name = "sortType", value = "Nhận (asc | desc), mặc định là desc")
                                      @RequestParam(value = "sortType", defaultValue = "desc") String sortType) {
        Response response;
        try {
            Clothes clothes = clothesRepository.findOne(clothesID);
            if (clothes == null) {
                return new NotFoundResponse("Clothes not Exist");
            }
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<ClothesPreview> clothesPreviews = clothesRepository.getSimilarClothesPreviews(pageable, clothes.getCategory().getId(), clothesID);
            response = new OkResponse(clothesPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************recommendClothes********************/
    @ApiOperation(value = "Lấy danh sách sản phẩm gợi ý", response = Iterable.class)
    @GetMapping("/recommendClothes/{id}")
    public Response getClothesRecommend(@PathVariable("id") String clothesID,
                                        @ApiParam(name = "pageIndex", value = "index trang, mặc định là 0")
                                        @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                        @ApiParam(name = "pageSize", value = "Kích thước trang, mặc định và tối đa là " + Constant.MAX_PAGE_SIZE)
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @ApiParam(name = "sortBy", value = "Trường cần sort, mặc định là " + ClothesRecommend.PRIORITY)
                                        @RequestParam(value = "sortBy", defaultValue = ClothesRecommend.PRIORITY) String sortBy,
                                        @ApiParam(name = "sortType", value = "Nhận (asc | desc), mặc định là desc")
                                        @RequestParam(value = "sortType", defaultValue = "desc") String sortType) {
        Response response;
        try {
            Clothes clothes = clothesRepository.findOne(clothesID);
            if (clothes == null) {
                return new NotFoundResponse("Clothes not Exist");
            }
            Pageable pageable = PageAndSortRequestBuilder.createPageRequest(pageIndex, pageSize, sortBy, sortType, Constant.MAX_PAGE_SIZE);
            Page<ClothesPreview> clothesPreviews = recommendClothesRepository.getRecommendClothesPreviews(pageable, clothes.getId());
            response = new OkResponse(clothesPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }

    /**********************Advertisement********************/
    @ApiOperation(value = "Lấy danh sách quảng cáo", response = Iterable.class)
    @GetMapping("/advertisement")
    public Response getAdvertisement() {
        Response response;
        try {
            response = new OkResponse(advertisementRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
}
