import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Hold {

    private Traener traener;
    private String ugeDag;
    private int tid;
    private List<KonkurrenceSvoemmer> deltagere = new ArrayList<>();
    private List<Hold> hold = new ArrayList<>();
    private String holdnavn;

    public Hold(String holdnavn, Traener traener, String ugeDag, int tid) {
        this.holdnavn = holdnavn;
        this.traener = traener;
        this.ugeDag = ugeDag;
        this.tid = tid;
        hold.add(this);
    }

public void holdListe(){
    System.out.println(deltagere);
}

    public void holdListe(){
        System.out.println(deltagere);
    }

    public void tilfoejTilHold(KonkurrenceSvoemmer svoemmer){
        deltagere.add(svoemmer);
        svoemmer.setHold(this);
        svoemmer.setTraener(this.traener);
    }

    public void getAlleHold(){
        System.out.println(hold.toString());
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


    public String toString() { // virker ikke ordentligt i det, at der mangler deltagere m.m.
        return "Hold: " + holdnavn + ", Træner: " + traener + ", Deltagere: " + deltagere;
    }
}
