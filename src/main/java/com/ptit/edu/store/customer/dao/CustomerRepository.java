package com.ptit.edu.store.customer.dao;

import com.ptit.edu.store.customer.models.data.Customer;
import com.ptit.edu.store.customer.models.view.HeaderProfile;
import com.ptit.edu.store.customer.models.view.Profile;
import com.ptit.edu.store.customer.models.view.CustomerStatictisPreView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,String> {
    Customer findByUser_Id(String userID);
    List<Customer> findAllByGender(int gender);

    @Query("select new com.ptit.edu.store.customer.models.view.CustomerStatictisPreView(" +
            "c.id,"+
            "c.firstName,"+
            "c.lastName," +
            "count(co.totalPrice)," +
            "sum(co.totalPrice)"+
            ") from Customer c left join c.orderCustomers co " +
            "GROUP BY c.id")
    List<CustomerStatictisPreView> getCustomerStatictis();



    @Query("select new com.ptit.edu.store.customer.models.view.HeaderProfile(" +
            "c.firstName,"+
            "c.lastName," +
            "c.id,"+
            "c.avatarUrl," +
            "c.email,"+
            "c.phone"+
            ") from Customer c where c.id = ?1")
    HeaderProfile getHeaderProfile(String id);

    @Query("select new com.ptit.edu.store.customer.models.view.Profile(c)" +
            "from Customer  c where c.id = ?1")
    Profile getProfile(String customerID);

    @Transactional
    @Modifying
    @Query("update Customer c set c.description = ?2 where c.id = ?1")
    void updateDescription(String customerID, String description);

    @Query("select c.id from Customer c")
    List<String> findAllID();
}
