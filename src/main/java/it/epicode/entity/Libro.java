package it.epicode.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Libro extends Stampa{
    private String autore;
    private String genere;
}
