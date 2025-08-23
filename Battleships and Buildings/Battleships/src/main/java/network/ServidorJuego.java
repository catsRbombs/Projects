package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ServidorJuego {
    private ScheduledExecutorService scheduler;
    private ServerSocket serverSocket;
    private List<ClienteHandler> clientes;
    private Map<String, ClienteHandler> mapaClientes;
    private int playerCount = 1;

    public ServidorJuego(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            clientes = new ArrayList<>();
            mapaClientes = new HashMap<>();
            
            // Inicializar el scheduler antes de usarlo
            scheduler = Executors.newScheduledThreadPool(1);
            
            System.out.println("Servidor iniciado en el puerto " + puerto);
            
            
		while (true) {
			Socket socket = serverSocket.accept();
			ClienteHandler clienteHandler = new ClienteHandler(socket, this, playerCount++); 
			clientes.add(clienteHandler);
			clienteHandler.start();
		}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	

    public synchronized void removerCliente(ClienteHandler cliente) {
        clientes.remove(cliente);
        mapaClientes.remove(cliente.getName());
    }

    public synchronized void broadcast(Message Message, ClienteHandler emisor) {
        for (ClienteHandler cliente : clientes) {
            if (cliente != emisor) {
                cliente.enviarMensaje(Message);
            }
        }
    }
	
	public void agregarPlayer(String name, ClienteHandler cliente ){
		mapaClientes.put(name, cliente);
	}
	

    public List<ClienteHandler> getClientes() {
        return clientes;
    }

    public ClienteHandler getClienteHandler(String nombre) {
        return mapaClientes.get(nombre);
    }
	
    public boolean allReady(){
        for (ClienteHandler cliente : clientes) {
            if (!cliente.ready) return false;
        }
        return true;
    }
    public void nextPlayerTurn(){
        for( ClienteHandler client : clientes ){
            if(client.myTurn == null) {client.myTurn = false; System.out.println("nullismo");}
            if(client.myTurn == true) {
                client.myTurn = false;
                getNextCliente(client).myTurn = true;
                getNextCliente(client).startTurn();
                break;
            }
        }
        updateTurnStatus();
    }
	
    public void updateTurnStatus(){
        String whoseTurn = "waiting";
        ClienteHandler me = null;
        for( ClienteHandler cliente : clientes){
            if(cliente.myTurn == true) {
                whoseTurn = cliente.getName();
                me = cliente;
                break;
            }
        }
        if(me!=null){
        broadcast(new Message("updateTurnStatus",whoseTurn), me);
        me.enviarMensaje(new Message("updateTurnStatus", "Your turn!"));
        }
        else {System.out.println("Algo fallo pasando turno...");}
    }
    
    public ClienteHandler getNextCliente(ClienteHandler current) {
        if (clientes == null || clientes.isEmpty()) {
            return null; // Si la lista está vacía, no hay "siguiente"
        }
    
        int index = clientes.indexOf(current);
    
        if (index == -1) {
            throw new IllegalArgumentException("El cliente proporcionado no está en la lista.");
        }
    
        // Calcular el siguiente índice circularmente
        int nextIndex = (index + 1);
        if(nextIndex == clientes.size()) {
            nextIndex = 0;
        }
        System.out.println("Indice del siguiente cliente!" + nextIndex);
        return clientes.get(nextIndex);
    }
}
