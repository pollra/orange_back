package com.pollra.web.categories.service;

import com.pollra.web.categories.domain.CategoriesDAO;
import com.pollra.web.categories.domain.en.CateFunc;
import com.pollra.web.categories.domain.en.CateRange;
import com.pollra.web.categories.exception.CategoriesDeleteException;
import com.pollra.web.categories.exception.CategoriesInsertException;
import com.pollra.web.categories.exception.CategoriesNullPointerException;
import com.pollra.web.categories.exception.NoSuchCategoriesException;
import com.pollra.web.categories.tool.CategoriesDataTool;
import com.pollra.web.repository.CategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void create(CategoriesDAO dao) throws CategoriesNullPointerException, CategoriesInsertException{
        // 데이터 입력
        CategoriesDAO result = categoriesRepository.save(dao);

        // 데이터 입력 확인
        if(result == null){
            throw new CategoriesInsertException("카테고리를 추가하는데 실패했습니다.");
        }
    }

    // 카테고리 수정
    @Transactional
    public void updateCategory(CategoriesDAO dao) throws CategoriesNullPointerException, CategoriesInsertException{
        // 데이터 존재여부 확인
        int dataCheckResult = categoriesRepository.countByNum(dao.getNum());
        if(dataCheckResult <= 0){
            throw new CategoriesNullPointerException("수정하려는 카테고리가 존재하지 않습니다.");
        }
        // 데이터 수정
        int dataInputResult = 0;
        try {
            dataInputResult = categoriesRepository.updateOneByCategoryToNum(dao.getName(), dao.getNum());
        }catch (Throwable e){
            log.error(e.getMessage());
        }
        if(dataInputResult <= 0){
            throw new CategoriesInsertException("카테고리 수정에 실패했습니다");
        }
    }

    // 카테고리 삭제
    @Transactional
    public void delete(CategoriesDAO dao){
        // 데이터 존재여부 확인
        int dataCheckResult = categoriesRepository.countByNum(dao.getNum());
        if(dataCheckResult <= 0){
            throw new CategoriesNullPointerException("삭제하려는 카테고리가 존재하지 않습니다");
        }
        // 데이터 삭제
        try {
            categoriesRepository.deleteById(dao.getNum());
        }catch (Throwable e){
            log.error(e.getMessage());
            throw new CategoriesDeleteException("삭제에 실패했습니다.");
        }
    }
}
