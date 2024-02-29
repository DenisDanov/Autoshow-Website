package com.example.app.dbData.ReplacedTokens;

import com.example.app.dbData.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplacedAuthTokensRepo extends JpaRepository<ReplacedAuthToken, Long> {

    List<ReplacedAuthToken> findByUser(User user);

    @Query("SELECT token FROM ReplacedAuthToken token WHERE token.replaced_token = :replacedToken")
    ReplacedAuthToken findByReplacedToken(String replacedToken);

    @Transactional
    @Modifying
    @Query("DELETE FROM ReplacedAuthToken au WHERE au.replacedToken = :token")
    void deleteByReplacedToken(@Param("token") String token);
}
