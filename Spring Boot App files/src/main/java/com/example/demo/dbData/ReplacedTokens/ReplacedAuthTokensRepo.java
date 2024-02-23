package com.example.demo.dbData.ReplacedTokens;

import com.example.demo.dbData.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplacedAuthTokensRepo extends JpaRepository<ReplacedAuthTokens, Long> {

    List<ReplacedAuthTokens> findByUser(User user);

    @Query("SELECT token FROM ReplacedAuthTokens token WHERE token.replaced_token = :replacedToken")
    ReplacedAuthTokens findByReplacedToken(String replacedToken);

    @Transactional
    @Modifying
    @Query("DELETE FROM ReplacedAuthTokens au WHERE au.replacedToken = :token")
    void deleteByReplacedToken(@Param("token") String token);
}
