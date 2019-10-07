package com.pollra.web.blog.service;

import com.pollra.web.blog.domain.BlogInfo;
import com.pollra.web.blog.domain.BlogInfoVo;
import com.pollra.web.blog.domain.en.BI_Range;
import com.pollra.web.blog.domain.en.BlogUpdateRange;
import com.pollra.web.blog.exception.SelectionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BlogInfoDataTool {
    private final String BLOG_ID = "id";
    private final String BLOG_TITLE = "title";
    private final String BLOG_EX = "ex";
    private final String BLOG_IMG = "img";
    private final String BLOG_META_TITLE = "meta-title";

    private HttpServletRequest request;

    public BlogInfoDataTool(HttpServletRequest request) {
        this.request = request;
    }

    // 블로그 변경에 필요한 데이터를 가져옴
    public BlogInfo getBlogInfo(){
        return _getBlogInfo();
    }

    // 데이터가 존재하는지 판단
    // null 이면 true
    public boolean isNull(BlogUpdateRange range, BlogInfo info) throws SelectionNotFoundException {
        if(info.getId().isEmpty()) return true;
        switch (range){
            case TITLE:
                if(info.getTitle().isEmpty()) return true;
                return false;
            case IMG_PATH:
                if(info.getImgPath().isEmpty()) return true;
                return false;
            case EXPLANATION:
                if(info.getExplanation().isEmpty()) return true;
                return false;
            case META_TITLE:
                if(info.getMetaTitle().isEmpty()) return true;
                return false;
            case TITLE_IMG_PATH:
                if(info.getTitle().isEmpty()) return true;
                if(info.getImgPath().isEmpty()) return true;
                return false;
            case TITLE_EXPLANATION:
                if(info.getTitle().isEmpty()) return true;
                if(info.getExplanation().isEmpty()) return true;
                return false;
            case IMG_PATH_EXPLANATION:
                if(info.getImgPath().isEmpty()) return true;
                if(info.getExplanation().isEmpty()) return true;
                return false;
            case TITLE_IMG_PATH_EXPLANATION:
                if(info.getTitle().isEmpty()) return true;
                if(info.getExplanation().isEmpty()) return true;
                if(info.getImgPath().isEmpty()) return true;
                return false;
            default:
                log.error("{} 존재하지 않는 선택지", range);
                throw new SelectionNotFoundException("존재하지 않는 선택지입니다.");
        }
    }

    /**
     * 배열로 리턴
     * @param info
     * @return id, title, explanation, imgPath, metaTitle
     */
    public Map<BlogUpdateRange, String> resultMap(BlogInfo info){
        log.info("resultMap start: {}", info.toString());
        String[] arrValue = new String[]{info.getId(), info.getTitle(), info.getExplanation(), info.getImgPath(), info.getMetaTitle()};
        log.info("resultMap arrValue 선언완료");
        Map<BlogUpdateRange, String> resultMap = new HashMap<>();
        log.info("resultMap resultMap 생성완료");
        for(int i=0; i<arrValue.length; i++){
            resultMap.put(BlogUpdateRange.values()[i],arrValue[i]);
        }
        log.info("resultMap resultMap 초기화 완료: {}", resultMap.toString());

        return resultMap;
    }

    /**======== 내부 함수 ==================================*/
    // TEI : Title Explanation ImgPath
    private BlogInfo _getBlogInfo(){
        BlogInfo info = new BlogInfo();
        info.setId(get(BLOG_ID));
        info.setTitle(get(BLOG_TITLE));
        info.setExplanation(get(BLOG_EX));
        info.setImgPath(get(BLOG_IMG));
        info.setMetaTitle(get(BLOG_META_TITLE));
        return info;
    }

    private String get(String target){
        try {
            return request.getParameter(target);
        }catch (Throwable e){
            log.warn("[{}] 데이터를 찾을 수 없습니다.",target);
            return "";
        }
    }

}
