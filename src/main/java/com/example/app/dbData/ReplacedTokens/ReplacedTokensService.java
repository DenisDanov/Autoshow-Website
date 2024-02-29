package com.example.app.dbData.ReplacedTokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplacedTokensService {

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    @Autowired
    public ReplacedTokensService(ReplacedAuthTokensRepo replacedAuthTokensRepo) {
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
    }

}
