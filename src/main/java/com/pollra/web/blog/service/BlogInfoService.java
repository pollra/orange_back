package com.pollra.web.blog.service;

import com.pollra.web.blog.domain.BlogInfo;
import com.pollra.web.blog.exception.NoSuchBlogInfoException;
import com.pollra.web.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BlogInfoService {
    private BlogRepository blogRepository;

    public BlogInfoService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    /**
     * Read method
     */
    public BlogInfo getBlogInfoOfId(String target){
        log.info("BlogInfoService - getBlogInfoOfId : start");
        // 아이디를 받고 그걸로 블로그의 정보를 가져옴.
        Optional<BlogInfo> blogInfo = Optional.of(new BlogInfo());
        blogInfo = blogRepository.findById(target);
        BlogInfo result = new BlogInfo();
        try {
            result = blogInfo.get();
        }catch (NoSuchBlogInfoException e){
            log.info("BlogInfo 데이터가 존재하지 않습니다.");
            log.info(e.getMessage());
        }

        return result;
    }

    /**
     * Create method
     */

    /**
     *  createOneBlogInfo
     *  계정 존재 여부를 확인하고 블로그 정보를 만듬.
     *
     * @param blogInfo
     * @return
     *      0 : 블로그 정보 생성 성공
     *      1 : 블로그 정보가 이미 존재함
     *      2 : 계정이 존재하지 않음
     */
    public int createOneBlogInfo(BlogInfo blogInfo){
        // 블로그 소유자의 아이디가 존재하는지 확인
        if(blogInfo.getId() == ""){
            return 2;
        }
        // 소유자의 존재가 확인됨.

        // 해당 소유자가 블로그를 이미 가지고있지는 않은지 체크
        if(isNotNull_blogInfo(getBlogInfoOfId(blogInfo.getId()))){
            // 이미 블로그가 존재하므로 에러코드 1 리턴
            return 1;
        }else{
            // 블로그가 존재하지 않음
            // 블로그 정보를 하나 생성
            blogRepository.save(blogInfo);
            return 0;
        }
    }

    /**
     * other method
     */
    /*
    * blogInfo 가 입력 가능한 정보인지 체크하는 메소드
    * 블로그 소유자의 아이디가 존재하는것이 확인 된 상태.
    * 즉, 데이터의 정합성 유무만 판단하면 됨.
    * false : 입력 불가 정보
    * true  : 입력 가능 정보
    */
    private boolean isNotNull_blogInfo(BlogInfo blogInfo){
        // id, title, explanation 이 널인지 체크
        if(blogInfo.getId() == "") return false;
        if(blogInfo.getTitle() == "") return false;
        if(blogInfo.getExplanation() == "") return false;
        return true;
    }

}
