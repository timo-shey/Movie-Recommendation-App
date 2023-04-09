package com.example.moviesearchapplication.dto.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequestDto {
    @NotNull(message = "Title must not be empty")
    private String title;
    @NotNull(message = "RatingValue must not be empty")
    @Max(5)
    @Min(1)
    private Integer ratingValue;
}