package com.pollra.web.post.service;

import com.pollra.web.post.domain.PostData;
import com.pollra.web.post.domain.PostInfo;
import com.pollra.web.post.domain.PostList;
import com.pollra.web.post.domain.TargetPost;
import com.pollra.web.post.exception.PostServiceException;
import com.pollra.web.post.exception.other.IncorrectPostDataException;
import com.pollra.web.post.exception.data.PostDataInsertException;
import com.pollra.web.post.exception.data.PostDataNotFoundException;
import com.pollra.web.post.exception.info.PostInfoInsertException;
import com.pollra.web.post.exception.list.PostListInsertException;
import com.pollra.web.post.tool.PostDataPretreatmentTool;
import com.pollra.web.repository.PostDataRepository;
import com.pollra.web.repository.PostInfoRepository;
import com.pollra.web.repository.PostListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PostServiceImpl implements PostService{

    private PostDataRepository dataRepository;
    private PostInfoRepository infoRepository;
    private PostListRepository listRepository;
    private PostDataPretreatmentTool tool;
    private HttpServletRequest request;

    public PostServiceImpl(PostDataRepository dataRepository, PostInfoRepository infoRepository, PostListRepository listRepository, PostDataPretreatmentTool dataTool, HttpServletRequest request) {
        this.dataRepository = dataRepository;
        this.infoRepository = infoRepository;
        this.listRepository = listRepository;
        this.tool = dataTool;
        this.request = request;
    }

    /**
     * create
     */
    public PostData createOne() throws PostServiceException {
        log.info("PostServiceImpl.createOne() start");
        PostData postData = tool.getPostData();
        log.info("PostServiceImpl.createOne() count 2");
        PostInfo postInfo = tool.getPostInfo();
        // 데이터 유효성 검사
        log.info("PostServiceImpl.createOne() data check");
        if(tool.isNull(TargetPost.DATA, postData)){
            throw new PostDataNotFoundException("입력되지 않은 데이터가 존재합니다: "+postData.toString());
        }
        log.info("createOnePostData start");
        // PostData 를 생성 후 DB에 입력
        postData = createOnePostData(postData);
        log.info("createOnePostData end");

        log.info("createOnePostInfo start");
        // PostInfo 를 생성 후 DB에 입력
        postInfo = createOnePostInfo(postData.getNum(), postInfo);
        log.info("createOnePostInfo end");

        log.info("createOnePostList start");
        // PostList 를 생성 후 DB에 입력
        createOnePostList(postData, postInfo);
        log.info("createOnePostList end, return");
        return postData;
    }

    /**
     * createOnePost
     * 데이터를 하나 받아서 새로운 포스팅을 만듬.
     * @param postData
     * @throws IncorrectPostDataException
     * @throws PostDataInsertException
     */
    private PostData createOnePostData(PostData postData)
        throws IncorrectPostDataException,
            PostDataInsertException {
        // 데이터 유효성 검사
        if(tool.isNull(TargetPost.DATA,postData)){
            // 입력한 데이터가 올바르지 않습니다.
            throw new IncorrectPostDataException("Post data entered is not valid.");
        }
        // 데이터 이스케이프 처리
        postData.setTitle(escapeString(postData.getTitle()));
        postData.setPostContent(escapeString(postData.getPostContent()));

        // Post 1 개 DB에 입력
        PostData resultPostData = dataRepository.save(postData);
        if(tool.isNull(TargetPost.DATA, resultPostData)){
            throw new PostDataInsertException("return value of the input is not checked.");
        }
        return resultPostData;
    }

    /**
     * createOnePostInfo
     * 여러 데이터를 받고 PostInfo 작성, 저장
     * @param postNum   정보를 생성하려는 글의 번호
     * @param postInfo imgPath   글 이미지 경로
     * @param postInfo owner     글 작성자
     * @return
     * @throws PostDataNotFoundException
     * @throws PostInfoInsertException
     */
    private PostInfo createOnePostInfo(Long postNum,PostInfo postInfo)
            throws PostDataNotFoundException,PostInfoInsertException{
        int ownerCount = 0;
        // 해당 포스팅이 존재하는지 확인
        if(0 >= dataRepository.countByNum(postNum)){
            // 포스트 데이터를 확인할 수 없음.
            throw new PostDataNotFoundException("PostData is not found");
        }

        // 기본 postInfo 작성
        postInfo.setNum(postNum);
        postInfo.setDate(new Date(System.currentTimeMillis()).toString());
        postInfo.setUri(postNum+"");
        log.warn("user: {}", request.getAttribute("jwt-user"));
        postInfo.setOwner(request.getAttribute("jwt-user").toString());

        // 작성자의 글을 카운트하고 Uri 에 입력
        ownerCount = infoRepository.countByOwner(postInfo.getOwner());
        if(ownerCount > 0){
            postInfo.setUri((ownerCount + 1)+"");
        }

        // 만든 postInfo 를 DB 에 입력
        PostInfo resultPostInfo = postInfo;
        try {
            resultPostInfo = infoRepository.save(postInfo);
        }catch (Exception e){
            throw new PostInfoInsertException("There was a problem entering the info");
        }
        if(tool.isNull(TargetPost.INFO,resultPostInfo)){
            // 입력 과정 오류
            throw new PostInfoInsertException("return value of the input is not checked.");
        }

        return resultPostInfo;
    }

    /**
     * createOnePostList
     * 데이터를 받고 PostList 작성, 저장
     * @param postData
     * @param postInfo
     * @return
     * @throws PostListInsertException
     */
    private PostList createOnePostList(PostData postData, PostInfo postInfo)
            throws PostListInsertException{

        // DB에 입력할 데이터 생성
        PostList postList = new PostList();
        postList.setTitle(postData.getTitle());
        postList.setImg_path(postInfo.getImgPath());
        postList.setDate(postInfo.getDate());
        postList.setUri(postInfo.getUri());
        postList.setOwner(postInfo.getOwner());

        // 데이터를 DB에 입력
        PostList dbInsertResult = listRepository.save(postList);
        if(tool.isNull(TargetPost.LIST, dbInsertResult)){
            throw new PostListInsertException("return value of the input is not checked.");
        }
        return dbInsertResult;
    }
    /**
     * delete
     */

    public void deleteOne(){

    }

    /**
     * update
     */
    public void updateOne(){

    }
    /**
     * read
     */
    public Object readOne(TargetPost targetPost){
        return null;
    }

    public List<Object> readList(TargetPost targetPost){
        return null;
    }
    /**
     * other method
     */

    // 이스케이프 처리
    private String escapeString(String text){
        Map<String, String> escape = new HashMap<>();
        escape.put("&","&amp;");
        escape.put("<","&lt;");
        escape.put(">","&gt;");
        escape.put("\"","&quot;");
        escape.put("\'","&#39;");
        escape.put("/","&#x2F;");
        escape.put("`","&#x60;");
        escape.put("=","&#x3D;");

        for(Map.Entry<String, String> item : escape.entrySet()){
            text.replaceAll(item.getKey(), item.getValue());
        }

        return text;
    }
}
