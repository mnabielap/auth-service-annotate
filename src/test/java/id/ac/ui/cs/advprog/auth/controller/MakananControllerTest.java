package id.ac.ui.cs.advprog.auth.controller;

import id.ac.ui.cs.advprog.auth.Util;
import id.ac.ui.cs.advprog.auth.dto.MakananRequest;
import id.ac.ui.cs.advprog.auth.model.makanan.Makanan;
import id.ac.ui.cs.advprog.auth.model.makanan.MakananCategory;
import id.ac.ui.cs.advprog.auth.repository.TokenRepository;
import id.ac.ui.cs.advprog.auth.service.JwtService;
import id.ac.ui.cs.advprog.auth.service.makanan.MakananServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MakananController.class)
@AutoConfigureMockMvc
class MakananControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MakananServiceImpl service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    TokenRepository tokenRepository;

    Makanan makanan;
    Object bodyContent;

    @BeforeEach
    void setUp() {
        makanan = Makanan.builder()
                .name("Hayase Yuuka")
                .category(MakananCategory.BAHAN_MAKANAN)
                .keterangan("Hayase Yuuka")
                .kalori(100)
                .manufacturer("Hayase Yuuka")
                .build();

        bodyContent = new Object() {
            public final String name = "Hayase Yuuka";
            public final String category = "BAHAN_MAKANAN";
            public final String dose = "Hayase Yuuka";
            public final Integer stock = 100;
            public final Integer price = 100;
            public final String manufacturer = "Hayase Yuuka";
        };
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllMakanan() throws Exception {
        List<Makanan> allMakanans = List.of(makanan);

        when(service.findAll()).thenReturn(allMakanans);

        mvc.perform(get("/api/v1/dummy-for-auth/makanan/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllMakanan"))
                .andExpect(jsonPath("$[0].name").value(makanan.getName()));

        verify(service, atLeastOnce()).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetMakananById() throws Exception {
        when(service.findById(any(Integer.class))).thenReturn(makanan);

        mvc.perform(get("/api/v1/dummy-for-auth/makanan/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getMakananById"))
                .andExpect(jsonPath("$.name").value(makanan.getName()));

        verify(service, atLeastOnce()).findById(any(Integer.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddMakanan() throws Exception {
        when(service.create(any(MakananRequest.class))).thenReturn(makanan);

        mvc.perform(post("/api/v1/dummy-for-auth/makanan/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addMakanan"))
                .andExpect(jsonPath("$.name").value(makanan.getName()));

        verify(service, atLeastOnce()).create(any(MakananRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPutMakanan() throws Exception {
        when(service.update(any(Integer.class), any(MakananRequest.class))).thenReturn(makanan);

        mvc.perform(put("/api/v1/dummy-for-auth/makanan/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("putMakanan"))
                .andExpect(jsonPath("$.name").value(makanan.getName()));

        verify(service, atLeastOnce()).update(any(Integer.class), any(MakananRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMakanan() throws Exception {
        mvc.perform(delete("/api/v1/dummy-for-auth/makanan/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteMakanan"));

        verify(service, atLeastOnce()).delete(any(Integer.class));
    }
}
