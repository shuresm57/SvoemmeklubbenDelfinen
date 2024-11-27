public class KonkurrenceSvoemmer extends Medlem{

    private boolean crawl, butterfly, bryst, ryg;
    private Traener traener;
    private Hold hold;

    public KonkurrenceSvoemmer() {
    }

    public KonkurrenceSvoemmer(int medlemsnummer, String navn, int alder, String telefon, String email, Hold hold, Traener traener) {
        super(medlemsnummer, navn, alder, telefon, email);
        this.hold = hold;
        this.traener = hold.getTraener();
    }

    public KonkurrenceSvoemmer(int medlemsnummer, String navn, int alder, String telefon, String email) {
        super(medlemsnummer, navn, alder, telefon, email);
        this.hold = hold;
        this.traener = hold.getTraener();
    }

    public boolean erCrawl() {
        return crawl;
    }

    public boolean erButterfly() {
        return butterfly;
    }

    public boolean erBryst() {
        return bryst;
    }

    public boolean erRyg() {
        return ryg;
    }

    public void setHold(Hold hold){
        this.hold = hold;
    }

    public Traener getTraener() {
            return traener;
        }

    public void setTraener(Traener traener){
        this.traener = traener;
    }

}
