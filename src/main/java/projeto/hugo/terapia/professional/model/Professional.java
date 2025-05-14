package projeto.hugo.terapia.professional.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import projeto.hugo.terapia.agendamentos.model.ConfigAgendamento;
import projeto.hugo.terapia.agendamentos.model.DateHourAgendamento;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table
@Getter
@Setter
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String crp;

    @Column(columnDefinition = "TEXT", unique = true)
    private String description;

    @Column
    private LocalDate dateBirth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Usuario user;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany
    @JoinTable(
            name = "professional_interests_relation",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private List<ProfessionalInterests> interests = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "professional_approaches_relation",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "approach_id")
    )
    private List<Approach> approaches = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "professional_specialties_relation",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private List<Specialty> specialties = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "professional_languages_relation",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languages = new ArrayList<>();

    @OneToOne(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfessionalPhoto photo;

    @OneToOne(mappedBy = "professional", cascade = CascadeType.ALL)
    private ConfigAgendamento configAgendamento;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateHourAgendamento> dateHourAgendamentos = new ArrayList<>();

    @Column(name="registration_completed")
    private Boolean registrationCompleted;

    @PrePersist
    private void prePersistRegistrationCompleted(){
        if(this.registrationCompleted == null){
            setRegistrationCompleted(false);
        }
    }

    @PreUpdate
    private void preUpdateRegistrationCompleted(){
        if(photo == null || interests.isEmpty() || approaches.isEmpty()
                || specialties.isEmpty() || languages.isEmpty()
                || crp == null || description == null){
            setRegistrationCompleted(false);
        } else {
            setRegistrationCompleted(true);
        }
    }
    // Exclusivos do profissional - FIM

    @Override
    public String toString() {
        return id.toString();
    }

}
