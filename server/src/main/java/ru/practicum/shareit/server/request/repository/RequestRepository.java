package ru.practicum.shareit.server.request.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.server.request.model.Request;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("select r from Request r where r.requestorId = ?1")
    Collection<Request> getUserRequests(Integer userId);

    @Query("select r from Request r where r.requestorId != ?1")
    Collection<Request> getOtherUserRequests(Integer userId);
}
