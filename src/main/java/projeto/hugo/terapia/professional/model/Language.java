package projeto.hugo.terapia.professional.model;
import jakarta.persistence.*;
import lombok.Data;
import projeto.hugo.terapia.professional.enumeracoes.LanguagesEnum;
import projeto.hugo.terapia.professional.enumeracoes.LevelLanguage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Data
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column()
    @Enumerated(EnumType.STRING)
    private LanguagesEnum language;

    @Column()
    @Enumerated(EnumType.STRING)
    private LevelLanguage level;

    @ManyToMany(mappedBy = "languages")
    private Set<Professional> professionals = new HashSet<>();

}
