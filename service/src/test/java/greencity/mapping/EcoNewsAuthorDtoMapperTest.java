package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.user.EcoNewsAuthorDto;
import greencity.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EcoNewsAuthorDtoMapperTest {

    private final EcoNewsAuthorDtoMapper ecoNewsAuthorDtoMapper = new EcoNewsAuthorDtoMapper();

    @Test
    void convert_EcoNewsAuthorDtoMapperTest_ShouldMapCorrectly() {
        User author = new User();
        author.setId(221L);
        author.setName("Sherlock Holmes");

        EcoNewsAuthorDto expected = new EcoNewsAuthorDto(author.getId(), author.getName());
        EcoNewsAuthorDto actual = ecoNewsAuthorDtoMapper.convert(author);

        assertNotNull(actual);
        assertEquals(author.getId(), actual.getId());
        assertEquals(author.getName(), actual.getName());
        assertEquals(expected, actual);
    }

    @Test
    void convert_EcoNewsAuthorDtoMapperTestWithEmptySource_ShouldMapWithNullFields() {
        User author = new User();
        EcoNewsAuthorDto actual = ecoNewsAuthorDtoMapper.convert(author);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getName());
    }

    @ParameterizedTest
    @NullSource
    void convert_EcoNewsAuthorDtoMapperTestWithNullSource_ShouldReturnNullPointerException(User nullAuthor) {
        assertThrows(NullPointerException.class, () -> {
            ecoNewsAuthorDtoMapper.convert(nullAuthor);
        });
    }
}