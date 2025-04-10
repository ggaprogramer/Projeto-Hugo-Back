package projeto.hugo.terapia.professional.model;

import jakarta.persistence.*;
import lombok.Data;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table
@Data
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String phone;

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
            name = "professional_interest",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private List<ProfessionalInterests> interests = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "professional_approach",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "approach_id")
    )
    private List<Approach> approaches = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "professional_specialty",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private List<Specialty> specialties = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "professional_language",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languages = new ArrayList<>();

    @OneToOne(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfessionalPhoto photo;

    @Column(name="registration_completed")
    private Boolean registrationCompleted;

    @PrePersist
    private void prePersistRegistrationCompleted(){
        if(this.registrationCompleted == null || interests.isEmpty() || approaches.isEmpty()
                || specialties.isEmpty() || languages.isEmpty()){
            setRegistrationCompleted(false);
        }
    }
    // Exclusivos do profissional - FIM

}
