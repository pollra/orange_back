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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    private PostDataRepository dataRepository;
    private PostInfoRepository infoRepository;
    private PostListRepository listRepository;
    private PostDataPretreatmentTool dataTool;

    public PostServiceImpl(PostDataRepository dataRepository, PostInfoRepository infoRepository, PostListRepository listRepository) {
        this.dataRepository = dataRepository;
        this.infoRepository = infoRepository;
        this.listRepository = listRepository;
        this.dataTool = new PostDataPretreatmentTool();
    }
    /**
     * create
     */
    public void createOne(HttpServletRequest request) throws PostServiceException {
        PostData postData = dataTool.getPostData(request);
        PostInfo postInfo = dataTool.getPostInfo(request);

        // PostData 를 생성 후 DB에 입력
        createOnePostData(postData);

        // PostInfo 를 생성 후 DB에 입력
        createOnePostInfo(postData.getNum(), postInfo);

        // PostList 를 생성 후 DB에 입력
        createOnePostList(postData, postInfo);
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
        // 데이터 정합성 검사
        if(isNull_postData(postData)){
            // 입력한 데이터가 올바르지 않습니다.
            throw new IncorrectPostDataException("Post data entered is not valid.");
        }
        // Post 1 개 DB에 입력
        PostData resultPostData = dataRepository.save(postData);
        if(isNull(TargetPost.DATA, resultPostData)){
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
        postInfo.setUri("1");

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
        if(isNull(TargetPost.INFO,resultPostInfo)){
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
        if(isNull(TargetPost.LIST, dbInsertResult)){
            throw new PostListInsertException("return value of the input is not checked.");
        }
        return dbInsertResult;
    }
    /**
     * delete
     */

    public void deleteOne(HttpServletRequest request){

    }

    /**
     * update
     */
    public void updateOne(HttpServletRequest request){

    }
    /**
     * read
     */
    public Object readOne(TargetPost targetPost, HttpServletRequest request){
        return null;
    }

    public List<Object> readList(TargetPost targetPost, HttpServletRequest request){
        return null;
    }
    /**
     * other method
     */

    /**
     * 입력 객체가 null 일 경우 true 리턴
     * @return
     *  true : 입력 객체의 데이터중 null 이 존재합니다.
     *  false : 입력 객체가 정상적입니다.
     */
    private boolean isNull(TargetPost targetPost, Object o){
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
