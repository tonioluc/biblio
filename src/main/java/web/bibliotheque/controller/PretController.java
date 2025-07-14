package web.bibliotheque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import web.bibliotheque.model.Prolongement;
import web.bibliotheque.service.ProlongementService;

@Controller
public class PretController {
    @Autowired
    private ProlongementService prolongementService;

    public void loadPretProlonger(Model model){
        List<Prolongement> prolongements = prolongementService.getAll();
        model.addAttribute("prolongements", prolongements);
    }

    @GetMapping("/liste-pret-prolonger")
    public String listePretsProlonger(Model model){
        loadPretProlonger(model);
        return "liste-prets-prolonges";
    }
    
}
