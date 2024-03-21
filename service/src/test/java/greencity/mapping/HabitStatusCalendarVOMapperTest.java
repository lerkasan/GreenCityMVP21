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
class HabitStatusCalendarVOMapperTest {

    private final HabitStatusCalendarVOMapper habitStatusCalendarVOMapper = new HabitStatusCalendarVOMapper();
    private Long id = 89L;
    private LocalDate currentDate = LocalDate.now();
    private HabitAssign habitAssign = new HabitAssign();

    @Test
    void convert_HabitStatusCalendarVOMapperTest_ShouldMapCorrectly() {
        HabitStatusCalendar habitStatusCalendar = new HabitStatusCalendar(id, currentDate, habitAssign);

        HabitStatusCalendarVO expected = HabitStatusCalendarVO.builder()
                .id(id)
                .enrollDate(currentDate)
                .habitAssignVO(HabitAssignVO.builder()
                        .id(habitAssign.getId())
                        .build())
                .build();

        HabitStatusCalendarVO actual = habitStatusCalendarVOMapper.convert(habitStatusCalendar);

        assertNotNull(actual);
        assertEquals(habitStatusCalendar.getId(), actual.getId());
        assertEquals(habitStatusCalendar.getEnrollDate(), actual.getEnrollDate());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitStatusCalendarVOMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        HabitStatusCalendar emptyHabitStatusCalendar = new HabitStatusCalendar();

        assertThrows(NullPointerException.class, () -> {
            habitStatusCalendarVOMapper.convert(emptyHabitStatusCalendar);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_HabitStatusCalendarVOMapperTestWithNullSource_ShouldReturnNullPointerException(
            HabitStatusCalendar nullHabitStatusCalendar) {
        assertThrows(NullPointerException.class, () -> {
            habitStatusCalendarVOMapper.convert(nullHabitStatusCalendar);
        });
    }
}