package ru.practicum.shareit.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.repository.RequestRepository;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    void getUserRequests_shouldReturnOnlyUserRequests() {
        User user1 = createUser("user1");
        User user2 = createUser("user2");

        Request request1 = createRequest("Need a drill", user1);
        createRequest("Need a hammer", user2);

        em.flush();

        Collection<Request> result =
                requestRepository.getUserRequests(user1.getId());

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Request::getId)
                .isEqualTo(request1.getId());
    }

    @Test
    void getOtherUserRequests_shouldReturnRequestsOfOtherUsers() {
        User user1 = createUser("user1");
        User user2 = createUser("user2");

        createRequest("Need a drill", user1);
        Request request2 = createRequest("Need a hammer", user2);

        em.flush();

        Collection<Request> result =
                requestRepository.getOtherUserRequests(user1.getId());

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Request::getId)
                .isEqualTo(request2.getId());
    }

    @Test
    void getUserRequests_shouldReturnEmptyWhenUserHasNoRequests() {
        User user = createUser("user");

        em.flush();

        Collection<Request> result =
                requestRepository.getUserRequests(user.getId());

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

    private Request createRequest(String description, User requestor) {
        Request request = new Request();
        request.setDescription(description);
        request.setRequestorId(requestor.getId());
        request.setCreated(LocalDateTime.now());
        em.persist(request);
        return request;
    }
}