import java.time.LocalDate;
public class Motionist extends Medlem {

    public Motionist(String medlemsnummer, String navn, String foedselsdato, String telefon, String email, String medlemsdato) {
        super(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
    }

    public Motionist(String medlemsnummer, String navn, String foedselsdato, String telefon, String email) {
        super(medlemsnummer, navn, foedselsdato, telefon, email);
    }

    @Override
    public String getMedlemstype() {
        return "Motionist";
    }
}