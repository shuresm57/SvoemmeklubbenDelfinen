import java.util.ArrayList;
import java.util.List;

public class Kontingent {

    private static final double UNGDOMS_KONTINGENT = 1000.0;
    private static final double SENIOR_KONTINGENT = 1600.0;
    private static final double SENIOR_RABAT = 0.75;
    private static final double PASSIVT_KONTINGENT = 500.0;
    private List<Medlem> medlemmer = new ArrayList<Medlem>();

    public static double beregnKontingent(Medlem medlem) {
        if (medlem.erPassiv()) {
            return PASSIVT_KONTINGENT;
        } else if (medlem.erJunior()) {
            return UNGDOMS_KONTINGENT;
        } else if (medlem.erSenior()) {
            return SENIOR_KONTINGENT * SENIOR_RABAT;
        } else {
            return SENIOR_KONTINGENT;
        }
    }

    public double totalKontingent() {
        double total = 0.0;
        for (Medlem medlem : medlemmer) {
            total += beregnKontingent(medlem);
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