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
    private final MakananService makananService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('makanan:read')")
    public ResponseEntity<List<Makanan>> getAllMakanan() {
        List<Makanan> response = null;
        response = makananService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAuthority('makanan:read')")
    public ResponseEntity<Makanan> getMakananById(@PathVariable Integer id) {
        Makanan response = null;
        response = makananService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('makanan:create')")
    public ResponseEntity<Makanan> addMakanan(@RequestBody MakananRequest request) {
        Makanan response = null;
        response = makananService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('makanan:update')")
    public ResponseEntity<Makanan> putMakanan(@PathVariable Integer id, @RequestBody MakananRequest request) {
        Makanan response = null;
        response = makananService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('makanan:delete')")
    public ResponseEntity<String> deleteMakanan(@PathVariable Integer id) {
        makananService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Medicine with id %d", id));
    }
}
