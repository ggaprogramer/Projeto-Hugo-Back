package projeto.hugo.terapia.professional.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import projeto.hugo.terapia.professional.enumeracoes.LevelLanguage;

import java.util.*;

@Entity
@Table
@Getter
@Setter
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String value;

    @Column
    private String label;

    @Column()
    @Enumerated(EnumType.STRING)
    private LevelLanguage level;

    @ManyToMany(mappedBy = "languages")
    private List<Professional> professionals = new ArrayList<>();

    @Override
    public String toString() {
        return this.id.toString();
    }

}
