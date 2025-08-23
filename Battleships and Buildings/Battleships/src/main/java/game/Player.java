// game/Player.java

package game;


import UI.ConnectorPlacementDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game.components.Armeria;
import game.components.Component;
import game.components.Conector;
import game.components.Mercado;
import game.components.Token;
import game.weapons.Bomb;
import game.weapons.Cannon;
import game.weapons.DoubleCannon;
import game.weapons.RedBeardCannon;
import game.weapons.Weapon;

import java.awt.Dimension;
import java.awt.Font;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import network.Client;
import network.Message;
import utils.Constants;

/**
 * La clase Player almacena información del jugador.
 * Gestiona acciones del jugador como comprar, vender y disparar.
 * También gestiona los comodines disponibles para el jugador.
 */
public class Player {

    public String name;
    public int money;
    public List<Component> components;
    public MapJuego map;
    public int steel; // Acero disponible
	public int escudo;
    public List<Weapon> weapons;
    public List<Token> componentsNotPlaced;
	public int escudos;
    public Client clientHost;

    /**
     * Constructor de la clase Player.
     *
     * @param name Nombre del jugador
     */
    public Player() {
        this.name = "Gabriel gay";
        this.money = Constants.INITIAL_MONEY;
        this.steel = 0;
        this.map = new MapJuego();
		this.escudos = 0;
        this.components = new ArrayList<>();
        this.weapons = new ArrayList<>();

        this.componentsNotPlaced = new ArrayList<>();
        componentsNotPlaced.add(new Token("FuenteDeEnergia", this)); 
        componentsNotPlaced.add(new Token("Mercado", this)); 
        // hola mundo
    }

    public Component getRandomComponent() {
        if (components == null || components.isEmpty()) {
            return null; // Retorna null si la lista está vacía
        }

        Random random = new Random();
        int randomIndex = random.nextInt(components.size());
        return components.get(randomIndex);
    }

	public void placeComponent(Component component, int x, int y) {
        if (map.placeComponent(component, x, y)) {
            components.add(component);
            // Update any UI elements if necessary
        } else {
            // Notify the player that the component cannot be placed here
            JOptionPane.showMessageDialog(null, "Cannot place component at this location.", "Placement Error", JOptionPane.ERROR_MESSAGE);
        }
    }
	
public void placeConnector() {
    // Verificar si el jugador tiene un token de Conector
    Token connectorToken = null;
    for (Token token : componentsNotPlaced) {
        if (token.getHolder().equals("Conector")) {
            connectorToken = token;
            break;
        }
    }
    if (connectorToken == null) {
        JOptionPane.showMessageDialog(null, "No tienes conectores en tu inventario.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Obtener componentes disponibles para conectar (excluyendo conectores)
    List<Component> availableComponents = getAvailableComponents();

    if (availableComponents.size() < 2) {
        JOptionPane.showMessageDialog(null, "Necesitas al menos dos componentes para colocar un conector.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Crear el diálogo personalizado para seleccionar componentes
    ConnectorPlacementDialog dialog = new ConnectorPlacementDialog(availableComponents);
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
        Component fromComponent = dialog.getFromComponent();
        Component toComponent = dialog.getToComponent();

        if (fromComponent == null || toComponent == null || fromComponent.equals(toComponent)) {
            JOptionPane.showMessageDialog(null, "Componentes seleccionados inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si la conexión ya existe
        if (map.graph.hasEdge(fromComponent, toComponent)) {
            JOptionPane.showMessageDialog(null, "Estos componentes ya están conectados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el conector utilizando el token
        Conector connector = (Conector) connectorToken.extractComponent(new Point(0, 0), countComponentsOfType(Conector.class) + 1);
        connector.setConnectedComponents(fromComponent, toComponent);

        // Agregar el conector al mapa y actualizar el grafo
        map.graph.addEdge(fromComponent, toComponent);
        //map.addConnector(connector);

        // Remover el token de componentsNotPlaced
        componentsNotPlaced.remove(connectorToken);

        JOptionPane.showMessageDialog(null, "Conector colocado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}



    // Helper method to get a list of components owned by the player
	public List<Component> getAvailableComponents() {
		List<Component> availableComponents = new ArrayList<>();
		for (Component component : components) {
			if (!(component instanceof Conector)) {
				availableComponents.add(component);
			}
		}
		return availableComponents;
	}


    // Helper method to select a component from a list
    public Component selectComponent(String message, List<Component> componentList) {
        String[] componentNames = componentList.stream().map(Component::getName).toArray(String[]::new);
        String selectedName = (String) JOptionPane.showInputDialog(null, message, "Select Component", JOptionPane.PLAIN_MESSAGE, null, componentNames, componentNames[0]);
        if (selectedName != null) {
            for (Component comp : componentList) {
                if (comp.getName().equals(selectedName)) {
                    return comp;
                }
            }
        }
        return null;
    }

    // Helper method to select the position to place the connector
    public Point selectConnectorPosition() {
        // Implement a method to select a position on the map
        // This could be similar to how you select positions when placing components
        // For simplicity, we can ask the user to input X and Y coordinates
        try {
            String xInput = JOptionPane.showInputDialog("Enter X coordinate for the connector (0-19):");
            String yInput = JOptionPane.showInputDialog("Enter Y coordinate for the connector (0-19):");
            int x = Integer.parseInt(xInput);
            int y = Integer.parseInt(yInput);
            return new Point(x, y);
        } catch (NumberFormatException e) {
            return null;
        }
    }
	
	public List<Component> getVisibleComponents() {
		List<Component> visibleComponents = new ArrayList<>();
		for (Component component : components) {
			if (!(map.graph).isConnectedToEnergySource(component)) {
				visibleComponents.add(component);
			}
		}
		return visibleComponents;
	}
	// In game/Player.java

	/**
	 * Displays the graph of components and connections.
	 */
	public void displayGraph() {
		// Generate the DOT format string
		String dotFormat = map.graph.toDotFormat();

		// Option 1: Write to a .dot file for external visualization
		try {
			FileWriter writer = new FileWriter("player_" + name + "_graph.dot");
			writer.write(dotFormat);
			writer.close();
			JOptionPane.showMessageDialog(null, "Graph exported to " + "player_" + name + "_graph.dot", "Graph Exported", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Option 2: Display in a JTextArea or any other GUI component
		JTextArea textArea = new JTextArea(dotFormat);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(500, 500));

		JOptionPane.showMessageDialog(null, scrollPane, "Graph Visualization", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public MapJuego getMap() {
        return map;
    }

    public Boolean hasArmoryWeapon(String Weapon) {
        Class<Armeria> type = Armeria.class;
        for (Component component : components) { // Usa la lista de la clase
            if (type.isInstance(component)) {
                    Armeria armeria = (Armeria) component;
                    if (armeria.getWeaponType().equals(Weapon)) {
                        return true;
                    }
                }
            }
            return false;
			
    }

	public int getEscudo() {
		return escudo;
	}

	public void setEscudo(int escudo) {
		this.escudo = escudo;
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}

	public void setWeapons(List<Weapon> weapons) {
		this.weapons = weapons;
	}
	
	public void actualizarDineroPlayer(){
		clientHost.gui.actualizarDinero();
	}

    public void setName(String name) {
        this.name = name;
    }

    public int weaponCount(String weaponName) {
        int count = 0;
        for (Weapon w : weapons) {
            if (w.getClass().getSimpleName().equalsIgnoreCase(weaponName)) {
                count++;
                }
            }
        return count;
    }
    public Armeria findArmoryPerWeapon(String Weapon){
        Class<Armeria> type = Armeria.class;
        for (Component component : components) { // Usa la lista de la clase
            if (type.isInstance(component)) {
                    Armeria armeria = (Armeria) component;
                    if (armeria.getWeaponType().equals(Weapon)) {
                        return armeria;
                    }
                }
            }
            return null;
    }

    public void handleArmory() {
        // Obtener la lista de armas disponibles con armorería
        List<String> availableWeapons = new ArrayList<>();
        for (Component component : components) {
            if (component instanceof Armeria) {
                Armeria armeria = (Armeria) component;
                availableWeapons.add(armeria.getWeaponType());
            }
        }

        if (availableWeapons.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tienes una armeria.", "Información", JOptionPane.INFORMATION_MESSAGE);
            actualizarDineroPlayer();
			return;
        }

        // Crear la ventana de armorería
        JFrame armoryFrame = new JFrame("Armorería");
        armoryFrame.setSize(400, 300);
        armoryFrame.setLayout(new BorderLayout());
        armoryFrame.setLocationRelativeTo(null);

        // Panel de selección de arma
        JPanel selectionPanel = new JPanel(new FlowLayout());
        JLabel selectLabel = new JLabel("Selecciona el arma a crear:");
        JComboBox<String> weaponComboBox = new JComboBox<>(availableWeapons.toArray(new String[0]));
        selectionPanel.add(selectLabel);
        selectionPanel.add(weaponComboBox);

        armoryFrame.add(selectionPanel, BorderLayout.CENTER);

        // Botón para crear el arma
        JButton createButton = new JButton("Create Weapon!");
        armoryFrame.add(createButton, BorderLayout.SOUTH);

        // Acción del botón "Create Weapon!"
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWeapon = (String) weaponComboBox.getSelectedItem();
                if (hasArmoryWeapon(selectedWeapon)) {
                    Armeria armeria = findArmoryPerWeapon(selectedWeapon);
                    if (armeria != null) {
                        armeria.produceWeapon(steel);
                        // Asumiendo que produceWeapon ya maneja la reducción de acero y la adición del arma
                    } else {
                        JOptionPane.showMessageDialog(armoryFrame, "Error al encontrar la armorería.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(armoryFrame, "No tienes una armorería para este tipo de arma.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
		actualizarDineroPlayer();
        armoryFrame.setVisible(true);
    }

    public void usarDinero(int dinero_usado) {
        this.money -= dinero_usado;
    }

    public void recibirDinero(int dinero_recibido) {
        this.money += dinero_recibido;
    }

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public <T extends Component> Boolean hasSearchedType(Class<T> type){
        for (Component component : components) { // Usa la lista de la clase
            if (type.isInstance(component)) {
                return true;
            }
        }
        return false;
    }
    
    public Boolean hasSearchedType(String token){
        for (Token tok : componentsNotPlaced) { // Usa la lista de la clase
            if  (tok.getHolder().equals(token)) {
                return true;
            }
        }
        return false;
    }

    public <T extends Component> int countComponentsOfType(Class<T> type) {
        int count = 0;
        for (Component component : components) { // Usa la lista de la clase
            if (type.isInstance(component)) {
                count++;
            }
        }
        return count;
    }

    // Getters y Setters

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }


    public int getSteel() {
        return steel;
    }

    public void setSteel(int steel) {
        this.steel = steel;
    }

    public List<Component> getComponents() {
        return components;
    }
	
    /**
     * Compra un componente.
     *
     * @param component Componente a comprar
     */
    public void buyComponent(Component component) {
        if (money >= component.getCost()) {
            money -= component.getCost();
            components.add(component);
            // Colocar componente en el mapa
            map.placeComponent(component, component.getPosition().x, component.getPosition().y);
            // Registrar la compra si es necesario
        } else {
            // No tiene suficiente dinero
            // Puedes lanzar una excepción o notificar al usuario
        }
    }

    /**
     * Vende un componente.
     *
     * @param component Componente a vender
     */
    public void sellComponent(Component component) {
        if (components.contains(component)) {
            money += component.getCost() / 2; // Recupera la mitad del costo
            components.remove(component);
            // Remover componente del mapa
            map.removeComponent(component.getPosition().x, component.getPosition().y);
            // Registrar la venta si es necesario
        } else {
            // El jugador no posee este componente
            // Puedes lanzar una excepción o notificar al usuario
        }
    }
	
	public void usarComodin(int seleccion){
		switch (seleccion) {
			case 1:
				ponerEscudos();
				break;
			case 2:
				ponerKraken();
				break;
			default:
				throw new AssertionError();
		}				
	}
	
	private void ponerEscudos() {
		Random random = new Random();
		int cantidadPosible = random.nextInt(4) + 2; // Genera un número aleatorio entre 2 y 5 (inclusive)
		int respuesta = JOptionPane.showConfirmDialog(null, "Se ha generado un escudo. Tiene " + cantidadPosible + " escudos posibles. ¿Desea asignarselo o descartarlo?", "Generar Escudo", JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION) {
			this.escudos = cantidadPosible;
		}

        clientHost.gui.setLblShield(cantidadPosible);
		// Si la respuesta es NO, no pasa nada
	}
	
	private void ponerKraken(){
		clientHost.enviarMensaje(new Message("mataKrakens", selectOtherPlayer()));
	}

    public String selectOtherPlayer() {
        // Lista de jugadores
        String[] players = {"player1", "player2", "player3", "player4"};

        // Crear una lista de opciones excluyendo this.name
        String[] filteredPlayers = java.util.Arrays.stream(players)
                .filter(player -> !player.equals(this.name))
                .toArray(String[]::new);

        // Crear un panel personalizado
        JPanel panel = new JPanel(new BorderLayout());
        
        // Crear el JLabel con el mensaje
        JLabel messageLabel = new JLabel("Has conseguido un kraken! Elige a quien castigará...");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Crear el JLabel con la imagen del Kraken
        JLabel imageLabel = new JLabel(new ImageIcon("Data/kraken.png"));
        
        // Agregar el mensaje arriba y la imagen a la izquierda
        panel.add(messageLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.WEST);

        // Mostrar el JOptionPane con el panel personalizado y las opciones
        String selectedPlayer = (String) JOptionPane.showInputDialog(
                null, 
                panel, 
                "Seleccionar jugador", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                filteredPlayers, 
                filteredPlayers[0]);

        // Retornar la selección
        return selectedPlayer;
    }

	public int getEscudos() {
		return escudos;
	}

	public void setEscudos(int escudos) {
		this.escudos = escudos;
	}
	
		


    public int getPriceWeapon(String weapon) {
        switch (weapon.toLowerCase()) {
            case "metal":
                return 10;
            case "cannon":
                return 500;
            case "doublecannon":
                return 1000;
            case "redbeardcannon":
                return 5000;
            case "bomb":
                return 2000;   
        }      
        System.out.println("Invalid weapon type");
		actualizarDineroPlayer();
        return -1;   
    }

    public void byebyeSteel(int bye) {
        steel -= bye;
    } 
    /**
     * Maneja las interacciones del jugador con el mercado.
     */
    public void handleMarket() {
        // Verificar si el jugador tiene un mercado
        if (hasSearchedType(Mercado.class) || hasSearchedType("Mercado")) {
            // Crear la ventana principal del mercado
            JFrame marketFrame = new JFrame("Mercado");
            marketFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            marketFrame.setSize(400, 300);
            marketFrame.setLocationRelativeTo(null);
            marketFrame.setLayout(new GridLayout(2, 2, 10, 10));

            // Botones para las opciones del mercado
            JButton buyButton = new JButton("Comprar del Mercado");
            JButton sellButton = new JButton("Vender al Mercado");
            JButton sendTradeButton = new JButton("Enviar Trade");
            JButton viewTradesButton = new JButton("Ver Trades");

            // Añadir acción a los botones de enviar y ver trades (no se implementarán)
            sendTradeButton.addActionListener(e -> {
                sendOfferPlayer();
            });

            viewTradesButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(marketFrame, "Se vera una oferta de intercambio cuando se reciba uno.", "Información", JOptionPane.INFORMATION_MESSAGE);
            });

            // Acción para el botón de Comprar
            buyButton.addActionListener(e -> {
                openBuyWindow();
            });

            // Acción para el botón de Vender
            sellButton.addActionListener(e -> {
                openSellWindow();
            });

            // Añadir botones al frame
            marketFrame.add(buyButton);
            marketFrame.add(sellButton);
            marketFrame.add(sendTradeButton);
            marketFrame.add(viewTradesButton);

            marketFrame.setVisible(true);
			actualizarDineroPlayer();
        } else {
            JOptionPane.showMessageDialog(null, "No tienes un Mercado en tu mapa.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
		actualizarDineroPlayer();
    }

    /**
     * Abre una ventana para comprar del mercado.
     */
    private void openBuyWindow() {
        JFrame buyFrame = new JFrame("Comprar del Mercado");
        buyFrame.setSize(400, 400);
        buyFrame.setLocationRelativeTo(null); // Centrar la ventana
        buyFrame.setLayout(new BorderLayout());

        // Panel principal para la selección de compra
        JPanel selectionPanel = new JPanel(new GridLayout(6, 1, 10, 10));

        // Opciones de compra (incluye armas, metal y tokens)
        String[] items = {
            "Metal", 
            "Cannon", 
            "Double Cannon", 
            "Redbeard Cannon", 
            "Bomb", 
            "Token: Mina", 
            "Token: Armeria", 
            "Token: TemploDeLaBruja", 
            "Token: Mercado", 
            "Token: FuenteDeEnergia", 
            "Token: Conector"
        };
        JComboBox<String> itemComboBox = new JComboBox<>(items);

        // Campo para ingresar la cantidad (solo aplica a Metal)
        JPanel quantityPanel = new JPanel(new FlowLayout());
        JLabel quantityLabel = new JLabel("Cantidad de Metal:");
        JTextField quantityField = new JTextField(10);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton purchaseButton = new JButton("Comprar");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(purchaseButton);
        buttonPanel.add(cancelButton);

        // Añadir componentes al panel principal
        selectionPanel.add(new JLabel("Selecciona el ítem a comprar:"));
        selectionPanel.add(itemComboBox);
        selectionPanel.add(quantityPanel); // Visible solo para Metal
        selectionPanel.add(new JLabel("")); // Espaciador
        selectionPanel.add(buttonPanel);

        buyFrame.add(selectionPanel, BorderLayout.CENTER);

        // Mostrar u ocultar el campo de cantidad según el ítem seleccionado
        itemComboBox.addActionListener(e -> {
            String selectedItem = (String) itemComboBox.getSelectedItem();
            quantityPanel.setVisible("Metal".equalsIgnoreCase(selectedItem));
            buyFrame.revalidate();
            buyFrame.repaint();
        });

        // Inicialmente, esconder el campo de cantidad si no es Metal
        quantityPanel.setVisible("Metal".equalsIgnoreCase((String) itemComboBox.getSelectedItem()));

        // Acción para el botón "Comprar"
        purchaseButton.addActionListener(e -> {
            String selectedItem = (String) itemComboBox.getSelectedItem();
            if ("Metal".equalsIgnoreCase(selectedItem)) {
                // Lógica para la compra de Metal
                String quantityText = quantityField.getText();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityText);
                    if (quantity <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(buyFrame, "Por favor, ingresa una cantidad válida de Metal.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int pricePerUnit = getPriceWeapon("metal");
                int totalPrice = (int) (pricePerUnit * 1.5 * quantity);
                if (canPurchase(totalPrice)) {
                    usarDinero(totalPrice);
                    steel += quantity;
                    JOptionPane.showMessageDialog(buyFrame, "Has comprado " + quantity + " kg de Metal por " + totalPrice + " monedas.", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    buyFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(buyFrame, "No tienes suficiente dinero para esta compra.", "Fondos insuficientes", JOptionPane.ERROR_MESSAGE);
                }
            } else if (selectedItem.startsWith("Token:")) {
                // Lógica para la compra de Tokens
                String tokenName = selectedItem.replace("Token: ", "").trim();
                int price = getPriceToken(tokenName); // Método para obtener precio del token
                if (price == -1) {
                    JOptionPane.showMessageDialog(buyFrame, "Token inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int totalPrice = (int) (price * 1.5);
                if (canPurchase(totalPrice)) {
                    usarDinero(totalPrice);
                    Token newToken = new Token(tokenName, this);
                    componentsNotPlaced.add(newToken); // Agregar a la lista de tokens no colocados
                    JOptionPane.showMessageDialog(buyFrame, "Has comprado un token de " + tokenName + " por " + totalPrice + " monedas.", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    buyFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(buyFrame, "No tienes suficiente dinero para esta compra.", "Fondos insuficientes", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Lógica para la compra de armas
                String weapon = selectedItem.toLowerCase().replace(" ", "");
                int price = getPriceWeapon(weapon); // Método para obtener precio del arma
                if (price == -1) {
                    JOptionPane.showMessageDialog(buyFrame, "Arma inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                    actualizarDineroPlayer();
					return;
                }
                int totalPrice = (int) (price * 1.5);
                if (canPurchase(totalPrice)) {
                    usarDinero(totalPrice);
                    Weapon newWeapon = createWeapon(weapon); // Método para crear el arma
                    if (newWeapon != null) {
                        weapons.add(newWeapon);
                        JOptionPane.showMessageDialog(buyFrame, "Has comprado un " + selectedItem + " por " + totalPrice + " monedas.", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
                        buyFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(buyFrame, "Error al crear el arma.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(buyFrame, "No tienes suficiente dinero para esta compra.", "Fondos insuficientes", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para el botón "Cancelar"
        cancelButton.addActionListener(e -> buyFrame.dispose());
		actualizarDineroPlayer();
        buyFrame.setVisible(true);
    }
    
    private int getPriceToken(String tokenName) {
    // Devuelve el precio del token según su nombre
    switch (tokenName) {
        case "Mina": return 1000;
        case "Armeria": return 1500;
        case "TemploDeLaBruja": return 2500;
        case "Mercado": return 2000;
        case "FuenteDeEnergia": return 12000;
        case "Conector": return 100;
        default: return -1; // Token inválido
    }
}

    /**
     * Abre una ventana para vender al mercado.
     */
    private void openSellWindow() {
        JFrame sellFrame = new JFrame("Vender al Mercado");
        sellFrame.setSize(400, 400);
        sellFrame.setLocationRelativeTo(null);
        sellFrame.setLayout(new BorderLayout());

        // Panel para seleccionar el tipo de venta
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(5, 1, 10, 10));

        // Opciones de venta
        String[] items = {"Metal", "Cannon", "Double Cannon", "Redbeard Cannon", "Bomb"};
        JComboBox<String> itemComboBox = new JComboBox<>(items);

        // Campo para ingresar la cantidad (solo para Metal)
        JPanel quantityPanel = new JPanel(new FlowLayout());
        JLabel quantityLabel = new JLabel("Cantidad de Metal:");
        JTextField quantityField = new JTextField(10);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton sellButton = new JButton("Vender");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(sellButton);
        buttonPanel.add(cancelButton);

        // Añadir componentes al panel de selección
        selectionPanel.add(new JLabel("Selecciona el ítem a vender:"));
        selectionPanel.add(itemComboBox);
        selectionPanel.add(quantityPanel);
        selectionPanel.add(new JLabel("")); // Espaciador
        selectionPanel.add(buttonPanel);

        sellFrame.add(selectionPanel, BorderLayout.CENTER);

        // Mostrar o esconder el campo de cantidad según el ítem seleccionado
        itemComboBox.addActionListener(e -> {
            String selectedItem = (String) itemComboBox.getSelectedItem();
            if ("Metal".equalsIgnoreCase(selectedItem)) {
                quantityPanel.setVisible(true);
            } else {
                quantityPanel.setVisible(false);
            }
			actualizarDineroPlayer();
            sellFrame.revalidate();
            sellFrame.repaint();
        });

        // Inicialmente, esconder el campo de cantidad si no es Metal
        String initialItem = (String) itemComboBox.getSelectedItem();
        quantityPanel.setVisible("Metal".equalsIgnoreCase(initialItem));

        // Acción para el botón de Vender
        sellButton.addActionListener(e -> {
            String selectedItem = (String) itemComboBox.getSelectedItem();
            if ("Metal".equalsIgnoreCase(selectedItem)) {
                String quantityText = quantityField.getText();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityText);
                    if (quantity <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(sellFrame, "Por favor, ingresa una cantidad válida de Metal.", "Error", JOptionPane.ERROR_MESSAGE);
                    actualizarDineroPlayer();
					return;
                }
                if (steel >= quantity) {
                    int pricePerUnit = getPriceWeapon("metal");
                    int totalPrice = (int) (pricePerUnit * 0.8 * quantity);
                    recibirDinero(totalPrice);
                    steel -= quantity;
                    JOptionPane.showMessageDialog(sellFrame, "Has vendido " + quantity + " kg de Metal por " + totalPrice + " monedas.", "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    actualizarDineroPlayer();
					sellFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(sellFrame, "No tienes suficiente Metal para vender.", "Metal Insuficiente", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                String weapon = selectedItem.toLowerCase().replace(" ", "");
                Weapon weaponToSell = findWeapon(weapon);
                if (weaponToSell != null) {
                    int price = (int) (getPriceWeapon(weapon) * 0.8);
                    recibirDinero(price);
                    weapons.remove(weaponToSell);
                    JOptionPane.showMessageDialog(sellFrame, "Has vendido un " + selectedItem + " por " + price + " monedas.", "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    sellFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(sellFrame, "No posees un " + selectedItem + " para vender.", "Arma No Encontrada", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para el botón de Cancelar
        cancelButton.addActionListener(e -> {
            sellFrame.dispose();
        });
		actualizarDineroPlayer();
        sellFrame.setVisible(true);
    }

    private Weapon createWeapon(String weapon) {
        switch (weapon.toLowerCase()) {
            case "cannon":
                return new Cannon(); // Asegúrate de tener una clase Cannon que extiende Weapon
            case "doublecannon":
                return new DoubleCannon(); // Asegúrate de tener una clase DoubleCannon que extiende Weapon
            case "redbeardcannon":
                return new RedBeardCannon(); // Asegúrate de tener una clase RedbeardCannon que extiende Weapon
            case "bomb":
                return new Bomb(); // Asegúrate de tener una clase Bomb que extiende Weapon
            default:
                return null;
        }
    }

    /**
     * Busca una instancia de Weapon en la lista de armas del jugador.
     *
     * @param weapon Nombre del arma
     * @return Instancia de Weapon o null si no se encuentra
     */
    public Weapon findWeapon(String weapon) {
        for (Weapon w : weapons) {
            if (w.getClass().getSimpleName().equalsIgnoreCase(weapon)) {
                return w;
            }
        }
        return null;
    }

    public void removeWeapon(Weapon weapon) {
        for (Weapon w : weapons) {
            if (w == weapon) {
                weapons.remove(w);
                break;
            }
        }
    }

    /**
     * Verifica si el jugador tiene suficiente dinero para realizar una compra.
     *
     * @param amount Cantidad de dinero a verificar
     * @return true si el jugador puede comprar, false en caso contrario
     */
    public boolean canPurchase(int amount) {
        return this.money >= amount;
    }
    
    public Set<String> toSetStringTokens(){
        Set<String> set = new LinkedHashSet<>();
        set.add("NONE");
        for (Token token : componentsNotPlaced) {
            set.add(token.getHolder());
        }
        return set;
    }
    
    public boolean isValidCoord(Component componente, Point coord){
        if (componente.isSalidoDelLimite(coord)) return false;
        for (Component component : components) {
            if (component.isChocando(coord) || componente.isSalidoDelLimite(coord)) return false;
        }
        return true;
    }
    
    public boolean isChocando(Point coord){
        for (Component component : components) {
            if (component.isChocando(coord)) return true;
        }
        return false;
    }

    // Métodos existentes de compra y venta...

    // Otros métodos y lógica del jugador

    public List<Token> getComponentsNotPlaced() {
        return componentsNotPlaced;
    }
	
	    public void sendOfferPlayer() {
        // List of players
        String[] players = {"player1", "player2", "player3", "player4"};

        // Exclude self
        List<String> playerList = new ArrayList<>(Arrays.asList(players));
        playerList.remove(this.name); // Remove self from options

        if (playerList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay otros jugadores disponibles para enviar una oferta.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Select player to send offer
        String recipient = (String) JOptionPane.showInputDialog(null, "Selecciona el jugador al que deseas enviar la oferta:", "Enviar Oferta", JOptionPane.PLAIN_MESSAGE, null, playerList.toArray(), playerList.get(0));
        if (recipient == null) {
            // User cancelled
            return;
        }

        // Open window to specify what is being offered
        JFrame offerFrame = new JFrame("Crear Oferta - Ofrecer");
        offerFrame.setSize(400, 300);
        offerFrame.setLocationRelativeTo(null);
        offerFrame.setLayout(new GridLayout(7, 2));

        // Fields for resources being offered
        JLabel moneyLabel = new JLabel("Dinero:");
        JTextField moneyField = new JTextField("0");
        JLabel steelLabel = new JLabel("Steel:");
        JTextField steelField = new JTextField("0");
        JLabel cannonLabel = new JLabel("Cannon:");
        JTextField cannonField = new JTextField("0");
        JLabel doubleCannonLabel = new JLabel("DoubleCannon:");
        JTextField doubleCannonField = new JTextField("0");
        JLabel redBeardCannonLabel = new JLabel("RedBeardCannon:");
        JTextField redBeardCannonField = new JTextField("0");
        JLabel bombLabel = new JLabel("Bomb:");
        JTextField bombField = new JTextField("0");

        JButton confirmButton = new JButton("Confirmar");

        offerFrame.add(moneyLabel);
        offerFrame.add(moneyField);
        offerFrame.add(steelLabel);
        offerFrame.add(steelField);
        offerFrame.add(cannonLabel);
        offerFrame.add(cannonField);
        offerFrame.add(doubleCannonLabel);
        offerFrame.add(doubleCannonField);
        offerFrame.add(redBeardCannonLabel);
        offerFrame.add(redBeardCannonField);
        offerFrame.add(bombLabel);
        offerFrame.add(bombField);
        offerFrame.add(new JLabel()); // Empty label for spacing
        offerFrame.add(confirmButton);

        confirmButton.addActionListener(e -> {
            // Validate if player has enough resources
            try {
                int offerMoney = Integer.parseInt(moneyField.getText());
                int offerSteel = Integer.parseInt(steelField.getText());
                int offerCannon = Integer.parseInt(cannonField.getText());
                int offerDoubleCannon = Integer.parseInt(doubleCannonField.getText());
                int offerRedBeardCannon = Integer.parseInt(redBeardCannonField.getText());
                int offerBomb = Integer.parseInt(bombField.getText());

                // Check if player has enough resources
                if (this.money < offerMoney ||
                    this.steel < offerSteel ||
                    weaponCount("Cannon") < offerCannon ||
                    weaponCount("DoubleCannon") < offerDoubleCannon ||
                    weaponCount("RedBeardCannon") < offerRedBeardCannon ||
                    weaponCount("Bomb") < offerBomb) {
                    JOptionPane.showMessageDialog(offerFrame, "No tiene los recursos suficientes.", "Error", JOptionPane.ERROR_MESSAGE);
                    actualizarDineroPlayer();
					return;
                }

                // Subtract the offered resources
                this.money -= offerMoney;
                this.steel -= offerSteel;
                removeWeapons("Cannon", offerCannon);
                removeWeapons("DoubleCannon", offerDoubleCannon);
                removeWeapons("RedBeardCannon", offerRedBeardCannon);
                removeWeapons("Bomb", offerBomb);

                offerFrame.dispose();
				actualizarDineroPlayer();

                // Proceed to ask what is being requested
                createRequestWindow(recipient, offerMoney, offerSteel, offerCannon, offerDoubleCannon, offerRedBeardCannon, offerBomb);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(offerFrame, "Por favor ingresa cantidades válidas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
		actualizarDineroPlayer();
        offerFrame.setVisible(true);
    }

    // Helper method to remove weapons
    public void removeWeapons(String weaponName, int quantity) {
        int removed = 0;
        Iterator<Weapon> iterator = weapons.iterator();
        while (iterator.hasNext() && removed < quantity) {
            Weapon w = iterator.next();
            if (w.getClass().getSimpleName().equalsIgnoreCase(weaponName)) {
                iterator.remove();
                removed++;
            }
        }
    }

    // Method to add weapons
    public void addWeapons(String weaponName, int quantity) {
        for (int i = 0; i < quantity; i++) {
            Weapon weapon = createWeapon(weaponName);
            if (weapon != null) {
                weapons.add(weapon);
            }
        }
    }

    // Method to create the request window
    private void createRequestWindow(String recipient, int offerMoney, int offerSteel, int offerCannon, int offerDoubleCannon, int offerRedBeardCannon, int offerBomb) {
        JFrame requestFrame = new JFrame("Crear Oferta - Solicitar");
        requestFrame.setSize(400, 300);
        requestFrame.setLocationRelativeTo(null);
        requestFrame.setLayout(new GridLayout(7, 2));

        // Fields for resources being requested
        JLabel moneyLabel = new JLabel("Dinero:");
        JTextField moneyField = new JTextField("0");
        JLabel steelLabel = new JLabel("Steel:");
        JTextField steelField = new JTextField("0");
        JLabel cannonLabel = new JLabel("Cannon:");
        JTextField cannonField = new JTextField("0");
        JLabel doubleCannonLabel = new JLabel("DoubleCannon:");
        JTextField doubleCannonField = new JTextField("0");
        JLabel redBeardCannonLabel = new JLabel("RedBeardCannon:");
        JTextField redBeardCannonField = new JTextField("0");
        JLabel bombLabel = new JLabel("Bomb:");
        JTextField bombField = new JTextField("0");

        JButton confirmButton = new JButton("Confirmar");

        requestFrame.add(moneyLabel);
        requestFrame.add(moneyField);
        requestFrame.add(steelLabel);
        requestFrame.add(steelField);
        requestFrame.add(cannonLabel);
        requestFrame.add(cannonField);
        requestFrame.add(doubleCannonLabel);
        requestFrame.add(doubleCannonField);
        requestFrame.add(redBeardCannonLabel);
        requestFrame.add(redBeardCannonField);
        requestFrame.add(bombLabel);
        requestFrame.add(bombField);
        requestFrame.add(new JLabel()); // Empty label for spacing
        requestFrame.add(confirmButton);

        confirmButton.addActionListener(e -> {
            try {
                int requestMoney = Integer.parseInt(moneyField.getText());
                int requestSteel = Integer.parseInt(steelField.getText());
                int requestCannon = Integer.parseInt(cannonField.getText());
                int requestDoubleCannon = Integer.parseInt(doubleCannonField.getText());
                int requestRedBeardCannon = Integer.parseInt(redBeardCannonField.getText());
                int requestBomb = Integer.parseInt(bombField.getText());

                // Create offer content
                Offer offer = new Offer(this.name, recipient,
                    offerMoney, offerSteel, offerCannon, offerDoubleCannon, offerRedBeardCannon, offerBomb,
                    requestMoney, requestSteel, requestCannon, requestDoubleCannon, requestRedBeardCannon, requestBomb);

                // Send offer to server
                clientHost.enviarMensaje(new Message("sendOffer", offer, recipient));
				actualizarDineroPlayer();
                requestFrame.dispose();
                JOptionPane.showMessageDialog(null, "Oferta enviada a " + recipient + ".", "Oferta Enviada", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(requestFrame, "Por favor ingresa cantidades válidas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
		actualizarDineroPlayer();
        requestFrame.setVisible(true);
    }
	

	// Method to add steel
	public void addSteel(int amount) {
		this.steel += amount;
	}

	// Method to get clientHost
	public Client getClientHost() {
		return clientHost;
	}

	// Setter for clientHost
	public void setClientHost(Client clientHost) {
		this.clientHost = clientHost;
	}

	// Method to return offered items in case of rejection
	public void returnOffer(Offer offer) {
		this.money += offer.getOfferMoney();
		this.steel += offer.getOfferSteel();
		addWeapons("Cannon", offer.getOfferCannon());
		addWeapons("DoubleCannon", offer.getOfferDoubleCannon());
		addWeapons("RedBeardCannon", offer.getOfferRedBeardCannon());
		addWeapons("Bomb", offer.getOfferBomb());
		actualizarDineroPlayer();
	}

	// Method to receive items when offer is accepted
	public void receiveOffer(Offer offer) {
		this.money += offer.getRequestMoney();
		this.steel += offer.getRequestSteel();
		addWeapons("Cannon", offer.getRequestCannon());
		addWeapons("DoubleCannon", offer.getRequestDoubleCannon());
		addWeapons("RedBeardCannon", offer.getRequestRedBeardCannon());
		addWeapons("Bomb", offer.getRequestBomb());
		actualizarDineroPlayer();
	}

}