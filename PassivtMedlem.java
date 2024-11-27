public class PassivtMedlem extends Medlem {

    public PassivtMedlem(int medlemsnummer, String navn, int alder, String telefon, String email) {
        super(medlemsnummer, navn, alder , telefon, email);
    }

    public boolean erPassiv(){
        return true;
    }
}
