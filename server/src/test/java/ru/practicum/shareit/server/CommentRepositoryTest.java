package ru.practicum.shareit.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.CommentRepository;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void getItemComments_shouldReturnCommentsForItem() {
        User owner = createUser("owner");
        User author = createUser("author");

        Item item = createItem("Item", "Description", owner);

        Comment comment1 = createComment("Good", item, author);
        Comment comment2 = createComment("Excellent", item, author);

        em.flush();

        Collection<Comment> result =
                commentRepository.getItemComments(item.getId());

        assertThat(result)
                .hasSize(2)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("Good", "Excellent");
    }

    @Test
    void getItemComments_shouldReturnEmptyWhenNoComments() {
        User owner = createUser("owner");
        Item item = createItem("Item", "Description", owner);

        em.flush();

        Collection<Comment> result =
                commentRepository.getItemComments(item.getId());

        assertThat(result).isEmpty();
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

    private Comment createComment(String text, Item item, User author) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        em.persist(comment);
        return comment;
    }
}
