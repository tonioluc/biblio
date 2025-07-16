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

    public List<JourFerie> getAllOrderByDate() {
        return jourFerieRepository.findAllByOrderByDateJourAsc();
    }

    public LocalDate getDateActif(LocalDate dateRetourPrevue){
        LocalDate result = dateRetourPrevue;
        List<JourFerie> joursFeries = this.getAllOrderByDate();
        System.out.println("nombre jour féries: " + joursFeries.size());
        for (JourFerie jourFerie : joursFeries) {
            if (jourFerie.getDateJour().equals(result)) {
                result = result.plusDays(1);
                System.out.println("Tafiditra ny jour férié: " + result);
            }
        }
        return result;
    }
}
