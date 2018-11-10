package com.ptit.edu.store.product.dao;

import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.product.models.view.ClothesPreview;
import com.ptit.edu.store.product.models.view.ClothesViewModel;
import com.ptit.edu.store.product.models.view.RateClothesViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ClothesRepository extends JpaRepository<Clothes, String>{
    Clothes findById(String clothesID);
    List<Clothes> findAllByCategory_Gender(int gender);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(c.id, c.name, c.price, cc.title," +
            "c.logoUrl, c.totalSave, sum(cr.rating), count(cr.rating)) " +
            "from Clothes c join c.category cc left join c.rateClothes cr " +
            "GROUP BY c.id")
    Page<ClothesPreview> getAllClothesPreviews(Pageable pageable);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(c.id, c.name, c.price, cc.title, " +
            "c.logoUrl, c.totalSave, sum(cr.rating) , count(cr.rating)) " +
            "from Clothes c join c.category cc left join c.rateClothes cr " +
            "where cc.id= ?1 " +
            "GROUP BY c.id")
    Page<ClothesPreview> getAllClothesPreviewsByCategory(Pageable pageable,String categoryID);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(c.id, c.name, c.price, cc.title, " +
            "c.logoUrl, c.totalSave, sum(cr.rating), count(cr.rating)) " +
            "from Clothes c join c.category cc left join c.rateClothes cr " +
            "where cc.id = ?1 and c.id <> ?2 "+
            "GROUP BY c.id")
    Page<ClothesPreview> getSimilarClothesPreviews(Pageable pageable, String categoryID, String clothesID);



    @Query("select new com.ptit.edu.store.product.models.view.ClothesViewModel(c.id, c.name, c.price,c.description," +
            "c.logoUrl, cc, c.totalSave) " +
            "from Clothes c join c.category cc " +
            "where c.id = ?1 ")
    ClothesViewModel getClothesViewModel(String clothesID);

    @Query("select c.id from Clothes c")
    List<String> findAllID();
}
