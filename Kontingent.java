import java.time.LocalDate;
import java.util.*;

//commit 5/12

public class Kontingent {

    private static final            double              UNGDOMS_KONTINGENT          = 1000.0;
    private static final            double              SENIOR_KONTINGENT           = 1600.0;
    private static final            double              SENIOR_RABAT                = 0.75;
    private static final            double              PASSIVT_KONTINGENT          = 500.0;
    private static final            String              FILE_PATH_RESTANCE          = "medlemmerIRestance.txt";

    private                         List<Medlem>        medlemmerIRestance          = new ArrayList<>();
    private                         MedlemManagement    mm                          = new MedlemManagement();
    private                         Scanner             scanner                     = new Scanner(System.in);

    private                         boolean             alreadyLoaded               = false;

    public Kontingent() {}

    public void alreadyLoaded(){
        if (!alreadyLoaded) {
            mm.loadMedlemmerFromFile();
            alreadyLoaded = true;
        }
    }

    public double beregnKontingent(Medlem medlem) {
        if (medlem instanceof PassivtMedlem) {
            return PASSIVT_KONTINGENT;
        } else if (medlem.erUnder18()) {
            return UNGDOMS_KONTINGENT;
        } else if (medlem.erOver65()) {
            return SENIOR_KONTINGENT * SENIOR_RABAT;
        } else {
            return SENIOR_KONTINGENT;
        }
    }

    public void totalKontingent() {
        alreadyLoaded();
        double total = 0.0;
        for (Medlem medlem : mm.getMedlemmer()) {
            total += beregnKontingent(medlem);
        }
        System.out.println("Total kontingent for " + LocalDate.now().getYear() + " " + total);
    }

    public void kontingentListe() {
        alreadyLoaded();
        for(Medlem medlem : mm.getMedlemmer()) {
            double kontingent = beregnKontingent(medlem);
            System.out.println("Navn: " + medlem.getNavn() + " " + "Kontingent: " + kontingent +
                    " Har betalt kontingent for " + LocalDate.now().getYear() + ": " + medlem.harBetalt());
        }
        totalKontingent();
    }

    public void tilfoejMedlemTilRestance() {
        alreadyLoaded();
        String medlemsnummer = scanner.nextLine();
        Medlem medlemToUpdate = null;
        for (Medlem m : mm.getMedlemmer()) {
            if (m.getMedlemsnummer().equalsIgnoreCase(medlemsnummer)) {
                medlemToUpdate = m;
                System.out.println("Medlem fundet: " + medlemToUpdate);

                if (medlemToUpdate instanceof KonkurrenceSvoemmer ks) {
                    ks.setBetalt(false);
                    medlemmerIRestance.add(ks);
                } else if (medlemToUpdate instanceof Motionist mo) {
                    mo.setBetalt(false);
                    medlemmerIRestance.add(mo);
                } else if (medlemToUpdate instanceof PassivtMedlem pm) {
                    pm.setBetalt(false);
                    medlemmerIRestance.add(pm);
                }

                FileUtil.saveMedlemmer(FILE_PATH_RESTANCE, medlemmerIRestance, false);
                System.out.println("Medlem tilføjet til restance: " + medlemToUpdate);
                break; // Stop loopet, når medlemmet er fundet og opdateret
            }
        }
        if (medlemToUpdate == null) {
            System.out.println("Medlem ikke fundet: " + medlemsnummer);
        }
    }

    public void getMedlemmerIRestance () {
            alreadyLoaded();
            if (!medlemmerIRestance.isEmpty()) {
                System.out.println("Medlemmer der er i restance:\n");
                medlemmerIRestance.forEach(System.out::println);
            } else {
                System.out.println("Ingen medlemmer er i restance.\n");
            }

        }

    public void runKontingent(){
            FileUtil.loadMedlemmerFromFile(FILE_PATH_RESTANCE, 6, medlemmerIRestance, null);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Forventet total kontingent for " + LocalDate.now().getYear());
                System.out.println("2. Medlemmer i restance");
                System.out.println("3. Sæt et medlem i restance");
                System.out.println("4. Slet et medlem fra restance");
                System.out.println("5. Vis medlemmer og betalingsstatus");
                System.out.println("9. Log ud");
                System.out.println("0. Afslut");


                int valg = scanner.nextInt();
                scanner.nextLine();


                switch (valg) {
                    case 1:
                        totalKontingent();
                        break;
                    case 2:
                        getMedlemmerIRestance();
                        break;
                    case 3:
                        System.out.println("Indtast medlemsnummer på medlemmet, der skal sættes i restance: ");
                        tilfoejMedlemTilRestance();
                        break;
                    case 4:
                        FileUtil.sletMedlem(FILE_PATH_RESTANCE);
                        break;
                    case 5:
                        System.out.println("Viser alle medlemmer\n");
                        kontingentListe();
                        break;
                    case 9:
                        System.out.println("Logger ud...");
                        Start st = new Start();
                        st.run();
                    case 0:
                        System.exit(0);
                    default:
                        System.out.println("Ugyldigt valg.");
                        break;
                }
            }
        }
    }


