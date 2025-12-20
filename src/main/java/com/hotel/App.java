package com.hotel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            // Imprimeix el menú:
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            // El switch està dins de gestionarOpció. Si ix, torna ací.
            gestionarOpcio(opcio);
            // I torna a preguntar mentre l'usuari no introduïsca un 6.
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Consultar dades d'una reserva");
        System.out.println("5. Consultar reserves per tipus");
        System.out.println("6. Eixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
        int opcioEscollida = opcio;

        switch (opcioEscollida) {
            case 1:
                reservarHabitacio();
                break;

            case 2:
                alliberarHabitacio();
                break;

            case 3:
                consultarDisponibilitat();
                break;

            case 4:
                obtindreReserva();
                break;

            case 5:
                obtindreReservaPerTipus();
                break;

            case 6:

                break;

            default:
                // En cas de sel·leccionar un número invàlid, imprimeix per pantalla un missatge
                // d'error i torna al menú.
                System.out.println("Opció no vàlida.");
                break;
        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        /**
         * Inicialitza totes les variables que ha de contindre la reserva.
         */
        Integer codiReserva = 0;
        String habitacio = "";
        ArrayList<String> serveis = new ArrayList<>();
        Float preuTotal = 0f;
        ArrayList<String> contingutReserva = new ArrayList<>();

        /**
         * Pregunta quin tipus d'habitació es dessitja i suma el preu al preu de la
         * reserva.
         */
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        habitacio = seleccionarTipusHabitacioDisponible();

        /**
         * Pregunta si es volen serveis i suma al preu de l'habitació el preu dels
         * serveis si s'han sel·leccionat.
         * 
         */
        serveis = seleccionarServeis();

        /**
         * Crida al mètode calcularPreuTotal per a mostrar el preu desglossat i retornar
         * el preu final amb IVA.
         */
        preuTotal = calcularPreuTotal(habitacio, serveis);

        /**
         * S'emplena l'ArrayList contingutReserva amb el tipus d'habitació, preu final i
         * serveis sel·leccionats de la reserva. Estes dades aniran vinculades al codi
         * de la reserva en un HashMap.
         */

        contingutReserva.add(habitacio);
        String preuTotalString = preuTotal.toString();
        contingutReserva.add(preuTotalString);

        if (!serveis.isEmpty()) {
            for (String valor : serveis) {
                contingutReserva.add(valor);
            }
        }

        /**
         * Crida a la funció generarCodiReserva per a generar el codi únic de la reserva
         * actual.
         */
        codiReserva = generarCodiReserva();

        /**
         * Afegeix la reserva completa al HashMap i resta 1 a la disponibilitat d'eixe
         * tipus d'habitació.
         */
        reserves.put(codiReserva, contingutReserva);

        Integer novaDisponibilitat = (disponibilitatHabitacions.get(habitacio) - 1);
        disponibilitatHabitacions.put(habitacio, novaDisponibilitat);

        System.out.println("Reserva creada amb èxit!");
        System.out.println("Codi de reserva: " + codiReserva);

    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i retorna el nom
     * del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        int tipusHabitacio = -1;
        String nomTipusHabitacio = "";
        do {
            System.out.println("\nSel·leccione un tipus d'habitació:");
            System.out.println("1: Estàndard");
            System.out.println("2: Suite");
            System.out.println("3: Deluxe");
            tipusHabitacio = sc.nextInt();
            sc.nextLine();
            switch (tipusHabitacio) {
                case 1:
                    nomTipusHabitacio = TIPUS_ESTANDARD;
                    break;

                case 2:
                    nomTipusHabitacio = TIPUS_SUITE;
                    break;

                case 3:
                    nomTipusHabitacio = TIPUS_DELUXE;
                    break;

                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
        } while (tipusHabitacio != 1 && tipusHabitacio != 2 && tipusHabitacio != 3);
        return nomTipusHabitacio;
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        String habitacio = null;

        /**
         * Consulta la disponibilitat (HashMap disponibilitatHabitacions) i el preu
         * (HashMap preusHabitacions) de les habitacions.
         */
        System.out.println("\nTipus d'habitació disponibles:");
        System.out.println("1. " + TIPUS_ESTANDARD + " - " + disponibilitatHabitacions.get(TIPUS_ESTANDARD)
                + " disponibles - " + preusHabitacions.get(TIPUS_ESTANDARD));
        System.out.println("2. " + TIPUS_SUITE + " - " + disponibilitatHabitacions.get(TIPUS_SUITE) + " disponibles - "
                + preusHabitacions.get(TIPUS_SUITE));
        System.out.println("3. " + TIPUS_DELUXE + " - " + disponibilitatHabitacions.get(TIPUS_DELUXE)
                + " disponibles - " + preusHabitacions.get(TIPUS_DELUXE));

        /**
         * Aquesta línia es una crida a la funció + assignació del valor retornat al
         * String habitacioSeleccionada.
         */
        String habitacioSeleccionada = seleccionarTipusHabitacio();

        /**
         * Si l'habitació sel·leccionada està disponible, la funció retorna el String
         * amb eixe valor. Si no, roman com a null.
         */
        if ((disponibilitatHabitacions.get(habitacioSeleccionada)) > 0) {
            System.out.println("Ha sel·leccionat " + habitacioSeleccionada);
            habitacio = habitacioSeleccionada;
        }

        return habitacio;
    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        /**
         * Inicialitza un ArrayList per als possibles serveis sel·leccionats i dos
         * String per a les respostes de si vol afegir un servei i quin servei.
         */
        ArrayList<String> serveisSeleccionats = new ArrayList<>();
        String respostaAfegir = "";
        int respostaServei = -1;

        System.out.println("Serveis addicionals:");
        System.out.println("1. Esmorzar (10€)");
        System.out.println("2. Gimnàs (15€)");
        System.out.println("3. Spa (20€)");
        System.out.println("4. Piscina (25€)");

        do {
            System.out.println("Vol afegir un servei? (s/n):");
            respostaAfegir = sc.nextLine();

            /**
             * Comprova que la entrada de l'usuari siga vàlida. Si no, repeteix la pregunta.
             */
            if (!respostaAfegir.equalsIgnoreCase("s") && !respostaAfegir.equalsIgnoreCase("n")) {
                do {
                    System.out.println("Opció no vàlida.");
                    System.out.println("Vol afegir un servei? (s/n):");
                    respostaAfegir = sc.nextLine();
                } while (!respostaAfegir.equalsIgnoreCase("s") && !respostaAfegir.equalsIgnoreCase("n"));

                /**
                 * Si l'usuari diu "n", la funció retorna null. Si diu "s", continua preguntant.
                 */
            } else if (respostaAfegir.equalsIgnoreCase("n")) {

                respostaServei = 0;

            } else if (respostaAfegir.equalsIgnoreCase("s")) {
                System.out.println("Sel·leccione un servei (o 0 per a finalitzar):");
                respostaServei = sc.nextInt();
                sc.nextLine();

                /**
                 * Afegeix el servei si no està ja sel·leccionat. Si la resposta és 0, ix del
                 * bucle.
                 */
                switch (respostaServei) {
                    case 0:

                        break;

                    case 1:
                        if (serveisSeleccionats.contains(SERVEI_ESMORZAR)) {
                            System.out.println("Aquest servei ja s'ha afegit.");
                        } else {
                            serveisSeleccionats.add(SERVEI_ESMORZAR);
                            System.out.println("Servei " + SERVEI_ESMORZAR + " afegit.");
                        }
                        break;

                    case 2:
                        if (serveisSeleccionats.contains(SERVEI_GIMNAS)) {
                            System.out.println("Aquest servei ja s'ha afegit.");
                        } else {
                            serveisSeleccionats.add(SERVEI_GIMNAS);
                            System.out.println("Servei " + SERVEI_GIMNAS + " afegit.");
                        }
                        break;

                    case 3:
                        if (serveisSeleccionats.contains(SERVEI_SPA)) {
                            System.out.println("Aquest servei ja s'ha afegit.");
                        } else {
                            serveisSeleccionats.add(SERVEI_SPA);
                            System.out.println("Servei " + SERVEI_SPA + " afegit.");
                        }
                        break;

                    case 4:
                        if (serveisSeleccionats.contains(SERVEI_PISCINA)) {
                            System.out.println("Aquest servei ja s'ha afegit.");
                        } else {
                            serveisSeleccionats.add(SERVEI_PISCINA);
                            System.out.println("Servei " + SERVEI_PISCINA + " afegit.");
                        }
                        break;

                    default:
                        System.out.println("Opció no vàlida.");
                        break;
                }
            }

        } while (respostaServei != 0);

        return serveisSeleccionats;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {

        System.out.println("Calculem el total...");

        float preuSenseIVA = 0f;
        float preuTotal = 0f;

        /**
         * Busca el preu de l'habitació i el mostra per pantalla.
         */
        float preuHab = preusHabitacions.get(tipusHabitacio);

        System.out.println("Preu habitació: " + preuHab);

        /**
         * Si s'han afegit serveis, suma el seu preu i el mostra.
         */
        float preuServeis = 0f;
        if (!serveisSeleccionats.isEmpty()) {
            for (String valor : serveisSeleccionats) {
                preuServeis = preuServeis + preusServeis.get(valor);
            }
            System.out.println("Serveis: ");
            for (String valor : serveisSeleccionats) {
                System.out.println("- " + valor + " (" + preusServeis.get(valor) + "€)");
            }
        }

        preuSenseIVA = preuHab + preuServeis;
        preuTotal = preuSenseIVA + (preuSenseIVA * 0.21f);

        System.out.println("Subtotal: " + preuSenseIVA + "€");
        System.out.println("IVA (21%): " + (preuSenseIVA * 0.21) + "€");
        System.out.println("TOTAL: " + preuTotal + "€");

        return preuTotal;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        /**
         * La funció random genera un codi entre 0 i 899, al qual es suma 100 per a que
         * menor que 1000. Després, torna eixe int com a codiReserva.
         */
        int codiReserva = 100 + random.nextInt(900);
        return codiReserva;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");

        int codi = -1;

        System.out.println("Introdueix el codi de la reserva: ");
        codi = sc.nextInt();
        sc.nextLine();

        /**
         * Si la reserva existeix, actualitza la disponibilitat d'eixe tipus d'habitació
         * i elimina la reserva.
         */

        if (!reserves.get(codi).isEmpty()) {
            System.out.println("Reserva trobada!");

            ArrayList<String> dadesReserva = reserves.get(codi);
            String habitacio = dadesReserva.get(0);

            Integer novaDisponibilitat = (disponibilitatHabitacions.get(habitacio) + 1);
            disponibilitatHabitacions.put(habitacio, novaDisponibilitat);

            reserves.remove(codi);

            System.out.println("Habitació alliberada correctament.");
            System.out.println("Disponibilitat actualitzada.");
        }

    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        System.out.println("===== DISPONIBILITAT D'HABITACIONS =====");
        String tipusHabitacio = "";
        System.out.println("Tipus" + "\t" + "Lliures" + "\t" + "Ocupades");
        for (Map.Entry<String, Integer> habitacio : disponibilitatHabitacions.entrySet()) {
            tipusHabitacio = habitacio.getKey();
            mostrarDisponibilitatTipus(tipusHabitacio);
        }
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
        // TODO: Implementar recursivitat
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");

        int codi = -1;
        boolean codiValid = false;
        /**
         * Demana el codi de la reserva i comprova que siga un enter de tres xifres. Si
         * és correcte, s'envia a la funció mostrarDadesReserva. Si no, torna a demanar
         * un codi.
         */
        while (codiValid == false) {
            System.out.println("Introdueix el codi de reserva:");
            if (sc.hasNextInt()) {
                codi = sc.nextInt();
                if (codi > 99 && codi < 1000) {
                    codiValid = true;
                } else {
                    System.out.println("Codi no vàlid. Per favor, introduïsca un codi numèric de tres xifres.");
                }
            } else {
                System.out.println("Codi no vàlid. Per favor, introduïsca un codi numèric de tres xifres.");
                sc.next();
            }
        }
        sc.nextLine();
        mostrarDadesReserva(codi);
    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        // TODO: Llistar reserves per tipus
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {

        /**
         * Consulta el HashMap reserves i mostra la informació si existeix una reserva
         * amb eixe codi. Si no, mostra un missatge d'error.
         */
        if (reserves.containsKey(codi)) {
            ArrayList<String> dadesReserva = new ArrayList<>();
            dadesReserva = reserves.get(codi);

            System.out.println("Dades de la reserva:");
            System.out.println(" -Tipus d'habitació: " + dadesReserva.get(0));
            System.out.println(" -Cost total: " + dadesReserva.get(1) + "€");
            /**
             * Comprova si l'ArrayList de la reserva conté més de dos posicions. En cas
             * afirmatiu vol dir que la reserva inclou serveis addicionals i recorre les
             * seues posicions mostrant-los.
             */
            if (dadesReserva.size() > 2) {
                System.out.println("Serveis addicionals:");
                for (int i = 2; i < dadesReserva.size(); i++) {
                    System.out.println(" -" + dadesReserva.get(i));
                }
            }
        } else {
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");
        }
    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        // Reb el text (missatge) que se li passa a la funció ("Seleccione una opció: ")
        int valor = 0;
        boolean correcte = false;
        // Mentre correcte siga true:
        while (!correcte) {
            // Imprimeix el text que se li passa a la funció ("Seleccione una opció: "):
            System.out.print(missatge);
            // Si l'usuari introdueix un int, el boolean passarà a true i s'enviarà eixe
            // int.
            // Al posar .nextInt(), si s'escriu una lletra per exemple, dona error.
            valor = sc.nextInt();
            correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
