import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class Traener {

    private String navn;
    private int alder;
    private String telefon;
    private String email;
    private Hold hold;
    private Traener traener;
    private static List<Traener> traenerListe = new ArrayList<>();


    public Traener(String navn, int alder, String telefon, String email){
    this.navn = navn;
    this.alder = alder;
    this.telefon = telefon;
    this.email = email;
    }

    public static Collection<Traener> getTraenerListe() {
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public int getAlder() {
        return alder;
    }

    public void setAlder(int alder) {
        this.alder = alder;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTraener() {
        String tr = hold.getTraener().toString();
        return tr;
    }


    public Hold getHold() {
        return hold;
    }

    public void setHold(Hold hold) {
        this.hold = hold;
    }

public static List<Traener> getTraenerListe() {
        return traenerListe;
}


    @Override
    public String toString() {
        return "Traener{" +
                "Navn='" + navn + '\'' +
                ", Alder=" + alder +
                ", Telefon nummer='" + telefon + '\'' +
                ", Email='" + email + '\'' +
                '}';
    }
}
