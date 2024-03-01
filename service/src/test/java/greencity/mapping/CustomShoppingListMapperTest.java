package greencity.mapping;

import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.entity.CustomShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void convert_CustomShoppingListMapperTest_ShouldMapToCategoryWithNullFields() {
        CustomShoppingListItemResponseDto customShoppingListItemResponseDto = new CustomShoppingListItemResponseDto();

        CustomShoppingListItem actual = customShoppingListMapper.convert(customShoppingListItemResponseDto);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getText());
        assertNull(actual.getStatus());
    }

    @Test
    void convert_CustomShoppingListMapperTest_ShouldReturnNullPointerException() {
        CustomShoppingListItemResponseDto customShoppingListItemResponseDto = null;

        assertThrows(NullPointerException.class, () -> {
            customShoppingListMapper.convert(customShoppingListItemResponseDto);
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
    void mapAllToList_WithValidInput_ShouldMapCorrectly() {
        CustomShoppingListItemResponseDto dto1 = new CustomShoppingListItemResponseDto();
        CustomShoppingListItemResponseDto dto2 = new CustomShoppingListItemResponseDto();
        List<CustomShoppingListItemResponseDto> dtoList = Arrays.asList(dto1, dto2);

        List<CustomShoppingListItem> actual = customShoppingListMapper.mapAllToList(dtoList);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void mapAllToList_WithNullInput_ShouldReturnEmptyList() {
        assertThrows(NullPointerException.class, () -> {
            customShoppingListMapper.mapAllToList(null);
        });
    }

    @Test
    void mapAllToList_WithEmptyInputList_ShouldReturnEmptyList() {
        List<CustomShoppingListItemResponseDto> dtoList = Arrays.asList();

        List<CustomShoppingListItem> actual = customShoppingListMapper.mapAllToList(dtoList);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
}