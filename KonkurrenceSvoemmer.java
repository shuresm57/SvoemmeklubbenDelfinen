import java.time.LocalDate;
public class KonkurrenceSvoemmer extends Medlem {

    public KonkurrenceSvoemmer(String medlemsnummer, String navn, String foedselsdato, String telefon, String email) {
        super(medlemsnummer, navn, foedselsdato, telefon, email);
    }

    @Override
    public String getMedlemstype() {
        return "KonkurrenceSvoemmer";
    }

    public void setTraener(Traener traener) {
    }

}