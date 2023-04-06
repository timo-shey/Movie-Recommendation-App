package com.example.moviesearchapplication.service.impl;

import com.example.moviesearchapplication.exception.FilmNotFoundException;
import com.example.moviesearchapplication.exception.UserNotFoundException;
import com.example.moviesearchapplication.model.Film;
import com.example.moviesearchapplication.model.Rating;
import com.example.moviesearchapplication.model.User;
import com.example.moviesearchapplication.repository.FilmRepository;
import com.example.moviesearchapplication.repository.RatingRepository;
import com.example.moviesearchapplication.repository.UserRepository;
import com.example.moviesearchapplication.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final RatingRepository ratingRepository;
    @Override
    public Film getFilmById(Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException("Film not found"));
    }

    @Override
    public List<Film> searchFilmsByName(String name) {
        return filmRepository.findByTitleContainingIgnoreCase(name);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public void rateFilm(Long filmId, Long userId, Integer ratingValue) {
        Film film = getFilmById(filmId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Rating rating = ratingRepository.findByFilmIdAndUserId(film.getId(), user.getId())
                .orElse(new Rating());
        rating.setFilmId(film.getId());
        rating.setUserId(user.getId());
        rating.setRatingValue(ratingValue);

        ratingRepository.save(rating);

//        Double averageRating = ratingRepository.getAverageRatingForFilm(filmId);
//        film.setAverageRating(averageRating);
        filmRepository.save(film);
    }

    @Override
    public List<Film> getRecommendedFilms(Long userId) {
        return null;
    }
}
