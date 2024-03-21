package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.habit.HabitManagementDto;
import greencity.entity.Habit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
class HabitManagementDtoMapperTest {

    private final HabitManagementDtoMapper habitManagementDtoMapper = new HabitManagementDtoMapper();
    private Long id = 84L;
    private int complexity = 48;
    private int defaultDuration = 115;
    private String image = "image";

    @Test
    void convert_HabitManagementDtoMapperTest_ShouldMapCorrectly() {
        Habit habit = new Habit(id, image, complexity, defaultDuration, true,
                id,null, new ArrayList<>(), null, null, null);

        HabitManagementDto expected = HabitManagementDto.builder()
                .id(id)
                .image(image)
                .complexity(complexity)
                .defaultDuration(defaultDuration)
                .habitTranslations(new ArrayList<>())
                .build();

        HabitManagementDto actual = habitManagementDtoMapper.convert(habit);

        assertNotNull(actual);
        assertEquals(habit.getId(), actual.getId());
        assertEquals(habit.getImage(), actual.getImage());
        assertEquals(habit.getComplexity(), actual.getComplexity());
        assertEquals(habit.getDefaultDuration(), actual.getDefaultDuration());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitManagementDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        Habit emptyHabit= new Habit();

        assertThrows(NullPointerException.class, () -> {
            habitManagementDtoMapper.convert(emptyHabit);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_HabitManagementDtoMapperTestWithNullSource_ShouldReturnNullPointerException(Habit nullHabit) {
        assertThrows(NullPointerException.class, () -> {
            habitManagementDtoMapper.convert(nullHabit);
        });
    }
}