import java.io.*;
import java.net.*;

public class CalcolatriceServer {

    private static final int PORTA = 8808;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {

            System.out.println("Server avviato sulla porta " + PORTA);

            while (true) {

                Socket client = serverSocket.accept();

                System.out.println("Client connesso!");

                gestisciClient(client);
            }

        } catch (IOException e) {
            System.out.println("Errore server: " + e.getMessage());
        }
    }

    private static void gestisciClient(Socket client) throws IOException {

    int contatore = 0;

    try (
        BufferedReader in = new BufferedReader(
            new InputStreamReader(client.getInputStream(), "UTF-8"));

        PrintWriter out = new PrintWriter(
            new OutputStreamWriter(client.getOutputStream(), "UTF-8"), true)
    ) {

        String richiesta;

        while ((richiesta = in.readLine()) != null) {

            if (richiesta.trim().equalsIgnoreCase("QUIT")) {

                out.println("CHIUSURA: Operazioni eseguite: "
                    + contatore + ". Arrivederci!");

                break;
            }

            String risposta = elaboraRichiesta(richiesta);

            if (risposta.startsWith("RISULTATO:")) {
                contatore++;
            }

            out.println(risposta);
        }

    } finally {
        client.close();
        System.out.println("Client disconnesso!");
    }
}
private static String elaboraRichiesta(String richiesta) {

    try {

        String[] parti = richiesta.trim().split("\\s+");

        if (parti.length != 3) {
            return "ERRORE: Formato non valido. Usa: NUMERO OPERAZIONE NUMERO";
        }

        double num1 = Double.parseDouble(parti[0]);
        String operazione = parti[1];
        double num2 = Double.parseDouble(parti[2]);

        if (!Double.isFinite(num1) || !Double.isFinite(num2)) {
            return "ERRORE: Sono ammessi soltanto numeri finiti";
        }

        double risultato = calcola(num1, operazione, num2);

        if (!Double.isFinite(risultato)) {
            return "ERRORE: Risultato non valido";
        }

        return "RISULTATO: " + risultato;

    } catch (NumberFormatException e) {

        return "ERRORE: Formato non valido. Usa: NUMERO OPERAZIONE NUMERO";

    } catch (ArithmeticException | IllegalArgumentException e) {

        return "ERRORE: " + e.getMessage();
    }
}


private static double calcola(double num1, String operazione, double num2) {

    switch (operazione) {

        case "+":
            return num1 + num2;

        case "-":
            return num1 - num2;

        case "*":
            return num1 * num2;

        case "/":

            if (num2 == 0) {
                throw new ArithmeticException("Divisione per zero non consentita");
            }

            return num1 / num2;

        default:
            throw new IllegalArgumentException("Operazione non supportata");
    }
}}
