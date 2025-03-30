package projeto.hugo.terapia.professional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.professional.dto.CreateProfessionalInterestsDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalInterestsDTO;
import projeto.hugo.terapia.professional.model.ProfessionalInterests;
import projeto.hugo.terapia.professional.repository.ProfessionalInterestsRepository;
import projeto.hugo.terapia.profile.dto.ProfileInterestsDTO;
import projeto.hugo.terapia.profile.model.ProfileInterests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionalInterestsService {

    private final ProfessionalInterestsRepository professionalInterestsRepository;

    public List<ProfessionalInterests> getInterestsProfessional(List<String> interests){
        List<ProfessionalInterests> professionalInterestsList = new ArrayList<>();

        for(String interest : interests){
            ProfessionalInterests profileInterest = professionalInterestsRepository.findByValue(interest);
            if(professionalInterestsRepository.findByValue(interest) != null){
                professionalInterestsList.add(profileInterest);
            }
        }

        return professionalInterestsList;
    }

    public List<ProfessionalInterests> createProfessionalInterests(List<CreateProfessionalInterestsDTO> createProfessionalInterestsDTOS){
        List<ProfessionalInterests> professionalInterestsList = new ArrayList<>();

        for(CreateProfessionalInterestsDTO professionalInterest : createProfessionalInterestsDTOS) {
            String value = professionalInterest.value();
            String label = professionalInterest.label();

            ProfessionalInterests professionalInterests = new ProfessionalInterests();
            professionalInterests.setValue(value);
            professionalInterests.setLabel(label);

            professionalInterestsRepository.save(professionalInterests);
            professionalInterestsList.add(professionalInterests);
        }

        return professionalInterestsList;
    }

    public List<ProfessionalInterestsDTO> extractProfessionalInterests(){
        List<ProfessionalInterests> professionalInterests = professionalInterestsRepository.findAll();
        return professionalInterests
                .stream()
                .map(interest ->
                        new ProfessionalInterestsDTO(interest.getValue(), interest.getLabel()))
                .collect(Collectors.toList());
    }

}
