package web.bibliotheque.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.dto.PretProlongerDto;
import web.bibliotheque.model.Pret;
import web.bibliotheque.repository.ProlongementRepostirory;

@Service
public class ProlongementService {
    @Autowired
    private ProlongementRepostirory prolongementRepostirory;
    public boolean pretProlonger(Pret pret){
        return prolongementRepostirory.findByPret(pret).isPresent();
    }

    public List<PretProlongerDto> convertPret(List<Pret> prets){
        List<PretProlongerDto> result = new ArrayList<>();
        for (Pret pret : prets) {
            PretProlongerDto pretProlongerDto = new PretProlongerDto();
            pretProlongerDto.setPret(pret);
            pretProlongerDto.setProlonger(pretProlonger(pret));
            result.add(pretProlongerDto);
        }
        return result;
    }
}
