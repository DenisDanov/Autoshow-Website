package com.example.app.data.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Column(name = "expire_date")
    private Instant expireDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    @Transient
    public boolean isExpired() {
        return Instant.now().isAfter(expireDate);
    }

    public void setExpireDate(Instant expiryDate) {
        this.expireDate = expiryDate;
    }
}
