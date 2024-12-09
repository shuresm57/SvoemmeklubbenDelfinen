import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
//commit 5/12
public class MedlemManagement {

    private static final String FILE_PATH_MEDLEMMER = "medlemmer.txt";  // Filstien til at gemme og læse medlemmer fra.
    private List<Medlem> medlemmer = new ArrayList<>();// Liste til at gemme medlemmer.
    private List<String> medlemsNumre = new ArrayList<>();
    private static final String FORMAND_USERNAME = "formand";
    private static final String FORMAND_PASSWORD = "1234";

    private static final String TRAENER_USERNAME = "traener";
    private static final String TRAENER_PASSWORD = "1234";

    private static final String KASSERER_USERNAME = "kasserer";
    private static final String KASSERER_PASSWORD = "1234";

    private static final Kontingent kontingent = new Kontingent();
    private static final Traener traener = new Traener();
    //changes 9/12
    public static void main(String[] args) {
        MedlemManagement persistens = new MedlemManagement();
        persistens.run();// Kør programmet og lad brugeren vælge og oprette medlemmer.
    }

    public List<Medlem> getMedlemmer() {
        return medlemmer;
    }

    public List<String> getMedlemsNumre() {
        return medlemsNumre;
    }

    public String login() {
        Console console = System.console();
        while (true) {
            System.out.println("Svømmeklub Delfinen ");
            System.out.print("Indtast brugernavn: ");
            String username = console.readLine();

            char[] passwordArray = console.readPassword("Indtast kodeord:");
            String password = new String(passwordArray);

            if (username.equals(FORMAND_USERNAME) && password.equals(FORMAND_PASSWORD)) {
                return "formand";
            } else if (username.equals(TRAENER_USERNAME) && password.equals(TRAENER_PASSWORD)) {
                return "traener";
            } else if (username.equals(KASSERER_USERNAME) && password.equals(KASSERER_PASSWORD)) {
                return "kasserer";
            } else {
                System.out.println("Forkert brugernavn eller kode. Prøv igen!");
            }
        }
    }

    public void run() {
        FileUtil.loadMedlemmerFromFile(FILE_PATH_MEDLEMMER,7, medlemmer,medlemsNumre);
        Scanner scanner = new Scanner(System.in);


        String rolle = login();

        while (true) {
            System.out.println("\nVelkommen, " + rolle + "!");
            if ("formand".equalsIgnoreCase(rolle)) {
                System.out.println("\n1. Opret medlem");
                System.out.println("2. Gem medlemmer til fil");
                System.out.println("3. Ændre medlemsoplysninger");
                System.out.println("4. Vis medlemmer");
                System.out.println("5. Slet medlemmer");
                System.out.println("0. Afslut");
            } else if ("træner".equalsIgnoreCase(rolle)) {
                System.out.println("\n1. Vis medlemmer");
                System.out.println("2. Oret hold");
                System.out.println("3. tilføj deltager til hold");
                System.out.println("4. Fjern hold");
                System.out.println("5. Tilføj træningsresultater");
                System.out.println("6. Vis træningsresultater");
                System.out.println("0. Afslut");
            } else if ("kasserer".equalsIgnoreCase(rolle)) {
                kontingent.runKontingent();
            }

            System.out.print("Vælg en mulighed: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            // Håndter muligheder baseret på rolle med if-else
            if ("formand".equalsIgnoreCase(rolle)) {
                if (option == 1) {
                    //loadMedlemmerFromFile();
                    opretMedlem();
                } else if (option == 2) {
                    FileUtil.saveMedlemmer(FILE_PATH_MEDLEMMER,medlemmer, true);
                } else if (option == 3) {
                    opdaterMedlem();
                } else if (option == 4) {
                    visMedlemmer();
                } else if (option == 5) {
                    FileUtil.sletMedlem(FILE_PATH_MEDLEMMER);
                } else if (option == 0) {
                    System.out.println("Programmet afsluttes.");
                    break;
                } else {
                    System.out.println("Ugyldigt valg.");
                }
            } else if ("træner".equalsIgnoreCase(rolle)) {
                traener.runTraener();
            } else if ("kasserer".equalsIgnoreCase(rolle)) {
                kontingent.runKontingent();
            }
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
    public void loadMedlemmerFromFile(){
        FileUtil.loadMedlemmerFromFile(FILE_PATH_MEDLEMMER,7,medlemmer,medlemsNumre);
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
                FileUtil.saveMedlemmer(FILE_PATH_MEDLEMMER,medlemmer, true);
                System.out.println("Medlem oprettet: " + medlem.getMedlemsnummer());
                break;
            } else {
                System.out.println("Ugyldig medlemstype valgt.");
            }
        }
    }

    // Metode til at opdatere et medlem
    public void opdaterMedlem() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Indtast medlemsnummer på medlemmet, der skal opdateres: ");
        String medlemsnummer = scanner.nextLine();


        Medlem medlemToUpdate = null;
        for (Medlem medlem : medlemmer) {
            if (medlem.getMedlemsnummer().equalsIgnoreCase(medlemsnummer)) {
                medlemToUpdate = medlem;
                break;
            }
        }

        if (medlemToUpdate == null) {
            System.out.println("Medlem med medlemsnummer " + medlemsnummer + " blev ikke fundet.");
            return;  // Stop hvis medlemmet ikke findes
        }
        while (true) {
            System.out.println("Vælg hvad du vil opdatere: ");
            System.out.println("1. Navn");
            System.out.println("2. Telefon");
            System.out.println("3. Email");
            System.out.println("0. Tilbage");

            int valg = scanner.nextInt();
            scanner.nextLine();  // Forbruger den sidste linjefeed.


            if (valg == 1) {
                System.out.println("Indtast nyt navn: ");
                String nytNavn = scanner.nextLine();
                medlemToUpdate.setNavn(nytNavn);
            } else if (valg == 2) {
                System.out.println("Indtast nyt telefonnummer: ");
                String nyTelefon = scanner.nextLine();
                medlemToUpdate.setTelefon(nyTelefon);
            } else if (valg == 3) {
                System.out.println("Indtast ny email: ");
                String nyEmail = scanner.nextLine();
                medlemToUpdate.setEmail(nyEmail);

            } else if (valg == 0) {
                System.out.println("Tilbage til hovedmenu.");
                return;
            } else {
                System.out.println("Ugyldigt valg.");
                FileUtil.saveMedlemmer(FILE_PATH_MEDLEMMER,medlemmer, true);
                System.out.println("Medlemmet er opdateret.");
            }
        }
    }
}