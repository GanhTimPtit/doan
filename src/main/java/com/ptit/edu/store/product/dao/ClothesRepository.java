package com.ptit.edu.store.product.dao;

import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.product.models.view.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;


public interface ClothesRepository extends JpaRepository<Clothes, String>{
    Clothes findById(String clothesID);
    List<Clothes> findAllByCategory_Gender(int gender);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(c.id, c.name, c.price, cc.title," +
            "c.logoUrl, c.totalSave, count(cr.value), sum(cr.value)) " +
            "from Clothes c join c.category cc left join c.ratings cr " +
            "GROUP BY c.id")
    Page<ClothesPreview> getAllClothesPreviews(Pageable pageable);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesSearchPreview(c.id, c.name) " +
            "from Clothes c")
    List<ClothesSearchPreview> getAllClothesSearchPreviews();

    @Query("select new com.ptit.edu.store.product.models.view.ClothesStatictisPreview(c.id, c.name, c.price, " +
            "c.logoUrl, sum(i.amount) ) " +
            "from Clothes c join c.items i join i.orderCustomer io " +
            "where io.status=1 and io.createdDate > ?1 and io.createdDate < ?2 " +
            "GROUP BY c.id " +
            "order by sum(i.amount) desc")
    Page<ClothesStatictisPreview> getAllClothesStatictisHotPreviews(Pageable pageable, Timestamp date1, Timestamp date2);

    @Query("select c.id " +
            "from Clothes c join c.items i join i.orderCustomer io " +
            "where io.status=1 and io.createdDate > ?1 and io.createdDate < ?2 ")
    List<String> getAllClothesIDStatictisHot(Timestamp date1, Timestamp date2);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesStatictisPreview(c.id, c.name, c.price, " +
            "c.logoUrl) " +
            "from Clothes c " +
            "where c.id not in ?1")
    Page<ClothesStatictisPreview> getAllClothesStatictisNotHotPreviews(Pageable pageable,List<String> listClothesID);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(c.id, c.name, c.price, cc.title, " +
            "c.logoUrl, c.totalSave, count(cr.value), sum(cr.value)) " +
            "from Clothes c join c.category cc left join c.ratings cr " +
            "where cc.id= ?1 " +
            "GROUP BY c.id")
    Page<ClothesPreview> getAllClothesPreviewsByCategory(Pageable pageable,String categoryID);

    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(c.id, c.name, c.price, cc.title, " +
            "c.logoUrl, c.totalSave, count(cr.value), sum(cr.value)) " +
            "from Clothes c join c.category cc left join c.ratings cr " +
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


    @Transactional
    @Modifying
    @Query("update Clothes c set c.name = ?2, c.logoUrl = ?3, c.price = ?4, c.description = ?5 where c.id = ?1")
    void updateClothes(String clothesID, String name, String logoUrl, int price, String description);

}
