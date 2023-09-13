package ua.gov.odisey.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.gov.odisey.web.rest.TestUtil.sameInstant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ua.gov.odisey.IntegrationTest;
import ua.gov.odisey.domain.Dodument;
import ua.gov.odisey.domain.enumeration.Status;
import ua.gov.odisey.repository.DodumentRepository;

/**
 * Integration tests for the {@link DodumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DodumentResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIONS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTIONS_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Status DEFAULT_STATUS = Status.DRAFT;
    private static final Status UPDATED_STATUS = Status.STATUS1;

    private static final String ENTITY_API_URL = "/api/doduments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DodumentRepository dodumentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDodumentMockMvc;

    private Dodument dodument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dodument createEntity(EntityManager em) {
        Dodument dodument = new Dodument()
            .description(DEFAULT_DESCRIPTION)
            .actionsDescription(DEFAULT_ACTIONS_DESCRIPTION)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
        return dodument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dodument createUpdatedEntity(EntityManager em) {
        Dodument dodument = new Dodument()
            .description(UPDATED_DESCRIPTION)
            .actionsDescription(UPDATED_ACTIONS_DESCRIPTION)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
        return dodument;
    }

    @BeforeEach
    public void initTest() {
        dodument = createEntity(em);
    }

    @Test
    @Transactional
    void createDodument() throws Exception {
        int databaseSizeBeforeCreate = dodumentRepository.findAll().size();
        // Create the Dodument
        restDodumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isCreated());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeCreate + 1);
        Dodument testDodument = dodumentList.get(dodumentList.size() - 1);
        assertThat(testDodument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDodument.getActionsDescription()).isEqualTo(DEFAULT_ACTIONS_DESCRIPTION);
        assertThat(testDodument.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDodument.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createDodumentWithExistingId() throws Exception {
        // Create the Dodument with an existing ID
        dodument.setId(1L);

        int databaseSizeBeforeCreate = dodumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDodumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDoduments() throws Exception {
        // Initialize the database
        dodumentRepository.saveAndFlush(dodument);

        // Get all the dodumentList
        restDodumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dodument.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].actionsDescription").value(hasItem(DEFAULT_ACTIONS_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDodument() throws Exception {
        // Initialize the database
        dodumentRepository.saveAndFlush(dodument);

        // Get the dodument
        restDodumentMockMvc
            .perform(get(ENTITY_API_URL_ID, dodument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dodument.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.actionsDescription").value(DEFAULT_ACTIONS_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDodument() throws Exception {
        // Get the dodument
        restDodumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDodument() throws Exception {
        // Initialize the database
        dodumentRepository.saveAndFlush(dodument);

        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();

        // Update the dodument
        Dodument updatedDodument = dodumentRepository.findById(dodument.getId()).get();
        // Disconnect from session so that the updates on updatedDodument are not directly saved in db
        em.detach(updatedDodument);
        updatedDodument
            .description(UPDATED_DESCRIPTION)
            .actionsDescription(UPDATED_ACTIONS_DESCRIPTION)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restDodumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDodument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDodument))
            )
            .andExpect(status().isOk());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
        Dodument testDodument = dodumentList.get(dodumentList.size() - 1);
        assertThat(testDodument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDodument.getActionsDescription()).isEqualTo(UPDATED_ACTIONS_DESCRIPTION);
        assertThat(testDodument.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDodument.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingDodument() throws Exception {
        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();
        dodument.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDodumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dodument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDodument() throws Exception {
        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();
        dodument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDodumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDodument() throws Exception {
        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();
        dodument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDodumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDodumentWithPatch() throws Exception {
        // Initialize the database
        dodumentRepository.saveAndFlush(dodument);

        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();

        // Update the dodument using partial update
        Dodument partialUpdatedDodument = new Dodument();
        partialUpdatedDodument.setId(dodument.getId());

        partialUpdatedDodument
            .description(UPDATED_DESCRIPTION)
            .actionsDescription(UPDATED_ACTIONS_DESCRIPTION)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restDodumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDodument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDodument))
            )
            .andExpect(status().isOk());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
        Dodument testDodument = dodumentList.get(dodumentList.size() - 1);
        assertThat(testDodument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDodument.getActionsDescription()).isEqualTo(UPDATED_ACTIONS_DESCRIPTION);
        assertThat(testDodument.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDodument.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDodumentWithPatch() throws Exception {
        // Initialize the database
        dodumentRepository.saveAndFlush(dodument);

        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();

        // Update the dodument using partial update
        Dodument partialUpdatedDodument = new Dodument();
        partialUpdatedDodument.setId(dodument.getId());

        partialUpdatedDodument
            .description(UPDATED_DESCRIPTION)
            .actionsDescription(UPDATED_ACTIONS_DESCRIPTION)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restDodumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDodument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDodument))
            )
            .andExpect(status().isOk());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
        Dodument testDodument = dodumentList.get(dodumentList.size() - 1);
        assertThat(testDodument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDodument.getActionsDescription()).isEqualTo(UPDATED_ACTIONS_DESCRIPTION);
        assertThat(testDodument.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDodument.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDodument() throws Exception {
        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();
        dodument.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDodumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dodument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDodument() throws Exception {
        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();
        dodument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDodumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDodument() throws Exception {
        int databaseSizeBeforeUpdate = dodumentRepository.findAll().size();
        dodument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDodumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dodument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dodument in the database
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDodument() throws Exception {
        // Initialize the database
        dodumentRepository.saveAndFlush(dodument);

        int databaseSizeBeforeDelete = dodumentRepository.findAll().size();

        // Delete the dodument
        restDodumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, dodument.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dodument> dodumentList = dodumentRepository.findAll();
        assertThat(dodumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
