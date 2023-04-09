package com.example.moviesearchapplication.repository;

import com.example.moviesearchapplication.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByTitle(String name);

    List<Film> findByTitleContainingIgnoreCase(String name);

    List<Film> findByGenreContainingIgnoreCase(String name);

    List<Film> findByDirectorContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT f, AVG(r.ratingValue) as avgRating, COUNT(DISTINCT u.id) as numUsers " +
            "FROM Film f " +
            "JOIN f.ratings r " +
            "JOIN r.user u " +
            "JOIN u.ratings ur " +
            "WHERE ur.film.genre IN :genres " +
            "  AND ur.film.director IN :directors " +
            "  AND ur.film <> f " +
            "  AND u.id <> :userId " +
            "GROUP BY f.id " +
            "HAVING COUNT(DISTINCT u.id) >= :minUsers " +
            "ORDER BY avgRating DESC, numUsers DESC")
    List<Film> findFilmsRatedByUserInGenresAndDirectors(@Param("genres") List<String> genres,
                                    @Param("directors") List<String> directors,
                                    @Param("userId") Long userId,
                                    @Param("minUsers") Long minUsers);

    Boolean existsByTitle(String title);
}
