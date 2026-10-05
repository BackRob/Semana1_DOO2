package model;

import java.time.LocalDate;
import java.time.LocalTime;

//semana 7, representa la tabla entrega (relacion entre pedido y repartidor)
public class Entrega {
    private int id;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    //constructor
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    //sets
    public void setId(int id) {this.id = id;}

    //gets
    public int getId() {return id;}
    public int getIdPedido() {return idPedido;}
    public int getIdRepartidor() {return idRepartidor;}
    public LocalDate getFecha() {return fecha;}
    public LocalTime getHora() {return hora;}

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
