
import java.io.*;
import java.io.IOException;
import java.util.*;

public class Kontingent {

    private             static final double         UNGDOMS_KONTINGENT      = 1000.0;
    private             static final double         SENIOR_KONTINGENT       = 1600.0;
    private             static final double         SENIOR_RABAT            = 0.75;
    private             static final double         PASSIVT_KONTINGENT      = 500.0;
    private             List<Medlem>                medlemmerIRestance      = new ArrayList<>();
    private             PersonPersistens            pp                      = new PersonPersistens();
    private             Scanner                     scanner                 = new Scanner(System.in);

    public Kontingent() {}

    public static void main(String[] args) {
        Kontingent k = new Kontingent();
        System.out.println(k.totalKontingent());
    }

    public double totalKontingent() {
        pp.loadMedlemmerFromFile();
        double total = 0.0;

        for (Medlem medlem : pp.getMedlemmer()) {
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
        pp.loadMedlemmerFromFile();

        String medlemsnummer = scanner.nextLine();

        Medlem medlemToUpdate = null;
        for (Medlem m : pp.getMedlemmer()) {
            if (m.getMedlemsnummer().equalsIgnoreCase(medlemsnummer)) {
                medlemToUpdate = m;
                break;
            }
        }
        if (medlemToUpdate == null) {
            System.out.println("Medlem med medlemsnummer " + medlemsnummer + " blev ikke fundet.");
        } else if (medlemToUpdate instanceof KonkurrenceSvoemmer ks) {
            ks.setBetalt(false);
            medlemmerIRestance.add(ks);
            System.out.println(medlemToUpdate.getMedlemsnummer() + " " + medlemToUpdate.getNavn() + " er blevet tilføjet til restancelisten.");
        } else if (medlemToUpdate instanceof Motionist mo) {
            mo.setBetalt(false);
            medlemmerIRestance.add(mo);
            System.out.println(medlemToUpdate.getMedlemsnummer() + " " + medlemToUpdate.getNavn() + " er blevet tilføjet til restancelisten.");
        } else if (medlemToUpdate instanceof PassivtMedlem pm) {
            pm.setBetalt(false);
            medlemmerIRestance.add(pm);
            System.out.println(medlemToUpdate.getMedlemsnummer() + " - " + medlemToUpdate.getNavn() + " er blevet tilføjet til restancelisten.");
        }

        // Print aktuelle medlemmer i restance
        System.out.println("Nuværende medlemmer i restance:");
        medlemmerIRestance.forEach(m -> System.out.println("Restance medlem: " + m.getMedlemsnummer()));

        // Gem medlemmer i restance til fil
        saveMedlemToFile();
    }

    public void saveMedlemToFile() {
        String FILE_PATH = "medlemmerIRestance.txt"; // Definer filstien

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Medlem medlem : medlemmerIRestance) {
                writer.write(medlem.getMedlemsnummer() + "," + medlem.getMedlemstype() + "," + medlem.getNavn() +
                        "," + medlem.getFoedselsdato() + "," + medlem.getTelefon() + "," + medlem.getEmail() + "," + medlem.getMedlemsdato());
                writer.newLine();
            }

            System.out.println("Medlemmerne er blevet gemt.");
        } catch (IOException e) {
            System.out.println("Fejl ved gemning til fil: " + e.getMessage());
        }
    }


    public void loadMedlemmerFromFile() {
        String FILE_PATH = "medlemmerIRestance.txt"; // Definer filstien
        medlemmerIRestance = new ArrayList<>(); // Sørg for at starte med en tom liste

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) { // Sørg for at linjen har det rigtige antal datafelter
                    String medlemsnummer = data[0];
                    String medlemstype = data[1];
                    String navn = data[2];
                    String foedselsdato = data[3];
                    String telefon = data[4];
                    String email = data[5];
                    String medlemsdato = data[6];

                    Medlem medlem = null;
                    switch (medlemstype) {
                        case "KonkurrenceSvoemmer":
                            medlem = new KonkurrenceSvoemmer(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                            break;
                        case "Motionist":
                            medlem = new Motionist(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                            break;
                        case "PassivtMedlem":
                            medlem = new PassivtMedlem(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                            break;
                    }

                    if (medlem != null) {
                        medlemmerIRestance.add(medlem);
                    }
                }
            }
            System.out.println("Medlemmerne er blevet indlæst fra fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved indlæsning fra fil: " + e.getMessage());
        }
    }

    public void getMedlemmerIRestance(){
        medlemmerIRestance.forEach(System.out::println);
    }
}

