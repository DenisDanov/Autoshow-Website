package com.example.app.data.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Calendar;

@Entity
@Table(name = "recently_viewed_tokens")
public class RecentlyViewedToken {

    private Date expireDate;

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

    public Date getExpireDate() {
        return this.expireDate;
    }

    public void setExpireDate() {
        java.util.Date currentDate = new java.util.Date();

        // Convert java.util.Date to java.sql.Date
        Date sqlDate = new Date(currentDate.getTime());

        // Set the date to one month from now
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sqlDate);
        calendar.add(Calendar.MONTH, 1);
        sqlDate = new Date(calendar.getTimeInMillis());
        this.expireDate = sqlDate;
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
