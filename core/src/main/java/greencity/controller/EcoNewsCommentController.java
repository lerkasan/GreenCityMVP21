package greencity.controller;

import greencity.annotations.ApiPageable;
import greencity.annotations.ApiPageableWithoutSort;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.*;
import greencity.dto.user.UserVO;
import greencity.service.EcoNewsCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/econews/comments")
public class EcoNewsCommentController {
    private final EcoNewsCommentService ecoNewsCommentService;

    /**
     * Method for creating {@link EcoNewsCommentVO}.
     *
     * @param econewsId id of {@link EcoNewsVO} to add comment to.
     * @param request   - dto for {@link EcoNewsCommentVO} entity.
     * @return dto {@link AddEcoNewsCommentDtoResponse}
     */
    @Operation(summary = "Add comment.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
            content = @Content(schema = @Schema(implementation = AddEcoNewsCommentDtoResponse.class))),
        @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("{econewsId}")
    public ResponseEntity<AddEcoNewsCommentDtoResponse> save(@PathVariable Long econewsId,
        @Valid @RequestBody AddEcoNewsCommentDtoRequest request,
        @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ecoNewsCommentService.save(econewsId, request, user));
    }

    /**
     * Method to count not deleted comments to certain {@link EcoNewsVO}.
     *
     * @param ecoNewsId to specify {@link EcoNewsVO}
     * @return amount of comments
     */
    @Operation(summary = "Count comments.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/count/comments/{ecoNewsId}")
    public int getCountOfComments(@PathVariable Long ecoNewsId) {
        return ecoNewsCommentService.countOfComments(ecoNewsId);
    }

    /**
     * Method to get all replies to {@link EcoNewsCommentVO} specified by
     * parentCommentId.
     *
     * @param parentCommentId specifies parent comment to all replies
     * @return Pageable of {@link EcoNewsCommentDto} replies
     */
    @Operation(summary = "Get all replies to comment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("replies/{parentCommentId}")
    @ApiPageable
    public ResponseEntity<PageableDto<EcoNewsCommentDto>> findAllReplies(@Parameter(hidden = true) Pageable pageable,
        @PathVariable Long parentCommentId, @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ecoNewsCommentService.findAllReplies(pageable, parentCommentId, user));
    }

    /**
     * Method to count replies to certain {@link EcoNewsCommentVO}.
     *
     * @param parentCommentId specifies parent comment to all replies
     * @return amount of replies
     */
    @Operation(summary = "Get count of replies to comment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("count/replies/{parentCommentId}")
    public int getCountOfReplies(@PathVariable Long parentCommentId) {
        return ecoNewsCommentService.countReplies(parentCommentId);
    }

    /**
     * Method to mark comment as deleted.
     *
     * @param id comment id
     */
    @Operation(summary = "Mark comment as deleted.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("")
    public ResponseEntity<Object> delete(Long id, @Parameter(hidden = true) @CurrentUser UserVO user) {
        ecoNewsCommentService.deleteById(id, user);
        return ResponseEntity.ok().build();
    }

    /**
     * Method to update certain {@link EcoNewsCommentVO} specified by id.
     *
     * @param id   of {@link EcoNewsCommentVO} to update
     * @param text new text of {@link EcoNewsCommentVO}
     */
    @Operation(summary = "Update comment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PatchMapping("")
    public void update(Long id, @RequestParam @NotBlank String text,
        @Parameter(hidden = true) @CurrentUser UserVO user) {
        ecoNewsCommentService.update(text, id, user);
    }

    /**
     * Method to like/dislike certain {@link EcoNewsCommentVO} specified by id.
     *
     * @param id of {@link EcoNewsCommentVO} to like/dislike
     */
    @Operation(summary = "Like comment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("like")
    public void like(@RequestParam("id") Long id, @Parameter(hidden = true) @CurrentUser UserVO user) {
        ecoNewsCommentService.like(id, user);
    }

    /**
     * Method to like/dislike comment and count likes.
     *
     * @param amountCommentLikesDto dto with id and count likes for comments.
     */
    @MessageMapping("/likeAndCount")
    public void getCountOfLike(@Payload AmountCommentLikesDto amountCommentLikesDto) {
        ecoNewsCommentService.countLikes(amountCommentLikesDto);
    }

    /**
     * Method to get all active comments to {@link EcoNewsVO} specified by
     * ecoNewsId.
     *
     * @param ecoNewsId id of {@link EcoNewsVO}
     * @return Pageable of {@link EcoNewsCommentDto}
     * @author Taras Dovganyuk
     */
    @Operation(summary = "Get all active comments.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/active")
    @ApiPageableWithoutSort
    public ResponseEntity<PageableDto<EcoNewsCommentDto>> getAllActiveComments(
        @Parameter(hidden = true) Pageable pageable, Long ecoNewsId,
        @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ecoNewsCommentService.getAllActiveComments(pageable, user, ecoNewsId));
    }

    /**
     * Method to get all active replies to {@link EcoNewsCommentVO} specified by
     * parentCommentId.
     *
     * @param parentCommentId specifies parent comment to all replies
     * @return Pageable of {@link EcoNewsCommentDto} replies
     * @author Dovganyuk Taras
     */
    @Operation(summary = "Get all active replies to comment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("replies/active/{parentCommentId}")
    @ApiPageable
    public ResponseEntity<PageableDto<EcoNewsCommentDto>> findAllActiveReplies(
        @Parameter(hidden = true) Pageable pageable, @PathVariable Long parentCommentId,
        @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ecoNewsCommentService.findAllActiveReplies(pageable, parentCommentId, user));
    }
}