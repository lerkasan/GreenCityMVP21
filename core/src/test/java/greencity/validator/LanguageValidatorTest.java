package greencity.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import greencity.annotations.ValidLanguage;
import greencity.service.LanguageService;

@ExtendWith(MockitoExtension.class)
public class LanguageValidatorTest {

    @Mock
    private LanguageService languageService;

    @InjectMocks
    private LanguageValidator languageValidator;

    private static Stream<List<String>> validLanguageCodesProvider() {
        return Stream.of(Arrays.asList("en", "fr", "de"), Arrays.asList("en", "es", "it"),
                Arrays.asList("en", "ja", "ko"));
    }

    private static Stream<List<String>> invalidLanguageCodesProvider() {
        return Stream.of(Arrays.asList("fr", "de"), Arrays.asList("ua", "it"), Arrays.asList("ja", "ko"));
    }

    @ParameterizedTest
    @NullSource
    public void initialize_whenLanguageCodesIsNull_doesNotThrowAnyException(ValidLanguage constraintAnnotation) {
        assertDoesNotThrow(() -> languageValidator.initialize(constraintAnnotation));
    }

    @ParameterizedTest
    @MethodSource("validLanguageCodesProvider")
    public void isValid_whenLocaleIsValid_returnTrue(List<String> languageCodes) {
        when(languageService.findAllLanguageCodes()).thenReturn(languageCodes);
        languageValidator.initialize(null);

        Locale validLocale = new Locale("en");

        assertTrue(languageValidator.isValid(validLocale, null));
    }

    @ParameterizedTest
    @MethodSource("invalidLanguageCodesProvider")
    public void isValid_whenLocaleIsInvalid_returnFalse(List<String> languageCodes) {
        when(languageService.findAllLanguageCodes()).thenReturn(languageCodes);
        languageValidator.initialize(null);

        Locale invalidLocale = new Locale("es");

        assertFalse(languageValidator.isValid(invalidLocale, null));
    }

    @Test
    public void isValid_whenLanguageCodesIsNull_throwNullPointerException() {
        when(languageService.findAllLanguageCodes()).thenReturn(null);
        languageValidator.initialize(null);

        assertThrows(NullPointerException.class, () -> languageValidator.isValid(new Locale("en"), null));
    }

    @Test
    public void isValid_whenLanguageCodesIsEmpty_returnFalse() {
        when(languageService.findAllLanguageCodes()).thenReturn(List.of());
        languageValidator.initialize(null);

        assertFalse(languageValidator.isValid(new Locale("en"), null));
    }

    @Test
    public void isValid_whenLocaleIsNull_throwNullPointerException() {
        when(languageService.findAllLanguageCodes()).thenReturn(Arrays.asList("en", "fr", "de"));
        languageValidator.initialize(null);

        assertThrows(NullPointerException.class, () -> languageValidator.isValid(null, null));
    }


}