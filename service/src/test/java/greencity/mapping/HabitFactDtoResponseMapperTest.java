package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.habit.HabitVO;
import greencity.dto.habitfact.HabitFactDtoResponse;
import greencity.dto.habitfact.HabitFactVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
class HabitFactDtoResponseMapperTest {

    private final HabitFactDtoResponseMapper habitFactDtoResponseMapper = new HabitFactDtoResponseMapper();
    private Long id = 89L;
    private HabitVO habit = new HabitVO();

    @Test
    void convert_HabitFactDtoResponseMapperTest_ShouldMapCorrectly() {
        HabitFactVO habitFactVO = new HabitFactVO(id, new ArrayList<>(), habit);

        HabitFactDtoResponse expected = HabitFactDtoResponse.builder()
                .id(id)
                .habit(habit)
                .translations(new ArrayList<>())
                .build();

        HabitFactDtoResponse actual = habitFactDtoResponseMapper.convert(habitFactVO);

        assertNotNull(actual);
        assertEquals(habitFactVO.getId(), actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitFactDtoResponseMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        HabitFactVO emptyHabitFactVO = new HabitFactVO();

        assertThrows(NullPointerException.class, () -> {
            habitFactDtoResponseMapper.convert(emptyHabitFactVO);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_HabitFactDtoResponseMapperTestWithNullSource_ShouldReturnNullPointerException(HabitFactVO nullHabitFactVO) {
        assertThrows(NullPointerException.class, () -> {
            habitFactDtoResponseMapper.convert(nullHabitFactVO);
        });
    }
}