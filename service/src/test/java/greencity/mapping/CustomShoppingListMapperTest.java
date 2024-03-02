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
class CustomShoppingListMapperTest {

    private final ModelMapper modelMapper = new ModelMapper();
    private final CustomShoppingListMapper customShoppingListMapper = new CustomShoppingListMapper();

    @Test
    void convert_CustomShoppingListMapperTest_ShouldMapCorrectly() {
        CustomShoppingListItemResponseDto customShoppingListItemResponseDto = new CustomShoppingListItemResponseDto();
        customShoppingListItemResponseDto.setId(15L);
        customShoppingListItemResponseDto.setText("Text");
        customShoppingListItemResponseDto.setStatus(ShoppingListItemStatus.ACTIVE);

        CustomShoppingListItem expected = CustomShoppingListItem.builder()
                .id(customShoppingListItemResponseDto.getId())
                .text(customShoppingListItemResponseDto.getText())
                .status(customShoppingListItemResponseDto.getStatus())
                .build();

        CustomShoppingListItem actual = customShoppingListMapper.convert(customShoppingListItemResponseDto);

        assertNotNull(actual);
        assertEquals(customShoppingListItemResponseDto.getId(), actual.getId());
        assertEquals(customShoppingListItemResponseDto.getText(), actual.getText());
        assertEquals(customShoppingListItemResponseDto.getStatus(), actual.getStatus());
        assertEquals(expected, actual);
    }

    @Test
    void convert_CustomShoppingListMapperTest_ShouldMapWithNullFields() {
        CustomShoppingListItemResponseDto customShoppingListItemResponseDto = new CustomShoppingListItemResponseDto();

        CustomShoppingListItem actual = customShoppingListMapper.convert(customShoppingListItemResponseDto);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getText());
        assertNull(actual.getStatus());
    }

    @ParameterizedTest
    @NullSource
    void convert_CustomShoppingListMapperTest_ShouldReturnNullPointerException(CustomShoppingListItemResponseDto nullCustomShoppingListItemResponseDto) {
        assertThrows(NullPointerException.class, () -> {
            customShoppingListMapper.convert(nullCustomShoppingListItemResponseDto);
        });
    }

    @Test
    void convert_CustomShoppingListMapperTest_ShouldProduceSameResult() {
        CustomShoppingListItemResponseDto customShoppingListItemResponseDto = new CustomShoppingListItemResponseDto();
        customShoppingListItemResponseDto.setId(88L);
        customShoppingListItemResponseDto.setText("Test Text");

        CustomShoppingListItem actual = customShoppingListMapper.convert(customShoppingListItemResponseDto);
        CustomShoppingListItem expectedByModelMapper = modelMapper.map(customShoppingListItemResponseDto,
                CustomShoppingListItem.class);

        assertEquals(expectedByModelMapper, actual);
    }

    @Test
    void mapAllToList_CustomShoppingListMapperTest_ShouldMapCorrectly() {
        CustomShoppingListItemResponseDto dto1 = new CustomShoppingListItemResponseDto();
        CustomShoppingListItemResponseDto dto2 = new CustomShoppingListItemResponseDto();
        List<CustomShoppingListItemResponseDto> dtoList = Arrays.asList(dto1, dto2);

        List<CustomShoppingListItem> actual = customShoppingListMapper.mapAllToList(dtoList);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @ParameterizedTest
    @NullSource
    void mapAllToList_CustomShoppingListMapperTest_NullPointerException(List<CustomShoppingListItemResponseDto> nullList) {
        assertThrows(NullPointerException.class, () -> {
            customShoppingListMapper.mapAllToList(nullList);
        });
    }

    @ParameterizedTest
    @EmptySource
    void mapAllToList_CustomShoppingListMapperTest_ShouldReturnEmptyList(List<CustomShoppingListItemResponseDto> emptyList) {
        List<CustomShoppingListItem> actual = customShoppingListMapper.mapAllToList(emptyList);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
}