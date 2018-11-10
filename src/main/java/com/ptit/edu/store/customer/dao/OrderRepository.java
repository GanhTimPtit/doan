package com.ptit.edu.store.customer.dao;

import com.ptit.edu.store.customer.models.data.OrderCustomer;
import com.ptit.edu.store.customer.models.view.ConfirmOrderPreview;
import com.ptit.edu.store.customer.models.view.OrderPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderCustomer, String> {
    @Query("select new com.ptit.edu.store.customer.models.view.OrderPreview(o)" +
            " from OrderCustomer o join o.customer cs " +
            "where cs.id= ?1 and o.status=1")
    Page<OrderPreview> getAllOrderPreview(String customerID, Pageable pageable);

    @Query("select new com.ptit.edu.store.customer.models.view.ConfirmOrderPreview(o.id, o.createdDate, o.nameCustomer,o.phone, " +
            "o.location, o.totalPrice, cs.avatarUrl)" +
            " from OrderCustomer o join o.customer cs " +
            "where o.status=0")
    Page<ConfirmOrderPreview> getAllConfirmOrderPreview(Pageable pageable);


    @Modifying
    @Transactional
    @Query("update OrderCustomer oc set oc.status = 1 where oc.id in ?1")
    void updateBillStatus(Set<String> billIDs);
}
