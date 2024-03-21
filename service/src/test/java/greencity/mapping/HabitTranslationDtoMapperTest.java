package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
class HabitTranslationDtoMapperTest {

    private final HabitTranslationDtoMapper habitTranslationDtoMapper = new HabitTranslationDtoMapper();
    private String name = "Name";
    private String description = "Description";
    private String habitItem = "HabitItem";
    private Language language = new Language();

    @Test
    void convert_HabitTranslationDtoMapperTest_ShouldMapCorrectly() {
        HabitTranslation habitTranslation = new HabitTranslation(110L,name, description, habitItem, language, new Habit());

        HabitTranslationDto expected = HabitTranslationDto.builder()
                .description(description)
                .habitItem(habitItem)
                .name(name)
                .languageCode(language.getCode())
                .build();

        HabitTranslationDto actual = habitTranslationDtoMapper.convert(habitTranslation);

        assertNotNull(actual);
        assertEquals(habitTranslation.getName(), actual.getName());
        assertEquals(habitTranslation.getHabitItem(), actual.getHabitItem());
        assertEquals(habitTranslation.getDescription(), actual.getDescription());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitTranslationDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        HabitTranslation emptyHabitTranslation = new HabitTranslation();

        assertThrows(NullPointerException.class, () -> {
            habitTranslationDtoMapper.convert(emptyHabitTranslation);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_HabitTranslationDtoMapperTestWithNullSource_ShouldReturnNullPointerException(
            HabitTranslation nullHabitTranslation) {
        assertThrows(NullPointerException.class, () -> {
            habitTranslationDtoMapper.convert(nullHabitTranslation);
        });
    }

    @Test
    void mapAllToList_HabitTranslationDtoMapperTest_ShouldMapCorrectly() {
        HabitTranslation habitTranslation1 = new HabitTranslation();
        HabitTranslation habitTranslation2 = new HabitTranslation();
        habitTranslation1.setLanguage(language);
        habitTranslation2.setLanguage(language);
        List<HabitTranslation> list = Arrays.asList(habitTranslation1, habitTranslation2);

        List<HabitTranslationDto> actual = habitTranslationDtoMapper.mapAllToList(list);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @ParameterizedTest
    @NullSource
    void mapAllToList_HabitTranslationDtoMapperTestWithNullSource_ShouldReturnNullPointerException(
            List<HabitTranslation> nullList) {
        assertThrows(NullPointerException.class, () -> {
            habitTranslationDtoMapper.mapAllToList(nullList);
        });
    }

    @ParameterizedTest
    @EmptySource
    void mapAllToList_HabitTranslationDtoMapperTestWithEmptySource_ShouldReturnEmptyList(List<HabitTranslation> emptyList) {
        List<HabitTranslationDto> actual = habitTranslationDtoMapper.mapAllToList(emptyList);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
}