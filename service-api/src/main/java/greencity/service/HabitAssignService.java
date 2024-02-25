package greencity.service;

import greencity.dto.habit.*;
import greencity.dto.user.UserVO;
import greencity.enums.HabitAssignStatus;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface HabitAssignService {
    /**
     * Method to find {@code HabitAssign} by habitAssignId, userId and specific
     * language.
     *
     * @param userId        {@code User} id.
     * @param habitAssignId {@code HabitAssign} id.
     * @param language      {@link String} of language code value.
     * @return {@link HabitAssignDto}.
     */
    HabitAssignDto getByHabitAssignIdAndUserId(Long habitAssignId, Long userId, String language);

    /**
     * Method for assigning {@code Habit} with default properties.
     *
     * @param habitId {@code Habit} id.
     * @param user    @link UserVO} id.
     * @return {@link HabitAssignManagementDto}.
     */
    HabitAssignManagementDto assignDefaultHabitForUser(Long habitId, UserVO user);

    /**
     * Method for assigning {@code Habit} with custom properties.
     *
     * @param habitId                        {@code Habit} id.
     * @param user                           {@link UserVO} id.
     * @param habitAssignCustomPropertiesDto {@link HabitAssignCustomPropertiesDto}
     *                                       instance.
     * @return {@link HabitAssignManagementDto}.
     */
    List<HabitAssignManagementDto> assignCustomHabitForUser(Long habitId, UserVO user,
        HabitAssignCustomPropertiesDto habitAssignCustomPropertiesDto);

    /**
     * Method updates {@code HabitAssign} duration of habit assigned for user.
     *
     * @param habitAssignId {@code AssignHabit} id.
     * @param userId        {@link Long} id.
     * @param duration      {@link Integer} with needed duration.
     * @return {@link HabitAssignUserDurationDto}.
     * @author Anton Bondar
     */
    HabitAssignUserDurationDto updateUserHabitInfoDuration(Long habitAssignId, Long userId, Integer duration);

    /**
     * Method to find all custom habit assigns by {@code User} id.
     *
     * @param userId   {@code User} id.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto} instances.
     */
    List<HabitAssignDto> getAllCustomHabitAssignsByUserId(Long userId, String language);

    /**
     * Method to get end date of assigned habit.
     *
     * @param habitAssign {@link HabitAssignDto} instance.
     * @return {@link ZonedDateTime} end date.
     */
    ZonedDateTime getEndDate(HabitAssignDto habitAssign);

    /**
     * Method to get readiness percent of assigned habit.
     *
     * @param habitAssign {@link HabitAssignDto} instance.
     * @return {@link Integer} readiness percent.
     */
    Integer getReadinessPercent(HabitAssignDto habitAssign);

    /**
     * Method to find {@code HabitAssign} by {@code Habit} id and {@code User} id.
     *
     * @param userId   {@code User} id.
     * @param habitId  {@code Habit} id.
     * @param language {@link String} of language code value.
     * @return HabitAssignDto.
     */
    HabitAssignDto findHabitAssignByUserIdAndHabitId(Long userId, Long habitId, String language);

    /**
     * Method to find {@code HabitAssign} by {@code HabitAssign} id and {@code User}
     * id.
     *
     * @param userId        {@code User} id.
     * @param habitAssignId {@code HabitAssign} id.
     * @param language      {@link String} of language code value.
     * @return HabitDto.
     */
    HabitDto findHabitByUserIdAndHabitAssignId(Long userId, Long habitAssignId, String language);

    /**
     * Method to find all (not cancelled) {@code HabitAssign}'s by {@code User} id
     * and acquired status.
     *
     * @param userId   {@code User} id.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto}.
     */
    List<HabitAssignDto> getAllHabitAssignsByUserIdAndStatusNotCancelled(Long userId, String language);

    /**
     * Method that return user shopping list and custom shopping list by
     * habitAssignId for specific language.
     *
     * @param userId        {@code User} id.
     * @param habitAssignId {@code HabitAssignId} id.
     * @param language      {@link String} of language code value.
     * @return {@link UserShoppingAndCustomShoppingListsDto} instance.
     */
    UserShoppingAndCustomShoppingListsDto getUserShoppingAndCustomShoppingLists(
        Long userId, Long habitAssignId, String language);

    /**
     * Method that finds list of user shopping list items and custom shopping list
     * items by userId, specific language and INPROGRESS status.
     *
     * @param userId   {@link Long} id.
     * @param language {@link String} of language code value.
     * @return {@link UserShoppingAndCustomShoppingListsDto}.
     * @author Lilia Mokhnatska
     */
    List<UserShoppingAndCustomShoppingListsDto> getListOfUserAndCustomShoppingListsWithStatusInprogress(Long userId,
        String language);

    /**
     * Method to find all(not cancelled) {@code HabitAssign}'s by {@code Habit} id
     * and acquired status.
     *
     * @param habitId  {@code Habit} id.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto}.
     */
    List<HabitAssignDto> getAllHabitAssignsByHabitIdAndStatusNotCancelled(Long habitId, String language);

    /**
     * Method to find number of {@code HabitAssign}'s by id and status.
     *
     * @param habitId {@code Habit} id.
     * @param status  {@code HabitAssignStatus} status.
     * @return Long number of Acquired.
     * @author Vira Maksymets
     */
    Long getNumberHabitAssignsByHabitIdAndStatus(Long habitId, HabitAssignStatus status);

    /**
     * Method to find all {@code HabitAssign}'s by {@code User} id and acquired
     * status.
     *
     * @param userId   {@code User} id.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto}.
     */
    List<HabitAssignDto> getAllHabitAssignsByUserIdAndStatusAcquired(Long userId, String language);

    /**
     * Method to find all cancelled {@code HabitAssign}'s by {@code User} id.
     *
     * @param userId   {@code User} id.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto}.
     */
    List<HabitAssignDto> getAllHabitAssignsByUserIdAndCancelledStatus(Long userId, String language);

    /**
     * Method to delete all {@code HabitAssign}'s by {@code Habit} instance.
     *
     * @param habit {@link HabitVO} instance.
     */
    void deleteAllHabitAssignsByHabit(HabitVO habit);

    /**
     * Method for updating inprogress, acquired {@code HabitAssign} in database by
     * {@code HabitAssign} id.
     *
     * @param habitAssignId {@code HabitAssign} id.
     * @param dto           {@link HabitAssignStatDto} dto with new cancelled and
     *                      acquired status.
     * @return {@link HabitAssignManagementDto} instance.
     */
    HabitAssignManagementDto updateStatusByHabitAssignId(Long habitAssignId, HabitAssignStatDto dto);

    /**
     * Method to enroll {@code HabitAssign} by habitAssignId.
     *
     * @param habitAssignId {@code HabitAssign} id to enroll.
     * @param userId        {@code User} id.
     * @param date          {@link LocalDate} date we want to enroll.
     * @param language      {@link String} of language code value.
     * @return {@link HabitAssignDto}.
     */
    HabitAssignDto enrollHabit(Long habitAssignId, Long userId, LocalDate date, String language);

    /**
     * Method to unenroll Habit in defined date.
     *
     * @param habitAssignId {@code HabitAssign} id to unenroll.
     * @param userId        {@code User} id.
     * @param date          {@link LocalDate} date we want unenroll.
     * @return {@link HabitAssignDto}.
     */
    HabitAssignDto unenrollHabit(Long habitAssignId, Long userId, LocalDate date);

    /**
     * Method to find all inprogress habit assigns on certain {@link LocalDate}.
     *
     * @param userId   {@code User} id.
     * @param date     {@link LocalDate} instance.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto} instances.
     */
    List<HabitAssignDto> findInprogressHabitAssignsOnDate(Long userId, LocalDate date, String language);

    /**
     * Method to find all inprogress habit assigns on certain including content
     * {@link LocalDate}.
     *
     * @param userId   {@code User} id.
     * @param date     {@link LocalDate} instance.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto} instances.
     */
    List<HabitAssignDto> findInprogressHabitAssignsOnDateContent(Long userId, LocalDate date, String language);

    /**
     * Method to find all inprogress {@link HabitsDateEnrollmentDto} between the
     * specified {@link LocalDate}s.
     *
     * @param userId   {@code User} id.
     * @param from     {@link LocalDate} instance.
     * @param to       {@link LocalDate} instance.
     * @param language {@link String} of language code value.
     * @return list of {@link HabitAssignDto} instances.
     */
    List<HabitsDateEnrollmentDto> findHabitAssignsBetweenDates(Long userId,
        LocalDate from, LocalDate to, String language);

    /**
     * Method add default habit.
     *
     * @param user {@link UserVO} instance.
     */
    void addDefaultHabit(UserVO user, String language);

    /**
     * Method to set {@link HabitAssignVO} status from inprogress to cancelled.
     *
     * @param habitId - id of {@link HabitVO}.
     * @param userId  - id of {@link UserVO} .
     * @return {@link HabitAssignDto}.
     */
    HabitAssignDto cancelHabitAssign(Long habitId, Long userId);

    /**
     * Method delete HabitAssign by habitAssignId for current User.
     *
     * @param habitAssignId {@link Long} id.
     * @param userId        {@link Long} id.
     */
    void deleteHabitAssign(Long habitAssignId, Long userId);

    /**
     * Method save HabitAssign.
     *
     * @param updateUserShoppingListDto {@link UpdateUserShoppingListDto}
     *                                  habitAssignDto.
     */
    void updateUserShoppingListItem(UpdateUserShoppingListDto updateUserShoppingListDto);

    /**
     * Method update shopping item by habit id and item id.
     *
     * @param habitId {@link Long} habit id.
     * @param itemId  {@link Long} item id.
     */
    void updateShoppingItem(Long habitId, Long itemId);

    /**
     * Method that update UserShoppingList and CustomShopping List.
     *
     * <ul>
     * <li>If items are present in the db, method update them;</li>
     * <li>If items don't present in the db and id is null, method try to add it to
     * user;</li>
     * <li>If some items from db don't present in the lists, method delete
     * them(Except items with DISABLED status).</li>
     * </ul>
     *
     * @param userId   {@code User} id.
     * @param habitId  {@code Habit} id.
     * @param listDto  {@link UserShoppingAndCustomShoppingListsDto} User and Custom
     *                 Shopping lists.
     * @param language {@link String} of language code value.
     */
    void fullUpdateUserAndCustomShoppingLists(Long userId, Long habitId, UserShoppingAndCustomShoppingListsDto listDto,
        String language);

    /**
     * Method updates value progressNotificationHasDisplayed to true.
     *
     * @param habitAssignId {@link Long} habit id.
     * @param userId        {@link Long} item id.
     */
    void updateProgressNotificationHasDisplayed(Long habitAssignId, Long userId);
}