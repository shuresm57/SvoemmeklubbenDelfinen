import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hold {

    private Traener traener;
    private String ugeDag;
    private int tid;
    private List<KonkurrenceSvoemmer> deltagere = new ArrayList<>();
    private List<Hold> holdListe = new ArrayList<>();
    private static final String FILE_PATH = "hold.txt";
    private String holdnavn;
    private static final int MAX_DELTAGERE_HOLD = 4;



    public static void main(String[]args){
            PersonPersistens persistens = new PersonPersistens();
            Traener t = new Traener();
            Traener traener = new Traener("Niko", 28, "dasda", "3055699");
            persistens.loadMedlemmerFromFile();

            //Traener traener = new Traener("Niko", 20, "30569972", "dmoiias@gmail.com");
            Hold h = new Hold();

       // traener.opretTraener();

       h.opretHold();

            // Tilføj automatisk konkurrencesvømmere til hold
            //h.tilfoejMedlemTilHoldFraArrayList(persistens.getMedlemmerafKlassen());

            //h.getHoldListe(); // Udskriv holdlisten for kontrol

    }

    public Hold(){
        this.traener = new Traener();
    }

    public Hold(String holdnavn, Traener traener, String ugeDag, int tid) {
        this.holdnavn = holdnavn;
        this.traener = traener;
        this.ugeDag = ugeDag;
        this.tid = tid;
        holdListe.add(this); // Fjern dette, hvis det ikke er nødvendigt
    }

    public List<Hold> getHoldListe() {
        System.out.println(holdListe);
        return holdListe;
    }

    public String getHoldnavn() {
        return holdnavn;
    }

    public List<KonkurrenceSvoemmer> getDeltagere() {
        return deltagere;
    }

    public Traener getTraener() {
        return traener;
    }

    public void setHoldnavn(String holdnavn) {
        this.holdnavn = holdnavn;
    }

    public void setUgeDag(String ugeDag) {
        this.ugeDag = ugeDag;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setTraener(Traener traener) {
        this.traener = traener;
    }


    public void tilfoejMedlemTilHoldFraArrayList(List<Medlem> medlemsListe) {
        for (Medlem medlem : medlemsListe) {
            if (medlem instanceof KonkurrenceSvoemmer) {
                KonkurrenceSvoemmer svoemmer = (KonkurrenceSvoemmer) medlem;

                boolean added = false; // Indikator for, om svømmeren er tilføjet
                for (Hold hold : holdListe) {
                    if (hold.deltagere.size() < MAX_DELTAGERE_HOLD && !hold.deltagere.contains(svoemmer)) {
                        hold.deltagere.add(svoemmer);
                        System.out.println(svoemmer.getNavn() + " er tilføjet til holdet " + hold.holdnavn);
                        added = true;
                        break;
                    }
                }

                // Hvis ingen eksisterende hold har plads, opret et nyt hold
                if (!added) {
                    String nytHoldNavn = "Hold " + (holdListe.size() + 1);
                    Hold nytHold = new Hold(nytHoldNavn, traener, ugeDag, tid); // Skab nyt hold
                    holdListe.add(nytHold); // Tilføj det nye hold til listen
                    System.out.println("Nyt hold oprettet: " + nytHoldNavn);
                    nytHold.deltagere.add(svoemmer); // Tilføj svømmeren til det nye hold
                    System.out.println(svoemmer.getNavn() + " er tilføjet til det nye hold " + nytHoldNavn);
                }
            } else {
                System.out.println("Medlemmet " + medlem.getNavn() + " er ikke en konkurrencesvømmer og blev ikke tilføjet.");
            }
        }

        // Gem ændringerne til fil
        saveToFile();
    }


    public void opretHold() {
        Scanner scanner = new Scanner(System.in);

        traener.loadFromFile(); // Sørger for, at trænerlisten er initialiseret korrekt

        while (true) {
            System.out.println("Indtast navn på hold der skal oprettes: ");
            String navn = scanner.nextLine();

            // Print eksisterende trænere
            System.out.println("Træner der skal tilføjes til hold:");
            for (int i = 0; i < traener.getTraenerListe().size(); i++) {
                System.out.println((i + 1) + ". " + traener.getTraenerListe().get(i).getNavn());
            }

            // Vælg træner
            System.out.println("Vælg træner (indtast nummer): ");
            int traenerValg = Integer.parseInt(scanner.nextLine());
            Traener valgtTraener = traener.getTraenerListe().get(traenerValg - 1);

            System.out.println("Indtast ugedag, hvor holdet skal træne: ");
            String ugeDag = scanner.nextLine();

            System.out.println("Indtast tidspunkt holdet skal træne: ");
            int tid = Integer.parseInt(scanner.nextLine());

            // Brug den valgte træner
            Hold nytHold = new Hold(navn, valgtTraener, ugeDag, tid);

            // Tilføj hold til listen
            holdListe.add(nytHold);
            saveToFile();
            System.out.println("Hold oprettet: " + nytHold.getHoldnavn());
            break;
        }
    }

    // Gem hele listen til fil
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Hold hold : holdListe) {
                String traenerNavn = (hold.traener != null) ? hold.traener.getNavn() : "Ingen træner";
                writer.write(hold.holdnavn + "," + traenerNavn + "," + hold.ugeDag + "," + hold.tid);
                writer.newLine();
                List<String> eksisterendeNavne = new ArrayList<>();
                for (KonkurrenceSvoemmer deltagere : hold.deltagere) {
                    if(!eksisterendeNavne.contains(deltagere.getNavn())){
                        writer.newLine();
                        writer.write(deltagere.getNavn());
                        eksisterendeNavne.add(deltagere.getNavn());
                    }
                }
            }
            System.out.println("Hold gemt til fil.");
        }
        catch (IOException e) {
            System.out.println("Fejl ved skrivning af hold til fil: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            Hold currentHold = null;

            // Sørg for, at traener er initialiseret korrekt
            if (traener == null) {
                traener = new Traener();
                traener.loadFromFile(); // Indlæs trænere
            }

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Traener matchedTraener = null;

                    // Find træneren i trænerlisten
                    for (Traener t : traener.getTraenerListe()) {
                        if (t.getNavn().equals(data[1])) {
                            matchedTraener = t;
                            break;
                        }
                    }

                    // Opret nyt hold med matched træner
                    currentHold = new Hold(data[0], matchedTraener, data[2], Integer.parseInt(data[3]));
                    holdListe.add(currentHold);
                } else if (currentHold != null) {
                    // Tilføj deltagere til holdet
                    KonkurrenceSvoemmer deltagere = new KonkurrenceSvoemmer(line, "", "", "", "");
                    currentHold.deltagere.add(deltagere);
                }
            }
            System.out.println("Hold indlæst fra fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved indlæsning af hold fra fil: " + e.getMessage());
        }
    }



    // Opdater hold og skriv til fil
    public void opdaterHold(String holdnavn, String nyUgeDag, int nyTid, Traener nyTraener) {
        for (Hold hold : getHoldListe()) {
            if (hold.getHoldnavn().equals(holdnavn)) {
                hold.setUgeDag(nyUgeDag);
                hold.setTid(nyTid);
                hold.setTraener(nyTraener);
                saveToFile(); // Gem hele listen til fil efter opdatering
                System.out.println("Holdet er blevet opdateret og gemt.");
                return;
            }
        }
        System.out.println("Hold med navnet " + holdnavn + " blev ikke fundet.");
    }

    // Fjern hold og opdater filen
    public void fjernHold(String holdnavn) {
        Hold holdToRemove = null;
        for (Hold hold : holdListe) {
            if (hold.getHoldnavn().equals(holdnavn)) {
                holdToRemove = hold;
                break;
            }
        }
        if (holdToRemove != null) {
            holdListe.remove(holdToRemove);
            saveToFile(); // Opdater filen efter fjernelse
            System.out.println("Hold fjernet og gemt til fil.");
        } else {
            System.out.println("Hold med navnet " + holdnavn + " blev ikke fundet.");
        }
    }

    @Override
    public String toString() {
        return "Hold: " + holdnavn + ", Træner: " + (traener != null ? traener.getNavn() : "Ingen træner") + ", Ugedag: " + ugeDag + ", Tid: " + tid + ", Deltagere: " + deltagere;
    }
}