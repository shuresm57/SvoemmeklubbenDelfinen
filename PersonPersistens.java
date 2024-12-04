import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    // Metode til at gemme medlemmer til fil
    public void saveMedlemToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Medlem medlem : medlemmer) {
                writer.write(medlem.getMedlemsnummer() + "," + medlem.getMedlemstype() + "," + medlem.getNavn() + "," + medlem.getFoedselsdato() + "," + medlem.getTelefon() + "," + medlem.getEmail());
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
                if (medlemsData.length == 6) {
                    String medlemsnummer = medlemsData[0];
                    String medlemstype = medlemsData[1];
                    String navn = medlemsData[2];
                    String foedselsdato = medlemsData[3];
                    String telefon = medlemsData[4];
                    String email = medlemsData[5];

                    Medlem medlem = null;
                    if ("KonkurrenceSvoemmer".equalsIgnoreCase(medlemstype)) {
                        medlem = new KonkurrenceSvoemmer(medlemsnummer, navn,foedselsdato, telefon, email);
                        medlemmer.add(medlem);
                        medlemsNumre.add(medlemsnummer);
                    } else if ("Motionist".equalsIgnoreCase(medlemstype)) {
                        medlem = new Motionist(medlemsnummer, navn, foedselsdato, telefon, email);
                        medlemmer.add(medlem);
                        medlemsNumre.add(medlemsnummer);
                    } else if ("PassivtMedlem".equalsIgnoreCase(medlemstype)) {
                        medlem = new PassivtMedlem(medlemsnummer, navn, foedselsdato, telefon, email);
                        medlemmer.add(medlem);
                        medlemsNumre.add(medlemsnummer);
                    }


                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning fra fil: " + e.getMessage());
        }
    }

    private String login() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Vælg din rolle: ");
            System.out.println("1. Formand");
            System.out.println("2. Træner");
            System.out.println("3. Kasserer");
            System.out.println("0. Afslut");

            System.out.print("Vælg en mulighed: ");
            int valg = scanner.nextInt();
            scanner.nextLine(); // Forbruger linjeskiftet

            if (valg == 1) {
                return "formand";
            } else if (valg == 2) {
                return "træner";
            } else if (valg == 3) {
                return "kasserer";
            } else if (valg == 0) {
                System.out.println("Programmet afsluttes.");
                System.exit(0); // Afslut programmet
            } else {
                System.out.println("Ugyldigt valg. Prøv igen.");
            }
        }
    }
    public void run() {
        PersonPersistens persistens = new PersonPersistens();
        persistens.loadMedlemmerFromFile();
        Scanner scanner = new Scanner(System.in);

        // Login-processen
        String rolle = login();

        // Hovedmenu baseret på rolle
        while (true) {
            System.out.println("\nVelkommen, " + rolle + "!");
            if ("formand".equalsIgnoreCase(rolle)) {
                System.out.println("\n1. Opret medlem");
                System.out.println("2. Gem medlemmer til fil");
                System.out.println("3. Ændre medlemsoplysninger");
                System.out.println("4. Vis medlemmer");
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
                System.out.println("\n1. Vis medlemmer");
                System.out.println("2. Opdater betalingsstatus");
                System.out.println("3. Vis betalingsoversigt");
                System.out.println("0. Afslut");
            }

            System.out.print("Vælg en mulighed: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Forbruger linjeskiftet

            // Håndter muligheder baseret på rolle med if-else
            if ("formand".equalsIgnoreCase(rolle)) {
                if (option == 1) {
                    opretMedlem();
                } else if (option == 2) {
                    saveMedlemToFile();
                } else if (option == 3) {
                    opdaterMedlem();
                } else if (option == 4) {
                    visMedlemmer();
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
                }else if (option == 4) {
                    System.out.println("Fjern hold (ikke implementeret endnu).");
                }else if (option == 5) {
                    System.out.println("Tilføj træningsresultater (ikke implementeret endnu).");
                }else if (option == 6) {
                    System.out.println("Vis træningsresultater (ikke implementeret endnu).");
                } else if (option == 0) {
                    System.out.println("Programmet afsluttes.");
                    break;
                } else {
                    System.out.println("Ugyldigt valg.");
                }
            } else if ("kasserer".equalsIgnoreCase(rolle)) {
                if (option == 1) {
                    visMedlemmer();
                } else if (option == 2) {
                    System.out.println("Opdater betalingsstatus (ikke implementeret endnu).");
                } else if (option == 3) {
                    System.out.println("Vis betalingsoversigt (ikke implementeret endnu).");
                } else if (option == 0) {
                    System.out.println("Programmet afsluttes.");
                    break;
                } else {
                    System.out.println("Ugyldigt valg.");
                }
            }
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
            }

            // Når medlemmet er opdateret, gem de ændrede oplysninger til filen
            saveMedlemToFile();
            System.out.println("Medlemmet er opdateret.");
        }
    }


}