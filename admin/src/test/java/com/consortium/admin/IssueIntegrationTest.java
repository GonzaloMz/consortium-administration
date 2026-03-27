package com.consortium.admin;

import com.consortium.admin.dto.BuildingRequest;
import com.consortium.admin.dto.BuildingResponse;
import com.consortium.admin.dto.BuildingUnitRequest;
import com.consortium.admin.dto.BuildingUnitResponse;
import com.consortium.admin.dto.IssueAttachmentRequest;
import com.consortium.admin.dto.IssueRequest;
import com.consortium.admin.dto.IssueResponse;
import com.consortium.admin.dto.IssueSpentRequest;
import com.consortium.admin.dto.IssueUnitRequest;
import com.consortium.admin.dto.PersonRequest;
import com.consortium.admin.dto.PersonResponse;
import com.consortium.admin.repository.BuildingRepository;
import com.consortium.admin.repository.BuildingUnitRepository;
import com.consortium.admin.repository.IssueAttachmentRepository;
import com.consortium.admin.repository.IssueRepository;
import com.consortium.admin.repository.IssueSpentRepository;
import com.consortium.admin.repository.IssueUnitRepository;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IssueIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueUnitRepository issueUnitRepository;

    @Autowired
    private IssueAttachmentRepository issueAttachmentRepository;

    @Autowired
    private IssueSpentRepository issueSpentRepository;

    @Autowired
    private BuildingUnitRepository buildingUnitRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private PersonRepository personRepository;

    private Long buildingId;
    private Long unitId;

    @BeforeEach
    void setUp() throws Exception {
        issueSpentRepository.deleteAll();
        issueAttachmentRepository.deleteAll();
        issueUnitRepository.deleteAll();
        issueRepository.deleteAll();
        buildingUnitRepository.deleteAll();
        buildingRepository.deleteAll();
        personRepository.deleteAll();

        // Create a building
        BuildingRequest buildingReq = new BuildingRequest("Issue Test St", null, null, "Issue Building");
        MvcResult buildingResult = mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildingReq)))
                .andExpect(status().isCreated())
                .andReturn();
        BuildingResponse buildingResp = objectMapper.readValue(
                buildingResult.getResponse().getContentAsString(), BuildingResponse.class);
        buildingId = buildingResp.getId();

        // Create a person (owner)
        PersonRequest personReq = new PersonRequest("Issue Owner", "issueowner@example.com", null);
        MvcResult personResult = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personReq)))
                .andExpect(status().isCreated())
                .andReturn();
        PersonResponse personResp = objectMapper.readValue(
                personResult.getResponse().getContentAsString(), PersonResponse.class);
        Long ownerId = personResp.getId();

        // Create a building unit
        BuildingUnitRequest unitReq = new BuildingUnitRequest(buildingId, ownerId, 0.5);
        MvcResult unitResult = mockMvc.perform(post("/api/building-units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unitReq)))
                .andExpect(status().isCreated())
                .andReturn();
        BuildingUnitResponse unitResp = objectMapper.readValue(
                unitResult.getResponse().getContentAsString(), BuildingUnitResponse.class);
        unitId = unitResp.getId();
    }

    @Test
    void createAndRetrieveIssue() throws Exception {
        IssueRequest request = new IssueRequest(buildingId, "Leaking pipe", false);

        MvcResult result = mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.buildingId").value(buildingId))
                .andExpect(jsonPath("$.detail").value("Leaking pipe"))
                .andExpect(jsonPath("$.commonExpense").value(false))
                .andExpect(jsonPath("$.creationDate").isNotEmpty())
                .andReturn();

        IssueResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), IssueResponse.class);

        mockMvc.perform(get("/api/issues/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.detail").value("Leaking pipe"));
    }

    @Test
    void updateIssue() throws Exception {
        IssueRequest request = new IssueRequest(buildingId, "Initial detail", false);

        MvcResult result = mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        IssueResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), IssueResponse.class);

        IssueRequest update = new IssueRequest(buildingId, "Updated detail", true);

        mockMvc.perform(put("/api/issues/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.detail").value("Updated detail"))
                .andExpect(jsonPath("$.commonExpense").value(true));
    }

    @Test
    void attachFileToIssue() throws Exception {
        IssueRequest request = new IssueRequest(buildingId, "Attachment test", false);

        MvcResult result = mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        IssueResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), IssueResponse.class);

        IssueAttachmentRequest attachRequest = new IssueAttachmentRequest("files/photo.jpg");

        mockMvc.perform(post("/api/issues/" + created.getId() + "/attachments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attachRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attachments.length()").value(1))
                .andExpect(jsonPath("$.attachments[0].attachment").value("files/photo.jpg"));
    }

    @Test
    void addObserverUnitToIssue() throws Exception {
        IssueRequest request = new IssueRequest(buildingId, "Unit observer test", false);

        MvcResult result = mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        IssueResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), IssueResponse.class);

        IssueUnitRequest unitRequest = new IssueUnitRequest(unitId);

        mockMvc.perform(post("/api/issues/" + created.getId() + "/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unitRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.units.length()").value(1))
                .andExpect(jsonPath("$.units[0].unitId").value(unitId));
    }

    @Test
    void addSpentToIssue() throws Exception {
        IssueRequest request = new IssueRequest(buildingId, "Spent test", true);

        MvcResult result = mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        IssueResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), IssueResponse.class);

        IssueSpentRequest spentRequest = new IssueSpentRequest(new BigDecimal("250.00"), "Plumber Co");

        mockMvc.perform(post("/api/issues/" + created.getId() + "/spents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(spentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spents.length()").value(1))
                .andExpect(jsonPath("$.spents[0].provider").value("Plumber Co"));
    }

    @Test
    void notFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/issues/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void missingBuildingIdReturnsBadRequest() throws Exception {
        IssueRequest request = new IssueRequest(null, "detail", false);

        mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
