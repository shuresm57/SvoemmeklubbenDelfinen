import java.io.*;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileUtil {


    public static void readEksisterendeMedlemsNumre(String filePath, List<String> eksisterendeMedlemsNumre) {
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
        List<String> eksisterendeMedlemsNumre = new ArrayList<>();
        readEksisterendeMedlemsNumre(filePath, eksisterendeMedlemsNumre);
        List<Medlem> nyeMedlemmer = new ArrayList<>();
        for (Medlem medlem : liste) {
            if (!eksisterendeMedlemsNumre.contains(medlem.getMedlemsnummer())) {
                nyeMedlemmer.add(medlem);
            }
        }

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
                String traenerNavn = (hold.getHoldnavn() != null) ? hold.getTraener().getNavn() : "Ingen træner";
                writer.write(hold.getHoldnavn() + ";" + traenerNavn + "," + hold.getUgeDag() + "," + hold.getTid() + hold.getDeltagere());
                writer.newLine();
            }
            System.out.println("Hold gemt til fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af hold til fil: " + e.getMessage());
        }
    }

    public static void saveStaevne(String filePath, List<Staevne> staevneListe) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Staevne staevne : staevneListe) {
                writer.write(staevne.getStaevneNavn() + "," + staevne.getDato() + System.lineSeparator());

                List<String> discipliner = staevne.getValgteDiscipliner();
                for (String disciplin : discipliner) {
                    writer.write(disciplin + System.lineSeparator());
                }

                writer.write("[");

                List<String> tider = staevne.getTider();
                for (int i = 0; i < tider.size(); i++) {
                    writer.write(System.lineSeparator() + "    " + tider.get(i));
                }

                writer.write(System.lineSeparator() + "]" + System.lineSeparator());
            }
            System.out.println("Stævne gemt til fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af stævne til fil: " + e.getMessage());
        }
    }

    public static void saveResultat(String filePath, Resultat resultat) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(resultat.getSvoemmer().getMedlemsnummer() + "," + resultat.getDisciplin() + "," +
                    resultat.getTid() + "," + resultat.getDato());
            writer.newLine();
            System.out.println("Resultat gemt: " + resultat.getDisciplin() + " - " + resultat.getTid());
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning af resultat til fil: " + e.getMessage());
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

    public static void laesResultaterFraFil(String filePath, HashMap<KonkurrenceSvoemmer, List<Resultat>> resultatMap) {
        MedlemManagement mm = new MedlemManagement();
        mm.loadMedlemmerFromFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String medlemsnummer = data[0].trim();
                    String disciplin = data[1].trim();
                    double tid = Double.parseDouble(data[2].trim());
                    String dato = data[3].trim();

                    KonkurrenceSvoemmer svoemmer = mm.findKonkurrenceSvoemmerByMedlemsnummer(medlemsnummer);

                    if (svoemmer != null) {

                        Resultat resultat = new Resultat(svoemmer, disciplin, tid, dato);
                        resultatMap.computeIfAbsent(svoemmer, k -> new ArrayList<>()).add(resultat);
                    } else {
                        System.out.println("Svømmer med medlemsnummer " + medlemsnummer + " ikke fundet.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning af resultater fra fil: " + e.getMessage());
        }
    }

    public static void opdaterMedlem(String filePath, Medlem opdateretMedlem) {

        MedlemManagement mm = new MedlemManagement();
        mm.loadMedlemmerFromFile();
        List<Medlem> medlemsListe = mm.getMedlemmer();
        boolean medlemFundet = false;


        for (int i = 0; i < medlemsListe.size(); i++) {
            Medlem medlem = medlemsListe.get(i);
            if (medlem.getMedlemsnummer().equalsIgnoreCase(opdateretMedlem.getMedlemsnummer())) {
                medlemsListe.set(i, opdateretMedlem);
                medlemFundet = true;
                break;
            }
        }

        if (!medlemFundet) {
            System.out.println("Medlem med medlemsnummer " + opdateretMedlem.getMedlemsnummer() + " blev ikke fundet.");
            return;
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Medlem medlem : medlemsListe) {
                String linje = String.format("%s,%s,%s,%s,%s,%s",
                        medlem.getMedlemsnummer(), medlem.getMedlemstype(),
                        medlem.getNavn(), medlem.getFoedselsdato(),
                        medlem.getTelefon(), medlem.getEmail());
                writer.write(linje);
                writer.newLine();
            }
            System.out.println("Medlem med medlemsnummer " + opdateretMedlem.getMedlemsnummer() + " er blevet opdateret.");
        } catch (IOException e) {
            System.out.println("Fejl ved opdatering af medlem i fil: " + e.getMessage());
        }
    }

    public static void opdaterHold(String filePath, Hold opdateretHold) {
        Hold hold = new Hold();
        hold.loadHoldFromFile();
        List<Hold> holdListe = hold.getHoldListe();
        boolean holdFundet = false;

        for (int i = 0; i < hold.getHoldListe().size(); i++) {
            Hold hold1 = hold.getHoldListe().get(i);
            if (hold.getHoldnavn().equalsIgnoreCase(opdateretHold.getHoldnavn())) {
                holdListe.set(i, opdateretHold);
                holdFundet = true;
                break;
            }
        }

        if (!holdFundet) {
            System.out.println("Hold med navnet " + opdateretHold.getHoldnavn() + " blev ikke fundet.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Hold hold1 : holdListe) {
                String linje = String.format("%s,%s,%s,%s,%s",
                        hold.getHoldnavn(), hold.getTraener(),
                        hold.getUgeDag(), hold.getTid(),
                        hold.getDeltagere());
                writer.write(linje);
                writer.newLine();
            }
            System.out.println("Hold med navnet: " + opdateretHold.getHoldnavn() + " er blevet opdateret.");
        } catch (IOException e) {
            System.out.println("Fejl ved opdatering af hold i fil: " + e.getMessage());
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
                            case "Motionist" -> new Motionist(medlemsnummer, navn, foedselsdato, telefon, email);
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

    public static void loadHoldFromFile(String filePath, List<Hold> holdListe) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linje;

            while ((linje = reader.readLine()) != null) {
                    String[] data = linje.split("!");
                    String holdnavn = data[0];
                    System.out.println(holdnavn + "\n");

            }
        } catch (IOException e) {
            throw new RuntimeException("Fejl ved indlæsning af fil: " + e.getMessage(), e);
        }
    }

    public static void loadTraenerFromFile(String filePath, List<Traener> traenerListe) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String navn = data[0];
                    String alder = data[1];
                    String telf = data[2];
                    String email = data[3];

                    Traener traener = new Traener(navn, alder, telf, email);
                    traenerListe.add(traener);
                }
            }
            System.out.println("Trænere indlæst fra fil.");
        } catch (IOException e) {
            System.out.println("Fejl ved indlæsning af trænere fra fil: " + e.getMessage());
        }
    }

    public static void loadStaevne(String filSti, List<Staevne> staevneListe) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filSti))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] headerData = line.split(",", 2);
                if (headerData.length < 2) continue;

                String navn = headerData[0].trim();
                String dato = headerData[1].trim();

                String disciplinerLine = reader.readLine().trim();
                List<String> discipliner = new ArrayList<>();
                for (String disciplin : disciplinerLine.replace("[", "").replace("]", "").split(",")) {
                    disciplin = disciplin.trim();
                    if (!disciplin.isEmpty()) {
                        discipliner.add(disciplin);
                    }
                }

                List<String> deltagere = new ArrayList<>();
                String tiderLine;
                while ((tiderLine = reader.readLine()) != null && !tiderLine.equals("]")) {
                    String trimmedLine = tiderLine.trim();
                    if (!trimmedLine.isEmpty()) {
                        deltagere.add(trimmedLine);
                    }
                }

                Staevne currentStaevne = new Staevne(navn, dato, discipliner, deltagere);
                staevneListe.add(currentStaevne);
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning fra fil: " + e.getMessage());
        }
    }

    public static void sletMedlem(String filePath) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Indtast medlemsnummer på medlemmet, der skal slettes: ");
        String medlemsnummer = scanner.nextLine();
        File inputfil = new File(filePath);
        File tempfil = new File("tempfil.txt");

        boolean medlemFundet = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempfil))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().contains(medlemsnummer.toLowerCase())) {
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
                System.out.println("Medlemmet med medlemsnummer " + medlemsnummer + " er blevet slettet.");
            } else {
                System.out.println("Kunne ikke opdatere medlemsfilen.");
            }
        } else {
            tempfil.delete();
            System.out.println("Medlemsnummeret blev ikke fundet.");
        }
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
}