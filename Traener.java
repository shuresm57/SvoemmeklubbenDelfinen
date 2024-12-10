import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
//changes 9/12
public class Traener {

    private String navn;
    private int alder;
    private String telefon;
    private String email;
    private Hold hold;
    private List<Traener> traenerListe = new ArrayList<>();
    private static final String FILE_PATH_TRAENER = "traenere.txt";
    private MedlemManagement mm = new MedlemManagement();
    private List<Resultat> resultater = new ArrayList<>();

    public void visResultater(){
        Resultat resultat = new Resultat();
        System.out.println(resultater);
    }

    public Traener() {

    }

    public Traener(String navn, int alder, String telefon, String email) {
        this.navn = navn;
        this.alder = alder;
        this.telefon = telefon;
        this.email = email;
    }

    public void addTraener(Traener traener) {
        traenerListe.add(traener);
    }

    public List<Traener> getTraenerListe() {
        return traenerListe;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public int getAlder() {
        return alder;
    }

    public void setAlder(int alder) {
        this.alder = alder;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void opretTraener() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Indtast navn på Træner der skal oprettes i systemet: ");
            String navn = scanner.nextLine();

            System.out.println("Indtast " + navn + "'s alder:");
            int alder = Integer.parseInt(scanner.nextLine());

            System.out.println("Indtast telefon");
            String telefon = scanner.nextLine();

            System.out.println("Indtast email");
            String email = scanner.nextLine();

            Traener traener = new Traener(navn, alder, telefon, email);
            addTraener(traener);
            FileUtil.writeTraenerToFile(FILE_PATH_TRAENER,traenerListe);
            System.out.println("Træner oprettet: " + traener.getNavn());
            break;
                        }
        }

    //Hjælpefunktion til FileUtil.loadHoldFromFile
    public void loadFromFile(){
        FileUtil.loadTraenerFromFile(FILE_PATH_TRAENER,traenerListe);
    }

    public void runTraener(){
        MedlemManagement mm = new MedlemManagement();
        Hold hold = new Hold();
        Staevne staevne = new Staevne();
        Resultat resultat = new Resultat();
        resultat.laesResultaterFraFil();

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Vis Hold");
        System.out.println("2. Oret hold");
        System.out.println("3. tilføj deltager til hold");
        System.out.println("4. Fjern hold");
        System.out.println("5. Tilføj træningsresultater");
        System.out.println("6. Vis træningsresultater");
        System.out.println("7. Opret stævne");
        System.out.println("8. Vis stævneresultater");
        System.out.println("9. Log ud");
        System.out.println("0. Afslut");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                hold.printHoldListe();
                break;
            case 2:
                hold.opretHold();
                break;
            case 3:
                System.out.println("Tilføj deltager til hold (ikke implementeret endnu).");
                break;
            case 4:
                System.out.println("Fjern hold.");
                FileUtil.sletHold(hold.getFILE_PATH_HOLD());
                break;
            case 5:
                System.out.println("Tilføj træningsresultater.");
                resultat.traeningsResultater();
                break;
            case 6:
                System.out.println("Vis træningsresultater.");
                resultat.laesResultaterFraFil();
                resultat.printResultater();
                break;
            case 7:
                System.out.println("Opret stævne.");
                staevne.opretStaevne();
            case 8:
                System.out.println("Vis stævneresultater. (virker ikke endnu)");
                staevne.printStaevneListe();
            case 9:
                System.out.println("Logger ud...");
                mm.login();
                break;
            case 0:
                System.out.println("Programmet afsluttes.");
                System.exit(0);
            default:
                System.out.println("Ugyldigt valg.");
                break;
        }
    }

    @Override
    public String toString() {
        return "Traener{" +
                "Navn='" + navn + '\'' +
                ", Alder=" + alder +
                ", Telefon nummer='" + telefon + '\'' +
                ", Email='" + email + '\'' +
                '}';
    }
}
