package com.pollra.web.repository;

import com.pollra.web.categories.domain.CategoriesDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesDAO, Long> {
    // Create Category
    // save method


    // update category
    // save method

    // delete
    // remove method

    // Read List
    List<CategoriesDAO> findAllByOwner(String owner);
}
