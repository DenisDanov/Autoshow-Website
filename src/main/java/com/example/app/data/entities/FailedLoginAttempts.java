package com.example.app.data.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "failed_login_attempts")
public class FailedLoginAttempts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "amount_failed_logins")
    private int amountFailedLogins;
    @Column(name = "is_user_locked")
    private boolean isUserLocked;
    @Column(name = "account_lock_expire_time")
    private Timestamp accountLockExpireTime;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public FailedLoginAttempts() {
    }

    public FailedLoginAttempts(int amountFailedLogins,
                               boolean isUserLocked,
                               Timestamp accountLockExpireTime,
                               User user) {
        this.amountFailedLogins = amountFailedLogins;
        this.isUserLocked = isUserLocked;
        this.accountLockExpireTime = accountLockExpireTime;
        this.user = user;
    }

    public int getAmountFailedLogins() {
        return amountFailedLogins;
    }

    public void setAmountFailedLogins(int amountFailedLogins) {
        this.amountFailedLogins = amountFailedLogins;
    }

    public boolean isUserLocked() {
        return isUserLocked;
    }

    public void setUserLocked(boolean userLocked) {
        isUserLocked = userLocked;
    }

    public Timestamp getAccountLockExpireTime() {
        return accountLockExpireTime;
    }

    public void setAccountLockExpireTime(Timestamp accountLockExpireTime) {
        this.accountLockExpireTime = accountLockExpireTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
