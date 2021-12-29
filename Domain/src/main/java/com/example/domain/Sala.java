package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sali")
public class Sala implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer nr_locuri;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Spectacol> lista_spectacole;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Vanzare> lista_vanzari;
}
