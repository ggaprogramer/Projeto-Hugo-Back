package projeto.hugo.terapia.professional.enumeracoes;

public enum LevelLanguage {
    NATIVO("Nativo"),
    BASICO("Básico"),
    INTERMEDIARIO("Intermediário"),
    AVANÇADO("Avançado"),
    FLUENTE("Fluente");

    private final String name;

    LevelLanguage(String name) {
        this.name = name;
    }
}
