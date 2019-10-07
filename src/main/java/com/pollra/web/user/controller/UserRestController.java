package com.pollra.web.user.controller;

import com.pollra.aop.jwt.anno.TokenCertification;
import com.pollra.aop.jwt.anno.TokenCredential;
import com.pollra.aop.jwt.anno.TokenLogout;
import com.pollra.aop.jwt.config.JwtConstants;
import com.pollra.response.ApiDataDetail;
import com.pollra.web.user.domain.UserAccount;
import com.pollra.web.user.domain.en.AccessClassification;
import com.pollra.web.user.domain.en.Range;
import com.pollra.web.user.domain.en.TargetUser;
import com.pollra.web.user.exception.*;
import com.pollra.web.user.service.UserService;
import com.pollra.web.user.tool.data.UserDataPretreatmentTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.Map;
import java.util.Set;

@Controller
@CrossOrigin
@Slf4j
@RequestMapping("/api/users")
public class UserRestController {
    private UserDataPretreatmentTool tool;
    private UserService userService;
    private HttpServletRequest request;
    private PasswordEncoder passwordEncoder;

    public UserRestController(UserDataPretreatmentTool tool, UserService userService, HttpServletRequest request, PasswordEncoder passwordEncoder) {
        this.tool = tool;
        this.userService = userService;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원 정보 수정
     * @return
     */
    @TokenCertification
    @PutMapping("type/{range}")
    public ResponseEntity<?> updateUserAccount(@PathVariable String range) {
        /*
        인가 과정 에러 발생 시 처리
        * */
        if(!(request.getAttribute("error").toString().isEmpty())){
            log.error(request.getAttribute("error").toString());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(request.getAttribute("error").toString()),HttpStatus.BAD_REQUEST);
        }
        System.out.println("updateUserAccount start");
        try{
            switch (range) {
                case "email":
                    userService.updateOne(Range.EMAIL);
                    break;
                case "password":
                    userService.updateOne(Range.PWS);
                    break;
                default:
                    return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("올바르지 않은 요청"), HttpStatus.BAD_REQUEST);
            }
        }catch (UserIdNotFoundException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }catch (SelectionNotFoundException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }catch(Throwable e){
            log.info(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("서버 오류"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("정보 변경에 성공했습니다."),HttpStatus.OK);
    }

    /**
     * 회원 정보 삭제
     * @return
     */
    @DeleteMapping()
    public ResponseEntity<?> deleteUserAccount(){
        return null;
    }

    /**
     * 회원 가입
     *
     * @return
     */
    @PostMapping
    public ResponseEntity<?> insertOneUser(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        String errorMessage = "";
        // 회원가입 로직
        UserAccount insertAccount = tool.getUserAccount(Range.ALL);
        int countState = 0;
        // 들어온 정보가 null 인지 판단
        try {
            if (tool.isNull(TargetUser.ACCOUNT, insertAccount)) {
                // null 일 경우
                throw new UserDataInsertionException("누락된 필수 입력항목이 존재합니다.");
            }
            // 데이터가 이미 존재하는지 검사
            userService.countOne(AccessClassification.ID);

            Set<ConstraintViolation<UserAccount>> violations = validator.validate(insertAccount);
            for(ConstraintViolation<UserAccount> violation : violations){
                errorMessage = violation.getMessage();
            }
            // 데이터 하나를 저장
            userService.createOne();

            log.info(insertAccount.getId()+"님의 데이터가 성공적으로 저장되었습니다.");
            // 성공적인 데이터 저장.
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("OK"),HttpStatus.OK);
        }catch (NullPointerException e) {
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터를 확인할 수 없습니다."), HttpStatus.BAD_REQUEST);
        }catch (UserServiceException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (TransactionSystemException e){
            log.error(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(errorMessage), HttpStatus.BAD_REQUEST);
        }catch (Throwable e){
            log.error(e.toString());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("알 수 없는 오류로 회원가입에 실패했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 아이디 조회
     * @return
     */
    @PostMapping("check/id")
    public ResponseEntity<?> getUserData(){
        UserAccount userAccount = null;
        try {
            userAccount = (UserAccount) userService.readOne(AccessClassification.ID);
        }catch (UserServiceException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("ok", userAccount.getId()),HttpStatus.OK);
    }

    /**
     * 로그인
     */
    @TokenCredential
    @PostMapping("login")
    public ResponseEntity<?> login(){
        try {
            if (!StringUtils.isEmpty(request.getAttribute(JwtConstants.TOKEN_HEADER).toString())) {
                log.info("로그인 성공 토큰을 발급했습니다.");
                return new ResponseEntity<ApiDataDetail>(
                        new ApiDataDetail(request.getAttribute("loginUser").toString()
                                ,request.getAttribute(JwtConstants.TOKEN_HEADER)
                        ), HttpStatus.OK);
            }
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(request.getAttribute("error").toString()),HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Throwable e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(request.getAttribute("error").toString()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 로그아웃
     * @return
     */
    @TokenLogout
    @PostMapping("logout")
    public ResponseEntity<?> logout(){
        try {
            if (request.getAttribute("error").toString().isEmpty()) {
                // 에러 메세지가 존재하지 않음
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("로그아웃에 성공했습니다.", request.getAttribute(JwtConstants.TOKEN_HEADER)), HttpStatus.OK);
            } else {
                return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(request.getAttribute("error").toString()), HttpStatus.BAD_REQUEST);
            }
        }catch (Throwable e){
            log.info(e.getMessage());
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("로그아웃에 성공했으나, 알 수 없는 문제가 발생했습니다.",""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
