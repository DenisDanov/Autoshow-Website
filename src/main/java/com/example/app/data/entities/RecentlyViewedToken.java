package com.example.app.data.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

@Entity
@Table(name = "recently_viewed_tokens")
public class RecentlyViewedToken {

    private LocalDateTime expireDate;

    @Column(columnDefinition = "TEXT")
    private String recentlyViewedCars;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public RecentlyViewedToken() {
    }

    public String getRecentlyViewedCars() {
        return recentlyViewedCars;
    }

    public LocalDateTime getExpireDate() {
        return this.expireDate;
    }

    public void setExpireDate() {
        LocalDateTime currentDate = LocalDateTime.now();

        // Add one month to the current date
        LocalDateTime expireDate = currentDate.plusMonths(1);

        // Set expireDate to the end of the month
        expireDate = expireDate.with(TemporalAdjusters.lastDayOfMonth());

        // Set the calculated expireDate
        this.expireDate = expireDate;
    }

    public void setRecentlyViewedCars(String recentlyViewedCars) {
        this.recentlyViewedCars = recentlyViewedCars;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
