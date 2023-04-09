package com.example.moviesearchapplication.service;

import com.example.moviesearchapplication.dto.request.FilmRequestDto;
import com.example.moviesearchapplication.dto.request.FilmUpdateRequestDto;
import com.example.moviesearchapplication.dto.request.RatingRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.FilmResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FilmService {
    ApiResponse getFilmById(Long id);
    ApiResponse addFilm(FilmRequestDto film);
    ApiResponse updateFilm(FilmUpdateRequestDto film);
    ApiResponse<List<FilmResponseDto>> searchFilmsByTitle(String title);
    ApiResponse<List<FilmResponseDto>> searchFilmsByGenre(String genre);
    ApiResponse<List<FilmResponseDto>> searchFilmsByDirector(String director);
    ApiResponse<List<FilmResponseDto>> getAllFilms();
    ApiResponse rateFilm(RatingRequestDto ratingRequestDto);
    ApiResponse<List<FilmResponseDto>> getRecommendedFilms();
}
