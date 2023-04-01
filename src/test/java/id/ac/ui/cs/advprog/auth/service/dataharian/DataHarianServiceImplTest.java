package id.ac.ui.cs.advprog.auth.service.dataharian;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianDetailsData;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.exceptions.DataHarianDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarianDetails;
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
class DataHarianServiceImplTest {

    @InjectMocks
    private DataHarianServiceImpl service;

    @Mock
    private DataHarianDetailsRepository dataHarianDetailsRepository;

    @Mock
    private DataHarianRepository dataHarianRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MakananRepository makananRepository;

    User user;
    DataHarian dataHarian;

    DataHarianDetails dataHarianDetails;
    DataHarianRequest dataHarianRequest;

    @BeforeEach
    void setUp() {
        user = new User();

        DataHarianDetailsData dataHarianDetailsData = new DataHarianDetailsData();
        dataHarianDetailsData.setMakananId(0);
        dataHarianDetailsData.setQuantity(0);
        dataHarianDetailsData.setMakananId(0);
        dataHarianRequest = new DataHarianRequest(List.of(dataHarianDetailsData));

        dataHarian = new DataHarian();
        dataHarianDetails = new DataHarianDetails();
    }

    @Test
    void whenCreateDataHarianButMakananNotFoundShouldThrowException() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        when(makananRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(MakananDoesNotExistException.class, () -> {
            service.create(0, dataHarianRequest);
        });
    }

    @Test
    void whenUpdateDataHarianButNotFoundShouldThrowException() {
        when(dataHarianRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(DataHarianDoesNotExistException.class, () -> {
            service.update(0, 0, dataHarianRequest);
        });
    }

    @Test
    void whenUpdateDataHarianButMakananNotFoundShouldThrowException() {
        when(dataHarianRepository.findById(any(Integer.class))).thenReturn(Optional.of(dataHarian));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        when(dataHarianDetailsRepository.findAllByDataHarianId(any(Integer.class))).thenReturn(List.of(dataHarianDetails));
        when(makananRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(MakananDoesNotExistException.class, () -> {
            service.update(0, 0, dataHarianRequest);
        });
    }

    @Test
    void whenDeleteDataHarianAndFoundShouldCallDeleteByIdOnRepo() {
        when(dataHarianRepository.findById(any(Integer.class))).thenReturn(Optional.of(dataHarian));
        service.delete(0);
        verify(dataHarianRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteDataHarianButNotFoundShouldThrowException() {
        when(dataHarianRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(DataHarianDoesNotExistException.class, () -> {
            service.delete(0);
        });
    }

}