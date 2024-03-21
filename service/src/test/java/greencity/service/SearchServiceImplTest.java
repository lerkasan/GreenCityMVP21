package greencity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import greencity.dto.PageableDto;
import greencity.dto.search.SearchNewsDto;
import greencity.dto.search.SearchResponseDto;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @InjectMocks
    public SearchServiceImpl searchService;

    @Mock
    public EcoNewsService ecoNewsService;

    @ParameterizedTest
    @CsvSource({
            "test, en", "test, ua", "test, ru"})
    void search_whenSearchQueryAndLanguageCodeValid_returnSearchResponseDto(String searchQuery,
                                                                                  String languageCode) {
        PageableDto<SearchNewsDto> pageableDto = Mockito.mock(PageableDto.class);
        when(ecoNewsService.search(searchQuery, languageCode)).thenReturn(pageableDto);

        when(pageableDto.getPage()).thenReturn(null);
        when(pageableDto.getTotalElements()).thenReturn(0L);

        SearchResponseDto searchResponseDto = searchService.search(searchQuery, languageCode);

        assertNull(searchResponseDto.getEcoNews());
        assertEquals(0L, searchResponseDto.getCountOfResults());

        verify(ecoNewsService).search(searchQuery, languageCode);
    }

    @Test
    void search_whenSearchQueryIsNull_shouldCallEcoNewsServiceWithNulls() {
        PageableDto<SearchNewsDto> pageableDto = Mockito.mock(PageableDto.class);
        when(ecoNewsService.search(null, null)).thenReturn(pageableDto);

        when(pageableDto.getPage()).thenReturn(null);
        when(pageableDto.getTotalElements()).thenReturn(0L);
        SearchResponseDto searchResponseDto = searchService.search(null, null);

        assertNull(searchResponseDto.getEcoNews());
        assertEquals(0L, searchResponseDto.getCountOfResults());

        verify(ecoNewsService).search(null, null);
    }

    @Test
    void searchAllNews_whenSearchQueryIsNull_shouldCallEcoNewsServiceWithNulls() {
        searchService.searchAllNews(null, null, null);

        verify(ecoNewsService).search(null, null, null);
    }

    @Test
    void searchAllNews_whenSearchQueryAndLanguageCodeValid_shouldCallEcoNewsServiceWithValidParameters() {
        Pageable pageable = Mockito.mock(Pageable.class);
        PageableDto<SearchNewsDto> pageableDto = Mockito.mock(PageableDto.class);
        when(ecoNewsService.search(pageable, "test", "en")).thenReturn(pageableDto);

        PageableDto<SearchNewsDto> result = searchService.searchAllNews(pageable, "test", "en");

        assertEquals(pageableDto, result);

        verify(ecoNewsService).search(pageable, "test", "en");
    }
}
