package projeto.hugo.terapia.professional.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.professional.enumeracoes.Approach;
import projeto.hugo.terapia.professional.enumeracoes.Specialty;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.ProfileInterests;

import java.sql.Types;
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

    @JdbcTypeCode(Types.ARRAY) // Define o tipo JDBC como ARRAY
    @Column(name = "interests", columnDefinition = "varchar[]")
    @Enumerated(EnumType.STRING)
    private List<ProfileInterests> interests;

    // Exclusivos do profissional - INÍCIO
    @JdbcTypeCode(Types.ARRAY) // Define o tipo JDBC como ARRAY
    @Column(name = "approach", columnDefinition = "varchar[]")
    @Enumerated(EnumType.STRING)
    private List<Approach> approach;

    @JdbcTypeCode(Types.ARRAY) // Define o tipo JDBC como ARRAY
    @Column(name = "specialty", columnDefinition = "varchar[]")
    @Enumerated(EnumType.STRING)
    private List<Specialty> specialty;

    @ManyToMany
    @JoinTable(
            name = "professional_language",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> languages = new HashSet<>();

    @Column(name="registration_completed")
    private Boolean registrationCompleted;

    @PrePersist
    private void prePersistRegistrationCompleted(){
        if(this.registrationCompleted == null){
            setRegistrationCompleted(false);
        }
    }
    // Exclusivos do profissional - FIM

}
