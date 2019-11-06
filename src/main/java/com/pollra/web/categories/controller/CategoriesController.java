package com.pollra.web.categories.controller;

import com.pollra.aop.jwt.anno.AdminCertification;
import com.pollra.response.ApiDataDetail;
import com.pollra.web.categories.domain.CategoriesDAO;
import com.pollra.web.categories.domain.en.CateFunc;
import com.pollra.web.categories.domain.en.CateRange;
import com.pollra.web.categories.exception.CategoriesDeleteException;
import com.pollra.web.categories.exception.CategoriesInsertException;
import com.pollra.web.categories.exception.CategoriesNullPointerException;
import com.pollra.web.categories.exception.CategoriesServiceException;
import com.pollra.web.categories.service.CategoriesService;
import com.pollra.web.categories.tool.CategoriesDataTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class CategoriesController {
    private CategoriesService categoriesService;
    private CategoriesDataTool tool;

    private HttpServletRequest request;

    public CategoriesController(CategoriesService categoriesService, CategoriesDataTool tool, HttpServletRequest request) {
        this.categoriesService = categoriesService;
        this.tool = tool;
        this.request = request;
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

    /**
     * CREATE
     *
     * @return
     */
    @AdminCertification
    @PostMapping("category")
    public ResponseEntity<?> create(){
        // admin 확인
        ResponseEntity<?> x = getResponseEntity();
        if (x != null) return x;

        // 데이터 받음 : name, owner
        CategoriesDAO dao = tool.getCategory(CateFunc.C);

        // 데이터 체크
        try {
            // 널체크
            if(dao.getOwner().equals("")){
                log.error("로그인 정보를 확인할 수 없습니다[data: {}, loginUser: {}]", dao.getOwner(), request.getAttribute("jwt-user").toString());
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("로그인된 정보를 확인할 수 없습니다."), HttpStatus.BAD_REQUEST);
            }

            if (tool.isNull(CateFunc.C, dao)) {
                log.warn("입력되지 않은 데이터가 존재합니다[{}]", dao == null ? "dao == null" : dao.toString());
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터를 다시 확인해 주세요"), HttpStatus.BAD_REQUEST);
            }

            // 입력된 owner 유효성 검사
            if (!request.getAttribute("jwt-user").equals(dao.getOwner())) {
                log.error("카테고리 등록 데이터가 정상적이지 않습니다[loginUser:{}, inputData:{}]", request.getAttribute("jwt-user"), dao.getOwner());
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("카테고리 등록 데이터가 정상적이지 않습니다."), HttpStatus.BAD_REQUEST);
            }
        }catch (Throwable e){
            log.error("데이터 유효성 검사를 하는 도중 에러가 발생했습니다.");
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터 인증 실패. "), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 데이터 추가
        try {
            categoriesService.create(dao);
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("ok"), HttpStatus.OK);
        }catch (CategoriesInsertException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * UPDATE
     *
     * @return
     */
    @AdminCertification
    @PutMapping("category")
    public ResponseEntity<?> update(){
        // admin 확인
        ResponseEntity<?> x = getResponseEntity();
        if (x != null) return x;

        // 데이터 받음
        CategoriesDAO dao = tool.getCategory(CateFunc.U);

        // 데이터 체크
        try {
            // 널체크
            if (tool.isNull(CateFunc.U, dao)) {
                log.warn("입력되지 않은 데이터가 존재합니다[{}]", dao == null ? "dao == null" : dao.toString());
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터를 다시 확인해 주세요"), HttpStatus.BAD_REQUEST);
            }
        }catch (Throwable e){
            log.error("데이터 유효성 검사를 하는 도중 에러가 발생했습니다.");
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터 인증 실패"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 데이터 업데이트
        try {
            categoriesService.updateCategory(dao);
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("ok"), HttpStatus.OK);
        } catch (CategoriesInsertException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CategoriesNullPointerException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CategoriesServiceException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @AdminCertification
    @DeleteMapping("category")
    public ResponseEntity<?> delete(){
        // admin 확인
        ResponseEntity<?> x = getResponseEntity();
        if (x != null) return x;

        // 데이터 받음
        CategoriesDAO dao = tool.getCategory(CateFunc.D);

        // 데이터 체크
        try {
            // 널체크
            if (tool.isNull(CateFunc.D, dao)) {
                log.warn("입력되지 않은 데이터가 존재합니다[{}]", dao == null ? "dao == null" : dao.toString());
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터를 다시 확인해 주세요"), HttpStatus.BAD_REQUEST);
            }
        }catch (Throwable e){
            log.error("데이터 유효성 검사를 하는 도중 에러가 발생했습니다.");
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터 인증 실패"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 데이터 업데이트
        try {
            categoriesService.delete(dao);
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("ok"), HttpStatus.OK);
        } catch (CategoriesDeleteException e){
            log.error(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CategoriesNullPointerException e){
            log.error(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CategoriesServiceException e){
            log.error(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /* 권한 확인
    ResponseEntity<?> x = getResponseEntity();
    if (x != null) return x;
     */
    private ResponseEntity<?> getResponseEntity() {
        try{
            if (request.getAttribute("error") != null && !(request.getAttribute("error").toString().isEmpty())) {
                log.error(request.getAttribute("error").toString());
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(request.getAttribute("error").toString()), HttpStatus.BAD_REQUEST);
            }
        }catch (Throwable e){
            log.error(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("인증 과정 오류발생"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
