import java.time.LocalDate;
import java.util.*;

//commit 5/12

public class Kontingent {

    private             static final double         UNGDOMS_KONTINGENT      = 1000.0;
    private             static final double         SENIOR_KONTINGENT       = 1600.0;
    private             static final double         SENIOR_RABAT            = 0.75;
    private             static final double         PASSIVT_KONTINGENT      = 500.0;
    private             List<Medlem>                medlemmerIRestance      = new ArrayList<>();
    private             MedlemManagement            mm                      = new MedlemManagement();
    private             Scanner                     scanner                 = new Scanner(System.in);
    private             static final String         FILE_PATH_RESTANCE      = "medlemmerIRestance.txt";

    public Kontingent() {}

    public static void main(String[] args) {
        Kontingent k = new Kontingent();
        System.out.println(k.totalKontingent());
    }

    public double totalKontingent() {
        mm.loadMedlemmerFromFile();
        double total = 0.0;

        for (Medlem medlem : mm.getMedlemmer()) {
            if (medlem instanceof PassivtMedlem) {
                total += PASSIVT_KONTINGENT;
            } else if (medlem.erJunior()) {
                total += UNGDOMS_KONTINGENT;
            } else if (medlem.erSenior()) {
                total += SENIOR_KONTINGENT * SENIOR_RABAT;
            } else {
                total += SENIOR_KONTINGENT;
            }
        }
        return total;
    }

    public void tilfoejMedlemTilRestance() {
        String medlemsnummer = scanner.nextLine();
        boolean findesAllerede = false;
        Medlem medlemToUpdate = null;
        for (Medlem m : mm.getMedlemmer()) {
            if (m.getMedlemsnummer().equalsIgnoreCase(medlemsnummer)) {
                medlemToUpdate = m;
                break;
            }
            else{
                break;
            }
        } if (medlemToUpdate instanceof KonkurrenceSvoemmer ks) {
            ks.setBetalt(false);
            medlemmerIRestance.add(ks);
        } else if (medlemToUpdate instanceof Motionist mo) {
            mo.setBetalt(false);
            medlemmerIRestance.add(mo);
        } else if (medlemToUpdate instanceof PassivtMedlem pm) {
            pm.setBetalt(false);
            medlemmerIRestance.add(pm);
        }
        else if (findesAllerede) {
        }
        FileUtil.saveMedlemmer(FILE_PATH_RESTANCE,medlemmerIRestance,false);
    }

    public void getMedlemmerIRestance(){
        if(!medlemmerIRestance.isEmpty()){
            System.out.println("Medlemmer der er i restance:\n");
            medlemmerIRestance.forEach(System.out::println);
        }
        else {
            System.out.println("Ingen medlemmer er i restance.\n");
        }

    }

    public void runKontingent() {
        FileUtil.loadMedlemmerFromFile(FILE_PATH_RESTANCE,6,medlemmerIRestance,null);
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Vælg hvad du vil se: ");
            System.out.println("1. Forventet total kontingent for " + LocalDate.now().getYear());
            System.out.println("2. Medlemmer i restance");
            System.out.println("3. Sæt et medlem i restance");
            System.out.println("4. Slet et medlem fra restance");
            System.out.println("5. Vis medlemmer");
            System.out.println("0. Tilbage til hovedmenu");


            int valg = scanner.nextInt();
            scanner.nextLine();


            switch (valg) {
                case 1:
                    System.out.println("Totalkontinget for " + LocalDate.now().getYear() + ": ");
                    System.out.println(totalKontingent());
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
                    FileUtil.loadMedlemmerFromFile(FILE_PATH_RESTANCE,6,medlemmerIRestance,null);
                    break;
                case 5:
                    System.out.println("Viser alle medlemmer\n");
                    mm.getMedlemmer().forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Ugyldigt valg.");
                    break;
            }
        }
    }
}

