package com.example.moviesearchapplication.service.impl;

import com.example.moviesearchapplication.dto.request.FilmRequestDto;
import com.example.moviesearchapplication.dto.request.FilmUpdateRequestDto;
import com.example.moviesearchapplication.dto.request.RatingRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.FilmResponseDto;
import com.example.moviesearchapplication.exception.ApplicationException;
import com.example.moviesearchapplication.exception.FilmNotFoundException;
import com.example.moviesearchapplication.exception.ValidationException;
import com.example.moviesearchapplication.model.Film;
import com.example.moviesearchapplication.model.Rating;
import com.example.moviesearchapplication.model.User;
import com.example.moviesearchapplication.repository.FilmRepository;
import com.example.moviesearchapplication.repository.RatingRepository;
import com.example.moviesearchapplication.service.FilmService;
import com.example.moviesearchapplication.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final RatingRepository ratingRepository;
    private final AppUtil appUtil;

    @Override
    public ApiResponse getFilmById(Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException("Film not found"));
        FilmResponseDto filmResponseDto = FilmResponseDto.builder()
                .id(film.getId())
                .title(film.getTitle())
                .genre(film.getGenre())
                .director(film.getDirector())
                .averageRating(film.getRating())
                .build();
        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Details For Film with id: " + id)
                .data(filmResponseDto)
                .build();
    }

    @Override
    public ApiResponse addFilm(FilmRequestDto filmRequestDto) {
        if (filmRepository.existsByTitle(filmRequestDto.getTitle())) {
            throw new ApplicationException("A film with this name already exists.");
        }
        Film film = Film.builder()
                .title(filmRequestDto.getTitle())
                .genre(filmRequestDto.getGenre())
                .director(filmRequestDto.getDirector())
                .build();
        filmRepository.save(film);
        FilmResponseDto responseDto = FilmResponseDto.builder()
                .id(film.getId())
                .title(film.getTitle())
                .genre(film.getGenre())
                .director(film.getDirector())
                .averageRating(film.getRating())
                .build();
        return ApiResponse.builder()
                .status("SUCCESS")
                .message("New Film Added")
                .data(responseDto)
                .build();
    }

    @Override
    public ApiResponse updateFilm(FilmUpdateRequestDto film) {
        Optional<Film> existingFilm = Optional.ofNullable(filmRepository.findById(film.getId()).orElseThrow(
                () -> new FilmNotFoundException("Film not found")
        ));
        Film updatedFilm = existingFilm.get();
        updatedFilm.setId(existingFilm.get().getId());
        updatedFilm.setTitle(film.getTitle());
        updatedFilm.setDirector(film.getDirector());
        updatedFilm.setGenre(film.getGenre());
        filmRepository.save(updatedFilm);
        FilmResponseDto responseDto = FilmResponseDto.builder()
                .id(updatedFilm.getId())
                .title(updatedFilm.getTitle())
                .genre(updatedFilm.getGenre())
                .director(updatedFilm.getDirector())
                .averageRating(updatedFilm.getRating())
                .build();
        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Film Updated")
                .data(responseDto)
                .build();
    }

    @Override
    public ApiResponse<List<FilmResponseDto>> searchFilmsByTitle(String title) {
        List<Film> films = filmRepository.findByTitleContainingIgnoreCase(title);
        return getListApiResponse(films);
    }


    @Override
    public ApiResponse<List<FilmResponseDto>> searchFilmsByGenre(String genre) {
        List<Film> films = filmRepository.findByGenreContainingIgnoreCase(genre);
        return getListApiResponse(films);
    }

    @Override
    public ApiResponse<List<FilmResponseDto>> searchFilmsByDirector(String director) {
        List<Film> films = filmRepository.findByDirectorContainingIgnoreCase(director);
        return getListApiResponse(films);
    }

    private ApiResponse<List<FilmResponseDto>> getListApiResponse(List<Film> films) {
        List<FilmResponseDto> responseDtos = new ArrayList<>();
        for (Film film : films) {
            responseDtos.add(FilmResponseDto.builder()
                    .id(film.getId())
                    .title(film.getTitle())
                    .genre(film.getGenre())
                    .director(film.getDirector())
                    .averageRating(film.getRating())
                    .build());
        }
        return ApiResponse.<List<FilmResponseDto>>builder()
                .status("SUCCESS")
                .message("Films Found")
                .data(responseDtos)
                .build();
    }

    @Override
    public ApiResponse<List<FilmResponseDto>> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        return getListApiResponse(films);
    }

    @Override
    public ApiResponse rateFilm(RatingRequestDto ratingRequestDto) {
        User activeUser = appUtil.getLoggedInUser();
        Film film = filmRepository.findByTitle(ratingRequestDto.getTitle()
        ).orElseThrow(
                ()-> new FilmNotFoundException("Film not found")
        );
        if (ratingRequestDto.getRatingValue() < 1 || ratingRequestDto.getRatingValue() > 5)
            throw new ValidationException("Rating should be between 1 - 5");

        Rating rating = ratingRepository.findByFilmAndUser(film, activeUser)
                .orElse(new Rating());
        rating.setFilm(film);
        rating.setUser(activeUser);
        rating.setRatingValue(ratingRequestDto.getRatingValue());
        ratingRepository.save(rating);
        Double averageRating = ratingRepository.getAverageRatingForFilm(film.getId());
        film.setRating(averageRating);
        filmRepository.save(film);
        FilmResponseDto responseDto = FilmResponseDto.builder()
                .id(film.getId())
                .title(film.getTitle())
                .genre(film.getGenre())
                .director(film.getDirector())
                .averageRating(film.getRating())
                .build();
        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Film Rated Successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public ApiResponse<List<FilmResponseDto>> getRecommendedFilms() {
        User activeUser = appUtil.getLoggedInUser();

        List<Film> ratedFilms = ratingRepository.findFilmsRatedByUser(activeUser.getId());

        List<String> genres = ratedFilms.stream().map(Film::getGenre).distinct().collect(Collectors.toList());
        List<String> directors = ratedFilms.stream().map(Film::getDirector).distinct().collect(Collectors.toList());

        List<Film> highlyRatedFilms = filmRepository.findFilmsRatedByUserInGenresAndDirectors(
                genres, directors, activeUser.getId(), 3L);

        highlyRatedFilms.removeIf(ratedFilms::contains);

        // Sort films by average rating and add to recommendedFilms list
        highlyRatedFilms.sort(Comparator.comparing(Film::getAverageRating).reversed());
        List<Film> recommendedFilms = new ArrayList<>(highlyRatedFilms);

        // Shuffle recommendedFilms and add the first 5 to recommendedFilmDtos
        Collections.shuffle(recommendedFilms);
        List<FilmResponseDto> recommendedFilmDtos = new ArrayList<>();
        int numFilmsToAdd = Math.min(recommendedFilms.size(), 5);
        for (int i = 0; i < numFilmsToAdd; i++) {
            Film film = recommendedFilms.get(i);
            FilmResponseDto filmResponseDto = new FilmResponseDto(film.getId(), film.getTitle(),
                    film.getDirector(), film.getGenre(), film.getAverageRating());
            recommendedFilmDtos.add(filmResponseDto);
        }
        return new ApiResponse<>("SUCCESS", "Recommended films fetched successfully.", recommendedFilmDtos);
    }
}
