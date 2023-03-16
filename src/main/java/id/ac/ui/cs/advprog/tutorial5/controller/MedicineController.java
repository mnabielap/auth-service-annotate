package id.ac.ui.cs.advprog.tutorial5.controller;

import id.ac.ui.cs.advprog.tutorial5.dto.MedicineRequest;
import id.ac.ui.cs.advprog.tutorial5.model.medicine.Medicine;
import id.ac.ui.cs.advprog.tutorial5.service.medicine.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('medicine:read')")
    public ResponseEntity<List<Medicine>> getAllMedicine() {
        List<Medicine> response = null;
        // TODO: Lengkapi kode berikut
        response = medicineService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAuthority('medicine:read')")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Integer id) {
        Medicine response = null;
        // TODO: Lengkapi kode berikut
        response = medicineService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('medicine:create')")
    public ResponseEntity<Medicine> addMedicine(@RequestBody MedicineRequest request) {
        Medicine response = null;
        // TODO: Lengkapi kode berikut
        response = medicineService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('medicine:update')")
    public ResponseEntity<Medicine> putMedicine(@PathVariable Integer id, @RequestBody MedicineRequest request) {
        Medicine response = null;
        // TODO: Lengkapi kode berikut
        response = medicineService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('medicine:delete')")
    public ResponseEntity<String> deleteMedicine(@PathVariable Integer id) {
        // TODO: Lengkapi kode berikut
        medicineService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Medicine with id %d", id));
    }
}
