package com.example.demologging.services;

import com.example.demologging.model.Nasabah;
import com.example.demologging.repository.NasabahRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NasabahService {

    @Autowired
    private final NasabahRepository nasabahRepository;

    public NasabahService(NasabahRepository nasabahRepository) {
        this.nasabahRepository = nasabahRepository;
    }

    public Nasabah save(Nasabah nasabah) {
        log.info("=========SAVING NASABAH========");
        nasabahRepository.save(nasabah);
        return nasabah;
    }

    public Iterable<Nasabah> getAll() {
        log.info("========GET ALL NASABAH========");
        return nasabahRepository.findAll();
    }

    public Nasabah findByKtp(String ktp) {
        log.info("========findByKtp========");
        return nasabahRepository.findByKtp(ktp).orElse(null);
    }


}
