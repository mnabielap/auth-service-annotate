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
class MakananServiceImplTest {

    @InjectMocks
    private MakananServiceImpl service;

    @Mock
    private MakananRepository repository;

    Makanan makanan;
    Makanan newMakanan;
    MakananRequest createRequest;
    MakananRequest updateRequest;

    @BeforeEach
    void setUp() {
        createRequest = MakananRequest.builder()
                .name("Hayase Yuuka")
                .keterangan("Hayase Yuuka")
                .category("BAHAN_MAKANAN")
                .kalori(100)
                .manufacturer("Hayase Yuuka")
                .build();

        updateRequest = MakananRequest.builder()
                .name("Ushio Noa")
                .keterangan("Ushio Noa")
                .category("BAHAN_MAKANAN")
                .kalori(100)
                .manufacturer("Ushio Noa")
                .build();


        makanan = Makanan.builder()
                .id(0)
                .name("Hayase Yuuka")
                .keterangan("Hayase Yuuka")
                .category(MakananCategory.BAHAN_MAKANAN)
                .kalori(100)
                .manufacturer("Hayase Yuuka")
                .build();

        newMakanan = Makanan.builder()
                .id(0)
                .name("Ushio Noa")
                .keterangan("Ushio Noa")
                .category(MakananCategory.BAHAN_MAKANAN)
                .kalori(100)
                .manufacturer("Ushio Noa")
                .build();
    }

    @Test
    void whenFindAllMakananShouldReturnListOfMakanans() {
        List<Makanan> allMakanans = List.of(makanan);

        when(repository.findAll()).thenReturn(allMakanans);

        List<Makanan> result = service.findAll();
        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(allMakanans, result);
    }

    @Test
    void whenFindByIdAndFoundShouldReturnMakanan() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(makanan));

        Makanan result = service.findById(0);
        verify(repository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(makanan, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(MakananDoesNotExistException.class, () -> {
            service.findById(0);
        });
    }

    @Test
    void whenCreateMakananShouldReturnTheCreatedMakanan() {
        when(repository.save(any(Makanan.class))).thenAnswer(invocation -> {
            var makanan = invocation.getArgument(0, Makanan.class);
            makanan.setId(0);
            return makanan;
        });

        Makanan result = service.create(createRequest);
        verify(repository, atLeastOnce()).save(any(Makanan.class));
        Assertions.assertEquals(makanan, result);
    }

    @Test
    void whenUpdateMakananAndFoundShouldReturnTheUpdatedMakanan() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(makanan));
        when(repository.save(any(Makanan.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Makanan.class));

        Makanan result = service.update(0, updateRequest);
        verify(repository, atLeastOnce()).save(any(Makanan.class));
        Assertions.assertEquals(newMakanan, result);
    }

    @Test
    void whenUpdateMakananAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(MakananDoesNotExistException.class, () -> {
            service.update(0, createRequest);
        });
    }

    @Test
    void whenDeleteMakananAndFoundShouldCallDeleteByIdOnRepo() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(makanan));

        service.delete(0);
        verify(repository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteMakananAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(MakananDoesNotExistException.class, () -> {
            service.delete(0);
        });
    }

}