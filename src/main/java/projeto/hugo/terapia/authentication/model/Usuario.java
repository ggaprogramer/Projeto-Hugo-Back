package projeto.hugo.terapia.authentication.model;
import lombok.Getter;
import lombok.Setter;
import projeto.hugo.terapia.profile.model.Profile;
import jakarta.persistence.*;
import projeto.hugo.terapia.authentication.enumeracoes.RolesUsers;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length=20)
    private String username;

    @Column(nullable = false, length=300)
    private String password;

    @Column(nullable = false, length=150)
    private String email;

    @Column(name="last_login", nullable = true)
    private LocalDateTime lastLogin;

    @Column(name="confirmacao_email")
    private Boolean confirmacaoEmail;

    @PrePersist
    private void verifyConfirmacaoEmail(){
        if(this.confirmacaoEmail == null){
            setConfirmacaoEmail(false);
        }
    }

    //@Convert(converter = RolesUsersConverter.class)
    @JdbcTypeCode(Types.ARRAY) // Define o tipo JDBC como ARRAY
    @Column(name = "roles", columnDefinition = "varchar[]")
    @Enumerated(EnumType.STRING)
    private List<RolesUsers> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

}
