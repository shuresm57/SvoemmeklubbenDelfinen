import java.io.*;
import java.util.*;

public class Resultat {

    private String disciplin;
    private double tid;
    private String dato;
    private KonkurrenceSvoemmer svoemmer;
    private Traener traener;
    private Resultat resultat;
    private List<String> discipliner;
    private List<Resultat> resultater = new ArrayList<>();
    private HashMap<KonkurrenceSvoemmer,List<Resultat>> resultatMap;
    private MedlemManagement mm = new MedlemManagement();
    private static final String FILE_PATH_RESULTAT = "traeningsResultater.txt";

    public static void main(String[] args) {
        Resultat r = new Resultat();
        //r.traeningsResultater();
        //r.visTop5Resultater();
        r.printResultater();
    }

    public Resultat(KonkurrenceSvoemmer svoemmer, String disciplin, double tid, String dato) {
        this.svoemmer = svoemmer;
        this.disciplin = disciplin;
        this.tid = tid;
        this.dato = dato;
    }

    public Resultat(KonkurrenceSvoemmer svoemmer, String medlemsnummer, String disciplin, double tid, String dato) {
        this.svoemmer = svoemmer;
        this.disciplin = disciplin;
        this.tid = tid;
        this.dato = dato;

    }

    public Resultat() {
        resultatMap = new HashMap<>();
    }

    public HashMap<KonkurrenceSvoemmer,List<Resultat>> getResultater() {
        return resultatMap;
    }

    public void printResultater() {
        FileUtil.laesResultaterFraFil(FILE_PATH_RESULTAT,resultatMap);
        for (Map.Entry<KonkurrenceSvoemmer, List<Resultat>> entry : resultatMap.entrySet()) {
            KonkurrenceSvoemmer svoemmer = entry.getKey(); // Svømmeren (KonkurrenceSvoemmer)
            List<Resultat> resultater = entry.getValue(); // Resultaterne for svømmeren

            System.out.println("Navn: " + svoemmer.getNavn() + ", Medlemsnr: " + svoemmer.getMedlemsnummer());

            for (Resultat resultat : resultater) {
                System.out.println("Disciplin: " + resultat.getDisciplin() + ", Resultat: " + resultat.getTid());
            }

            System.out.println(); // Ny linje efter hver svømmer
        }
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

    public String getDato() {
        return dato;
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
            resultatMap.put(svoemmer, resultater);
            FileUtil.saveResultat(FILE_PATH_RESULTAT, resu);
            System.out.println("Resultat oprettet: " + resu);
            break;
        }
    }

    public void visTop5Resultater() {
        mm.loadMedlemmerFromFile();
        FileUtil.laesResultaterFraFil(FILE_PATH_RESULTAT,resultatMap);
        List<String> discipliner = Arrays.asList("Butterfly", "Crawl", "Rygcrawl", "Brystsvømning");
        for (String disciplin : discipliner) {
            List<Resultat> disciplinResultater = new ArrayList<>();

            for (Map.Entry<KonkurrenceSvoemmer, List<Resultat>> entry : resultatMap.entrySet()) {
                List<Resultat> resultater = entry.getValue();
                Resultat resu = new Resultat();
                if (resu.getDisciplin().equalsIgnoreCase(disciplin)) {
                    disciplinResultater.add(resu);
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
