package greencity.filters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import greencity.entity.ShoppingListItem;
import greencity.entity.ShoppingListItem_;
import greencity.entity.localization.ShoppingListItemTranslation;
import greencity.entity.localization.ShoppingListItemTranslation_;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemSpecificationTest {

    @Mock
    private CriteriaBuilder criteriaBuilderMock;
    @Mock
    private CriteriaQuery<ShoppingListItemTranslation> criteriaQueryMock;
    @Mock
    private Root<ShoppingListItem> shoppingListItemRootMock;
    @Mock
    private Root<ShoppingListItemTranslation> shoppingListItemTranslationRootMock;
    @Mock
    private Predicate allPredicateMock;
    @Mock
    private Path<Object> pathShoppingListItemContentMock;
    @Mock
    private Predicate andContentPredicate;
    @Mock
    private Predicate andIdPredicate;
    @Mock
    private SearchCriteria searchCriteriaForAll;
    @Mock
    private SingularAttribute<ShoppingListItem, Long> id;
    @Mock
    private SingularAttribute<ShoppingListItemTranslation, ShoppingListItem> singularAttribute;
    private ShoppingListItemSpecification shoppingListItemSpecification;
    private ShoppingListItemSpecification shoppingListItemSpecificationForIdCriteria;
    private List<SearchCriteria> criteriaList;
    private List<SearchCriteria> criteriaListForIdCriteria;
    private String content = "example";

    @BeforeEach
    void setUp() {
        criteriaList = new ArrayList<>();
        criteriaList.add(SearchCriteria.builder()
                .key(ShoppingListItemTranslation_.CONTENT)
                .type(ShoppingListItemTranslation_.CONTENT)
                .value(content)
                .build());
        ShoppingListItem_.id = id;
        ShoppingListItemTranslation_.shoppingListItem = singularAttribute;
        shoppingListItemSpecification = new ShoppingListItemSpecification(criteriaList);
    }

    @Test
    void toPredicate_ShoppingListItemSpecificationTest_shouldReturnContentPredicate() {
        //checking for CONTENT criteria
        when(criteriaBuilderMock.conjunction()).thenReturn(allPredicateMock);
        when(criteriaBuilderMock.and(eq(allPredicateMock), eq(andContentPredicate))).thenReturn(andContentPredicate);

        //checking when in getTranslationPredicate searchCriteria.getValue().toString().trim().equals("") is false
        when(criteriaQueryMock.from(ShoppingListItemTranslation.class)).thenReturn(shoppingListItemTranslationRootMock);
        lenient().when(searchCriteriaForAll.getValue()).thenReturn(content);
        when(shoppingListItemTranslationRootMock.get(any(SingularAttribute.class))).thenReturn(pathShoppingListItemContentMock);
        when(criteriaBuilderMock.and(
                criteriaBuilderMock.like(shoppingListItemTranslationRootMock.get(content),
                        "%" + criteriaList.get(0).getValue() + "%"),criteriaBuilderMock.equal(
                        shoppingListItemTranslationRootMock.get(ShoppingListItemTranslation_.shoppingListItem).get(ShoppingListItem_.id),
                        shoppingListItemRootMock.get(ShoppingListItem_.id)
                ))).thenReturn(andContentPredicate);

        Predicate actual = shoppingListItemSpecification.toPredicate(shoppingListItemRootMock, criteriaQueryMock, criteriaBuilderMock);

        verify(criteriaBuilderMock).and(allPredicateMock, andContentPredicate);
        verify(shoppingListItemRootMock, never()).get(ShoppingListItem_.ID);

        assertEquals(andContentPredicate,actual);
    }

    @Test
    void toPredicate_ShoppingListItemSpecificationTest_shouldReturnIdPredicate() {
        //checking for ID criteria
        criteriaListForIdCriteria = new ArrayList<>();
        criteriaListForIdCriteria.add(SearchCriteria.builder()
                .key(ShoppingListItem_.ID)
                .type(ShoppingListItem_.ID)
                .value(1L)
                .build());
        shoppingListItemSpecificationForIdCriteria = new ShoppingListItemSpecification(criteriaListForIdCriteria);

        when(criteriaBuilderMock.conjunction()).thenReturn(allPredicateMock);
        when(criteriaBuilderMock.and(eq(allPredicateMock), any())).thenReturn(andIdPredicate);

        Predicate actual = shoppingListItemSpecificationForIdCriteria.toPredicate(shoppingListItemRootMock, criteriaQueryMock, criteriaBuilderMock);

        verify(criteriaBuilderMock).and(eq(allPredicateMock), any());
        verify(shoppingListItemRootMock).get(ShoppingListItem_.ID);
        verify(shoppingListItemRootMock, never()).get(ShoppingListItemTranslation_.CONTENT);

        assertEquals(andIdPredicate,actual);
    }

    @Test
    void getTranslationPredicate_ShoppingListItemSpecificationTest_shouldReturnAllPredicates() {
        //checking when in getTranslationPredicate searchCriteria.getValue().toString().trim().equals("") is true
        when(criteriaQueryMock.from(ShoppingListItemTranslation.class)).thenReturn(shoppingListItemTranslationRootMock);
        when(searchCriteriaForAll.getValue()).thenReturn("");
        when(criteriaBuilderMock.conjunction()).thenReturn(allPredicateMock);

        Predicate actual = shoppingListItemSpecification.getTranslationPredicate(shoppingListItemRootMock, criteriaQueryMock, criteriaBuilderMock, searchCriteriaForAll);

        verify(criteriaBuilderMock).conjunction();
        verify(criteriaBuilderMock, never()).and(any(), any());

        assertEquals(allPredicateMock,actual);
    }
}