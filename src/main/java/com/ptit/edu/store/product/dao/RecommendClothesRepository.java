package com.ptit.edu.store.product.dao;

import com.ptit.edu.store.product.models.data.RecommendClothes;
import com.ptit.edu.store.product.models.view.ClothesPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecommendClothesRepository extends JpaRepository<RecommendClothes,String> {
    @Query("select new com.ptit.edu.store.product.models.view.ClothesPreview(rc.clothesRecommend) " +
            " from RecommendClothes rc where rc.clothes.id= ?1")
    Page<ClothesPreview> getRecommendClothesPreviews(Pageable pageable, String id);
}
