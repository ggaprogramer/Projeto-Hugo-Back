package projeto.hugo.terapia.professional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.professional.dto.CreateLanguagesDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalLanguageDTO;
import projeto.hugo.terapia.professional.enumeracoes.LevelLanguage;
import projeto.hugo.terapia.professional.model.Language;
import projeto.hugo.terapia.professional.repository.ProfessionalLanguagesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionalLanguagesService {

    private final ProfessionalLanguagesRepository professionalLanguagesRepository;

    public List<Language> getLanguagesProfessional(List<String> languages){
        List<Language> professionalLanguageList = new ArrayList<>();

        for(String language : languages){
            Language professionalLanguage = professionalLanguagesRepository.findByValue(language);
            if(professionalLanguage != null){
                professionalLanguageList.add(professionalLanguage);
            }
        }

        return professionalLanguageList;
    }

    public List<ProfessionalLanguageDTO> createProfessionalLanguages(List<CreateLanguagesDTO> createLanguagesDTO){
        List<Language> professionalLanguageList = new ArrayList<>();

        for(CreateLanguagesDTO professionalLanguage : createLanguagesDTO) {
            String languageName = professionalLanguage.language();
            LevelLanguage levelLanguage = professionalLanguage.levelLanguage();

            Language language = new Language();
            language.setLanguage(languageName);
            language.setLevel(levelLanguage);

            professionalLanguagesRepository.save(language);
            professionalLanguageList.add(language);
        }

        return professionalLanguageList.stream()
                .map(language ->
                        new ProfessionalLanguageDTO(language.getLanguage(), language.getLevel().name()))
                .collect(Collectors.toList());
    }

    public List<ProfessionalLanguageDTO> extractProfessionalLanguages(){
        List<Language> professionalLanguages = professionalLanguagesRepository.findAll();
        return professionalLanguages.stream()
                .map(language ->
                        new ProfessionalLanguageDTO(language.getLanguage(), language.getLevel().name()))
                .collect(Collectors.toList());
    }

}
