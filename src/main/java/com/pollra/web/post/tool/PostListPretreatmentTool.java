package com.pollra.web.post.tool;

import com.pollra.web.post.domain.PL_Range;
import com.pollra.web.post.domain.PostList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class PostListPretreatmentTool {
    private HttpServletRequest request;

    // constant
    private final String CATEGORY = "";

    public PostListPretreatmentTool(HttpServletRequest request) {
        this.request = request;
    }


    public PostList getPostList(PL_Range range){
        PostList list = new PostList();
        switch (range){
            case CATEGORY:
                return this._getCategory();
            default:
                log.warn("[!] 구현되지 않은 범위의 값입니다:{}",range);
        }
        return null;
    }

    /**
     * Null 체크
     * 대상 객체가 null 일 경우 true
     * @return
     */
    public boolean isNull(PostList postList){
        return _isNull_all(postList);
    }

    public boolean isNull_array(List<PostList> lists){
        return _isNull_array(lists);
    }

    private boolean _isNull_array(List<PostList> postList){
//        log.warn("postList == null : {} / postList.size() <= 0 : {}",(postList == null),(postList.size() <= 0));
//        log.warn("postList: {}",postList.toString());
        if(postList == null || postList.size() <= 0) return true;
        return false;
    }

    private boolean _isNull_all(PostList postList){
        if(StringUtils.isEmpty(postList.getTitle())) return true;
        if(StringUtils.isEmpty(postList.getCategory())) return true;
        if(StringUtils.isEmpty(postList.getOwner())) return true;
        return false;
    }

    private PostList _getCategory(){
        PostList list = new PostList();
        list.setCategory((String)this.get(CATEGORY));
        return list;
    }

    private Object get(String name){
        return request.getParameter(name);
    }
}
