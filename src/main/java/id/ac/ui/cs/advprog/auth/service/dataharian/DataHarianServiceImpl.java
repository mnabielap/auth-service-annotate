package id.ac.ui.cs.advprog.auth.service.dataharian;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianAdminResponse;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianUserResponse;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.exceptions.DataHarianDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
import id.ac.ui.cs.advprog.auth.repository.MakananRepository;
import id.ac.ui.cs.advprog.auth.repository.DataHarianDetailsRepository;
import id.ac.ui.cs.advprog.auth.repository.DataHarianRepository;
import id.ac.ui.cs.advprog.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataHarianServiceImpl implements DataHarianService {
    private final DataHarianRepository dataHarianRepository;
    private final DataHarianDetailsRepository dataHarianDetailsRepository;
    private final MakananRepository makananRepository;
    private final UserRepository userRepository;

    @Override
    public List<DataHarianAdminResponse> findAll() {
        return dataHarianRepository.findAll()
                .stream()
                .map(dataHarian -> DataHarianAdminResponse.fromDataHarian(dataHarian, dataHarianDetailsRepository.findAllByDataHarianId(dataHarian.getId())))
                .toList();
    }

    @Override
    public List<DataHarianUserResponse> findAllByUserId(Integer userId) {
        return dataHarianRepository.findAllByUserId(userId)
                .stream()
                .map(dataHarian -> DataHarianUserResponse.fromDataHarian(dataHarian, dataHarianDetailsRepository.findAllByDataHarianId(dataHarian.getId())))
                .toList();
    }

    @Override
    public DataHarian create(Integer userId, DataHarianRequest dataHarianRequest) {
        var dataHarian = DataHarian.builder()
                .tanggal(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        dataHarianRepository.save(dataHarian);
        dataHarianRequest.getDataHarianDetailsData().forEach(details -> {
            var makanan = makananRepository.findById(details.getMakananId());
            if (makanan.isEmpty()) {
                throw new MakananDoesNotExistException(details.getMakananId());
            }
            dataHarianDetailsRepository.save(
                    DataHarianDetails.builder()
                            .dataHarian(dataHarian)
                            .quantity(details.getQuantity())
                            .totalKalori(details.getQuantity()*makanan.get().getKalori())
                            .makanan(makanan.get())
                            .build()
            );
        });
        return dataHarian;
    }
    @Override
    public DataHarian update(Integer userId, Integer id, DataHarianRequest dataHarianRequest) {
        if (isDataHarianDoesNotExist(id)) {
            throw new DataHarianDoesNotExistException(id);
        }
        var dataHarian = DataHarian.builder()
                .id(id)
                .tanggal(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        dataHarianRepository.save(dataHarian);

        var listOfDataHarianDetailsInDB = dataHarianDetailsRepository.findAllByDataHarianId(id);
        dataHarianRequest.getDataHarianDetailsData().forEach(details -> {
            var makanan = makananRepository.findById(details.getMakananId());
            if (makanan.isEmpty()) {
                throw new MakananDoesNotExistException(details.getMakananId());
            }

            var dataHarianDetails = dataHarianDetailsRepository.findByDataHarianIdAndMakananId(dataHarian.getId(), makanan.get().getId());
            if (dataHarianDetails.isEmpty()) {
                dataHarianDetailsRepository.save(
                        DataHarianDetails.builder()
                                .dataHarian(dataHarian)
                                .quantity(details.getQuantity())
                                .totalKalori(details.getQuantity()*makanan.get().getKalori())
                                .makanan(makanan.get())
                                .build()
                );
            } else {
                listOfDataHarianDetailsInDB.remove(dataHarianDetails.get());
                dataHarianDetailsRepository.save(
                        DataHarianDetails.builder()
                                .id(dataHarianDetails.get().getId())
                                .dataHarian(dataHarian)
                                .quantity(details.getQuantity())
                                .totalKalori(details.getQuantity()*makanan.get().getKalori())
                                .makanan(makanan.get())
                                .build()
                );
            }
        });
        dataHarianDetailsRepository.deleteAll(listOfDataHarianDetailsInDB);
        return dataHarian;
    }

    @Override
    public void delete(Integer id) {
        if (isDataHarianDoesNotExist(id))
            throw new DataHarianDoesNotExistException(id);
        dataHarianRepository.deleteById(id);
    }

    private boolean isDataHarianDoesNotExist(Integer id) {
        return dataHarianRepository.findById(id).isEmpty();
    }
}
