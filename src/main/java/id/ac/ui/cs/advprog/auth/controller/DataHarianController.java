package id.ac.ui.cs.advprog.auth.controller;

import id.ac.ui.cs.advprog.auth.dto.dataharian.*;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import id.ac.ui.cs.advprog.auth.service.dataharian.DataHarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dummy-for-auth/dataharian")
@RequiredArgsConstructor
public class DataHarianController {
    private final DataHarianService dataHarianService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('dataharian:read_all')")
    public ResponseEntity<List<DataHarianAdminResponse>> getAllDataHarian() {
        List<DataHarianAdminResponse> response = null;
        response = dataHarianService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('dataharian:read_self')")
    public ResponseEntity<DataHarianSummaryResponse> getAllUserDataHarian() {
        List<DataHarianUserResponse> response = null;
        response = dataHarianService.findAllByUserId(AuthenticationController.getCurrentUser().getId());
        return ResponseEntity.ok(DataHarianSummaryResponse.createSummaryResponse(AuthenticationController.getCurrentUser(),response));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('dataharian:create')")
    public ResponseEntity<DataHarian> createDataHarian(@RequestBody DataHarianRequest dataHarianRequest) {
        DataHarian response = null;
        response = dataHarianService.create(AuthenticationController.getCurrentUser().getId(), dataHarianRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('dataharian:update')")
    public ResponseEntity<DataHarian> updateDataHarian(@PathVariable Integer id, @RequestBody DataHarianRequest dataHarianRequest) {
        DataHarian response = null;
        response = dataHarianService.update(AuthenticationController.getCurrentUser().getId(), id, dataHarianRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('dataharian:delete')")
    public ResponseEntity<String> deleteDataHarian(@PathVariable Integer id) {
        dataHarianService.delete(id);
        return ResponseEntity.ok(String.format("Deleted DataHarian with id %d", id));
    }

}
