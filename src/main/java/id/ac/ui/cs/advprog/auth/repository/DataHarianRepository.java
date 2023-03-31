package id.ac.ui.cs.advprog.auth.repository;

import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataHarianRepository extends JpaRepository<DataHarian, Integer> {
    @NonNull
    List<DataHarian> findAll();

    List<DataHarian> findAllByUserId(Integer userId);
    @NonNull
    Optional<DataHarian> findById(@NonNull Integer id);
}
