package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.user.UserFilterDtoResponse;
import greencity.entity.Filter;
import greencity.entity.User;
import greencity.enums.FilterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class FilterDtoResponseMapperTest {

    private final FilterDtoResponseMapper filterDtoResponseMapper = new FilterDtoResponseMapper();

    @Test
    void convert_FilterDtoResponseMapperTest_ShouldMapCorrectly() {
        Filter filter = new Filter(159L, new User(), "Name", FilterType.USERS.toString(), "id; ROLE_USER; ACTIVATED");
        String[] criterias = filter.getValues().split(";");

        UserFilterDtoResponse expected = UserFilterDtoResponse
                .builder()
                .id(filter.getId())
                .name(filter.getName())
                .searchCriteria(criterias[0])
                .userRole(criterias[1])
                .userStatus(criterias[2])
                .build();

        UserFilterDtoResponse actual = filterDtoResponseMapper.convert(filter);

        assertNotNull(actual);
        assertEquals(filter.getName(), actual.getName());
        assertEquals(expected, actual);
    }

    @Test
    void convert_FilterDtoResponseMapperTestWithEmptySource_ShouldMapWithNullFields() {
        Filter emptyFilter = new Filter();

        assertThrows(NullPointerException.class, () -> {
            filterDtoResponseMapper.convert(emptyFilter);;
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_FilterDtoResponseMapperTestWithNullSource_ShouldReturnNullPointerException(Filter nullFilter) {
        assertThrows(NullPointerException.class, () -> {
            filterDtoResponseMapper.convert(nullFilter);
        });
    }
}