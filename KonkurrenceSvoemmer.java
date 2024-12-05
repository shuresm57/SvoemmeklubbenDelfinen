import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KonkurrenceSvoemmer extends Medlem {
    private List<Resultat> resultater = new ArrayList<>();

    public KonkurrenceSvoemmer(String medlemsnummer, String navn, String foedselsdato, String telefon, String email, String medlemsdato) {
        super(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
    }

    public KonkurrenceSvoemmer(String medlemsnummer, String navn, String foedselsdato, String telefon, String email) {
        super(medlemsnummer, navn, foedselsdato, telefon, email);
    }

    public KonkurrenceSvoemmer(){

    }

    /*
    // Metode til at registrere et nyt resultat
    public void registrerResultat(String disciplin, double tid, Date dato) {
        for (Resultat resultat : resultater) {
            if (resultat.getDisciplin().equalsIgnoreCase(disciplin)) {
                if (tid < resultat.getTid()) { // Opdater kun, hvis tiden er bedre
                    resultat = new Resultat(this, disciplin, tid, dato);
                    System.out.println("Ny personlig rekord i " + disciplin + ": " + tid + " den " + dato);
                } else {
                    System.out.println("Tid i " + disciplin + " er ikke bedre end den nuværende rekord.");
                }
                return; // Afslut, da disciplinen allerede blev opdateret
            }
        }
        // Hvis disciplinen ikke findes, tilføj et nyt resultat
        resultater.add(new Resultat(this, disciplin, tid, dato));
        System.out.println("Første resultat registreret i " + disciplin + ": " + tid + " den " + dato);
    }


     */





    // Vis alle resultater
    public void visResultater() {
        System.out.println("Bedste resultater for " + getNavn() + ":");
        for (Resultat resultat : resultater) {
            System.out.println(resultat);
        }
    }

    @Override
    public String getMedlemstype() {
        return "KonkurrenceSvoemmer";
    }
}