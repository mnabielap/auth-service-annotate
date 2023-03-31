package id.ac.ui.cs.advprog.auth.repository;

import id.ac.ui.cs.advprog.auth.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataHarianRepository extends JpaRepository<Order, Integer> {
    @NonNull
    List<Order> findAll();

    List<Order> findAllByUserId(Integer userId);
    @NonNull
    Optional<Order> findById(@NonNull Integer id);
}
