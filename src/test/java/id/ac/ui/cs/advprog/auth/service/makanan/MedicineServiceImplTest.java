package id.ac.ui.cs.advprog.auth.service.makanan;

import id.ac.ui.cs.advprog.auth.dto.MakananRequest;
import id.ac.ui.cs.advprog.auth.exceptions.MakananDoesNotExistException;
import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import id.ac.ui.cs.advprog.auth.model.makanan.MakananCategory;
import id.ac.ui.cs.advprog.auth.repository.MakananRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceImplTest {

    @InjectMocks
    private MakananServiceImpl service;

    @Mock
    private MakananRepository repository;

    Makanan medicine;
    Makanan newMedicine;
    MakananRequest createRequest;
    MakananRequest updateRequest;

    @BeforeEach
    void setUp() {
        createRequest = MakananRequest.builder()
                .name("Hayase Yuuka")
                .keterangan("Hayase Yuuka")
                .category("BAHAN_MAKANAN")
                .stock(100)
                .kalori(100)
                .manufacturer("Hayase Yuuka")
                .build();

        updateRequest = MakananRequest.builder()
                .name("Ushio Noa")
                .keterangan("Ushio Noa")
                .category("BAHAN_MAKANAN")
                .stock(100)
                .kalori(100)
                .manufacturer("Ushio Noa")
                .build();


        medicine = Makanan.builder()
                .id(0)
                .name("Hayase Yuuka")
                .keterangan("Hayase Yuuka")
                .category(MakananCategory.BAHAN_MAKANAN)
                .stock(100)
                .kalori(100)
                .manufacturer("Hayase Yuuka")
                .build();

        newMedicine = Makanan.builder()
                .id(0)
                .name("Ushio Noa")
                .keterangan("Ushio Noa")
                .category(MakananCategory.BAHAN_MAKANAN)
                .stock(100)
                .kalori(100)
                .manufacturer("Ushio Noa")
                .build();
    }

    @Test
    void whenFindAllMedicineShouldReturnListOfMedicines() {
        List<Makanan> allMedicines = List.of(medicine);

        when(repository.findAll()).thenReturn(allMedicines);

        List<Makanan> result = service.findAll();
        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(allMedicines, result);
    }

    @Test
    void whenFindByIdAndFoundShouldReturnMedicine() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(medicine));

        Makanan result = service.findById(0);
        verify(repository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(medicine, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(MakananDoesNotExistException.class, () -> {
            service.findById(0);
        });
    }

    @Test
    void whenCreateMedicineShouldReturnTheCreatedMedicine() {
        when(repository.save(any(Makanan.class))).thenAnswer(invocation -> {
            var medicine = invocation.getArgument(0, Makanan.class);
            medicine.setId(0);
            return medicine;
        });

        Makanan result = service.create(createRequest);
        verify(repository, atLeastOnce()).save(any(Makanan.class));
        Assertions.assertEquals(medicine, result);
    }

    @Test
    void whenUpdateMedicineAndFoundShouldReturnTheUpdatedMedicine() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(medicine));
        when(repository.save(any(Makanan.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Makanan.class));

        Makanan result = service.update(0, updateRequest);
        verify(repository, atLeastOnce()).save(any(Makanan.class));
        Assertions.assertEquals(newMedicine, result);
    }

    @Test
    void whenUpdateMedicineAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(MakananDoesNotExistException.class, () -> {
            service.update(0, createRequest);
        });
    }

    @Test
    void whenDeleteMedicineAndFoundShouldCallDeleteByIdOnRepo() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(medicine));

        service.delete(0);
        verify(repository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteMedicineAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(MakananDoesNotExistException.class, () -> {
            service.delete(0);
        });
    }

}