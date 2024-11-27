import java.util.ArrayList;
import java.util.List;

public class Hold {

    private Traener traener;
    private String ugeDag;
    private int tid;
    private List<KonkurrenceSvoemmer> deltagere = new ArrayList<>();
    private static List<Hold> holdListe = new ArrayList<>();
    private String holdnavn;

    public Hold(String holdnavn, Traener traener, String ugeDag, int tid) {
        this.holdnavn = holdnavn;
        this.traener = traener;
        this.ugeDag = ugeDag;
        this.tid = tid;
        holdListe.add(this);
    }

    public static List<Hold> getHoldListe() {
        return holdListe;
    }

    public String getHoldnavn() {
        return holdnavn;
    }

    public void tilfoejTilHold(KonkurrenceSvoemmer svoemmer) {
        deltagere.add(svoemmer);
        svoemmer.setHold(this);
        svoemmer.setTraener(this.traener);
    }

    public void getAlleHold() {
        System.out.println(holdListe.toString());
    }

    public Traener getTraener() {
        return traener;
    }

    public String getUgeDag() {
        return ugeDag;
    }

    public int getTid() {
        return tid;
    }

    @Override
    public String toString() {
        return "Hold: " + holdnavn + ", Træner: " + (traener != null ? traener.getNavn() : "Ingen træner") + ", Ugedag: " + ugeDag + ", Tid: " + tid + ", Deltagere: " + deltagere;
    }
}