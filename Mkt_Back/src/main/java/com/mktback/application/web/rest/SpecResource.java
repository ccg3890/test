package com.mktback.application.web.rest;

import com.mktback.application.repository.SpecRepository;
import com.mktback.application.service.SpecService;
import com.mktback.application.service.dto.SpecDTO;
import com.mktback.application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mktback.application.domain.Spec}.
 */
@RestController
@RequestMapping("/api")
public class SpecResource {

    private final Logger log = LoggerFactory.getLogger(SpecResource.class);

    private static final String ENTITY_NAME = "spec";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecService specService;

    private final SpecRepository specRepository;

    public SpecResource(SpecService specService, SpecRepository specRepository) {
        this.specService = specService;
        this.specRepository = specRepository;
    }

    /**
     * {@code POST  /specs} : Create a new spec.
     *
     * @param specDTO the specDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specDTO, or with status {@code 400 (Bad Request)} if the spec has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specs")
    public ResponseEntity<SpecDTO> createSpec(@Valid @RequestBody SpecDTO specDTO) throws URISyntaxException {
        log.debug("REST request to save Spec : {}", specDTO);
        if (specDTO.getId() != null) {
            throw new BadRequestAlertException("A new spec cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecDTO result = specService.save(specDTO);
        return ResponseEntity
            .created(new URI("/api/specs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /specs/:id} : Updates an existing spec.
     *
     * @param id the id of the specDTO to save.
     * @param specDTO the specDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specDTO,
     * or with status {@code 400 (Bad Request)} if the specDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specs/{id}")
    public ResponseEntity<SpecDTO> updateSpec(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody SpecDTO specDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Spec : {}, {}", id, specDTO);
        if (specDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecDTO result = specService.update(specDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /specs/:id} : Partial updates given fields of an existing spec, field will ignore if it is null
     *
     * @param id the id of the specDTO to save.
     * @param specDTO the specDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specDTO,
     * or with status {@code 400 (Bad Request)} if the specDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/specs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecDTO> partialUpdateSpec(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody SpecDTO specDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Spec partially : {}, {}", id, specDTO);
        if (specDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecDTO> result = specService.partialUpdate(specDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specDTO.getId()));
    }

    /**
     * {@code GET  /specs} : get all the specs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specs in body.
     */
    @GetMapping("/specs")
    public ResponseEntity<List<SpecDTO>> getAllSpecs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Specs");
        Page<SpecDTO> page = specService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specs/:id} : get the "id" spec.
     *
     * @param id the id of the specDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specs/{id}")
    public ResponseEntity<SpecDTO> getSpec(@PathVariable String id) {
        log.debug("REST request to get Spec : {}", id);
        Optional<SpecDTO> specDTO = specService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specDTO);
    }

    /**
     * {@code DELETE  /specs/:id} : delete the "id" spec.
     *
     * @param id the id of the specDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specs/{id}")
    public ResponseEntity<Void> deleteSpec(@PathVariable String id) {
        log.debug("REST request to delete Spec : {}", id);
        specService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
