import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//commit 5/12
public class KonkurrenceSvoemmer extends Medlem {
    private List<Resultat> resultater = new ArrayList<>();
    //changes 9/12
    public KonkurrenceSvoemmer(String medlemsnummer, String navn, String foedselsdato, String telefon, String email, String medlemsdato) {
        super(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
    }

    public KonkurrenceSvoemmer(String medlemsnummer, String navn, String foedselsdato, String telefon, String email) {
        super(medlemsnummer, navn, foedselsdato, telefon, email);
    }

    public KonkurrenceSvoemmer(){

    }

    @Override
    public String getMedlemstype() {
        return "KonkurrenceSvoemmer";
    }
}