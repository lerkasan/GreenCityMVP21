package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.econews.EcoNewsVO;
import greencity.dto.user.UserVO;
import greencity.entity.EcoNews;
import greencity.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
class EcoNewsVOMapperTest {

    private final EcoNewsVOMapper ecoNewsVOMapper = new EcoNewsVOMapper();
    private ZonedDateTime currentDate = ZonedDateTime.now();

    @Test
    void convert_EcoNewsVOMapperTest_ShouldMapCorrectly() {
        EcoNews ecoNews = new EcoNews(155L, currentDate, "image", "source", "info",
                new User(), "title", "text", new ArrayList<>(), new ArrayList<>(), new HashSet<>(), new HashSet<>());

        EcoNewsVO expected = expectedConvert(ecoNews);
        EcoNewsVO actual = ecoNewsVOMapper.convert(ecoNews);

        assertNotNull(actual);
        assertEquals(ecoNews.getId(), actual.getId());
        assertEquals(ecoNews.getText(), actual.getText());
        assertEquals(ecoNews.getTitle(), actual.getTitle());
        assertEquals(ecoNews.getCreationDate(), actual.getCreationDate());
        assertEquals(expected, actual);
    }

    @Test
    void convert_EcoNewsVOMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        EcoNews ecoNews = new EcoNews();

        assertThrows(NullPointerException.class, () -> {
            ecoNewsVOMapper.convert(ecoNews);
        });
    }

    private EcoNewsVO expectedConvert(EcoNews ecoNews) {
        return EcoNewsVO.builder()
                .id(ecoNews.getId())
                .author(UserVO.builder()
                        .id(ecoNews.getAuthor().getId())
                        .name(ecoNews.getAuthor().getName())
                        .userStatus(ecoNews.getAuthor().getUserStatus())
                        .role(ecoNews.getAuthor().getRole())
                        .build())
                .creationDate(ecoNews.getCreationDate())
                .imagePath(ecoNews.getImagePath())
                .source(ecoNews.getSource())
                .text(ecoNews.getText())
                .title(ecoNews.getTitle())
                .tags(new ArrayList<>())
                .usersLikedNews(new HashSet<>())
                .ecoNewsComments(new ArrayList<>())
                .usersDislikedNews(new HashSet<>())
                .build();
    }
}