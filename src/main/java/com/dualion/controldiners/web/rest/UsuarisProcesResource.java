package com.dualion.controldiners.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dualion.controldiners.service.UsuarisProcesService;
import com.dualion.controldiners.web.rest.errors.BadRequestAlertException;
import com.dualion.controldiners.web.rest.util.HeaderUtil;
import com.dualion.controldiners.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

import com.dualion.controldiners.service.dto.UsuarisProcesDTO;
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
 * REST controller for managing UsuarisProces.
 */
@RestController
public class UsuarisProcesResource {

	private final Logger log = LoggerFactory.getLogger(UsuarisProcesResource.class);
    
	@Autowired
    private UsuarisProcesService usuarisProcesService;

	private static final String ENTITY_NAME = "usuarisProces";
	
    /**
     * GET  /usuaris-proces/actiu : get all the usuarisProces del proces actiu.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of usuarisProces in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/public/usuaris-proces/actiu")
    @Timed
    public ResponseEntity<List<UsuarisProcesDTO>> getAllUsuarisProcesActiu()
        throws URISyntaxException {
        log.debug("REST request to get a page of UsuarisProces");
        List<UsuarisProcesDTO> list = usuarisProcesService.findAllProcesActiu();
        log.debug("UsuarisProces: " + list.toString());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /**
     * GET  /usuaris-proces : get all the usuarisProces.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of usuarisProces in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/public/usuaris-proces")
    @Timed
    public ResponseEntity<List<UsuarisProcesDTO>> getAllUsuarisProces(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UsuarisProces");
        Page<UsuarisProcesDTO> page = usuarisProcesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usuaris-proces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /usuaris-proces/:id : get the "id" usuarisProces.
     *
     * @param id the id of the usuarisProcesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usuarisProcesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/public/usuaris-proces/{id}")
    @Timed
    public ResponseEntity<UsuarisProcesDTO> getUsuarisProces(@PathVariable Long id) {
        log.debug("REST request to get UsuarisProces : {}", id);
        UsuarisProcesDTO usuarisProcesDTO = usuarisProcesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(usuarisProcesDTO));
    }
}
