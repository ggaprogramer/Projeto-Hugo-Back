package projeto.hugo.terapia.authentication.dto;

import projeto.hugo.terapia.authentication.enumeracoes.RolesUsers;

import java.util.List;

public record isAuthenticatedResponseDTO(String token, List<RolesUsers> roles) {
}
