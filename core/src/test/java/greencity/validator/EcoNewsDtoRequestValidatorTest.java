package greencity.validator;

import greencity.dto.econews.AddEcoNewsDtoRequest;
import greencity.exception.exceptions.InvalidURLException;
import greencity.exception.exceptions.WrongCountOfTagsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static greencity.ModelUtils.getAddEcoNewsDtoRequest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class EcoNewsDtoRequestValidatorTest {
    private EcoNewsDtoRequestValidator validator;
    private AddEcoNewsDtoRequest request;

    @BeforeEach
    void setUp() {
        validator = new EcoNewsDtoRequestValidator();
        request = getAddEcoNewsDtoRequest();
    }


    @ParameterizedTest
    @MethodSource("validSourceProvider")
    void isValid_CorrectData_True(String source) {
        request.setSource(source);
        assertTrue(validator.isValid(request, null));
    }

    @ParameterizedTest
    @MethodSource("invalidUrlsProvider")
    void isValid_IncorrectUrl_ExceptionThrown(String source) {
        request.setSource(source);

        assertThrows(InvalidURLException.class,()-> validator.isValid(request, null));
    }

    @Test
    void isValid_EmptyTags_ExceptionThrown() {
        request.setSource("https://eco-lavca.ua");
        request.setTags(List.of());

        assertThrows(WrongCountOfTagsException.class,()-> validator.isValid(request, null));
    }

    @Test
    void isValid_ToMuchTags_ExceptionThrown() {
        request.setSource("https://eco-lavca.ua");
        request.setTags(getTags());

        assertThrows(WrongCountOfTagsException.class,()-> validator.isValid(request, null));
    }


    private List<String> getTags() {
        return Arrays.asList("tag1","tag2","tag3","tag4");
    }



    private static Stream<String> validSourceProvider(){
        return Stream.of(
                "https://www.google.com",
                "https://www.nytimes.com",
                "https://www.github.com",
                "https://en.wikipedia.org/wiki/Main_Page",
                "https://www.amazon.com",
                "", null
        );
    }

    private static Stream<String> invalidUrlsProvider(){
        return Stream.of(
                "ht://eco-lavca.ua",
                "https://www.nytim\\.com",
                "http://1080::8:800:200C:417A]/index.html",
                "htps://en.wikip-org"
        );
    }
}