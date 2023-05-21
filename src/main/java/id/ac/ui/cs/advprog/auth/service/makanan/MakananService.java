package id.ac.ui.cs.advprog.auth.service.makanan;

import id.ac.ui.cs.advprog.auth.dto.makanan.MakananRequest;
import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MakananService {
    List<Makanan> findAll();
    Makanan findById(Integer id);
    Makanan create(MakananRequest request);
    Makanan update(Integer id, MakananRequest request);
    void delete(Integer id);
}
