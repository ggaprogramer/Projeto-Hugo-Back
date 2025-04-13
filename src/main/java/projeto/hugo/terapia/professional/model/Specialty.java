package projeto.hugo.terapia.professional.model;

import jakarta.persistence.*;
import lombok.Data;
import projeto.hugo.terapia.professional.enumeracoes.LevelLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String value;

    @Column
    private String label;

    @ManyToMany(mappedBy = "specialties")
    private List<Professional> professionals = new ArrayList<>();

}
