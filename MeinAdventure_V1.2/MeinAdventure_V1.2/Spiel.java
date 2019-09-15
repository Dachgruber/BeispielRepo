/**
 * Dies ist die Hauptklasse der Anwendung "MeinAdventureFirstTry".
 * "MeinAdventureFirstTry" ist ein sehr einfaches, textbasiertes
 * Adventure-Game. Ein Spieler kann sich in einer Umgebung bewegen,
 * mehr nicht. Das Spiel sollte auf jeden Fall ausgebaut werden,
 * damit es interessanter wird!
 
 * Zum Spielen muss eine Instanz dieser Klasse erzeugt werden und
 * an ihr die Methode "spielen" aufgerufen werden.
 
 * Diese Instanz dieser Klasse erzeugt und initialisiert alle
 * anderen Objekte der Anwendung: Sie legt alle R�ume und einen
 * Parser an und startet das Spiel. Sie wertet auch die Befehle
 * aus, die der Parser liefert und sorgt f�r ihre Ausf�hrung.
 
 * Gruppe: Max Mustermann, Mini Musterfrau, Willi Wichtig
 * Verantwortlich: Willi Wichtig
 * Version: 1.2
 * Datum: 17.08.2019
 */

class Spiel 
{
    private Parser parser;
    private Spieler spieler;
    private Raum draussen, klassenraum, lehrerzimmer, flur, buero;
        
    /**
     Erzeuge ein Spiel und initialisiere die interne Raumkarte.
     */
    public Spiel() 
    {
        Raum startRaum = raeumeAnlegen();
        spieler = new Spieler("Willi", startRaum);
        parser = new Parser();
    }

    /**
     * Erzeuge alle R�ume und verbinde ihre Ausg�nge miteinander.
     */
    private Raum raeumeAnlegen()
    {         
        // die R�ume erzeugen
        draussen = new Raum("vor dem Haupteingang der Schule");
        klassenraum = new Raum("in einem Klassenraum der Schule");
        lehrerzimmer = new Raum("im Lehrerzimmer der Schule");
        flur = new Raum("im Flur der Schule");
        buero = new Raum("im Verwaltungsb�ro der Schule");
       
        // put items in the room
        klassenraum.addItem(new Item("klassenbuch8b", "Klassenbuch der 8b", 1.1));
        buero.addItem(new Item("klassenbuch9b", "Klassenbuch der 9b", 1.1));
        buero.addItem(new Item("bild", "ein Landschaftsbild", 30.0));
        buero.addItem(new Item("telefon", "ein altes Telefon", 2.0));
        flur.addItem(new Item("keks", "ein magischer Keks", 0.2));
        
        // create a key that will be used to lock the door to the office
        Item key = new Item("schl�ssel", "Schl�ssel zum Sekretariat", 0.5);
        flur.addItem(key);
        
        // die Ausg�nge initialisieren
        // draussen.setAusgang("Norden", flur);
        // flur.setAusgang("S�den", draussen);
        
        new Tuer(draussen, "norden", flur, "s�den",null);
        
        // klassenraum.setAusgang("S�den", flur);
        // flur.setAusgang("Norden", klassenraum);
        new Tuer(flur,"norden",klassenraum,"s�den", null);
        
        // lehrerzimmer.setAusgang("Osten", flur);
        // flur.setAusgang("Westen", lehrerzimmer);
        new Tuer(flur, "westen", lehrerzimmer, "osten",null);
        
        // flur.setAusgang("Osten", buero);
        // buero.setAusgang("Westen", flur);
        new Tuer(buero, "westen", flur, "osten",key);
      
       

        return draussen;  // das Spiel startet draussen
    }

    /**
     * Die Hauptmethode zum Spielen. Sie l�uft bis zum Ende des Spiels
     * in einer Schleife.
     */
    public void spielen() 
    {            
        willkommenstextAusgeben();

        // Die Hauptschleife. Hier lesen wir wiederholt Befehle ein
        // und f�hren sie aus, bis das Spiel beendet wird.
                
        boolean beendet = false;
        while (! beendet) {
            Befehl befehl = parser.liefereBefehl();
            beendet = verarbeiteBefehl(befehl);
            if(spieler.istTot()){
                printVerloren();
                beendet = true;
            }
            if(spieler.getAktuellerRaum() ==buero&&spieler.hatItem("klassenbuch8b")){
                printGewonnen();
                beendet = true;
            }   
        }
        System.out.println("Danke f�rs Spielen. Bis bald! :)");
    }

    /**
     * Einen Begr��ungstext f�r den Spieler ausgeben.
     */
    private void willkommenstextAusgeben()
    {
        System.out.println();
        System.out.println("Willkommen in der Welt von Kaki!");
        System.out.println("Kaki ist ein neues, unglaublich langweiliges Spiel.");
        System.out.println("Tippe 'Hilfe', wenn du Hilfe brauchst.");
        System.out.println();
        System.out.println(spieler.getLangeBeschreibung());
    }

    private void printVerloren()
    {
        System.out.println("\nDu hast verloren!");
    }
    
    private void printGewonnen()
    {
        System.out.println("\nDu hast gewonnen!");       
        
    }
    
    
    /**
     * Verarbeite einen gegebenen Befehl (f�hre ihn aus).
     * Wenn der Befehl das Spiel beendet, wird 'true' zur�ckgeliefert,
     * andernfalls 'false'.
     */
    private boolean verarbeiteBefehl(Befehl befehl) 
    {
        boolean moechteBeenden = false;

        if(befehl.istUnbekannt()) {
            System.out.println("Ich wei� nicht, was du meinst...");
            return false;
        }

        String befehlswort = befehl.getBefehlswort();
        if (befehlswort.equals("hilfe"))
            hilfstextAusgeben();
        else if (befehlswort.equals("gehe"))
            wechsleRaum(befehl);
        else if (befehlswort.equals("beenden")) 
            moechteBeenden = beenden(befehl);
        else if (befehlswort.equals("nimm")) 
            nehmen(befehl);
        else if (befehlswort.equals("ablegen")) 
            ablegen(befehl);
        else if (befehlswort.equals("iss")) 
            essen(befehl);
        else if (befehlswort.equals("laden")) 
            laden(befehl);
        else if (befehlswort.equals("feuern")) 
            feuern(befehl);    
        
        return moechteBeenden;
    }

    // Implementierung der Benutzerbefehle:

    /**
     * get Hilfsinformationen aus.
     * Hier geben wir eine etwas alberne und unklare Beschreibung
     * aus, sowie eine Liste der Befehlsw�rter.
     */
    private void hilfstextAusgeben() 
    {
        System.out.println("Du befindest dich vor deiner Schule und bist ganz allein.");
        System.out.println("Du suchst das Klassenbuch der 8b, um es in das B�ro zu bringen.");
        System.out.println();
        System.out.println("Dir stehen folgende Befehle zur Verf�gung:");
        parser.zeigeBefehle();
    }

    /**
     * Versuche, den Raum zu wechseln. Wenn es einen Ausgang gibt,
     * wechsele in den neuen Raum, ansonsten gib eine Fehlermeldung
     * aus.
     */
    private void wechsleRaum(Befehl befehl) 
    {
        if(!befehl.hatZweitesWort()) {
            // gett es kein zweites Wort, wissen wir nicht, wohin...
            System.out.println("Wohin m�chtest du gehen?");
            return;
        }

        String richtung = befehl.getZweitesWort();

        // Wir versuchen den Raum zu verlassen.
        Tuer tuer = spieler.getAktuellerRaum().getTuer(richtung);

        if (tuer == null)
            System.out.println("Dort ist keine T�r!");
        else {
            if(spieler.geheDurch(richtung)) {
                System.out.println(spieler.getLangeBeschreibung());
            } 
            else {
                System.out.println("Die T�r ist verschlossen. Du ben�tigst eine passenden Schl�ssel.");
            }                
                        
        }
    }

    /** 
     * Versuche ein Item aus dem aktuellen Raum aufzunehmen.
     * 
     */
    private void nehmen(Befehl befehl) 
    {
        if(!befehl.hatZweitesWort()) {
            // Wenn kein zweites Wort da ist, ...
            System.out.println("Was m�chtest du nehmen?");
            return;
        }

        String itemName = befehl.getZweitesWort();
        Item item = spieler.pickUpItem(itemName);
        
        if(item == null) {
            System.out.println("Du kannst den Gegenstand nicht nehmen: " + itemName);
        } 
        else {
            System.out.println("Aufgenommen: " + item.getBeschreibung());
        }
    }
    
    
    /** 
     * L�sst ein Item im aktuellen Raum fallen.
     */
    private void ablegen(Befehl befehl) 
    {
        if(!befehl.hatZweitesWort()) {
            // Wenn kein zweites Wort da ist, ...
            System.out.println("Was m�chtest du ablegen?");
            return;
        }

        String itemName = befehl.getZweitesWort();
        Item item = spieler.dropItem(itemName);
        
        if(item == null) {
            System.out.println("Du tr�gst nicht: " + itemName);
        } 
        else {
            System.out.println("Abgelegt: " + item.getBeschreibung());
        }
    }
    
    /**
     * Gib die Items aus, die der Spieler aktuell tr�gt.
     */
    private void printItems() 
    {
        System.out.println(spieler.getItemsString());   
    }
    

    private void essen(Befehl befehl) 
    {
        if(!befehl.hatZweitesWort()) {
            // if there is no second word, we don't know what to eat...
            System.out.println("Was m�chtest du essen?");
            return;
        }
        String itemName = befehl.getZweitesWort();
        Item item = spieler.essen(itemName);
        if(item == null) {
            System.out.println("Kann nicht essen: " + itemName);            
        } 
        else {
            System.out.println("Es wurde gegessen: " + item.getBeschreibung());
        }
    }
    
    
    /**
     * "Beenden" wurde eingegeben. �berpr�fe den Rest des Befehls,
     * ob das Spiel wirklich beendet werden soll. Liefere 'true',
     * wenn der Befehl das Spiel beendet, 'false' sonst.
     */
    private boolean beenden(Befehl befehl) 
    {
        if(befehl.hatZweitesWort()) {
            System.out.println("Was soll beendet werden?");
            return false;
        }
        else
            return true;  // Das Spiel soll beendet werden.
    }
    
    
    private void laden(Befehl befehl) 
    {
        spieler.ladeBeamer();
        System.out.println("Beamer geladen.");
    }
    
    
    private void feuern(Befehl befehl) 
    {
        if(spieler.feuerBeamer()) {
            System.out.println("Beamer gefeuert.");
            System.out.println(spieler.getLangeBeschreibung());
        }
        else {
            System.out.println("Beamer ist nicht geladen.");
        }
    }
    
}
