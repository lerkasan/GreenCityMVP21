package greencity.mapping;

import greencity.dto.habit.AddCustomHabitDtoRequest;
import greencity.entity.Habit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class CustomHabitMapperTest {
    private final ModelMapper modelMapper = new ModelMapper();
    private final CustomHabitMapper customHabitMapper = new CustomHabitMapper();

    @Test
    void convert_CustomHabitMapperTest_ShouldMapCorrectly() {
        AddCustomHabitDtoRequest addCustomHabitDtoRequest = new AddCustomHabitDtoRequest();
        addCustomHabitDtoRequest.setImage("SomeImage");
        addCustomHabitDtoRequest.setComplexity(555);
        addCustomHabitDtoRequest.setDefaultDuration(111);

        Habit expected = Habit.builder()
                .image(addCustomHabitDtoRequest.getImage())
                .complexity(addCustomHabitDtoRequest.getComplexity())
                .defaultDuration(addCustomHabitDtoRequest.getDefaultDuration())
                .isCustomHabit(true)
                .build();

        Habit actual = customHabitMapper.convert(addCustomHabitDtoRequest);

        assertNotNull(actual);
        assertEquals(addCustomHabitDtoRequest.getImage(), actual.getImage());
        assertEquals(addCustomHabitDtoRequest.getComplexity(), actual.getComplexity());
        assertEquals(addCustomHabitDtoRequest.getDefaultDuration(), actual.getDefaultDuration());
        assertEquals(expected, actual);
    }

    @Test
    void convert_CustomHabitMapperTest_ShouldMapToCategoryWithNullFields() {
        AddCustomHabitDtoRequest addCustomHabitDtoRequest = new AddCustomHabitDtoRequest();
        Habit actual = customHabitMapper.convert(addCustomHabitDtoRequest);

        assertNotNull(actual);
        assertNull(actual.getImage());
        assertNull(actual.getComplexity());
        assertNull(actual.getDefaultDuration());
    }

    @Test
    void convert_CustomHabitMapperTest_ShouldReturnNullPointerException() {
        AddCustomHabitDtoRequest addCustomHabitDtoRequest = null;

        assertThrows(NullPointerException.class, () -> {
            customHabitMapper.convert(addCustomHabitDtoRequest);
        });
    }

    @Test
    void convert_CustomHabitMapperTest_ShouldProduceSameResult() {
        AddCustomHabitDtoRequest addCustomHabitDtoRequest = new AddCustomHabitDtoRequest(22,
                44, null, "Image", null, null);

        Habit actual = customHabitMapper.convert(addCustomHabitDtoRequest);
        Habit expected = modelMapper.map(addCustomHabitDtoRequest, Habit.class);
        expected.setIsCustomHabit(true);

        assertEquals(expected, actual);
    }
}