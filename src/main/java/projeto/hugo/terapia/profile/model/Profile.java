package projeto.hugo.terapia.profile.model;

import projeto.hugo.terapia.authentication.enumeracoes.RolesUsers;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.profile.enumeracoes.ProfileInterests;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String phone;

    @Column
    private String age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Usuario user;

    @JdbcTypeCode(Types.ARRAY) // Define o tipo JDBC como ARRAY
    @Column(name = "interests", columnDefinition = "varchar[]")
    @Enumerated(EnumType.STRING)
    private List<ProfileInterests> interests;

}
