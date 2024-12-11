import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hold {


    private                 String                          holdnavn;
    private                 String                          ugeDag;
    private                 int                             tid;

    private                 List<KonkurrenceSvoemmer>       deltagere       = new ArrayList<>();
    private                 List<Hold>                      holdListe       = new ArrayList<>();
    private                 List<KonkurrenceSvoemmer>       KsvoemmerListe = new ArrayList<>();

    private                 Traener                         traener;
    private                 KonkurrenceSvoemmer             ks;

    private static final    String                          FILE_PATH_HOLD  = "hold.txt";

    public void iniKS(){
        MedlemManagement mm = new MedlemManagement();
        mm.loadMedlemmerFromFile();
        for(Medlem medlem : mm.getMedlemmer()){
            if (medlem instanceof KonkurrenceSvoemmer){
                KsvoemmerListe.add((KonkurrenceSvoemmer)medlem);
            }
        }
    }


    public Hold() {
        this.traener = new Traener();
    }

    public Hold(String holdnavn, Traener traener, String ugeDag, int tid, List<KonkurrenceSvoemmer> deltagere) {
        this.holdnavn = holdnavn;
        this.traener = traener;
        this.ugeDag = ugeDag;
        this.tid = tid;
        this.deltagere = deltagere;
        holdListe.add(this);
    }

    public Hold(String holdnavn) {
        this.holdnavn = holdnavn;
    }

    public List<Hold> getHoldListe() {
        return holdListe;
    }

    public void printHoldListe() {
        FileUtil.loadHoldFromFile(FILE_PATH_HOLD,holdListe);
        for(Hold hold : holdListe){
            System.out.println(hold.getHoldnavn());
        }
    }

    public String getHoldnavn() {
        return holdnavn;
    }

    public List<KonkurrenceSvoemmer> getDeltagere() {
        return deltagere;
    }

    public void addDeltager(KonkurrenceSvoemmer svoemmer) {
        deltagere.add(svoemmer);
    }
    public void removeDeltager(KonkurrenceSvoemmer svoemmer) {
        deltagere.remove(svoemmer);
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

    public void loadHoldFromFile(){
        FileUtil.loadHoldFromFile(FILE_PATH_HOLD,holdListe);
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
        iniKS();

        FileUtil.loadTraenerFromFile(traener.getFilePathTraener(),traener.getTraenerListe());

        System.out.println("Indtast navn på hold der skal oprettes: ");
        String navn = scanner.nextLine();


        System.out.println("Træner der skal tilføjes til hold:");
        for (int i = 0; i < traener.getTraenerListe().size(); i++) {
            System.out.println((i + 1) + ". " + traener.getTraenerListe().get(i).getNavn());
        }


        System.out.println("Vælg træner (indtast nummer): ");
        int traenerValg = Integer.parseInt(scanner.nextLine());
        Traener valgtTraener = traener.getTraenerListe().get(traenerValg - 1);

        List<KonkurrenceSvoemmer> svoemmere = new ArrayList<>();

        while(true){
            System.out.println("Vælg deltager (indtast nummer). Tast 0 for at afslutte. ");
            for(int i = 0; i < KsvoemmerListe.size(); i++){
                System.out.println((i + 1) + ". " + KsvoemmerListe.get(i).getNavn());
            }
            int deltagerValg = scanner.nextInt();
            if(deltagerValg == 0) break;

            if(deltagerValg > 0 && deltagerValg <= KsvoemmerListe.size()){
                KonkurrenceSvoemmer valgtDeltager = KsvoemmerListe.get(deltagerValg - 1);
                if(!svoemmere.contains(valgtDeltager)){
                    svoemmere.add(valgtDeltager);
                    System.out.println(valgtDeltager.getNavn() + " tilføjet til hold");
                }
            }
        }

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

        Hold nytHold = new Hold(navn, valgtTraener, ugeDag, tid, svoemmere);
        holdListe.add(nytHold);
        FileUtil.saveHold(FILE_PATH_HOLD,holdListe);
        System.out.println("Hold oprettet: " + nytHold.getHoldnavn());
    }



    @Override
    public String toString () {
            return "Hold: " + holdnavn + ", Træner: " + (traener != null ? traener.getNavn() : "Ingen træner") + ", Ugedag: " + ugeDag + ", Tid: " + tid + ", Deltagere: " + deltagere;
        }
    }

