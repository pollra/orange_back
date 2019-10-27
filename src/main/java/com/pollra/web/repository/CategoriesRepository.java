package com.pollra.web.repository;

import com.pollra.web.categories.domain.CategoriesDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesDAO, Long> {
    // Create Category
    // save method


    // update category
    @Modifying
    @Query(value = "UPDATE categories SET name = :ca_name WHERE num = :ca_num", nativeQuery = true)
    int updateOneByCategoryToNum(@Param("ca_name") String name, @Param("ca_num") Long num);

    // delete
    // remove method


    // Read List
    List<CategoriesDAO> findAllByOwner(String owner);
    int countByNum(Long num);

}
