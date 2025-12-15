package ru.practicum.shareit.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void searchItems_shouldFindByNameIgnoreCase() {
        User owner = createUser("owner");

        Item item = createItem("Drill", "Powerful tool", owner);
        em.flush();

        Collection<Item> result = itemRepository.searchItems("dRi");

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Item::getId)
                .isEqualTo(item.getId());
    }

    @Test
    void searchItems_shouldFindByDescriptionIgnoreCase() {
        User owner = createUser("owner");

        Item item = createItem("Hammer", "Very STRONG tool", owner);
        em.flush();

        Collection<Item> result = itemRepository.searchItems("strong");

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Item::getName)
                .isEqualTo("Hammer");
    }

    @Test
    void searchItems_shouldReturnEmptyWhenNoMatches() {
        User owner = createUser("owner");

        createItem("Hammer", "Tool", owner);
        em.flush();

        Collection<Item> result = itemRepository.searchItems("phone");

        assertThat(result).isEmpty();
    }

    @Test
    void addComment_shouldInsertCommentIntoDatabase() {
        User owner = createUser("owner");
        User author = createUser("author");

        Item item = createItem("Item", "Description", owner);
        em.flush();

        LocalDateTime created = LocalDateTime.now();

        itemRepository.addComment(
                "Nice item",
                item.getId(),
                author.getId(),
                created
        );

        // verify native insert worked
        Long count = em.getEntityManager()
                .createQuery(
                        "select count(c) from Comment c where c.item.id = :itemId",
                        Long.class
                )
                .setParameter("itemId", item.getId())
                .getSingleResult();

        assertThat(count).isEqualTo(1);
    }

    /* ---------- Helpers ---------- */

    private User createUser(String name) {
        User user = new User();
        user.setName(name);
        user.setEmail(name + "@mail.com");
        em.persist(user);
        return user;
    }

    private Item createItem(String name, String description, User owner) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(true);
        item.setOwner(owner);
        em.persist(item);
        return item;
    }
}