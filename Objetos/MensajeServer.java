package Objetos;

import org.json.simple.*;
import org.json.simple.parser.*;

public class MensajeServer {
    public JSONObject mensajeRecibido(String mensaje) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(mensaje.trim());
        } catch (ParseException e) {
            System.out.println("Error en el parseo...");
        }
        JSONObject objetoAux = (JSONObject) obj;
        return objetoAux;
    }

}
