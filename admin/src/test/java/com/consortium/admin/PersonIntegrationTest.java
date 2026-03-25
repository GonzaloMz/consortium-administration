package com.consortium.admin;

import com.consortium.admin.dto.PersonRequest;
import com.consortium.admin.dto.PersonResponse;
import com.consortium.admin.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    @Test
    void createAndRetrievePerson() throws Exception {
        PersonRequest request = new PersonRequest("Alice", "alice@example.com", "avatars/alice.png");

        MvcResult result = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.avatar").value("avatars/alice.png"))
                .andReturn();

        PersonResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), PersonResponse.class);

        mockMvc.perform(get("/api/persons/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void listPersons() throws Exception {
        PersonRequest r1 = new PersonRequest("Bob", "bob@example.com", null);
        PersonRequest r2 = new PersonRequest("Carol", "carol@example.com", "avatars/carol.jpg");

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updatePerson() throws Exception {
        PersonRequest request = new PersonRequest("Dave", "dave@example.com", null);

        MvcResult result = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        PersonResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), PersonResponse.class);

        PersonRequest update = new PersonRequest("David", "david@example.com", "avatars/david.png");

        mockMvc.perform(put("/api/persons/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David"))
                .andExpect(jsonPath("$.email").value("david@example.com"))
                .andExpect(jsonPath("$.avatar").value("avatars/david.png"));
    }

    @Test
    void deletePerson() throws Exception {
        PersonRequest request = new PersonRequest("Eve", "eve@example.com", null);

        MvcResult result = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        PersonResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), PersonResponse.class);

        mockMvc.perform(delete("/api/persons/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/persons/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void duplicateEmailReturnsConflict() throws Exception {
        PersonRequest request = new PersonRequest("Frank", "frank@example.com", null);

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void invalidEmailReturnsBadRequest() throws Exception {
        PersonRequest request = new PersonRequest("Grace", "not-an-email", null);

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void notFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/persons/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void avatarCanBeNull() throws Exception {
        PersonRequest request = new PersonRequest("Hank", "hank@example.com", null);

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.avatar").doesNotExist());
    }
}
