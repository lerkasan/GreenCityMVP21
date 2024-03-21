package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.habit.HabitAssignVO;
import greencity.dto.habitstatuscalendar.HabitStatusCalendarVO;
import greencity.entity.HabitAssign;
import greencity.entity.HabitStatusCalendar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
class HabitStatusCalendarMapperTest {

    private final HabitStatusCalendarMapper habitStatusCalendarMapper = new HabitStatusCalendarMapper();
    private Long id = 89L;
    private LocalDate currentDate = LocalDate.now();
    private HabitAssignVO habitAssignVO = new HabitAssignVO();

    @Test
    void convert_HabitStatusCalendarMapperTest_ShouldMapCorrectly() {
        HabitStatusCalendarVO habitStatusCalendarVO = new HabitStatusCalendarVO(id, currentDate, habitAssignVO);

        HabitStatusCalendar expected = HabitStatusCalendar.builder()
                .id(id)
                .enrollDate(currentDate)
                .habitAssign(HabitAssign.builder()
                        .id(habitAssignVO.getId())
                        .build())
                .build();

        HabitStatusCalendar actual = habitStatusCalendarMapper.convert(habitStatusCalendarVO);

        assertNotNull(actual);
        assertEquals(habitStatusCalendarVO.getId(), actual.getId());
        assertEquals(habitStatusCalendarVO.getEnrollDate(), actual.getEnrollDate());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitStatusCalendarMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        HabitStatusCalendarVO emptyHabitStatusCalendarVO = new HabitStatusCalendarVO();

        assertThrows(NullPointerException.class, () -> {
            habitStatusCalendarMapper.convert(emptyHabitStatusCalendarVO);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_HabitStatusCalendarMapperTestWithNullSource_ShouldReturnNullPointerException(
            HabitStatusCalendarVO nullHabitStatusCalendarVO) {
        assertThrows(NullPointerException.class, () -> {
            habitStatusCalendarMapper.convert(nullHabitStatusCalendarVO);
        });
    }
}