package ru.practicum.shareit.gateway.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.booking.dto.AddBookingDto;
import ru.practicum.shareit.gateway.booking.model.BookingSearchStatus;
import ru.practicum.shareit.gateway.client.BaseClient;

import java.util.Map;
@Qualifier("bookingClient")
@Service
public class BookingClient extends BaseClient {
    public static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl,  RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBooking(Long userId, Long id) {
        return get( "/" + id, userId);
    }

    public ResponseEntity<Object> addBooking(Long userId, AddBookingDto addBookingDto) {
        return post("", userId, addBookingDto);
    }

    public ResponseEntity<Object> approveBooking(Long id, Long userId, String approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + id + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getCurrentUserBookings(Long userId, BookingSearchStatus state) {
        return get("", userId, Map.of("state", state));
    }

    public ResponseEntity<Object> getUserItemsBookings(Long userId, BookingSearchStatus state) {
        return get( "/owner", userId, Map.of("state", state));
    }
}
