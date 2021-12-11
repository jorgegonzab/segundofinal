
package Cliente;

import Objetos.MensajeCliente;
import Objetos.MensajeServer;
import Objetos.Sensor;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.simple.JSONObject;

public class Cliente {

 public static void main(String[] args) throws IOException {

        Socket unSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        Scanner lectura = new Scanner(System.in);
        String id_cliente = "AAAA";
        try {
            unSocket = new Socket("localhost", 4444);
            // enviamos nosotros
            out = new PrintWriter(unSocket.getOutputStream(), true);

            //viene del servidor
            in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        }

        
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        MensajeCliente mensaje = new MensajeCliente();

        String fromServer;
        String fromUser;
        Boolean continuar = true;
        JSONObject objetoAux;
        while (continuar) {
            menu();
            System.out.println("Opcion: ");
            Integer opcion = lectura.nextInt();
            lectura.nextLine();
            switch(opcion) {
                case 1:
                    // Creamos un nuevo registro para enviar al server.
                        System.out.println("Ingresar datos a enviar: ");
                        System.out.println("ID Estacion: ");
                        String id_estacion = lectura.nextLine();
                        System.out.println("Ciudad: ");
                        String ciudad = lectura.nextLine();
                        System.out.println("Porcentaje de Humedad: ");
                        Float porcentaje_humedad = lectura.nextFloat();
                        System.out.println("Temperatura: ");
                        Float temperatura = lectura.nextFloat();
                        System.out.println("Velocidad del Viento: ");
                        Float velocidad_viento = lectura.nextFloat();
                        lectura.nextLine();
                        System.out.println("Fecha (AAAAMMDD): ");
                        String fecha = lectura.nextLine();
                        System.out.println("Hora (HHMMSS): ");
                        String hora = lectura.nextLine();
                        Sensor auxSensor = new Sensor(id_estacion, ciudad,
                        porcentaje_humedad, temperatura, velocidad_viento, 
                        fecha, hora);
                        fromUser = mensaje.createJSONString(id_cliente, auxSensor, opcion);
                        out.println(fromUser);
                        fromUser="";
                    break;
                case 2:
                    System.out.println("Ciudad a Consultar: ");
                    String ciudadConsulta = lectura.nextLine();
                    fromUser = mensaje.createJSONString(id_cliente, opcion, ciudadConsulta);
                    out.println(fromUser);
                    fromUser="";
                    // Esperamos la respuesta del server
                    fromServer = in.readLine();
                    if (fromServer == null) {
                        System.out.println("Mensaje no recibido...");
                        System.out.println("Fin de la ejecucion...");
                        System.exit(0);
                    }
                    JSONObject aux1 = mensaje.mensajeRecibido(fromServer);
                    System.out.println("--------------------------------");
                    System.out.println("Ciudad: " + (String) aux1.get("ciudad")+ 
                    "\nTemperatura: " + ((Double) aux1.get("temperatura")).floatValue());
                    System.out.println("--------------------------------");
                    fromServer="";
                    break;
                case 3:
                    System.out.println("Fecha a Consultar (formato AAAAMMDD): ");
                    String fechaConsulta = lectura.nextLine();
                    fromUser = mensaje.createJSONString(id_cliente, opcion, fechaConsulta);
                    out.println(fromUser);
                    // Esperamos la respuesta del server
                    fromServer = in.readLine();
                    JSONObject aux2 = mensaje.mensajeRecibido(fromServer);
                    System.out.println("--------------------------------");
                    System.out.println("Promedio Temp.: " + (Double) aux2.get("temperatura_promedio"));
                    System.out.println("--------------------------------");
                    break;
                case 4:
                    System.out.println("Finalizando cliente...");
                    fromUser = mensaje.createJSONString(id_cliente, opcion, "Cliente finalizado");
                    out.println(fromUser);
                    fromUser="";
                    continuar=false;
                    break;
                default:
                    System.out.println("Operacion No Soportada!!");
            }
        }
        
        out.close();
        in.close();
        stdIn.close();
        unSocket.close();
    }
 
    public static void menu() {
        System.out.println(" 1 - Agregar registro");
        System.out.println(" 2 - Consultar por una ciudad en especifico");
        System.out.println( "3 - Promedio en fecha especifica");
        System.out.println(" 4 - Salir...");
    }
}