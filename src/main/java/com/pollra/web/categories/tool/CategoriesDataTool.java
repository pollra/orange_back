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
    private final String CA_NUM = "ca_num";
    private final String CA_NAME = "ca_name";
    private final String CA_OWNER = "ca_owner";

    private HttpServletRequest request;

    public CategoriesDataTool(HttpServletRequest request) {
        this.request = request;
    }

    public CategoriesDAO getCategory(CateFunc func){
        switch (func){
            case C:
                return getCategory_create();
            case U:
                return getCategory_update();
            case D:
                return getCategory_delete();
            default:
                return null;
        }
    }

    private CategoriesDAO getCategory_delete() {
        CategoriesDAO result = new CategoriesDAO();
        try {
            result.setNum(get(CA_NUM).toString().equals("") ? 0L : Long.parseLong(get(CA_NUM).toString()));
        }catch (NumberFormatException e){
            log.error("입력된 ca_num 을 Long 자료형으로 변경 중 예외 발생[{} => Long 자료형]", get(CA_NUM).toString());
            result.setNum(0L);
        }
        return result;
    }

    private CategoriesDAO getCategory_create(){
        CategoriesDAO result = new CategoriesDAO();
        result.setName((String) get(CA_NAME));
        result.setOwner((String)get(CA_OWNER));
        return result;
    }
    private CategoriesDAO getCategory_update(){
        CategoriesDAO result = new CategoriesDAO();
        try {
            result.setNum(get(CA_NUM).toString().equals("") ? 0L : Long.parseLong(get(CA_NUM).toString()));
        }catch (NumberFormatException e){
            log.error("입력된 ca_num 을 Long 자료형으로 변경 중 예외 발생[{} => Long 자료형]", get(CA_NUM).toString());
            result.setNum(0L);
        }
        result.setName((String) get(CA_NAME));
        return result;
    }

    public boolean isNull(CateFunc func, CategoriesDAO dao){
        switch (func){
            case C:
                return isNull_C(dao);
            case U:
                return isNull_U(dao);
            case D:
                return isNull_D(dao);
            default:
                log.warn("선택된 범위는 구현되지 않은 기능입니다[{}]", func);
                return true;
        }
    }

    private boolean isNull_D(CategoriesDAO dao) {
        if(dao == null) return true;
        if(dao.getNum() <= 0) return true;
        return false;
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
