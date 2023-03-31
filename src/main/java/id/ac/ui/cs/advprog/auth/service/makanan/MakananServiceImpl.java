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
        if (isMedicineDoesNotExist(id))
            throw new MakananDoesNotExistException(id);
        return makananRepository.findById(id).get();
    }

    @Override
    public Makanan create(MakananRequest request) {
        Makanan medicine = new Makanan();
        medicine = setMedicideFromRequest(medicine, request);
        return makananRepository.save(medicine);
    }

    @Override
    public Makanan update(Integer id, MakananRequest request) {
        if (isMedicineDoesNotExist(id)) {
            throw new MakananDoesNotExistException(id);
        }
        Makanan medicine = findById(id);
        medicine = setMedicideFromRequest(medicine, request);
        return this.makananRepository.save(medicine);
    }

    private Makanan setMedicideFromRequest(Makanan medicine, MakananRequest request) {
        medicine.setName(request.getName());
        medicine.setKeterangan(request.getDose());
        medicine.setKalori(request.getPrice());
        medicine.setCategory(MakananCategory.valueOf(request.getCategory()));
        medicine.setManufacturer(request.getManufacturer());
        medicine.setStock(request.getStock());
        return medicine;
    }

    @Override
    public void delete(Integer id) {
        if (isMedicineDoesNotExist(id)) {
            throw new MakananDoesNotExistException(id);
        }
        makananRepository.deleteById(id);
    }

    private boolean isMedicineDoesNotExist(Integer id) {
        return makananRepository.findById(id).isEmpty();
    }
}
