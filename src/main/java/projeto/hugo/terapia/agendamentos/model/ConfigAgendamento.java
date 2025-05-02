package projeto.hugo.terapia.agendamentos.model;

import jakarta.persistence.*;
import lombok.Data;
import projeto.hugo.terapia.professional.model.Professional;

import java.util.UUID;

@Entity
@Table
@Data
public class ConfigAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Double price;

    @Column
    private Integer duration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "professional_id", referencedColumnName = "id")
    private Professional professional;

}
