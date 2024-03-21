package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.entity.CustomShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
class UtilsMapperTest {
    private final ModelMapper modelMapper = new ModelMapper();
    private CustomShoppingListItemResponseDto source1 = new CustomShoppingListItemResponseDto(
            166L, "Text", ShoppingListItemStatus.ACTIVE);
    private CustomShoppingListItemResponseDto source2 = new CustomShoppingListItemResponseDto(
            167L, "TestText", ShoppingListItemStatus.DONE);
    private List<CustomShoppingListItemResponseDto> list = Arrays.asList(source1, source2, source2);

    @Test
    void map_UtilsMapperTest_ShouldMapCorrectly() {
        CustomShoppingListItem expectedByModelMapper = modelMapper.map(source1, CustomShoppingListItem.class);
        CustomShoppingListItem actual = UtilsMapper.map(source1, CustomShoppingListItem.class);

        assertEquals(source1.getId(), actual.getId());
        assertEquals(source1.getText(), actual.getText());
        assertEquals(source1.getStatus(), actual.getStatus());
        assertEquals(expectedByModelMapper, actual);
    }

    @Test
    void map_UtilsMapperTestWithEmptySource_ShouldMapWithNullFields() {
        CustomShoppingListItemResponseDto emptySource = new CustomShoppingListItemResponseDto();

        CustomShoppingListItem actual = UtilsMapper.map(emptySource, CustomShoppingListItem.class);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getText());
    }

    @ParameterizedTest
    @NullSource
    void map_UtilsMapperTestWithNullSource_ShouldReturnIllegalArgumentException(CustomShoppingListItemResponseDto nullSource) {
        assertThrows(IllegalArgumentException.class, () -> {
            UtilsMapper.map(nullSource, CustomShoppingListItem.class);
        });
    }

    @Test
    void mapAllToList_UtilsMapperTest_ShouldMapCorrectly() {
        List<CustomShoppingListItem> actual = UtilsMapper.mapAllToList(list, CustomShoppingListItem.class);

        assertNotNull(actual);
        assertEquals(3, actual.size());
    }
    @ParameterizedTest
    @EmptySource
    void mapAllToList_UtilsMapperTestWithEmptySource_ShouldReturnEmptyList(List<CustomShoppingListItemResponseDto> emptyList) {
        List<CustomShoppingListItem> actual = UtilsMapper.mapAllToList(emptyList, CustomShoppingListItem.class);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
    @ParameterizedTest
    @NullSource
    void mapAllToList_UtilsMapperTestWithNullSource_NullPointerException(List<CustomShoppingListItemResponseDto> nullList) {
        assertThrows(NullPointerException.class, () -> {
            UtilsMapper.mapAllToList(nullList, CustomShoppingListItem.class);
        });
    }

    @Test
    void mapAllToSet_UtilsMapperTest_ShouldMapCorrectly() {
        Set<CustomShoppingListItemResponseDto> set = new HashSet<>(list);

        Set<CustomShoppingListItem> actual = UtilsMapper.mapAllToSet(set, CustomShoppingListItem.class);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @ParameterizedTest
    @EmptySource
    void mapAllToSet_UtilsMapperTestWithEmptySource_ShouldReturnEmptySet(Set<CustomShoppingListItemResponseDto> emptySet) {
        Set<CustomShoppingListItem> actual = UtilsMapper.mapAllToSet(emptySet, CustomShoppingListItem.class);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
    @ParameterizedTest
    @NullSource
    void mapAllToSet_UtilsMapperTestWithNullSource_NullPointerException(Set<CustomShoppingListItemResponseDto> nullSet) {
        assertThrows(NullPointerException.class, () -> {
            UtilsMapper.mapAllToSet(nullSet, CustomShoppingListItem.class);
        });
    }
}