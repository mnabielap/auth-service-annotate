package id.ac.ui.cs.advprog.tutorial5.controller;

import id.ac.ui.cs.advprog.tutorial5.dto.order.OrderRequest;
import id.ac.ui.cs.advprog.tutorial5.dto.order.OrderAdminResponse;
import id.ac.ui.cs.advprog.tutorial5.dto.order.OrderUserResponse;
import id.ac.ui.cs.advprog.tutorial5.model.auth.User;
import id.ac.ui.cs.advprog.tutorial5.model.order.Order;
import id.ac.ui.cs.advprog.tutorial5.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('order:read_all')")
    public ResponseEntity<List<OrderAdminResponse>> getAllOrder() {
        List<OrderAdminResponse> response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('order:read_self')")
    public ResponseEntity<List<OrderUserResponse>> getAllUserOrder() {
        List<OrderUserResponse> response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.findAllByUserId(getCurrentUser().getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('order:create')")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.create(getCurrentUser().getId(), orderRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('order:update')")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody OrderRequest orderRequest) {
        Order response = null;
        // TODO: Lengkapi kode berikut
        response = orderService.update(getCurrentUser().getId(), id, orderRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('order:delete')")
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
