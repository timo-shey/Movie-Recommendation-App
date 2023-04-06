package com.example.moviesearchapplication.repository;

import com.example.moviesearchapplication.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByFilmIdAndUserId(Long filmId, Long userId);
//    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.film.id = :filmId")
//    Double getAverageRatingForFilm(@Param("filmId") Long filmId);
}
