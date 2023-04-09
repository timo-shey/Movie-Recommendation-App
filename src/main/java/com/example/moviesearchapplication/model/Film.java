package com.example.moviesearchapplication.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "film_tbl")
public class Film extends Base {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String director;
    @Column(nullable = false)
    private String genre;
    private Double rating;
    @OneToMany(mappedBy = "film")
    private List<Rating> ratings;

    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        int totalRatings = ratings.size();
        int sumRatings = 0;
        for (Rating rating : ratings) {
            sumRatings += rating.getRatingValue();
        }
        return (double) sumRatings / totalRatings;
    }
}
