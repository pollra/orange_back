package com.pollra.web.post.tool;

import com.pollra.web.blog.exception.SelectionNotFoundException;
import com.pollra.web.post.domain.PostInfo;
import com.pollra.web.post.domain.en.PI_getRange;
import com.pollra.web.post.domain.en.Pi_NullRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class PostInfoTool {
    private final String INFO_DATE = "";
    private final String INFO_IMG = "";
    private final String INFO_OWNER = "";
    private final String INFO_URI = "";
    private final String INFO_CATEGORY = "category";

    private HttpServletRequest request;

    public PostInfoTool(HttpServletRequest request) {
        this.request = request;
    }


    public PostInfo getPostInfo(PI_getRange range){
        switch (range){
            case CATEGORY:
                PostInfo result = new PostInfo();
                result.setCategory(get(INFO_CATEGORY));
                return result;
            default:
                log.error("getPostInfo 존재하지 않는 선택지({}) 입니다.", range);
                return null;
        }
    }

    public boolean isNull(Pi_NullRange range, PostInfo info){
        switch (range){
            case UPDATE:
                return isNull_update(info);
            default:
                log.error("isNull(postInfo) 존재하지 않는 선택지({}) 입니다.", range);
        }
        return true;
    }

    private boolean isNull_update(PostInfo info){
        if(info.getCategory().equals("")) return true;
        return false;
    }

    private String get(String target){
        String result = request.getParameter(target);
        if(result == null || result.equals("")){
            log.warn("{} 정보의 데이터를 가져올 수 없습니다.", target);
            return "";
        }
        return result;
    }
}
