package greencity.controller;

import greencity.annotations.*;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.habit.AddCustomHabitDtoRequest;
import greencity.dto.habit.AddCustomHabitDtoResponse;
import greencity.dto.habit.HabitDto;
import greencity.dto.habit.HabitVO;
import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.dto.user.UserProfilePictureDto;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.BadRequestException;
import greencity.service.HabitService;
import greencity.service.TagsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/habit")
public class HabitController {
    private final HabitService habitService;
    private final TagsService tagsService;

    /**
     * Method finds {@link HabitVO} by given id with locale translation.
     *
     * @param id     of {@link HabitVO}.
     * @param locale {@link Locale} with needed language code.
     * @return {@link HabitDto}.
     */
    @Operation(summary = "Find habit by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
            content = @Content(schema = @Schema(implementation = HabitDto.class))),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{id}")
    @ApiLocale
    public ResponseEntity<HabitDto> getHabitById(@PathVariable Long id,
        @Parameter(hidden = true) @ValidLanguage Locale locale) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(habitService.getByIdAndLanguageCode(id, locale.getLanguage()));
    }

    /**
     * Method finds all default and custom with created by current user and his
     * friends habits that available for tracking for specific language.
     *
     * @param locale   {@link Locale} with needed language code.
     * @param pageable {@link Pageable} instance.
     * @return Pageable of {@link HabitTranslationDto}.
     */
    @Operation(summary = "Find all habits.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("")
    @ApiPageableWithLocale
    public ResponseEntity<PageableDto<HabitDto>> getAll(
        @Parameter(hidden = true) @CurrentUser UserVO userVO,
        @Parameter(hidden = true) @ValidLanguage Locale locale,
        @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
            habitService.getAllHabitsByLanguageCode(userVO, pageable, locale.getLanguage()));
    }

    /**
     * Method finds shoppingList for habit in specific language.
     *
     * @param locale {@link Locale} with needed language code.
     * @param id     {@link Long} with needed habit id.
     * @return List of {@link ShoppingListItemDto}.
     */
    @Operation(summary = "Get shopping list.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("{id}/shopping-list")
    @ApiLocale
    public ResponseEntity<List<ShoppingListItemDto>> getShoppingListItems(
        @PathVariable Long id,
        @Parameter(hidden = true) @ValidLanguage Locale locale) {
        return ResponseEntity.status(HttpStatus.OK).body(
            habitService.getShoppingListForHabit(id, locale.getLanguage()));
    }

    /**
     * Method finds all habits by tags and language code.
     *
     * @param locale   {@link Locale} with needed language code.
     * @param pageable {@link Pageable} instance.
     * @param tags     {@link List} of {@link String}
     * @return Pageable of {@link HabitDto}.
     */
    @Operation(summary = "Find all habits by tags and language code.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("/tags/search")
    @ApiPageableWithLocale
    public ResponseEntity<PageableDto<HabitDto>> getAllByTagsAndLanguageCode(
        @Parameter(hidden = true) @ValidLanguage Locale locale,
        @RequestParam List<String> tags,
        @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
            habitService.getAllByTagsAndLanguageCode(pageable, tags, locale.getLanguage()));
    }

    /**
     * Method finds all habits by tags, isCustomHabit, complexities and language
     * code.
     *
     * @param locale        {@link Locale} with needed language code.
     * @param pageable      {@link Pageable} instance.
     * @param tags          {@link List} of {@link String}.
     * @param isCustomHabit {@link Boolean} value.
     * @param complexities  {@link List} of {@link Integer}.
     * @return Pageable of {@link HabitDto} instance.
     */
    @Operation(summary = "Find all habits by tags, isCustomHabit, complexities.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED)
    })
    @GetMapping("/search")
    @ApiPageableWithLocale
    public ResponseEntity<PageableDto<HabitDto>> getAllByDifferentParameters(
        @Parameter(hidden = true) @CurrentUser UserVO userVO,
        @Parameter(hidden = true) @ValidLanguage Locale locale,
        @RequestParam(required = false, name = "tags") Optional<List<String>> tags,
        @RequestParam(required = false, name = "isCustomHabit") Optional<Boolean> isCustomHabit,
        @RequestParam(required = false, name = "complexities") Optional<List<Integer>> complexities,
        @Parameter(hidden = true) Pageable pageable) throws BadRequestException {
        if (isValid(tags, isCustomHabit, complexities)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                habitService.getAllByDifferentParameters(userVO, pageable, tags,
                    isCustomHabit, complexities, locale.getLanguage()));
        } else {
            throw new BadRequestException("You should enter at least one parameter");
        }
    }

    /**
     * Method checks if at least one of the input parameters (tags, isCustomHabit,
     * complexities) is present.
     *
     * @param tags          {@link List} of {@link String}.
     * @param isCustomHabit {@link Boolean} value.
     * @param complexities  {@link List} of {@link Integer}.
     *
     * @author Lilia Mokhnatska
     */
    private boolean isValid(Optional<List<String>> tags, Optional<Boolean> isCustomHabit,
        Optional<List<Integer>> complexities) {
        return ((tags.isPresent() && !tags.get().isEmpty()) || isCustomHabit.isPresent()
            || (complexities.isPresent() && !complexities.get().isEmpty()));
    }

    /**
     * The method which returns all habit's tags.
     *
     * @return list of {@link String} (tag's names).
     * @author Markiyan Derevetskyi
     */
    @Operation(summary = "Find all habits tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("/tags")
    @ApiLocale
    public ResponseEntity<List<String>> findAllHabitsTags(@Parameter(hidden = true) @ValidLanguage Locale locale) {
        return ResponseEntity.status(HttpStatus.OK).body(tagsService.findAllHabitsTags(locale.getLanguage()));
    }

    /**
     * Method for creating Custom Habit.
     *
     * @param request {@link AddCustomHabitDtoRequest} - new custom habit dto.
     * @return dto {@link AddCustomHabitDtoResponse}
     *
     * @author Lilia Mokhnatska.
     */
    @Operation(summary = "Add new custom habit.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
            content = @Content(schema = @Schema(implementation = AddCustomHabitDtoResponse.class))),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @PostMapping(path = "/custom", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AddCustomHabitDtoResponse> addCustomHabit(
        @RequestPart @Valid AddCustomHabitDtoRequest request,
        @Parameter(description = "Image of habit") @ImageValidation MultipartFile image,
        @Parameter(hidden = true) Principal principal) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(habitService.addCustomHabit(request, image, principal.getName()));
    }

    /**
     * Retrieves the list of profile pictures of the user's friends (which have
     * INPROGRESS assign to the habit).
     *
     * @param habitId {@link HabitVO} id.
     * @param userVO  {@link UserVO}.
     * @return List of friends profile picture.
     */
    @Operation(
        summary = "Retrieves the list of profile pictures of the user's friends "
            + "(which have INPROGRESS assign to the habit).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{habitId}/friends/profile-pictures")
    public ResponseEntity<List<UserProfilePictureDto>> getFriendsAssignedToHabitProfilePictures(
        @PathVariable Long habitId,
        @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(habitService.getFriendsAssignedToHabitProfilePictures(habitId, userVO.getId()));
    }
}
