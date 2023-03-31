package id.ac.ui.cs.advprog.auth.controller;

import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianAdminResponse;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianUserResponse;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.model.order.Order;
import id.ac.ui.cs.advprog.auth.service.dataharian.DataHarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dataharian")
@RequiredArgsConstructor
public class DataHarianController {
    private final DataHarianService orderService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('dataharian:read_all')")
    public ResponseEntity<List<DataHarianAdminResponse>> getAllOrder() {
        List<DataHarianAdminResponse> response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('dataharian:read_self')")
    public ResponseEntity<List<DataHarianUserResponse>> getAllUserOrder() {
        List<DataHarianUserResponse> response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.findAllByUserId(getCurrentUser().getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('dataharian:create')")
    public ResponseEntity<Order> createOrder(@RequestBody DataHarianRequest orderRequest) {
        Order response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.create(getCurrentUser().getId(), orderRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('dataharian:update')")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody DataHarianRequest orderRequest) {
        Order response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.update(getCurrentUser().getId(), id, orderRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('dataharian:delete')")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        // TODO: Lengkapi kode berikut
        orderService.delete(id);
        return ResponseEntity.ok(String.format("Deleted Order with id %d", id));
    }

    private static User getCurrentUser() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());
    }
}
