public class Main {

    public static void main(String[] args) {

        Kontingent kk = new Kontingent();
        PersonPersistens pp = new PersonPersistens();
        pp.loadMedlemmerFromFile();


        System.out.println(kk.totalKontingent());

        pp.visMedlemmer();

        /*
        // Opret objekter
        Kontingent kontingent = new Kontingent();
        Medlem medlem = null;
        Hold hold = null;
        Traener traener = null;

        Scanner scanner = new Scanner(System.in);
        boolean runProgram = true;

        while (runProgram) {
            // Menuvalg
            System.out.println("Vælg en funktion:");
            System.out.println("1. Tilføj medlem");
            System.out.println("2. Beregn kontingent");
            System.out.println("3. Se alle medlemmer");
            System.out.println("4. Se hold");
            System.out.println("5. Beregn total kontingent");
            System.out.println("6. Se medlemmer i restance");
            System.out.println("7. Afslut");

            int valg = scanner.nextInt();
            scanner.nextLine();

            switch (valg) {
                case 1: // Opret medlem
                    System.out.println("Indtast navn:");
                    String navn = scanner.nextLine();
                    System.out.println("Indtast alder:");
                    int alder = Integer.parseInt(scanner.nextLine());
                    System.out.println("Indtast telefon:");
                    String telefon = scanner.nextLine();
                    System.out.println("Indtast email:");
                    String email = scanner.nextLine();

                    // Opret medlem
                   // Medlem medlem = new Motionist(navn, alder, telefon, email); // Eller hvilken som helst medlemsklasse du vælger
                    System.out.println("Medlem oprettet med medlemsnummer: " + medlem.getMedlemsnummer());
                    break;

                case 2: // Beregn kontingent
                    if (medlem != null) {
                        double kontingentBeløb = Kontingent.beregnKontingent(medlem);
                        System.out.println("Kontingent for " + medlem.getNavn() + ": " + kontingentBeløb);
                    } else {
                        System.out.println("Ingen medlem er tilføjet endnu.");
                    }
                    break;

                case 3: // Se alle medlemmer i restance
                    if (!kontingent.medlemmerIRestance(kontingent.getMedlemmer()).isEmpty()) {
                        System.out.println("Medlemmer i restance:");
                        for (Medlem m : kontingent.medlemmerIRestance(kontingent.getMedlemmer())) {
                            System.out.println(m);
                        }
                    } else {
                        System.out.println("Ingen medlemmer i restance.");
                    }
                    break;

                case 4: // Se hold
                    System.out.println("Indtast holdnavn:");
                    String holdNavn = scanner.nextLine();
                    System.out.println("Indtast træner navn:");
                    String traenerNavn = scanner.nextLine();
                    hold = new Hold(holdNavn, traener, "Mandag", 16);
                    traener = new Traener(traenerNavn, 35, "12345678", "traener@mail.com");
                    hold.getTraener();
                    System.out.println("Holdet " + holdNavn + " med træner " + traenerNavn + " er oprettet.");
                    break;

                case 5: // Beregn total kontingent
                    double totalKontingent = kontingent.totalKontingent();
                    System.out.println("Total kontingent indtægter: " + totalKontingent);
                    break;

                case 6: // Se medlemmer i restance
                    System.out.println("Medlemmer i restance:");
                    for (Medlem m : kontingent.medlemmerIRestance(kontingent.getMedlemmer())) {
                        System.out.println(m);
                    }
                    break;

                case 7: // Afslut
                    runProgram = false;
                    System.out.println("Programmet afsluttes.");
                    break;

                default:
                    System.out.println("Ugyldigt valg, prøv igen.");
                    break;
            }
        }

        scanner.close();*/
    }
}