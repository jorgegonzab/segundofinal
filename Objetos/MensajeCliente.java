package Objetos;

import org.json.simple.*;
import org.json.simple.parser.*;

public class MensajeCliente {
    
    public String createJSONString(String id_cliente, Sensor s, Integer opcion) {
        JSONObject objetoAux =  new JSONObject();
        
        objetoAux.put("id_cliente", id_cliente);
        objetoAux.put("opcion", opcion);
        objetoAux.put("id_estacion", s.id_estacion);
        objetoAux.put("ciudad", s.ciudad);
        objetoAux.put("porcentaje_humedad", s.porcentaje_humedad);
        objetoAux.put("temperatura", s.temperatura);
        objetoAux.put("velocidad_viento", s.velocidad_viento);
        objetoAux.put("fecha", s.fecha);
        objetoAux.put("hora", s.hora);
        
        return objetoAux.toJSONString();
    }
    
    public String createJSONString(String id_cliente, Integer opcion, String mensaje) {
        JSONObject objetoAux = new JSONObject();
        objetoAux.put("id_cliente", id_cliente);
        objetoAux.put("opcion", opcion);
        objetoAux.put("contenido", mensaje);
        
        return objetoAux.toJSONString();
    } 
    
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