package com.pollra.web.blog.service;

import com.pollra.web.blog.domain.BlogInfo;
import com.pollra.web.blog.domain.BlogInfoVo;
import com.pollra.web.blog.domain.en.BI_Range;
import com.pollra.web.blog.domain.en.BlogUpdateRange;
import com.pollra.web.blog.exception.BlogDataNotFoundException;
import com.pollra.web.blog.exception.NoSuchBlogInfoException;
import com.pollra.web.blog.exception.SelectionNotFoundException;
import com.pollra.web.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class BlogInfoService {
    private BlogRepository blogRepository;
    private BlogInfoDataTool dataTool;

    public BlogInfoService(BlogRepository blogRepository, BlogInfoDataTool dataTool) {
        this.blogRepository = blogRepository;
        this.dataTool = dataTool;
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
     * Update method
     */

    public void updateBlogInfo(BlogInfo blogInfo){
        BlogUpdateRange judgment = updateJudgment(blogInfo);
        if(judgment == null){
            log.error("judgment 가 null 을 리턴했습니다.");
            return;
        }
        log.info("judgment: {}",judgment);
        switch (judgment){
            // one
            case TITLE:
                if(dataTool.isNull(BlogUpdateRange.TITLE, blogInfo)) break;
                blogRepository.updateOneByTitleById(blogInfo.getTitle(), blogInfo.getId());
                return;
            case IMG_PATH:
                if(dataTool.isNull(BlogUpdateRange.IMG_PATH, blogInfo)) break;
                blogRepository.updateOneByImgPathById(blogInfo.getImgPath(), blogInfo.getId());
                return;
            case EXPLANATION:
                if(dataTool.isNull(BlogUpdateRange.EXPLANATION, blogInfo)) break;
                blogRepository.updateOneByExplanationById(blogInfo.getExplanation(), blogInfo.getId());
                return;
            case META_TITLE:
                if(dataTool.isNull(BlogUpdateRange.META_TITLE, blogInfo)) break;
                blogRepository.updateOneByMetaTitleById(blogInfo.getMetaTitle(), blogInfo.getId());
                return;
            // two
            case TITLE_IMG_PATH:
                if(dataTool.isNull(BlogUpdateRange.TITLE_IMG_PATH, blogInfo)) break;
                blogRepository.updateTwoByTiTleAndImgPathById(blogInfo.getTitle(), blogInfo.getImgPath(), blogInfo.getId());
                return;
            case TITLE_EXPLANATION:
                if(dataTool.isNull(BlogUpdateRange.TITLE_EXPLANATION, blogInfo)) break;
                blogRepository.updateTwoByTiTleAndExplanationById(blogInfo.getTitle(), blogInfo.getExplanation(), blogInfo.getId());
                return;
            case IMG_PATH_EXPLANATION:
                if(dataTool.isNull(BlogUpdateRange.IMG_PATH_EXPLANATION, blogInfo)) break;
                blogRepository.updateTwoByImgPathAndExplanationById(blogInfo.getImgPath(), blogInfo.getExplanation(), blogInfo.getId());
                return;
            // all
            case TITLE_IMG_PATH_EXPLANATION:
                if(dataTool.isNull(BlogUpdateRange.TITLE_IMG_PATH_EXPLANATION, blogInfo)) break;
                blogRepository.updateTIEById(blogInfo.getTitle(), blogInfo.getImgPath(), blogInfo.getExplanation(), blogInfo.getId());
                return;
            default:
                log.error("[{}]존재하지 않는 선택지입니다.", judgment);
                throw new SelectionNotFoundException("정상적인 명령이 아닙니다.");
        }
        log.error("입력되지 않은 데이터가 존재합니다[{}, {}]", judgment, blogInfo.toString());
        throw new BlogDataNotFoundException("입력되지 않은 데이터가 존재합니다.");
    }

    // 변경하려는 정보가 무엇인지 판단하여 BlogUpdateRange 로 리턴
    private BlogUpdateRange updateJudgment(BlogInfo blogInfo){
        List<BlogUpdateRange> result = new ArrayList<>();
        for(Map.Entry<BlogUpdateRange, String> item : dataTool.resultMap(blogInfo).entrySet()){
//            log.info("updateJudgment [{}:{}]",(item.getValue()!=null),(item.getKey() != BlogUpdateRange.ID));
            if(item.getValue()!=null && item.getKey() != BlogUpdateRange.ID) result.add(item.getKey());
        }
        if(result.size() <= 0){
            log.error("[{}] 데이터 확인에 실패했습니다.", result.size());
        }else if(result.size() == 1){   // 1
            if(
                result.get(0).equals(BlogUpdateRange.IMG_PATH_EXPLANATION) ||
                result.get(0).equals(BlogUpdateRange.TITLE_EXPLANATION) ||
                result.get(0).equals(BlogUpdateRange.TITLE_IMG_PATH) ||
                result.get(0).equals(BlogUpdateRange.TITLE_IMG_PATH_EXPLANATION)
            ){
                log.error("[{}]존재할 수 없는 선택입니다.", result.get(0));
                return null;
            }
            return result.get(0);
        }else if(result.size() == 2){   // 2
            // TITLE, EXPLANATION
            if(
                (result.get(0).equals(BlogUpdateRange.TITLE) || result.get(1).equals(BlogUpdateRange.TITLE))
                    &&
                (result.get(0).equals(BlogUpdateRange.EXPLANATION) || result.get(1).equals(BlogUpdateRange.EXPLANATION))
            ){
                return BlogUpdateRange.TITLE_EXPLANATION;

            // TITLE, IMG_PATH
            }else if(
                (result.get(0).equals(BlogUpdateRange.TITLE) || result.get(1).equals(BlogUpdateRange.TITLE))
                    &&
                (result.get(0).equals(BlogUpdateRange.IMG_PATH) || result.get(1).equals(BlogUpdateRange.IMG_PATH))
            ){
                return BlogUpdateRange.TITLE_IMG_PATH;

            // IMG_PATH, EXPLANATION
            }else if(
                (result.get(0).equals(BlogUpdateRange.IMG_PATH) || result.get(1).equals(BlogUpdateRange.IMG_PATH))
                    &&
                (result.get(0).equals(BlogUpdateRange.EXPLANATION) || result.get(1).equals(BlogUpdateRange.EXPLANATION))
            ){
                return BlogUpdateRange.IMG_PATH_EXPLANATION;
            }else{
                log.error("올바르지 않은 선택입니다.(else 2 'TITLE, IMG_PATH, EXPLANATION')");
                return null;
            }
        }else if (result.size() == 3){
            // TITLE, IMG_PATH, EXPLANATION
            if(
                (result.get(0).equals(BlogUpdateRange.TITLE) || result.get(1).equals(BlogUpdateRange.TITLE) || result.get(2).equals(BlogUpdateRange.TITLE))
                    &&
                (result.get(0).equals(BlogUpdateRange.IMG_PATH) || result.get(1).equals(BlogUpdateRange.IMG_PATH) || result.get(2).equals(BlogUpdateRange.IMG_PATH))
                    &&
                (result.get(0).equals(BlogUpdateRange.EXPLANATION) || result.get(1).equals(BlogUpdateRange.EXPLANATION) || result.get(2).equals(BlogUpdateRange.EXPLANATION))
            ){
                return BlogUpdateRange.TITLE_IMG_PATH_EXPLANATION;
            }else{
                log.error("올바르지 않은 선택입니다.(else 3 'TITLE, IMG_PATH, EXPLANATION')");
                return null;
            }
        }else{
            log.error("[{}] 존재할 수 없는 다중선택입니다: {}",result.size(), result.toString());
            return null;
        }
        return null;
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
