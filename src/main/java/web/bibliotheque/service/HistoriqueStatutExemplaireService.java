package web.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.bibliotheque.model.HistoriqueStatutExemplaire;

import web.bibliotheque.repository.HistoriqueStatutExemplaireRepository;

@Service
public class HistoriqueStatutExemplaireService {
    @Autowired
    private HistoriqueStatutExemplaireRepository historiqueStatutExemplaireRepository;

    public void create(HistoriqueStatutExemplaire historiqueStatutExemplaire){
        historiqueStatutExemplaireRepository.save(historiqueStatutExemplaire);
    }
    public List<HistoriqueStatutExemplaire> getDernierStatutAvantDate(Long idExemplaire,LocalDate date){
        return historiqueStatutExemplaireRepository.findDernierStatutAvantDate(idExemplaire, date);
    }
}
