package com.pollra.web.categories.controller;

import com.pollra.web.categories.domain.CategoriesDAO;
import com.pollra.web.categories.service.CategoriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class CategoriesController {
    private CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("public/categories/list")
    public ResponseEntity<?> getCategoriesList(){
        List<CategoriesDAO> categories = categoriesService.getCategoriesList("pollra");
        log.info("categories.size(): "+ categories.size());
        if(categories.size() >= 1){
            return new ResponseEntity<List<CategoriesDAO>>(categories, HttpStatus.OK);
        }
        return new ResponseEntity<Error>(
                new Error("데이터가 존재하지 않습니다."), HttpStatus.NOT_FOUND);
    }
}
