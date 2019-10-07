package com.pollra.web.blog.controller;

import com.pollra.aop.jwt.anno.AdminCertification;
import com.pollra.aop.jwt.anno.TokenCertification;
import com.pollra.aop.jwt.anno.TokenCredential;
import com.pollra.response.ApiDataDetail;
import com.pollra.web.blog.domain.BlogInfo;
import com.pollra.web.blog.domain.BlogInfoVo;
import com.pollra.web.blog.domain.en.BI_Range;
import com.pollra.web.blog.domain.en.BlogUpdateRange;
import com.pollra.web.blog.exception.BlogDataNotFoundException;
import com.pollra.web.blog.exception.BlogServiceException;
import com.pollra.web.blog.exception.NoSuchBlogInfoException;
import com.pollra.web.blog.exception.SelectionNotFoundException;
import com.pollra.web.blog.service.BlogInfoDataTool;
import com.pollra.web.blog.service.BlogInfoService;
import com.pollra.web.post.domain.PostDataVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api/blog")
public class BlogController {
    private BlogInfoService blogService;
    private HttpServletRequest request;
    private BlogInfoDataTool dataTool;

    public BlogController(BlogInfoService blogService, HttpServletRequest request, BlogInfoDataTool dataTool) {
        this.blogService = blogService;
        this.request = request;
        this.dataTool = dataTool;
    }

    @GetMapping
    public ResponseEntity<?> getBlogInfo(){
        BlogInfo blogInfo = new BlogInfo();
//        InfoPrint.headersPrint(request);
        blogInfo = blogService.getBlogInfoOfId("pollra");
        return new ResponseEntity<>(new BlogInfoVo(blogInfo), HttpStatus.OK);
    }

    /**
     * 블로그의 메타 타이틀을 수정
     * meta_title
     * @return
     */
    @AdminCertification
    @PutMapping
    @Transactional
    public ResponseEntity<?> updateMetaBlogInfo() {
        try{
            log.info("updateMetaBlogInfo");
            int valueCount = 0;
            log.info("valueCount 선언 완료");
            // 에러 확인 - 익셉션 핸들러 도입하면 사라질 코드
            if (request.getAttribute("error") != null) {
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(request.getAttribute("error").toString()), HttpStatus.BAD_REQUEST);
            }
            // 데이터를 받음 - 익셉션 핸들러 도입하면 사라질 코드
            BlogInfo info = dataTool.getBlogInfo();
            log.info("데이터 받기 완료");
            for (Map.Entry<BlogUpdateRange, String> item : dataTool.resultMap(info).entrySet()) {
                if (item.getKey().equals(BlogUpdateRange.ID)) {
                    if(item.getValue() == null) valueCount = -10;
                    valueCount++;
                } else if (item.getValue() != null) valueCount++;
            }
            log.info("데이터 확인 완료");

            if (valueCount < 2) {
                log.error("데이터를 확인할 수 없습니다.");
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터를 확인할 수 없습니다."), HttpStatus.BAD_REQUEST);
            }
            // 서비스 실행
            try {
                log.info("blogService.updateBlogInfo({})", info.toString());
                blogService.updateBlogInfo(info);
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터 변경 성공"), HttpStatus.OK);
            } catch (SelectionNotFoundException e) {
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
            } catch (BlogDataNotFoundException e) {
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
            } catch (BlogServiceException e) {
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Throwable e){
            log.error("알 수 없는 에러 발생 : {}", e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("알 수 없는 에러가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
