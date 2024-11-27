import java.util.ArrayList;
import java.util.List;

public class Kontingent {

    private static final double UNGDOMS_KONTINGENT = 1000.0;
    private static final double SENIOR_KONTINGENT = 1600.0;
    private static final double SENIOR_RABAT = 0.75;
    private static final double PASSIVT_KONTINGENT = 500.0;
    private List<Medlem> medlemmer = new ArrayList<Medlem>();
    private Medlem medlem;

    public List<Medlem> getMedlemmer() {
        return medlemmer;
    }

    public double beregnKontingent() {
        for (Medlem medlem : medlemmer) {

            if (medlem instanceof PassivtMedlem) {
                return PASSIVT_KONTINGENT;
            } else if (medlem instanceof KonkurrenceSvoemmer && medlem.erJunior() || medlem instanceof Motionist && medlem.erJunior()) {
                return UNGDOMS_KONTINGENT;
            } else if (medlem instanceof KonkurrenceSvoemmer && medlem.erSenior() || medlem instanceof Motionist && medlem.erSenior()) {
                return SENIOR_KONTINGENT * SENIOR_RABAT;
            } else if (medlem instanceof KonkurrenceSvoemmer || medlem instanceof Motionist){
                return SENIOR_KONTINGENT;
            }
        }
        return 0;
    }

    public double totalKontingent()
    {
        double total = 0.0;
        for (Medlem medlem : medlemmer) {
            total += beregnKontingent();
        }
        return total;
    }

    public static List<Medlem> medlemmerIRestance(List<Medlem> medlemmer) {
        List<Medlem> iRestance = new ArrayList<>();
        for (Medlem medlem : medlemmer) {
            if (medlem.erIRestance()) {
                iRestance.add(medlem);
            }
        }
        return iRestance;
    }
}
