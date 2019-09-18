package com.pollra.web.user.tool.data;

import com.pollra.tool.http.InfoPrint;
import com.pollra.web.user.domain.en.Range;
import com.pollra.web.user.domain.en.TargetUser;
import com.pollra.web.user.domain.UserAccount;
import com.pollra.web.user.exception.SelectionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;

@Service
@Slf4j
public class UserDataPretreatmentTool {
    private HttpServletRequest request;

    public UserDataPretreatmentTool(HttpServletRequest request) {
        this.request = request;
    }

    // user account
    private final String USER_ID = "username";
    private final String USER_PW = "password";
    private final String USER_PW_MATCH = "password-match";
    private final String USER_EMAIL = "email";
    private final String DEFAULT_AUTH = "USER";

    public UserAccount getUserAccount(Range range) {
//        InfoPrint.headersPrint(request);
        UserAccount userAccount = new UserAccount();
        switch (range){
            case ID:
                userAccount.setId(get(USER_ID));
                log.info("insert id: "+userAccount.getId());
                return userAccount;
            case PWS:
                userAccount.setPassword(get(USER_PW));
                userAccount.setPasswordMatch(get(USER_PW_MATCH));
                log.warn("insert pw: {}, pwMatch: {}"
                        ,userAccount.getPassword()
                        ,userAccount.getPasswordMatch()
                );
                return userAccount;
            case EMAIL:
                userAccount.setEmail(get(USER_EMAIL));
                log.warn("insert email: {}"
                        ,userAccount.getEmail()
                );
            case ALL:
                userAccount.setId(get(USER_ID));
                userAccount.setPassword(get(USER_PW));
                userAccount.setPasswordMatch(get(USER_PW_MATCH));
                userAccount.setEmail(get(USER_EMAIL));
                userAccount.setAuth(DEFAULT_AUTH);
                log.warn("insert id: {}, pw: {}, pwMatch: {}"
                        ,userAccount.getId()
                        ,userAccount.getPassword()
                        ,userAccount.getPasswordMatch()
                );

                return userAccount;
            default:
                throw new SelectionNotFoundException("선택지가 올바르지 않습니다.");
        }
    }

    public boolean isNull(TargetUser targetUser, Object o){
        TargetUser target = TargetUser.ACCOUNT;
        try {
            switch (targetUser) {
                case ACCOUNT:
                    target = TargetUser.ACCOUNT;
                    return isNull_account((UserAccount) o);
                case ACCOUNT_ID:
                    target = TargetUser.ACCOUNT_ID;
                    return isNull_account_id((UserAccount) o);
                case ACCOUNT_ID_EMAIL:
                    target = TargetUser.ACCOUNT_ID_EMAIL;
                    return isNull_account_id_range(Range.EMAIL, (UserAccount) o);
                case ACCOUNT_ID_PWD:
                    target = TargetUser.ACCOUNT_ID_PWD;
                    return isNull_account_id_range(Range.PWS, (UserAccount) o);
                default:
                    throw new SelectionNotFoundException("미구현 서비스입니다.");
            }
        }catch (NullPointerException e){
            log.warn("isNull 함수에서 {} 를 타겟으로 실행하던 도중 NullPointerException 발생",target.name());
            return true;
        }
    }

    private boolean isNull_account(UserAccount userAccount) throws NullPointerException{
        if(userAccount.getId().isEmpty()) return true;
        if(userAccount.getPassword().isEmpty()) return true;
        if(userAccount.getPasswordMatch().isEmpty()) return true;
        return false;
    }

    private boolean isNull_account_id(UserAccount userAccount) throws NullPointerException{
        if(userAccount.getId().isEmpty()) return true;
        return false;
    }

    private boolean isNull_account_id_range(Range range, UserAccount userAccount) throws NullPointerException{
        if(userAccount.getId().isEmpty()) return true;
        switch (range){
            case EMAIL:
                if(StringUtils.isEmpty(userAccount.getEmail())) return true;
                break;
            case PWS:
                if(StringUtils.isEmpty(userAccount.getPassword())) return true;
                if(StringUtils.isEmpty(userAccount.getPasswordMatch())) return true;
                break;
            default:
                log.warn("선택된 항목은 존재하지 않는 항목입니다.");
        }
        return false;
    }

    private String get(String name){
        return request.getParameter(name);
    }
}
