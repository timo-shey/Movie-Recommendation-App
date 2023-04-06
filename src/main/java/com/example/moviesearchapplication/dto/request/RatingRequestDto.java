package com.example.moviesearchapplication.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RatingRequest {
    @NotNull(message = "FilmId must not be empty")
    private Long filmId;
    @NotNull(message = "RatingValue must not be empty")
    @Max(5)
    @Min(1)
    private Integer ratingValue;
}