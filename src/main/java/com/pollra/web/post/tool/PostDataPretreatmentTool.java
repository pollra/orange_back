package com.pollra.web.post.tool;

import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.domain.PostInfo;

import javax.servlet.http.HttpServletRequest;

public class PostDataPretreatmentTool {
    // post data
    private final String DATA_TITLE = "post-data-title";
    private final String DATA_CONTENT = "post-data-content";

    // post info
    private final String INFO_OWNER = "post-info-owner";
    private final String INFO_IMG_PATH = "post-info-img-path";

    public PostData getPostData(HttpServletRequest request) {
        PostData postData = new PostData();
        postData.setTitle(request.getParameter(DATA_TITLE));
        postData.setPostContent(request.getParameter(DATA_CONTENT));
        return postData;
    }

    public PostInfo getPostInfo(HttpServletRequest request) {
        PostInfo postInfo = new PostInfo();
        postInfo.setOwner(request.getParameter(INFO_OWNER));
        postInfo.setImgPath(request.getParameter(INFO_IMG_PATH));
        return postInfo;
    }
}
