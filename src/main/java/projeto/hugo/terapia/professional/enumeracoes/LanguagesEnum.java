package projeto.hugo.terapia.professional.enumeracoes;

public enum LanguagesEnum {
    PORTUGUES("Português"),
    INGLES("Inglês"),
    ESPANHOL("Espanhol"),
    FRANCES("Francês"),
    ALEMAO("Alemão"),
    ITALIANO("Italiano"),
    CHINES("Chinês"),
    JAPONES("Japonês"),
    ARABE("Árabe"),
    RUSSO("Russo");

    private final String nome;

    LanguagesEnum(String nome) {
        this.nome = nome;
    }
}
