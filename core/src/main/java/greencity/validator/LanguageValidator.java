package greencity.validator;

import greencity.annotations.ValidLanguage;
import greencity.service.LanguageService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class LanguageValidator implements ConstraintValidator<ValidLanguage, Locale> {
    private List<String> codes;

    private final LanguageService languageService;

    @Override
    public void initialize(ValidLanguage constraintAnnotation) {
        codes = languageService.findAllLanguageCodes();
    }

    @Override
    public boolean isValid(Locale value, ConstraintValidatorContext context) {
        return codes.contains(value.getLanguage());
    }
}
