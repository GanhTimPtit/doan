package com.ptit.edu.store.product.dao;

import com.ptit.edu.store.product.models.data.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {
}
