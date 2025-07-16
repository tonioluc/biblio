package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.JourFerie;

@Service
public class JourFerieService {
    @Autowired
    private web.bibliotheque.repository.JourFerieRepository jourFerieRepository;

    public List<JourFerie> getAll() {
        return jourFerieRepository.findAll();
    }

    public LocalDate getDateActif(LocalDate dateRetourPrevue){
        LocalDate result = dateRetourPrevue;
        for (JourFerie jourFerie : this.getAll()) {
            if (jourFerie.getDateJour().equals(result)) {
                result.plusDays(1);
                System.out.println("Tafiditra ny jour férié: " + result);
            }
        }
        return result;
    }
}
