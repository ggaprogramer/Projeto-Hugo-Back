package projeto.hugo.terapia.profile.enumeracoes;

import lombok.Getter;

@Getter
public enum UnitSizeFile {
    B("Bytes"),
    KB("KB"),
    MB("MB"),
    GB("GB"),
    TB("TB");

    private String descricao;

    UnitSizeFile(String descricao){
        this.descricao = descricao;
    }
}
