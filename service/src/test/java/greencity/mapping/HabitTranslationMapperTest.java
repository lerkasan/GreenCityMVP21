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
class HabitTranslationMapperTest {

    private final HabitTranslationMapper habitTranslationMapper = new HabitTranslationMapper();
    private String name = "Name";
    private String description = "Description";
    private String habitItem = "HabitItem";
    private Language language = new Language();

    @Test
    void convert_HabitTranslationMapperTest_ShouldMapCorrectly() {
        HabitTranslationDto habitTranslationDto = new HabitTranslationDto(description, habitItem, language.getCode(), name);

        HabitTranslation expected = HabitTranslation.builder()
                .description(description)
                .habitItem(habitItem)
                .name(name)
                .build();

        HabitTranslation actual = habitTranslationMapper.convert(habitTranslationDto);

        assertNotNull(actual);
        assertEquals(habitTranslationDto.getName(), actual.getName());
        assertEquals(habitTranslationDto.getHabitItem(), actual.getHabitItem());
        assertEquals(habitTranslationDto.getDescription(), actual.getDescription());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitTranslationMapperTestWithEmptySource_ShouldMapWithNullFields() {
        HabitTranslationDto emptyHabitTranslationDto = new HabitTranslationDto();

        HabitTranslation actual = habitTranslationMapper.convert(emptyHabitTranslationDto);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getHabitItem());
        assertNull(actual.getDescription());

    }

    @ParameterizedTest
    @NullSource
    void convert_HabitTranslationMapperTestWithNullSource_ShouldReturnNullPointerException(
            HabitTranslationDto nullHabitTranslationDto) {
        assertThrows(NullPointerException.class, () -> {
            habitTranslationMapper.convert(nullHabitTranslationDto);
        });
    }

    @Test
    void mapAllToList_HabitTranslationMapperTest_ShouldMapCorrectly() {
        HabitTranslationDto habitTranslationDto1 = new HabitTranslationDto();
        HabitTranslationDto habitTranslationDto2 = new HabitTranslationDto();
        List<HabitTranslationDto> list = Arrays.asList(habitTranslationDto1, habitTranslationDto2);

        List<HabitTranslation> actual = habitTranslationMapper.mapAllToList(list);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @ParameterizedTest
    @NullSource
    void mapAllToList_HabitTranslationMapperTestWithNullSource_ShouldReturnNullPointerException(
            List<HabitTranslationDto> nullList) {
        assertThrows(NullPointerException.class, () -> {
            habitTranslationMapper.mapAllToList(nullList);
        });
    }

    @ParameterizedTest
    @EmptySource
    void mapAllToList_HabitTranslationMapperTestWithEmptySource_ShouldReturnEmptyList(List<HabitTranslationDto> emptyList) {
        List<HabitTranslation> actual = habitTranslationMapper.mapAllToList(emptyList);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
}