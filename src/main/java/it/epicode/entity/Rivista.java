package it.epicode.entity;

import it.epicode.enums.Periodicita;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Rivista extends Stampa {
    Periodicita periodicita;
}
