package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.tag.NewTagDto;
import greencity.entity.*;
import greencity.entity.localization.TagTranslation;
import greencity.enums.TagType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

@ExtendWith(SpringExtension.class)
class NewTagDtoMapperTest {

    private final NewTagDtoMapper newTagDtoMapper = new NewTagDtoMapper();
    private Long id = 189L;
    private TagTranslation translation = new TagTranslation();
    private Language language = new Language();
    private Tag tag = new Tag(id, TagType.EVENT, null, null, null);;

    @Test
    void convert_NewTagDtoMapperTestWithEnglishName_ShouldMapCorrectly() {
        String name = "English Name";
        language.setCode("en");
        translation.setLanguage(language);
        translation.setName(name);
        tag.setTagTranslations(List.of(translation));

        NewTagDto expected = NewTagDto.builder()
                .name(name)
                .nameUa(null)
                .id(id)
                .build();

        NewTagDto actual = newTagDtoMapper.convert(tag);

        assertNotNull(actual);
        assertEquals(tag.getId(), actual.getId());
        assertEquals(name, actual.getName());
        assertNull(actual.getNameUa());
        assertEquals(expected, actual);
    }

    @Test
    void convert_NewTagDtoMapperTestWithUkrainianName_ShouldMapCorrectly() {
        String name = "Українське ім'я";
        language.setCode("ua");
        translation.setLanguage(language);
        translation.setName(name);
        tag.setTagTranslations(List.of(translation));

        NewTagDto expected = NewTagDto.builder()
                .name(null)
                .nameUa(name)
                .id(id)
                .build();

        NewTagDto actual = newTagDtoMapper.convert(tag);

        assertEquals(name, actual.getNameUa());
        assertNull(actual.getName());
        assertEquals(expected, actual);
    }

    @Test
    void convert_NewTagDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        Tag emptyTag = new Tag();

        assertThrows(NullPointerException.class, () -> {
            newTagDtoMapper.convert(emptyTag);
        });

    }

    @ParameterizedTest
    @NullSource
    void convert_NewTagDtoMapperTestWithNullSource_ShouldReturnNullPointerException(
            Tag nullTag) {
        assertThrows(NullPointerException.class, () -> {
            newTagDtoMapper.convert(nullTag);
        });
    }
}