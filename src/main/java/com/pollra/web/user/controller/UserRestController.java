package com.pollra.web.user.controller;

import com.pollra.config.security.SecurityConstants;
import com.pollra.web.user.domain.UserAccount;
import com.pollra.web.user.domain.en.AccessClassification;
import com.pollra.web.user.domain.en.Range;
import com.pollra.web.user.domain.en.TargetUser;
import com.pollra.web.user.exception.*;
import com.pollra.web.user.service.UserService;
import com.pollra.web.user.tool.data.UserDataPretreatmentTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Status;
import org.hibernate.annotations.Target;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
@Slf4j
@RequestMapping("/api")
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
     * 아이디 조회
     * @return
     */
    @PostMapping("public/users/id")
    public ResponseEntity<?> getUserData(){
        UserAccount userAccount = null;
        try {
            userAccount = (UserAccount) userService.readOne(AccessClassification.ID);
        }catch (UserServiceException e){
            return new ResponseEntity<Error>(new Error(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userAccount.getId(),HttpStatus.OK);
    }
    @PostMapping("public/users")
    public ResponseEntity<?> insertOneUser(){
        // 회원가입 로직
        UserAccount insertAccount = tool.getUserAccount(Range.ALL);
        int countState = 0;
        // 들어온 정보가 null 인지 판단
        try {
            if (tool.isNull(TargetUser.ACCOUNT, insertAccount)) {
                // null 일 경우
                throw new UserDataInsertionException("데이터가 null 입니다.");
            }
            // 데이터 정합성 검사
            userService.countOne(AccessClassification.ID);
            userService.createOne();

            log.info(insertAccount.getId()+"님의 데이터가 성공적으로 저장되었습니다.");
            // 성공적인 데이터 저장.
            return new ResponseEntity<String>("OK",HttpStatus.OK);
        }catch (NullPointerException e) {
            log.info(e.toString());
            return new ResponseEntity<Error>(new Error("데이터를 확인할 수 없습니다."), HttpStatus.BAD_REQUEST);
        }catch (UserServiceException e){
            String[] eme_a = e.toString().split("[.]");
            log.info(eme_a[eme_a.length-1]);
            return new ResponseEntity<Error>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 로그인은 필터로 수행
     */

    /**
     * 회원 정보 수정
     * @return
     */
    @PutMapping("protected/users")
    public ResponseEntity<?> updateUserAccount(){

        return null;
    }

    /**
     * 회원 정보 삭제
     * @return
     */
    @DeleteMapping("private/users")
    public ResponseEntity<?> deleteUserAccount(){
        return null;
    }
}
