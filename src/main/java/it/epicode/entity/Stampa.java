package it.epicode.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "stampe")
//@NamedQuery(name="Trova_tutto_Stampa", query="SELECT a FROM Stampa a")
public class Stampa {
@Id
@GeneratedValue(strategy = GenerationType.UUID)
    private String isbn;
    private String titolo;
    private int annoPublicazione;
    private int numeroPagine;
}
