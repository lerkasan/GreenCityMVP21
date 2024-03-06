package greencity.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(SpringExtension.class)
class ImageValidatorTest {

    private ImageValidator imageValidator;

    @BeforeEach
    public void init() {
        imageValidator = new ImageValidator();
    }

    @ParameterizedTest
    @NullSource
    void isValid_ImageIsNull_True(MultipartFile image) {
        assertTrue(imageValidator.isValid(image, null));
    }

    @ParameterizedTest
    @MethodSource("filesWithValidContentType")
    void isValid_ImageHasValidContentType_True(MultipartFile image) {
        assertTrue(imageValidator.isValid(image, null));
    }

    @ParameterizedTest
    @MethodSource("filesWithInvalidContentType")
    void isValid_ImageHasInvalidContentType_False(MultipartFile image) {
        assertFalse(imageValidator.isValid(image, null));
    }

    private static Stream<MultipartFile> filesWithValidContentType() {
        Stream<String> validImageContentTypes = Stream.of("image/jpeg", "image/png", "image/jpg");
        return validImageContentTypes
                .map(contentType -> new MockMultipartFile("image", "image", contentType, "valid-content-type".getBytes()));
    }

    private static Stream<MultipartFile> filesWithInvalidContentType() {
        Stream<String> invalidContentTypes = Stream.of("image/bmp", "image/gif", "audio/mp3", "video/mp4", "text/plain", "application/json");
        return invalidContentTypes
                .map(contentType -> new MockMultipartFile("file", "file", contentType, "invalid-content-type".getBytes()));
    }
}