public class Main {
    public static void main(String[] args) {

        Traener t = new Traener("Mathias", 21, "30569972", "mathiasmortensen@outlook.dk");

        Hold hold = new Hold("junior delfinerne",t, "Søndag", 1230);

        Traener md = new Traener("Markus D", 22, "112", "md@delfinen.dk");
        Hold juniorHold1 = new Hold("Junior Delfinerne 1", md, "Mandag Tirsdag", 1230);

        KonkurrenceSvoemmer ks = new KonkurrenceSvoemmer(100, "Valde", 20, "25148607","vosrensen");
        t.setHold(hold);
        hold.tilfoejTilHold(ks);
        ks.setHold(hold);
        System.out.println(ks.getTraener());
        //hold.getAlleHold();

        //System.out.println(t.getHold());

    }
}


