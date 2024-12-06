import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
//commit 5/12
public class PersonPersistens {

    private static final String FILE_PATH = "medlemmer.txt";  // Filstien til at gemme og læse medlemmer fra.
    private List<Medlem> medlemmer = new ArrayList<>();// Liste til at gemme medlemmer.
    private List<String> medlemsNumre = new ArrayList<>();
    private static final String FORMAND_USERNAME = "formand";
    private static final String FORMAND_PASSWORD = "1234";

    private static final String TRAENER_USERNAME = "traener";
    private static final String TRAENER_PASSWORD = "1234";

    private static final String KASSERER_USERNAME = "kasserer";
    private static final String KASSERER_PASSWORD = "1234";

    public static void main(String[] args) {
        PersonPersistens persistens = new PersonPersistens();
        persistens.run();// Kør programmet og lad brugeren vælge og oprette medlemmer.
    }

    public List<Medlem> getMedlemmer() {
        return medlemmer;
    }

    public List<String> getMedlemsNumre() {
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
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
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
        // Sammensæt det nye medlemsnummer og returner det
        return kode + String.format("%03d", naesteNummer);
    }

    public void visMedlemmer() {
        if (medlemmer.isEmpty()) {
            System.out.println("Der er ingen medlemmer at vise.");
        } else {
            System.out.println("Liste over medlemmer:");
            for (Medlem medlem : medlemmer) {
                System.out.println(medlem); // Sørg for, at toString() er implementeret i Medlem-klassen
            }
        }
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
                saveMedlemToFile();
                System.out.println("Medlem oprettet: " + medlem.getMedlemsnummer());
                break;
            } else {
                System.out.println("Ugyldig medlemstype valgt.");
            }
            // Tjek om input er gyldigt
        }
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

                // Når medlemmet er opdateret, gem de ændrede oplysninger til filen
                saveMedlemToFile();
                System.out.println("Medlemmet er opdateret.");
            }
        }
    }

    public void sletMedlem() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Indtast medlemsnummer på medlemmet, der skal slettes: ");
        String medlemsnummer = scanner.nextLine(); // Læs medlemsnummer fra brugeren
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


    // Metode til at gemme medlemmer til fil
    public void saveMedlemToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH,true))) {
            for (Medlem medlem : medlemmer) {
                writer.write(medlem.getMedlemsnummer() + "," + medlem.getMedlemstype() + "," + medlem.getNavn() +
                        "," + medlem.getFoedselsdato() + "," + medlem.getTelefon() + "," + medlem.getEmail() + "," + medlem.getMedlemsdato());
                writer.newLine();
            }

            //medlemmer.add(medlem);
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
                if (medlemsData.length == 7) {
                    String medlemsnummer = medlemsData[0];
                    String medlemstype = medlemsData[1];
                    String navn = medlemsData[2];
                    String foedselsdato = medlemsData[3];
                    String telefon = medlemsData[4];
                    String email = medlemsData[5];
                    String medlemsdato = medlemsData[6];


                    Medlem medlem = null;
                    if ("KonkurrenceSvoemmer".equalsIgnoreCase(medlemstype)) {
                        medlem = new KonkurrenceSvoemmer(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                        medlemmer.add(medlem);
                        medlemsNumre.add(medlemsnummer);  // Tilføj medlemsnummeret til medlemsNumre
                    } else if ("Motionist".equalsIgnoreCase(medlemstype)) {
                        medlem = new Motionist(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                        medlemmer.add(medlem);
                        medlemsNumre.add(medlemsnummer);  // Tilføj medlemsnummeret til medlemsNumre
                    } else if ("PassivtMedlem".equalsIgnoreCase(medlemstype)) {
                        medlem = new PassivtMedlem(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                        medlemmer.add(medlem);
                        medlemsNumre.add(medlemsnummer);  // Tilføj medlemsnummeret til medlemsNumre
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning fra fil: " + e.getMessage());
        }
    }


    public String login() {
        Console console = System.console();
        while (true) {
            System.out.println("Svømmeklub Delfinen");
            System.out.print("Indtast brugernavn:" + "\n");
            String username = console.readLine();

            char[] passwordArray = console.readPassword("Indtast kodeord:" + "\n");
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
        loadMedlemmerFromFile();
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
                runKontingent();
            }

            System.out.print("Vælg en mulighed: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Forbruger linjeskiftet

            // Håndter muligheder baseret på rolle med if-else
            if ("formand".equalsIgnoreCase(rolle)) {
                if (option == 1) {
                    //loadMedlemmerFromFile();
                    opretMedlem();
                } else if (option == 2) {
                    saveMedlemToFile();
                } else if (option == 3) {
                    opdaterMedlem();
                } else if (option == 4) {
                    //loadMedlemmerFromFile();
                    visMedlemmer();
                } else if (option == 5) {
                    sletMedlem();
                } else if (option == 0) {
                    System.out.println("Programmet afsluttes.");
                    break;
                } else {
                    System.out.println("Ugyldigt valg.");
                }
            } else if ("træner".equalsIgnoreCase(rolle)) {
                if (option == 1) {
                    visMedlemmer();
                } else if (option == 2) {
                    System.out.println("Opret hold (ikke implementeret endnu).");
                } else if (option == 3) {
                    System.out.println("Tilføj deltager til hold (ikke implementeret endnu).");
                } else if (option == 4) {
                    System.out.println("Fjern hold (ikke implementeret endnu).");
                } else if (option == 5) {
                    System.out.println("Tilføj træningsresultater (ikke implementeret endnu).");
                } else if (option == 6) {
                    System.out.println("Vis træningsresultater (ikke implementeret endnu).");
                } else if (option == 0) {
                    System.out.println("Programmet afsluttes.");
                    break;
                } else {
                    System.out.println("Ugyldigt valg.");
                }
            } else if ("kasserer".equalsIgnoreCase(rolle)) {
                runKontingent();
            }
        }
    }

    public void runKontingent() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Kontingent kk = new Kontingent();

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
                    System.out.println(kk.totalKontingent());
                    break;
                case 2:
                    kk.loadMedlemmerFromFile();
                    System.out.println("Medlemmer der er i restance: \n");
                    kk.getMedlemmerIRestance();
                    break;
                case 3:
                    System.out.println("Indtast medlemsnummer på medlemmet, der skal sættes i restance: ");
                    kk.tilfoejMedlemTilRestance();
                    break;
                case 4:
                    System.out.println("Indtast medlemsnummer på medlemmet, der skal slettes i restance: ");
                    kk.sletMedlemRestance();
                    break;
                case 5:
                    System.out.println("Viser alle medlemmer\n");
                    medlemmer.forEach(System.out::println);
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