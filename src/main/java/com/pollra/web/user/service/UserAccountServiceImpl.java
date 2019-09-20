package com.pollra.web.user.service;

import com.pollra.web.repository.UserAccountRepository;
import com.pollra.web.user.domain.UserAccountDto;
import com.pollra.web.user.domain.en.AccessClassification;
import com.pollra.web.user.domain.en.Range;
import com.pollra.web.user.domain.en.TargetUser;
import com.pollra.web.user.domain.UserAccount;
import com.pollra.web.user.domain.en.UserAuth;
import com.pollra.web.user.exception.*;
import com.pollra.web.user.tool.data.UserDataPretreatmentTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserAccountServiceImpl implements UserService{

    private UserAccountRepository accountRepository;
    private UserDataPretreatmentTool tool;
    private PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountRepository accountRepository, UserDataPretreatmentTool tool, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.tool = tool;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * create
     */
    @Override
    @Transactional
    public void createOne()
            throws UserServiceException, NullPointerException {
        // 회원가입 폼을 통해 전달된 정보를 UserAccount 객체에 담는다
        UserAccount userAccount = tool.getUserAccount(Range.ALL);

        // 담아진 객체 정보를 확인한다
        if(tool.isNull(TargetUser.ACCOUNT, userAccount)){
            throw new IncorrectUserDataException("입력된 객체의 정보가 올바르지 않습니다.");
        }

        // 패스워드가 일치하는지 확인한다.
        if(!passwordMatchCheck()){
            throw new UserPasswordMatchingException("패스워드가 일치하지 않습니다.");
        }

        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));

        // DB에 입력한다
        UserAccount dbInsertionResult = accountRepository.save(userAccount);
        if(tool.isNull(TargetUser.ACCOUNT, dbInsertionResult)){
            throw new UserDataInsertionException("데이터 입력 과정에서 문제가 발생했습니다.");
        }
    }

    /**
     * update
     */
    @Override
    @Transactional
    public void updateOne(Range range) throws UserIdNotFoundException,UsernameNotFoundException,SelectionNotFoundException{
        // 유저 데이터 수정
        // 요청받은 데이터를 객체에 저장함
        // 경우는 두가지. 비밀번호 변경과 이메일 변경
        // 두가지의 경우 들어오는 데이터로 볼수있는것은
        // password 수정 : username, password, password-match
        // email 수정 : username, email
        UserAccount insertUserAccount = new UserAccount();
        switch (range){
            // 데이터가 null 일 경우 exception 발생
            case EMAIL:
                insertUserAccount = tool.getUserAccount(Range.EMAIL);
                insertUserAccount.setId(tool.getUserAccount(Range.ID).getId());
                if(tool.isNull(TargetUser.ACCOUNT_ID_EMAIL, insertUserAccount)) throw new IncorrectUserDataException("데이터가 정확하지 않습니다");
                break;
            case PWS:
                insertUserAccount = tool.getUserAccount(Range.PWS);
                insertUserAccount.setId(tool.getUserAccount(Range.ID).getId());
                if(tool.isNull(TargetUser.ACCOUNT_ID_PWD, insertUserAccount)) throw new IncorrectUserDataException("데이터가 정확하지 않습니다");
                break;
            default:
                log.info("구현되지 않은 선택지입니다.");
                throw new SelectionNotFoundException("해당 선택지는 구현되지 않은 선택지입니다.");
        }


        // 해당 메소드에서 데이터를 불러오고 데이터가 존재하는지 여부도 판단함.
        // 데이터 존재 여부만 판단하기 위해 사용.
        UserAccount userAccessAccount = (UserAccount)readOne(AccessClassification.ID);
        UserAccount dbResultAccount;
        switch (range){
            // 데이터가 null 일 경우 exception 발생
            case EMAIL:
                accountRepository.updateByEmail(insertUserAccount.getEmail(), insertUserAccount.getId());
                if(tool.isNull(TargetUser.ACCOUNT_ID_EMAIL, insertUserAccount)) throw new IncorrectUserDataException("데이터가 정확하지 않습니다");
                break;
            case PWS:
                accountRepository.updateByPassword(passwordEncoder.encode(insertUserAccount.getPassword()), passwordEncoder.encode(insertUserAccount.getPasswordMatch()), insertUserAccount.getId());
                if(tool.isNull(TargetUser.ACCOUNT_ID_PWD, insertUserAccount)) throw new IncorrectUserDataException("데이터가 정확하지 않습니다");
                break;
            default:
                log.info("구현되지 않은 선택지입니다.");
                throw new SelectionNotFoundException("해당 선택지는 구현되지 않은 선택지입니다.");
        }
//        UserAccount dbResultAccount = accountRepository.updateByPassword(insertUserAccount);

        // 저장 결과가 null 일 경우 데이터 저장에 실패.
        // exception 을 리턴함.
//        if(tool.isNull(TargetUser.ACCOUNT, dbResultAccount)){
//            throw new UserDataInsertionException("데이터 저장에 실패했습니다.");
//        }
    }

    /**
     * read
     */
    @Override
    public Object readOne(AccessClassification accessClassification)
            throws UserIdNotFoundException,UsernameNotFoundException,SelectionNotFoundException{
        // 유저 데이터를 하나 불러온다
        switch (accessClassification){
            case ID:
                UserAccount userAccount = tool.getUserAccount(Range.ID);
                if(tool.isNull(TargetUser.ACCOUNT_ID, userAccount)){
                    // 아이디를 확인할 수 없음
                    throw new UserIdNotFoundException("아이디를 확인할 수 없습니다.");
                }
                UserAccount dbAccessAccount = accountRepository.getById(userAccount.getId());

                if(tool.isNull(TargetUser.ACCOUNT_ID, dbAccessAccount)){
                    throw new UsernameNotFoundException("가입정보를 확인할 수 없습니다.");
                }
                return dbAccessAccount;
            default:
                throw new SelectionNotFoundException("해당 정보로는 검색할 수 없습니다.");
        }
    }

    @Override
    public int countOne(AccessClassification ac) throws UserServiceException {
        int result = -1;
        switch (ac){
            case ID:
                // 아이디를 조회
                UserAccount userAccount = tool.getUserAccount(Range.ID);
                if(userAccount.getId().isEmpty()){
                    // 아이디 정보가 안넘어왔으면 예외 발생
                    throw new UserIdNotFoundException("계정 정보를 확인할 수 없습니다.");
                }
                result = accountRepository.countById(userAccount.getId());
                if(result < 0) throw new UserServiceException("계정 정보를 조회하던 도중 문제가 발생했습니다.");
                if(result > 0) throw new UserDuplicateException("이미 존재하는 계정입니다.");
                return result;
            default:
                return result;
            }
    }

    @Override
    public List<Object> readList(AccessClassification accessClassification) {
        // 회원 리스트를 보는 기능. 현재 쓸모없음.
        return null;
    }

    /**
     * delete
     */
    @Override
    public void deleteOne(AccessClassification accessClassification) {
        // 회원 탈퇴기능. 현재는 쓸모없음
    }

    /**
     * other
     */

    /*
     * 패스워드가 일치하는지 확인하고
     * 사용해도 된다면 true 를 리턴
     */
    @Override
    public boolean passwordMatchCheck() {
        UserAccount userAccount = tool.getUserAccount(Range.PWS);
        log.warn("pw : {}, pw-mc : {}", userAccount.getPassword(), userAccount.getPasswordMatch());
        if(userAccount.getPassword().equals(userAccount.getPasswordMatch())){
            return true;
        }
        return false;
    }

    /*
     * 아이디를 사용해도 되는지 체크
     * 사용해도 된다면 true 를 리턴
     */
    @Override
    public boolean idDuplicateCheck() throws UserServiceException{
        UserAccount userAccount = tool.getUserAccount(Range.ID);

        // null check
        if(!tool.isNull(TargetUser.ACCOUNT_ID, readOne(AccessClassification.ID))){
            return false;
        }
        // 해당 아이디를 사용해도 됨.
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount userAccount = accountRepository.getById(username);
//        UserDetails userDetails = new UserAccountDto(
//                userAccount.getId(),
//                userAccount.getPassword(),
//                userAccount.isLocked(),
//                userAccount.getAuth()
//        );
        return null;
    }
}
