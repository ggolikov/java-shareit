package ru.practicum.shareit.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingStatus;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void getUserBookings_shouldReturnBookingsByBookerId() {
        User owner = createUser("owner");
        User booker = createUser("booker");

        Item item = createItem("item", "desc", owner);

        Booking booking = createBooking(item,  booker);
        em.persist(booking);
        em.flush();

        Collection<Booking> result =
                bookingRepository.getUserBookings(booker.getId());

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Booking::getId)
                .isEqualTo(booking.getId());
    }

    @Test
    void getItemBookings_shouldReturnBookingsOrderedByStartAsc() {
        User owner = createUser("owner");
        User booker = createUser("booker");

        Item item = createItem("item", "desc", owner);

        Booking early = createBooking(item, booker,
                LocalDateTime.now().minusDays(2));
        Booking later = createBooking(item, booker,
                LocalDateTime.now().plusDays(2));

        em.persist(later);
        em.persist(early);
        em.flush();

        Collection<Booking> result =
                bookingRepository.getItemBookings(item.getId());

        assertThat(result)
                .hasSize(2)
                .extracting(Booking::getStart)
                .containsExactly(
                        early.getStart(),
                        later.getStart()
                );
    }

    @Test
    void getUserItemsBookings_shouldReturnBookingsForItemsOwnedByUser() {
        User owner = createUser("owner");
        User booker = createUser("booker");

        Item item = createItem("item", "desc", owner);

        Booking booking = createBooking(item, booker);
        em.persist(booking);
        em.flush();

        Collection<Booking> result =
                bookingRepository.getUserItemsBookings(owner.getId());

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Booking::getItem)
                .extracting(Item::getId)
                .isEqualTo(item.getId());
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
        item.setOwner(owner);
        item.setAvailable(true);
        em.persist(item);
        return item;
    }

    private Booking createBooking(Item item, User booker) {
        return createBooking(item, booker, LocalDateTime.now());
    }

    private Booking createBooking(Item item, User booker, LocalDateTime start) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(start);
        booking.setEnd(start.plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }
}