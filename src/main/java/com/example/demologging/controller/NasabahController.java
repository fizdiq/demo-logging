package com.example.demologging.controller;

import com.example.demologging.dto.handler.ResponseHandler;
import com.example.demologging.model.Nasabah;
import com.example.demologging.services.NasabahService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/nasabah")
@Slf4j
public class NasabahController {

    @Autowired
    private final NasabahService nasabahService;

    public NasabahController(NasabahService nasabahService) {
        this.nasabahService = nasabahService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody final Nasabah nasabah) {
        MDC.put("key", nasabah.getKtp());
        try {
            log.info("======== EXECUTING SAVE NASABAH ========");
            Nasabah result = nasabahService.save(nasabah);
            return ResponseHandler.generateResponse("Sukses menambah nasabah!", HttpStatus.CREATED, result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("========= ERROR SAVING NASABAH =========");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        MDC.put("key", UUID.randomUUID().toString().replace("-", ""));
        try {
            log.info("======== EXECUTING GET ALL NASABAH ==========");
            Iterable<Nasabah> result = nasabahService.getAll();
            return ResponseHandler.generateResponse("Sukses mendapatkan semua nasabah!", HttpStatus.ACCEPTED, result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("========ERROR GET ALL=========");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        } finally {
            MDC.clear();
        }
    }

    @PostMapping("/{ktp}")
    public ResponseEntity<Object> findByKtp(@PathVariable final String ktp) {
        MDC.put("key", ktp);
        try {
            log.info("======== EXECUTING FIND BY KTP =========");
            Nasabah result = nasabahService.findByKtp(ktp);
            if (result != null) {
                log.info("======== NASABAH FOUND =========");
                return ResponseHandler.generateResponse("Sukses mendapatkan nasabah", HttpStatus.ACCEPTED, result);
            } else {
                log.error("======== NASABAH NOT FOUND ========");
                return ResponseHandler.generateResponse("Gagal mendapatkan nasabah", HttpStatus.BAD_REQUEST, null);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("========ERROR FIND BY KTP=========");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        } finally {
            MDC.clear();
        }
    }
}
