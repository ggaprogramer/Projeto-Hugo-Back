package projeto.hugo.terapia.profile.model;

import lombok.Getter;
import lombok.Setter;
import projeto.hugo.terapia.agendamentos.model.Session;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Profile {

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

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfilePhoto photo;

    @ManyToMany
    @JoinTable(
            name = "profile_interests_relation",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private List<ProfileInterests> interests = new ArrayList<>();

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
        if(photo == null || interests.isEmpty()){
            setRegistrationCompleted(false);
        } else {
            setRegistrationCompleted(true);
        }
    }

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @Override
    public String toString() {
        return this.id.toString();
    }

}