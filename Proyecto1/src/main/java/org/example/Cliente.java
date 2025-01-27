package org.example;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try {
            System.out.println("Introduce la clave AES compartida:");
            BufferedReader keyReader = new BufferedReader(new InputStreamReader(System.in));
            String keyStr = keyReader.readLine();
            SecretKey aesKey = AESUtils.stringToKey(keyStr);

            try (Socket socket = new Socket("localhost", 1234)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

                String userInput;
                System.out.println("Ingrese un mensaje: ");
                while ((userInput = stdIn.readLine()) != null) {
                    String mensajeCifrado = AESUtils.encrypt(userInput, aesKey);
                    out.println(mensajeCifrado);

                    String respuestaCifrada = in.readLine();
                    String respuestaDescifrada = AESUtils.decrypt(respuestaCifrada, aesKey);
                    System.out.println("Servidor: " + respuestaDescifrada);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
