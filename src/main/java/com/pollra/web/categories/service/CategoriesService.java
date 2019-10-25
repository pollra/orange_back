package com.pollra.web.categories.service;

import com.pollra.web.categories.domain.CategoriesDAO;
import com.pollra.web.categories.domain.en.CateFunc;
import com.pollra.web.categories.domain.en.CateRange;
import com.pollra.web.categories.exception.CategoriesInsertException;
import com.pollra.web.categories.exception.CategoriesNullPointerException;
import com.pollra.web.categories.exception.NoSuchCategoriesException;
import com.pollra.web.categories.tool.CategoriesDataTool;
import com.pollra.web.repository.CategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoriesService {
    private CategoriesRepository categoriesRepository;
    private CategoriesDataTool tool;

    public CategoriesService(CategoriesRepository categoriesRepository, CategoriesDataTool tool) {
        this.categoriesRepository = categoriesRepository;
        this.tool = tool;
    }

    public List<CategoriesDAO> getCategoriesList(String owner){
        List<CategoriesDAO> result;
        try {
            result = categoriesRepository.findAllByOwner(owner);
            log.info(result.toString());
        }catch (NoSuchCategoriesException e){
            log.info("Categories 데이터가 존재하지 않습니다.");
            log.info(e.getMessage());
            result = new ArrayList<>();
        }
        return result;
    }

    // 카테고리 추가
    // controller 에 추가해야 하는 기능:
    // 해당 카테고리의 주인인지 판별하는 기능이 필요.
    // 그럼 데이터를 불러오는곳을 밖으로 빼야함.
    // 로그인 중인 오너와 입력된 오너가 같은지 확인
    // 같지 않으면 어떤 문제?
    // 권한탈취 후 다른 owner 로 데이터를 넣을 수 있음
    public void create(CategoriesDAO dao) throws CategoriesNullPointerException, CategoriesInsertException{
        // 데이터 입력
        CategoriesDAO result = categoriesRepository.save(dao);

        // 데이터 입력 확인
        if(result == null){
            throw new CategoriesInsertException("카테고리를 추가하는데 실패했습니다.");
        }
    }

    // 카테고리 수정
    public void update(CategoriesDAO dao){
        // 데이터 존재여부 확인
        // 데이터 수정
        // 데이터 수정 확인
    }

    // 카테고리 삭제
    public void delete(CategoriesDAO dao){

    }

}
