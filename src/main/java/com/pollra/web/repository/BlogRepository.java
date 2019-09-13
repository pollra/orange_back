package com.pollra.web.repository;

import com.pollra.web.blog.domain.BlogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogInfo, String> {
    // Create
    // save method

    // Update
    // save method

    // Delete
    // remove method

    // Read
    @Override
    BlogInfo getOne(String id);
    Optional<BlogInfo> findById(String id);


}
