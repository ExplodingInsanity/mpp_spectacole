package com.example.domain;

import com.example.domain.utils.Converter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "spectacole")
public class Spectacol implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date data_spectacol;
    private String titlu;
    private Float pret_bilet;
    @Convert(converter = Converter.class)
    private List<Integer> lista_locuri_vandute;
    private Float sold;
//    @ManyToOne
//    private Room room;
}
