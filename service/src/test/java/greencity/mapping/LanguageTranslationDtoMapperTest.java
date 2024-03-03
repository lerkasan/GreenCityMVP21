package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.language.LanguageDTO;
import greencity.dto.language.LanguageTranslationDTO;
import greencity.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class LanguageTranslationDtoMapperTest {

    private final LanguageTranslationDtoMapper languageTranslationDtoMapper = new LanguageTranslationDtoMapper();
    private String content = "Content";
    private Language language = new Language();

    @Test
    void convert_LanguageTranslationDtoMapperTest_ShouldMapCorrectly() {
        HabitFactTranslation habitFactTranslation = new HabitFactTranslation();
        habitFactTranslation.setLanguage(language);
        habitFactTranslation.setContent(content);

        LanguageTranslationDTO expected = LanguageTranslationDTO.builder()
                .content(content)
                .language(LanguageDTO.builder()
                        .id(language.getId())
                        .code(language.getCode())
                        .build())
                .build();

        LanguageTranslationDTO actual = languageTranslationDtoMapper.convert(habitFactTranslation);

        assertNotNull(actual);
        assertEquals(habitFactTranslation.getContent(), actual.getContent());
        assertEquals(expected, actual);
    }

    @Test
    void convert_LanguageTranslationDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        HabitFactTranslation emptyHabitFactTranslation = new HabitFactTranslation();

        assertThrows(NullPointerException.class, () -> {
            languageTranslationDtoMapper.convert(emptyHabitFactTranslation);
        });

    }

    @ParameterizedTest
    @NullSource
    void convert_LanguageTranslationDtoMapperTestWithNullSource_ShouldReturnNullPointerException(
            HabitFactTranslation nullHabitFactTranslation) {
        assertThrows(NullPointerException.class, () -> {
            languageTranslationDtoMapper.convert(nullHabitFactTranslation);
        });
    }
}