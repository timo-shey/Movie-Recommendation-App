package com.example.moviesearchapplication.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmRequestDto {
    @NotNull(message = "Title must not be empty")
    private String title;
    @NotNull(message = "Director must not be empty")
    private String director;
    @NotNull(message = "Genre must not be empty")
    private String genre;
}
