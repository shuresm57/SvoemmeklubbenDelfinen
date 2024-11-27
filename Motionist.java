public class Motionist extends Medlem {

    public Motionist(String medlemsnummer, String navn, int alder, String telefon, String email) {
        super(medlemsnummer, navn, alder, telefon, email);
    }

    @Override
    public String getMedlemstype() {
        return "Motionist";
    }
}