package projeto.hugo.terapia.authentication.dto;

import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;

public record ResponseLoginDTO (StatusResponse status, String message, String type, String token, TypeProfile typeProfile) {
}
