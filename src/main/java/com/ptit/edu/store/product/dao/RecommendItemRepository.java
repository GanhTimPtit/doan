package com.ptit.edu.store.product.dao;

import com.ptit.edu.store.product.models.data.RecommendItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendItemRepository extends JpaRepository<RecommendItem,String> {
}
