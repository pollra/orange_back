package com.pollra.web.categories.controller;

import com.pollra.response.ApiDataDetail;
import com.pollra.web.categories.domain.CategoriesDAO;
import com.pollra.web.categories.domain.en.CateFunc;
import com.pollra.web.categories.domain.en.CateRange;
import com.pollra.web.categories.exception.CategoriesInsertException;
import com.pollra.web.categories.exception.CategoriesNullPointerException;
import com.pollra.web.categories.service.CategoriesService;
import com.pollra.web.categories.tool.CategoriesDataTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class CategoriesController {
    private CategoriesService categoriesService;
    private CategoriesDataTool tool;

    public CategoriesController(CategoriesService categoriesService, CategoriesDataTool tool) {
        this.categoriesService = categoriesService;
        this.tool = tool;
    }

    @GetMapping("categories/value/list")
    public ResponseEntity<?> getCategoriesList(){
        List<CategoriesDAO> categories = categoriesService.getCategoriesList("pollra");
        log.info("categories.size(): "+ categories.size());
        if(categories.size() >= 1){
//            return new ResponseEntity<List<CategoriesDAO>>(categories, HttpStatus.OK);
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("ok", categories), HttpStatus.OK);
        }
        return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터가 존재하지 않습니다."), HttpStatus.NOT_FOUND);
    }
    @PostMapping("category")
    public ResponseEntity<?> create(){
        // 데이터 받음 : name, owner
        CategoriesDAO dao = tool.getCategory(CateRange.DATA);

        // 널체크
        if(tool.isNull(CateFunc.C, dao)){
            log.warn("입력되지 않은 데이터가 존재합니다[{}]", dao == null? "dao == null" : dao.toString());
//            throw new CategoriesNullPointerException("데이터를 다시 확인해주세요.");
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터를 다시 확인해 주세요"), HttpStatus.BAD_REQUEST);
        }

        // 데이터 추가
        try {
            categoriesService.create(dao);
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("ok"), HttpStatus.OK);
        }catch (CategoriesInsertException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("category")
    public ResponseEntity<?> update(){
        return null;
    }
}
