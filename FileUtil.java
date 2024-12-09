import java.io.*;
import java.util.*;

public class FileUtil {

    public static void readEksisterendeMedlemsNumre(String filePath, Set<String> eksisterendeMedlemsNumre) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linje;
            while ((linje = reader.readLine()) != null) {
                String[] data = linje.split(",");
                if (data.length > 0) {
                    eksisterendeMedlemsNumre.add(data[0]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Filen blev ikke fundet. Fortsætter med at tilføje nye medlemmer.");
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af fil: " + e.getMessage());
        }
    }

    public static void saveMedlemmer(String filePath, List<Medlem> liste, boolean tjekMedlemsNr) {
        Set<String> eksisterendeMedlemsNumre = new HashSet<>();
        readEksisterendeMedlemsNumre(filePath, eksisterendeMedlemsNumre);

        List<Medlem> nyeMedlemmer = liste.stream()
                .filter(m -> !eksisterendeMedlemsNumre.contains(m.getMedlemsnummer()))
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Medlem medlem : nyeMedlemmer) {
                String linje = medlem.getMedlemsnummer() + "," + medlem.getMedlemstype() + "," + medlem.getNavn() +
                        "," + medlem.getFoedselsdato() + "," + medlem.getTelefon() + "," + medlem.getEmail();
                if (tjekMedlemsNr) {
                    linje += "," + medlem.getMedlemsdato();
                }
                writer.write(linje);
                writer.newLine();
                System.out.println(medlem.getMedlemsnummer() + " - " + medlem.getNavn() + " er blevet gemt.");
            }
        } catch (IOException e) {
            System.out.println("Fejl ved gemning af medlemmer: " + e.getMessage());
        }
    }

    public static void saveHold(String filePath, List<Hold> holdListe) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Hold hold : holdListe) {
                String traenerNavn = (hold.getTraener() != null) ? hold.getTraener().getNavn() : "Ingen træner";
                writer.write(hold.getHoldnavn() + "," + traenerNavn + "," + hold.getUgeDag() + "," + hold.getTid());
                writer.newLine();
                List<String> eksisterendeNavne = new ArrayList<>();
                for (KonkurrenceSvoemmer deltagere : hold.getDeltagere()) {
                    if (!eksisterendeNavne.contains(deltagere.getNavn())) {
                        writer.write(deltagere.getNavn());
                        writer.newLine();
                        eksisterendeNavne.add(deltagere.getNavn());
                    }
                }
            }
            System.out.println("Hold gemt til fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af hold til fil: " + e.getMessage());
        }
    }

    public static void writeTraenerToFile(String filePath, List<Traener> traenerListe) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Traener traener : traenerListe) {
                writer.write(traener.getNavn() + "," + traener.getAlder() + "," + traener.getTelefon() + "," + traener.getEmail());
                writer.newLine();
            }
            System.out.println("Træner(e) gemt til fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af trænere til fil: " + e.getMessage());
        }
    }

    public static void loadMedlemmerFromFile(String filePath, int listeLength, List<Medlem> medlemsListe, List<String> medlemsNumre) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linje;
            while ((linje = reader.readLine()) != null) {
                String[] medlemsData = linje.split(",");
                if (medlemsData.length == listeLength) {
                    String medlemsnummer = medlemsData[0];
                    String medlemstype = medlemsData[1];
                    String navn = medlemsData[2];
                    String foedselsdato = medlemsData[3];
                    String telefon = medlemsData[4];
                    String email = medlemsData[5];
                    Medlem medlem = null;
                    if (listeLength == 7) {
                        String medlemsdato = medlemsData[6];
                        medlem = switch (medlemstype) {
                            case "KonkurrenceSvoemmer" ->
                                    new KonkurrenceSvoemmer(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                            case "Motionist" ->
                                    new Motionist(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                            case "PassivtMedlem" ->
                                    new PassivtMedlem(medlemsnummer, navn, foedselsdato, telefon, email, medlemsdato);
                            default -> medlem;
                        };
                    } else {
                        medlem = switch (medlemstype) {
                            case "KonkurrenceSvoemmer" ->
                                    new KonkurrenceSvoemmer(medlemsnummer, navn, foedselsdato, telefon, email);
                            case "Motionist" ->
                                    new Motionist(medlemsnummer, navn, foedselsdato, telefon, email);
                            case "PassivtMedlem" ->
                                    new PassivtMedlem(medlemsnummer, navn, foedselsdato, telefon, email);
                            default -> medlem;
                        };
                    }

                    if (medlem != null) {
                        medlemsListe.add(medlem);
                        if (medlemsNumre != null) {
                            medlemsNumre.add(medlemsnummer);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning fra fil: " + e.getMessage());
        }
    }

    public static void loadHoldFromFile (String filePath, List<Hold> holdListe) {

        Traener traener = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Hold currentHold = null;

            // Sørg for, at traener er initialiseret korrekt
            if (traener == null) {
                traener = new Traener();
                traener.loadFromFile(); // Indlæs trænere
            }

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Traener matchedTraener = null;

                    // Find træneren i trænerlisten
                    for (Traener t : traener.getTraenerListe()) {
                        if (t.getNavn().equals(data[1])) {
                            matchedTraener = t;
                            break;
                        }
                    }

                    // Opret nyt hold med matched træner
                    currentHold = new Hold(data[0], matchedTraener, data[2], Integer.parseInt(data[3]));
                    holdListe.add(currentHold);
                } else if (currentHold != null) {
                    // Tilføj deltagere til holdet
                    KonkurrenceSvoemmer deltagere = new KonkurrenceSvoemmer(line, "", "", "", "", "");
                    currentHold.getDeltagere().add(deltagere);
                }
            }
            System.out.println("Hold indlæst fra fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved indlæsning af hold fra fil: " + e.getMessage());
        }
    }

    public static void loadTraenerFromFile(String filePath, List<Traener> traenerListe) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Traener traener = new Traener(data[0], Integer.parseInt(data[1]), data[2], data[3]);
                    traenerListe.add(traener);
                }
            }
            System.out.println("Trænere indlæst fra fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved indlæsning af trænere fra fil: " + e.getMessage());
        }
    }

    public static void sletMedlem(String filePath) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Indtast medlemsnummer på medlemmet, der skal slettes: ");
        String medlemsnummer = scanner.nextLine(); // Læs medlemsnummer fra brugeren
        File inputfil = new File(filePath);
        File tempfil = new File("tempfil.txt");

        boolean medlemFundet = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempfil))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().contains(medlemsnummer.toLowerCase())) {
                    medlemFundet = true; // Marker medlem som fundet
                    continue; // Spring denne linje over (dvs. slet medlemmet)
                }
                writer.write(currentLine + System.lineSeparator());
            }

        } catch (IOException e) {
            System.out.println("Der opstod en fejl under læsning eller skrivning af filen: " + e.getMessage());
            return;
        }

        if (medlemFundet) {
            // Erstat den gamle fil med den nye
            if (inputfil.delete() && tempfil.renameTo(new File(filePath))) {
                System.out.println("Medlemmet med medlemsnummer " + medlemsnummer + " er blevet slettet.");
            } else {
                System.out.println("Kunne ikke opdatere medlemsfilen.");
            }
        } else {
            tempfil.delete(); // Slet tempfilen, da der ikke var nogen ændringer
            System.out.println("Medlemsnummeret blev ikke fundet.");
        }
        //If kontigent.listen.contains medlemsnr remove.medlem også for medlemlisten i medlemmanagement
    }

    public static void sletHold(String filePath) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Indtast navnet på holdet, der skal slettes: ");
        String holdNavn = scanner.nextLine();
        File inputfil = new File(filePath);
        File tempfil = new File("tempfil.txt");

        boolean medlemFundet = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempfil))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().contains(holdNavn.toLowerCase())) {
                    medlemFundet = true;
                    continue;
                }
                writer.write(currentLine + System.lineSeparator());
            }

        } catch (IOException e) {
            System.out.println("Der opstod en fejl under læsning eller skrivning af filen: " + e.getMessage());
            return;
        }

        if (medlemFundet) {

            if (inputfil.delete() && tempfil.renameTo(new File(filePath))) {
                System.out.println("Holdet med navnet " + holdNavn + " er blevet slettet.");
            } else {
                System.out.println("Kunne ikke opdatere holdfilen.");
            }
        } else {
            tempfil.delete();
            System.out.println("Holdet blev ikke fundet.");
        }
    }

    public static void sletTraener(String filePath) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Indtast navnet på træneren, der skal slettes: ");
        String traenerNavn = scanner.nextLine();
        File inputfil = new File(filePath);
        File tempfil = new File("tempfil.txt");

        boolean traenerFundet = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempfil))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().contains(traenerNavn.toLowerCase())) {
                    traenerFundet = true;
                    continue;
                }
                writer.write(currentLine + System.lineSeparator());
            }

        } catch (IOException e) {
            System.out.println("Der opstod en fejl under læsning eller skrivning af filen: " + e.getMessage());
            return;
        }

        if (traenerFundet) {

            if (inputfil.delete() && tempfil.renameTo(new File(filePath))) {
                System.out.println("Træneren med navnet " + traenerNavn + " er blevet slettet.");
            } else {
                System.out.println("Kunne ikke opdatere trænerfilen.");
            }
        } else {
            tempfil.delete();
            System.out.println("Træneren blev ikke fundet.");
        }
    }
}
