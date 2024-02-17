package com.example.demo.dbData.ReplacedTokens;
import com.example.demo.dbData.User;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Table(name = "replaced_auth_tokens")
@Entity
public class ReplacedAuthTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(unique = true)
    private String replacedToken;

    private Timestamp expireDate;

    public ReplacedAuthTokens() {
    }

    public ReplacedAuthTokens(User user, String replacedToken, Timestamp expireDate) {
        this.user = user;
        this.replacedToken = replacedToken;
        this.expireDate = expireDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReplacedToken() {
        return replacedToken;
    }

    public void getReplacedToken(String replaced_token) {
        this.replacedToken = replaced_token;
    }

    public Timestamp getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }
}
