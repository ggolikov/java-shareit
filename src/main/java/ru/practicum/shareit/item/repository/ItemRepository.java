package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ItemRepository  extends JpaRepository<Item, Integer> {
    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    Collection<Item> searchItems(String text);

    @Modifying
    @Query(value = "insert into comments (text, item_id, author_id, created) values(?1, ?2, ?3, ?4)", nativeQuery = true)
    void addComment(String text, int itemId, int authorId, LocalDateTime created);
}
