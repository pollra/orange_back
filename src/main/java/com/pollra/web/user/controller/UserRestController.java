package com.pollra.web.user.controller;

import com.pollra.aop.jwt.anno.TokenCertification;
import com.pollra.aop.jwt.anno.TokenCredential;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

            // 데이터 하나를 저장
            userService.createOne();

            log.info(insertAccount.getId()+"님의 데이터가 성공적으로 저장되었습니다.");
            // 성공적인 데이터 저장.
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("OK"),HttpStatus.OK);
        }catch (NullPointerException e) {
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail("데이터를 확인할 수 없습니다."), HttpStatus.BAD_REQUEST);
        }catch (UserServiceException e){
            return new ResponseEntity<ApiDataDetail>(new ApiDataDetail(e.getMessage()), HttpStatus.BAD_REQUEST);
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
}
