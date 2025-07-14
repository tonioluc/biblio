package web.bibliotheque.dto;

import web.bibliotheque.model.Pret;

public class PretProlongerDto {
    Pret pret;
    boolean prolonger;
    public Pret getPret() {
        return pret;
    }
    public void setPret(Pret pret) {
        this.pret = pret;
    }
    public boolean isProlonger() {
        return prolonger;
    }
    public void setProlonger(boolean prolonger) {
        this.prolonger = prolonger;
    }
    
}
