import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public abstract class Medlem {

    protected String medlemsnummer;
    protected String navn;
    protected String foedselsdato;
    protected LocalDateTime medlemsdato;
    protected boolean harBetaltKontingent;
    protected String telefon;
    protected String email;
    protected List<Medlem> medlemmer = new ArrayList<>();


    public Medlem() {
        this.medlemmer = new ArrayList<>();
    }

    public Medlem(String medlemsnummer, String navn, String foedselsdato, String telefon, String email, String medlemsdato) {
        this.medlemsnummer = medlemsnummer;
        this.navn = navn;
        this.foedselsdato = LocalDate.parse(foedselsdato).toString();
        this.telefon = telefon;
        this.email = email;
        this.medlemsdato = LocalDateTime.now();
        this.harBetaltKontingent = true;
    }

    public Medlem(String medlemsnummer, String navn, String foedselsdato, String telefon, String email) {
        this.medlemsnummer = medlemsnummer;
        this.navn = navn;
        this.foedselsdato = LocalDate.parse(foedselsdato).toString();
        this.telefon = telefon;
        this.email = email;
        medlemsdato = LocalDateTime.now();
    }
    //commit 5/12
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

    public String getMedlemsdato(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return medlemsdato.format(formatter);
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
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
        return getAlder() >= 65;
    }

    public boolean erJunior(){
        return getAlder() < 18;
    }

    public boolean harBetalt(){
        return harBetaltKontingent;
    }

    public void setBetalt(boolean harBetaltKontingent){
        this.harBetaltKontingent = harBetaltKontingent;
    }

    public String toString(){
        return "Medlemsnummer: " + ", " + medlemsnummer + ", " +"Navn: " + navn + ", " + "Fødselsdato: " + foedselsdato + ", " + "Telefon: " + telefon + ", " + "Email: " + email;
    }

    public String getFoedselsdato() {
        return foedselsdato;
    }
}