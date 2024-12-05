import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Staevne {

    private List<KonkurrenceSvoemmer> konksvom = new ArrayList<>();
    private int tid;
    private String staevne;
    private int placering;
    private Resultat resultat;

    public Staevne(List<KonkurrenceSvoemmer> konksvom, Resultat resultat) {
        this.konksvom = konksvom;
        this.resultat = resultat;
    }

    public void KonkurrenceResultat(String staevne, int placering, int tid) {
        this.staevne = staevne;
        this.placering = placering;
        this.tid = tid;
    }

    @Override
    public String toString() {
        return "Stævne: " + staevne + ", Placering: " + placering + ", Tid: " + tid;
    }

     /*public static Staevne fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length == 4) {
            String medlemsnummer = parts[0];
            String staevne = parts[1];
            int placering = Integer.parseInt(parts[2]);
            int tid = Integer.parseInt(parts[3]);
            return new Staevne(medlemsnummer, staevne, placering, tid);
        }
        throw new IllegalArgumentException("Ugyldigt dataformat: " + data);
    }*/

    public void registerResultater() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Indtast medlemsnummer for svømmeren: ");
        String medlemsnummer = scanner.nextLine();
    }
}
