import java.io.*;
import java.util.ArrayList;

public class filewriter {
/*

    private ArrayList<Pizza> pizzaMenu;
    private final String FILE_PATH = "menu.txt"; // Bruges til at låse FILE_PATH så den ikke ved en fejl bliver ændret

    public Menu() {
        this.pizzaMenu = new ArrayList<>();
        loadMenuFromFile();  // Dette gør så vi aflæser menu.txt filen fra programstart
    }


    // Metoden til at gemme og opdatere pizza menuen til vores menu.txt fil
    private void saveMenuToFile() {

        // Bruges til at gemme/opdatere vores nye pizzaer til txt filen
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Pizza pizza : pizzaMenu) {
                writer.write(pizza.getNummer() + "," + pizza.getNavn() + "," + pizza.getPris());
                writer.newLine();
            }
            //sortere pizzanumre i rækkefølge fra 0 og op
            pizzaMenu.sort((p1, p2) -> Integer.compare(Integer.parseInt(p1.getNummer()), Integer.parseInt(p2.getNummer())));

            System.out.println("Menuen er blevet opdateret og gemt.");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning til fil: " + e.getMessage()); //Giver os besked om hvad den mulige fejl er
        }
    }

    // Metode til at indlæse pizza menuen fra en vores menu.txt fil
    private void loadMenuFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String Linje;
            while ((Linje = reader.readLine()) != null) {
                String[] pizzaData = Linje.split(","); //Gør kommaet til en opdeler
                if (pizzaData.length == 3) { //Sikrer sig at pizzaData arrayet har 3 elementer(nummer, navn og pris)
                    String nummer = pizzaData[0];
                    String navn = pizzaData[1];
                    int pris = Integer.parseInt(pizzaData[2]);

                    Pizza pizza = new Pizza(nummer, navn, pris);  //Tilføjer den nye pizza til menuen, hvis formatet er overholdt
                    pizzaMenu.add(pizza);
                }
            }
            pizzaMenu.sort((p1, p2) -> Integer.compare(Integer.parseInt(p1.getNummer()), Integer.parseInt(p2.getNummer())));
        }
        catch (IOException e) {
            System.out.println("Fejl ved læsning fra fil: " + e.getMessage()); //Giver os besked om hvad den mulige fejl er
        }
    }

    //Ændrer prisen på den valgte pizza
    public void opdaterPizzaPris(String nummer, int nyPris) {
        for (int i = 0; i < pizzaMenu.size(); i++){
            Pizza pizza = pizzaMenu.get(i); //Går menuen igennem, så programmet kan se at prisen er opdateret
            if (pizza.getNummer().equals(nummer)) {
                pizza.setPris(nyPris);
                saveMenuToFile();  // Opdaterer txt filen efter ændring af pris
                System.out.println("Prisen for pizzaen " + pizza.getNavn() + " er opdateret til " + nyPris + " kr.");
                return;
            }
        }
        System.out.println("Pizza med nummer " + nummer + " blev ikke fundet.");
    }

    public void saveOrderToFile() {
        this.setAktiv(false);
        this.writeToFile();
    }
*/


}
