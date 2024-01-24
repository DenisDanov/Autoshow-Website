package com.example.demo.dbData.recentlyViewedToken;

import com.example.demo.dbData.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "recently_viewed_token")
public class RecentlyViewedToken {

    private String expireDate;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(this.expireDate, formatter);
    }

    public void setExpireDate() {
        // Get the current date and time
        LocalDateTime currentDate = LocalDateTime.now();

        // Add one month to the current date
        LocalDateTime expireDate = currentDate.plusMonths(1);

        // Format the expiration date as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.expireDate = expireDate.format(formatter);
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
