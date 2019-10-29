package com.pollra.web.repository;

import com.pollra.web.user.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount getById(String id);
    int countById(String id);

    // UPDATE "public"."user_account" SET "password" = '123', "password_match" = '123' WHERE "num" = 4
    @Modifying
    @Query(value = "UPDATE user_account SET password = :pw, password_match = :pwm WHERE id = :userid"
            ,nativeQuery =true)
    void updateByPassword(@Param("pw") String password,@Param("pwm") String password_match,@Param("userid") String id);

    @Modifying
    @Query(value = "UPDATE user_account SET email = :email WHERE user_account.id = :userid",nativeQuery =true)
    void updateByEmail(@Param("email") String email, @Param("userid")String id);

    @Modifying
    @Query(value = "UPDATE user_account SET locked = :val WHERE num = :num", nativeQuery = true)
    int updateByLocked(@Param("val") boolean val, @Param("num") int id);

    @Modifying
    @Query(value = "SELECT * FROM user_account",nativeQuery = true)
    List<UserAccount> getAll();

}
