package greencity.repository;

import greencity.entity.localization.ShoppingListItemTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ShoppingListItemTranslationRepo extends JpaRepository<ShoppingListItemTranslation, Long> {
    /**
     * Method for getting all shopping list item translations for given language.
     *
     * @param languageCode code of needed language
     * @return List of {@link ShoppingListItemTranslation}, that contains all
     *         shopping list item translations for needed language.
     */
    List<ShoppingListItemTranslation> findAllByLanguageCode(String languageCode);

    /**
     * Method returns available shopping list items translations for specific user
     * and language code.
     *
     * @param userId       target user id
     * @param languageCode code of needed language
     * @return List of available {@link ShoppingListItemTranslation}'s.
     */
    @Query("SELECT g FROM ShoppingListItemTranslation g WHERE g.shoppingListItem.id NOT IN "
        + "(SELECT ug.shoppingListItem.id FROM UserShoppingListItem ug WHERE ug.habitAssign.id = ?1 "
        + "AND ug.status = 'ACTIVE') AND g.language.code = ?2")
    List<ShoppingListItemTranslation> findAvailableByUserId(Long userId, String languageCode);

    /**
     * Method returns shopping list item translation for particular selected item
     * for specific user and language code.
     *
     * @param itemId       target user id
     * @param languageCode code of needed language
     * @return {@link ShoppingListItemTranslation}
     */
    @Query(nativeQuery = true, value = "SELECT * FROM shopping_list_item_translations as g "
        + "where g.shopping_list_item_id = (SELECT ug.shopping_list_item_id FROM user_shopping_list as ug WHERE "
        + "ug.id=:itemId) AND g.language_id = (SELECT id FROM languages l where l.code =:languageCode)")
    ShoppingListItemTranslation findByLangAndUserShoppingListItemId(String languageCode, Long itemId);

    /**
     * Method updates Shopping list item translation for particular selected item
     * for specific language code.
     *
     * @param itemId       target shopping list item id
     * @param languageCode code of needed language
     * @param content      new content
     * @return {@link ShoppingListItemTranslation}
     */
    @Query("UPDATE ShoppingListItemTranslation SET content=?3"
        + "WHERE id = ?1 AND language.code = ?2")
    ShoppingListItemTranslation updateTranslationContent(Long itemId, String languageCode, String content);

    /**
     * Method for getting shopping list translations for given habit in specific
     * language.
     *
     * @param languageCode code of needed language
     * @param habitId      code of needed language
     * @return List of {@link ShoppingListItemTranslation}, that contains all
     *         shopping list item translations for needed habit.
     */
    @Query("SELECT gt FROM ShoppingListItemTranslation gt JOIN ShoppingListItem g ON g.id = gt.shoppingListItem.id "
        + "JOIN g.habits h ON h.id = :habitId"
        + " WHERE gt.language.code = :languageCode")
    List<ShoppingListItemTranslation> findShoppingListByHabitIdAndByLanguageCode(String languageCode,
        @Param(value = "habitId") Long habitId);
}
