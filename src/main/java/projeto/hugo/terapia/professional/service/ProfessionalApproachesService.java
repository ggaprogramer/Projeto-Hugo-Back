package projeto.hugo.terapia.professional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.professional.dto.CreateApproachesDTO;
import projeto.hugo.terapia.professional.dto.CreateProfessionalInterestsDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalApproachDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalInterestsDTO;
import projeto.hugo.terapia.professional.model.Approach;
import projeto.hugo.terapia.professional.model.ProfessionalInterests;
import projeto.hugo.terapia.professional.repository.ProfessionalApproachesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionalApproachesService {

    private final ProfessionalApproachesRepository professionalApproachesRepository;

    public List<Approach> getApproachesProfessional(List<String> approaches){
        List<Approach> professionalApproachesList = new ArrayList<>();

        for(String approach : approaches){
            Approach professionalApproach = professionalApproachesRepository.findByValue(approach);
            if(professionalApproach != null){
                professionalApproachesList.add(professionalApproach);
            }
        }

        return professionalApproachesList;
    }

    public List<ProfessionalApproachDTO> createProfessionalApproaches(List<CreateApproachesDTO> createApproachesDTO){
        List<Approach> professionalApproachesList = new ArrayList<>();

        for(CreateApproachesDTO professionalApproach : createApproachesDTO) {
            String value = professionalApproach.value();
            String label = professionalApproach.label();

            Approach approach = new Approach();
            approach.setValue(value);
            approach.setLabel(label);

            professionalApproachesRepository.save(approach);
            professionalApproachesList.add(approach);
        }

        return professionalApproachesList.stream()
                .map(approach ->
                        new ProfessionalApproachDTO(approach.getValue(), approach.getLabel()))
                .collect(Collectors.toList());
    }

    public List<ProfessionalApproachDTO> extractProfessionalApproaches(){
        List<Approach> professionalApproaches = professionalApproachesRepository.findAll();
        return professionalApproaches.stream()
                .map(approach ->
                        new ProfessionalApproachDTO(approach.getValue(), approach.getLabel()))
                .collect(Collectors.toList());
    }

}
