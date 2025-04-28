package projeto.hugo.terapia.professional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.professional.dto.CreateSpecialtiesDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalSpecialtyDTO;
import projeto.hugo.terapia.professional.model.Specialty;
import projeto.hugo.terapia.professional.repository.ProfessionalSpecialtiesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionalSpecialtiesService {

    private final ProfessionalSpecialtiesRepository professionalSpecialtiesRepository;

    public List<Specialty> getSpecialtiesProfessional(List<String> specialties){
        List<Specialty> professionalSpecialtyList = new ArrayList<>();

        for(String specialtiy : specialties){
            Specialty professionalSpecialty = professionalSpecialtiesRepository.findByValue(specialtiy);
            if(professionalSpecialty != null){
                professionalSpecialtyList.add(professionalSpecialty);
            }
        }

        return professionalSpecialtyList;
    }

    public List<UUID> getSpecialtiesProfessionalUUID(List<String> specialties){
        List<UUID> professionalSpecialtyList = new ArrayList<>();

        for(String specialtiy : specialties){
            Specialty professionalSpecialty = professionalSpecialtiesRepository.findByValue(specialtiy);
            if(professionalSpecialty != null){
                professionalSpecialtyList.add(professionalSpecialty.getId());
            }
        }

        return professionalSpecialtyList;
    }

    public List<ProfessionalSpecialtyDTO> createProfessionalSpecialties(List<CreateSpecialtiesDTO> createSpecialtiesDTO){
        List<Specialty> professionalSpecialtyList = new ArrayList<>();

        for(CreateSpecialtiesDTO professionalSpeciality : createSpecialtiesDTO) {
            String value = professionalSpeciality.value();
            String label = professionalSpeciality.label();

            Specialty specialty = new Specialty();
            specialty.setValue(value);
            specialty.setLabel(label);

            professionalSpecialtiesRepository.save(specialty);
            professionalSpecialtyList.add(specialty);
        }

        return professionalSpecialtyList.stream()
                .map(speciality ->
                        new ProfessionalSpecialtyDTO(speciality.getValue(), speciality.getLabel()))
                .collect(Collectors.toList());
    }

    public List<ProfessionalSpecialtyDTO> extractProfessionalSpecialties(){
        List<Specialty> professionalSpecialties = professionalSpecialtiesRepository.findAll();
        return professionalSpecialties.stream()
                .map(specialty ->
                        new ProfessionalSpecialtyDTO(specialty.getLabel(), specialty.getValue()))
                .collect(Collectors.toList());
    }

}
