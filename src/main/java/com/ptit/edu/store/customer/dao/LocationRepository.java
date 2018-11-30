package com.ptit.edu.store.customer.dao;

import com.ptit.edu.store.customer.models.data.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,String> {
}
