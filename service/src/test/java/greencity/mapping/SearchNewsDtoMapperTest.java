package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.search.SearchNewsDto;
import greencity.dto.user.EcoNewsAuthorDto;
import greencity.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
class SearchNewsDtoMapperTest {

    private final SearchNewsDtoMapper searchNewsDtoMapper = new SearchNewsDtoMapper();
    private Long id = 189L;
    private String title = "title";
    private User author = new User();
    private ZonedDateTime currentDate = ZonedDateTime.now();

    @Test
    void convert_SearchNewsDtoMapperTest_ShouldMapCorrectly() {
        EcoNews ecoNews = new EcoNews(id, currentDate, "image", "source", "info",
                author, title, "text", null, new ArrayList<>(), null, null);

        SearchNewsDto expected = SearchNewsDto.builder()
                .id(id)
                .title(title)
                .author(new EcoNewsAuthorDto(author.getId(),
                        author.getName()))
                .creationDate(currentDate)
                .tags(new ArrayList<>())
                .build();

        SearchNewsDto actual = searchNewsDtoMapper.convert(ecoNews);

        assertNotNull(actual);
        assertEquals(ecoNews.getId(), actual.getId());
        assertEquals(ecoNews.getTitle(), actual.getTitle());
        assertEquals(ecoNews.getCreationDate(), actual.getCreationDate());
        assertEquals(expected, actual);
    }

    @Test
    void convert_SearchNewsDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        EcoNews emptyEcoNews = new EcoNews();

        assertThrows(NullPointerException.class, () -> {
            searchNewsDtoMapper.convert(emptyEcoNews);
        });

    }

    @ParameterizedTest
    @NullSource
    void convert_SearchNewsDtoMapperTestWithNullSource_ShouldReturnNullPointerException(
            EcoNews nullEcoNews) {
        assertThrows(NullPointerException.class, () -> {
            searchNewsDtoMapper.convert(nullEcoNews);
        });
    }
}