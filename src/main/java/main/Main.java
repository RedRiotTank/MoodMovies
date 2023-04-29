package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;


/**
 * @brief test brief
 * @note test note.
 * @author test auth.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        int portNumber = 8080; // Puerto en el que se va a escuchar

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Servidor iniciado. Esperando conexiones entrantes...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;

                    if (in.readLine().startsWith("POST")) {
                        System.out.println("Processing POST request...");
                        StringBuilder requestBody = new StringBuilder();
                        while (in.ready()) { // Leer todo el cuerpo de la solicitud
                            requestBody.append((char) in.read());
                        }
                        String[] formData = requestBody.toString().split("&"); // Separar los datos del formulario
                        System.out.printf(formData[1] );

                    }
                    else if (in.readLine().startsWith("GET")) {
                        System.out.println("Processing GET request...");
                    }


                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("");
                out.println("<html><body><h1>Hello, World!</h1></body></html>");

                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor en el puerto " + portNumber);
            e.printStackTrace();
        }

    }
}
