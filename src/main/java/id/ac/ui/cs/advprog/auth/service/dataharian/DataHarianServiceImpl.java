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
                .map(order -> DataHarianAdminResponse.fromOrder(order, dataHarianDetailsRepository.findAllByDataHarianId(order.getId())))
                .toList();
    }

    @Override
    public List<DataHarianUserResponse> findAllByUserId(Integer userId) {
        return dataHarianRepository.findAllByUserId(userId)
                .stream()
                .map(order -> DataHarianUserResponse.fromOrder(order, dataHarianDetailsRepository.findAllByDataHarianId(order.getId())))
                .toList();
    }

    @Override
    public DataHarian create(Integer userId, DataHarianRequest orderRequest) {
        var dataHarian = DataHarian.builder()
                .tanggal(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        dataHarianRepository.save(dataHarian);
        orderRequest.getOrderDetailsData().forEach(details -> {
            var makanan = makananRepository.findById(details.getMedicineId());
            if (makanan.isEmpty()) {
                throw new MakananDoesNotExistException(details.getMedicineId());
            }
            dataHarianDetailsRepository.save(
                    DataHarianDetails.builder()
                            .dataHarian(dataHarian)
                            .quantity(details.getQuantity())
                            .totalKalori(details.getTotalPrice())
                            .makanan(makanan.get())
                            .build()
            );
        });
        return dataHarian;
    }
    @Override
    public DataHarian update(Integer userId, Integer id, DataHarianRequest orderRequest) {
        if (isOrderDoesNotExist(id)) {
            throw new DataHarianDoesNotExistException(id);
        }
        var dataHarian = DataHarian.builder()
                .id(id)
                .tanggal(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        dataHarianRepository.save(dataHarian);

        var listOfOrderDetailsInDB = dataHarianDetailsRepository.findAllByDataHarianId(id);
        orderRequest.getOrderDetailsData().forEach(details -> {
            var makanan = makananRepository.findById(details.getMedicineId());
            if (makanan.isEmpty()) {
                throw new MakananDoesNotExistException(details.getMedicineId());
            }

            // Update Order includes the updates of OrderDetails.
            // There are 3 scenarios of OrderDetails update:
            // 1. OrderDetails exists both in Database and Put Request
            // 2. OrderDetails exists only in Put Request
            // 3. OrderDetails exists only in Database

            var orderDetails = dataHarianDetailsRepository.findByDataHarianIdAndMakananId(dataHarian.getId(), makanan.get().getId());
            if (orderDetails.isEmpty()) {
                dataHarianDetailsRepository.save(
                        DataHarianDetails.builder()
                                .dataHarian(dataHarian)
                                .quantity(details.getQuantity())
                                .totalKalori(details.getTotalPrice())
                                .makanan(makanan.get())
                                .build()
                );
            } else {
                listOfOrderDetailsInDB.remove(orderDetails.get());
                dataHarianDetailsRepository.save(
                        DataHarianDetails.builder()
                                .id(orderDetails.get().getId())
                                .dataHarian(dataHarian)
                                .quantity(details.getQuantity())
                                .totalKalori(details.getTotalPrice())
                                .makanan(makanan.get())
                                .build()
                );
            }
        });
        dataHarianDetailsRepository.deleteAll(listOfOrderDetailsInDB);
        return dataHarian;
    }

    @Override
    public void delete(Integer id) {
        if (isOrderDoesNotExist(id))
            throw new DataHarianDoesNotExistException(id);
        dataHarianRepository.deleteById(id);
    }

    private boolean isOrderDoesNotExist(Integer id) {
        return dataHarianRepository.findById(id).isEmpty();
    }
}
