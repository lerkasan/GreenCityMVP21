package greencity.controller;

import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.enums.ShoppingListItemStatus;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.CustomShoppingListItemNotSavedException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.CustomShoppingListItemService;

import lombok.SneakyThrows;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import greencity.ModelUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class)
public class CustomShoppingListItemControllerTest {
    private static final String controllerLink = "/custom/shopping-list-items";
    private MockMvc mockMvc;

    @InjectMocks
    private CustomShoppingListItemController controller;

    @Mock
    private CustomShoppingListItemService listItemService;

    @Mock
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new CustomExceptionHandler(errorAttributes,objectMapper))
                .build();
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void getAllAvailableListItems_withCorrectData_Ok(long userId, long habitId){
        var dtos = generateCustomShoppingListItemResponseDtos();

        when(listItemService.findAllAvailableCustomShoppingListItems(userId,habitId))
                .thenReturn(dtos);

        mockMvc.perform(get(controllerLink + "/{userId}/{habitId}",userId,habitId)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(writeAsString(dtos)));

        verify(listItemService).findAllAvailableCustomShoppingListItems(userId,habitId);
    }


    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void saveListItems_withCorrectData_Created(long userId, long habitId){
        var dto = ModelUtils.getBulkSaveCustomShoppingListItemDto();

        mockMvc.perform(post(controllerLink + "/{userId}/{habitAssignId}/custom-shopping-list-items"
                        ,userId,habitId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeAsString(dto)))
                .andExpect(status().isCreated());

        verify(listItemService).save(dto,userId,habitId);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void saveListItems_withWrongUserOrHabitId_NotFound(long userId, long habitId){
        var dto = ModelUtils.getBulkSaveCustomShoppingListItemDto();
        doThrow(NotFoundException.class).when(listItemService).save(dto,userId,habitId);

        mockMvc.perform(post(controllerLink + "/{userId}/{habitAssignId}/custom-shopping-list-items"
                        ,userId,habitId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertThat(r.getResolvedException())
                        .isInstanceOf(NotFoundException.class));

        verify(listItemService).save(dto,userId,habitId);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void saveListItems_withDuplicates_BadRequest(long userId, long habitId){
        var dto = ModelUtils.getBulkSaveCustomShoppingListItemDto();
        doThrow(CustomShoppingListItemNotSavedException.class)
                .when(listItemService).save(dto,userId,habitId);

        mockMvc.perform(post(controllerLink + "/{userId}/{habitAssignId}/custom-shopping-list-items"
                        ,userId,habitId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(r -> assertThat(r.getResolvedException())
                        .isInstanceOf(CustomShoppingListItemNotSavedException.class));

        verify(listItemService).save(dto,userId,habitId);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void saveListItems_withNoContent_BadRequest(long userId, long habitId){
        mockMvc.perform(post(controllerLink + "/{userId}/{habitAssignId}/custom-shopping-list-items"
                        ,userId,habitId))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void saveListItems_withBadContent_BadRequest(long userId, long habitId){
        var dto = ModelUtils.getBulkSaveCustomShoppingListItemDto();
        dto.getCustomShoppingListItemSaveRequestDtoList().get(0).setText("");

        mockMvc.perform(post(controllerLink + "/{userId}/{habitAssignId}/custom-shopping-list-items"
                        ,userId,habitId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(r -> assertThat(r.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }


    @SneakyThrows
    @ParameterizedTest
    @MethodSource("shoppingListItemStatusProvider")
    public void updateItemStatus_withCorrectData_Ok(String status){
        long userId = 1;
        long itemId = 2;
        var dto = ModelUtils.getCustomShoppingListItemResponseDto();
        dto.setStatus(ShoppingListItemStatus.valueOf(status));

        when(listItemService.updateItemStatus(userId,itemId,status))
                .thenReturn(dto);

        mockMvc.perform(patch(controllerLink + "/{userId}/custom-shopping-list-items",userId)
                        .param("itemId", valueOf(itemId))
                        .param("status",status)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(writeAsString(dto)));

        verify(listItemService).updateItemStatus(userId, itemId,status);
    }


    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,2"})
    public void updateItemStatus_withWrongStatus_BadRequest(long userId, long itemId){
        String status = "WrongStatus";

        when(listItemService.updateItemStatus(userId,itemId,status))
                .thenThrow(BadRequestException.class);

        mockMvc.perform(patch(controllerLink + "/{userId}/custom-shopping-list-items",userId)
                        .param("itemId", valueOf(itemId))
                        .param("status",status))
                .andExpect(status().isBadRequest())
                .andExpect(r -> assertThat(r.getResolvedException())
                        .isInstanceOf(BadRequestException.class));

        verify(listItemService).updateItemStatus(userId, itemId,status);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,2"})
    public void updateItemStatus_withWrongUserOrItemId_NotFound(long userId, long itemId){
        String status = "ACTIVE";
        when(listItemService.updateItemStatus(userId,itemId,status))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch(controllerLink + "/{userId}/custom-shopping-list-items",userId)
                        .param("itemId", valueOf(itemId))
                        .param("status",status))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertThat(r.getResolvedException())
                        .isInstanceOf(NotFoundException.class));

        verify(listItemService).updateItemStatus(userId, itemId,status);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void updateItemStatusToDone_withCorrectData_Ok(long userId, long itemId){
        mockMvc.perform(patch(controllerLink + "/{userId}/done",userId)
                        .param("itemId", valueOf(itemId)))
                .andExpect(status().isOk());

        verify(listItemService).updateItemStatusToDone(userId, itemId);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1,1"})
    public void updateItemStatusToDone_withWrongUserOrItemId_NotFound(long userId, long itemId){
        doThrow(NotFoundException.class).when(listItemService)
                .updateItemStatusToDone(userId, itemId);

        mockMvc.perform(patch(controllerLink + "/{userId}/done",userId)
                        .param("itemId", valueOf(itemId)))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertThat(r.getResolvedException())
                        .isInstanceOf(NotFoundException.class));

        verify(listItemService).updateItemStatusToDone(userId, itemId);
    }


    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1, '1,2,3,4'"})
    public void bulkDeleteListItems_withCorrectData_Ok(long userId,String ids){
        var longIds = convertToLongIds(ids);
        when(listItemService.bulkDelete(ids)).thenReturn(longIds);

        mockMvc.perform(delete(controllerLink + "/{userId}/custom-shopping-list-items",userId)
                        .param("ids", ids)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$",Matchers.hasSize(longIds.size())));

        verify(listItemService).bulkDelete(ids);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1, '1,2,3,4'"})
    public void bulkDeleteListItems_withIdWhichAreNotPresent_NotFound(long userId,String ids){
        when(listItemService.bulkDelete(ids)).thenThrow(NotFoundException.class);

        mockMvc.perform(delete(controllerLink + "/{userId}/custom-shopping-list-items",userId)
                        .param("ids", ids))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertThat(r.getResolvedException())
                        .isInstanceOf(NotFoundException.class));

        verify(listItemService).bulkDelete(ids);
    }


    @SneakyThrows
    @ParameterizedTest
    @MethodSource("shoppingListItemStatusProvider")
    public void getAllListItemsByStatus_WithCorrectData_Ok(String status){
        long userId = 1;
        var dtos = getCustomShoppingListItemResponseDtosByStatus(status);

        when(listItemService.findAllUsersCustomShoppingListItemsByStatus(userId,status))
                .thenReturn(dtos);

        mockMvc.perform(get(controllerLink + "/{userId}/custom-shopping-list-items",userId)
                        .param("status",status)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(writeAsString(dtos)));
        verify(listItemService).findAllUsersCustomShoppingListItemsByStatus(userId,status);
    }

    private List<CustomShoppingListItemResponseDto> getCustomShoppingListItemResponseDtosByStatus(String status) {
        return generateCustomShoppingListItemResponseDtos().stream()
                .filter(r -> r.getStatus().compareTo(ShoppingListItemStatus.valueOf(status)) == 0)
                .toList();
    }


    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1"})
    public void getAllListItemsByStatus_WithWrongOrNullStatus_Ok(long userId){
        var dtos = generateCustomShoppingListItemResponseDtos();
        when(listItemService.findAllUsersCustomShoppingListItemsByStatus(userId,null))
                .thenReturn(dtos);

        mockMvc.perform(get(controllerLink + "/{userId}/custom-shopping-list-items",userId)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(writeAsString(dtos)));

        verify(listItemService).findAllUsersCustomShoppingListItemsByStatus(userId,null);
    }



    private static Stream<String> shoppingListItemStatusProvider(){
        return Stream.of(ShoppingListItemStatus.values())
                .map(Enum::name);
    }

    private List<Long> convertToLongIds(String ids){
        return Arrays.stream(ids.split(","))
                .map(Long::valueOf)
                .toList();
    }

    @SneakyThrows
    private<T> String writeAsString(T element){
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(element);
    }


    private List<CustomShoppingListItemResponseDto> generateCustomShoppingListItemResponseDtos(){
        return List.of(new CustomShoppingListItemResponseDto(1L,"text1", ShoppingListItemStatus.ACTIVE),
                new CustomShoppingListItemResponseDto(2L,"text2", ShoppingListItemStatus.DONE),
                new CustomShoppingListItemResponseDto(2L,"text3", ShoppingListItemStatus.DISABLED),
                new CustomShoppingListItemResponseDto(3L,"text4", ShoppingListItemStatus.INPROGRESS));
    }
}