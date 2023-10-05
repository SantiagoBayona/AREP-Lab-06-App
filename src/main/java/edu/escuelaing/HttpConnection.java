package edu.escuelaing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String[] SERVERS = new String[]{"http://logger-service1:4568", "http://logger-service2:4568", "http://logger-service3:4568"};

    private static int curretServer = 0;

    public static String logMessage(String msg) throws IOException {
        int server = curretServer;
        curretServer = (curretServer + 1) % 3;
        return getUrlResponse(SERVERS[server] + "/logService?value=" + msg);
    }

    public static String getUrlResponse(String url) throws IOException {

        URL obj = new URL(url);
        System.out.println(obj);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        StringBuffer response = new StringBuffer();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();

        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        return null;
    }

}