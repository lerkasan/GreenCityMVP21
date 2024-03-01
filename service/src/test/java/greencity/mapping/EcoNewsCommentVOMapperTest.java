package greencity.mapping;

import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.dto.user.UserVO;
import greencity.entity.EcoNews;
import greencity.entity.EcoNewsComment;
import greencity.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class EcoNewsCommentVOMapperTest {
    private final EcoNewsCommentVOMapper ecoNewsCommentVOMapper = new EcoNewsCommentVOMapper();
    private LocalDateTime currentDate = LocalDateTime.now();
    private LocalDateTime previousDate = currentDate.minusMinutes(5L);
    private User user = new User();
    private EcoNewsComment ecoNewsComment = new EcoNewsComment(158L,"Text", previousDate, previousDate,
            null, null, user, new EcoNews(), false, false, new HashSet<>());
    @Test
    void convert_EcoNewsCommentVOMapperTest_ShouldMapCorrectly() {
        EcoNewsCommentVO expected = EcoNewsCommentVO.builder()
                .id(ecoNewsComment.getId())
                .modifiedDate(ecoNewsComment.getModifiedDate())
                .text(ecoNewsComment.getText())
                .user(UserVO.builder()
                        .id(ecoNewsComment.getUser().getId())
                        .role(ecoNewsComment.getUser().getRole())
                        .name(ecoNewsComment.getUser().getName())
                        .build())
                .deleted(ecoNewsComment.isDeleted())
                .currentUserLiked(ecoNewsComment.isCurrentUserLiked())
                .createdDate(ecoNewsComment.getCreatedDate())
                .usersLiked(ecoNewsComment.getUsersLiked().stream().map(user -> UserVO.builder()
                                .id(user.getId())
                                .build())
                        .collect(Collectors.toSet()))
                .ecoNews(EcoNewsVO.builder()
                        .id(ecoNewsComment.getEcoNews().getId())
                        .build())
                .build();

        EcoNewsCommentVO actual = ecoNewsCommentVOMapper.convert(ecoNewsComment);

        assertNotNull(actual);
        assertEquals(ecoNewsComment.getModifiedDate(), actual.getModifiedDate());
        assertEquals(ecoNewsComment.getText(), actual.getText());
        assertEquals(ecoNewsComment.isCurrentUserLiked(), actual.isCurrentUserLiked());
        assertNull(actual.getParentComment());
        assertEquals(expected, actual);
    }
    @Test
    void convert_EcoNewsCommentVOMapperTest_ShouldMapCorrectlyWithParentComment() {
        EcoNewsComment ecoNewsCommentWithParentComment = new EcoNewsComment(199L,"Text", currentDate, currentDate,
                ecoNewsComment, null, user, new EcoNews(), false, false,new HashSet<>());

        EcoNewsCommentVO expected = ecoNewsCommentVOMapper.convert(ecoNewsComment);
        EcoNewsCommentVO actual = ecoNewsCommentVOMapper.convert(ecoNewsCommentWithParentComment);

        assertEquals(expected, actual.getParentComment());
    }

    @Test
    void convert_EcoNewsCommentVOMapperTest_ShouldReturnNullPointerException() {
        EcoNewsComment ecoNewsComment = new EcoNewsComment();

        assertThrows(NullPointerException.class, () -> {
            ecoNewsCommentVOMapper.convert(ecoNewsComment);
        });
    }
}