package com.example.moviesearchapplication.controller;

import com.example.moviesearchapplication.dto.request.FilmRequestDto;
import com.example.moviesearchapplication.dto.request.FilmUpdateRequestDto;
import com.example.moviesearchapplication.dto.request.RatingRequestDto;
import com.example.moviesearchapplication.dto.response.ApiResponse;
import com.example.moviesearchapplication.dto.response.FilmResponseDto;
import com.example.moviesearchapplication.service.FilmService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    public void testGetFilmById() throws Exception {
        Long id = 1L;
        FilmRequestDto requestDto = FilmRequestDto.builder()
                .director("director")
                .title("title")
                .genre("genre")
                .build();
        filmService.addFilm(requestDto);
        FilmResponseDto responseDto = FilmResponseDto.builder()
                .id(id)
                .averageRating(null)
                .title("new title")
                .genre("new genre")
                .director("new director")
                .build();
        ApiResponse expectedApiResponse = new ApiResponse(HttpStatus.OK.name(), "Success", responseDto);
        when(filmService.getFilmById(id)).thenReturn(expectedApiResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/films/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse actualApiResponse = new ObjectMapper().readValue(jsonResult, ApiResponse.class);
        Assert.assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        Assert.assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        String expectedData = new ObjectMapper().writeValueAsString(expectedApiResponse.getData());
        String actualData = new ObjectMapper().writeValueAsString(actualApiResponse.getData());
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testAddFilm() throws Exception {
        Long id = 1L;
        FilmRequestDto requestDto = FilmRequestDto.builder()
                .director("director")
                .title("title")
                .genre("genre")
                .build();
        filmService.addFilm(requestDto);
        FilmResponseDto responseDto = FilmResponseDto.builder()
                .id(id)
                .averageRating(null)
                .title("new title")
                .genre("new genre")
                .director("new director")
                .build();
        ApiResponse expectedApiResponse = new ApiResponse(HttpStatus.CREATED.name(), "Success", responseDto);
        when(filmService.addFilm(requestDto)).thenReturn(expectedApiResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/films/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse actualApiResponse = new ObjectMapper().readValue(jsonResult, ApiResponse.class);
        Assert.assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        Assert.assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        String expectedData = new ObjectMapper().writeValueAsString(expectedApiResponse.getData());
        String actualData = new ObjectMapper().writeValueAsString(actualApiResponse.getData());
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testUpdateFilm() throws Exception {
        Long id = 1L;
        FilmRequestDto requestDto = FilmRequestDto.builder()
                .director("director")
                .title("title")
                .genre("genre")
                .build();
        filmService.addFilm(requestDto);
        FilmUpdateRequestDto filmUpdateRequestDto = FilmUpdateRequestDto.builder()
                .id(id)
                .director("new director")
                .genre("new genre")
                .title("new title")
                .build();
        FilmResponseDto responseDto = FilmResponseDto.builder()
                .id(id)
                .averageRating(null)
                .title("new title")
                .genre("new genre")
                .director("new director")
                .build();
        ApiResponse expectedApiResponse = new ApiResponse(HttpStatus.OK.name(), "Success", responseDto);
        when(filmService.updateFilm(filmUpdateRequestDto)).thenReturn(expectedApiResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/films/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(filmUpdateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse actualApiResponse = new ObjectMapper().readValue(jsonResult, ApiResponse.class);
        Assert.assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        Assert.assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        String expectedData = new ObjectMapper().writeValueAsString(expectedApiResponse.getData());
        String actualData = new ObjectMapper().writeValueAsString(actualApiResponse.getData());
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testSearchFilmsByTitle() throws Exception {
        String title = "test";
        FilmRequestDto requestDto = FilmRequestDto.builder()
                .director("director")
                .title(title)
                .genre("genre")
                .build();
        filmService.addFilm(requestDto);
        ApiResponse<List<FilmResponseDto>> expectedApiResponse = new ApiResponse<>(HttpStatus.OK.name(), "Success", new ArrayList<>());
        when(filmService.searchFilmsByTitle(title)).thenReturn(expectedApiResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/films/searchByTitle")
                        .param("title", title))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse<List<FilmResponseDto>> actualApiResponse = new ObjectMapper().readValue(jsonResult, new TypeReference<>() {});
        Assert.assertEquals(expectedApiResponse, actualApiResponse);
    }

    @Test
    public void searchFilmsByGenre() throws Exception {
        String genre = "comedy";
        FilmRequestDto requestDto = FilmRequestDto.builder()
                .director("director")
                .title("title")
                .genre(genre)
                .build();
        filmService.addFilm(requestDto);
        ApiResponse<List<FilmResponseDto>> expectedApiResponse = new ApiResponse<>(HttpStatus.OK.name(), "Success", new ArrayList<>());
        when(filmService.searchFilmsByGenre(genre)).thenReturn(expectedApiResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/films/searchByGenre")
                        .param("genre", genre))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiResponse<List<FilmResponseDto>> actualApiResponse = new ObjectMapper().readValue(jsonResult, new TypeReference<>() {});
        Assert.assertEquals(expectedApiResponse, actualApiResponse);
    }

    @Test
    public void searchFilmsByDirector() throws Exception {
        String director = "test";
        FilmRequestDto requestDto = FilmRequestDto.builder()
                .director(director)
                .title("title")
                .genre("genre")
                .build();
        filmService.addFilm(requestDto);
        ApiResponse<List<FilmResponseDto>> expectedApiResponse = new ApiResponse<>(HttpStatus.OK.name(), "Success", new ArrayList<>());
        when(filmService.searchFilmsByDirector(director)).thenReturn(expectedApiResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/films/searchByDirector")
                        .param("director", director))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();

        ApiResponse<List<FilmResponseDto>> actualApiResponse = new ObjectMapper().readValue(jsonResult, new TypeReference<>() {});
        Assert.assertEquals(expectedApiResponse, actualApiResponse);
    }

    @Test
    public void getAllFilms() {
        List<FilmResponseDto> films = Arrays.asList(new FilmResponseDto(
                1L, "Film 1", "Director 1", "Genre 1", 2.0), new FilmResponseDto(
                        2L, "Film 2", "Director 2", "Genre 2", 5.0));
        ApiResponse<List<FilmResponseDto>> expectedApiResponse = new ApiResponse<>(HttpStatus.OK.name(), "Success", films);
        when(filmService.getAllFilms()).thenReturn(expectedApiResponse);

        ResponseEntity<ApiResponse<List<FilmResponseDto>>> response = filmController.getAllFilms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(films, response.getBody().getData());
    }

    @Test
    public void testRateFilm() {
        Long id = 1L;
        FilmRequestDto requestDto = FilmRequestDto.builder()
                .director("director")
                .title("title")
                .genre("genre")
                .build();
        filmService.addFilm(requestDto);
        FilmResponseDto responseDto = FilmResponseDto.builder()
                .id(id)
                .averageRating(null)
                .title("title")
                .genre("genre")
                .director("director")
                .build();
        RatingRequestDto ratingRequestDto = RatingRequestDto.builder()
                .title("title")
                .ratingValue(5)
                .build();
        ApiResponse expectedApiResponse = new ApiResponse<>(HttpStatus.OK.name(), "Film rated successfully", responseDto);
        when(filmService.rateFilm(ratingRequestDto)).thenReturn(expectedApiResponse);

        ResponseEntity<ApiResponse> response = filmController.rateFilm(ratingRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Film rated successfully", response.getBody().getMessage());
    }

    @Test
    public void testGetRecommendedFilms() {
        // given
        List<FilmResponseDto> films = Arrays.asList(new FilmResponseDto(
                1L, "Film 1", "Director 1", "Genre 1", 2.0), new FilmResponseDto(
                2L, "Film 2", "Director 2", "Genre 2", 5.0));
        ApiResponse<List<FilmResponseDto>> expectedApiResponse = new ApiResponse<>(HttpStatus.OK.name(), "Success", films);
        when(filmService.getRecommendedFilms()).thenReturn(expectedApiResponse);

        ResponseEntity<ApiResponse<List<FilmResponseDto>>> response = filmController.getRecommendedFilms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(films, response.getBody().getData());
    }
}