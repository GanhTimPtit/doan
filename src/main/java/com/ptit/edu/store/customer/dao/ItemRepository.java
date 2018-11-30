package com.ptit.edu.store.customer.dao;

import com.ptit.edu.store.customer.models.data.Item;
import com.ptit.edu.store.customer.models.view.HeaderProfile;
import com.ptit.edu.store.customer.models.view.ItemPreview;
import com.ptit.edu.store.product.models.view.ClothesPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item,String> {
    @Query("select new com.ptit.edu.store.customer.models.view.ItemPreview( c.id," +
            " c.logoUrl, c.name, o.color, o.size, o.amount, o.price) "+
            "from Item o join o.orderCustomer oc join o.clothes c join oc.customer occ where oc.id = ?1 and occ.id = ?2")
    List<ItemPreview> getItemPreview(String oderID, String customerID);

    @Query("select new com.ptit.edu.store.customer.models.view.ItemPreview(c.id," +
            " c.logoUrl, c.name, o.color, o.size, o.amount, o.price) "+
            "from Item o join o.orderCustomer oc join o.clothes c where oc.id = ?1")
    List<ItemPreview> getItemPreview(String oderID);



}
