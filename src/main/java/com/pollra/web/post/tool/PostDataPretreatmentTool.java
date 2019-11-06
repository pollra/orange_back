package com.pollra.web.post.tool;

import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.domain.PostInfo;
import com.pollra.web.post.domain.PostList;
import com.pollra.web.post.domain.TargetPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class PostDataPretreatmentTool {
    private HttpServletRequest request;

    public PostDataPretreatmentTool(HttpServletRequest request) {
        this.request = request;
    }

    // post data
    private final String DATA_NUM = "num";
    private final String DATA_TITLE = "title";
    private final String DATA_CONTENT = "content";

    // post info
    private final String INFO_IMG_PATH = "imgPath";
    private final String INFO_CATEGORY = "category";

    public PostData getPostData() {
        PostData postData = new PostData();
        postData.setTitle(get(DATA_TITLE));
        postData.setPostContent(get(DATA_CONTENT));
        return postData;
    }

    public PostInfo getPostInfo() {
        PostInfo postInfo = new PostInfo();
        postInfo.setImgPath(get(INFO_IMG_PATH));
        postInfo.setCategory(get(INFO_CATEGORY).toLowerCase());
        return postInfo;
    }

    /**
     * 입력 객체가 null 일 경우 true 리턴
     * @return
     *  true : 입력 객체의 데이터중 null 이 존재합니다.
     *  false : 입력 객체가 정상적입니다.
     */
    public boolean isNull(TargetPost targetPost, Object o){
        switch (targetPost){
            case DATA:
                return isNull_postData((PostData) o);
            case INFO:
                return isNull_postInfo((PostInfo) o);
            case LIST:
                return isNull_postList((PostList) o);
            default:
                return false;
        }
    }

    private boolean isNull_postData(PostData postData){
        if(postData == null) return true;
        if(postData.getTitle() == "") return true;
        if(postData.getPostContent()=="") return true;
        return false;
    }
    private boolean isNull_postInfo(PostInfo postInfo){
        if(postInfo == null) return true;
        if(postInfo.getNum() == null && postInfo.getNum() < 0) return true;
        if(postInfo.getUri()=="") return true;
        if(postInfo.getDate() == "") return true;
        if(postInfo.getOwner()=="") return true;
        return false;
    }
    private boolean isNull_postList(PostList postList){
        if(postList.getOwner() == "") return true;
        if(postList.getDate() == "") return true;
        if(postList.getTitle() == "") return true;
        if(postList.getUri() == "") return true;
        return false;
    }

    private String get(String target){
        String result = "";
        try{
            result = request.getParameter(target);
            log.info("들어온 데이터: "+result);
            if(result == null) throw new NullPointerException(target+"데이터가 null 입니다.");
        }catch (NullPointerException e){
            log.error(target+"데이터를 확인할 수 없습니다.");
            result = "";
        }
        return result;
    }
}
