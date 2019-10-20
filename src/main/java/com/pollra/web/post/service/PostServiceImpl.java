package com.pollra.web.post.service;

import com.pollra.web.post.domain.*;
import com.pollra.web.post.domain.en.PI_getRange;
import com.pollra.web.post.domain.en.Pi_NullRange;
import com.pollra.web.post.domain.en.Pl_NullRange;
import com.pollra.web.post.exception.PostServiceException;
import com.pollra.web.post.exception.other.*;
import com.pollra.web.post.exception.data.PostDataInsertException;
import com.pollra.web.post.exception.data.PostNotFoundException;
import com.pollra.web.post.exception.info.PostInfoInsertException;
import com.pollra.web.post.exception.list.PostListInsertException;
import com.pollra.web.post.tool.PostDataPretreatmentTool;
import com.pollra.web.post.tool.PostInfoTool;
import com.pollra.web.post.tool.PostListPretreatmentTool;
import com.pollra.web.repository.PostDataRepository;
import com.pollra.web.repository.PostInfoRepository;
import com.pollra.web.repository.PostListRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private PostDataPretreatmentTool dataTool;
    private PostListPretreatmentTool listTool;
    private PostInfoTool infoTool;
    private HttpServletRequest request;

    public PostServiceImpl(PostDataRepository dataRepository, PostInfoRepository infoRepository, PostListRepository listRepository, PostDataPretreatmentTool dataTool, PostListPretreatmentTool listTool, PostInfoTool infoTool, HttpServletRequest request) {
        this.dataRepository = dataRepository;
        this.infoRepository = infoRepository;
        this.listRepository = listRepository;
        this.dataTool = dataTool;
        this.listTool = listTool;
        this.infoTool = infoTool;
        this.request = request;
    }

    /**
     * create
     */
    public PostData createOne() throws PostServiceException {
        log.info("PostServiceImpl.createOne() start");
        PostData postData = dataTool.getPostData();
//        log.info("PostServiceImpl.createOne() count 2");
        PostInfo postInfo = dataTool.getPostInfo();
        // 데이터 유효성 검사
        log.info("PostServiceImpl.createOne() data check");
        if(dataTool.isNull(TargetPost.DATA, postData)){
            throw new PostNotFoundException("입력되지 않은 데이터가 존재합니다: "+postData.toString());
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
        if(dataTool.isNull(TargetPost.DATA,postData)){
            // 입력한 데이터가 올바르지 않습니다.
            throw new IncorrectPostDataException("Post data entered is not valid.");
        }
        // 데이터 이스케이프 처리
        postData.setTitle(escapeString(postData.getTitle()));
        postData.setPostContent(escapeString(postData.getPostContent()));

        // Post 1 개 DB에 입력
        PostData resultPostData = dataRepository.save(postData);
        if(dataTool.isNull(TargetPost.DATA, resultPostData)){
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
     * @throws PostNotFoundException
     * @throws PostInfoInsertException
     */
    private PostInfo createOnePostInfo(Long postNum,PostInfo postInfo)
            throws PostNotFoundException,PostInfoInsertException{
        int ownerCount = 0;
        // 해당 포스팅이 존재하는지 확인
        if(0 >= dataRepository.countByNum(postNum)){
            // 포스트 데이터를 확인할 수 없음.
            throw new PostNotFoundException("PostData is not found");
        }

        // 기본 postInfo 작성
        postInfo.setNum(postNum);
        postInfo.setDate(new SimpleDateFormat("yy.MM.dd").format(new Date(System.currentTimeMillis())));
        postInfo.setUri(postNum+"");
        log.warn("user: {}", request.getAttribute("jwt-user"));
        postInfo.setOwner(request.getAttribute("jwt-user").toString());

        // 작성자의 글을 카운트하고 Uri 에 입력
        ownerCount = infoRepository.countByOwner(postInfo.getOwner());
        if(ownerCount > 0){
            postInfo.setUri(postNum.intValue()+"");
        }

        // 만든 postInfo 를 DB 에 입력
        PostInfo resultPostInfo = postInfo;
        try {
            resultPostInfo = infoRepository.save(postInfo);
        }catch (Exception e){
            throw new PostInfoInsertException("There was a problem entering the info");
        }
        if(dataTool.isNull(TargetPost.INFO,resultPostInfo)){
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
        postList.setId(postData.getNum());
        postList.setTitle(postData.getTitle());
        postList.setImg_path(postInfo.getImgPath());
        postList.setDate(postInfo.getDate());
        postList.setUri(postInfo.getUri());
        postList.setOwner(postInfo.getOwner());
        postList.setCategory(postInfo.getCategory());

        // 데이터를 DB에 입력
        PostList dbInsertResult = listRepository.save(postList);
        if(dataTool.isNull(TargetPost.LIST, dbInsertResult)){
            throw new PostListInsertException("return value of the input is not checked.");
        }
        return dbInsertResult;
    }
    /**
     * delete
     */
    @Transactional
    public void deleteOne(String num){
        /**
         * 넘버를 받음
         */
        // 숫자 받음
        // 데이터가 null 인지 확인
        Long deleteTarget = 0L;
        try{
            deleteTarget = Long.parseLong(num);
        }catch (NumberFormatException e){
            throw new PostNumberFormatException("넘어온 데이터가 숫자가 아닙니다.");
        }
        // 데이터가 잘 변환되었는지 확인
        if(deleteTarget == 0L){
            throw new PostNotFoundException("존재할 수 없는 포스팅 번호입니다.");
        }

        // 데이터가 존재하는지 확인
        int dataCount = dataRepository.countByNum(deleteTarget);
        if(dataCount <= 0){
            throw new PostNotFoundException("해당 데이터가 존재하지 않습니다.");
        }
        // 데이터 삭제
        try {
            dataRepository.deleteById(deleteTarget);
            infoRepository.deleteById(deleteTarget);
            listRepository.deleteById(deleteTarget);
        }catch (Throwable e){
            log.error("데이터 삭제 도중 에러 발생"+e.getMessage());
            throw new IncorrectPostDataException("데이터 삭제에 실패했습니다.");
        }
    }

    /**
     * update
     */
    @Transactional
    public void updateOne() throws PostNullPointerException{
        try {
            // 정보를 받음
            /**
             * 받는 정보
             * PostData ( num, title, content )
             * PostInfo ( category )
             */
            PostData data = dataTool.getPostData();
            PostInfo info = infoTool.getPostInfo(PI_getRange.CATEGORY);
            log.info("info({})",info.toString());

        /*
        변경해야 하는 정보

        postData.title
        postData.post_content

        postInfo.category

        postList.title
        postList.category
         */

            // 데이터 null 체크
            // postData 체크
            if (dataTool.isNull(TargetPost.DATA, data)) {
                throw new PostNullPointerException("입력 데이터가 올바르지 않습니다.");
            }
            // postInfo 체크
            if (infoTool.isNull(Pi_NullRange.UPDATE, info)) {
                throw new PostNullPointerException("입력된 글 정보 데이터가 올바르지 않습니다.");
            }
            // postList 체크
            PostList list = new PostList();
            list.setTitle(data.getTitle());
            list.setCategory(info.getCategory());
            if (listTool.isNull(Pl_NullRange.UPDATE, list)) {
                throw new PostNullPointerException("데이터 조합에 실패했습니다.");
            }

            // 해당 데이터가 DB에 존재하는 데이터인지 확인
            if (dataRepository.countByNum(data.getNum()) <= 0) {
                throw new PostNotFoundException("존재하지 않는 포스트의 변경 요청입니다.");
            }

            dataRepository.updatePostContentAndTitleByNum(data.getTitle(), data.getPostContent(), data.getNum());
            infoRepository.updateCategoryByNum(info.getCategory(), data.getNum());
            listRepository.updateTitleAndCategoryByNum(data.getTitle(), info.getCategory(), data.getNum());
        }catch (Throwable e){
            log.error(e.getMessage());
            throw new PostServiceException("예상치 못한 에러");
        }
    }
    /**
     * read
     */
    /**
     * 페이지 넘버를 받고 게시글 하나를 리턴한다.
     * @param targetPost
     * @return
     */
    public Object readOne(TargetPost targetPost, int postNum){
        log.info("PostService.readOne({},{})",targetPost,postNum);
        switch (targetPost){
            case DATA:
                return readOne_postData(postNum);
            // 포스트 리다이렉트를 리턴하려할 때
            case INFO:
                return readOne_postInfo(InfoRange.NUM, postNum);
            // 포스트를 보려고할 때
            case LIST:
                return readOne_postInfo(InfoRange.URI, postNum);
            default:
                throw new SelectionNotFoundException("존재하지 않는 선택입니다.");
        }
    }

    private PostInfo readOne_postInfo(InfoRange range, int postNum) {
        log.info("readOne_postInfo({}/{})",range , postNum);
        switch (range) {
            case URI:
                if (postNum <= 0) {
                    throw new PostNotFoundException("존재하지 않는 포스트 번호입니다.");
                }
                List<PostInfo> postInfo = infoRepository.getAllByUri(postNum + "");
                if (postInfo.size() <= 0) {
                    throw new PostNotFoundException("포스팅이 존재하지 않습니다.");
                }
                if (dataTool.isNull(TargetPost.INFO, postInfo.get(0))) {
                    throw new PostNotFoundException("존재하지 않는 포스트입니다.");
                }
                return postInfo.get(0);
            case NUM:
                log.info("readOne_postInfo({})", postNum);
                if(postNum <= 0){
                    throw new PostNotFoundException("존재하지 않는 포스트 번호입니다.");
                }
                PostInfo postInfo2 = infoRepository.getByNum(Long.parseLong(postNum+""));
                if(postInfo2 == null || dataTool.isNull(TargetPost.INFO, postInfo2)){
                    throw new PostNotFoundException("포스팅이 존재하지 않습니다.");
                }
                if(dataTool.isNull(TargetPost.INFO, postInfo2)){
                    throw new PostNotFoundException("존재하지 않는 포스트입니다.");
                }
                return postInfo2;
            default:
                throw new SelectionNotFoundException("존재하지 않는 선택지 입니다");
        }
    }

    private PostData readOne_postData(int postNum){
        if(postNum <= 0){
            throw new PostNotFoundException("존재하지 않는 포스트 번호입니다.");
        }
        // 포스트 정보가 존재하는지 확인
        // 정보를 DB에서 검색해봄
        PostData postData = dataRepository.getByNum(Long.parseLong(postNum+""));
        if(dataTool.isNull(TargetPost.DATA, postData)){
            throw new PostNotFoundException("존재하지 않는 포스트입니다.");
        }
        // 검색한 정보가 null 인지 확인
        // null 이 아니라면 리턴
        return postData;
    }

    /**
     * 카테고리 하나를 받고 그 카테고리에 속한 글목록을 리턴한다.
     * @return
     */
    public List<PostList> readList(PL_Range range,String value) {
        // 카테고리가 입력됨 하나의 카테고리 출력
        switch (range){
            case CATEGORY:
                return readListOfCategory(value);
            case OWNER:
                return readListOfOwner(value);
            default:
                log.info("존재하지 않는 선택지 입니다");
                throw new SelectionNotFoundException("존재하지 않는 선택지 입니다");
        }
    }
    private List<PostList> readListOfCategory(String category) {
        // 입력된 카테고리가 없으면 모든 카테고리 출력
        if(StringUtils.isEmpty(category)){
            throw new IncorrentInsertDataException("카테고리 데이터를 확인할 수 없습니다.");
        }
        // DB 에서 조회
        List<PostList> resultList = listRepository.findAllByCategory(category);
        if(listTool.isNull_array(resultList)) {
            throw new IncorrentInsertDataException("카테고리 데이터를 찾을 수 없습니다: " + category);
        }
        // 데이터 리턴
        return resultList;
    }
    private List<PostList> readListOfOwner(String owner) {
        // DB 에서 조회
        List<PostList> resultList = listRepository.findAllByOwner(owner);
        if(listTool.isNull_array(resultList)) {
            throw new IncorrentInsertDataException("카테고리 데이터를 찾을 수 없습니다: " + owner);
        }
        // 데이터 리턴
        return resultList;
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
