package projeto.hugo.terapia.authentication.enumeracoes;
import lombok.Getter;

@Getter
public enum RolesUsers {
    ADMIN("ADMIN"),
    USER("USER"),
    PROFESSIONAL("PROFESSIONAL"),
    PROFILE("PROFILE");

    private String descricao;

    RolesUsers(String descricao){
        this.descricao = descricao;
    }
}
