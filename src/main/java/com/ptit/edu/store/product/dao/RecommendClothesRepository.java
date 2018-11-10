package com.ptit.edu.store.product.dao;

import com.ptit.edu.store.product.models.data.RecommendClothes;
import com.ptit.edu.store.product.models.view.ClothesPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecommendClothesRepository extends JpaRepository<RecommendClothes,String> {
    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(rcc.id, rcc.name, rcc.price, cc.title, " +
            "rcc.logoUrl, rcc.totalSave, sum(cr.rating), count(1)) " +
            "from RecommendClothes rc join rc.clothes c join rc.clothesRecommend rcc " +
            "join rcc.category cc " +
            "left join rcc.rateClothes cr " +
            "where c.id= ?1 "+
            "GROUP BY rcc.id, c.id, rc.priority")
    Page<ClothesPreview> getRecommendClothesPreviews(Pageable pageable, String id);
}
