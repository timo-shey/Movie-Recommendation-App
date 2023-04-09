package com.example.moviesearchapplication.controller;

import com.example.moviesearchapplication.dto.request.FilmRequestDto;
import com.example.moviesearchapplication.dto.request.FilmUpdateRequestDto;
import com.example.moviesearchapplication.dto.request.RatingRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.FilmResponseDto;
import com.example.moviesearchapplication.service.FilmService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getFilmById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addFilm(@Valid @RequestBody FilmRequestDto film) {
        ApiResponse addedFilm = filmService.addFilm(film);
        return new ResponseEntity<>(addedFilm, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateFilm(@PathVariable("id") Long id, @Valid @RequestBody FilmUpdateRequestDto film) {
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    @GetMapping("/searchByTitle")
    public ResponseEntity<ApiResponse<List<FilmResponseDto>>> searchFilmsByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(filmService.searchFilmsByTitle(title));
    }

    @GetMapping("/searchByGenre")
    public ResponseEntity<ApiResponse<List<FilmResponseDto>>> searchFilmsByGenre(@RequestParam("genre") String genre) {
        return ResponseEntity.ok(filmService.searchFilmsByGenre(genre));
    }

    @GetMapping("/searchByDirector")
    public ResponseEntity<ApiResponse<List<FilmResponseDto>>> searchFilmsByDirector(@RequestParam("director") String director) {
        return ResponseEntity.ok(filmService.searchFilmsByDirector(director));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FilmResponseDto>>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @PostMapping("/rate")
    public ResponseEntity<ApiResponse> rateFilm(@Valid @RequestBody RatingRequestDto ratingRequestDto) {
        return ResponseEntity.ok(filmService.rateFilm(ratingRequestDto));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<FilmResponseDto>>> getRecommendedFilms() {
        return ResponseEntity.ok(filmService.getRecommendedFilms());
    }
}
