
package Objetos;

public class Sensor {
    public String id_estacion = "";
    public String ciudad = "";
    public Float porcentaje_humedad = 0f;
    public Float temperatura = 0f;
    public Float velocidad_viento = 0f;
    public String fecha = ""; //En formato AAAAMMDD
    public String hora = "";  //En formato HHMMSS
    
    public Sensor(String id_estacion, String ciudad, 
        Float porcentaje_humedad, Float temperatura, 
        Float velocidad_viento, String fecha, 
        String hora) {
        
        this.id_estacion = id_estacion;
        this.ciudad = ciudad;
        this.porcentaje_humedad = porcentaje_humedad;
        this.temperatura = temperatura;
        this.velocidad_viento = velocidad_viento;
        this.fecha = fecha;
        this.hora = hora;
    }
    
}
