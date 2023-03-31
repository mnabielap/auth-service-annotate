package id.ac.ui.cs.advprog.auth.service.makanan;

import id.ac.ui.cs.advprog.auth.dto.MakananRequest;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import id.ac.ui.cs.advprog.auth.model.makanan.MakananCategory;
import id.ac.ui.cs.advprog.auth.repository.MakananRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MakananServiceImpl implements MakananService {
    private final MakananRepository makananRepository;
    @Override
    public List<Makanan> findAll() {
        return makananRepository.findAll()
                .stream()
                .toList();
    }

    @Override
    public Makanan findById(Integer id) {
        if (isMakananDoesNotExist(id))
            throw new MakananDoesNotExistException(id);
        return makananRepository.findById(id).get();
    }

    @Override
    public Makanan create(MakananRequest request) {
        Makanan makanan = new Makanan();
        makanan = setMakananFromRequest(makanan, request);
        return makananRepository.save(makanan);
    }

    @Override
    public Makanan update(Integer id, MakananRequest request) {
        if (isMakananDoesNotExist(id)) {
            throw new MakananDoesNotExistException(id);
        }
        Makanan makanan = findById(id);
        makanan = setMakananFromRequest(makanan, request);
        return this.makananRepository.save(makanan);
    }

    private Makanan setMakananFromRequest(Makanan medicine, MakananRequest request) {
        medicine.setName(request.getName());
        medicine.setKeterangan(request.getKeterangan());
        medicine.setKalori(request.getKalori());
        medicine.setCategory(MakananCategory.valueOf(request.getCategory()));
        medicine.setManufacturer(request.getManufacturer());
        medicine.setStock(request.getStock());
        return medicine;
    }

    @Override
    public void delete(Integer id) {
        if (isMakananDoesNotExist(id)) {
            throw new MakananDoesNotExistException(id);
        }
        makananRepository.deleteById(id);
    }

    private boolean isMakananDoesNotExist(Integer id) {
        return makananRepository.findById(id).isEmpty();
    }
}
