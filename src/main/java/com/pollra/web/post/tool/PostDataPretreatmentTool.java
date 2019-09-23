package com.pollra.web.post.tool;

import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.domain.PostInfo;
import com.pollra.web.post.domain.PostList;
import com.pollra.web.post.domain.TargetPost;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class PostDataPretreatmentTool {
    private HttpServletRequest request;

    public PostDataPretreatmentTool(HttpServletRequest request) {
        this.request = request;
    }

    // post data
    private final String DATA_TITLE = "title";
    private final String DATA_CONTENT = "content";

    // post info
    private final String INFO_IMG_PATH = "imgPath";
    private final String INFO_CATEGORY = "category";

    public PostData getPostData() {
        PostData postData = new PostData();
        postData.setTitle(request.getParameter(DATA_TITLE));
        postData.setPostContent(request.getParameter(DATA_CONTENT));
        return postData;
    }

    public PostInfo getPostInfo() {
        PostInfo postInfo = new PostInfo();
        postInfo.setImgPath(request.getParameter(INFO_IMG_PATH));
        postInfo.setCategory(request.getParameter(INFO_CATEGORY).toLowerCase());
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
        if(postData.getTitle() == "") return true;
        if (postData.getPostContent()=="") return true;
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
}
