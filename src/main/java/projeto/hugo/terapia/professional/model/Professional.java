package projeto.hugo.terapia.professional.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.ProfileInterests;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

}
