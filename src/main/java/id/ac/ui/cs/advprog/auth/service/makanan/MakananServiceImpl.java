package id.ac.ui.cs.advprog.auth.service.makanan;

import id.ac.ui.cs.advprog.auth.dto.makanan.MakananRequest;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import id.ac.ui.cs.advprog.auth.model.makanan.MakananCategory;
import id.ac.ui.cs.advprog.auth.repository.MakananRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Makanan> optionalMakanan = makananRepository.findById(id);
        return optionalMakanan.orElse(null);
    }

    @Override
    public Makanan create(MakananRequest request) {
        var makanan = new Makanan();
        setMakananFromRequest(makanan, request);
        return makananRepository.save(makanan);
    }

    @Override
    public Makanan update(Integer id, MakananRequest request) {
        if (isMakananDoesNotExist(id)) {
            throw new MakananDoesNotExistException(id);
        }
        var makanan = findById(id);
        setMakananFromRequest(makanan, request);
        return this.makananRepository.save(makanan);
    }

    private void setMakananFromRequest(Makanan makanan, MakananRequest request) {
        makanan.setName(request.getName());
        makanan.setKeterangan(request.getKeterangan());
        makanan.setKalori(request.getKalori());
        makanan.setCategory(MakananCategory.valueOf(request.getCategory()));
        makanan.setManufacturer(request.getManufacturer());
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
