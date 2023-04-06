package com.example.moviesearchapplication.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "film_tbl")
public class Film extends Base {
    private String title;
    private String director;
    private String genre;
    private Double averageRating;

    @Column(nullable = false)
    private Long ratingId;
}
