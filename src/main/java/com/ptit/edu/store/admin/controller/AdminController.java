package com.ptit.edu.store.admin.controller;

import com.ptit.edu.store.admin.dao.ApplicationVersionRepository;
import com.ptit.edu.store.admin.dao.StoreBranchRepository;
import com.ptit.edu.store.admin.models.data.QuanAo;
import com.ptit.edu.store.admin.models.data.StoreBranch;
import com.ptit.edu.store.admin.models.body.LatLngBody;
import com.ptit.edu.store.admin.models.body.StoreBranchBody;
import com.ptit.edu.store.admin.models.view.StoreBranchViewModel;
import com.ptit.edu.store.product.dao.CategoryRepository;
import com.ptit.edu.store.product.dao.ClothesRepository;
import com.ptit.edu.store.product.models.data.Clothes;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.print.Doc;
import javax.validation.constraints.Null;
import java.io.FileInputStream;
import java.io.InputStream;
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
    ApplicationVersionRepository applicationVersionRepository;

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
            Elements listSp= document.select("div.category-products").get(0).getElementsByTag("li");
            for(Element e:listSp){
                String url = e.select("div.category-product-list-item-view").get(0).getElementsByTag("a").attr("href");
                Document documentCon = Jsoup.connect(url).get();
                Element elementCon1 = documentCon.select("div.product-essential").get(0);
                String name=     elementCon1.select("div.product-name").get(0).getElementsByTag("h1").text();
                String price=     elementCon1.select("div.product-name").get(0).getElementsByTag("span").text();
                String src=     elementCon1.select("div.product-image-gallery").get(0).getElementsByTag("img").attr("src");
                String description=     elementCon1.select("div.product-detail-tabs").get(0).getElementsByTag("p").text();
                Clothes clothes= new Clothes();
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
    Response getVersionApp(){
        Response response;
        try {
            int version = applicationVersionRepository.findAll().get(0).getVersion();
            response = new OkResponse(version);
        }catch (Exception e){
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
                            clothes1.setPrice((int)((double) getCellValue(cell)));
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
                if (latLngBody != null) {
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
