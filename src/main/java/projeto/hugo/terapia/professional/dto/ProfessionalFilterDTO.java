package projeto.hugo.terapia.professional.dto;

import java.util.List;

public record ProfessionalFilterDTO(
        Integer pagina,
        Integer tamanho,
        String ordenarPor,
        String direcao,
        String nome,
        List<String> abordagens,
        List<String> especialidades,
        List<String> interesses,
        List<String> idiomas,
        Double precoMinimo,
        Double precoMaximo,
        GenderFilter genero,
        List<Disponibilidade> disponibilidade
) {
}
