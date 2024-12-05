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
    private static final String FILE_PATH = "traenere.txt";

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
            writeToFile();
            System.out.println("Træner oprettet: " + traener.getNavn());
            break;
                        }
        }

    public void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Traener traener : traenerListe) {
                writer.write(traener.navn + "," + traener.alder + "," + traener.telefon + "," + traener.email);
                writer.newLine();
            }
            System.out.println("Træner(e) gemt til fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af trænere til fil: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Traener traener = new Traener(data[0], Integer.parseInt(data[1]), data[2], data[3]);
                    traenerListe.add(traener);
                }
            }
            System.out.println("Trænere indlæst fra fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved indlæsning af trænere fra fil: " + e.getMessage());
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
