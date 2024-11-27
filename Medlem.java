import java.util.ArrayList;
import java.util.List;

public abstract class Medlem {

    protected String medlemsnummer;
    protected String navn;
    protected int alder;
    protected boolean erPassiv;
    protected String telefon;
    protected String email;
    protected List<Medlem> medlemmer = new ArrayList<>();
    protected Hold hold;

    public Medlem() {
        this.medlemmer = new ArrayList<>();
    }

    public Medlem(String medlemsnummer, String navn, int alder, String telefon, String email) {
        this.medlemsnummer = medlemsnummer;
        this.navn = navn;
        this.alder = alder;
        this.telefon = telefon;
        this.email = email;
    }

    public abstract String getMedlemstype();


    public void setMedlemsnummer(String medlemsnummer) {
        this.medlemsnummer = medlemsnummer;
    }

    public String getMedlemsnummer() {
        return medlemsnummer;
    }

    public String getNavn() {
        return navn;
    }

    public int getAlder() {
        return alder;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setHold(Hold hold) {
        this.hold = hold;
    }
    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setAlder(int alder) {
        this.alder = alder;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean erSenior(){
        if(getAlder() >= 65){
            return true;
        }
        return false;
    }

    public String toString(){
        return "Medlemsnummer: " + ", " + medlemsnummer + ", " +"Navn: " + navn + ", " + "Alder: " + alder + ", " + "Telefon: " + telefon + ", " + "Email: " + email;
    }

    public boolean erJunior(){
        if(getAlder() < 18){
            return true;
        }
        return false;
    }

    public boolean harBetalt(){
        return true;
    }

    public boolean erIRestance(){
        return !harBetalt();
    }
}