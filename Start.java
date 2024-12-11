import java.io.Console;
import java.util.Scanner;

public class Start {

    private                 MedlemManagement    medManagement       = new MedlemManagement();
    private                 Kontingent          kontingent          = new Kontingent();
    private                 Traener             traener             = new Traener();

    private static final    String              FORMAND_USERNAME    = "formand";
    private static final    String              FORMAND_PASSWORD    = "formand";
    private static final    String              TRAENER_USERNAME    = "traener";
    private static final    String              TRAENER_PASSWORD    = "traener";
    private static final    String              KASSERER_USERNAME   = "kasserer";
    private static final    String              KASSERER_PASSWORD   = "kasserer";

    public Start() {
    }

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
        Kontingent k = new Kontingent();
        k.alreadyLoaded();

        String rolle = login();

        while (true) {
            System.out.println("\nMenu for " + rolle + ", vælg en mulighed:");
            if ("formand".equalsIgnoreCase(rolle)) {
                medManagement.runMedlemManagement();
                continue;
            } else if ("traener".equalsIgnoreCase(rolle)) {
                traener.runTraener();
                continue;
            } else if ("kasserer".equalsIgnoreCase(rolle)) {
                kontingent.runKontingent();
                continue;
            }

            System.out.print("Vælg en mulighed: ");
            scanner.nextLine();

        }

    }





}
