import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CalcolatriceClient {

    private static final String HOST = "localhost";
    private static final int PORTA = 8808;

    public static void main(String[] args) {

        try (
            Socket socket = new Socket(HOST, PORTA);

            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));

            Scanner scanner = new Scanner(System.in)
        ) {

            System.out.println("=== CALCOLATRICE REMOTA ===");
            System.out.println("Formato: NUMERO OPERAZIONE NUMERO");
            System.out.println("Operazioni: +  -  *  /");
            System.out.println("Scrivi quit per uscire");

            while (true) {

                System.out.print("Calcolo > ");

                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("quit")) {

                    out.println("QUIT");

                    String chiusura = in.readLine();

                    System.out.println(chiusura);

                    break;
                }

                out.println(input);

                String risposta = in.readLine();

                if (risposta == null) {
                    System.out.println("Server disconnesso");
                    break;
                }

                System.out.println(risposta);
            }

        } catch (IOException e) {

            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }
}
