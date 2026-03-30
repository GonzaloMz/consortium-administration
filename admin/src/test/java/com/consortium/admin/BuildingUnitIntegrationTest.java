package com.consortium.admin;

import com.consortium.admin.dto.BuildingRequest;
import com.consortium.admin.dto.BuildingResponse;
import com.consortium.admin.dto.BuildingUnitRequest;
import com.consortium.admin.dto.BuildingUnitResponse;
import com.consortium.admin.dto.PersonRequest;
import com.consortium.admin.dto.PersonResponse;
import com.consortium.admin.repository.BuildingRepository;
import com.consortium.admin.repository.BuildingUnitRepository;
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
class BuildingUnitIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BuildingUnitRepository buildingUnitRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private PersonRepository personRepository;

    private Long buildingId;
    private Long ownerId;

    @BeforeEach
    void setUp() throws Exception {
        buildingUnitRepository.deleteAll();
        buildingRepository.deleteAll();
        personRepository.deleteAll();

        // Create a building
        BuildingRequest buildingReq = new BuildingRequest("Unit Test St", null, null, "Unit Test Building");
        MvcResult buildingResult = mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildingReq)))
                .andExpect(status().isCreated())
                .andReturn();
        BuildingResponse buildingResp = objectMapper.readValue(
                buildingResult.getResponse().getContentAsString(), BuildingResponse.class);
        buildingId = buildingResp.getId();

        // Create a person (owner)
        PersonRequest personReq = new PersonRequest("Owner One", "owner1@example.com", null);
        MvcResult personResult = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personReq)))
                .andExpect(status().isCreated())
                .andReturn();
        PersonResponse personResp = objectMapper.readValue(
                personResult.getResponse().getContentAsString(), PersonResponse.class);
        ownerId = personResp.getId();
    }

    @Test
    void createAndRetrieveBuildingUnit() throws Exception {
        BuildingUnitRequest request = new BuildingUnitRequest(buildingId, ownerId, 0.5);

        MvcResult result = mockMvc.perform(post("/api/building-units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.buildingId").value(buildingId))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.ownerId").value(ownerId))
                .andExpect(jsonPath("$.coefficient").value(0.5))
                .andReturn();

        BuildingUnitResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), BuildingUnitResponse.class);

        mockMvc.perform(get("/api/building-units/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1));
    }

    @Test
    void unitNumberAutoIncrementsPerBuilding() throws Exception {
        BuildingUnitRequest req1 = new BuildingUnitRequest(buildingId, ownerId, 0.3);
        BuildingUnitRequest req2 = new BuildingUnitRequest(buildingId, ownerId, 0.7);

        mockMvc.perform(post("/api/building-units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(1));

        mockMvc.perform(post("/api/building-units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(2));
    }

    @Test
    void updateBuildingUnit() throws Exception {
        BuildingUnitRequest request = new BuildingUnitRequest(buildingId, ownerId, 0.5);

        MvcResult result = mockMvc.perform(post("/api/building-units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        BuildingUnitResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), BuildingUnitResponse.class);

        BuildingUnitRequest update = new BuildingUnitRequest(buildingId, ownerId, 0.8);

        mockMvc.perform(put("/api/building-units/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coefficient").value(0.8));
    }

    @Test
    void coefficientZeroReturnsBadRequest() throws Exception {
        BuildingUnitRequest request = new BuildingUnitRequest(buildingId, ownerId, 0.0);

        mockMvc.perform(post("/api/building-units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void coefficientOneReturnsBadRequest() throws Exception {
        BuildingUnitRequest request = new BuildingUnitRequest(buildingId, ownerId, 1.0);

        mockMvc.perform(post("/api/building-units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void notFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/building-units/99999"))
                .andExpect(status().isNotFound());
    }
}
