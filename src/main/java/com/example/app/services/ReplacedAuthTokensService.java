package com.example.app.services;

import com.example.app.data.entities.ReplacedAuthToken;
import com.example.app.data.entities.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplacedAuthTokensService {

    List<ReplacedAuthToken> findByUser(User user);

    ReplacedAuthToken findByReplacedToken(String replacedToken);

    void deleteByReplacedToken(@Param("token") String token);

    void save(ReplacedAuthToken replacedAuthToken);

    List<ReplacedAuthToken> findAll();

    void deleteAll(List<ReplacedAuthToken> replacedAuthTokens);
}
