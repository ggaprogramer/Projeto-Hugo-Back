package projeto.hugo.terapia.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.profile.dto.ProfileInterestsDTO;
import projeto.hugo.terapia.profile.model.ProfileInterests;
import projeto.hugo.terapia.profile.repository.ProfileInterestsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileInterestsService {

    private final ProfileInterestsRepository profileInterestsRepository;

    public List<ProfileInterests> getInterestsProfile(List<String> interests){
        List<ProfileInterests> profileInterestsList = new ArrayList<>();

        for(String interest : interests){
            ProfileInterests profileInterest = profileInterestsRepository.findByValue(interest);
            if(profileInterestsRepository.findByValue(interest) != null){
                profileInterestsList.add(profileInterest);
            }
        }

        return profileInterestsList;
    }

    public List<ProfileInterestsDTO> createProfileInterests(List<ProfileInterestsDTO> createProfileInterestsDTO){
        List<ProfileInterests> profileInterestsList = new ArrayList<>();

        for(ProfileInterestsDTO profileInterest : createProfileInterestsDTO){
            String value = profileInterest.value();
            String label = profileInterest.label();

            ProfileInterests profileInterests = new ProfileInterests();
            profileInterests.setValue(value);
            profileInterests.setLabel(label);

            profileInterestsRepository.save(profileInterests);
            profileInterestsList.add(profileInterests);
        }

        return profileInterestsList.stream()
                .map(interest ->
                        new ProfileInterestsDTO(interest.getValue(), interest.getLabel()))
                .collect(Collectors.toList());
    }

    public List<ProfileInterestsDTO> extractProfileInterests(){
        List<ProfileInterests> profileInterests = profileInterestsRepository.findAll();
        return profileInterests
                .stream()
                .map(interest ->
                        new ProfileInterestsDTO(interest.getValue(), interest.getLabel()))
                        .collect(Collectors.toList());
    }

}
