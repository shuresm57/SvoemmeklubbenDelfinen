
import java.io.*;
import java.io.IOException;
import java.util.*;

//commit 5/12

public class Kontingent {

    private             static final double         UNGDOMS_KONTINGENT      = 1000.0;
    private             static final double         SENIOR_KONTINGENT       = 1600.0;
    private             static final double         SENIOR_RABAT            = 0.75;
    private             static final double         PASSIVT_KONTINGENT      = 500.0;
    private             List<Medlem>                medlemmerIRestance      = new ArrayList<>();
    private             PersonPersistens            pp                      = new PersonPersistens();
    private             Scanner                     scanner                 = new Scanner(System.in);
    private             static final String         FILE_PATH               = "medlemmerIRestance.txt";

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
        } else if (medlemToUpdate instanceof Motionist mo) {
            mo.setBetalt(false);
            medlemmerIRestance.add(mo);
        } else if (medlemToUpdate instanceof PassivtMedlem pm) {
            pm.setBetalt(false);
            medlemmerIRestance.add(pm);
        }
        saveMedlemToFile();
    }

    public void saveMedlemToFile() {
        List<String> eksisterendeMedlemsnumre = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linje;
            while ((linje = reader.readLine()) != null) {
                String[] data = linje.split(","); // Forvent at medlemsnummer er det første felt
                if (data.length > 0) {
                    eksisterendeMedlemsnumre.add(data[0]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Filen blev ikke fundet.");
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af fil: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Medlem medlem : medlemmerIRestance) {
                if (eksisterendeMedlemsnumre.contains(medlem.getMedlemsnummer())) {
                    System.out.println("Medlem findes allerede i restance listen.");
                }else{
                    writer.write(medlem.getMedlemsnummer() + "," + medlem.getMedlemstype() + "," + medlem.getNavn() +
                            "," + medlem.getFoedselsdato() + "," + medlem.getTelefon() + "," + medlem.getEmail());
                    writer.newLine();
                    System.out.println(medlem.getMedlemsnummer() + " - " + medlem.getNavn() + " er blevet gemt.");
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved gemning til fil: " + e.getMessage());
        }
    }


    public void loadMedlemmerFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) { // Sørg for at linjen har det rigtige antal datafelter
                    String medlemsnummer = data[0];
                    String medlemstype = data[1];
                    String navn = data[2];
                    String foedselsdato = data[3];
                    String telefon = data[4];
                    String email = data[5];
                    Medlem medlem = null;
                    switch (medlemstype) {
                        case "KonkurrenceSvoemmer":
                            medlem = new KonkurrenceSvoemmer(medlemsnummer, navn, foedselsdato, telefon, email);
                            break;
                        case "Motionist":
                            medlem = new Motionist(medlemsnummer, navn, foedselsdato, telefon, email);
                            break;
                        case "PassivtMedlem":
                            medlem = new PassivtMedlem(medlemsnummer, navn, foedselsdato, telefon, email);
                            break;
                    }
                    if (medlem != null) {
                        medlemmerIRestance.add(medlem);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved indlæsning fra fil: " + e.getMessage());
        }
    }

    public void sletMedlemRestance() {
        Scanner scanner = new Scanner(System.in);

        String medlemsnummer = scanner.nextLine();
        File inputfil = new File(FILE_PATH);
        File tempfil = new File("tempfil.txt");

        boolean medlemFundet = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempfil))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().contains(medlemsnummer.toLowerCase())) {
                    medlemFundet = true; // Marker medlem som fundet
                    continue; // Spring denne linje over (dvs. slet medlemmet)
                }
                writer.write(currentLine + System.lineSeparator());
            }

        } catch (IOException e) {
            System.out.println("Der opstod en fejl under læsning eller skrivning af filen: " + e.getMessage());
            return;
        }

        if (medlemFundet) {
            // Erstat den gamle fil med den nye
            if (inputfil.delete() && tempfil.renameTo(new File(FILE_PATH))) {
                System.out.println("Medlemmet med medlemsnummer " + medlemsnummer + " er blevet slettet.");
            } else {
                System.out.println("Kunne ikke opdatere medlemsfilen.");
            }
        } else {
            tempfil.delete(); // Slet tempfilen, da der ikke var nogen ændringer
            System.out.println("Medlemsnummeret blev ikke fundet.");
        }
    }

    public void getMedlemmerIRestance(){
        medlemmerIRestance.forEach(System.out::println);
    }
}

