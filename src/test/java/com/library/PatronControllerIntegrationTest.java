package com.library;

import com.library.model.Patron;
import com.library.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PatronControllerIntegrationTest extends BaseTest {

    @Autowired
    private PatronRepository patronRepository;

    @Test
    @Rollback
    void createPatron_shouldReturn201() throws Exception {
        Patron newPatron = new Patron("John Doe", "john@example.com", "1234567890");

        mockMvc.perform(post("/api/patrons").with(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPatron)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    @Rollback
    void getAllPatrons_shouldReturnList() throws Exception {
        patronRepository.save(new Patron("Patron 1", "patron1@example.com", "1111111111"));
        patronRepository.save(new Patron("Patron 2", "patron2@example.com", "2222222222"));

        mockMvc.perform(get("/api/patrons").with(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].name").value("Patron 1"))
                .andExpect(jsonPath("$.data.content[1].name").value("Patron 2"));
    }

    @Test
    @Rollback
    void getPatronById_shouldReturnPatron() throws Exception {
        Patron savedPatron = patronRepository.save(
            new Patron("Specific Patron", "specific@example.com", "3333333333"));

        mockMvc.perform(get("/api/patrons/" + savedPatron.getId()).with(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Specific Patron"));
    }

    @Test
    @Rollback
    void updatePatron_shouldUpdateSuccessfully() throws Exception {
        Patron savedPatron = patronRepository.save(
            new Patron("Original", "original@example.com", "4444444444"));

        Patron updateData = new Patron("Updated", "updated@example.com", "4444444444");

        mockMvc.perform(put("/api/patrons/" + savedPatron.getId()).with(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Updated"))
                .andExpect(jsonPath("$.data.email").value("updated@example.com"));
    }

    @Test
    @Rollback
    void deletePatron_shouldReturn200() throws Exception {
        Patron savedPatron = patronRepository.save(
            new Patron("To Delete", "delete@example.com", "5555555555"));

        mockMvc.perform(delete("/api/patrons/" + savedPatron.getId()).with(auth))
                .andExpect(status().isOk());
    }

    @Test
    void getPatron_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/patrons/999").with(auth))
                .andExpect(status().isNotFound());
    }
}