package com.pollra.web.user.service;

import com.pollra.web.user.domain.UserAccount;
import com.pollra.web.user.domain.UserAccountVO;
import com.pollra.web.user.domain.en.AccessClassification;
import com.pollra.web.user.domain.en.Range;
import com.pollra.web.user.exception.UserServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    // create
    void createOne() throws UserServiceException;

    // update
    void updateOne(Range range) throws UserServiceException;
    void locked(int num, boolean val) throws UserServiceException;

    // read
    int countOne(AccessClassification ac) throws UserServiceException;
    Object readOne(AccessClassification ac) throws UserServiceException;
    List<UserAccountVO> readList() throws UserServiceException;

    // delete
    void deleteOne(AccessClassification ac) throws UserServiceException;

    // other
    boolean passwordMatchCheck();
    boolean idDuplicateCheck();

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
