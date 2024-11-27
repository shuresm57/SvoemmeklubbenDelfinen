import java.util.Date;


public class Resultat {
    private String disciplin;
    private double tid;
    private Date dato;
    private KonkurrenceSvoemmer svoemmer;

    public Resultat(KonkurrenceSvoemmer svoemmer, String disciplin, double tid, Date dato) {
        this.svoemmer = svoemmer;
        this.disciplin = disciplin;
        this.tid = tid;
        this.dato = dato;
    }

    public String getDisciplin() {
        return disciplin;
    }

    public double getTid() {
        return tid;
    }

    public Date getDato() {
        return dato;
    }


    public int compareTo(Resultat andetResultat) {
        return Double.compare(this.tid, andetResultat.getTid());
    }
}



