package Server;

import Objetos.MensajeServer;
import Objetos.Sensor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Server {
    static ArrayList<Sensor> listaSensores = new ArrayList<Sensor>();
    static Socket clientSocket = null;
    
     public static void main(String[] args) {

            try {
                int puertoServidor = 4444;
                int tiempo_procesamiento_miliseg = 2000;
		
		try{
			tiempo_procesamiento_miliseg = Integer.parseInt(args[0]);
		}catch(Exception e1){
			System.out.println("Se omite el argumento, tiempo de procesamiento " + tiempo_procesamiento_miliseg  + ". Ref: " + e1);
		}
		
		
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(puertoServidor);
                } catch (IOException e) {
                    System.err.println("No se puede abrir el puerto: " +puertoServidor+ ".");
                    System.exit(1);
                }
                System.out.println("Puerto abierto: "+puertoServidor+".");
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    System.err.println("Fallo el accept().");
                    System.exit(1);
                }

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                        clientSocket.getInputStream()));
                    
                String inputLine, outputLine;
                Boolean continuar = true;
                MensajeServer mensaje = new MensajeServer();
                JSONObject objetoAux;
                while(continuar) {
                    inputLine = in.readLine();
                    objetoAux=mensaje.mensajeRecibido(inputLine);
                    Integer opcion =  ((Long) objetoAux.get("opcion")).intValue();

                    switch(opcion) {
                        case 1:
                            // Agregar registro
                            String id_estacion = (String) objetoAux.get("id_estacion");
                            String ciudad = (String) objetoAux.get("ciudad");
                            Float porcentaje_humedad =  ((Double) objetoAux.get("porcentaje_humedad")).floatValue();
                            Float temperatura = ((Double) objetoAux.get("temperatura")).floatValue();
                            Float velocidad_viento = ((Double) objetoAux.get("velocidad_viento")).floatValue();
                            String fecha = (String) objetoAux.get("fecha");
                            String hora = (String) objetoAux.get("hora");

                            Sensor aux = new Sensor(id_estacion, ciudad, porcentaje_humedad,
                            temperatura, velocidad_viento, fecha, hora);
                            listaSensores.add(aux);                        
                            break;
                        case 2:
                            // Responder
                            String ciudadBuscar = (String) objetoAux.get("contenido");
                            //Enviamos de respuesta la ultima temperatura de la ciudad.
                            Float temperaturaEnviar = new Server().ultimaMedicion(ciudadBuscar);

                            JSONObject a = new JSONObject();
                            a.put("ciudad", ciudadBuscar);
                            a.put("temperatura", temperaturaEnviar);
                            // Envia al cliente la informacion requerida
                            out.println(a.toJSONString());
                            break;
                        case 3:
                            // Temp. promedio
                            String fechaEspecificada = (String) objetoAux.get("contenido");
                            Float temperaturaPromedio = new Server().temperaturaPromedio(fechaEspecificada);
                            JSONObject b = new JSONObject();
                            b.put("fecha", fechaEspecificada);
                            b.put("temperatura_promedio", temperaturaPromedio);
                            // Envia al cliente la informacion requerida
                            out.println(b.toJSONString());
                            break;
                        case 4:
                            // Finalizar
                            continuar = false;
                            break;
                        default:
                    }
                }
                out.close();
                in.close();
                clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
    
    public Float ultimaMedicion(String ciudad) {
        Float temperatura = null;
        String mayorFecha = "0";
        String mayorHora = "0";
        for (Sensor actual: listaSensores) {
            //Si es la misma ciudad.
            if ((actual.ciudad).equals(ciudad)) {                
                if (Integer.parseInt(actual.fecha) >= Integer.parseInt(mayorFecha)
                        && Integer.parseInt(actual.hora) >= Integer.parseInt(mayorHora)) {
                        mayorFecha = actual.fecha;
                        mayorHora = actual.hora;
                        temperatura = actual.temperatura;
                }
            }
        }   
        return temperatura;
    }
        
    public Float temperaturaPromedio(String fecha) {
        Float promedio = null;
        Float suma = 0f;
        int cantidad = 0;
        for(Sensor actual: listaSensores) {
            if ((actual.fecha).equals(fecha)) {
                suma += actual.temperatura;
                cantidad++;
            }
        }
        if (cantidad!=0) {
            promedio = (Float) suma/cantidad;
        }   
        return promedio;
    }
}