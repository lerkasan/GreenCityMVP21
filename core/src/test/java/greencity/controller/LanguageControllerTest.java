package greencity.controller;

import greencity.service.LanguageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LanguageControllerTest {
    @Mock
    private LanguageService languageService;

    @InjectMocks
    private LanguageController languageController;

    @Test
    @DisplayName("Test getAllLanguageCodes")
    public void testGetAllLanguageCodes() {
        List<String> expectedLanguageCodes = Arrays.asList("en", "es", "fr");
        when(languageService.findAllLanguageCodes()).thenReturn(expectedLanguageCodes);

        ResponseEntity<List<String>> response = languageController.getAllLanguageCodes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLanguageCodes, response.getBody());
    }
}