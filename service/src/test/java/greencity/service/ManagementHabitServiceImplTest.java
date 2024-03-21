package greencity.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import greencity.constant.AppConstant;
import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.filter.FilterHabitDto;
import greencity.dto.habit.HabitManagementDto;
import greencity.dto.habit.HabitVO;
import greencity.dto.habittranslation.HabitTranslationManagementDto;
import greencity.dto.language.LanguageDTO;
import greencity.entity.Habit;
import greencity.entity.HabitTranslation;
import greencity.entity.Language;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.WrongIdException;
import greencity.repository.HabitRepo;
import greencity.repository.HabitTranslationRepo;
import greencity.repository.options.HabitFilter;

@ExtendWith(MockitoExtension.class)
class ManagementHabitServiceImplTest {

    @InjectMocks
    private ManagementHabitServiceImpl managementHabitService;

    @Mock
    private HabitRepo habitRepo;

    @Mock
    private HabitTranslationRepo habitTranslationRepo;

    @Mock
    private LanguageService languageService;

    @Mock
    private FileService fileService;

    @Mock
    private HabitAssignService habitAssignService;

    @Mock
    private HabitFactService habitFactService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void getById_whenCorrectFlow_returnValidHabitManagementDto() {
        Long id = 1L;
        Habit habit = new Habit();
        HabitManagementDto expectedHabitManagementDto = new HabitManagementDto();

        when(habitRepo.findById(anyLong())).thenReturn(Optional.of(habit));
        when(modelMapper.map(any(), any())).thenReturn(expectedHabitManagementDto);

        assertEquals(expectedHabitManagementDto, managementHabitService.getById(id));

        verify(habitRepo).findById(id);
        verify(modelMapper).map(habit, HabitManagementDto.class);
    }

    @Test
    void getById_whenHabitNotFound_throwNotFoundException() {
        Long id = 1L;
        String expectedMessage = ErrorMessage.HABIT_NOT_FOUND_BY_ID + id;

        when(habitRepo.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> managementHabitService.getById(id));

        assertEquals(expectedMessage, exception.getMessage());

        verify(habitRepo).findById(id);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getAllHabitsDto_whenCorrectFlow_returnValidPageableDto() {
        String searchReg = "searchReg";
        Integer durationFrom = 1;
        Integer durationTo = 2;
        Integer complexity = 3;
        Boolean withoutImage = false;
        Boolean withImage = true;
        PageRequest pageable = PageRequest.of(0, 10);

        Long totalElements = 1L;
        Habit habit = new Habit();
        Page<Habit> habits = new PageImpl<>(Collections.singletonList(habit), pageable, totalElements);
        HabitManagementDto habitDto = new HabitManagementDto();
        List<HabitManagementDto> habitDtos = Collections.singletonList(habitDto);

        PageableDto<HabitManagementDto> expectedPageableDto =
            new PageableDto<>(habitDtos, habits.getTotalElements(), pageable.getPageNumber(), habits.getTotalPages());

        when(habitRepo.findAll(any(HabitFilter.class), any(Pageable.class))).thenReturn(habits);
        when(modelMapper.map(any(), any())).thenReturn(habitDto);

        FilterHabitDto filterHabitDto =
            new FilterHabitDto(searchReg, durationFrom, durationTo, complexity, withoutImage,
                withImage);

        PageableDto<HabitManagementDto> actualPageableDto = assertDoesNotThrow(
            () -> managementHabitService.getAllHabitsDto(searchReg, durationFrom, durationTo, complexity, withoutImage,
                withImage, pageable));

        assertEquals(expectedPageableDto, actualPageableDto);

        verify(habitRepo).findAll(new HabitFilter(filterHabitDto), pageable);
        verify(modelMapper).map(habit, HabitManagementDto.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "null, true",
        "true, null",
        "null, null",
        "false, false"}, nullValues = "null")
    void getAllHabitsDto_whenWithOrWithoutImageIsNull_setFalseInsteadOfNull(Boolean withoutImage, Boolean withImage) {
        String searchReg = "searchReg";
        Integer durationFrom = 1;
        Integer durationTo = 2;
        Integer complexity = 3;
        PageRequest pageable = PageRequest.of(0, 10);

        Long totalElements = 1L;
        Habit habit = new Habit();
        Page<Habit> habits = new PageImpl<>(Collections.singletonList(habit), pageable, totalElements);
        HabitManagementDto habitDto = new HabitManagementDto();
        List<HabitManagementDto> habitDtos = Collections.singletonList(habitDto);

        PageableDto<HabitManagementDto> expectedPageableDto =
            new PageableDto<>(habitDtos, habits.getTotalElements(), pageable.getPageNumber(), habits.getTotalPages());

        when(habitRepo.findAll(any(HabitFilter.class), any(Pageable.class))).thenReturn(habits);
        when(modelMapper.map(any(), any())).thenReturn(habitDto);

        Boolean finalWithoutImage = withoutImage;
        Boolean finalWithImage = withImage;
        PageableDto<HabitManagementDto> actualPageableDto = assertDoesNotThrow(
            () -> managementHabitService.getAllHabitsDto(searchReg, durationFrom, durationTo, complexity,
                finalWithoutImage, finalWithImage, pageable));

        withImage = withImage != null && withImage;
        withoutImage = withoutImage != null && withoutImage;
        FilterHabitDto filterHabitDto =
            new FilterHabitDto(searchReg, durationFrom, durationTo, complexity, withoutImage,
                withImage);

        assertEquals(expectedPageableDto, actualPageableDto);

        verify(habitRepo).findAll(new HabitFilter(filterHabitDto), pageable);
        verify(modelMapper).map(habit, HabitManagementDto.class);
    }

    @Test
    void saveHabitAndTranslations_whenCorrectFlow_returnValidHabitManagementDto() {
        String languageCode = "en";
        LanguageDTO languageDTO = LanguageDTO.builder().id(1L).code(languageCode).build();
        Language language = Language.builder().id(1L).code(languageCode).build();

        HabitManagementDto habitManagementDto = HabitManagementDto.builder().id(1L)
            .image(AppConstant.DEFAULT_HABIT_IMAGE)
            .habitTranslations(Collections.singletonList(
                HabitTranslationManagementDto.builder().habitItem("Item").description("Description")
                    .languageCode("en").name("Name").build()))
            .build();
        Habit habit = Habit.builder()
            .image(AppConstant.DEFAULT_HABIT_IMAGE)
            .habitTranslations(
                habitManagementDto.getHabitTranslations().stream()
                    .map(habitTranslationDto -> HabitTranslation.builder()
                        .description(habitTranslationDto.getDescription())
                        .habitItem(habitTranslationDto.getHabitItem())
                        .name(habitTranslationDto.getName())
                        .language(language)
                        .build())
                    .collect(Collectors.toList()))
            .build();
        habit.getHabitTranslations().forEach(habitTranslation -> habitTranslation.setHabit(habit));

        when(languageService.findByCode(anyString())).thenReturn(languageDTO);
        when(modelMapper.map(any(), eq(Language.class))).thenReturn(language);
        when(habitRepo.save(habit)).thenReturn(habit);
        when(modelMapper.map(any(), eq(HabitManagementDto.class))).thenReturn(habitManagementDto);

        assertEquals(habitManagementDto, managementHabitService.saveHabitAndTranslations(habitManagementDto, null));

        verify(languageService).findByCode(languageCode);
        verify(modelMapper).map(languageDTO, Language.class);
        verify(habitRepo).save(habit);
        verify(habitTranslationRepo).saveAll(habit.getHabitTranslations());
        verify(modelMapper).map(habit, HabitManagementDto.class);
    }

    @Test
    void update_whenAllIsValid_expectCorrectFlow() {
        Language language = Language.builder().id(1L).code("en").build();
        HabitTranslation habitTranslation = HabitTranslation.builder()
            .id(1L)
            .habitItem("Item")
            .description("Description")
            .language(language)
            .name("Name")
            .build();

        Habit habit = Habit.builder()
            .id(1L)
            .habitTranslations(Collections.singletonList(habitTranslation))
            .build();

        HabitTranslationManagementDto habitTranslationManagementDto = HabitTranslationManagementDto.builder()
            .habitItem("Item")
            .description("Description")
            .languageCode("en")
            .name("Name")
            .build();

        HabitManagementDto habitManagementDto = HabitManagementDto.builder()
            .id(1L)
            .image("image")
            .habitTranslations(Collections.singletonList(habitTranslationManagementDto))
            .build();

        Map<String, HabitTranslationManagementDto> managementDtoMap = habitManagementDto.getHabitTranslations().stream()
            .collect(Collectors.toMap(HabitTranslationManagementDto::getLanguageCode,
                Function.identity()));

        habit.getHabitTranslations().forEach(
            ht -> enhanceTranslationWithDto(managementDtoMap.get(ht.getLanguage().getCode()), ht));

        when(habitRepo.findById(anyLong())).thenReturn(Optional.of(habit));
        when(habitRepo.save(habit)).thenReturn(habit);

        assertDoesNotThrow(() -> managementHabitService.update(habitManagementDto, null));

        verify(habitRepo).findById(habitManagementDto.getId());
        verify(habitRepo).save(habit);
    }

    @Test
    void update_whenHabitNotFound_throwWrongIdException() {
        Long id = 1L;
        String expectedMessage = ErrorMessage.HABIT_NOT_FOUND_BY_ID;
        HabitManagementDto habitManagementDto = HabitManagementDto.builder().id(id).build();

        when(habitRepo.findById(anyLong())).thenReturn(Optional.empty());

        WrongIdException exception = assertThrows(WrongIdException.class,
            () -> managementHabitService.update(habitManagementDto, null));

        assertEquals(expectedMessage, exception.getMessage());

        verify(habitRepo).findById(id);
        verifyNoMoreInteractions(habitRepo);
    }

    private void enhanceTranslationWithDto(HabitTranslationManagementDto htDto, HabitTranslation ht) {
        ht.setDescription(htDto.getDescription());
        ht.setHabitItem(htDto.getHabitItem());
        ht.setName(htDto.getName());
    }

    @Test
    void delete_whenAllIsValid_expectCorrectFlow() {
        Long id = 1L;
        Habit habit = Habit.builder().id(id).build();
        HabitVO habitVO = HabitVO.builder().id(id).build();

        when(habitRepo.findById(anyLong())).thenReturn(Optional.of(habit));
        when(modelMapper.map(any(), eq(HabitVO.class))).thenReturn(habitVO);

        assertDoesNotThrow(() -> managementHabitService.delete(id));

        verify(habitRepo).findById(id);
        verify(modelMapper).map(habit, HabitVO.class);
        verify(habitTranslationRepo).deleteAllByHabit(habit);
        verify(habitFactService).deleteAllByHabit(habitVO);
        verify(habitAssignService).deleteAllHabitAssignsByHabit(habitVO);
        verify(habitRepo).delete(habit);
    }

    @Test
    void delete_whenHabitNotFound_throwWrongIdException() {
        Long id = 1L;
        String expectedMessage = ErrorMessage.HABIT_NOT_FOUND_BY_ID;

        when(habitRepo.findById(anyLong())).thenReturn(Optional.empty());

        WrongIdException exception = assertThrows(WrongIdException.class,
            () -> managementHabitService.delete(id));

        assertEquals(expectedMessage, exception.getMessage());

        verify(habitRepo).findById(id);
        verifyNoMoreInteractions(habitRepo);
    }

    @Test
    void deleteAll_whenAllIsValid_expectCorrectFlow() {
        Habit habit = new Habit();
        HabitVO habitVO = new HabitVO();
        List<Long> idList = List.of(1L, 2L, 3L, 4L, 5L);

        when(habitRepo.findById(anyLong())).thenReturn(Optional.of(habit));
        when(modelMapper.map(habit, HabitVO.class)).thenReturn(habitVO);

        assertDoesNotThrow(() -> managementHabitService.deleteAll(idList));
        verify(habitRepo, times(idList.size())).delete(any(Habit.class));
        verify(habitTranslationRepo, times(idList.size())).deleteAllByHabit(any(Habit.class));
    }
}
