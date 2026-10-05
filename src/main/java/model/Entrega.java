package model;

import java.time.LocalDate;
import java.time.LocalTime;

//representa la tabla entregas (relacion entre pedido y repartidor)
public class Entrega {
    private int id;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    //datos de las otras tablas (JOIN), solo para mostrar en la tabla de la ventana
    private String direccionPedido;
    private String nombreRepartidor;

    //constructor
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    //constructor completo, se usa al leer desde la BD
    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this(idPedido, idRepartidor, fecha, hora);
        this.id = id;
    }

    //sets
    public void setId(int id) {this.id = id;}
    public void setIdPedido(int idPedido) {this.idPedido = idPedido;}
    public void setIdRepartidor(int idRepartidor) {this.idRepartidor = idRepartidor;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}
    public void setHora(LocalTime hora) {this.hora = hora;}
    public void setDireccionPedido(String direccionPedido) {this.direccionPedido = direccionPedido;}
    public void setNombreRepartidor(String nombreRepartidor) {this.nombreRepartidor = nombreRepartidor;}

    //gets
    public int getId() {return id;}
    public int getIdPedido() {return idPedido;}
    public int getIdRepartidor() {return idRepartidor;}
    public LocalDate getFecha() {return fecha;}
    public LocalTime getHora() {return hora;}
    public String getDireccionPedido() {return direccionPedido;}
    public String getNombreRepartidor() {return nombreRepartidor;}

    @Override
    public String toString() {
        return "Entrega{" +
                "id=" + id +
                ", idPedido=" + idPedido +
                ", idRepartidor=" + idRepartidor +
                ", fecha=" + fecha +
                ", hora=" + hora +
                '}';
    }
}
