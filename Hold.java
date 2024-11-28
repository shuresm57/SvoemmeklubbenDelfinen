import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hold {

    private Traener traener;
    private String ugeDag;
    private int tid;
    private List<KonkurrenceSvoemmer> deltagere = new ArrayList<>();
    private static List<Hold> holdListe = new ArrayList<>();
    private static final String FILE_PATH = "hold.txt";
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

    public void tilfoejTilHold(Medlem medlem) {
        if (medlem instanceof KonkurrenceSvoemmer) {
            KonkurrenceSvoemmer svoemmer = (KonkurrenceSvoemmer) medlem;
            if (!deltagere.contains(svoemmer)) {
                deltagere.add(svoemmer);
                System.out.println(svoemmer.getNavn() + " er tilføjet til holdet " + holdnavn);
                saveToFile();
            } else {
                System.out.println(svoemmer.getNavn() + " er allerede tilknyttet holdet " + holdnavn);
            }
        } else {
            System.out.println("Kun konkurrencesvømmere kan tilføjes til et hold."); //Kun KS kan være på hold, pls virk
        }
    }

    // Gem hele listen til fil
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Hold hold : holdListe) {
                String traenerNavn = (hold.traener != null) ? hold.traener.getNavn() : "Ingen træner";
                writer.write(hold.holdnavn + "," + traenerNavn + "," + hold.ugeDag + "," + hold.tid);
                writer.newLine();
                for (KonkurrenceSvoemmer deltagere : hold.deltagere) {
                    writer.write(deltagere.getNavn());
                    writer.newLine();
                }
            }
            System.out.println("Hold gemt til fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af hold til fil: " + e.getMessage());
        }
    }

    // Indlæs hold fra fil
    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            Hold currentHold = null;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Traener traener = null;
                    for (Traener t : traener.getTraenerListe()) {
                        if (t.getNavn().equals(data[1])) {
                            traener = t;
                            break;
                        }
                    }
                    currentHold = new Hold(data[0], traener, data[2], Integer.parseInt(data[3]));
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