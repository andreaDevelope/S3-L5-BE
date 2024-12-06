package it.epicode.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "stampe")
public class Stampa {
@Id
@GeneratedValue(strategy = GenerationType.UUID)
    private String isbn;
    private String titolo;
    private int annoPublicazione;
    private int numeroPagine;
}
