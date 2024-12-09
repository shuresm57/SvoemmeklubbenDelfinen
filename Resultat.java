import java.io.*;
import java.util.*;

public class Resultat {

    private String disciplin;
    private double tid;
    private String dato;
    private KonkurrenceSvoemmer svoemmer;
    private Traener traener;
    private List<String> discipliner;
    private List<Resultat> resultater;
    private MedlemManagement mm = new MedlemManagement();

    private static final String filePath = "traeningsResultater.txt";

    public static void main(String[] args) {
        Resultat r = new Resultat();
        //r.traeningsResultater();
        r.visTop5Resultater();
    }

    public Resultat(KonkurrenceSvoemmer svoemmer, String disciplin, double tid, String dato) {
        this.svoemmer = svoemmer;
        this.disciplin = disciplin;
        this.tid = tid;
        this.dato = dato;
    }

    public Resultat() {
        resultater = new ArrayList<>();
    }

    public List<Resultat> getResultater() {
        return resultater;
    }

    public List<String> getDiscipliner() {
        return discipliner;
    }

    public void setDiscipliner(List<String> discipliner) {
        this.discipliner = discipliner;
    }

    public String getDisciplin() {
        return disciplin;
    }

    public double getTid() {
        return tid;
    }

    public KonkurrenceSvoemmer getSvoemmer() {
        return svoemmer;
    }

    public void traeningsResultater() {
        discipliner = new ArrayList<>();
        discipliner.add("Butterfly");
        discipliner.add("Crawl");
        discipliner.add("Rygcrawl");
        discipliner.add("Brystsvømning");
        Scanner scanner = new Scanner(System.in);
        MedlemManagement ps = new MedlemManagement();
        ps.loadMedlemmerFromFile();

        while (true) {
            System.out.println("Indtast medlemsnummer på svømmer for at oprette træningsresultater:");
            String medlemsnummer = scanner.nextLine();

            Medlem medlemToUpdate = null;
            for (Medlem medlem : ps.getMedlemmer()) {
                if (medlem.getMedlemsnummer().equalsIgnoreCase(medlemsnummer)) {
                    medlemToUpdate = medlem;
                    break;  // Stop med at søge, når medlemmet er fundet
                }
            }

            if (medlemToUpdate == null) {
                System.out.println("Medlem med medlemsnummer " + medlemsnummer + " blev ikke fundet.");
                continue;  // Fortsæt til næste iteration af løkken, hvis medlemsnummer ikke blev fundet
            }

            System.out.println("Medlem fundet: " + medlemToUpdate.getNavn());

            System.out.println("Indtast dato for afholdelse af træning (dd-MM-yyyy):");
            String dato = scanner.nextLine();

            System.out.println("Vælg disciplin:");
            for (int i = 0; i < getDiscipliner().size(); i++) {
                System.out.println((i + 1) + ". " + getDiscipliner().get(i));
            }

            System.out.println("Vælg disciplin, indtast nummer:");
            int disciplinValg = Integer.parseInt(scanner.nextLine());
            String valgtDisciplin = getDiscipliner().get(disciplinValg - 1);

            System.out.println("Tid for disciplin i sekunder:");
            double tid = Double.parseDouble(scanner.nextLine());

            Resultat resu = new Resultat((KonkurrenceSvoemmer) medlemToUpdate, valgtDisciplin, tid, dato);

            resultater.add(resu);
            gemResultatTilFil(resu);
            System.out.println("Resultat oprettet: " + resu);
            break;
        }
    }

    public void gemResultatTilFil(Resultat resultat) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(resultat.svoemmer.getMedlemsnummer() + "," + resultat.getDisciplin() + "," +
                    resultat.getTid() + "," + resultat.dato);
            writer.newLine();  // Nyt resultat på en ny linje
            System.out.println("Resultat gemt: " + resultat.getDisciplin() + " - " + resultat.getTid());
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af resultat til fil: " + e.getMessage());
        }
    }

    public void laesResultaterFraFil() {
        resultater = new ArrayList<>();
        MedlemManagement mm = new MedlemManagement();
        mm.loadMedlemmerFromFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String medlemsnummer = data[0];
                    String disciplin = data[1];
                    double tid = Double.parseDouble(data[2]);
                    String dato = data[3];

                    // Find svømmer ud fra medlemsnummer
                    KonkurrenceSvoemmer svoemmer = mm.findKonkurrenceSvoemmerByMedlemsnummer(medlemsnummer);

                    // Hvis svømmeren ikke findes, skal du måske håndtere det
                    if (svoemmer != null) {
                        Resultat resultat = new Resultat(svoemmer, disciplin, tid, dato);
                        resultater.add(resultat);
                    } else {
                        System.out.println("Svømmer med medlemsnummer " + medlemsnummer + " ikke fundet.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af resultater fra fil: " + e.getMessage());
        }
    }


    public void visTop5Resultater() {
        mm.loadMedlemmerFromFile();
        laesResultaterFraFil();

        List<String> discipliner = Arrays.asList("Butterfly", "Crawl", "Rygcrawl", "Brystsvømning");

        for (String disciplin : discipliner) {
            List<Resultat> disciplinResultater = new ArrayList<>();

            for (Resultat resultat : resultater) {
                if (resultat.getDisciplin().equalsIgnoreCase(disciplin)) {
                    disciplinResultater.add(resultat);
                }
            }

            disciplinResultater.sort(Comparator.comparingDouble(Resultat::getTid));

            System.out.println("Top 5 resultater for " + disciplin);
            for (int i = 0; i < Math.min(5, disciplinResultater.size()); i++) {
                Resultat resu = disciplinResultater.get(i);
                System.out.println((i + 1) + ". " + resu.getSvoemmer().getNavn() + " - Tid: " + resu.getTid() + " sekunder");
            }
        }
    }


    @Override
    public String toString() {
        return "Disciplin: " + disciplin + ", Tid: " + tid + ", Dato: " + dato;
    }
}
