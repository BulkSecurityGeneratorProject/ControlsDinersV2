package com.dualion.controldiners.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.dualion.controldiners.service.PotService;
import com.dualion.controldiners.web.rest.errors.BadRequestAlertException;
import com.dualion.controldiners.web.rest.util.HeaderUtil;
import com.dualion.controldiners.web.rest.util.PaginationUtil;
import com.dualion.controldiners.web.rest.vm.ExtreureVM;
import com.dualion.controldiners.web.rest.vm.PagamentVM;

import io.github.jhipster.web.util.ResponseUtil;

import com.dualion.controldiners.service.dto.PotDTO;
import com.dualion.controldiners.service.exception.PotException;
import com.dualion.controldiners.service.exception.ProcesException;
import com.dualion.controldiners.service.exception.QuantitatException;
import com.dualion.controldiners.service.exception.UsuarisProcesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Pot.
 */
@RestController
public class PotResource {

	private final Logger log = LoggerFactory.getLogger(PotResource.class);
    
	@Autowired
    private PotService potService;

	private static final String ENTITY_NAME = "pot";
	
    /**
     * POST  /api/pots/pagament : Create a new pagament.
     *
     * @param PagamentVM 
     * @return the ResponseEntity with status 201 (Created) and with body the new potDTO, or with status 400 (Bad Request) if the pot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/pots/pagament")
    @Timed
    public ResponseEntity<PotDTO> createPagament(@Valid @RequestBody PagamentVM pagamentVM) throws URISyntaxException {
    	log.debug("REST request to Pagament pot : {}", pagamentVM);
    	PotDTO potDTO;
		try {
			potDTO = potService.savePagament(pagamentVM);
		} catch (QuantitatException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "quantitatnotexist");
		} catch (ProcesException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "procesnoactive");
		} catch (UsuarisProcesException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "usuarisproces");
		}

		return ResponseEntity.created(new URI("/api/pots/" + potDTO.getId()))
		        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, potDTO.getId().toString()))
		        .body(potDTO);
    }
    
    /**
     * POST  /api/pots/cancelarpagament : Cancel·lar pagament.
     *
     * @param PagamentVM 
     * @return the ResponseEntity with status 201 (Created) and with body the new potDTO, or with status 400 (Bad Request) if the pot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/pots/cancelarpagament")
    @Timed
    public ResponseEntity<PotDTO> createCancelarPagament(@Valid @RequestBody PagamentVM pagamentVM) throws URISyntaxException {
    	log.debug("REST request to Cancelar Pagament pot : {}", pagamentVM);
    	PotDTO potDTO;
    	try {
    		potDTO = potService.cancelarPagament(pagamentVM);
    	} catch (PotException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "potnegatiu");
		} catch (ProcesException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "procesnoactive");
		} catch (UsuarisProcesException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "usuarisproces");
		}
		
		return ResponseEntity.created(new URI("/api/pots/" + potDTO.getId()))
		        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, potDTO.getId().toString()))
		        .body(potDTO);
    }

    /**
     * POST  /api/pots/extreure : Create a new Extraccio.
     *
     * @param PagamentVM 
     * @return the ResponseEntity with status 201 (Created) and with body the new potDTO, or with status 400 (Bad Request) if the pot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/pots/extreure")
    @Timed
    public ResponseEntity<PotDTO> createExtreure(@Valid @RequestBody ExtreureVM extreureVM) throws URISyntaxException {
    	log.debug("REST request to extreure pot : {}", extreureVM);
    	PotDTO potDTO;
		try {
			potDTO = potService.saveExtreure(extreureVM);
		} catch (PotException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "potnegatiu");
		}
		
		return ResponseEntity.created(new URI("/api/pots/" + potDTO.getId()))
		        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, potDTO.getId().toString()))
		        .body(potDTO);
    }
    
    /**
     * GET  /public/pots : get all the pots.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pots in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/public/pots")
    @Timed
    public ResponseEntity<List<PotDTO>> getAllPots(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pots");
        Page<PotDTO> page = potService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /public/pots/:id : get the "id" pot.
     *
     * @param id the id of the potDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the potDTO, or with status 404 (Not Found)
     */
    @GetMapping("/public/pots/{id}")
    @Timed
    public ResponseEntity<PotDTO> getPot(@PathVariable Long id) {
        log.debug("REST request to get Pot : {}", id);
        PotDTO potDTO = potService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(potDTO));
    }

    /**
     * GET  /public/pots/last : get last pot.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the potDTO, or with status 404 (Not Found)
     */
    @GetMapping("/public/pots/last")
    @Timed
    public ResponseEntity<PotDTO> getLastPot() {
        log.debug("REST request to get last Pot");
        PotDTO potDTO = potService.findLast();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(potDTO));
    }
}
