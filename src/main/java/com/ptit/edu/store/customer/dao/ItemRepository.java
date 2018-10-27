package com.ptit.edu.store.customer.dao;

import com.ptit.edu.store.customer.models.data.Item;
import com.ptit.edu.store.customer.models.view.HeaderProfile;
import com.ptit.edu.store.customer.models.view.ItemPreview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,String> {
    @Query("select new com.ptit.edu.store.customer.models.view.ItemPreview( o"+
            ") from Item o where o.orderCustomer.id = ?1 and o.orderCustomer.customer.id= ?2")
    List<ItemPreview> getItemPreview(String oderID, String customerID);
}
