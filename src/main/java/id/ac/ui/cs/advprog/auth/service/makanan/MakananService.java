package id.ac.ui.cs.advprog.auth.service.makanan;

import id.ac.ui.cs.advprog.auth.dto.MakananRequest;
import id.ac.ui.cs.advprog.auth.model.medicine.Medicine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MakananService {
    List<Medicine> findAll();
    Medicine findById(Integer id);
    Medicine create(MakananRequest request);
    Medicine update(Integer id, MakananRequest request);
    void delete(Integer id);
}
