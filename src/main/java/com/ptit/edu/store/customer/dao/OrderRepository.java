package com.ptit.edu.store.customer.dao;

import com.ptit.edu.store.customer.models.data.OrderCustomer;
import com.ptit.edu.store.customer.models.view.OrderPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderCustomer,String> {
    @Query("select new com.ptit.edu.store.customer.models.view.OrderPreview(o)" +
            " from OrderCustomer o where o.customer.id= ?1")
    Page<OrderPreview> getAllOrderPreview( Pageable pageable, String customerID);


}
