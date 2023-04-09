package com.example.moviesearchapplication.repository;

import com.example.moviesearchapplication.model.Film;
import com.example.moviesearchapplication.model.Rating;
import com.example.moviesearchapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByFilmAndUser(Film film, User user);
    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.film.id = :filmId")
    Double getAverageRatingForFilm(@Param("filmId") Long filmId);

    @Query("SELECT r.film FROM Rating r WHERE r.user.id = :userId")
    List<Film> findFilmsRatedByUser(@Param("userId") Long userId);
}
