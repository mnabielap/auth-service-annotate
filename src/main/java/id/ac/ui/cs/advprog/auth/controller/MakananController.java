package id.ac.ui.cs.advprog.auth.controller;

import id.ac.ui.cs.advprog.auth.dto.MakananRequest;
import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import id.ac.ui.cs.advprog.auth.service.makanan.MakananService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/makanan")
@RequiredArgsConstructor
public class MakananController {
    private final MakananService medicineService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('makanan:read')")
    public ResponseEntity<List<Makanan>> getAllMedicine() {
        List<Makanan> response = null;
        response = medicineService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAuthority('makanan:read')")
    public ResponseEntity<Makanan> getMedicineById(@PathVariable Integer id) {
        Makanan response = null;
        response = medicineService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('makanan:create')")
    public ResponseEntity<Makanan> addMedicine(@RequestBody MakananRequest request) {
        Makanan response = null;
        response = medicineService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('makanan:update')")
    public ResponseEntity<Makanan> putMedicine(@PathVariable Integer id, @RequestBody MakananRequest request) {
        Makanan response = null;
        response = medicineService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('makanan:delete')")
    public ResponseEntity<String> deleteMedicine(@PathVariable Integer id) {
        medicineService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Medicine with id %d", id));
    }
}
