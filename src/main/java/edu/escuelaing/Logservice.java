package edu.escuelaing;

import java.io.IOException;

import static spark.Spark.*;

public class Logservice {

    public static void main(String[] args) {
        port(getPort());
        staticFiles.location("/public");

        get("logService", (req, res) -> {
            String msg = req.queryParams("value").replace(" ", "%20");;
            return logMessage(msg);
        });
    }

    private static String logMessage(String msg) throws IOException {
        return HttpConnection.logMessage(msg);
    }


    private static int getPort() {
        if(System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}