package greencity.controller;

import greencity.dto.PageableDto;
import greencity.dto.search.SearchNewsDto;
import greencity.dto.search.SearchResponseDto;
import greencity.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SearchControllerTest {
    @InjectMocks
    private SearchController searchController;

    @Mock
    private SearchService mockSearchService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    @DisplayName("Test search with valid request and verify response")
    public void testSearch_ValidRequest_ReturnsOk() throws Exception {

        String searchQuery = "valid query";
        Locale locale = Locale.ENGLISH;
        SearchResponseDto expectedResponse = SearchResponseDto.builder().ecoNews(Collections.emptyList()).countOfResults(0L).build();
        when(mockSearchService.search(anyString(), eq(locale.getLanguage()))).thenReturn(expectedResponse);

        ResponseEntity<SearchResponseDto> response = searchController.search(searchQuery, locale);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK (200)");
        assertEquals(expectedResponse, response.getBody(), "Response body should match expected");
    }

    @Test
    @DisplayName("Test search with null search query and expect bad request")
    public void testSearch_NullSearchQuery_ReturnsBadRequest() throws Exception {

        mockMvc.perform(get("/search").locale(Locale.ENGLISH).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test search with very long search query and expect successful response")
    public void testSearch_VeryLongSearchQuery_ReturnsOk() throws Exception {

        String veryLongQuery = new String(new char[5001]).replace('\0', 'a');
        Locale locale = Locale.ENGLISH;

        ResponseEntity<SearchResponseDto> response = searchController.search(veryLongQuery, locale);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test search ECO NEWS and expect successful response")
    void searchEcoNews_NullPageable_ReturnsOK() {

        String searchQuery = "test";
        Locale locale = Locale.US;
        Pageable pageable = null;

        SearchService mockSearchService = mock(SearchService.class);

        when(mockSearchService.searchAllNews(pageable, searchQuery, locale.getLanguage())).thenReturn(new PageableDto<>(Collections.emptyList(), 100, 1, 10));

        SearchController searchController = new SearchController(mockSearchService);

        ResponseEntity<PageableDto<SearchNewsDto>> response = searchController.searchEcoNews(pageable, searchQuery, locale);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test null search")
    void searchEcoNews_NullSearchQuery_ReturnsOK() {

        Pageable pageable = PageRequest.of(0, 10);
        Locale locale = Locale.US;

        ResponseEntity<PageableDto<SearchNewsDto>> response = searchController.searchEcoNews(pageable, "", locale);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test search ECO NEWS with valid Pageable")
    void searchEcoNews_ValidPageable_ReturnsOK() {

        String searchQuery = "test";
        Locale locale = Locale.US;
        Pageable pageable = PageRequest.of(1, 20);

        SearchService mockSearchService = mock(SearchService.class);

        List<SearchNewsDto> newsList = Collections.singletonList(new SearchNewsDto());
        PageableDto<SearchNewsDto> expectedResponse = new PageableDto<>(newsList, 100, 2, 20);
        when(mockSearchService.searchAllNews(pageable, searchQuery, locale.getLanguage())).thenReturn(expectedResponse);

        SearchController searchController = new SearchController(mockSearchService);

        ResponseEntity<PageableDto<SearchNewsDto>> response = searchController.searchEcoNews(pageable, searchQuery, locale);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}