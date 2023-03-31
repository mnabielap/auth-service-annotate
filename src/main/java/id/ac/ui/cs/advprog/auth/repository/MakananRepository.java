package id.ac.ui.cs.advprog.auth.repository;

import id.ac.ui.cs.advprog.auth.model.medicine.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MakananRepository extends JpaRepository<Medicine, Integer> {
    @NonNull
    List<Medicine> findAll();
    @NonNull
    Optional<Medicine> findById(@NonNull Integer id);
    void deleteById(@NonNull Integer id);
}
