package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.econews.EcoNewsDto;
import greencity.dto.user.EcoNewsAuthorDto;
import greencity.entity.EcoNews;
import greencity.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
class EcoNewsDtoMapperTest {

    private final EcoNewsDtoMapper ecoNewsDtoMapper = new EcoNewsDtoMapper();
    private ZonedDateTime currentDate = ZonedDateTime.now();

    @Test
    void convert_EcoNewsDtoMapperTest_ShouldMapCorrectly() {
        EcoNews ecoNews = new EcoNews(155L, currentDate, "image", "source", "info",
                new User(), "title", "text", new ArrayList<>(), new ArrayList<>(), new HashSet<>(), new HashSet<>());

        EcoNewsDto expected = expectedConvert(ecoNews);
        EcoNewsDto actual = ecoNewsDtoMapper.convert(ecoNews);

        assertNotNull(actual);
        assertEquals(ecoNews.getId(), actual.getId());
        assertEquals(ecoNews.getText(), actual.getContent());
        assertEquals(ecoNews.getTitle(), actual.getTitle());
        assertEquals(ecoNews.getCreationDate(), actual.getCreationDate());
        assertEquals(expected, actual);
    }

    @Test
    void convert_EcoNewsDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        EcoNews ecoNews = new EcoNews();

        assertThrows(NullPointerException.class, () -> {
            ecoNewsDtoMapper.convert(ecoNews);
        });
    }

    private EcoNewsDto expectedConvert(EcoNews ecoNews) {
        return EcoNewsDto.builder()
                .id(ecoNews.getId())
                .author(EcoNewsAuthorDto.builder()
                        .id(ecoNews.getAuthor().getId())
                        .name(ecoNews.getAuthor().getName())
                        .build())
                .content(ecoNews.getText())
                .creationDate(ecoNews.getCreationDate())
                .imagePath(ecoNews.getImagePath())
                .likes(ecoNews.getUsersLikedNews().size())
                .shortInfo(ecoNews.getShortInfo())
                .tags(new ArrayList<>())
                .tagsUa(new ArrayList<>())
                .title(ecoNews.getTitle())
                .build();
    }
}