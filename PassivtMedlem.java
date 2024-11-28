import java.time.LocalDate;

public class PassivtMedlem extends Medlem {

    public PassivtMedlem(String medlemsnummer, String navn, String fødselsdato, String telefon, String email) {
        super(medlemsnummer, navn, fødselsdato, telefon, email);
    }

    @Override
    public String getMedlemstype() {
        return "PassivtMedlem";
    }
}