package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.user.UserFilterDtoRequest;
import greencity.entity.Filter;
import greencity.enums.FilterType;
import greencity.enums.Role;
import greencity.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class FilterDtoRequestMapperTest {

    private final FilterDtoRequestMapper filterDtoRequestMapper = new FilterDtoRequestMapper();

    @Test
    void convert_FilterDtoRequestMapperTest_ShouldMapCorrectly() {
        UserFilterDtoRequest filterUserDto = new UserFilterDtoRequest("Name", "id",
                UserStatus.ACTIVATED.toString(), Role.ROLE_USER.toString());

        Filter expected = Filter.builder()
                .name(filterUserDto.getName())
                .type(FilterType.USERS.toString())
                .values(String.join(";",
                        String.valueOf(filterUserDto.getSearchCriteria()),
                        String.valueOf(filterUserDto.getUserRole()),
                        String.valueOf(filterUserDto.getUserStatus())))
                .build();

        Filter actual = filterDtoRequestMapper.convert(filterUserDto);

        assertNotNull(actual);
        assertEquals(filterUserDto.getName(), actual.getName());
        assertEquals(expected, actual);
    }

    @Test
    void convert_FilterDtoRequestMapperTestWithEmptySource_ShouldMapWithNullFields() {
        UserFilterDtoRequest emptyFilterUserDto = new UserFilterDtoRequest();

        assertThrows(NullPointerException.class, () -> {
            filterDtoRequestMapper.convert(emptyFilterUserDto);
        });
    }

    @ParameterizedTest
    @NullSource
    void convert_FilterDtoRequestMapperTestWithNullSource_ShouldReturnNullPointerException(UserFilterDtoRequest nullFilterUserDto) {
        assertThrows(NullPointerException.class, () -> {
            filterDtoRequestMapper.convert(nullFilterUserDto);
        });
    }
}
