package greencity.service;

import greencity.entity.Habit;
import greencity.entity.ShoppingListItem;
import greencity.repository.HabitRepo;
import greencity.repository.ShoppingListItemRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitShoppingListItemServiceImplTest {

    @Mock
    private ShoppingListItemRepo shoppingListItemRepo;

    @Mock
    private HabitRepo habitRepo;

    @InjectMocks
    private HabitShoppingListItemServiceImpl habitShoppingListItemService;

    @Test
    void unlinkShoppingListItemsFromHabit_whenShopIdsAndHabitIdValid_shouldHaveCorrectFlow() {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        Set<ShoppingListItem> shoppingListItems = new HashSet<>();
        shoppingListItems.add(shoppingListItem);

        Habit habit = new Habit();
        habit.setShoppingListItems(shoppingListItems);

        Long habitId = 1L;
        Long shoppingId = 1L;
        List<Long> shoppingListIds = List.of(shoppingId);

        when(habitRepo.findById(habitId)).thenReturn(Optional.of(habit));
        when(shoppingListItemRepo.findById(shoppingId)).thenReturn(Optional.of(shoppingListItem));

        assertDoesNotThrow(() -> habitShoppingListItemService.unlinkShoppingListItems(shoppingListIds, habitId));

        verify(habitRepo).findById(habitId);
        verify(shoppingListItemRepo).findById(shoppingId);
        verify(habitRepo).save(habit);
    }

    @Test
    void unlinkShoppingListItemsFromHabit_whenHabitRepoReturnEmptyOptional_throwNoSuchElementException() {
        Long habitId = 1L;
        Long shoppingId = 1L;
        List<Long> shoppingListIds = List.of(shoppingId);

        when(habitRepo.findById(habitId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
            () -> habitShoppingListItemService.unlinkShoppingListItems(shoppingListIds, habitId));

        verify(habitRepo).findById(habitId);
        verifyNoInteractions(shoppingListItemRepo);
        verifyNoMoreInteractions(habitRepo);
    }

    @Test
    void unlinkShoppingListItemsFromHabit_whenShoppingListItemRepoReturnEmptyOptional_throwNoSuchElementException() {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        Set<ShoppingListItem> shoppingListItems = new HashSet<>();
        shoppingListItems.add(shoppingListItem);

        Habit habit = new Habit();
        habit.setShoppingListItems(shoppingListItems);

        Long habitId = 1L;
        Long shoppingId = 1L;
        List<Long> shoppingListIds = List.of(shoppingId);

        when(habitRepo.findById(habitId)).thenReturn(Optional.of(habit));
        when(shoppingListItemRepo.findById(shoppingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> habitShoppingListItemService.unlinkShoppingListItems(shoppingListIds, habitId));

        verify(habitRepo).findById(habitId);
        verify(shoppingListItemRepo).findById(shoppingId);
        verifyNoMoreInteractions(habitRepo, shoppingListItemRepo);
    }
}
