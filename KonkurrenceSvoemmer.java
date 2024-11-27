public class KonkurrenceSvoemmer extends Medlem {

    public KonkurrenceSvoemmer(String medlemsnummer, String navn, int alder, String telefon, String email) {
        super(medlemsnummer, navn, alder, telefon, email);
    }

    @Override
    public String getMedlemstype() {
        return "KonkurrenceSvoemmer";
    }

    public void setTraener(Traener traener) {
    }

}