package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;
import greencity.dto.category.CategoryDtoResponse;
import greencity.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CategoryDtoResponseMapperTest {
    
    private final ModelMapper modelMapper = new ModelMapper();
    private final CategoryDtoResponseMapper categoryDtoResponseMapper = new CategoryDtoResponseMapper();
    
    @Test
    void convert_CategoryDtoResponseMapperTest_ShouldMapCorrectly() {
        Category category = new Category(55L,"Name", "Ім'я", null, null);

        CategoryDtoResponse expected = CategoryDtoResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();

        CategoryDtoResponse actual = categoryDtoResponseMapper.convert(category);

        assertNotNull(actual);
        assertEquals(category.getId(), actual.getId());
        assertEquals(category.getName(), actual.getName());
        assertEquals(expected, actual);
    }

    @Test
    void convert_CategoryDtoResponseMapperTest_ShouldMapToCategoryWithNullNameAndNullId() {
        Category category = new Category();

        CategoryDtoResponse actual = categoryDtoResponseMapper.convert(category);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getName());
    }

    @Test
    void convert_CategoryDtoResponseMapperTest_ShouldReturnNullPointerException() {
        Category category = null;

        assertThrows(NullPointerException.class, () -> {
            categoryDtoResponseMapper.convert(category);
        });
    }

    @Test
    void convert_CategoryDtoResponseMapperTest_ShouldProduceSameResult() {
        Category category = new Category();
        category.setId(77L);
        category.setName("Some Name");

        CategoryDtoResponse actual = categoryDtoResponseMapper.convert(category);
        CategoryDtoResponse categoryByModelMapper = modelMapper.map(category, CategoryDtoResponse.class);

        assertEquals(categoryByModelMapper, actual);
    }
}
