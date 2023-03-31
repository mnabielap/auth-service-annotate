package id.ac.ui.cs.advprog.auth.service.dataharian;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianAdminResponse;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianUserResponse;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.exceptions.DataHarianDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.order.Order;
import id.ac.ui.cs.advprog.auth.model.order.OrderDetails;
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
                .map(order -> DataHarianAdminResponse.fromOrder(order, dataHarianDetailsRepository.findAllByOrderId(order.getId())))
                .toList();
    }

    @Override
    public List<DataHarianUserResponse> findAllByUserId(Integer userId) {
        return dataHarianRepository.findAllByUserId(userId)
                .stream()
                .map(order -> DataHarianUserResponse.fromOrder(order, dataHarianDetailsRepository.findAllByOrderId(order.getId())))
                .toList();
    }

    @Override
    public Order create(Integer userId, DataHarianRequest orderRequest) {
        var order = Order.builder()
                .orderDate(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        dataHarianRepository.save(order);
        orderRequest.getOrderDetailsData().forEach(details -> {
            var medicine = makananRepository.findById(details.getMedicineId());
            if (medicine.isEmpty()) {
                // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
                // return;
                throw new MakananDoesNotExistException(details.getMedicineId());
            }
            dataHarianDetailsRepository.save(
                    OrderDetails.builder()
                            .order(order)
                            .quantity(details.getQuantity())
                            .totalPrice(details.getTotalPrice())
                            .medicine(medicine.get())
                            .build()
            );
        });
        return order;
    }
    @Override
    public Order update(Integer userId, Integer id, DataHarianRequest orderRequest) {
        if (isOrderDoesNotExist(id)) {
            // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
            // return null;
            throw new DataHarianDoesNotExistException(id);
        }
        var order = Order.builder()
                .id(id)
                .orderDate(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        dataHarianRepository.save(order);

        var listOfOrderDetailsInDB = dataHarianDetailsRepository.findAllByOrderId(id);
        orderRequest.getOrderDetailsData().forEach(details -> {
            var medicine = makananRepository.findById(details.getMedicineId());
            if (medicine.isEmpty()) {
                // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
                // return;
                throw new MakananDoesNotExistException(details.getMedicineId());
            }

            // Update Order includes the updates of OrderDetails.
            // There are 3 scenarios of OrderDetails update:
            // 1. OrderDetails exists both in Database and Put Request
            // 2. OrderDetails exists only in Put Request
            // 3. OrderDetails exists only in Database

            var orderDetails = dataHarianDetailsRepository.findByOrderIdAndMedicineId(order.getId(), medicine.get().getId());
            if (orderDetails.isEmpty()) {
                dataHarianDetailsRepository.save(
                        OrderDetails.builder()
                                .order(order)
                                .quantity(details.getQuantity())
                                .totalPrice(details.getTotalPrice())
                                .medicine(medicine.get())
                                .build()
                );
            } else {
                listOfOrderDetailsInDB.remove(orderDetails.get());
                dataHarianDetailsRepository.save(
                        OrderDetails.builder()
                                .id(orderDetails.get().getId())
                                .order(order)
                                .quantity(details.getQuantity())
                                .totalPrice(details.getTotalPrice())
                                .medicine(medicine.get())
                                .build()
                );
            }
        });
        dataHarianDetailsRepository.deleteAll(listOfOrderDetailsInDB);
        return order;
    }

    @Override
    public void delete(Integer id) {
        // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
        if (isOrderDoesNotExist(id))
            throw new DataHarianDoesNotExistException(id);
        dataHarianRepository.deleteById(id);
    }

    private boolean isOrderDoesNotExist(Integer id) {
        return dataHarianRepository.findById(id).isEmpty();
    }
}
