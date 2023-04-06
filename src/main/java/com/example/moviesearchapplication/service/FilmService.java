package com.example.moviesearchapplication.service;

import com.example.moviesearchapplication.model.Film;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FilmService {
    Film getFilmById(Long id);
    List<Film> searchFilmsByName(String name);
    List<Film> getAllFilms();
    void rateFilm(Long filmId, Long userId, Integer ratingValue);
    List<Film> getRecommendedFilms(Long userId);
}
