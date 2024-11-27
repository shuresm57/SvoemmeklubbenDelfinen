import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonPersistens {

    private static final String FILE_PATH = "medlemmer.txt";  // Filstien til at gemme og læse medlemmer fra.
    private List<Medlem> medlemmer = new ArrayList<>();// Liste til at gemme medlemmer.
    private List<String> medlemsNumre = new ArrayList<>();

    public static void main(String[] args) {
        PersonPersistens persistens = new PersonPersistens();
        persistens.run();  // Kør programmet og lad brugeren vælge og oprette medlemmer.
    }

    public List<Medlem> getMedlemmerafKlassen() {
        return medlemmer;
    }

    public List<String> getMedlemmer() {
        return medlemsNumre;
    }

    // Metode til at generere medlemsnummer
    public String genererMedlemsnummer(String medlemstype) {
        String kode = "";
        if ("1".equalsIgnoreCase(medlemstype)) {
            kode = "KS";
        } else if ("2".equalsIgnoreCase(medlemstype)) {
            kode = "MN";
        } else if ("3".equalsIgnoreCase(medlemstype)) {
            kode = "PM";
        } else {
            throw new IllegalArgumentException("Ugyldig medlemstype: " + medlemstype);
        }

        // Bestem næste ledige nummer for den pågældende kode
        int naesteNummer = 1;
        for (Medlem medlem : medlemmer) {
            if (medlem.getMedlemsnummer().startsWith(kode)) {
                int eksisterendeNummer = Integer.parseInt(medlem.getMedlemsnummer().substring(2));
                if (eksisterendeNummer >= naesteNummer) {
                    naesteNummer = eksisterendeNummer + 1;
                    medlemsNumre.add(medlem.getMedlemsnummer());
                }
            }
        }

        // Sammensæt det nye medlemsnummer og returner det
        return kode + String.format("%03d", naesteNummer);
    }

    public void visMedlemmer() {
        System.out.println("Medlemsliste: ");
        for (Medlem medlem : medlemmer) {
            System.out.println(medlem);
        }
    }

    // Metode til at oprette og gemme et medlem
    public void opretMedlem() {
        Scanner scanner = new Scanner(System.in);

        String medlemstype = "";
        while (true) {
            System.out.println("Vælg medlemstype (1 = Konkurrencesvømmer, 2 = Motionist, 3 = Passivt medlem): ");
            medlemstype = scanner.nextLine().trim();

            // Tjek om input er gyldigt
            if ("1".equalsIgnoreCase(medlemstype) || "2".equalsIgnoreCase(medlemstype) || "3".equalsIgnoreCase(medlemstype)) {
                break;  // Udfør ud af løkken hvis input er gyldigt
            } else {
                System.out.println("Ugyldigt valg, prøv igen.");  // Hvis input er ugyldigt, vis en fejlmeddelelse
            }
        }

        // Generér medlemsnummer baseret på medlemstype
        String medlemsnummer = genererMedlemsnummer(medlemstype);
        // Bed brugeren om at indtaste oplysninger for medlemmet
        System.out.println("Indtast navn: ");
        String navn = scanner.nextLine();

        System.out.println("Indtast alder: ");
        int alder = scanner.nextInt();
        scanner.nextLine();  // Forbruger den sidste linjefeed.

        System.out.println("Indtast telefonnummer: ");
        String telefon = scanner.nextLine();

        System.out.println("Indtast email: ");
        String email = scanner.nextLine();

        // Opret medlemmet baseret på medlemstype
        Medlem medlem = null;
        if ("1".equalsIgnoreCase(medlemstype)) {
            medlem = new KonkurrenceSvoemmer(medlemsnummer, navn, alder, telefon, email);
        } else if ("2".equalsIgnoreCase(medlemstype)) {
            medlem = new Motionist(medlemsnummer, navn, alder, telefon, email);
        } else if ("3".equalsIgnoreCase(medlemstype)) {
            medlem = new PassivtMedlem(medlemsnummer, navn, alder, telefon, email);
        }

        // Tilføj medlemmet til listen
        if (medlem != null) {
            medlemmer.add(medlem);
            medlemsNumre.add(medlemsnummer);
            saveMedlemToFile();
            System.out.println("Medlem oprettet: " + medlem.getMedlemsnummer());
        } else {
            System.out.println("Ugyldig medlemstype valgt.");
        }
    }

    // Metode til at gemme medlemmer til fil
    public void saveMedlemToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Medlem medlem : medlemmer) {
                writer.write(medlem.getMedlemsnummer() + "," + medlem.getMedlemstype() + "," + medlem.getNavn() + "," + medlem.getAlder() + "," + medlem.getTelefon() + "," + medlem.getEmail());
                writer.newLine();
            }
            System.out.println("Medlemmerne er blevet gemt.");
        } catch (IOException e) {
            System.out.println("Fejl ved gemning til fil: " + e.getMessage());
        }
    }

    // Metode til at læse medlemmer fra fil
    public void loadMedlemmerFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linje;
            while ((linje = reader.readLine()) != null) {
                String[] medlemsData = linje.split(",");
                if (medlemsData.length == 6) {
                    String medlemsnummer = medlemsData[0];
                    String medlemstype = medlemsData[1];
                    String navn = medlemsData[2];
                    int alder = Integer.parseInt(medlemsData[3]);
                    String telefon = medlemsData[4];
                    String email = medlemsData[5];

                    Medlem medlem = null;
                    if ("KonkurrenceSvoemmer".equalsIgnoreCase(medlemstype)) {
                        medlem = new KonkurrenceSvoemmer(medlemsnummer, navn, alder, telefon, email);
                    } else if ("Motionist".equalsIgnoreCase(medlemstype)) {
                        medlem = new Motionist(medlemsnummer, navn, alder, telefon, email);
                    } else if ("PassivtMedlem".equalsIgnoreCase(medlemstype)) {
                        medlem = new PassivtMedlem(medlemsnummer, navn, alder, telefon, email);
                    }

                    if (medlem != null) {
                        medlemmer.add(medlem);
                        medlemsNumre.add(medlemsnummer);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning fra fil: " + e.getMessage());
        }
    }

    // Metode til at køre programmet
    public void run() {
        Scanner scanner = new Scanner(System.in);

        // Læs medlemmer fra fil ved programstart
        loadMedlemmerFromFile();

        // Giv brugeren mulighed for at oprette et medlem
        while (true) {
            System.out.println("\n1. Opret medlem");
            System.out.println("2. Gem medlemmer til fil");
            System.out.println("3. Ændre medlemsoplysninger");
            System.out.println("4. Vis medlemmer");
            System.out.println("0. Afslut");
            System.out.print("Vælg en mulighed: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Forbruger den sidste linjefeed.

            if (option == 1) {
                opretMedlem();
            } else if (option == 2) {
                saveMedlemToFile();
            } else if (option == 3) {
                opdaterMedlem();
            } else if (option == 4) {
                visMedlemmer();
            } else if (option == 0) {
                break;
            } else {
                System.out.println("Ugyldigt valg.");
            }
        }

        // Luk scanner for at undgå ressource lækage
    }


    // Metode til at opdatere et medlem
    public void opdaterMedlem() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Indtast medlemsnummer på medlemmet, der skal opdateres: ");
        String medlemsnummer = scanner.nextLine();

        // Find medlemmet baseret på medlemsnummer
        Medlem medlemToUpdate = null;
        for (Medlem medlem : medlemmer) {
            if (medlem.getMedlemsnummer().equalsIgnoreCase(medlemsnummer)) {
                medlemToUpdate = medlem;
                break;  // Stop med at søge, når medlemmet er fundet
            }
        }

        if (medlemToUpdate == null) {
            System.out.println("Medlem med medlemsnummer " + medlemsnummer + " blev ikke fundet.");
            return;  // Stop hvis medlemmet ikke findes
        }
        while (true) {
            System.out.println("Vælg hvad du vil opdatere: ");
            System.out.println("1. Navn");
            System.out.println("2. Alder");
            System.out.println("3. Telefon");
            System.out.println("4. Email");
            System.out.println("0. Tilbage");

            int valg = scanner.nextInt();
            scanner.nextLine();  // Forbruger den sidste linjefeed.


            switch (valg) {
                case 1:
                    System.out.println("Indtast nyt navn: ");
                    String nytNavn = scanner.nextLine();
                    medlemToUpdate.setNavn(nytNavn);
                    break;
                case 2:
                    System.out.println("Indtast ny alder: ");
                    int nyAlder = scanner.nextInt();
                    medlemToUpdate.setAlder(nyAlder);
                    break;
                case 3:
                    System.out.println("Indtast nyt telefonnummer: ");
                    String nyTelefon = scanner.nextLine();
                    medlemToUpdate.setTelefon(nyTelefon);
                    break;
                case 4:
                    System.out.println("Indtast ny email: ");
                    String nyEmail = scanner.nextLine();
                    medlemToUpdate.setEmail(nyEmail);
                    break;
                case 0:
                    System.out.println("Tilbage til hovedmenu.");
                    return;
                default:
                    System.out.println("Ugyldigt valg.");
                    break;
            }
            // Når medlemmet er opdateret, gem de ændrede oplysninger til filen
            saveMedlemToFile();
            System.out.println("Medlemmet er opdateret.");
        }

    }
}