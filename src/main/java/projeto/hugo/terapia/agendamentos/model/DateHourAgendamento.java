package projeto.hugo.terapia.agendamentos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import projeto.hugo.terapia.professional.model.Professional;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class DateHourAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dayHour;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @Override
    public String toString() {
        return id.toString();
    }

}
