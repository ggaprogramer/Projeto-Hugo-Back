package projeto.hugo.terapia.professional.enumeracoes;

public enum LevelLanguage {
    NATIVO("Nativo"),
    BASICO("Básico"),
    INTERMEDIARIO("Intermediário"),
    AVANÇADO("Avançado"),
    FLUENTE("Fluente");

    private final String nome;

    LevelLanguage(String nome) {
        this.nome = nome;
    }
}
