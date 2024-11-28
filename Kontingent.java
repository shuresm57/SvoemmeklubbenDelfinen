import java.util.ArrayList;
import java.util.List;

public class Kontingent {

    private static final double UNGDOMS_KONTINGENT = 1000.0;
    private static final double SENIOR_KONTINGENT = 1600.0;
    private static final double SENIOR_RABAT = 0.75;
    private static final double PASSIVT_KONTINGENT = 500.0;
    private List<Medlem> medlemmer = new ArrayList<Medlem>();
    private Medlem medlem;
    private PersonPersistens pp = new PersonPersistens();


    public Kontingent() {
        pp.loadMedlemmerFromFile();
        for(Medlem medlem1 : medlemmer) {
            medlemmer.add(medlem1);
        }
    }

    public List<Medlem> getMedlemmer() {
        return medlemmer;
    }

    public double beregnKontingent() {
        double kontingent = 0;
        for (Medlem medlem : medlemmer) {
            if (medlem instanceof PassivtMedlem)
            {
                kontingent = PASSIVT_KONTINGENT;
            } else if (medlem instanceof KonkurrenceSvoemmer && medlem.erJunior() || medlem instanceof Motionist && medlem.erJunior()) {
                kontingent = UNGDOMS_KONTINGENT;
            } else if (medlem instanceof KonkurrenceSvoemmer && medlem.erSenior() || medlem instanceof Motionist && medlem.erSenior()) {
                kontingent = SENIOR_KONTINGENT * SENIOR_RABAT;
            } else if (medlem instanceof KonkurrenceSvoemmer || medlem instanceof Motionist) {
                kontingent = SENIOR_KONTINGENT;
            }
        }
        return kontingent;
    }

    public double totalKontingent(){
        double total = 0.0;

        for(Medlem medlem : medlemmer){
            medlemmer.add(medlem);
           total += beregnKontingent();
        }
        return total;
        }

    public void medlemmerIRestance() {
        List<Medlem> iRestance = new ArrayList<>();
        for (Medlem medlem : medlemmer) {
            if (medlem.erIRestance()) {
                iRestance.add(medlem);
                System.out.println(iRestance);
            }
        }
    }

}
