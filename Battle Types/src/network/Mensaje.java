package network;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo; // Por ejemplo, "chat", "comando", "actualizacion", "desafio", etc.
    private String contenido;
	private Object contenidoObjeto;
    private String emisor;
    private String destinatario; // Nuevo campo para mensajes privados

	public Mensaje(String tipo, Object contenido, String emisor){
		this.tipo = tipo;
        this.contenidoObjeto = contenido;
        this.emisor = emisor;
        this.destinatario = null; // Por defecto, sin destinatario
	}
    // Constructor para mensajes sin destinatario (broadcast)
    public Mensaje(String tipo, String contenido, String emisor) {
        this.tipo = tipo;
        this.contenido = contenido;
        this.emisor = emisor;
        this.destinatario = null; // Por defecto, sin destinatario
    }

    // Constructor para mensajes con destinatario (privados)
    public Mensaje(String tipo, String contenido, String emisor, String destinatario) {
        this.tipo = tipo;
        this.contenido = contenido;
        this.emisor = emisor;
        this.destinatario = destinatario;
    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

	public Object getContenidoObjeto() {
		return contenidoObjeto;
	}

	public void setContenidoObjeto(Object contenidoObjeto) {
		this.contenidoObjeto = contenidoObjeto;
	}

    public String getContenido() {
        return contenido;
    }

    public String getEmisor() {
        return emisor;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
	
	
}
