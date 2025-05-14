package projeto.hugo.terapia.agendamentos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import projeto.hugo.terapia.agendamentos.enumeracoes.StatusSession;
import projeto.hugo.terapia.payment.model.Payment;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.profile.model.Profile;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String link;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private Payment payment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "date_hour_session_id", referencedColumnName = "id", nullable = false)
    private DateHourAgendamento dateHourSession;

    @Column
    private StatusSession status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    @Override
    public String toString() {
        return id.toString();
    }

}
