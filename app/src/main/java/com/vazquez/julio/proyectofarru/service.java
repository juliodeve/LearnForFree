package com.vazquez.julio.proyectofarru;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by julio on 06/02/2018.
 */

public class service {
    public String enviarPost(String mail, String pass, String uri) {
        String parametros = "mail=" + mail + "&pass=" + pass;
        HttpURLConnection connection = null;
        String respuesta = "";
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());
            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

    public String enviarPost(String uri) {
        HttpURLConnection connection = null;
        String respuesta = "";
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "");
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());
            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

}
