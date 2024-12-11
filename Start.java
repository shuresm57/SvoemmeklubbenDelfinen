import java.io.Console;
import java.util.Scanner;

public class Start {

    private MedlemManagement medManagement = new MedlemManagement();
    private Kontingent kontingent = new Kontingent();
    private Traener traener = new Traener();

    private static final String FORMAND_USERNAME = "formand";
    private static final String FORMAND_PASSWORD = "1234";

    private static final String TRAENER_USERNAME = "traener";
    private static final String TRAENER_PASSWORD = "1234";

    private static final String KASSERER_USERNAME = "kasserer";
    private static final String KASSERER_PASSWORD = "1234";

    public Start (){}

    public static String login() {
        Console console = System.console();
        while (true) {
            System.out.println("Svømmeklub Delfinen ");
            System.out.print("Indtast brugernavn: ");
            String username = console.readLine();

            char[] passwordArray = console.readPassword("Indtast kodeord: ");
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
        medManagement.loadMedlemmerFromFile();
        Scanner scanner = new Scanner(System.in);

        String rolle = login();

        while (true) {
            System.out.println("\nVelkommen, " + rolle + "!");
            if ("formand".equalsIgnoreCase(rolle)) {
                medManagement.runMedlemManagement();
            } else if ("traener".equalsIgnoreCase(rolle)) {
                traener.runTraener();
            } else if ("kasserer".equalsIgnoreCase(rolle)) {
                kontingent.runKontingent();
            }

            System.out.print("Vælg en mulighed: ");
            scanner.nextLine();

            // Håndter muligheder baseret på rolle med if-else
            if ("formand".equalsIgnoreCase(rolle)) {
                medManagement.runMedlemManagement();
            } else if ("traener".equalsIgnoreCase(rolle)) {
                traener.runTraener();
            } else if ("kasserer".equalsIgnoreCase(rolle)) {
                kontingent.runKontingent();
            }
        }
    }


}
