package id.ac.ui.cs.advprog.tutorial5.service.order;

import id.ac.ui.cs.advprog.tutorial5.dto.order.OrderRequest;
import id.ac.ui.cs.advprog.tutorial5.dto.order.OrderAdminResponse;
import id.ac.ui.cs.advprog.tutorial5.dto.order.OrderUserResponse;
import id.ac.ui.cs.advprog.tutorial5.exceptions.MedicineDoesNotExistException;
import id.ac.ui.cs.advprog.tutorial5.exceptions.OrderDoesNotExistException;
import id.ac.ui.cs.advprog.tutorial5.model.order.Order;
import id.ac.ui.cs.advprog.tutorial5.model.order.OrderDetails;
import id.ac.ui.cs.advprog.tutorial5.repository.MedicineRepository;
import id.ac.ui.cs.advprog.tutorial5.repository.OrderDetailsRepository;
import id.ac.ui.cs.advprog.tutorial5.repository.OrderRepository;
import id.ac.ui.cs.advprog.tutorial5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderAdminResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(order -> OrderAdminResponse.fromOrder(order, orderDetailsRepository.findAllByOrderId(order.getId())))
                .toList();
    }

    @Override
    public List<OrderUserResponse> findAllByUserId(Integer userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(order -> OrderUserResponse.fromOrder(order, orderDetailsRepository.findAllByOrderId(order.getId())))
                .toList();
    }

    @Override
    public Order create(Integer userId, OrderRequest orderRequest) {
        var order = Order.builder()
                .orderDate(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        orderRepository.save(order);
        orderRequest.getOrderDetailsData().forEach(details -> {
            var medicine = medicineRepository.findById(details.getMedicineId());
            if (medicine.isEmpty()) {
                // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
                // return;
                throw new MedicineDoesNotExistException(details.getMedicineId());
            }
            orderDetailsRepository.save(
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
    public Order update(Integer userId, Integer id, OrderRequest orderRequest) {
        if (isOrderDoesNotExist(id)) {
            // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
            // return null;
            throw new OrderDoesNotExistException(id);
        }
        var order = Order.builder()
                .id(id)
                .orderDate(new Date())
                .user(userRepository.findById(userId).orElse(null))
                .build();
        orderRepository.save(order);

        var listOfOrderDetailsInDB = orderDetailsRepository.findAllByOrderId(id);
        orderRequest.getOrderDetailsData().forEach(details -> {
            var medicine = medicineRepository.findById(details.getMedicineId());
            if (medicine.isEmpty()) {
                // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
                // return;
                throw new MedicineDoesNotExistException(details.getMedicineId());
            }

            // Update Order includes the updates of OrderDetails.
            // There are 3 scenarios of OrderDetails update:
            // 1. OrderDetails exists both in Database and Put Request
            // 2. OrderDetails exists only in Put Request
            // 3. OrderDetails exists only in Database

            var orderDetails = orderDetailsRepository.findByOrderIdAndMedicineId(order.getId(), medicine.get().getId());
            if (orderDetails.isEmpty()) {
                orderDetailsRepository.save(
                        OrderDetails.builder()
                                .order(order)
                                .quantity(details.getQuantity())
                                .totalPrice(details.getTotalPrice())
                                .medicine(medicine.get())
                                .build()
                );
            } else {
                listOfOrderDetailsInDB.remove(orderDetails.get());
                orderDetailsRepository.save(
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
        orderDetailsRepository.deleteAll(listOfOrderDetailsInDB);
        return order;
    }

    @Override
    public void delete(Integer id) {
        // TODO: Lengkapi kode berikut (Pastikan Anda memanfaatkan Exceptions yang ada!)
        if (isOrderDoesNotExist(id))
            throw new OrderDoesNotExistException(id);
        orderRepository.deleteById(id);
    }

    private boolean isOrderDoesNotExist(Integer id) {
        return orderRepository.findById(id).isEmpty();
    }
}
