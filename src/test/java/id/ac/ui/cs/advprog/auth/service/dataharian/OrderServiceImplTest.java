package id.ac.ui.cs.advprog.auth.service.dataharian;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianDetailsData;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.exceptions.DataHarianDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.model.order.Order;
import id.ac.ui.cs.advprog.auth.model.order.OrderDetails;
import id.ac.ui.cs.advprog.auth.repository.MakananRepository;
import id.ac.ui.cs.advprog.auth.repository.DataHarianDetailsRepository;
import id.ac.ui.cs.advprog.auth.repository.DataHarianRepository;
import id.ac.ui.cs.advprog.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private DataHarianServiceImpl service;

    @Mock
    private DataHarianDetailsRepository orderDetailsRepository;

    @Mock
    private DataHarianRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MakananRepository medicineRepository;

    User user;
    Order order;

    OrderDetails orderDetails;
    DataHarianRequest orderRequest;

    @BeforeEach
    void setUp() {
        user = new User();

        DataHarianDetailsData orderDetailsData = new DataHarianDetailsData();
        orderDetailsData.setMedicineId(0);
        orderDetailsData.setQuantity(0);
        orderDetailsData.setTotalPrice(0);
        orderRequest = new DataHarianRequest(List.of(orderDetailsData));

        order = new Order();
        orderDetails = new OrderDetails();
    }

    @Test
    void whenCreateOrderButMedicineNotFoundShouldThrowException() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        when(medicineRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(MakananDoesNotExistException.class, () -> {
            service.create(0, orderRequest);
        });
    }

    @Test
    void whenUpdateOrderButNotFoundShouldThrowException() {
        when(orderRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(DataHarianDoesNotExistException.class, () -> {
            service.update(0, 0, orderRequest);
        });
    }

    @Test
    void whenUpdateOrderButMedicineNotFoundShouldThrowException() {
        when(orderRepository.findById(any(Integer.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        when(orderDetailsRepository.findAllByOrderId(any(Integer.class))).thenReturn(List.of(orderDetails));
        when(medicineRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(MakananDoesNotExistException.class, () -> {
            service.update(0, 0, orderRequest);
        });
    }

    @Test
    void whenDeleteOrderAndFoundShouldCallDeleteByIdOnRepo() {
        when(orderRepository.findById(any(Integer.class))).thenReturn(Optional.of(order));
        service.delete(0);
        verify(orderRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteOrderButNotFoundShouldThrowException() {
        when(orderRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(DataHarianDoesNotExistException.class, () -> {
            service.delete(0);
        });
    }

}