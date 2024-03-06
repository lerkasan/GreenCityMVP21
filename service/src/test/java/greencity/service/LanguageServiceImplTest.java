package greencity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import greencity.ModelUtils;
import greencity.constant.AppConstant;
import greencity.dto.language.LanguageDTO;
import greencity.entity.Language;
import greencity.entity.localization.TagTranslation;
import greencity.exception.exceptions.LanguageNotFoundException;
import greencity.repository.LanguageRepo;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class LanguageServiceImplTest {

    private final Language language = ModelUtils.getLanguage();
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LanguageRepo languageRepo;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private LanguageServiceImpl languageService;

    @Test
    void getAllLanguages_whenNoExceptionThrown_expectCorrectFlow() {
        List<LanguageDTO> emptyList = Collections.emptyList();

        List<Language> languages = Collections.singletonList(language);
        when(languageRepo.findAll()).thenReturn(languages);
        when(modelMapper.map(languages, new TypeToken<List<LanguageDTO>>() {
        }.getType()))
            .thenReturn(emptyList);

        assertEquals(emptyList, languageService.getAllLanguages());

        verify(languageRepo).findAll();
        verify(modelMapper).map(languages, new TypeToken<List<LanguageDTO>>() {
        }.getType());
    }

    @Test
    void extractLanguageCodeFromRequest_whenLanguageCodeIsNotNull_returnLanguageCode() {
        String expectedLanguageCode = "ua";

        when(httpServletRequest.getParameter("language")).thenReturn(expectedLanguageCode);
        assertEquals(expectedLanguageCode, languageService.extractLanguageCodeFromRequest());

        verify(httpServletRequest).getParameter("language");
    }

    @ParameterizedTest
    @NullSource
    void extractLanguageCodeFromRequest_whenLanguageCodeIsNull_returnDefaultLanguageCode(String nullLanguageCode) {
        when(httpServletRequest.getParameter("language")).thenReturn(nullLanguageCode);
        
        Assertions.assertEquals(AppConstant.DEFAULT_LANGUAGE_CODE, languageService.extractLanguageCodeFromRequest());
        
        verify(httpServletRequest).getParameter("language");
    }

    @Test
    void findByCode_whenLanguageRepoReturnExistingLanguage_returnValidLanguageDto() {
        LanguageDTO languageDTO = new LanguageDTO(1L, "en");

        when(modelMapper.map(language, LanguageDTO.class)).thenReturn(languageDTO);
        when(languageRepo.findByCode(language.getCode())).thenReturn(Optional.of(language));

        assertEquals(languageDTO, languageService.findByCode(language.getCode()));

        verify(languageRepo).findByCode(language.getCode());
        verify(modelMapper).map(language, LanguageDTO.class);
    }

    @Test
    void findByCode_whenLanguageRepoReturnEmptyOptional_throwLanguageNotFoundException() {
        when(languageRepo.findByCode(language.getCode())).thenReturn(Optional.empty());

        assertThrows(LanguageNotFoundException.class, () -> languageService.findByCode(language.getCode()));
    }

    @Test
    void findAllLanguageCodes_whenNoExceptionThrown_returnValidList() {
        List<String> codes = Collections.singletonList(language.getCode());

        when(languageRepo.findAllLanguageCodes()).thenReturn(codes);

        assertEquals(codes, languageService.findAllLanguageCodes());

        verify(languageRepo).findAllLanguageCodes();
    }

    @Test
    void findByTagTranslationId_whenNoExceptionThrown_returnValidLanguageDto() {
        TagTranslation tagTranslation = new TagTranslation(1L, "Tag name", null, null);
        LanguageDTO languageDTO = new LanguageDTO(1L, "en");

        when(languageRepo.findByTagTranslationId(tagTranslation.getId())).thenReturn(Optional.of(language));
        when(modelMapper.map(language, LanguageDTO.class)).thenReturn(languageDTO);

        assertEquals(languageDTO, languageService.findByTagTranslationId(tagTranslation.getId()));

        verify(languageRepo).findByTagTranslationId(tagTranslation.getId());
    }
}