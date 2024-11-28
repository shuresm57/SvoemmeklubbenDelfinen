import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.Period;

public abstract class Medlem {

    protected String medlemsnummer;
    protected String navn;
    protected int alder;
    protected String foedselsdato;
    protected boolean erPassiv;
    protected String telefon;
    protected String email;
    protected List<Medlem> medlemmer = new ArrayList<>();
    protected Hold hold;

    public Medlem() {
        this.medlemmer = new ArrayList<>();
    }

    public Medlem(String medlemsnummer, String navn, String foedselsdato, String telefon, String email) {
        this.medlemsnummer = medlemsnummer;
        this.navn = navn;
        this.foedselsdato = LocalDate.parse(foedselsdato).toString();
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
        return Period.between(LocalDate.parse(foedselsdato), LocalDate.now()).getYears();
    }


    public String getFoedselsdato(){
        return foedselsdato;
    }

    public void setFoedselsdato(String fødseldato){
        this.foedselsdato = foedselsdato;}

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

    public String toString(){
        return "Medlemsnummer: " + ", " + medlemsnummer + ", " +"Navn: " + navn + ", " + "Fødselsdato: " + foedselsdato + ", " + "Telefon: " + telefon + ", " + "Email: " + email;
    }

}