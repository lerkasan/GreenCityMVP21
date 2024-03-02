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
import java.util.List;

@ExtendWith(SpringExtension.class)
class CustomShoppingListResponseDtoMapperTest {

    private final ModelMapper modelMapper = new ModelMapper();
    private final CustomShoppingListResponseDtoMapper customShoppingListResponseDtoMapper = new CustomShoppingListResponseDtoMapper();

    @Test
    void convert_CustomShoppingListResponseDtoMapperTest_ShouldMapCorrectly() {
        CustomShoppingListItem customShoppingListItem = new CustomShoppingListItem();
        customShoppingListItem.setId(12L);
        customShoppingListItem.setText("SomeText");
        customShoppingListItem.setStatus(ShoppingListItemStatus.ACTIVE);

        CustomShoppingListItemResponseDto expected = CustomShoppingListItemResponseDto.builder()
                .id(customShoppingListItem.getId())
                .text(customShoppingListItem.getText())
                .status(customShoppingListItem.getStatus())
                .build();

        CustomShoppingListItemResponseDto actual = customShoppingListResponseDtoMapper.convert(customShoppingListItem);

        assertNotNull(actual);
        assertEquals(customShoppingListItem.getId(), actual.getId());
        assertEquals(customShoppingListItem.getText(), actual.getText());
        assertEquals(customShoppingListItem.getStatus(), actual.getStatus());
        assertEquals(expected, actual);
    }

    @Test
    void convert_CustomShoppingListResponseDtoMapperTest_ShouldMapWithNullFields() {
        CustomShoppingListItem customShoppingListItem = new CustomShoppingListItem();

        CustomShoppingListItemResponseDto actual = customShoppingListResponseDtoMapper.convert(customShoppingListItem);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getText());
    }

    @ParameterizedTest
    @NullSource
    void convert_CustomShoppingListResponseDtoMapperTest_ShouldReturnNullPointerException(
            CustomShoppingListItem customShoppingListItem) {
        assertThrows(NullPointerException.class, () -> {
            customShoppingListResponseDtoMapper.convert(customShoppingListItem);
        });
    }

    @Test
    void convert_CustomShoppingListResponseDtoMapperTest_ShouldProduceSameResult() {
        CustomShoppingListItem customShoppingListItem = new CustomShoppingListItem();
        customShoppingListItem.setId(45L);
        customShoppingListItem.setStatus(ShoppingListItemStatus.DONE);

        CustomShoppingListItemResponseDto actual = customShoppingListResponseDtoMapper.convert(customShoppingListItem);
        CustomShoppingListItemResponseDto expectedByModelMapper = modelMapper.map(customShoppingListItem,
                CustomShoppingListItemResponseDto.class);

        assertEquals(expectedByModelMapper, actual);
    }

    @Test
    void mapAllToList_CustomShoppingListResponseDtoMapperTest_ShouldMapCorrectly() {
        CustomShoppingListItem dto1 = new CustomShoppingListItem();
        CustomShoppingListItem dto2 = new CustomShoppingListItem();
        List<CustomShoppingListItem> dtoList = Arrays.asList(dto1, dto2);

        List<CustomShoppingListItemResponseDto> actual = customShoppingListResponseDtoMapper.mapAllToList(dtoList);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @ParameterizedTest
    @NullSource
    void mapAllToList_CustomShoppingListResponseDtoMapperTest_NullPointerException(List<CustomShoppingListItem> nullList) {
        assertThrows(NullPointerException.class, () -> {
            customShoppingListResponseDtoMapper.mapAllToList(nullList);
        });
    }

    @ParameterizedTest
    @EmptySource
    void mapAllToList_CustomShoppingListResponseDtoMapperTest_ShouldReturnEmptyList(List<CustomShoppingListItem> emptyList) {
        List<CustomShoppingListItemResponseDto> actual = customShoppingListResponseDtoMapper.mapAllToList(emptyList);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
}