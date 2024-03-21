package greencity.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.SerializationFeature;
import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.dto.shoppinglistitem.ShoppingListItemRequestDto;
import greencity.dto.user.UserShoppingListItemResponseDto;
import greencity.dto.user.UserVO;
import greencity.enums.ShoppingListItemStatus;
import greencity.service.ShoppingListItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.validation.Validator;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemControllerTest {
    private static final String shoppingListItemLink = "/user/shopping-list-items";
    private MockMvc mockMvc;
    @Mock
    private ShoppingListItemService shoppingListItemService;
    @Mock
    private Validator mockValidator;
    @InjectMocks
    private ShoppingListItemController shoppingListItemController;
    private Long id = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Locale locale = Locale.ENGLISH;
    private UserShoppingListItemResponseDto userShoppingListItemResponseDto = new UserShoppingListItemResponseDto();
    private UserVO userVO= new UserVO();

    @BeforeEach
    void setUp() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mockMvc = MockMvcBuilders.standaloneSetup(shoppingListItemController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setValidator(mockValidator)
                .build();
    }

    @Test
    void saveUserShoppingListItemsWithoutLanguageParam_ShoppingListItemControllerTest_shouldReturnIsCreatedStatus()
            throws Exception {
        List<ShoppingListItemRequestDto> shoppingListItems = Arrays.asList(
                new ShoppingListItemRequestDto(2L),
                new ShoppingListItemRequestDto(3L),
                new ShoppingListItemRequestDto(1L)
        );

        when(shoppingListItemService.saveUserShoppingListItems(any(), anyLong(), any(), any()))
                .thenReturn(List.of(userShoppingListItemResponseDto));

        mockMvc.perform(post(shoppingListItemLink)
                        .param("habitId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoppingListItems)))
                .andExpect(status().isCreated());

        verify(shoppingListItemService).saveUserShoppingListItems(any(), eq(id), any(), any());
    }

    @Test
    void getShoppingListItemsAssignedToUserWithLanguageParam_ShoppingListItemControllerTest_shouldReturnIsOkStatus()
            throws Exception {
        when(shoppingListItemService.getUserShoppingList(any(), anyLong(), anyString()))
                .thenReturn(List.of(userShoppingListItemResponseDto));

        mockMvc.perform(get(shoppingListItemLink + "/habits/{habitId}/shopping-list", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                        .content(objectMapper.writeValueAsString(locale)))
                .andExpect(status().isOk());

        verify(shoppingListItemService).getUserShoppingList(any(), eq(id), eq(locale.getLanguage()));
    }

    @Test
    void getShoppingListItemsAssignedToUserWithoutLanguageParam_ShoppingListItemControllerTest_shouldReturnIsOkStatus()
            throws Exception {
        when(shoppingListItemService.getUserShoppingList(any(), anyLong(), anyString()))
                .thenReturn(List.of(userShoppingListItemResponseDto));

        mockMvc.perform(get(shoppingListItemLink + "/habits/{habitId}/shopping-list", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO)))
                .andExpect(status().isOk());

        verify(shoppingListItemService).getUserShoppingList(any(), eq(id), anyString());
    }

    @Test
    void delete_ShoppingListItemControllerTest_shouldReturnIsOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(shoppingListItemLink)
                        .param("habitId", "1")
                        .param("shoppingListItemId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO)))
                .andExpect(status().isOk());

        verify(shoppingListItemService).deleteUserShoppingListItemByItemIdAndUserIdAndHabitId(eq(2L), any(), eq(id));
    }

    @Test
    void updateUserShoppingListItemStatusWithoutLanguage_ShoppingListItemControllerTest_shouldReturnIsCreatedStatus()
            throws Exception {
        userShoppingListItemResponseDto.setStatus(ShoppingListItemStatus.DONE);
        when(shoppingListItemService.updateUserShopingListItemStatus(any(), anyLong(), anyString()))
                .thenReturn(userShoppingListItemResponseDto);

        mockMvc.perform(patch(shoppingListItemLink + "/{userShoppingListItemId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                )
                .andExpect(status().isCreated());

        verify(shoppingListItemService).updateUserShopingListItemStatus(any(), eq(id), anyString());
    }

    @Test
    void updateUserShoppingListItemStatusWithLanguageParam_ShoppingListItemControllerTest_shouldReturnIsCreatedStatus()
            throws Exception {
        userShoppingListItemResponseDto.setStatus(ShoppingListItemStatus.DONE);
        when(shoppingListItemService.updateUserShopingListItemStatus(any(), anyLong(), anyString()))
                .thenReturn(userShoppingListItemResponseDto);

        mockMvc.perform(patch(shoppingListItemLink + "/{userShoppingListItemId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                        .content(objectMapper.writeValueAsString(locale.getLanguage())))
                .andExpect(status().isCreated());

        verify(shoppingListItemService).updateUserShopingListItemStatus(any(),  eq(id), eq(locale.getLanguage()));
    }

    @Test
    void updateUserShoppingListItemStatus_ShoppingListItemControllerTest_shouldReturnIsOkStatus() throws Exception {
        userShoppingListItemResponseDto.setStatus(ShoppingListItemStatus.ACTIVE);
        when(shoppingListItemService.updateUserShoppingListItemStatus(any(), anyLong(), anyString(), anyString()))
                .thenReturn(List.of(userShoppingListItemResponseDto));

        mockMvc.perform(patch(shoppingListItemLink + "/{userShoppingListItemId}/status/{status}",id, ShoppingListItemStatus.ACTIVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                        .content(objectMapper.writeValueAsString(locale))
                )
                .andExpect(status().isOk());

        verify(shoppingListItemService).updateUserShoppingListItemStatus(any(), eq(id), eq(locale.getLanguage()),
                eq(ShoppingListItemStatus.ACTIVE.toString()));
    }

    @Test
    void bulkDeleteUserShoppingListItems_ShoppingListItemControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(shoppingListItemService.deleteUserShoppingListItems(anyString()))
                .thenReturn(List.of(id));

        mockMvc.perform(MockMvcRequestBuilders.delete(shoppingListItemLink + "/user-shopping-list-items")
                        .param("ids", "1,2,3"))
                .andExpect(status().isOk());

        verify(shoppingListItemService).deleteUserShoppingListItems(eq("1,2,3"));
    }

    @Test
    void findInProgressByUserId_ShoppingListItemControllerTest_shouldReturnIsOkStatus() throws Exception {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        when(shoppingListItemService.findInProgressByUserIdAndLanguageCode(anyLong(), anyString()))
                .thenReturn(List.of(shoppingListItemDto));

        mockMvc.perform(get(shoppingListItemLink + "/{userId}/get-all-inprogress", id)
                        .param("lang", "en"))
                .andExpect(status().isOk());

        verify(shoppingListItemService).findInProgressByUserIdAndLanguageCode(eq(id), eq(locale.getLanguage()));
    }
}