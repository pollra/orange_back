package com.pollra.web.categories.tool;

import com.pollra.web.categories.domain.CategoriesDAO;
import com.pollra.web.categories.domain.en.CateFunc;
import com.pollra.web.categories.domain.en.CateRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class CategoriesDataTool {
    private final String CA_NUM = "";
    private final String CA_NAME = "";
    private final String CA_OWNER = "";

    private HttpServletRequest request;

    public CategoriesDataTool(HttpServletRequest request) {
        this.request = request;
    }

    public CategoriesDAO getCategory(CateRange range){
        switch (range){
            case DATA:
                return getCategory_Data();
            case ALL:
                return getCategory_All();
            default:
                return null;
        }
    }
    private CategoriesDAO getCategory_Data(){
        CategoriesDAO result = new CategoriesDAO();
        result.setName((String) get(CA_NAME));
        result.setOwner((String)get(CA_OWNER));
        return result;
    }
    private CategoriesDAO getCategory_All(){
        CategoriesDAO result = new CategoriesDAO();
        result.setNum((Long) get(CA_NUM));
        result.setName((String) get(CA_NAME));
        result.setOwner((String)get(CA_OWNER));
        return result;
    }

    public boolean isNull(CateFunc func, CategoriesDAO dao){
        switch (func){
            case C:
                return isNull_C(dao);
            case U:
                return isNull_U(dao);
            default:
                log.warn("선택된 범위는 구현되지 않은 기능입니다[{}]", func);
                return true;
        }
    }

    private boolean isNull_C(CategoriesDAO dao){
        if(dao == null) return true;
        if(dao.getName().equals("")) return true;
        if(dao.getOwner().equals("")) return true;
        return false;
    }

    private boolean isNull_U(CategoriesDAO dao){
        if(dao == null) return true;
        if(dao.getNum() <= 0) return true;
        if(dao.getName().equals("")) return true;
        return false;
    }

    private Object get(String target){
        String item = request.getParameter(target);
        if(item.equals("")) log.info("{} 의 데이터가 null 입니다.", target);
        return item;
    }
}
