package com.ptit.edu.store.product.dao;

import com.ptit.edu.store.product.models.data.RateClothes;
import com.ptit.edu.store.product.models.view.RateClothesViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RateClothesRepository extends JpaRepository<RateClothes, String>{
    boolean existsByCustomerIdAndClothesId(String customerID, String clothesID);

    @Query("select new com.ptit.edu.store.product.models.view.RateClothesViewModel(cs.firstName, cs.lastName," +
            "cs.avatarUrl, r.rateDate, r.message, r.rating) " +
            "from RateClothes r join r.clothes c join r.customer cs where c.id = ?1")
    List<RateClothesViewModel> getAllRate(String clothesID, Sort pageable);

    RateClothes findByClothes_IdAndCustomer_Id(String clothesID,String customerID);
//    @Transactional
//    @Modifying
//    @Query("update RateClothes r set r.message = ?1, r.rating = ?2,r.rateDate = ?3 where r.")
//    void updateRateClothes(String message, int rating);
}
