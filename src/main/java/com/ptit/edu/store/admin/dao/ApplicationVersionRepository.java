package com.ptit.edu.store.admin.dao;

import com.ptit.edu.store.admin.models.data.ApplicationVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationVersionRepository extends JpaRepository<ApplicationVersion,String> {
}
