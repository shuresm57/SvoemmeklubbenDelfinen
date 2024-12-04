import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Resultat {
    private String disciplin;
    private double tid;
    private String dato;
    private KonkurrenceSvoemmer svoemmer;
    Traener traener;
    private List<String> Discipliner;
    private List<Resultat> resultater;

    private static final String filePath = "traeningsResultater.txt";

    public static void main(String[] args) {
        Resultat r = new Resultat();
        r.traeningsResultater();
    }


    public List<Resultat> getResultater() {
        return resultater;
    }

    public List<String> getDiscipliner() {
        return Discipliner;
    }

    public void setDiscipliner(List<String> Discipliner) {
        this.Discipliner = Discipliner;
    }

    public Resultat(KonkurrenceSvoemmer svoemmer, String disciplin, double tid, String dato) {
        this.svoemmer = svoemmer;
        this.disciplin = disciplin;
        this.tid = tid;
        this.dato = dato;
    }

    public Resultat() {

    }


    public void traeningsResultater() {
        Discipliner = new ArrayList<>();
        Discipliner.add("Butterfly");
        Discipliner.add("Crawl");
        Discipliner.add("Rygcrawl");
        Discipliner.add("Brystsvømning");
        Scanner scanner = new Scanner(System.in);
        PersonPersistens ps = new PersonPersistens();
        ps.loadMedlemmerFromFile();

        //resultat.loadFromFile(); // Sørger for, at trænerlisten er initialiseret korrekt
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

            // Fortsæt med resten af logikken, når medlemsnummeret er fundet
            // F.eks. indhent resultater, dato, disciplin osv.
            System.out.println("Medlem fundet: " + medlemToUpdate.getNavn());

            // ...




        System.out.println("Indtast dato for afholdelse af træning");
            String dato = scanner.nextLine();

            System.out.println("Vælg disciplin: ");
            for (int i = 0; i < getDiscipliner().size(); i++) {
                System.out.println((i + 1) + ". " + getDiscipliner().get(i));
            }

            System.out.println("Vælg disciplin, indtast nummmer: ");
            int disciplinValg = Integer.parseInt(scanner.nextLine());
            String valgtDisciplin = getDiscipliner().get(disciplinValg - 1);

            System.out.println("tid for disciplin i sekunder: ");
            double tid = Double.parseDouble(scanner.nextLine());


            // Brug den valgte træner
            Resultat resu = new Resultat(svoemmer, valgtDisciplin, tid, dato);
            // Tilføj hold til listen


            //gem til arraylist af resultater
            resultater.add(resu);
            gemResultatTilFil();
            System.out.println("Resultat oprettet: " + getResultater() );
            break;
        }
    }

    public void gemResultatTilFil() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Skriver resultatet i filen
            writer.write(svoemmer.getMedlemsnummer() + "," + disciplin + "," + tid + "," + dato);
            writer.newLine();  // Nyt resultat på en ny linje
            System.out.println("Resultat gemt: " + disciplin + " - " + tid);
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af resultat til fil: " + e.getMessage());
        }
    }

    public static void laesResultaterFraFil(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String medlemsnummer = data[0];
                    String disciplin = data[1];
                    double tid = Double.parseDouble(data[2]);
                    Date dato = new Date(data[3]);

                    // Her kan du oprette et nyt Resultat objekt og håndtere det som nødvendigt
                    System.out.println("Medlemsnummer: " + medlemsnummer + ", Disciplin: " + disciplin + ", Tid: " + tid + ", Dato: " + dato);
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af resultater fra fil: " + e.getMessage());
        }
    }


    public String getDisciplin() {
        return disciplin;
    }

    public double getTid() {
        return tid;
    }



    @Override
    public String toString() {
        return "Disciplin: " + disciplin + ", Tid: " + tid + ", Dato: " + dato;
    }
}


