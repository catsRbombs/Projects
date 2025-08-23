/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;
import game.MapJuego;
import game.Player;
import game.components.Component;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.components.Mercado;
import game.components.Token;
import game.components.Whirpool;
import game.weapons.Weapon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import network.Client;
import network.Message;

/**
 *
 * @author Efraim Cuevas
 */
public class PlayerGUI extends javax.swing.JFrame {
    JLabel miGrid[][];
    Point coordSeleccionadaPlayer;
    JLabel gridEnemigo[][];
    Point coordAAtacar;
    Player player;
    Client client;
    MapJuego mapaEnemigo;
	ConnectionLayer connectionLayer;
    /**
     * Creates new form PlayerGUI
     */
    public PlayerGUI(Client cliente) {
	this.client = cliente;
        initComponents();
        pnlMenuInicio.setVisible(false);
        pnlTienda.setVisible(false);
        pnlTablero.setVisible(true);
        miGrid = new JLabel[20][20];
        gridEnemigo = new JLabel[20][20];
        this.player = client.player;
        createOwnLblMatrix();
        createEnemyLblMatrix();
	configurarChat();
        whirpoolPlacement();
    }

	public void createOwnLblMatrix() {
    pnlMiArea.setPreferredSize(new Dimension(460, 460));
    pnlMiArea.setLayout(null); // Usaremos layout nulo para posicionar los componentes manualmente

    miGrid = new JLabel[20][20];

    int cellSize = 23; // Tamaño de cada celda (460 / 20)

    for (int i = 0; i < 20; i++) {
        for (int j = 0; j < 20; j++) {
            JLabel label = new JLabel();
            label.setBackground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setOpaque(true);

            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);

            // Establecer el tamaño y posición del label
            label.setBounds(j * cellSize, i * cellSize, cellSize, cellSize);

            // Crear un Point para las coordenadas actuales
            Point coord = new Point(i, j);

            // Agregar un MouseListener para detectar clics
            label.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    // Llamar a la función con las coordenadas actuales
                    setCoordenadaMiGrid(coord);
                }
            });

            pnlMiArea.add(label, Integer.valueOf(1)); // Añadir al nivel 1 del JLayeredPane
            miGrid[i][j] = label;
        }
    }

    // Crear e inicializar connectionLayer
    connectionLayer = new ConnectionLayer(miGrid);
    connectionLayer.setBounds(0, 0, pnlMiArea.getWidth(), pnlMiArea.getHeight());
    pnlMiArea.add(connectionLayer, Integer.valueOf(2)); // Añadir al nivel 2 del JLayeredPane

    pnlMiArea.repaint();
    pnlMiArea.revalidate();
}

	

	public void createEnemyLblMatrix() {
		pnlEnemyArea.setLayout(new java.awt.GridLayout(20, 20));

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				JLabel label = new JLabel();
				label.setBackground(new java.awt.Color(255, 255, 255));
				label.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				label.setOpaque(true);

				label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				label.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

				// Crear un Point para las coordenadas actuales
				Point coord = new Point(i, j);

				// Agregar un MouseListener para detectar clics
				label.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mousePressed(java.awt.event.MouseEvent e) {
						// Llamar a la función con las coordenadas actuales
						setCoordenadaEnemyGrid(coord);
					}
				});

				pnlEnemyArea.add(label);
				gridEnemigo[i][j] = label;
			}
		}

		pnlEnemyArea.repaint();
		pnlEnemyArea.revalidate();
	}
        
    public void loadEnemyGrid(MapJuego gridMap){
		this.mapaEnemigo = gridMap;
		System.out.println("MAPA ENEMIGO: \n" + mapaEnemigo.toStringRepresentation());;
                clearEnemyGrid();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Component comp = gridMap.getComponent(i, j);
                if (comp != null && comp.isVisibleToEnemies()) { //&& comp.isVisibleToEnemies()
                    System.out.println("COMPONENTE AQUI" + comp.getName());
                    gridEnemigo[i][j].setBackground(getBuildingColor(removeTrailingNumbers(comp.getName())));
                }
            }
        }
        ponerPuntitosRojos();
    }
    
    public void ponerPuntitosRojos(){
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (mapaEnemigo.getIntGridValue(i, j) == -1) {
                    gridEnemigo[i][j].setBackground(Color.red);
                    gridEnemigo[i][j].repaint();
                    gridEnemigo[i][j].revalidate();
                } else if (mapaEnemigo.getIntGridValue(i, j) == -2){
                    gridEnemigo[i][j].setBackground(new Color(102, 0, 0));
                    gridEnemigo[i][j].repaint();
                    gridEnemigo[i][j].revalidate();
                }
            }
        }
    }
    
    public void clearEnemyGrid(){
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                gridEnemigo[i][j].setBackground(Color.white);
            }   
        }
        pnlEnemyArea.repaint();
        pnlEnemyArea.revalidate();
    }
        
    public void setCoordenadaMiGrid(Point coord){
        if (coordSeleccionadaPlayer != null && miGrid[coordSeleccionadaPlayer.x][coordSeleccionadaPlayer.y].getBackground().equals(Color.GRAY)) clearSelectedButtonColor(miGrid[coordSeleccionadaPlayer.x][coordSeleccionadaPlayer.y]); 
        if (miGrid[coord.x][coord.y].getBackground().equals(Color.WHITE)){
            coordSeleccionadaPlayer = coord;
            miGrid[coord.x][coord.y].setBackground(Color.GRAY);
        }
    }
    
    public void setCoordenadaEnemyGrid(Point coord){
        if (coordAAtacar != null) clearSelectedButtonColor(gridEnemigo[coordAAtacar.x][coordAAtacar.y]);
        coordAAtacar = coord;
        gridEnemigo[coord.x][coord.y].setBackground(Color.GRAY);
    }

    public JButton getBtnStart() {
        return btnStart;
    }
    
    public static String removeTrailingNumbers(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\d+$", "");
    }



    
    
    public void escribirEnResultados(String str){
        txaBitacora.append(str + "\n");
        txaBitacora.repaint();
        txaBitacora.revalidate();
    }
    
    public void attackTurn(){
        btnAttack.setEnabled(true);
        btnSkipTurn.setEnabled(true);
        pnlTablero.repaint();
        pnlTablero.revalidate();
    }

    public void notAttackTurn(){
        btnAttack.setEnabled(false);
        btnSkipTurn.setEnabled(false);
        pnlTablero.repaint();
        pnlTablero.revalidate();
    }

    public void searchNdelete(Component dying) {
		MapJuego mapa = this.player.getMap();
		for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Component comp = mapa.getComponent(i, j);
                if(comp!=null){
                if (comp.getName() == dying.getName()) {
                    this.player.getMap().intGrid[i][j] = -2;
					this.player.getMap().grid[i][j] = null;
					miGrid[i][j].setBackground(Color.RED);
                }
            }
            }
        }
	}

	public void actualizarDinero(){
		lblDinero.setText(Integer.toString(player.getMoney()));
		lblMetal.setText(Integer.toString(player.getSteel()));
                lblMetal.revalidate();
                lblMetal.repaint();
                lblDinero.repaint();
                lblDinero.revalidate();
	}
    
    public void clearSelectedButtonColor(JLabel label){
        label.setBackground(Color.WHITE);
        label.revalidate();
        label.repaint();
    }
    
    public void setupBuildComboBox(){
        Set<String> opciones = player.toSetStringTokens();
        
        // Limpiar el JComboBox
        buildcmBox.removeAllItems();

        // Agregar cada opción al JComboBox
        for (String opcion : opciones) {
            buildcmBox.addItem(opcion);
        }

        // Mostrar el diálogo
        buildDialog.setVisible(true);
    }
    public void startTurn() {
        btnAttack.setVisible(true);
        btnAttack.setEnabled(true);
        btnSkipTurn.setVisible(true);
        btnSkipTurn.setEnabled(true);
    }

    public void updateTurn(String turn) {
        lblTurn.setText(turn);
    }
    
    public void endTurn() {
        btnAttack.setVisible(false);
        btnSkipTurn.setVisible(false);
    }
    
    public void whirpoolPlacement(){
        Random random = new Random();
        Point pt1 = new Point(random.nextInt(0, 20), random.nextInt(0, 20));
        Point pt2 = new Point(random.nextInt(0, 20), random.nextInt(0, 20));
        Whirpool remolinanashe = new Whirpool(pt1);
        Whirpool remolinanasheismo = new Whirpool(pt2);
        player.placeComponent(remolinanashe, pt1.x, pt1.y);
        player.placeComponent(remolinanasheismo, pt2.x, pt2.y);
        colorComponentsInGrid(remolinanashe, miGrid, getBuildingColor(remolinanashe));
        colorComponentsInGrid(remolinanasheismo, miGrid, getBuildingColor(remolinanasheismo));
        
    }
     
    public void skibidiPlacement(){
        Component nuevo = player.getComponentsNotPlaced().get(buildcmBox.getSelectedIndex()-1).extractComponent(coordSeleccionadaPlayer, player.getComponents().size());
        //arreglar lo de arriba en lo del tag
        
        //Falta implementar lo de la lógica del mapa
        if (player.isValidCoord(nuevo,coordSeleccionadaPlayer)  /*|| player.getMap().placeComponent(nuevo, coordSeleccionadaPlayer.x, coordSeleccionadaPlayer.y)*/) {
            System.out.println("Se hizo el skibidi placement");
            buildDialog.setVisible(false);
            pnlTienda.setVisible(false);
            pnlTablero.setVisible(true);
            player.placeComponent(nuevo, coordSeleccionadaPlayer.x, coordSeleccionadaPlayer.y);
            colorComponentsInGrid(nuevo, miGrid, getBuildingColor());
            player.getComponentsNotPlaced().remove(player.getComponentsNotPlaced().get(buildcmBox.getSelectedIndex()-1));
        } else JOptionPane.showMessageDialog(buildDialog, "No puedes colocar tu edificación ahí!.", "Información", JOptionPane.INFORMATION_MESSAGE); 
    }   
		
    public void colorComponentsInGrid(Component cmp, JLabel[][] grid, Color color){
        int x = cmp.getPosition().x;
        int y = cmp.getPosition().y;
        
        for (int i = 0; i < cmp.getWidth(); i++) {
            for (int j = 0; j < cmp.getHeight(); j++) {
                grid[x+i][y+j].setBackground(color);
            }
        }  
        pnlTablero.repaint();
        pnlTablero.revalidate();
    }
    
    public Color getBuildingColor(Component comp){
        switch (comp.getName()) {
            case "Mina":
                return Color.YELLOW;
            case "Armeria":
                return Color.BLUE;
            case "TemploDeLaBruja":
                return Color.MAGENTA;
            case "Mercado":
                return Color.CYAN;
            case "FuenteDeEnergia":
                return Color.ORANGE;
            case "Conector":
                return Color.DARK_GRAY;
            case "Whirpool":
                return new Color(101, 224, 185);
            default:
                return null;
        }
    }
    public Color getBuildingColor(String comp){
        switch (comp) {
            case "Mina":
                return Color.YELLOW;
            case "Armeria":
                return Color.BLUE;
            case "TemploDeLaBruja":
                return Color.MAGENTA;
            case "Mercado":
                return Color.CYAN;
            case "FuenteDeEnergia":
                return Color.ORANGE;
            case "Conector":
                return Color.DARK_GRAY;
            default:
                return null;
        }
    }
    
    public Color getBuildingColor(){
        switch (player.getComponentsNotPlaced().get(buildcmBox.getSelectedIndex()-1).getHolder()) {
            case "Mina":
                return Color.YELLOW;
            case "Armeria":
                return Color.BLUE;
            case "TemploDeLaBruja":
                return Color.MAGENTA;
            case "Mercado":
                return Color.CYAN;
            case "FuenteDeEnergia":
                return Color.ORANGE;
            case "Conector":
                return Color.DARK_GRAY;
            default:
                return null;
        }
    }
    
    public void startButtonEnabler(){
        if (player.getName().equals("player1")) {
            btnStart.setEnabled(true);
        } else btnStart.setEnabled(false);
    }
    
    public void setLblShield(int shieldtype) {
        jLabel3.setIcon(new ImageIcon("Data/shield" + shieldtype + ".png"));
        jLabel3.repaint();
        jLabel3.revalidate();
    }
    
    public void atacarJugador(){
        if (mapaEnemigo.esValidoAtacar(coordAAtacar)) {
            String weaponSelected = (String) weaponSelectorCmBox.getSelectedItem();
            switch (weaponSelected){
                case "Cannon":
                if(player.findWeapon(weaponSelected) != null){
                    mapaEnemigo.realizarAtaque(coordAAtacar);
                    mapaEnemigo.actualizarVisibilidadComponente(coordAAtacar);
                    player.removeWeapon(player.findWeapon(weaponSelected));
                  }
                else{JOptionPane.showMessageDialog(null, "No tienes cannons restantes.", "Información", JOptionPane.INFORMATION_MESSAGE); }
                break;
                case "DoubleCannon":
                    if(player.findWeapon(weaponSelected) != null){
                        mapaEnemigo.realizarAtaque(coordAAtacar);
                        mapaEnemigo.actualizarVisibilidadComponente(coordAAtacar);
                        Point random = randomPoint();
                        mapaEnemigo.realizarAtaque(random);
                        mapaEnemigo.actualizarVisibilidadComponente(random);
                         random = randomPoint();
                        mapaEnemigo.realizarAtaque(random);
                        mapaEnemigo.actualizarVisibilidadComponente(random);
                         random = randomPoint();
                        mapaEnemigo.realizarAtaque(random);
                        mapaEnemigo.actualizarVisibilidadComponente(random);
                         random = randomPoint();
                        mapaEnemigo.realizarAtaque(random);
                        mapaEnemigo.actualizarVisibilidadComponente(random);
                        player.removeWeapon(player.findWeapon(weaponSelected));
                        break;
                    }
                    else{JOptionPane.showMessageDialog(null, "No tienes doublecannons restantes.", "Información", JOptionPane.INFORMATION_MESSAGE); }
                    break;
                case "Bomb":
                if(player.findWeapon(weaponSelected) != null){
                    mapaEnemigo.realizarAtaque(coordAAtacar);
                    mapaEnemigo.actualizarVisibilidadComponente(coordAAtacar);
                    mapaEnemigo.realizarAtaque(new Point(coordAAtacar.x +1, coordAAtacar.y));
                    mapaEnemigo.actualizarVisibilidadComponente(new Point(coordAAtacar.x +1, coordAAtacar.y));
                    player.removeWeapon(player.findWeapon(weaponSelected));
                }
                else{JOptionPane.showMessageDialog(null, "No tienes bombas restantes.", "Información", JOptionPane.INFORMATION_MESSAGE); }
                break;
                case "RedBeardCannon":
                    mapaEnemigo.realizarAtaque(coordAAtacar);
                    mapaEnemigo.actualizarVisibilidadComponente(coordAAtacar);
                    break;
                default: 
                JOptionPane.showMessageDialog(null, "Este arma no está disponible.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
            loadEnemyGrid(mapaEnemigo);
            player.clientHost.enviarMensaje(new Message("Broadcast", player.getName() + coordAAtacar.x + coordAAtacar.y));
            coordAAtacar = null;
        }
    }

    public static Point randomPoint() {
        return generateRandomPoint(0,0,19,19);
    }
    public static Point generateRandomPoint(int xMin, int xMax, int yMin, int yMax) {
        Random random = new Random();

        // Asegurarse de que los límites son válidos
        if (xMin > xMax || yMin > yMax) {
            throw new IllegalArgumentException("Los límites no son válidos");
        }

        // Generar valores aleatorios para x e y dentro de los límites
        int randomX = random.nextInt(xMax - xMin + 1) + xMin;
        int randomY = random.nextInt(yMax - yMin + 1) + yMin;

        return new Point(randomX, randomY);
    }

    public void abrirInventory(){
        Set<String> inventario = new LinkedHashSet<>();
        for (Token token : player.getComponentsNotPlaced()) {
            inventario.add(token.getHolder() + "\t x1");
        }
        for (Weapon weapon : player.getWeapons()) {
            inventario.add(weapon.getClass().getSimpleName() + "\t x1");
        }
        txaInventario.setText(String.join("\n", inventario));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inventoryDialog = new javax.swing.JDialog();
        jLabel8 = new javax.swing.JLabel();
        btnCloseInventory = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaInventario = new javax.swing.JTextArea();
        buildDialog = new javax.swing.JDialog();
        btnCloseBuild = new javax.swing.JButton();
        btnBuildStructure = new javax.swing.JButton();
        buildcmBox = new javax.swing.JComboBox<>();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pnlTablero = new javax.swing.JPanel();
        txfChatInput = new javax.swing.JTextField();
        btnSendText = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaChat = new javax.swing.JTextArea();
        pnlMiArea = new javax.swing.JPanel();
        pnlEnemyArea = new javax.swing.JPanel();
        pnlPlayers = new javax.swing.JPanel();
        btnP1 = new javax.swing.JButton();
        btnP2 = new javax.swing.JButton();
        btnP3 = new javax.swing.JButton();
        btnP4 = new javax.swing.JButton();
        btnShop = new javax.swing.JButton();
        btnReady = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaBitacora = new javax.swing.JTextArea();
        btnArmory = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAttack = new javax.swing.JButton();
        weaponSelectorCmBox = new javax.swing.JComboBox<>();
        btnInventory = new javax.swing.JButton();
        btnSkipTurn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblDinero = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblMetal = new javax.swing.JLabel();
        placeConnectoBtn = new javax.swing.JButton();
        lblTurnOf = new javax.swing.JLabel();
        lblTurn = new javax.swing.JLabel();
        viewGraphBtn1 = new javax.swing.JButton();
        pnlTienda = new javax.swing.JPanel();
        btnCloseShop = new javax.swing.JButton();
        btnBuildOption = new javax.swing.JButton();
        btnTrade = new javax.swing.JButton();
        pnlMenuInicio = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        inventoryDialog.setMinimumSize(new java.awt.Dimension(500, 337));
        inventoryDialog.setModal(true);
        inventoryDialog.setResizable(false);

        jLabel8.setText("INVENTORY");

        btnCloseInventory.setText("CLOSE");
        btnCloseInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseInventoryActionPerformed(evt);
            }
        });

        txaInventario.setEditable(false);
        txaInventario.setColumns(20);
        txaInventario.setRows(5);
        jScrollPane3.setViewportView(txaInventario);

        javax.swing.GroupLayout inventoryDialogLayout = new javax.swing.GroupLayout(inventoryDialog.getContentPane());
        inventoryDialog.getContentPane().setLayout(inventoryDialogLayout);
        inventoryDialogLayout.setHorizontalGroup(
            inventoryDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inventoryDialogLayout.createSequentialGroup()
                        .addGap(0, 219, Short.MAX_VALUE)
                        .addGroup(inventoryDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryDialogLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(213, 213, 213))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryDialogLayout.createSequentialGroup()
                                .addComponent(btnCloseInventory)
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryDialogLayout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())))
        );
        inventoryDialogLayout.setVerticalGroup(
            inventoryDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCloseInventory)
                .addContainerGap())
        );

        buildDialog.setMinimumSize(new java.awt.Dimension(501, 336));

        btnCloseBuild.setText("CLOSE");
        btnCloseBuild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseBuildActionPerformed(evt);
            }
        });

        btnBuildStructure.setText("BUILD");
        btnBuildStructure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuildStructureActionPerformed(evt);
            }
        });

        buildcmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NONE" }));

        javax.swing.GroupLayout buildDialogLayout = new javax.swing.GroupLayout(buildDialog.getContentPane());
        buildDialog.getContentPane().setLayout(buildDialogLayout);
        buildDialogLayout.setHorizontalGroup(
            buildDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buildDialogLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(btnBuildStructure)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
                .addComponent(btnCloseBuild)
                .addGap(80, 80, 80))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buildDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buildcmBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(215, 215, 215))
        );
        buildDialogLayout.setVerticalGroup(
            buildDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buildDialogLayout.createSequentialGroup()
                .addContainerGap(151, Short.MAX_VALUE)
                .addComponent(buildcmBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96)
                .addGroup(buildDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuildStructure)
                    .addComponent(btnCloseBuild))
                .addGap(44, 44, 44))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTablero.setBackground(new java.awt.Color(28, 28, 28));

        txfChatInput.setBackground(new java.awt.Color(102, 102, 102));
        txfChatInput.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        txfChatInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfChatInputActionPerformed(evt);
            }
        });

        btnSendText.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        btnSendText.setText("SEND");
        btnSendText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendTextActionPerformed(evt);
            }
        });

        txaChat.setEditable(false);
        txaChat.setBackground(new java.awt.Color(102, 102, 102));
        txaChat.setColumns(20);
        txaChat.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        txaChat.setRows(5);
        jScrollPane1.setViewportView(txaChat);

        pnlMiArea.setBackground(new java.awt.Color(255, 255, 255));
        pnlMiArea.setPreferredSize(new java.awt.Dimension(460, 460));

        javax.swing.GroupLayout pnlMiAreaLayout = new javax.swing.GroupLayout(pnlMiArea);
        pnlMiArea.setLayout(pnlMiAreaLayout);
        pnlMiAreaLayout.setHorizontalGroup(
            pnlMiAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );
        pnlMiAreaLayout.setVerticalGroup(
            pnlMiAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        pnlEnemyArea.setBackground(new java.awt.Color(255, 255, 255));
        pnlEnemyArea.setPreferredSize(new java.awt.Dimension(460, 460));

        javax.swing.GroupLayout pnlEnemyAreaLayout = new javax.swing.GroupLayout(pnlEnemyArea);
        pnlEnemyArea.setLayout(pnlEnemyAreaLayout);
        pnlEnemyAreaLayout.setHorizontalGroup(
            pnlEnemyAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );
        pnlEnemyAreaLayout.setVerticalGroup(
            pnlEnemyAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        pnlPlayers.setBackground(new java.awt.Color(153, 153, 153));

        btnP1.setText("P1");
        btnP1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnP1ActionPerformed(evt);
            }
        });

        btnP2.setText("P2");
        btnP2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnP2ActionPerformed(evt);
            }
        });

        btnP3.setText("P3");
        btnP3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnP3ActionPerformed(evt);
            }
        });

        btnP4.setText("P4");
        btnP4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnP4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPlayersLayout = new javax.swing.GroupLayout(pnlPlayers);
        pnlPlayers.setLayout(pnlPlayersLayout);
        pnlPlayersLayout.setHorizontalGroup(
            pnlPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlayersLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnP4)
                    .addComponent(btnP3)
                    .addComponent(btnP2)
                    .addComponent(btnP1))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlPlayersLayout.setVerticalGroup(
            pnlPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlayersLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnP1)
                .addGap(18, 18, 18)
                .addComponent(btnP2)
                .addGap(18, 18, 18)
                .addComponent(btnP3)
                .addGap(18, 18, 18)
                .addComponent(btnP4)
                .addContainerGap(290, Short.MAX_VALUE))
        );

        btnShop.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        btnShop.setText("SHOP");
        btnShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShopActionPerformed(evt);
            }
        });

        btnReady.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        btnReady.setText("READY");
        btnReady.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadyActionPerformed(evt);
            }
        });

        btnStart.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        btnStart.setText("START");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        txaBitacora.setEditable(false);
        txaBitacora.setBackground(new java.awt.Color(102, 102, 102));
        txaBitacora.setColumns(20);
        txaBitacora.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        txaBitacora.setRows(5);
        jScrollPane2.setViewportView(txaBitacora);

        btnArmory.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        btnArmory.setText("ARMERÍA");
        btnArmory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArmoryActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(255, 0, 0));
        jLabel3.setOpaque(true);

        jLabel4.setBackground(new java.awt.Color(255, 0, 0));
        jLabel4.setOpaque(true);

        btnAttack.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        btnAttack.setText("ATTACK");
        btnAttack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttackActionPerformed(evt);
            }
        });

        weaponSelectorCmBox.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        weaponSelectorCmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cannon", "Multiple Cannons", "Red Beard's Cannon", "Bomb" }));

        btnInventory.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        btnInventory.setText("INVENTORY");
        btnInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventoryActionPerformed(evt);
            }
        });

        btnSkipTurn.setText("SKIP TURN");
        btnSkipTurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSkipTurnActionPerformed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(204, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Balance: $");

        lblDinero.setBackground(new java.awt.Color(204, 255, 255));
        lblDinero.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        lblDinero.setForeground(new java.awt.Color(255, 255, 255));
        lblDinero.setText("0");

        jLabel9.setBackground(new java.awt.Color(204, 255, 255));
        jLabel9.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Metal (Kg):");

        lblMetal.setBackground(new java.awt.Color(204, 255, 255));
        lblMetal.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        lblMetal.setForeground(new java.awt.Color(255, 255, 255));
        lblMetal.setText("0");

        placeConnectoBtn.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        placeConnectoBtn.setText("Place connector");
        placeConnectoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placeConnectoBtnActionPerformed(evt);
            }
        });

        lblTurnOf.setBackground(new java.awt.Color(204, 255, 255));
        lblTurnOf.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        lblTurnOf.setForeground(new java.awt.Color(255, 255, 255));
        lblTurnOf.setText("Turno de:");

        lblTurn.setBackground(new java.awt.Color(204, 255, 255));
        lblTurn.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        lblTurn.setForeground(new java.awt.Color(255, 255, 255));
        lblTurn.setText("waiting...");

        viewGraphBtn1.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        viewGraphBtn1.setText("GRAFO");
        viewGraphBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewGraphBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTableroLayout = new javax.swing.GroupLayout(pnlTablero);
        pnlTablero.setLayout(pnlTableroLayout);
        pnlTableroLayout.setHorizontalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(pnlTableroLayout.createSequentialGroup()
                        .addComponent(txfChatInput, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSendText, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlTableroLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTableroLayout.createSequentialGroup()
                        .addComponent(pnlMiArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlEnemyArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTableroLayout.createSequentialGroup()
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnShop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnInventory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnArmory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(viewGraphBtn1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTableroLayout.createSequentialGroup()
                                .addComponent(lblMetal, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(lblTurnOf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(lblTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(135, 135, 135))
                            .addGroup(pnlTableroLayout.createSequentialGroup()
                                .addComponent(lblDinero, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(placeConnectoBtn)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(weaponSelectorCmBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSkipTurn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlTableroLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnAttack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addComponent(btnReady, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlPlayers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlTableroLayout.setVerticalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTableroLayout.createSequentialGroup()
                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTableroLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReady, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(pnlTableroLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pnlMiArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlEnemyArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTableroLayout.createSequentialGroup()
                                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAttack))
                            .addGroup(pnlTableroLayout.createSequentialGroup()
                                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnShop)
                                    .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(weaponSelectorCmBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(lblDinero)
                                        .addComponent(placeConnectoBtn)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlTableroLayout.createSequentialGroup()
                                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(btnInventory)
                                            .addComponent(jLabel9)
                                            .addComponent(lblMetal))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(btnArmory, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblTurnOf)
                                            .addComponent(lblTurn)
                                            .addComponent(viewGraphBtn1))
                                        .addGap(0, 37, Short.MAX_VALUE))
                                    .addComponent(btnSkipTurn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlTableroLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnSendText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txfChatInput)))
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18))
        );

        pnlTienda.setBackground(new java.awt.Color(28, 28, 28));

        btnCloseShop.setFont(new java.awt.Font("Tw Cen MT", 0, 36)); // NOI18N
        btnCloseShop.setText("CLOSE");
        btnCloseShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseShopActionPerformed(evt);
            }
        });

        btnBuildOption.setFont(new java.awt.Font("Tw Cen MT", 0, 36)); // NOI18N
        btnBuildOption.setText("BUILD");
        btnBuildOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuildOptionActionPerformed(evt);
            }
        });

        btnTrade.setFont(new java.awt.Font("Tw Cen MT", 0, 36)); // NOI18N
        btnTrade.setText("TRADE");
        btnTrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTradeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTiendaLayout = new javax.swing.GroupLayout(pnlTienda);
        pnlTienda.setLayout(pnlTiendaLayout);
        pnlTiendaLayout.setHorizontalGroup(
            pnlTiendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTiendaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCloseShop, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(410, 410, 410))
            .addGroup(pnlTiendaLayout.createSequentialGroup()
                .addGap(241, 241, 241)
                .addGroup(pnlTiendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTrade, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuildOption, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(246, Short.MAX_VALUE))
        );
        pnlTiendaLayout.setVerticalGroup(
            pnlTiendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTiendaLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(btnBuildOption, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnTrade, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93)
                .addComponent(btnCloseShop, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlMenuInicio.setBackground(new java.awt.Color(28, 28, 28));

        jButton2.setBackground(new java.awt.Color(28, 28, 28));
        jButton2.setFont(new java.awt.Font("Tw Cen MT", 0, 48)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("START");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 0, 24)); // NOI18N
        jLabel1.setText("You are");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel2.setText("P#");

        javax.swing.GroupLayout pnlMenuInicioLayout = new javax.swing.GroupLayout(pnlMenuInicio);
        pnlMenuInicio.setLayout(pnlMenuInicioLayout);
        pnlMenuInicioLayout.setHorizontalGroup(
            pnlMenuInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuInicioLayout.createSequentialGroup()
                .addGroup(pnlMenuInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMenuInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnlMenuInicioLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlMenuInicioLayout.createSequentialGroup()
                            .addGap(506, 506, 506)
                            .addComponent(jLabel1)))
                    .addGroup(pnlMenuInicioLayout.createSequentialGroup()
                        .addGap(431, 431, 431)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(486, Short.MAX_VALUE))
        );
        pnlMenuInicioLayout.setVerticalGroup(
            pnlMenuInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuInicioLayout.createSequentialGroup()
                .addContainerGap(293, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(213, 213, 213)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        jLayeredPane1.setLayer(pnlTablero, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlTienda, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlMenuInicio, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTienda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlTablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlMenuInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTienda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlTablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlMenuInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        getContentPane().add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnSendTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSendTextActionPerformed

    private void btnCloseShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseShopActionPerformed
        pnlMenuInicio.setVisible(false);
        pnlTienda.setVisible(false);
        pnlTablero.setVisible(true);
        lblDinero.setText(Integer.toString(player.getMoney()));
    }//GEN-LAST:event_btnCloseShopActionPerformed

    private void btnShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShopActionPerformed
        if(player.hasSearchedType(Mercado.class) || player.hasSearchedType("Mercado")){
            pnlMenuInicio.setVisible(false);
            pnlTienda.setVisible(true);
            pnlTablero.setVisible(false);
        } else 
            JOptionPane.showMessageDialog(null, "No tienes un Mercado en tu mapa.", "Información", JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_btnShopActionPerformed

    private void btnTradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTradeActionPerformed
        player.handleMarket();
    }//GEN-LAST:event_btnTradeActionPerformed

    private void btnCloseInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseInventoryActionPerformed
        inventoryDialog.setVisible(false);
    }//GEN-LAST:event_btnCloseInventoryActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        inventoryDialog.setLocationRelativeTo(jLayeredPane1);
        abrirInventory();
        inventoryDialog.setVisible(true);
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void btnCloseBuildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseBuildActionPerformed
        buildDialog.setVisible(false);
    }//GEN-LAST:event_btnCloseBuildActionPerformed

    private void btnBuildOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuildOptionActionPerformed
        setupBuildComboBox();
        buildDialog.setLocationRelativeTo(jLayeredPane1);
        buildDialog.setVisible(true);
        lblDinero.setText(Integer.toString(player.getMoney()));
    }//GEN-LAST:event_btnBuildOptionActionPerformed

    private void btnBuildStructureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuildStructureActionPerformed
        String opcion = (String) buildcmBox.getSelectedItem();
        switch (opcion) {
            case "NONE":
                JOptionPane.showMessageDialog(buildDialog, "No has seleccionado ninguna construcción!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                if (coordSeleccionadaPlayer == null)JOptionPane.showMessageDialog(buildDialog, "Debes seleccionar la coordenada donde colocarás la construcción", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    skibidiPlacement();
                }
        }
    }//GEN-LAST:event_btnBuildStructureActionPerformed

    private void btnArmoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArmoryActionPerformed
        player.handleArmory();
    }//GEN-LAST:event_btnArmoryActionPerformed

    private void txfChatInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfChatInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfChatInputActionPerformed

    private void btnReadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadyActionPerformed
        if (player.getComponentsNotPlaced().size() == 0) {
            client.enviarMensaje(new Message("ready", null));
            btnReady.setEnabled(false);
        } else JOptionPane.showMessageDialog(pnlTablero, "Te falta colocar construcciones en tu tablero!", "Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnReadyActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        client.myTurn = true;
        client.enviarMensaje(new Message("updateTurns", "true"));
        client.enviarMensaje(new Message("start", "."));
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnAttackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttackActionPerformed
        if (coordAAtacar == null ) JOptionPane.showMessageDialog(pnlTablero, "Debes seleccionar una casilla primero para atacar!", "Error", JOptionPane.ERROR_MESSAGE);
        else {

            if (mapaEnemigo == null) {
                JOptionPane.showMessageDialog(pnlTablero, "Debes seleccionar a un jugador para atacar!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                atacarJugador();
            }
        }
    }//GEN-LAST:event_btnAttackActionPerformed

    private void btnSkipTurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSkipTurnActionPerformed

        client.endTurn();
    }//GEN-LAST:event_btnSkipTurnActionPerformed

    private void placeConnectoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placeConnectoBtnActionPerformed
        player.placeConnector();
    }//GEN-LAST:event_placeConnectoBtnActionPerformed

    private void viewGraphBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewGraphBtn1ActionPerformed
        player.displayGraph();
    }//GEN-LAST:event_viewGraphBtn1ActionPerformed

    private void 
    btnP1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnP1ActionPerformed
        client.enviarMensaje(new Message("pedirTableroP1", player.getName()));
    }//GEN-LAST:event_btnP1ActionPerformed

    private void btnP2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnP2ActionPerformed
        client.enviarMensaje(new Message("pedirTableroP2", player.getName()));
    }//GEN-LAST:event_btnP2ActionPerformed

    private void btnP3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnP3ActionPerformed
        client.enviarMensaje(new Message("pedirTableroP3", player.getName()));
    }//GEN-LAST:event_btnP3ActionPerformed

    private void btnP4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnP4ActionPerformed
        client.enviarMensaje(new Message("pedirTableroP4", player.getName()));
    }//GEN-LAST:event_btnP4ActionPerformed

	private void configurarChat() {
		// Añadir ActionListener al botón de enviar texto
		btnSendText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enviarMensajeChat();
			}
		});

		// Añadir KeyListener al campo de texto para detectar ENTER
		txfChatInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enviarMensajeChat();
				}
			}
		});
	}
	
	public void recibirMensajeChat(Message mensaje) {
		// Añadir el mensaje recibido al área de chat
		txaChat.append(mensaje.getData() + "\n");
	}

    public void recibirMensajeChatString(String mensaje) {
		// Añadir el mensaje recibido al área de chat
		txaChat.append(mensaje + "\n");
	}

	private void enviarMensajeChat() {
		String mensaje = txfChatInput.getText().trim();
		if (!mensaje.isEmpty()) {
			txaChat.append("> " + player.getName() + ": " + mensaje + "\n");
			txfChatInput.setText(""); // Limpiar el campo de texto después de enviar
			client.enviarMensaje(new Message("chat", "> " + player.getName() + ": " + mensaje + "\n"));
			
		}
	}
	
    /**
     * @param args the command line arguments
     */

	public JLabel[][] getMiGrid() {
		return miGrid;
	}

	public void setMiGrid(JLabel[][] miGrid) {
		this.miGrid = miGrid;
	}

	public Point getCoordSeleccionadaPlayer() {
		return coordSeleccionadaPlayer;
	}

	public void setCoordSeleccionadaPlayer(Point coordSeleccionadaPlayer) {
		this.coordSeleccionadaPlayer = coordSeleccionadaPlayer;
	}

	public JLabel[][] getGridEnemigo() {
		return gridEnemigo;
	}

	public void setGridEnemigo(JLabel[][] gridEnemigo) {
		this.gridEnemigo = gridEnemigo;
	}

	public Point getCoordAAtacar() {
		return coordAAtacar;
	}

	public void setCoordAAtacar(Point coordAAtacar) {
		this.coordAAtacar = coordAAtacar;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public JButton getBtnArmory() {
		return btnArmory;
	}

	public void setBtnArmory(JButton btnArmory) {
		this.btnArmory = btnArmory;
	}

	public JButton getBtnAttack() {
		return btnAttack;
	}

	public void setBtnAttack(JButton btnAttack) {
		this.btnAttack = btnAttack;
	}

	public JButton getBtnBuildOption() {
		return btnBuildOption;
	}

	public void setBtnBuildOption(JButton btnBuildOption) {
		this.btnBuildOption = btnBuildOption;
	}

	public JButton getBtnBuildStructure() {
		return btnBuildStructure;
	}

	public void setBtnBuildStructure(JButton btnBuildStructure) {
		this.btnBuildStructure = btnBuildStructure;
	}

	public JButton getBtnCloseBuild() {
		return btnCloseBuild;
	}

	public void setBtnCloseBuild(JButton btnCloseBuild) {
		this.btnCloseBuild = btnCloseBuild;
	}

	public JButton getBtnCloseInventory() {
		return btnCloseInventory;
	}

	public void setBtnCloseInventory(JButton btnCloseInventory) {
		this.btnCloseInventory = btnCloseInventory;
	}

	public JButton getBtnCloseShop() {
		return btnCloseShop;
	}

	public void setBtnCloseShop(JButton btnCloseShop) {
		this.btnCloseShop = btnCloseShop;
	}

	public JButton getBtnInventory() {
		return btnInventory;
	}

	public void setBtnInventory(JButton btnInventory) {
		this.btnInventory = btnInventory;
	}

	public JButton getBtnReady() {
		return btnReady;
	}

	public void setBtnReady(JButton btnReady) {
		this.btnReady = btnReady;
	}

	public JButton getBtnSendText() {
		return btnSendText;
	}

	public void setBtnSendText(JButton btnSendText) {
		this.btnSendText = btnSendText;
	}

	public JButton getBtnShop() {
		return btnShop;
	}

	public void setBtnShop(JButton btnShop) {
		this.btnShop = btnShop;
	}

	public JButton getBtnSkipTurn() {
		return btnSkipTurn;
	}

	public void setBtnSkipTurn(JButton btnSkipTurn) {
		this.btnSkipTurn = btnSkipTurn;
	}

	public JButton getBtnTrade() {
		return btnTrade;
	}

	public void setBtnTrade(JButton btnTrade) {
		this.btnTrade = btnTrade;
	}

	public JDialog getBuildDialog() {
		return buildDialog;
	}

	public void setBuildDialog(JDialog buildDialog) {
		this.buildDialog = buildDialog;
	}

	public JComboBox<String> getBuildcmBox() {
		return buildcmBox;
	}

	public void setBuildcmBox(JComboBox<String> buildcmBox) {
		this.buildcmBox = buildcmBox;
	}

	public JDialog getInventoryDialog() {
		return inventoryDialog;
	}

	public void setInventoryDialog(JDialog inventoryDialog) {
		this.inventoryDialog = inventoryDialog;
	}

	public JButton getjButton2() {
		return jButton2;
	}

	public void setjButton2(JButton jButton2) {
		this.jButton2 = jButton2;
	}

	public JButton getjButton6() {
		return btnStart;
	}

	public void setjButton6(JButton jButton6) {
		this.btnStart = jButton6;
	}

	public JLabel getjLabel1() {
		return jLabel1;
	}

	public void setjLabel1(JLabel jLabel1) {
		this.jLabel1 = jLabel1;
	}

	public JLabel getjLabel2() {
		return jLabel2;
	}

	public void setjLabel2(JLabel jLabel2) {
		this.jLabel2 = jLabel2;
	}

	public JLabel getjLabel3() {
		return jLabel3;
	}

	public void setjLabel3(JLabel jLabel3) {
		this.jLabel3 = jLabel3;
	}

	public JLabel getjLabel4() {
		return jLabel4;
	}

	public void setjLabel4(JLabel jLabel4) {
		this.jLabel4 = jLabel4;
	}

	public JLabel getjLabel6() {
		return jLabel6;
	}

	public void setjLabel6(JLabel jLabel6) {
		this.jLabel6 = jLabel6;
	}

	public JLabel getjLabel8() {
		return jLabel8;
	}

	public void setjLabel8(JLabel jLabel8) {
		this.jLabel8 = jLabel8;
	}

	public JLabel getjLabel9() {
		return jLabel9;
	}

	public void setjLabel9(JLabel jLabel9) {
		this.jLabel9 = jLabel9;
	}

	public JLayeredPane getjLayeredPane1() {
		return jLayeredPane1;
	}

	public void setjLayeredPane1(JLayeredPane jLayeredPane1) {
		this.jLayeredPane1 = jLayeredPane1;
	}

	public JScrollPane getjScrollPane1() {
		return jScrollPane1;
	}

	public void setjScrollPane1(JScrollPane jScrollPane1) {
		this.jScrollPane1 = jScrollPane1;
	}

	public JScrollPane getjScrollPane2() {
		return jScrollPane2;
	}

	public void setjScrollPane2(JScrollPane jScrollPane2) {
		this.jScrollPane2 = jScrollPane2;
	}

	public JScrollPane getjScrollPane3() {
		return jScrollPane3;
	}

	public void setjScrollPane3(JScrollPane jScrollPane3) {
		this.jScrollPane3 = jScrollPane3;
	}

	public JTextArea getjTextArea3() {
		return txaInventario;
	}

	public void setjTextArea3(JTextArea jTextArea3) {
		this.txaInventario = jTextArea3;
	}

	public JLabel getLblDinero() {
		return lblDinero;
	}

	public void setLblDinero(JLabel lblDinero) {
		this.lblDinero = lblDinero;
	}

	public JLabel getLblMetal() {
		return lblMetal;
	}

	public void setLblMetal(JLabel lblMetal) {
		this.lblMetal = lblMetal;
	}

	public JPanel getPnlEnemyArea() {
		return pnlEnemyArea;
	}

	public void setPnlEnemyArea(JPanel pnlEnemyArea) {
		this.pnlEnemyArea = pnlEnemyArea;
	}

	public JPanel getPnlMenuInicio() {
		return pnlMenuInicio;
	}

	public void setPnlMenuInicio(JPanel pnlMenuInicio) {
		this.pnlMenuInicio = pnlMenuInicio;
	}

	public JPanel getPnlMiArea() {
		return pnlMiArea;
	}

	public void setPnlMiArea(JPanel pnlMiArea) {
		this.pnlMiArea = pnlMiArea;
	}

	public JPanel getPnlPlayers() {
		return pnlPlayers;
	}

	public void setPnlPlayers(JPanel pnlPlayers) {
		this.pnlPlayers = pnlPlayers;
	}

	public JPanel getPnlTablero() {
		return pnlTablero;
	}

	public void setPnlTablero(JPanel pnlTablero) {
		this.pnlTablero = pnlTablero;
	}

	public JPanel getPnlTienda() {
		return pnlTienda;
	}

	public void setPnlTienda(JPanel pnlTienda) {
		this.pnlTienda = pnlTienda;
	}

	public JTextArea getTxaBitacora() {
		return txaBitacora;
	}

	public void setTxaBitacora(JTextArea txaBitacora) {
		this.txaBitacora = txaBitacora;
	}

	public JTextArea getTxaChat() {
		return txaChat;
	}

	public void setTxaChat(JTextArea txaChat) {
		this.txaChat = txaChat;
	}

	public JTextField getTxfChatInput() {
		return txfChatInput;
	}

	public void setTxfChatInput(JTextField txfChatInput) {
		this.txfChatInput = txfChatInput;
	}

	public JComboBox<String> getWeaponSelectorCmBox() {
		return weaponSelectorCmBox;
	}

	public void setWeaponSelectorCmBox(JComboBox<String> weaponSelectorCmBox) {
		this.weaponSelectorCmBox = weaponSelectorCmBox;
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArmory;
    private javax.swing.JButton btnAttack;
    private javax.swing.JButton btnBuildOption;
    private javax.swing.JButton btnBuildStructure;
    private javax.swing.JButton btnCloseBuild;
    private javax.swing.JButton btnCloseInventory;
    private javax.swing.JButton btnCloseShop;
    private javax.swing.JButton btnInventory;
    private javax.swing.JButton btnP1;
    private javax.swing.JButton btnP2;
    private javax.swing.JButton btnP3;
    private javax.swing.JButton btnP4;
    private javax.swing.JButton btnReady;
    private javax.swing.JButton btnSendText;
    private javax.swing.JButton btnShop;
    private javax.swing.JButton btnSkipTurn;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnTrade;
    private javax.swing.JDialog buildDialog;
    private javax.swing.JComboBox<String> buildcmBox;
    private javax.swing.JDialog inventoryDialog;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDinero;
    private javax.swing.JLabel lblMetal;
    private javax.swing.JLabel lblTurn;
    private javax.swing.JLabel lblTurnOf;
    private javax.swing.JButton placeConnectoBtn;
    private javax.swing.JPanel pnlEnemyArea;
    private javax.swing.JPanel pnlMenuInicio;
    private javax.swing.JPanel pnlMiArea;
    private javax.swing.JPanel pnlPlayers;
    private javax.swing.JPanel pnlTablero;
    private javax.swing.JPanel pnlTienda;
    private javax.swing.JTextArea txaBitacora;
    private javax.swing.JTextArea txaChat;
    private javax.swing.JTextArea txaInventario;
    private javax.swing.JTextField txfChatInput;
    private javax.swing.JButton viewGraphBtn1;
    private javax.swing.JComboBox<String> weaponSelectorCmBox;
    // End of variables declaration//GEN-END:variables
}
