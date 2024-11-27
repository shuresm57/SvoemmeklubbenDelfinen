import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Medlem {

    protected int medlemsnummer;
    protected String navn;
    protected int alder;
    protected boolean erPassiv;
    protected String telefon;
    protected String email;
    protected List<Medlem> medlemmer = new ArrayList<Medlem>();
    protected final String FILE_PATH = "medlemmer.txt";

    public Medlem() {
        this.medlemmer = new ArrayList<>();
        loadMedlemmerFromFile();
    }

    public Medlem(int medlemsnummer, String navn, int alder, String telefon, String email) {
        this.medlemmer = new ArrayList<>();
        this.medlemsnummer = medlemsnummer;
        this.navn = navn;
        this.alder = alder;
        this.telefon = telefon;
        this.email = email;
    }


    private void loadMedlemmerFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String Linje;
            while ((Linje = reader.readLine()) != null) {
                String[] medlemsData = Linje.split(","); //Gør kommaet til en opdeler
                if (medlemsData.length == 5) { //Sikrer sig at pizzaData arrayet har 3 elementer(nummer, navn og pris)
                    int nummer = Integer.parseInt(medlemsData[0]);
                    String navn = medlemsData[1];
                    int alder = Integer.parseInt(medlemsData[2]);
                    String telefon = medlemsData[3];
                    String email = medlemsData[4];

                    Motionist motionist = new Motionist(nummer, navn, alder, telefon, email);
                    medlemmer.add(motionist);

                } else if (medlemsData.length == 6) {
                    int nummer = Integer.parseInt(medlemsData[0]);
                    String navn = medlemsData[1];
                    int alder = Integer.parseInt(medlemsData[2]);
                    String telefon = medlemsData[3];
                    String email = medlemsData[4];
                    boolean erPassiv = Boolean.parseBoolean(medlemsData[5]);

                    PassivtMedlem pm = new PassivtMedlem(nummer, navn, alder, telefon, email);
                    medlemmer.add(pm);
                } else if (medlemsData.length == 7) {
                    int nummer = Integer.parseInt(medlemsData[0]);
                    String navn = medlemsData[1];
                    int alder = Integer.parseInt(medlemsData[2]);
                    String telefon = medlemsData[3];
                    String email = medlemsData[4];
                    String holdNavn = medlemsData[5];
                    String traenerNavn = medlemsData[6];


                    Hold hold = findEllerOpretHold(holdNavn, traenerNavn);
                    Traener traener = hold.getTraener();

                    KonkurrenceSvoemmer ks = new KonkurrenceSvoemmer(nummer, navn, alder, telefon, email, hold, traener);
                    medlemmer.add(ks);
                    hold.tilfoejTilHold(ks);

                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning fra fil: " + e.getMessage()); //Giver os besked om hvad den mulige fejl er
        }
    }

    private Hold findEllerOpretHold(String holdNavn, String traenerNavn) {
        for (Hold hold : Hold.getHoldListe()) {
            if (hold.getHoldnavn().equals(holdNavn)) {
                return hold;
            }
        }
        Traener traener = findEllerOpretTraener(traenerNavn);
        Hold nytHold = new Hold(holdNavn, traener, "Ugedag ikke specificeret", 0);
        Hold.getHoldListe().add(nytHold);
        return nytHold;
    }

    private Traener findEllerOpretTraener(String traenerNavn) {
        for (Traener traener : Traener.getTraenerListe()) {
            if (traener.getNavn().equals(traenerNavn)) {
                return traener;
            }
        }
        Traener nyTraener = new Traener(traenerNavn, 0, "Telefon ikke specificeret", "Email ikke specificeret");
        Traener.getTraenerListe().add(nyTraener);
        return nyTraener;
    }




    public Medlem(int medlemsnummer, String navn, int alder, String telefon, String email, boolean erPassiv) { //
        this.medlemsnummer = medlemsnummer;
        this.navn = navn;
        this.alder = alder;
        this.telefon = telefon;
        this.email = email;
        this.erPassiv = erPassiv;
    }

    public boolean erJunior() {
        return alder < 18;
    }

    public boolean erSenior() {
        return alder > 18;
    }


    public boolean erIRestance() {
        return erIRestance();
    }

    public void visMedlemmer() {
        for (Medlem medlem : medlemmer) {
            System.out.println(medlem);
        }
    }

    public boolean erPassiv() {
        return erPassiv();
    }


    public int getMedlemsnummer() {
        return medlemsnummer;
    }

    public void setMedlemsnummer(int medlemsnummer) {
        this.medlemsnummer = medlemsnummer;
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

    public void setTelefon() {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail() {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Medlem{" +
                "medlemsnummer=" + medlemsnummer +
                ", navn='" + navn + '\'' +
                ", alder=" + alder +
                '}';
    }
}
