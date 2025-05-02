package projeto.hugo.terapia.professional.enumeracoes;

import lombok.Getter;

@Getter
public enum LevelLanguage {
    NATIVO("Nativo"),
    BASICO("Básico"),
    INTERMEDIARIO("Intermediário"),
    AVANCADO("Avançado"),
    FLUENTE("Fluente");

    private final String name;

    LevelLanguage(String name) {
        this.name = name;
    }
}
