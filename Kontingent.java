import java.util.*;

public class Kontingent {

    private             static final double         UNGDOMS_KONTINGENT      = 1000.0;
    private             static final double         SENIOR_KONTINGENT       = 1600.0;
    private             static final double         SENIOR_RABAT            = 0.75;
    private             static final double         PASSIVT_KONTINGENT      = 500.0;
    private             List<Medlem>                medlemmer               = new ArrayList<>();
    private             List<Medlem>                medlemmerIRestance      = new ArrayList<>();
    private             Medlem                      medlem;
    private             PersonPersistens            pp                      = new PersonPersistens();


    public Kontingent() {}

    public void loadMedlemmerFraFil(List<Medlem> medlemmerFraFil) {
        medlemmer.addAll(medlemmerFraFil);
    }

    public double totalKontingent() {
        double total = 0.0;
        for (Medlem medlem : medlemmer) {
            if (medlem instanceof PassivtMedlem) {
                total += PASSIVT_KONTINGENT;
            } else if (medlem instanceof KonkurrenceSvoemmer && medlem.erJunior() || medlem instanceof Motionist && medlem.erJunior()) {
                total += UNGDOMS_KONTINGENT;
            } else if (medlem instanceof KonkurrenceSvoemmer && medlem.erSenior() || medlem instanceof Motionist && medlem.erSenior()) {
                total += SENIOR_KONTINGENT * SENIOR_RABAT;
            } else if (medlem instanceof KonkurrenceSvoemmer || medlem instanceof Motionist) {
                total += SENIOR_KONTINGENT;
            }
        }
        return total;
    }

    public void tilfoejMedlemTilRestance(Medlem m){
        medlemmerIRestance.add(m);
    }

    public void medlemmerIRestanceListen() {
        for(Medlem m : medlemmer){
            if(!m.harBetalt()){
                System.out.println(m.getMedlemsnummer() + " - " + m.getNavn());
            }
        }
    }
}
