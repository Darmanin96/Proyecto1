package org.example;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try {
            SecretKey aesKey = AESUtils.generateKey();
            System.out.println("Clave AES (compártela con el cliente): " + AESUtils.keyToString(aesKey));

            try (ServerSocket serverSocket = new ServerSocket(1234)) {
                System.out.println("Servidor en espera de conexiones...");
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String mensajeCifrado;
                while ((mensajeCifrado = in.readLine()) != null) {
                    String mensajeDescifrado = AESUtils.decrypt(mensajeCifrado, aesKey);
                    System.out.println("Cliente: " + mensajeDescifrado);

                    String respuesta = "Mensaje recibido: " + mensajeDescifrado;
                    String respuestaCifrada = AESUtils.encrypt(respuesta, aesKey);
                    out.println(respuestaCifrada);
                }
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
