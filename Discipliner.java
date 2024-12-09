import java.util.ArrayList;
import java.util.List;

public class Discipliner {

    private  List<String>discipliner = new ArrayList<>();

    public Discipliner(){
        discipliner.add("Butterfly");
        discipliner.add("Crawl");
        discipliner.add("Rygcrawl");
        discipliner.add("Brystsvømning");
    }

    public List<String> getDiscipliner(){
        return discipliner;
    }

}
