package com.mktback.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mktback.application.IntegrationTest;
import com.mktback.application.domain.Category;
import com.mktback.application.domain.enumeration.CategoryGrade;
import com.mktback.application.domain.enumeration.CategoryStatus;
import com.mktback.application.repository.CategoryRepository;
import com.mktback.application.service.dto.CategoryDTO;
import com.mktback.application.service.mapper.CategoryMapper;
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
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final CategoryStatus DEFAULT_STATUS = CategoryStatus.Active;
    private static final CategoryStatus UPDATED_STATUS = CategoryStatus.Inactive;

    private static final String DEFAULT_PARENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_ID = "BBBBBBBBBB";

    private static final CategoryGrade DEFAULT_GRADE = CategoryGrade.A;
    private static final CategoryGrade UPDATED_GRADE = CategoryGrade.B;

    private static final String DEFAULT_CREATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_USER = "BBBBBBBBBB";

    private static final String DEFAULT_MODIFY_USER = "AAAAAAAAAA";
    private static final String UPDATED_MODIFY_USER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFY_DT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFY_DT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private Category category;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity(EntityManager em) {
        Category category = new Category()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .parentId(DEFAULT_PARENT_ID)
            .grade(DEFAULT_GRADE)
            .createUser(DEFAULT_CREATE_USER)
            .modifyUser(DEFAULT_MODIFY_USER)
            .createDt(DEFAULT_CREATE_DT)
            .modifyDt(DEFAULT_MODIFY_DT);
        return category;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity(EntityManager em) {
        Category category = new Category()
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .parentId(UPDATED_PARENT_ID)
            .grade(UPDATED_GRADE)
            .createUser(UPDATED_CREATE_USER)
            .modifyUser(UPDATED_MODIFY_USER)
            .createDt(UPDATED_CREATE_DT)
            .modifyDt(UPDATED_MODIFY_DT);
        return category;
    }

    @BeforeEach
    public void initTest() {
        category = createEntity(em);
    }

    @Test
    @Transactional
    void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();
        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCategory.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testCategory.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testCategory.getCreateUser()).isEqualTo(DEFAULT_CREATE_USER);
        assertThat(testCategory.getModifyUser()).isEqualTo(DEFAULT_MODIFY_USER);
        assertThat(testCategory.getCreateDt()).isEqualTo(DEFAULT_CREATE_DT);
        assertThat(testCategory.getModifyDt()).isEqualTo(DEFAULT_MODIFY_DT);
    }

    @Test
    @Transactional
    void createCategoryWithExistingId() throws Exception {
        // Create the Category with an existing ID
        category.setId("existing_id");
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreateUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setCreateUser(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDtIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setCreateDt(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        category.setId(UUID.randomUUID().toString());
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].createUser").value(hasItem(DEFAULT_CREATE_USER)))
            .andExpect(jsonPath("$.[*].modifyUser").value(hasItem(DEFAULT_MODIFY_USER)))
            .andExpect(jsonPath("$.[*].createDt").value(hasItem(DEFAULT_CREATE_DT.toString())))
            .andExpect(jsonPath("$.[*].modifyDt").value(hasItem(DEFAULT_MODIFY_DT.toString())));
    }

    @Test
    @Transactional
    void getCategory() throws Exception {
        // Initialize the database
        category.setId(UUID.randomUUID().toString());
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()))
            .andExpect(jsonPath("$.createUser").value(DEFAULT_CREATE_USER))
            .andExpect(jsonPath("$.modifyUser").value(DEFAULT_MODIFY_USER))
            .andExpect(jsonPath("$.createDt").value(DEFAULT_CREATE_DT.toString()))
            .andExpect(jsonPath("$.modifyDt").value(DEFAULT_MODIFY_DT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategory() throws Exception {
        // Initialize the database
        category.setId(UUID.randomUUID().toString());
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .parentId(UPDATED_PARENT_ID)
            .grade(UPDATED_GRADE)
            .createUser(UPDATED_CREATE_USER)
            .modifyUser(UPDATED_MODIFY_USER)
            .createDt(UPDATED_CREATE_DT)
            .modifyDt(UPDATED_MODIFY_DT);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategory);

        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCategory.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCategory.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testCategory.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testCategory.getModifyUser()).isEqualTo(UPDATED_MODIFY_USER);
        assertThat(testCategory.getCreateDt()).isEqualTo(UPDATED_CREATE_DT);
        assertThat(testCategory.getModifyDt()).isEqualTo(UPDATED_MODIFY_DT);
    }

    @Test
    @Transactional
    void putNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        category.setId(UUID.randomUUID().toString());
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.name(UPDATED_NAME).grade(UPDATED_GRADE).createUser(UPDATED_CREATE_USER).modifyUser(UPDATED_MODIFY_USER);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCategory.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testCategory.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testCategory.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testCategory.getModifyUser()).isEqualTo(UPDATED_MODIFY_USER);
        assertThat(testCategory.getCreateDt()).isEqualTo(DEFAULT_CREATE_DT);
        assertThat(testCategory.getModifyDt()).isEqualTo(DEFAULT_MODIFY_DT);
    }

    @Test
    @Transactional
    void fullUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        category.setId(UUID.randomUUID().toString());
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .parentId(UPDATED_PARENT_ID)
            .grade(UPDATED_GRADE)
            .createUser(UPDATED_CREATE_USER)
            .modifyUser(UPDATED_MODIFY_USER)
            .createDt(UPDATED_CREATE_DT)
            .modifyDt(UPDATED_MODIFY_DT);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCategory.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCategory.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testCategory.getCreateUser()).isEqualTo(UPDATED_CREATE_USER);
        assertThat(testCategory.getModifyUser()).isEqualTo(UPDATED_MODIFY_USER);
        assertThat(testCategory.getCreateDt()).isEqualTo(UPDATED_CREATE_DT);
        assertThat(testCategory.getModifyDt()).isEqualTo(UPDATED_MODIFY_DT);
    }

    @Test
    @Transactional
    void patchNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        category.setId(UUID.randomUUID().toString());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategory() throws Exception {
        // Initialize the database
        category.setId(UUID.randomUUID().toString());
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, category.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
