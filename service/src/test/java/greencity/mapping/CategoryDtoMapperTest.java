package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;
import greencity.dto.category.CategoryDto;
import greencity.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CategoryDtoMapperTest {
    
    private final ModelMapper modelMapper = new ModelMapper();
    private final CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapper();
    
    @Test
    void convert_CategoryDtoMapperTest_ShouldMapCorrectly() {
        CategoryDto categoryDto = new CategoryDto("Name");

        Category expected = Category.builder()
                .name(categoryDto.getName())
                .build();

        Category actual = categoryDtoMapper.convert(categoryDto);

        assertNotNull(actual);
        assertEquals(categoryDto.getName(), actual.getName());
        assertEquals(expected, actual);
    }

    @Test
    void convert_CategoryDtoMapperTest_ShouldMapToCategoryWithNullName() {
        CategoryDto categoryDto = new CategoryDto();

        Category actual = categoryDtoMapper.convert(categoryDto);

        assertNotNull(actual);
        assertNull(actual.getName());
    }

    @Test
    void convert_CategoryDtoMapperTest_ShouldReturnNullPointerException() {
        CategoryDto categoryDto = null;

        assertThrows(NullPointerException.class, () -> {
            categoryDtoMapper.convert(categoryDto);
        });
    }

    @Test
    void convert_CategoryDtoMapperTest_ShouldProduceSameResult() {
        CategoryDto categoryDto = new CategoryDto("Test");

        Category actual = categoryDtoMapper.convert(categoryDto);
        Category categoryByModelMapper = modelMapper.map(categoryDto, Category.class);

        assertEquals(categoryByModelMapper, actual);
    }
}
