import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hold {

    private Traener traener;
    private String ugeDag;
    private int tid;
    private List<KonkurrenceSvoemmer> deltagere = new ArrayList<>();
    private List<Hold> holdListe = new ArrayList<>();
    private static final String FILE_PATH_HOLD = "hold.txt";
    private String holdnavn;
    private static final int MAX_DELTAGERE_HOLD = 4;
    //changes 9/12
    public static void main(String[] args) {
    }

    public Hold() {
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

    public void printHoldListe() {
        FileUtil.loadHoldFromFile(FILE_PATH_HOLD,holdListe);
        holdListe.forEach(System.out::println);
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

    public String getUgeDag() {
        return ugeDag;
    }

    public String getFILE_PATH_HOLD() {
        return FILE_PATH_HOLD;
    }

    public int getTid() {
        return tid;
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

    public void opretHold() {
        Scanner scanner = new Scanner(System.in);

        FileUtil.loadTraenerFromFile(FILE_PATH_HOLD,traener.getTraenerListe());

        System.out.println("Indtast navn på hold der skal oprettes: ");
        String navn = scanner.nextLine();


        System.out.println("Træner der skal tilføjes til hold:");
        for (int i = 0; i < traener.getTraenerListe().size(); i++) {
            System.out.println((i + 1) + ". " + traener.getTraenerListe().get(i).getNavn());
        }


        System.out.println("Vælg træner (indtast nummer): ");
        int traenerValg = Integer.parseInt(scanner.nextLine());
        Traener valgtTraener = traener.getTraenerListe().get(traenerValg - 1);


        String ugeDag = "";
        while (ugeDag.isEmpty()) {
            System.out.println("Vælg en dag: ");
            System.out.println("1. Mandag");
            System.out.println("2. Tirsdag");
            System.out.println("3. Onsdag");
            System.out.println("4. Torsdag");
            System.out.println("5. Fredag");
            System.out.println("6. Lørdag");
            System.out.println("7. Søndag");

            int valg = scanner.nextInt();
            scanner.nextLine();

            switch (valg) {
                case 1: ugeDag = "Mandag"; break;
                case 2: ugeDag = "Tirsdag"; break;
                case 3: ugeDag = "Onsdag"; break;
                case 4: ugeDag = "Torsdag"; break;
                case 5: ugeDag = "Fredag"; break;
                case 6: ugeDag = "Lørdag"; break;
                case 7: ugeDag = "Søndag"; break;
                default: System.out.println("Ugyldigt valg, prøv igen."); break;
            }
        }

        // Angiv tidspunkt for træning
        int tid = 0;
        while (tid <= 0) {
            System.out.println("Angiv tidspunkt holdet skal træne (f.eks. 10 for kl. 10:00): ");
            try {
                tid = Integer.parseInt(scanner.nextLine());
                if (tid <= 0) {
                    System.out.println("Tidspunkt skal være et positivt tal.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ugyldigt input, prøv igen.");
            }
        }

        Hold nytHold = new Hold(navn, valgtTraener, ugeDag, tid);
        holdListe.add(nytHold);
        FileUtil.saveHold(FILE_PATH_HOLD,holdListe);
        System.out.println("Hold oprettet: " + nytHold.getHoldnavn());
    }

    public void opdaterHold (String holdnavn, String nyUgeDag,int nyTid, Traener nyTraener){
            for (Hold hold : getHoldListe()) {
                if (hold.getHoldnavn().equals(holdnavn)) {
                    hold.setUgeDag(nyUgeDag);
                    hold.setTid(nyTid);
                    hold.setTraener(nyTraener);
                    FileUtil.saveHold(FILE_PATH_HOLD,holdListe);
                    System.out.println("Holdet er blevet opdateret og gemt.");
                    return;
                }
            }
            System.out.println("Hold med navnet " + holdnavn + " blev ikke fundet.");
        }

    public void fjernHold (String holdnavn){
            Hold holdToRemove = null;
            for (Hold hold : holdListe) {
                if (hold.getHoldnavn().equals(holdnavn)) {
                    holdToRemove = hold;
                    break;
                }
            }
            if (holdToRemove != null) {
                holdListe.remove(holdToRemove);
                FileUtil.saveHold(FILE_PATH_HOLD,holdListe);
                System.out.println("Hold fjernet og gemt til fil.");
            } else {
                System.out.println("Hold med navnet " + holdnavn + " blev ikke fundet.");
            }
        }

        @Override
        public String toString () {
            return "Hold: " + holdnavn + ", Træner: " + (traener != null ? traener.getNavn() : "Ingen træner") + ", Ugedag: " + ugeDag + ", Tid: " + tid + ", Deltagere: " + deltagere;
        }
    }

