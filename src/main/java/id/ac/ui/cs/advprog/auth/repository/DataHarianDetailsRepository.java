package id.ac.ui.cs.advprog.auth.repository;

import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataHarianDetailsRepository extends JpaRepository<DataHarianDetails, Integer> {
    @NonNull
    List<DataHarianDetails> findAll();
    List<DataHarianDetails> findAllByDataHarianId(Integer dataHarianId);
    Optional<DataHarianDetails> findByDataHarianIdAndMakananId(Integer dataHarianId, Integer makananId);
}
