package com.example.app.data.entities;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Table(name = "replaced_auth_tokens")
@Entity
public class ReplacedAuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "replaced_token",unique = true, nullable = false)
    private String replacedToken;

    @Column(name = "expire_date")
    private Timestamp expireDate;

    public ReplacedAuthToken() {
    }

    public ReplacedAuthToken(User user, AuthenticationToken authenticationToken, Timestamp expireDate) {
        this.user = user;
        this.replacedToken = authenticationToken.getToken();
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
