package com.example.myproject.services;

import com.example.myproject.data.Review;
import com.example.myproject.data.ReviewRepo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepo repository;

    public ReviewService(ReviewRepo repository) {
        this.repository = repository;
    }

    public Optional<Review> get(Long id) {
        return repository.findById(id);
    }

    public Review update(Review entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Review> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Review> list(Pageable pageable, Specification<Review> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
