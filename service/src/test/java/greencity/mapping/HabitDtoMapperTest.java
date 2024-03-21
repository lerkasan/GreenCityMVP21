package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.habit.HabitDto;
import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.entity.Habit;
import greencity.entity.HabitTranslation;
import greencity.entity.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
class HabitDtoMapperTest {

    private final HabitDtoMapper habitDtoMapper = new HabitDtoMapper();
    private Long id = 89L;
    private Habit habit = new Habit();
    private Language language = new Language();
    @Test
    void convert_HabitDtoMapperTest_ShouldMapCorrectly() {
        habit.setId(id);
        habit.setImage("Image");
        habit.setTags(new HashSet<>());
        HabitTranslation habitTranslation = new HabitTranslation(id, "Name", "", "", language,
                habit);

        HabitDto expected = HabitDto.builder()
                .id(id)
                .image("Image")
                .defaultDuration(null)
                .complexity(null)
                .habitTranslation(HabitTranslationDto.builder()
                        .description("")
                        .habitItem("")
                        .name("Name")
                        .languageCode(language.getCode())
                        .build())
                .tags(new ArrayList<>())
                .shoppingListItems(new ArrayList<>())
                .build();

        HabitDto actual = habitDtoMapper.convert(habitTranslation);

        assertNotNull(actual);
        assertEquals(habitTranslation.getId(), actual.getId());
        assertNull(actual.getDefaultDuration());
        assertNull(actual.getComplexity());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        HabitTranslation emptyHabitTranslation = new HabitTranslation();

        assertThrows(NullPointerException.class, () -> {
            habitDtoMapper.convert(emptyHabitTranslation);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_HabitDtoMapperTestWithNullSource_ShouldReturnNullPointerException(HabitTranslation nullHabitTranslation) {
        assertThrows(NullPointerException.class, () -> {
            habitDtoMapper.convert(nullHabitTranslation);
        });
    }
}