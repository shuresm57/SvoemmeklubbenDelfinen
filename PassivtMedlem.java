import java.time.LocalDate;

public class PassivtMedlem extends Medlem {
    //changes 9/12
    public PassivtMedlem(String medlemsnummer, String navn, String fødselsdato, String telefon, String email, String medlemsdato) {
        super(medlemsnummer, navn, fødselsdato, telefon, email, medlemsdato);
    }

    public PassivtMedlem(String medlemsnummer, String navn, String fødselsdato, String telefon, String email) {
        super(medlemsnummer, navn, fødselsdato, telefon, email);
    }

    @Override
    public String getMedlemstype() {
        return "PassivtMedlem";
    }
}