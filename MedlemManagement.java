import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

//commit 5/12
public class MedlemManagement {

    // Filstien til at gemme og læse medlemmer fra.
    private static final String FILE_PATH_MEDLEMMER = "medlemmer.txt";

    // Liste til at gemme medlemmer.
    private List<Medlem> medlemmer = new ArrayList<>();
    private List<String> medlemsNumre = new ArrayList<>();

    public List<Medlem> getMedlemmer() {
        return medlemmer;
    }

    public List<String> getMedlemsNumre() {
        return medlemsNumre;
    }

    public void runMedlemManagement() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n1. Opret medlem");
        System.out.println("2. Gem medlemmer til fil");
        System.out.println("3. Ændre medlemsoplysninger");
        System.out.println("4. Vis medlemmer");
        System.out.println("5. Slet medlemmer");
        System.out.println("9. Log ud");
        System.out.println("0. Afslut");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                //loadMedlemmerFromFile();
                opretMedlem();
                break;
            case 2:
                FileUtil.saveMedlemmer(FILE_PATH_MEDLEMMER, medlemmer, true);
                break;
            case 3:
                opdaterMedlem();
                break;
            case 4:
                visMedlemmer();
                break;
            case 5:
                FileUtil.sletMedlem(FILE_PATH_MEDLEMMER);
                break;
            case 9:
                System.out.println("Logger ud...");
                Start st = new Start();
                st.run();
                break;
            case 0:
                System.out.println("Programmet afsluttes.");
                System.exit(0);
                break;
            default:
                System.out.println("Ugyldigt valg.");
                break;
        }
    }

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
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_MEDLEMMER))) {
            String linje;
            while ((linje = reader.readLine()) != null) {
                if (linje.startsWith(kode)) {
                    String[] medlemsData = linje.split(",");
                    if (medlemsData.length >= 1) {
                        String eksisterendeNummer = medlemsData[0].substring(2);
                        int nummer = Integer.parseInt(eksisterendeNummer);
                        if (nummer >= naesteNummer) {
                            naesteNummer = nummer + 1;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af medlemsnumre fra fil: " + e.getMessage());
        }

        return kode + String.format("%03d", naesteNummer);
    }

    public void visMedlemmer() {
        if (medlemmer.isEmpty()) {
            System.out.println("Der er ingen medlemmer at vise.");
        } else {
            System.out.println("Liste over medlemmer:");
            for (Medlem medlem : medlemmer) {
                System.out.println(medlem);
            }
        }
    }

    public List<KonkurrenceSvoemmer> listeAfSvoemmere() {
        if (medlemmer.isEmpty()) {
            loadMedlemmerFromFile();
        }
        List<KonkurrenceSvoemmer> svoemmere = new ArrayList<>(medlemmer.size());
        for (Medlem medlem : medlemmer) {
            if (medlem instanceof KonkurrenceSvoemmer) {
                svoemmere.add((KonkurrenceSvoemmer) medlem);
            }
        }
        return svoemmere;
    }

    public KonkurrenceSvoemmer findKonkurrenceSvoemmerByMedlemsnummer(String medlemsnummer) {
        loadMedlemmerFromFile(); // Sørger for, at medlemmerne er indlæst.

        for (Medlem medlem : medlemmer) {
            if (medlem instanceof KonkurrenceSvoemmer && medlem.getMedlemsnummer().equals(medlemsnummer)) {
                return (KonkurrenceSvoemmer) medlem;
            }
        }

        System.out.println("Ingen konkurrencesvømmer fundet med medlemsnummer: " + medlemsnummer);
        return null; // Returner null, hvis ingen matcher.
    }

    //hjælpemetode
    public void loadMedlemmerFromFile() {
        FileUtil.loadMedlemmerFromFile(FILE_PATH_MEDLEMMER, 6, medlemmer, medlemsNumre);
    }

    // Metode til at oprette og gemme et medlem
    public void opretMedlem() {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String medlemstype = "";
        while (true) {
            System.out.println("Vælg medlemstype (1 = Konkurrencesvømmer, 2 = Motionist, 3 = Passivt medlem): ");
            medlemstype = scanner.nextLine().trim();

            String medlemsnummer = genererMedlemsnummer(medlemstype);
            // Bed brugeren om at indtaste oplysninger for medlemmet
            System.out.println("Indtast navn: ");
            String navn = scanner.nextLine();
            String foedselsdato = null;
            while (foedselsdato == null) {
                try {
                    System.out.println("Indtast foedselsdato (dd-MM-yyyy): ");
                    String foedselsdatoInput = scanner.nextLine();
                    foedselsdato = String.valueOf(LocalDate.parse(foedselsdatoInput, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                } catch (DateTimeParseException e) {
                    System.out.println("Forkert format. Prøv igen med formatet dd-MM-yyyy.");
                }
            }

            System.out.println("Indtast telefonnummer: ");
            String telefon = scanner.nextLine();

            System.out.println("Indtast email: ");
            String email = scanner.nextLine();

            // Opret medlemmet baseret på medlemstype
            Medlem medlem = null;

            if ("1".equalsIgnoreCase(medlemstype)) {
                medlem = new KonkurrenceSvoemmer(medlemsnummer, navn, foedselsdato, telefon, email);
            } else if ("2".equalsIgnoreCase(medlemstype)) {
                medlem = new Motionist(medlemsnummer, navn, foedselsdato, telefon, email);
            } else if ("3".equalsIgnoreCase(medlemstype)) {
                medlem = new PassivtMedlem(medlemsnummer, navn, foedselsdato, telefon, email);

            }

            // Tilføj medlemmet til listen
            if (medlem != null) {
                medlemmer.add(medlem);
                medlemsNumre.add(medlemsnummer);
                FileUtil.saveMedlemmer(FILE_PATH_MEDLEMMER, medlemmer, true);
                System.out.println("Medlem oprettet: " + medlem.getMedlemsnummer());
                break;
            } else {
                System.out.println("Ugyldig medlemstype valgt.");
            }
        }
    }

    public void opdaterMedlem() {
        Scanner scanner = new Scanner(System.in);
        FileUtil.loadMedlemmerFromFile(FILE_PATH_MEDLEMMER, 6, medlemmer, medlemsNumre);
        Medlem medlemToUpdate = null;

        System.out.println("Indtast medlemsnummer på medlemmet, der skal opdateres:");
        String medlemsnummer = scanner.nextLine().trim();

        // Find medlemmet i listen
        for (Medlem medlem : medlemmer) {
            if (medlem.getMedlemsnummer().equalsIgnoreCase(medlemsnummer)) {
                medlemToUpdate = medlem;
                break;
            }
        }

        if (medlemToUpdate == null) {
            System.out.println("Medlem med medlemsnummer " + medlemsnummer + " blev ikke fundet.");
            return;
        }

        System.out.println("Medlem med nummeret er fundet: " + medlemToUpdate.getNavn());
        System.out.println("Hvad vil du opdatere?");
        System.out.println("1. Navn");
        System.out.println("2. Telefon nummer");
        System.out.println("3. Email");

        int option = scanner.nextInt();
        scanner.nextLine(); // Forbrug newline

        // Opdater det nødvendige felt baseret på brugerens valg
        switch (option) {
            case 1:
                System.out.println("Indtast nyt navn: ");
                String navn = scanner.nextLine().trim();
                medlemToUpdate.setNavn(navn);
                break;
            case 2:
                System.out.println("Indtast nyt telefon nummer: ");
                String telefon = scanner.nextLine().trim();
                medlemToUpdate.setTelefon(telefon);
                break;
            case 3:
                System.out.println("Indtast ny email: ");
                String email = scanner.nextLine().trim();
                medlemToUpdate.setEmail(email);
                break;
            default:
                System.out.println("Ugyldigt valg.");
                return;
        }

        // Opdater medlemmet i filen
        FileUtil.opdaterMedlem(FILE_PATH_MEDLEMMER, medlemToUpdate);
        System.out.println("Medlemmet er blevet opdateret: " + medlemToUpdate.getNavn());
    }



}
