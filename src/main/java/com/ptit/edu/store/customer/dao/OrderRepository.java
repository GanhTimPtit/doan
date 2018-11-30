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

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderCustomer, String> {
    @Query("select new com.ptit.edu.store.customer.models.view.OrderPreview(o.id, o.createdDate, o.nameCustomer,o.phone, l.name,"+
            "o.payments, o.totalPrice, o.status)" +
            " from OrderCustomer o join o.customer cs join o.location l " +
            "where cs.id= ?1 and o.status=?2")
    Page<OrderPreview> getPageOrderPreview(String customerID,int status, Pageable pageable);

    @Query("select new com.ptit.edu.store.customer.models.view.OrderPreview(o.id, o.createdDate, o.nameCustomer,o.phone, l.name,"+
            "o.payments, o.totalPrice, o.status)" +
            " from OrderCustomer o join o.customer cs join o.location l " +
            "where cs.id= ?1 and o.status=1 or o.status=2 or o.status=3")
    Page<OrderPreview> getPageOrderPreview(String customerID, Pageable pageable);

    @Query("select sum(o.totalPrice)" +
            " from OrderCustomer o " +
            "where o.status=1 and o.createdDate > ?1 and o.createdDate < ?2 " +
            "GROUP BY o.status")
    Long sumCostOrderPreview(Timestamp date1, Timestamp date2);

    @Query("select new com.ptit.edu.store.customer.models.view.ConfirmOrderPreview(o.id, o.createdDate, o.nameCustomer,o.phone, l.name, " +
            "l.lat, l.log, o.totalPrice, cs.avatarUrl, o.status, cs.id)" +
            " from OrderCustomer o join o.customer cs join o.location l " +
            "where o.status=?1")
    Page<ConfirmOrderPreview> getAllConfirmOrderPreview(int status, Pageable pageable);


    @Modifying
    @Transactional
    @Query("update OrderCustomer oc set oc.status = ?1 where oc.status=?2 and oc.id in ?3")
    void updateBillStatus(int status,int statusWhere, Set<String> billIDs);

    @Modifying
    @Transactional
    @Query("update OrderCustomer oc set oc.status = ?1 where oc.status=?2 and oc.id = ?3")
    void deleteBillStatus(int status,int statusWhere, String billIDs);

    @Query("select cs.id" +
            " from OrderCustomer od join od.customer cs where od.id in ?1")
    List<String> getAllCustomerID(Set<String> billDIs);

    boolean existsByIdAndStatus(String orderID, int status);
}
