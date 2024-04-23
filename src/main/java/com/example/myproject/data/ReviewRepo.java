package com.example.myproject.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReviewRepo extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
}
