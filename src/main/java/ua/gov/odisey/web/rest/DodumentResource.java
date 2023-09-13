package ua.gov.odisey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import ua.gov.odisey.domain.Dodument;
import ua.gov.odisey.repository.DodumentRepository;
import ua.gov.odisey.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link ua.gov.odisey.domain.Dodument}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DodumentResource {

    private final Logger log = LoggerFactory.getLogger(DodumentResource.class);

    private static final String ENTITY_NAME = "dodument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DodumentRepository dodumentRepository;

    public DodumentResource(DodumentRepository dodumentRepository) {
        this.dodumentRepository = dodumentRepository;
    }

    /**
     * {@code POST  /doduments} : Create a new dodument.
     *
     * @param dodument the dodument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dodument, or with status {@code 400 (Bad Request)} if the dodument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doduments")
    public ResponseEntity<Dodument> createDodument(@RequestBody Dodument dodument) throws URISyntaxException {
        log.debug("REST request to save Dodument : {}", dodument);
        if (dodument.getId() != null) {
            throw new BadRequestAlertException("A new dodument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dodument result = dodumentRepository.save(dodument);
        return ResponseEntity
            .created(new URI("/api/doduments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doduments/:id} : Updates an existing dodument.
     *
     * @param id the id of the dodument to save.
     * @param dodument the dodument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dodument,
     * or with status {@code 400 (Bad Request)} if the dodument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dodument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doduments/{id}")
    public ResponseEntity<Dodument> updateDodument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dodument dodument
    ) throws URISyntaxException {
        log.debug("REST request to update Dodument : {}, {}", id, dodument);
        if (dodument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dodument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dodumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dodument result = dodumentRepository.save(dodument);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dodument.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doduments/:id} : Partial updates given fields of an existing dodument, field will ignore if it is null
     *
     * @param id the id of the dodument to save.
     * @param dodument the dodument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dodument,
     * or with status {@code 400 (Bad Request)} if the dodument is not valid,
     * or with status {@code 404 (Not Found)} if the dodument is not found,
     * or with status {@code 500 (Internal Server Error)} if the dodument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doduments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dodument> partialUpdateDodument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dodument dodument
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dodument partially : {}, {}", id, dodument);
        if (dodument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dodument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dodumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dodument> result = dodumentRepository
            .findById(dodument.getId())
            .map(existingDodument -> {
                if (dodument.getDescription() != null) {
                    existingDodument.setDescription(dodument.getDescription());
                }
                if (dodument.getActionsDescription() != null) {
                    existingDodument.setActionsDescription(dodument.getActionsDescription());
                }
                if (dodument.getDate() != null) {
                    existingDodument.setDate(dodument.getDate());
                }
                if (dodument.getStatus() != null) {
                    existingDodument.setStatus(dodument.getStatus());
                }

                return existingDodument;
            })
            .map(dodumentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dodument.getId().toString())
        );
    }

    /**
     * {@code GET  /doduments} : get all the doduments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doduments in body.
     */
    @GetMapping("/doduments")
    public ResponseEntity<List<Dodument>> getAllDoduments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Doduments");
        Page<Dodument> page = dodumentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doduments/:id} : get the "id" dodument.
     *
     * @param id the id of the dodument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dodument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doduments/{id}")
    public ResponseEntity<Dodument> getDodument(@PathVariable Long id) {
        log.debug("REST request to get Dodument : {}", id);
        Optional<Dodument> dodument = dodumentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dodument);
    }

    /**
     * {@code DELETE  /doduments/:id} : delete the "id" dodument.
     *
     * @param id the id of the dodument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doduments/{id}")
    public ResponseEntity<Void> deleteDodument(@PathVariable Long id) {
        log.debug("REST request to delete Dodument : {}", id);
        dodumentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
