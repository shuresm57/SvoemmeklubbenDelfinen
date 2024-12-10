import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Staevne {


    private String dato;
    private String staevneNavn;

    private static final String FILE_PATH_STAEVNE = "staevneResultater.txt";
    private List<String> tider = new ArrayList<>();
    private List<String> deltagere = new ArrayList<>();
    private List<String> kssvoemmer = new ArrayList<>();
    private List<Staevne> staevneListe = new ArrayList<>();
    private List<String> valgteDiscipliner = new ArrayList<>();
    MedlemManagement mm = new MedlemManagement();
    //changes 9/12
    public static void main(String[] args) {
        Staevne s = new Staevne();
        s.opretStaevne();
    }


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

    public void tilfoejKSframedlemtilKs() {
        mm.loadMedlemmerFromFile();
        for (Medlem ks : mm.getMedlemmer()) {
            if (ks instanceof KonkurrenceSvoemmer) {
                kssvoemmer.add(ks.getMedlemsnummer() + " " + ks.getNavn());
            }
        }
    }

    public List<String> getDeltagere() {
        return deltagere;
    }

    public List<String> getKssvoemmer() {
        return kssvoemmer;
    }

    public void opretStaevne() {
        Scanner scanner = new Scanner(System.in);
        tilfoejKSframedlemtilKs();
        Discipliner d = new Discipliner();
        List<String> staevneDeltagere = new ArrayList<>();

        while (true) {
            System.out.println("Indtast navn og meter på stævnet, der skal oprettes: ");
            String navn = scanner.nextLine();

            System.out.println("Indtast dato for afholdelse af stævne (dd-MM-yyyy):");
            String dato = scanner.nextLine();

            while (true) {
                System.out.println("Vælg disciplin (indtast nummer). Tast 0 for at afslutte:");
                for (int i = 0; i < d.getDiscipliner().size(); i++) {
                    System.out.println((i + 1) + ". " + d.getDiscipliner().get(i));
                }

                int disciplinValg = scanner.nextInt();
                scanner.nextLine(); // Forbrug newline karakteren

                if (disciplinValg == 0) break; // Afslut valg af discipliner

                if (disciplinValg > 0 && disciplinValg <= d.getDiscipliner().size()) {
                    String valgtDisciplin = d.getDiscipliner().get(disciplinValg - 1);
                    if (!valgteDiscipliner.contains(valgtDisciplin)) {
                        valgteDiscipliner.add(valgtDisciplin);
                        System.out.println("Disciplin tilføjet: " + valgtDisciplin);
                        break; // Gå videre til næste punkt i while-løkken efter valg af disciplin
                    } else {
                        System.out.println("Disciplinen er allerede valgt.");
                        break; // Gå videre til næste punkt i while-løkken selv hvis disciplinen er allerede valgt
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
                if (svoemmerValg == 0) break; // Afslut valg af discipliner

                if (svoemmerValg > 0 && svoemmerValg <= kssvoemmer.size()) {
                    String valgtSvoemmer = kssvoemmer.get(svoemmerValg - 1);
                    if (!deltagere.contains(valgtSvoemmer)) {
                        deltagere.add(valgtSvoemmer);
                        System.out.println("Svømmer tilføjet: " + valgtSvoemmer);
                        System.out.println("Tilføj tid til svømmer: ");
                        double tid = scanner.nextDouble();
                        //indsæt catch for hvis man kommer til at skrive et punktum
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
            System.out.println("Stævne oprettet: " + nytStaevne.getStaevneNavn());
            break;
        }
    }

    @Override
    public String toString() {
        return staevneNavn;

    }
}



