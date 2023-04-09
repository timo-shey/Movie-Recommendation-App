package com.example.moviesearchapplication.service.impl;

import com.example.moviesearchapplication.dto.request.FilmRequestDto;
import com.example.moviesearchapplication.dto.request.FilmUpdateRequestDto;
import com.example.moviesearchapplication.dto.request.RatingRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.FilmResponseDto;
import com.example.moviesearchapplication.model.Film;
import com.example.moviesearchapplication.model.Rating;
import com.example.moviesearchapplication.model.User;
import com.example.moviesearchapplication.repository.FilmRepository;
import com.example.moviesearchapplication.repository.RatingRepository;
import com.example.moviesearchapplication.utils.AppUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceImplTest {
    @Mock
    private FilmRepository filmRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private AppUtil appUtil;

    @InjectMocks
    private FilmServiceImpl filmService;

    @Test
    public void testGetFilmById() {
        Film film = new Film();
        film.setId(1L);
        film.setTitle("The Shawshank Redemption");
        film.setGenre("Drama");
        film.setDirector("Frank Darabont");
        film.setRating(3.0);

        FilmResponseDto filmResponseDto = new FilmResponseDto();
        filmResponseDto.setId(film.getId());
        filmResponseDto.setTitle(film.getTitle());
        filmResponseDto.setGenre(film.getGenre());
        filmResponseDto.setDirector(film.getDirector());
        filmResponseDto.setAverageRating(film.getRating());

        ApiResponse expectedResponse = ApiResponse.builder()
                .status("SUCCESS")
                .message("Details For Film with id: " + film.getId())
                .data(filmResponseDto)
                .build();

        when(filmRepository.findById(film.getId())).thenReturn(Optional.of(film));

        ApiResponse actualResponse = filmService.getFilmById(film.getId());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testAddFilm() {
        FilmRequestDto filmRequestDto = new FilmRequestDto();
        filmRequestDto.setTitle("The Shawshank Redemption");
        filmRequestDto.setGenre("Drama");
        filmRequestDto.setDirector("Frank Darabont");

        Film film = new Film();
        film.setTitle(filmRequestDto.getTitle());
        film.setGenre(filmRequestDto.getGenre());
        film.setDirector(filmRequestDto.getDirector());

        FilmResponseDto filmResponseDto = new FilmResponseDto();
        filmResponseDto.setTitle(film.getTitle());
        filmResponseDto.setGenre(film.getGenre());
        filmResponseDto.setDirector(film.getDirector());
        filmResponseDto.setAverageRating(film.getRating());

        ApiResponse expectedResponse = ApiResponse.builder()
                .status("SUCCESS")
                .message("New Film Added")
                .data(filmResponseDto)
                .build();

        when(filmRepository.existsByTitle(filmRequestDto.getTitle())).thenReturn(false);
        when(filmRepository.save(Mockito.any(Film.class))).thenReturn(film);

        ApiResponse actualResponse = filmService.addFilm(filmRequestDto);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void updateFilm() {
        Long id = 1L;
        FilmUpdateRequestDto filmDto = new FilmUpdateRequestDto();
        filmDto.setId(id);
        filmDto.setTitle("New Title");
        filmDto.setDirector("New Director");
        filmDto.setGenre("New Genre");

        Film existingFilm = new Film();
        existingFilm.setId(id);
        existingFilm.setTitle("Old Title");
        existingFilm.setDirector("Old Director");
        existingFilm.setGenre("Old Genre");

        when(filmRepository.findById(id)).thenReturn(Optional.of(existingFilm));

        ApiResponse response = filmService.updateFilm(filmDto);

        verify(filmRepository, times(1)).findById(id);
        verify(filmRepository, times(1)).save(existingFilm);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Film Updated", response.getMessage());

        FilmResponseDto responseDto = (FilmResponseDto) response.getData();
        assertEquals(id, responseDto.getId());
        assertEquals("New Title", responseDto.getTitle());
        assertEquals("New Director", responseDto.getDirector());
        assertEquals("New Genre", responseDto.getGenre());
    }

    @Test
    public void searchFilmsByTitle() {
        String title = "The Matrix";
        List<Film> films = new ArrayList<>();
        films.add(new Film());
        films.add(new Film());

        when(filmRepository.findByTitleContainingIgnoreCase(title)).thenReturn(films);

        // Act
        ApiResponse<List<FilmResponseDto>> response = filmService.searchFilmsByTitle(title);

        // Assert
        verify(filmRepository, times(1)).findByTitleContainingIgnoreCase(title);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Films Found", response.getMessage());

        List<FilmResponseDto> responseDtos = response.getData();
        assertEquals(2, responseDtos.size());
    }

    @Test
    public void searchFilmsByGenre() {
        String genre = "Action";
        List<Film> films = new ArrayList<>();
        films.add(new Film());
        films.add(new Film());

        when(filmRepository.findByGenreContainingIgnoreCase(genre)).thenReturn(films);

        ApiResponse<List<FilmResponseDto>> response = filmService.searchFilmsByGenre(genre);

        verify(filmRepository, times(1)).findByGenreContainingIgnoreCase(genre);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Films Found", response.getMessage());

        List<FilmResponseDto> responseDtos = response.getData();
        assertEquals(2, responseDtos.size());
    }

    @Test
    public void searchFilmsByDirector() {
        String director = "Christopher Nolan";
        List<Film> films = new ArrayList<>();
        films.add(new Film());
        films.add(new Film());

        when(filmRepository.findByDirectorContainingIgnoreCase(director)).thenReturn(films);

        ApiResponse<List<FilmResponseDto>> response = filmService.searchFilmsByDirector(director);

        verify(filmRepository, times(1)).findByDirectorContainingIgnoreCase(director);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Films Found", response.getMessage());

        List<FilmResponseDto> responseDtos = response.getData();
        assertEquals(2, responseDtos.size());
    }

    @Test
    public void getAllFilms() {
        List<Film> films = new ArrayList<>();
        Film film1 = new Film();
        film1.setId(1L);
        film1.setTitle("Film 1");
        film1.setDirector("Director 1");
        film1.setGenre("Action");
        film1.setRating(4.0);
        films.add(film1);
        Film film2 = new Film();
        film2.setId(2L);
        film2.setTitle("Film 2");
        film2.setDirector("Director 2");
        film2.setGenre("Comedy");
        film2.setRating(3.0);
        films.add(film2);
        when(filmRepository.findAll()).thenReturn(films);

        ApiResponse<List<FilmResponseDto>> response = filmService.getAllFilms();
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Films Found", response.getMessage());
        List<FilmResponseDto> filmDtos = response.getData();
        assertEquals(2, filmDtos.size());
        assertEquals("Film 1", filmDtos.get(0).getTitle());
        assertEquals("Director 1", filmDtos.get(0).getDirector());
        assertEquals("Action", filmDtos.get(0).getGenre());
        assertEquals(4.0, filmDtos.get(0).getAverageRating(), 0.0);
        assertEquals("Film 2", filmDtos.get(1).getTitle());
        assertEquals("Director 2", filmDtos.get(1).getDirector());
        assertEquals("Comedy", filmDtos.get(1).getGenre());
        assertEquals(3.0, filmDtos.get(1).getAverageRating(), 0.0);
    }

    @Test
    public void rateFilm() {
        User activeUser = new User();
        activeUser.setId(1L);
        when(appUtil.getLoggedInUser()).thenReturn(activeUser);

        Film film = new Film();
        film.setId(1L);
        film.setTitle("Test Film");
        film.setGenre("Action");
        film.setDirector("Test Director");
        when(filmRepository.findByTitle(anyString())).thenReturn(Optional.of(film));

        RatingRequestDto ratingRequestDto = new RatingRequestDto();
        ratingRequestDto.setTitle("Test Film");
        ratingRequestDto.setRatingValue(4);

        Rating rating = new Rating();
        rating.setFilm(film);
        rating.setUser(activeUser);
        rating.setRatingValue(4);
        when(ratingRepository.findByFilmAndUser(film, activeUser)).thenReturn(Optional.of(rating));

        when(ratingRepository.getAverageRatingForFilm(film.getId())).thenReturn(4.0);

        ApiResponse response = filmService.rateFilm(ratingRequestDto);

        verify(filmRepository).save(film);
        verify(ratingRepository).save(rating);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Film Rated Successfully", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    public void getRecommendedFilms() {
        User activeUser = new User();
        activeUser.setId(1L);
        when(appUtil.getLoggedInUser()).thenReturn(activeUser);

        List<Film> ratedFilms = new ArrayList<>();
        Film ratedFilm1 = new Film();
        ratedFilm1.setId(1L);
        ratedFilm1.setTitle("Test Film 1");
        ratedFilm1.setGenre("Action");
        ratedFilm1.setDirector("Test Director 1");
        ratedFilm1.setRating(4.0);
        ratedFilms.add(ratedFilm1);

        Film ratedFilm2 = new Film();
        ratedFilm2.setId(2L);
        ratedFilm2.setTitle("Test Film 2");
        ratedFilm2.setGenre("Comedy");
        ratedFilm2.setDirector("Test Director 2");
        ratedFilm2.setRating(3.5);
        ratedFilms.add(ratedFilm2);

        List<Film> highlyRatedFilms = new ArrayList<>();
        Film highlyRatedFilm1 = new Film();
        highlyRatedFilm1.setId(3L);
        highlyRatedFilm1.setTitle("Highly Rated Film 1");
        highlyRatedFilm1.setGenre("Action");
        highlyRatedFilm1.setDirector("Test Director 1");
        highlyRatedFilm1.setRating(4.5);
        highlyRatedFilms.add(highlyRatedFilm1);

        Film highlyRatedFilm2 = new Film();
        highlyRatedFilm2.setId(4L);
        highlyRatedFilm2.setTitle("Highly Rated Film 2");
        highlyRatedFilm2.setGenre("Comedy");
        highlyRatedFilm2.setDirector("Test Director 2");
        highlyRatedFilm2.setRating(4.0);
        highlyRatedFilms.add(highlyRatedFilm2);

        when(ratingRepository.findFilmsRatedByUser(activeUser.getId())).thenReturn(ratedFilms);
        when(filmRepository.findFilmsRatedByUserInGenresAndDirectors(
                Arrays.asList("Action", "Comedy"), Arrays.asList("Test Director 1", "Test Director 2"),
                activeUser.getId(), 3L)).thenReturn(highlyRatedFilms);

        ApiResponse<List<FilmResponseDto>> response = filmService.getRecommendedFilms();

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Recommended films fetched successfully.", response.getMessage());
        List<FilmResponseDto> recommendedFilms = response.getData();
        assertEquals(2, recommendedFilms.size());
        assertEquals("Highly Rated Film 2", recommendedFilms.get(0).getTitle());
        assertEquals("Highly Rated Film 1", recommendedFilms.get(1).getTitle());
    }
}