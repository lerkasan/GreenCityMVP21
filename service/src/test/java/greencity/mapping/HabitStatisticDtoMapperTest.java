package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.habitstatistic.HabitStatisticDto;
import greencity.entity.HabitAssign;
import greencity.entity.HabitStatistic;
import greencity.enums.HabitRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.ZonedDateTime;

@ExtendWith(SpringExtension.class)
class HabitStatisticDtoMapperTest {

    private final HabitStatisticDtoMapper habitStatisticDtoMapper = new HabitStatisticDtoMapper();
    private Long id = 89L;
    private ZonedDateTime currentDate = ZonedDateTime.now();
    private HabitRate habitRate = HabitRate.GOOD;
    private int amountOfItems = 15;

    @Test
    void convert_HabitStatisticDtoMapperTest_ShouldMapCorrectly() {
        HabitStatistic habitStatistic = new HabitStatistic(id, habitRate, currentDate, amountOfItems,
                new HabitAssign());

        HabitStatisticDto expected = HabitStatisticDto.builder()
                .id(id)
                .amountOfItems(amountOfItems)
                .createDate(currentDate)
                .habitRate(habitRate)
                .habitAssignId(null)
                .build();

        HabitStatisticDto actual = habitStatisticDtoMapper.convert(habitStatistic);

        assertNotNull(actual);
        assertEquals(habitStatistic.getId(), actual.getId());
        assertEquals(habitStatistic.getCreateDate(), actual.getCreateDate());
        assertEquals(habitStatistic.getHabitRate(), actual.getHabitRate());
        assertEquals(habitStatistic.getAmountOfItems(), actual.getAmountOfItems());
        assertNull(actual.getHabitAssignId());
        assertEquals(expected, actual);
    }

    @Test
    void convert_HabitStatisticDtoMapperTestWithEmptySource_ShouldReturnNullPointerException() {
        HabitStatistic emptyHabitStatistic = new HabitStatistic();

        assertThrows(NullPointerException.class, () -> {
            habitStatisticDtoMapper.convert(emptyHabitStatistic);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_HabitStatisticDtoMapperTestWithNullSource_ShouldReturnNullPointerException(
            HabitStatistic nullHabitStatistic) {
        assertThrows(NullPointerException.class, () -> {
            habitStatisticDtoMapper.convert(nullHabitStatistic);
        });
    }
}