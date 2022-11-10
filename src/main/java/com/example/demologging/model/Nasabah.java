package com.example.demologging.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NASABAH")
public class Nasabah {

    @Id
    @GeneratedValue
    private Integer id;

    @Length(min = 2, message = "Nama harus lebih dari 2 huruf.")
    private String nama;

    private String alamat;

    private String ktp;
}
