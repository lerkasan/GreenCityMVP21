package greencity.mapping;

import static org.junit.jupiter.api.Assertions.*;

import greencity.dto.category.CategoryDtoResponse;
import greencity.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
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
    void convert_CategoryDtoResponseMapperTest_ShouldMapWithNullNameAndNullId() {
        Category category = new Category();

        CategoryDtoResponse actual = categoryDtoResponseMapper.convert(category);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNull(actual.getName());
    }

    @ParameterizedTest
    @NullSource
    void convert_CategoryDtoResponseMapperTest_ShouldReturnNullPointerException(Category category) {
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
