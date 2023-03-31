package id.ac.ui.cs.advprog.auth.controller;

import id.ac.ui.cs.advprog.auth.Util;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianAdminResponse;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianRequest;
import id.ac.ui.cs.advprog.auth.dto.dataharian.DataHarianUserResponse;
import id.ac.ui.cs.advprog.auth.model.auth.User;
import id.ac.ui.cs.advprog.auth.model.dataharian.DataHarian;
import id.ac.ui.cs.advprog.auth.service.JwtService;
import id.ac.ui.cs.advprog.auth.service.dataharian.DataHarianServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DataHarianController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DataHarianServiceImpl service;

    @MockBean
    private JwtService jwtService;

    @Mock
    User user;

    DataHarian order;
    Object bodyContent;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        order = DataHarian.builder()
                .id(1)
                .build();

        bodyContent = new Object() {
            public final Integer id = 1;
        };
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllOrder() throws Exception {
        DataHarianAdminResponse orderAdminResponse = new DataHarianAdminResponse();
        orderAdminResponse.setDataHarianId(1);

        when(service.findAll()).thenReturn(List.of(orderAdminResponse));

        mvc.perform(get("/api/v1/dataharian/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllDataHarian"))
                .andExpect(jsonPath("$[0].dataHarianId").value(String.valueOf(orderAdminResponse.getDataHarianId())));

        verify(service, atLeastOnce()).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUserOrder() throws Exception {
        DataHarianUserResponse orderUserResponse = new DataHarianUserResponse();
        orderUserResponse.setDataHarianId(1);

        when(service.findAllByUserId(any(Integer.class))).thenReturn(List.of(orderUserResponse));

        mvc.perform(get("/api/v1/dataharian/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllUserDataHarian"))
                .andExpect(jsonPath("$[0].dataHarianId").value(String.valueOf(orderUserResponse.getDataHarianId())));

        verify(service, atLeastOnce()).findAllByUserId(any(Integer.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateOrder() throws Exception {
        when(service.create(any(Integer.class), any(DataHarianRequest.class))).thenReturn(order);

        mvc.perform(post("/api/v1/dataharian/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createDataHarian"))
                .andExpect(jsonPath("$.id").value(String.valueOf(order.getId())));

        verify(service, atLeastOnce()).create(any(Integer.class), any(DataHarianRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateOrder() throws Exception {
        when(service.update(any(Integer.class), any(Integer.class), any(DataHarianRequest.class))).thenReturn(order);

        mvc.perform(put("/api/v1/dataharian/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateDataHarian"))
                .andExpect(jsonPath("$.id").value(String.valueOf(order.getId())));

        verify(service, atLeastOnce()).update(any(Integer.class), any(Integer.class), any(DataHarianRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteOrder() throws Exception {
        mvc.perform(delete("/api/v1/dataharian/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteDataHarian"));

        verify(service, atLeastOnce()).delete(any(Integer.class));
    }

}