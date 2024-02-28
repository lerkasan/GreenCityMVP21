package greencity.controller;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.SerializationFeature;
import greencity.dto.habit.*;
import greencity.enums.HabitAssignStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import greencity.service.HabitAssignService;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.http.MediaType;
import greencity.dto.user.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.Validator;

@ExtendWith(MockitoExtension.class)
class HabitAssignControllerTest {
    private static final String habitAssignLink = "/habit/assign";
    private MockMvc mockMvc;
    @Mock
    private HabitAssignService habitAssignService;
    @Mock
    private Validator mockValidator;
    @InjectMocks
    private HabitAssignController habitAssignController;
    private Long id = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();
    private LocalDate currentDate = LocalDate.now().atStartOfDay().toLocalDate();
    private LocalDate futureDate = currentDate.plusDays(3);
    private Locale locale = new Locale("en");
    private UserVO userVO = new UserVO();
    private HabitAssignDto habitAssignDto = new HabitAssignDto();
    private HabitAssignManagementDto habitAssignManagementDto = new HabitAssignManagementDto();
    private UserShoppingAndCustomShoppingListsDto userShoppingAndCustomShoppingListsDto = new UserShoppingAndCustomShoppingListsDto();

    @BeforeEach
    void setUp() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mockMvc = MockMvcBuilders.standaloneSetup(habitAssignController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setValidator(mockValidator)
                .build();
    }

    @Test
    void assignDefault_HabitAssignControllerTest_shouldReturnCreatedStatus() throws Exception {
        when(habitAssignService.assignDefaultHabitForUser(anyLong(), any(UserVO.class)))
                .thenReturn(new HabitAssignManagementDto());

        mockMvc.perform(post(habitAssignLink + "/{habitId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(habitAssignService).assignDefaultHabitForUser(anyLong(), any(UserVO.class));
    }

    @Test
    void assignCustom_HabitAssignControllerTest_shouldReturnCreatedStatus() throws Exception {
        HabitAssignCustomPropertiesDto habitAssignCustomPropertiesDto = new HabitAssignCustomPropertiesDto();

        when(habitAssignService.assignCustomHabitForUser(eq(id), eq(userVO), eq(habitAssignCustomPropertiesDto)))
                .thenReturn(List.of(habitAssignManagementDto));

        mockMvc.perform(post(habitAssignLink + "/{habitId}/custom", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                        .content(objectMapper.writeValueAsString(habitAssignCustomPropertiesDto)))
                .andExpect(status().isCreated());

        verify(habitAssignService).assignCustomHabitForUser(eq(id), eq(userVO), eq(habitAssignCustomPropertiesDto));
    }

    @Test
    void updateHabitAssignDuration_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.updateUserHabitInfoDuration(anyLong(), any(), anyInt()))
                .thenReturn(new HabitAssignUserDurationDto());

        mockMvc.perform(put(habitAssignLink + "/{habitAssignId}/update-habit-duration", id)
                        .param("duration", "1"))
                .andExpect(status().isOk());

        verify(habitAssignService).updateUserHabitInfoDuration(eq(id), any(), anyInt());
    }

    @Test
    void getHabitAssign_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        mockMvc.perform(get(habitAssignLink + "/{habitId}",id))
                .andExpect(status().isOk());

        verify(habitAssignService).getByHabitAssignIdAndUserId(eq(id) ,any(), any());
    }

    @Test
    void getCurrentUserHabitAssignsByIdAndAcquired_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.getAllHabitAssignsByUserIdAndStatusNotCancelled(any(), anyString()))
                .thenReturn(List.of(habitAssignDto));

        mockMvc.perform(get(habitAssignLink + "/allForCurrentUser"))
                .andExpect(status().isOk());

        verify(habitAssignService).getAllHabitAssignsByUserIdAndStatusNotCancelled(any(), anyString());
    }

    @Test
    void getUserShoppingAndCustomShoppingListsByUserIdAndHabitIdAndLocale_HabitAssignControllerTest_shouldReturnIsOkStatus()
            throws Exception {
        when(habitAssignService.getUserShoppingAndCustomShoppingLists(any(), eq(id), eq(locale.getLanguage())))
                .thenReturn(userShoppingAndCustomShoppingListsDto);

        mockMvc.perform(get(habitAssignLink + "/{habitAssignId}/allUserAndCustomList", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                        .content(objectMapper.writeValueAsString(locale)))
                .andExpect(status().isOk());

        verify(habitAssignService).getUserShoppingAndCustomShoppingLists(any(), eq(id), eq(locale.getLanguage()));
    }

    @Test
    void getUserShoppingAndCustomShoppingListsByUserIdAndHabitId_HabitAssignControllerTest_shouldReturnIsOkStatus()
            throws Exception {
        when(habitAssignService.getUserShoppingAndCustomShoppingLists(any(), eq(id), any()))
                .thenReturn(userShoppingAndCustomShoppingListsDto);

        mockMvc.perform(get(habitAssignLink + "/{habitAssignId}/allUserAndCustomList", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(habitAssignService).getUserShoppingAndCustomShoppingLists(any(), eq(id), any());
    }

    @Test
    void updateUserAndCustomShoppingLists_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        mockMvc.perform(put(habitAssignLink + "/{habitAssignId}/allUserAndCustomList", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                        .content(objectMapper.writeValueAsString(locale))
                        .content(objectMapper.writeValueAsString(userShoppingAndCustomShoppingListsDto)))
                .andExpect(status().isOk());

        verify(habitAssignService).fullUpdateUserAndCustomShoppingLists(any(), eq(id),
                eq(userShoppingAndCustomShoppingListsDto), eq(locale.getLanguage()));
    }

    @Test
    void getListOfUserAndCustomShoppingListsInprogress_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception{
        when(habitAssignService.getListOfUserAndCustomShoppingListsWithStatusInprogress(any(), eq(locale.getLanguage())))
                .thenReturn(List.of(userShoppingAndCustomShoppingListsDto));

        mockMvc.perform(get(habitAssignLink + "/allUserAndCustomShoppingListsInprogress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locale)))
                .andExpect(status().isOk());

        verify(habitAssignService).getListOfUserAndCustomShoppingListsWithStatusInprogress(any(), eq(locale.getLanguage()));
    }

    @Test
    void getAllHabitAssignsByHabitIdAndAcquired_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.getAllHabitAssignsByHabitIdAndStatusNotCancelled(eq(id), eq(locale.getLanguage())))
                .thenReturn(List.of(habitAssignDto));

        mockMvc.perform(get(habitAssignLink + "/{habitId}/all", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locale)))
                .andExpect(status().isOk());

        verify(habitAssignService).getAllHabitAssignsByHabitIdAndStatusNotCancelled(eq(id), eq(locale.getLanguage()));
    }

    @Test
    void getHabitAssignByHabitId_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.findHabitAssignByUserIdAndHabitId(any(), eq(id), eq(locale.getLanguage())))
                .thenReturn(habitAssignDto);

        mockMvc.perform(get(habitAssignLink + "/{habitId}/active", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userVO))
                        .content(objectMapper.writeValueAsString(locale)))
                .andExpect(status().isOk());

        verify(habitAssignService).findHabitAssignByUserIdAndHabitId(any(), eq(id), eq(locale.getLanguage()));
    }

    @Test
    void getUsersHabitByHabitAssignId_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.findHabitByUserIdAndHabitAssignId(any(), eq(id), eq(locale.getLanguage())))
                .thenReturn(new HabitDto());

        mockMvc.perform(get(habitAssignLink + "/{habitAssignId}/more", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locale)))
                .andExpect(status().isOk());

        verify(habitAssignService).findHabitByUserIdAndHabitAssignId(any(), eq(id), eq(locale.getLanguage()));
    }

    @Test
    void updateAssignByHabitId_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        HabitAssignStatDto habitAssignStatDto = new HabitAssignStatDto();
        habitAssignStatDto.setStatus(HabitAssignStatus.INPROGRESS);

        when(habitAssignService.updateStatusByHabitAssignId(eq(id), any(HabitAssignStatDto.class)))
                .thenReturn(habitAssignManagementDto);

        mockMvc.perform(patch(habitAssignLink + "/{habitAssignId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habitAssignStatDto)))
                .andExpect(status().isOk());

        verify(habitAssignService).updateStatusByHabitAssignId(eq(id), any(HabitAssignStatDto.class));
    }

    @Test
    void enrollHabit_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.enrollHabit(eq(id), any(), eq(currentDate), any()))
                .thenReturn(habitAssignDto);

        mockMvc.perform(post(habitAssignLink + "/{habitAssignId}/enroll/{date}", id, currentDate))
                .andExpect(status().isOk());

        verify(habitAssignService).enrollHabit(eq(id), any(), eq(currentDate), any());
    }

    @Test
    void unenrollHabit_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.unenrollHabit(eq(id), any(), eq(currentDate)))
                .thenReturn(habitAssignDto);

        mockMvc.perform(post(habitAssignLink + "/{habitAssignId}/unenroll/{date}", id, currentDate))
                .andExpect(status().isOk());

        verify(habitAssignService).unenrollHabit(eq(id), any(), eq(currentDate));
    }

    @Test
    void getInprogressHabitAssignOnDate_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.findInprogressHabitAssignsOnDate(any(), eq(currentDate), anyString()))
                .thenReturn(List.of(habitAssignDto));

        mockMvc.perform(get(habitAssignLink + "/active/{date}", currentDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locale.getLanguage())))
                .andExpect(status().isOk());

        verify(habitAssignService).findInprogressHabitAssignsOnDate(any(), eq(currentDate), eq(locale.getLanguage()));
    }

    @Test
    void getHabitAssignBetweenDates_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        HabitsDateEnrollmentDto habitsDateEnrollmentDto = new HabitsDateEnrollmentDto();

        when(habitAssignService.findHabitAssignsBetweenDates(any(), eq(currentDate), eq(futureDate), anyString()))
                .thenReturn(List.of(habitsDateEnrollmentDto));

        mockMvc.perform(get(habitAssignLink + "/activity/{from}/to/{to}", currentDate, futureDate))
                .andExpect(status().isOk());

        verify(habitAssignService).findHabitAssignsBetweenDates(any(), eq(currentDate), eq(futureDate), anyString());
    }

    @Test
    void cancelHabitAssign_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        when(habitAssignService.cancelHabitAssign(eq(id), any()))
                .thenReturn(habitAssignDto);

        mockMvc.perform(patch(habitAssignLink + "/cancel/{habitId}", id))
                .andExpect(status().isOk());

        verify(habitAssignService).cancelHabitAssign(eq(id), any());
    }

    @Test
    void deleteHabitAssign_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        mockMvc.perform(delete(habitAssignLink + "/delete/{habitAssignId}", id))
                .andExpect(status().isOk());

        verify(habitAssignService).deleteHabitAssign(eq(id), any());
    }

    @Test
    void updateShoppingListStatus_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        UpdateUserShoppingListDto updateUserShoppingListDto = new UpdateUserShoppingListDto();
        updateUserShoppingListDto.setHabitAssignId(id);
        updateUserShoppingListDto.setUserShoppingListItemId(id);

        mockMvc.perform(put(habitAssignLink + "/saveShoppingListForHabitAssign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserShoppingListDto)))
                .andExpect(status().isOk());

        verify(habitAssignService).updateUserShoppingListItem(eq(updateUserShoppingListDto));
    }

    @Test
    void updateProgressNotificationHasDisplayed_HabitAssignControllerTest_shouldReturnIsOkStatus() throws Exception {
        mockMvc.perform(put(habitAssignLink + "/{habitAssignId}/updateProgressNotificationHasDisplayed", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(habitAssignService).updateProgressNotificationHasDisplayed(eq(id), any());
    }
}