package com.example.moviesearchapplication.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rating_tbl")
public class Rating extends Base {
    private Integer ratingValue;

    @Column(nullable = false)
    private Long filmId;

    @Column(nullable = false)
    private Long userId;
}
