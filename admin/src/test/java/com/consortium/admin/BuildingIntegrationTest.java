package com.consortium.admin;

import com.consortium.admin.dto.BuildingRequest;
import com.consortium.admin.dto.BuildingResponse;
import com.consortium.admin.repository.BuildingRepository;
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
class BuildingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BuildingRepository buildingRepository;

    @BeforeEach
    void setUp() {
        buildingRepository.deleteAll();
    }

    @Test
    void createAndRetrieveBuilding() throws Exception {
        BuildingRequest request = new BuildingRequest("123 Main St", -34.6037, -58.3816, "Tower A");

        MvcResult result = mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.direction").value("123 Main St"))
                .andExpect(jsonPath("$.name").value("Tower A"))
                .andExpect(jsonPath("$.latitude").value(-34.6037))
                .andExpect(jsonPath("$.longitude").value(-58.3816))
                .andExpect(jsonPath("$.creationDate").isNotEmpty())
                .andReturn();

        BuildingResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), BuildingResponse.class);

        mockMvc.perform(get("/api/buildings/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tower A"));
    }

    @Test
    void listBuildings() throws Exception {
        BuildingRequest r1 = new BuildingRequest("Addr 1", null, null, "Building 1");
        BuildingRequest r2 = new BuildingRequest("Addr 2", null, null, "Building 2");

        mockMvc.perform(post("/api/buildings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/buildings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateBuilding() throws Exception {
        BuildingRequest request = new BuildingRequest("Old Street", null, null, "Old Name");

        MvcResult result = mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        BuildingResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), BuildingResponse.class);

        BuildingRequest update = new BuildingRequest("New Street", 1.0, 2.0, "New Name");

        mockMvc.perform(put("/api/buildings/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direction").value("New Street"))
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void deleteBuilding() throws Exception {
        BuildingRequest request = new BuildingRequest("Delete St", null, null, "To Delete");

        MvcResult result = mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        BuildingResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), BuildingResponse.class);

        mockMvc.perform(delete("/api/buildings/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/buildings/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletedBuildingNotListedInFindAll() throws Exception {
        BuildingRequest request = new BuildingRequest("Hidden St", null, null, "Hidden");

        MvcResult result = mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        BuildingResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), BuildingResponse.class);

        mockMvc.perform(delete("/api/buildings/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void notFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/buildings/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void missingRequiredFieldReturnsBadRequest() throws Exception {
        BuildingRequest request = new BuildingRequest("", null, null, "Name");

        mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
