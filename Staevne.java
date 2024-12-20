import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Staevne {


    private                 String              dato;
    private                 String              staevneNavn;

    private                 List<String>        tider                   = new ArrayList<>();
    private                 List<String>        deltagere               = new ArrayList<>();
    private                 List<String>        kssvoemmer              = new ArrayList<>();
    private                 List<Staevne>       staevneListe            = new ArrayList<>();
    private                 List<String>        valgteDiscipliner       = new ArrayList<>();

    private                 MedlemManagement    mm                      = new MedlemManagement();

    private static final    String              FILE_PATH_STAEVNE       = "staevneResultater.txt";

    public Staevne() {
    }

    public Staevne(String navn, String dato, List<String> valgteDiscipliner, List<String> deltagere, List<String> tider) {
        this.staevneNavn = navn;
        this.dato = dato;
        this.valgteDiscipliner = valgteDiscipliner;
        this.deltagere = deltagere;
        this.tider = tider;
    }

    public Staevne(String navn, String dato, List<String> valgteDiscipliner, List<String> tider) {
        this.staevneNavn = navn;
        this.dato = dato;
        this.valgteDiscipliner = valgteDiscipliner;
        this.tider = tider;
    }

    public String getNavn(){
        return staevneNavn;
    }

    public List<String> getTider() {
        return tider;
    }

    public String getDato() {
        return dato;
    }

    public String getStaevneNavn() {
        return staevneNavn;
    }

    public List<String> getValgteDiscipliner() {
        return valgteDiscipliner;
    }

    public List<String> getDeltagere() {
        return deltagere;
    }

    public List<String> getKssvoemmer() {
        return kssvoemmer;
    }

    public void printStaevneListe() {
        FileUtil.loadStaevne(FILE_PATH_STAEVNE,staevneListe);
        for (Staevne staevne : staevneListe) {
            System.out.println("Navn: " + staevne.getStaevneNavn());
            System.out.println("Dato: " + staevne.getDato());
            System.out.println("Disciplin: " + staevne.getValgteDiscipliner());
            System.out.println("Tider: " + staevne.getTider());
        }
    }

    public void tilfoejKSframedlemtilKs() {
        mm.loadMedlemmerFromFile();
        for (Medlem ks : mm.getMedlemmer()) {
            if (ks instanceof KonkurrenceSvoemmer) {
                kssvoemmer.add(ks.getMedlemsnummer() + " " + ks.getNavn());
            }
        }
    }

    public void opretStaevne() {
        Scanner scanner = new Scanner(System.in);
        tilfoejKSframedlemtilKs();
        Discipliner d = new Discipliner();
        List<String> staevneDeltagere = new ArrayList<>();

        while (true) {
            System.out.println("Indtast byen på stævnet, der skal oprettes: ");
            String navn = scanner.nextLine();

            System.out.println("Indtast dato for afholdelse af stævne (dd-MM-yyyy):");
            String dato = scanner.nextLine();

            while (true) {
                System.out.println("Vælg disciplin (indtast nummer):");
                for (int i = 0; i < d.getDiscipliner().size(); i++) {
                    System.out.println((i + 1) + ". " + d.getDiscipliner().get(i));
                }

                int disciplinValg = scanner.nextInt();
                scanner.nextLine();

                if (disciplinValg == 0) break;

                if (disciplinValg > 0 && disciplinValg <= d.getDiscipliner().size()) {
                    String valgtDisciplin = d.getDiscipliner().get(disciplinValg - 1);
                    if (!valgteDiscipliner.contains(valgtDisciplin)) {
                        System.out.println("Indtast længden på disciplinen i meter: ");
                        String laengde = scanner.nextLine();
                        valgteDiscipliner.add(valgtDisciplin + " - " + laengde);
                        System.out.println("Disciplin tilføjet: " + valgtDisciplin + " - " + laengde);
                        break;
                    } else {
                        System.out.println("Disciplinen er allerede valgt.");
                        break;
                    }
                } else {
                    System.out.println("Ugyldigt valg. Prøv igen.");
                }

            }
            while (true) {
                System.out.println("Vælg svømmer (indtast nummer). Tast 0 for at afslutte:");
                for (int i = 0; i < kssvoemmer.size(); i++) {
                    System.out.println((i + 1) + ". " + kssvoemmer.get(i));
                }

                int svoemmerValg = scanner.nextInt();
                if (svoemmerValg == 0) break;

                if (svoemmerValg > 0 && svoemmerValg <= kssvoemmer.size()) {
                    String valgtSvoemmer = kssvoemmer.get(svoemmerValg - 1);
                    if (!deltagere.contains(valgtSvoemmer)) {
                        deltagere.add(valgtSvoemmer);
                        System.out.println("Svømmer tilføjet: " + valgtSvoemmer);
                        System.out.println("Tilføj tid til svømmer: ");
                        double tid = scanner.nextDouble();
                        tider.add(("\n" +"Svømmer: " + valgtSvoemmer + " tid for svømmer: " + String.valueOf(tid)) + " sekunder."+"\n");
                    } else {
                        System.out.println("Svømmeren er allerede valgt.");
                    }
                } else {
                    System.out.println("Ugyldigt valg. Prøv igen.");
                }
            }
            Staevne nytStaevne = new Staevne(navn, dato, valgteDiscipliner, tider);
            staevneListe.add(nytStaevne);
            FileUtil.saveStaevne(FILE_PATH_STAEVNE, staevneListe);
            System.out.println("Stævne oprettet: " + navn);
            break;
        }
    }

    @Override
    public String toString() {
        return staevneNavn;

    }
}



