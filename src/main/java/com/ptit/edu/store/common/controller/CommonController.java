package com.ptit.edu.store.common.controller;

import com.ptit.edu.store.constants.Constant;
import com.ptit.edu.store.product.dao.ClothesRepository;
import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.product.models.view.ClothesPreview;
import com.ptit.edu.store.response_model.NotFoundResponse;
import com.ptit.edu.store.response_model.OkResponse;
import com.ptit.edu.store.response_model.Response;
import com.ptit.edu.store.response_model.ServerErrorResponse;
import com.ptit.edu.store.utils.PageAndSortRequestBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commons")
@Api(value = "common-api", description = "Nhóm API Common, không yêu cầu access token")
@CrossOrigin(origins = "*")
public class CommonController {
    @Autowired
    private ClothesRepository clothesRepository;


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

    /***************** Lay chi tiet clothes********************/
    @ApiOperation(value = "Lấy chi tiết quần áo", response = Iterable.class)
    @GetMapping("/clothes/{id}")
    public Response getDetailClothes(@PathVariable("id") String clothesID) {
        Response response;
        try {
            response = new OkResponse(clothesRepository.findById(clothesID));
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
            Page<ClothesPreview> clothesPreviews = clothesRepository.getSimilarClothesPreviews(pageable, clothes.getCategory().getId());
            response = new OkResponse(clothesPreviews);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ServerErrorResponse();
        }
        return response;
    }
}
