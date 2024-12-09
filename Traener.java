import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//commit 5/12
public class Traener {

    private String navn;
    private int alder;
    private String telefon;
    private String email;
    private Hold hold;
    private List<Traener> traenerListe = new ArrayList<>();
    private static final String FILE_PATH_TRAENER = "traenere.txt";
    private MedlemManagement mm = new MedlemManagement();

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
        Scanner scanner = new Scanner(System.in);

        System.out.print("Vælg en mulighed: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                mm.visMedlemmer();
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
                System.out.println("Tilføj træningsresultater (ikke implementeret endnu).");
                break;
            case 6:
                System.out.println("Vis træningsresultater (ikke implementeret endnu).");
                break;
            case 0:
                System.out.println("Programmet afsluttes.");
                break;
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
