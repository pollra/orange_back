package com.pollra.web.categories.service;

import com.pollra.web.categories.domain.CategoriesDAO;
import com.pollra.web.categories.exception.NoSuchCategoriesException;
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

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
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
}
