package projeto.hugo.terapia.professional.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import projeto.hugo.terapia.profile.model.Profile;

import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class ProfessionalPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "professional_id", referencedColumnName = "id", nullable = false)
    private Professional professional;

    @Column
    private String bucket;

    @Column
    private String name;

}
