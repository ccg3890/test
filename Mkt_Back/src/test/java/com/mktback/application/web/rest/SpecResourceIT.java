package com.mktback.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mktback.application.IntegrationTest;
import com.mktback.application.domain.Spec;
import com.mktback.application.domain.enumeration.CategoryGrade;
import com.mktback.application.domain.enumeration.CategoryStatus;
import com.mktback.application.repository.SpecRepository;
import com.mktback.application.service.dto.SpecDTO;
import com.mktback.application.service.mapper.SpecMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpecResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final CategoryStatus DEFAULT_STATUS = CategoryStatus.Active;
    private static final CategoryStatus UPDATED_STATUS = CategoryStatus.Inactive;

    private static final String DEFAULT_PARENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_ID = "BBBBBBBBBB";

    private static final CategoryGrade DEFAULT_GRADE = CategoryGrade.A;
    private static final CategoryGrade UPDATED_GRADE = CategoryGrade.B;

    private static final LocalDate DEFAULT_CREATE_DT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFY_DT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFY_DT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/specs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SpecRepository specRepository;

    @Autowired
    private SpecMapper specMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecMockMvc;

    private Spec spec;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spec createEntity(EntityManager em) {
        Spec spec = new Spec()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .parentId(DEFAULT_PARENT_ID)
            .grade(DEFAULT_GRADE)
            .createDt(DEFAULT_CREATE_DT)
            .modifyDt(DEFAULT_MODIFY_DT);
        return spec;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spec createUpdatedEntity(EntityManager em) {
        Spec spec = new Spec()
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .parentId(UPDATED_PARENT_ID)
            .grade(UPDATED_GRADE)
            .createDt(UPDATED_CREATE_DT)
            .modifyDt(UPDATED_MODIFY_DT);
        return spec;
    }

    @BeforeEach
    public void initTest() {
        spec = createEntity(em);
    }

    @Test
    @Transactional
    void createSpec() throws Exception {
        int databaseSizeBeforeCreate = specRepository.findAll().size();
        // Create the Spec
        SpecDTO specDTO = specMapper.toDto(spec);
        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specDTO)))
            .andExpect(status().isCreated());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeCreate + 1);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpec.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSpec.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testSpec.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testSpec.getCreateDt()).isEqualTo(DEFAULT_CREATE_DT);
        assertThat(testSpec.getModifyDt()).isEqualTo(DEFAULT_MODIFY_DT);
    }

    @Test
    @Transactional
    void createSpecWithExistingId() throws Exception {
        // Create the Spec with an existing ID
        spec.setId("existing_id");
        SpecDTO specDTO = specMapper.toDto(spec);

        int databaseSizeBeforeCreate = specRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpecs() throws Exception {
        // Initialize the database
        spec.setId(UUID.randomUUID().toString());
        specRepository.saveAndFlush(spec);

        // Get all the specList
        restSpecMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spec.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].createDt").value(hasItem(DEFAULT_CREATE_DT.toString())))
            .andExpect(jsonPath("$.[*].modifyDt").value(hasItem(DEFAULT_MODIFY_DT.toString())));
    }

    @Test
    @Transactional
    void getSpec() throws Exception {
        // Initialize the database
        spec.setId(UUID.randomUUID().toString());
        specRepository.saveAndFlush(spec);

        // Get the spec
        restSpecMockMvc
            .perform(get(ENTITY_API_URL_ID, spec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spec.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()))
            .andExpect(jsonPath("$.createDt").value(DEFAULT_CREATE_DT.toString()))
            .andExpect(jsonPath("$.modifyDt").value(DEFAULT_MODIFY_DT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSpec() throws Exception {
        // Get the spec
        restSpecMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpec() throws Exception {
        // Initialize the database
        spec.setId(UUID.randomUUID().toString());
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeUpdate = specRepository.findAll().size();

        // Update the spec
        Spec updatedSpec = specRepository.findById(spec.getId()).get();
        // Disconnect from session so that the updates on updatedSpec are not directly saved in db
        em.detach(updatedSpec);
        updatedSpec
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .parentId(UPDATED_PARENT_ID)
            .grade(UPDATED_GRADE)
            .createDt(UPDATED_CREATE_DT)
            .modifyDt(UPDATED_MODIFY_DT);
        SpecDTO specDTO = specMapper.toDto(updatedSpec);

        restSpecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specDTO))
            )
            .andExpect(status().isOk());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpec.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSpec.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testSpec.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testSpec.getCreateDt()).isEqualTo(UPDATED_CREATE_DT);
        assertThat(testSpec.getModifyDt()).isEqualTo(UPDATED_MODIFY_DT);
    }

    @Test
    @Transactional
    void putNonExistingSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(UUID.randomUUID().toString());

        // Create the Spec
        SpecDTO specDTO = specMapper.toDto(spec);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(UUID.randomUUID().toString());

        // Create the Spec
        SpecDTO specDTO = specMapper.toDto(spec);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(UUID.randomUUID().toString());

        // Create the Spec
        SpecDTO specDTO = specMapper.toDto(spec);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecWithPatch() throws Exception {
        // Initialize the database
        spec.setId(UUID.randomUUID().toString());
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeUpdate = specRepository.findAll().size();

        // Update the spec using partial update
        Spec partialUpdatedSpec = new Spec();
        partialUpdatedSpec.setId(spec.getId());

        partialUpdatedSpec
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .grade(UPDATED_GRADE)
            .createDt(UPDATED_CREATE_DT)
            .modifyDt(UPDATED_MODIFY_DT);

        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpec))
            )
            .andExpect(status().isOk());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpec.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSpec.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testSpec.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testSpec.getCreateDt()).isEqualTo(UPDATED_CREATE_DT);
        assertThat(testSpec.getModifyDt()).isEqualTo(UPDATED_MODIFY_DT);
    }

    @Test
    @Transactional
    void fullUpdateSpecWithPatch() throws Exception {
        // Initialize the database
        spec.setId(UUID.randomUUID().toString());
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeUpdate = specRepository.findAll().size();

        // Update the spec using partial update
        Spec partialUpdatedSpec = new Spec();
        partialUpdatedSpec.setId(spec.getId());

        partialUpdatedSpec
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .parentId(UPDATED_PARENT_ID)
            .grade(UPDATED_GRADE)
            .createDt(UPDATED_CREATE_DT)
            .modifyDt(UPDATED_MODIFY_DT);

        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpec.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpec))
            )
            .andExpect(status().isOk());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
        Spec testSpec = specList.get(specList.size() - 1);
        assertThat(testSpec.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpec.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSpec.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testSpec.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testSpec.getCreateDt()).isEqualTo(UPDATED_CREATE_DT);
        assertThat(testSpec.getModifyDt()).isEqualTo(UPDATED_MODIFY_DT);
    }

    @Test
    @Transactional
    void patchNonExistingSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(UUID.randomUUID().toString());

        // Create the Spec
        SpecDTO specDTO = specMapper.toDto(spec);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(UUID.randomUUID().toString());

        // Create the Spec
        SpecDTO specDTO = specMapper.toDto(spec);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpec() throws Exception {
        int databaseSizeBeforeUpdate = specRepository.findAll().size();
        spec.setId(UUID.randomUUID().toString());

        // Create the Spec
        SpecDTO specDTO = specMapper.toDto(spec);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(specDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spec in the database
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpec() throws Exception {
        // Initialize the database
        spec.setId(UUID.randomUUID().toString());
        specRepository.saveAndFlush(spec);

        int databaseSizeBeforeDelete = specRepository.findAll().size();

        // Delete the spec
        restSpecMockMvc
            .perform(delete(ENTITY_API_URL_ID, spec.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spec> specList = specRepository.findAll();
        assertThat(specList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
