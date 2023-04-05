package com.example.moviesearchapplication.service;

import com.example.moviesearchapplication.model.Film;

import java.util.List;

public class FilmServiceImpl implements FilmService{
    @Override
    public Film getFilmById(Long id) {
        return null;
    }

    @Override
    public List<Film> searchFilmsByName(String name) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }

    @Override
    public void rateFilm(Long filmId, Long userId, Integer ratingValue) {

    }

    @Override
    public List<Film> getRecommendedFilms(Long userId) {
        return null;
    }
}
