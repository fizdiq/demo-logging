package com.example.demologging.repository;

import com.example.demologging.model.Nasabah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NasabahRepository extends JpaRepository<Nasabah, Integer> {

    Optional<Nasabah> findByKtp(String ktp);
}
