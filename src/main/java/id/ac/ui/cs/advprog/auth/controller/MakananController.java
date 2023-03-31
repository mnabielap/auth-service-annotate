package id.ac.ui.cs.advprog.auth.controller;

import id.ac.ui.cs.advprog.auth.dto.MakananRequest;
import id.ac.ui.cs.advprog.auth.model.medicine.Medicine;
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
    public ResponseEntity<List<Medicine>> getAllMedicine() {
        List<Medicine> response = null;
        response = medicineService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAuthority('makanan:read')")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Integer id) {
        Medicine response = null;
        response = medicineService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('makanan:create')")
    public ResponseEntity<Medicine> addMedicine(@RequestBody MakananRequest request) {
        Medicine response = null;
        response = medicineService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('makanan:update')")
    public ResponseEntity<Medicine> putMedicine(@PathVariable Integer id, @RequestBody MakananRequest request) {
        Medicine response = null;
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
