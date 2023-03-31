package id.ac.ui.cs.advprog.auth.service.makanan;

import id.ac.ui.cs.advprog.auth.dto.MakananRequest;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.medicine.Medicine;
import id.ac.ui.cs.advprog.auth.model.medicine.MedicineCategory;
import id.ac.ui.cs.advprog.auth.repository.MakananRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MakananServiceImpl implements MakananService {
    private final MakananRepository makananRepository;
    @Override
    public List<Medicine> findAll() {
        // TODO: Lengkapi kode berikut
        return makananRepository.findAll()
                .stream()
                .toList();
    }

    @Override
    public Medicine findById(Integer id) {
        // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
        if (isMedicineDoesNotExist(id))
            throw new MakananDoesNotExistException(id);
        return makananRepository.findById(id).get();
    }

    @Override
    public Medicine create(MakananRequest request) {
        // TODO: Lengkapi kode berikut
        Medicine medicine = new Medicine();
        medicine = setMedicideFromRequest(medicine, request);
        return makananRepository.save(medicine);
    }

    @Override
    public Medicine update(Integer id, MakananRequest request) {
        // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
        if (isMedicineDoesNotExist(id)) {
            throw new MakananDoesNotExistException(id);
        }
        Medicine medicine = findById(id);
        medicine = setMedicideFromRequest(medicine, request);
        return this.makananRepository.save(medicine);
    }

    private Medicine setMedicideFromRequest(Medicine medicine, MakananRequest request) {
        medicine.setName(request.getName());
        medicine.setDose(request.getDose());
        medicine.setPrice(request.getPrice());
        medicine.setCategory(MedicineCategory.valueOf(request.getCategory()));
        medicine.setManufacturer(request.getManufacturer());
        medicine.setStock(request.getStock());
        return medicine;
    }

    @Override
    public void delete(Integer id) {
        // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
        if (isMedicineDoesNotExist(id)) {
            throw new MakananDoesNotExistException(id);
        }
        makananRepository.deleteById(id);
    }

    private boolean isMedicineDoesNotExist(Integer id) {
        return makananRepository.findById(id).isEmpty();
    }
}
