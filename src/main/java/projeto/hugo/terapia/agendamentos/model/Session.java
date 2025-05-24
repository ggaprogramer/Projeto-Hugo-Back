package projeto.hugo.terapia.agendamentos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column
    private Integer duration;

    @Column(updatable = false)
    private LocalDateTime dateHourSessionFinallized;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "date_hour_session_id", referencedColumnName = "id", nullable = false)
    private DateHourAgendamento dateHourSession;

    @Column(columnDefinition="varchar")
    @Enumerated(EnumType.STRING)
    private StatusSession status;

    @Column(updatable = false)
    private LocalDateTime dateCreated;

    @Column
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist(){
        if(this.status == null){
            setStatus(StatusSession.PROCESSING);
        }
        if(this.dateCreated == null){
            setDateCreated(LocalDateTime.now());
        }
        if(this.lastModifiedDate == null){
            setLastModifiedDate(LocalDateTime.now());
        }
        if(this.dateHourSessionFinallized == null){
            setDateHourSessionFinallized(dateHourSession.getDayHour().plusMinutes(duration));
        }

    }

    @PreUpdate
    private void preUpdate(){
        setLastModifiedDate(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return id.toString();
    }

}
