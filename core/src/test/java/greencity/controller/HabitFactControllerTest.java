package greencity.controller;

import static greencity.constant.ErrorMessage.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

import greencity.ModelUtils;
import greencity.dto.PageableDto;
import greencity.dto.habitfact.HabitFactDtoResponse;
import greencity.dto.habitfact.HabitFactPostDto;
import greencity.dto.habitfact.HabitFactUpdateDto;
import greencity.dto.habitfact.HabitFactVO;
import greencity.dto.language.LanguageTranslationDTO;
import greencity.exception.exceptions.NotDeletedException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.NotUpdatedException;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.HabitFactService;
import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
class HabitFactControllerTest {

    private static final String habitFactControllerLink = "/facts";

    private static final Long validId = 1L;

    private static final Long invalidId = -1L;

    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private HabitFactService habitFactService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private HabitFactController habitFactController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(habitFactController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new CustomExceptionHandler(errorAttributes, objectMapper))
            .setValidator(validator)
            .build();
    }

    @Test
    void getRandomFactByHabitId_ValidHabitId_Ok() throws Exception {
        Locale locale = Locale.forLanguageTag("en");
        LanguageTranslationDTO languageTranslationDTO = ModelUtils.getLanguageTranslationDTO();
        when(habitFactService.getRandomHabitFactByHabitIdAndLanguage(anyLong(), anyString()))
            .thenReturn(languageTranslationDTO);

        mockMvc.perform(get(habitFactControllerLink + "/random/{habitId}", validId)
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(result -> assertEquals(languageTranslationDTO.getLanguage().getCode(),
                result.getResponse().getLocale().getLanguage()))
            .andExpect(jsonPath("$.language.code").value(languageTranslationDTO.getLanguage().getCode()))
            .andExpect(jsonPath("$.content").value(languageTranslationDTO.getContent()));

        verify(habitFactService).getRandomHabitFactByHabitIdAndLanguage(validId, locale.getLanguage());
    }

    @Test
    void getRandomFactByHabitId_InvalidHabitId_NotFound() throws Exception {
        Locale locale = Locale.forLanguageTag("en");
        when(habitFactService.getRandomHabitFactByHabitIdAndLanguage(anyLong(), anyString()))
            .thenThrow(new NotFoundException(HABIT_FACT_NOT_FOUND_BY_ID + invalidId));

        mockMvc.perform(get(habitFactControllerLink + "/random/{habitId}", invalidId)
            .accept(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertInstanceOf(NotFoundException.class, result.getResolvedException()))
            .andExpect(result -> assertEquals(HABIT_FACT_NOT_FOUND_BY_ID + invalidId,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(habitFactService).getRandomHabitFactByHabitIdAndLanguage(invalidId, locale.getLanguage());
    }

    @Test
    void getHabitFactOfTheDay_ValidLanguageId_Ok() throws Exception {
        LanguageTranslationDTO languageTranslationDTO = ModelUtils.getLanguageTranslationDTO();
        when(habitFactService.getHabitFactOfTheDay(validId)).thenReturn(languageTranslationDTO);

        mockMvc.perform(get(habitFactControllerLink + "/dayFact/{languageId}", validId)
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(result -> assertEquals(languageTranslationDTO.getLanguage().getCode(),
                result.getResponse().getLocale().getLanguage()))
            .andExpect(jsonPath("$.language.code").value(languageTranslationDTO.getLanguage().getCode()))
            .andExpect(jsonPath("$.content").value(languageTranslationDTO.getContent()));

        verify(habitFactService).getHabitFactOfTheDay(validId);
    }

    @Test
    void getAll_Ok() throws Exception {
        int totalElements = 2;
        int currentPage = 1;
        int totalPages = 1;

        Pageable page = PageRequest.of(0, 20);
        Locale locale = Locale.forLanguageTag("en");
        LanguageTranslationDTO languageTranslationDTO = ModelUtils.getLanguageTranslationDTO();
        PageableDto<LanguageTranslationDTO> pageableDto = new PageableDto<>(
            List.of(languageTranslationDTO, languageTranslationDTO), totalElements, currentPage, totalPages);
        when(habitFactService.getAllHabitFacts(page, locale.getLanguage())).thenReturn(pageableDto);

        mockMvc.perform(get(habitFactControllerLink)
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page").exists())
            .andExpect(jsonPath("$.page").isArray())
            .andExpect(jsonPath("$.page").isNotEmpty())
            .andExpect(jsonPath("$.page", hasSize(totalElements)))
            .andExpect(jsonPath("$.page[0].content").value(languageTranslationDTO.getContent()))
            .andExpect(jsonPath("$.page[0].language.code").value(languageTranslationDTO.getLanguage().getCode()))
            .andExpect(jsonPath("$.totalElements").value(totalElements))
            .andExpect(jsonPath("$.currentPage").value(currentPage))
            .andExpect(jsonPath("$.totalPages").value(totalPages));

        verify(habitFactService).getAllHabitFacts(page, locale.getLanguage());
    }

    @Test
    void save_ValidHabitFactPostDto_Created() throws Exception {
        HabitFactVO habitFactVO = ModelUtils.getHabitFactVO();
        HabitFactPostDto habitFactPostDto = ModelUtils.getHabitFactPostDto();
        HabitFactDtoResponse habitFactDtoResponse = ModelUtils.getHabitFactDtoResponse();
        when(modelMapper.map(habitFactVO, HabitFactDtoResponse.class)).thenReturn(habitFactDtoResponse);
        when(habitFactService.save(habitFactPostDto)).thenReturn(habitFactVO);

        mockMvc.perform(post(habitFactControllerLink)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(habitFactPostDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.habit.id").value(habitFactVO.getHabit().getId()))
            .andExpect(jsonPath("$.habit.id").value(habitFactVO.getHabit().getId()))
            .andExpect(jsonPath("$.translations").isArray())
            .andExpect(jsonPath("$.translations").isNotEmpty())
            .andExpect(jsonPath("$.translations", hasSize(1)))
            .andExpect(jsonPath("$.translations[0].language.code")
                .value(habitFactPostDto.getTranslations().getFirst().getLanguage().getCode()))
            .andExpect(
                jsonPath("$.translations[0].content").value(habitFactPostDto.getTranslations().getFirst().getContent()));

        verify(habitFactService).save(habitFactPostDto);
        verify(modelMapper).map(habitFactVO, HabitFactDtoResponse.class);
    }

    @Test
    void save_HabitFactPostDtoWithInvalidHabitId_NotFound() throws Exception {
        HabitFactVO habitFactVO = ModelUtils.getHabitFactVO();
        HabitFactPostDto habitFactPostDto = ModelUtils.getHabitFactPostDto();
        when(habitFactService.save(habitFactPostDto))
            .thenThrow(new NotFoundException(HABIT_NOT_FOUND_BY_ID + invalidId));

        mockMvc.perform(post(habitFactControllerLink)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(habitFactPostDto)))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertInstanceOf(NotFoundException.class, result.getResolvedException()))
            .andExpect(result -> assertEquals(HABIT_NOT_FOUND_BY_ID + invalidId,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(habitFactService).save(habitFactPostDto);
        verify(modelMapper, times(0)).map(habitFactVO, HabitFactDtoResponse.class);
    }

    @Test
    void save_HabitFactPostDtoWithConstraintViolation_BadRequest() throws Exception {
        String message = "Constraint violation error";
        HabitFactVO habitFactVO = ModelUtils.getHabitFactVO();
        HabitFactPostDto habitFactPostDto = ModelUtils.getHabitFactPostDto();

        when(habitFactService.save(habitFactPostDto))
            .thenThrow(new ConstraintViolationException(message, Collections.emptySet()));

        mockMvc.perform(post(habitFactControllerLink)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(habitFactPostDto)))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()))
            .andExpect(
                result -> assertEquals(message, Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(habitFactService).save(habitFactPostDto);
        verify(modelMapper, times(0)).map(habitFactVO, HabitFactDtoResponse.class);
    }

    @Test
    void update_ValidHabitFactUpdateDto_Ok() throws Exception {
        HabitFactVO habitFactVO = ModelUtils.getHabitFactVO();
        HabitFactPostDto habitFactPostDto = ModelUtils.getHabitFactPostDto();
        HabitFactUpdateDto habitFactUpdateDto = ModelUtils.getHabitFactUpdateDto();
        when(modelMapper.map(habitFactVO, HabitFactPostDto.class)).thenReturn(habitFactPostDto);
        when(habitFactService.update(habitFactUpdateDto, validId)).thenReturn(habitFactVO);

        mockMvc.perform(put(habitFactControllerLink + "/{id}", validId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(habitFactUpdateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.habit.id").value(habitFactVO.getHabit().getId()))
            .andExpect(jsonPath("$.habit.id").value(habitFactVO.getHabit().getId()))
            .andExpect(jsonPath("$.translations").isArray())
            .andExpect(jsonPath("$.translations").isNotEmpty())
            .andExpect(jsonPath("$.translations", hasSize(1)))
            .andExpect(jsonPath("$.translations[0].language.code")
                .value(habitFactPostDto.getTranslations().getFirst().getLanguage().getCode()))
            .andExpect(
                jsonPath("$.translations[0].content").value(habitFactPostDto.getTranslations().getFirst().getContent()));

        verify(habitFactService).update(habitFactUpdateDto, validId);
        verify(modelMapper).map(habitFactVO, HabitFactPostDto.class);
    }

    @Test
    void update_HabitFactUpdateDtoWithNonExistentHabitFact_BadRequest() throws Exception {
        HabitFactVO habitFactVO = ModelUtils.getHabitFactVO();
        HabitFactUpdateDto habitFactUpdateDto = ModelUtils.getHabitFactUpdateDto();
        when(habitFactService.update(habitFactUpdateDto, invalidId))
            .thenThrow(new NotUpdatedException(HABIT_FACT_NOT_FOUND_BY_ID + invalidId));

        mockMvc.perform(put(habitFactControllerLink + "/{id}", invalidId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(habitFactUpdateDto)))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(NotUpdatedException.class, result.getResolvedException()))
            .andExpect(result -> assertEquals(HABIT_FACT_NOT_FOUND_BY_ID + invalidId,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(habitFactService).update(habitFactUpdateDto, invalidId);
        verify(modelMapper, times(0)).map(habitFactVO, HabitFactPostDto.class);
    }

    @Test
    void delete_ValidId_Ok() throws Exception {
        when(habitFactService.delete(anyLong())).thenReturn(anyLong());

        mockMvc.perform(delete(habitFactControllerLink + "/{id}", validId))
            .andExpect(status().isOk());

        verify(habitFactService).delete(validId);
    }

    @Test
    void delete_InvalidId_BadRequest() throws Exception {
        when(habitFactService.delete(anyLong())).thenThrow(new NotDeletedException(HABIT_FACT_NOT_DELETED_BY_ID));

        mockMvc.perform(delete(habitFactControllerLink + "/{id}", invalidId))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(NotDeletedException.class, result.getResolvedException()))
            .andExpect(result -> assertEquals(HABIT_FACT_NOT_DELETED_BY_ID,
                Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(habitFactService).delete(invalidId);
    }
}
