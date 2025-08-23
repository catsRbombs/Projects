// network/Message.java

package network;

import java.io.Serializable;

/**
 * La clase Message define diferentes tipos de mensajes para la comunicación.
 * Implementa Serializable para permitir su transmisión a través de streams.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type; // Tipo de mensaje (e.g., "Move", "Attack", "Chat")
    private Object data; // Datos asociados al mensaje
	private String destinatario;

    /**
     * Constructor de la clase Message.
     *
     * @param type Tipo de mensaje
     * @param data Datos del mensaje
     */
    public Message(String type, Object data) {
        this.type = type;
        this.data = data;
    }
	
	public Message(String type, Object data, String destinatario){
		this.type = type;
        this.data = data;
		this.destinatario = destinatario;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	

    // Getters y Setters

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message [type=" + type + ", data=" + data + "]";
    }

	public String getRecipient() {
		return destinatario;
	}
}
