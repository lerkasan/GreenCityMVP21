package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.econewscomment.EcoNewsCommentAuthorDto;
import greencity.dto.econewscomment.EcoNewsCommentDto;
import greencity.entity.EcoNews;
import greencity.entity.EcoNewsComment;
import greencity.entity.User;
import greencity.enums.CommentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDateTime;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
class EcoNewsCommentDtoMapperTest {

    private final EcoNewsCommentDtoMapper ecoNewsCommentDtoMapper = new EcoNewsCommentDtoMapper();
    private LocalDateTime currentDate = LocalDateTime.now();
    private LocalDateTime previousDate = currentDate.minusMinutes(5L);
    private User user = new User();

    @Test
    void convert_EcoNewsCommentDtoMapperTest_ShouldMapCorrectly() {
        EcoNewsComment ecoNewsComment = new EcoNewsComment(158L,"Text", currentDate, currentDate,
                null, null, user, new EcoNews(), false, false,new HashSet<>());

        EcoNewsCommentDto expected = expectedConvert(ecoNewsComment);
        EcoNewsCommentDto actual = ecoNewsCommentDtoMapper.convert(ecoNewsComment);

        assertNotNull(actual);
        assertEquals(ecoNewsComment.getModifiedDate(), actual.getModifiedDate());
        assertEquals(CommentStatus.ORIGINAL, actual.getStatus());
        assertEquals(ecoNewsComment.getText(), actual.getText());
        assertEquals(ecoNewsComment.getUsersLiked().size(), actual.getLikes());
        assertEquals(ecoNewsComment.isCurrentUserLiked(), actual.isCurrentUserLiked());
        assertEquals(expected, actual);
    }

    @Test
    void convert_EcoNewsCommentDtoMapperTest_ShouldSetDeletedStatus() {
        EcoNewsComment ecoNewsComment = new EcoNewsComment(158L,"Text", previousDate, currentDate,
                null, null, user, new EcoNews(), true, false,new HashSet<>());

        EcoNewsCommentDto dto = ecoNewsCommentDtoMapper.convert(ecoNewsComment);

        assertEquals(CommentStatus.DELETED, dto.getStatus());
    }

    @Test
    void convert_EcoNewsCommentDtoMapperTest_ShouldSetEditedStatus() {
        EcoNewsComment ecoNewsComment = new EcoNewsComment(158L,"Text", previousDate, currentDate,
                null, null, user, new EcoNews(), false, false,new HashSet<>());

        EcoNewsCommentDto dto = ecoNewsCommentDtoMapper.convert(ecoNewsComment);

        assertEquals(CommentStatus.EDITED, dto.getStatus());
    }

    @Test
    void convert_EcoNewsCommentDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        EcoNewsComment ecoNewsComment = new EcoNewsComment();

        assertThrows(NullPointerException.class, () -> {
            ecoNewsCommentDtoMapper.convert(ecoNewsComment);
        });
    }

    private EcoNewsCommentDto expectedConvert(EcoNewsComment ecoNewsComment) {
        return EcoNewsCommentDto.builder()
                .id(ecoNewsComment.getId())
                .modifiedDate(ecoNewsComment.getModifiedDate())
                .status(CommentStatus.ORIGINAL)
                .text(ecoNewsComment.getText())
                .author(EcoNewsCommentAuthorDto.builder()
                        .id(ecoNewsComment.getUser().getId())
                        .name(ecoNewsComment.getUser().getName())
                        .userProfilePicturePath(ecoNewsComment.getUser().getProfilePicturePath())
                        .build())
                .build();
    }
}