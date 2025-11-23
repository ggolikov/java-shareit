package ru.practicum.shareit.booking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b where b.booker.id = ?1")
    Collection<Booking> getUserBookings(Integer userId);

    @Query("select b from Booking b where b.item.id = ?1 order by b.start asc")
    Collection<Booking> getItemBookings(Integer itemId);

    @Query(value = """
        select * from bookings where item_id IN (
            select it.id from items as it where it.owner_id = ?1
        )
""", nativeQuery = true)

    Collection<Booking> getUserItemsBookings(Integer itemId);
}
