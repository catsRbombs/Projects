package game;

import java.io.Serializable;
import java.util.List;
import com.google.gson.Gson;
import game.components.Armeria;

import game.components.Component;
import game.components.Conector;
import game.components.FuenteDeEnergia;
import game.components.Mercado;
import game.components.Mina;
import game.components.TemploDeLaBruja;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import utils.Constants;

/**
 * La clase Map representa la matriz de 20x20.
 * Gestiona la colocación y destrucción de componentes.
 * Actualiza el grafo basado en las conexiones.
 */
public class MapJuego implements Serializable {

    public Component[][] grid;
    public int[][] intGrid; // Matriz entera para representar el estado
    public Graph graph;

    public static MapJuego fromStringRepresentation(String data) {
        System.out.println("Contenido del JSON: " + data);
        Gson gson = new Gson();
        return gson.fromJson(data, MapJuego.class);
    }
	
	// Dentro de MapJuego
	private List<ConectorReference> conectorReferences = new ArrayList<>();

	// Clase interna para almacenar las referencias temporales
	private static class ConectorReference {
		Conector conector;
		int fromX, fromY, toX, toY;

		public ConectorReference(Conector conector, int fromX, int fromY, int toX, int toY) {
			this.conector = conector;
			this.fromX = fromX;
			this.fromY = fromY;
			this.toX = toX;
			this.toY = toY;
		}
	}


    /**
     * Constructor de la clase Map.
     */
    public MapJuego() {
        grid = new Component[Constants.MAP_SIZE][Constants.MAP_SIZE];
        intGrid = new int[Constants.MAP_SIZE][Constants.MAP_SIZE]; // Inicializar intGrid
        graph = new Graph(); // Inicializar el grafo
    }

	public MapJuego(String compressedMap) {
		System.out.println("HOLAAAAAA: \n"+compressedMap);
		grid = new Component[Constants.MAP_SIZE][Constants.MAP_SIZE];
		intGrid = new int[Constants.MAP_SIZE][Constants.MAP_SIZE];
		graph = new Graph(); // Inicializar el grafo

		String[] lines = compressedMap.split("\n");
		int lineIndex = 0;

		// Leer Grid
		if (!lines[lineIndex].equals("Grid:")) {
			throw new IllegalArgumentException("Datos de mapa invalidos");
		}
		lineIndex++;

		// Leer y reconstruir grid
		for (int i = 0; i < Constants.MAP_SIZE; i++) {
			String[] cells = lines[lineIndex].split(",");
			for (int j = 0; j < Constants.MAP_SIZE; j++) {
				String cellData = cells[j];
				if (cellData.equals("null")) {
					grid[i][j] = null;
				} else {
					Component comp = deserializeComponent(cellData);
					grid[i][j] = comp;
				}
			}
			lineIndex++;
		}

		// Leer IntGrid
		if (!lines[lineIndex].equals("IntGrid:")) {
			throw new IllegalArgumentException("Datos de mapa inválidos");
		}
		lineIndex++;

		// Leer y reconstruir intGrid
		for (int i = 0; i < Constants.MAP_SIZE; i++) {
			String[] cells = lines[lineIndex].split(",");
			for (int j = 0; j < Constants.MAP_SIZE; j++) {
				intGrid[i][j] = Integer.parseInt(cells[j]);
			}
			lineIndex++;
		}

		// Reconstruir el grafo si es necesario
		rebuildGraph();
	}
	
private Component deserializeComponent(String data) {
    String[] parts = data.split(";");
    Map<String, String> properties = new HashMap<>();
    for (String part : parts) {
        String[] keyValue = part.split("=");
        if (keyValue.length == 2) {
            properties.put(keyValue[0], keyValue[1]);
        }
    }

    String type = properties.get("Type");

    // Propiedades comunes
    String name = properties.get("name");
    int positionX = Integer.parseInt(properties.get("positionX"));
    int positionY = Integer.parseInt(properties.get("positionY"));
    int width = Integer.parseInt(properties.get("width"));
    int height = Integer.parseInt(properties.get("height"));
    int cost = Integer.parseInt(properties.get("cost"));
    boolean visibleToEnemies = Boolean.parseBoolean(properties.get("visibleToEnemies"));

    // Crear componente basado en el tipo
    Component comp = null;
    if (type.equals("Conector")) {
        Conector conector = new Conector(new Point(positionX, positionY), width, height, cost, name);
        conector.setVisibleToEnemies(visibleToEnemies);

        // Obtener posiciones de los componentes conectados
        int fromX = Integer.parseInt(properties.get("fromX"));
        int fromY = Integer.parseInt(properties.get("fromY"));
        int toX = Integer.parseInt(properties.get("toX"));
        int toY = Integer.parseInt(properties.get("toY"));

        // Si las coordenadas son -1, las tratamos como nulas
        if (fromX == -1 && fromY == -1) {
            fromX = -1;
            fromY = -1;
        }
        if (toX == -1 && toY == -1) {
            toX = -1;
            toY = -1;
        }

        // Almacenar las referencias temporales
        conectorReferences.add(new ConectorReference(conector, fromX, fromY, toX, toY));

        comp = conector;
    } else if (type.equals("FuenteDeEnergia")) {
        FuenteDeEnergia fuente = new FuenteDeEnergia(new Point(positionX, positionY), name);
        fuente.setVisibleToEnemies(visibleToEnemies);
        comp = fuente;
    } else if (type.equals("Mina")) {
        Mina mina = new Mina(new Point(positionX, positionY), width, height, new Player(), 0);
        mina.setVisibleToEnemies(visibleToEnemies);
        comp = mina;
    } else if (type.equals("Armeria")) {
        Armeria armeria = new Armeria(new Point(positionX, positionY), new Player(), 0);
        armeria.setVisibleToEnemies(visibleToEnemies);
        comp = armeria;
    } else if (type.equals("TemploDeLaBruja")) {
        TemploDeLaBruja templo = new TemploDeLaBruja(new Point(positionX, positionY), new Player(), 0);
        templo.setVisibleToEnemies(visibleToEnemies);
        comp = templo;
    } else if (type.equals("Mercado")) {
        Mercado mercado = new Mercado(new Point(positionX, positionY), 0);
        mercado.setVisibleToEnemies(visibleToEnemies);
        comp = mercado;
    } 
    // Agrega aquí la deserialización de otras subclases si es necesario

    return comp;
}


	
	private void rebuildGraph() {
		// Crear un mapa de posición a componente para resolver referencias
		Map<Point, Component> positionToComponent = new HashMap<>();
		for (int i = 0; i < Constants.MAP_SIZE; i++) {
			for (int j = 0; j < Constants.MAP_SIZE; j++) {
				Component comp = grid[i][j];
				if (comp != null) {
					positionToComponent.put(comp.getPosition(), comp);
				}
			}
		}

		// Resolver referencias en Conectores
		for (ConectorReference cr : conectorReferences) {
			Conector conector = cr.conector;
			Component fromComp = null;
			Component toComp = null;

			if (cr.fromX != -1 && cr.fromY != -1) {
				fromComp = positionToComponent.get(new Point(cr.fromX, cr.fromY));
			}
			if (cr.toX != -1 && cr.toY != -1) {
				toComp = positionToComponent.get(new Point(cr.toX, cr.toY));
			}

			if (fromComp != null && toComp != null) {
				conector.setConnectedComponents(fromComp, toComp);
				graph.addEdge(fromComp, toComp);
			}
		}

		// Agregar otros componentes al grafo
		for (Component comp : positionToComponent.values()) {
			if (!(comp instanceof Conector)) {
				graph.addComponent(comp);
			}
		}
	}

	
	    public void updateFromMap(MapJuego otherMap) {
        for (int i = 0; i < Constants.MAP_SIZE; i++) {
            for (int j = 0; j < Constants.MAP_SIZE; j++) {
                // Actualizar grid si hay diferencias
                Component myComp = this.grid[i][j];
                Component otherComp = otherMap.grid[i][j];

                if (componentsAreDifferent(myComp, otherComp)) {
                    this.grid[i][j] = otherComp;
                }

                // Actualizar intGrid si hay diferencias
                if (this.intGrid[i][j] != otherMap.intGrid[i][j]) {
                    this.intGrid[i][j] = otherMap.intGrid[i][j];
                }
            }
        }

        // Reconstruir el grafo si es necesario
        rebuildGraph();
    }
	
	
	
	private boolean componentsAreDifferent(Component c1, Component c2) {
		if (c1 == null && c2 == null) {
			return false;
		}
		if (c1 == null || c2 == null) {
			return true;
		}
		if (!c1.getClass().equals(c2.getClass())) {
			return true;
		}
		// Comparar propiedades comunes
		if (!c1.getName().equals(c2.getName())) return true;
		if (!c1.getPosition().equals(c2.getPosition())) return true;
		if (c1.getWidth() != c2.getWidth()) return true;
		if (c1.getHeight() != c2.getHeight()) return true;
		if (c1.getCost() != c2.getCost()) return true;
		if (c1.isVisibleToEnemies() != c2.isVisibleToEnemies()) return true;

		// Comparar propiedades específicas de subclases
		if (c1 instanceof Conector && c2 instanceof Conector) {
			Conector con1 = (Conector) c1;
			Conector con2 = (Conector) c2;

			Point con1FromPos = (con1.getFromComponent() != null) ? con1.getFromComponent().getPosition() : null;
			Point con1ToPos = (con1.getToComponent() != null) ? con1.getToComponent().getPosition() : null;
			Point con2FromPos = (con2.getFromComponent() != null) ? con2.getFromComponent().getPosition() : null;
			Point con2ToPos = (con2.getToComponent() != null) ? con2.getToComponent().getPosition() : null;

			if (!Objects.equals(con1FromPos, con2FromPos)) return true;
			if (!Objects.equals(con1ToPos, con2ToPos)) return true;
		}

		// Agrega aquí comparaciones para otras subclases si es necesario

		return false;
	}





	
	public String toStringRepresentation() {
		StringBuilder sb = new StringBuilder();

		// Serializar grid
		sb.append("Grid:\n");
		for (int i = 0; i < Constants.MAP_SIZE; i++) {
			for (int j = 0; j < Constants.MAP_SIZE; j++) {
				Component comp = grid[i][j];
				if (comp == null) {
					sb.append("null");
				} else {
					sb.append(serializeComponent(comp));
				}
				if (j < Constants.MAP_SIZE - 1) {
					sb.append(",");
				}
			}
			sb.append("\n");
		}

		// Serializar intGrid
		sb.append("IntGrid:\n");
		for (int i = 0; i < Constants.MAP_SIZE; i++) {
			for (int j = 0; j < Constants.MAP_SIZE; j++) {
				sb.append(intGrid[i][j]);
				if (j < Constants.MAP_SIZE - 1) {
					sb.append(",");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}
	
	private String serializeComponent(Component comp) {
		StringBuilder sb = new StringBuilder();

		sb.append("Type=").append(comp.getClass().getSimpleName()); // Tipo de componente

		// Propiedades comunes
		sb.append(";name=").append(comp.getName());
		sb.append(";positionX=").append(comp.getPosition().x);
		sb.append(";positionY=").append(comp.getPosition().y);
		sb.append(";width=").append(comp.getWidth());
		sb.append(";height=").append(comp.getHeight());
		sb.append(";cost=").append(comp.getCost());
		sb.append(";visibleToEnemies=").append(comp.isVisibleToEnemies());

		// Propiedades específicas de subclases
		if (comp instanceof Conector) {
			Conector conector = (Conector) comp;
			// Usamos las posiciones de los componentes conectados si están disponibles
			if (conector.getFromComponent() != null) {
				sb.append(";fromX=").append(conector.getFromComponent().getPosition().x);
				sb.append(";fromY=").append(conector.getFromComponent().getPosition().y);
			} else {
				sb.append(";fromX=-1;fromY=-1");
			}
			if (conector.getToComponent() != null) {
				sb.append(";toX=").append(conector.getToComponent().getPosition().x);
				sb.append(";toY=").append(conector.getToComponent().getPosition().y);
			} else {
				sb.append(";toX=-1;toY=-1");
			}
		}

		// Agrega aquí la serialización de otras subclases si es necesario

		return sb.toString();
	}




    /**
     * Coloca un componente en una posición específica.
     *
     * @param component Componente a colocar
     * @param x         Coordenada X
     * @param y         Coordenada Y
     * @return true si se colocó exitosamente, false en caso contrario
     */
    public boolean placeComponent(Component component, int x, int y) {
        // Verificar si el componente cabe en la posición
        if (canPlaceComponent(component, x, y)) {
            // Colocar el componente en el grid y actualizar intGrid
            for (int i = 0; i < component.getWidth(); i++) {
                for (int j = 0; j < component.getHeight(); j++) {
                    grid[x + i][y + j] = component;
                    intGrid[x + i][y + j] = 1; // Marcar como casilla con componente
                }
            }

            // Si el componente es un conector, conectar los dos componentes
            if (component instanceof Conector) {
                Conector connector = (Conector) component;
                Component from = connector.getFromComponent();
                Component to = connector.getToComponent();
                if (from != null && to != null) {
                    graph.addEdge(from, to);
                }
            } else {
                // Agregar el componente al grafo
                graph.addComponent(component);
            }

            return true;
        } else {
            // No se puede colocar el componente aquí
            return false;
        }
    }


    private boolean canPlaceComponent(Component component, int x, int y) {
        // Verificar límites
        if (x + component.getWidth() > Constants.MAP_SIZE || y + component.getHeight() > Constants.MAP_SIZE) {
            return false;
        }
        // Verificar si el espacio está disponible
        for (int i = 0; i < component.getWidth(); i++) {
            for (int j = 0; j < component.getHeight(); j++) {
                if (grid[x + i][y + j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Elimina un componente de una posición específica.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    public void removeComponent(int x, int y) {
        Component component = grid[x][y];
        if (component != null) {
            int startX = component.getPosition().x;
            int startY = component.getPosition().y;
            // Remover el componente del grid y actualizar intGrid
            for (int i = 0; i < component.getWidth(); i++) {
                for (int j = 0; j < component.getHeight(); j++) {
                    grid[startX + i][startY + j] = null;
                    intGrid[startX + i][startY + j] = 0; // Marcar como casilla vacía
                }
            }

            // Remover el componente del grafo
            graph.removeComponent(component);

            // Si el componente es un conector o fuente de energía, verificar componentes desconectados
            if (component instanceof Conector || component instanceof FuenteDeEnergia) {
                List<Component> disconnected = graph.getDisconnectedComponents();
                // Manejar la visibilidad de los componentes desconectados
                for (Component disconnectedComponent : disconnected) {
                    // Hacer que el componente sea visible para los enemigos
                    disconnectedComponent.setVisibleToEnemies(true);
                }
            }
        }
    }

    /**
     * Marca una casilla como atacada.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    public void markCellAsAttacked(int x, int y) {
        // Verificar si las coordenadas están dentro de los límites
        if (x >= 0 && x < Constants.MAP_SIZE && y >= 0 && y < Constants.MAP_SIZE) {
            intGrid[x][y] = 2; // Marcar como casilla atacada
            // Si deseas realizar acciones adicionales al ser atacada, puedes agregarlas aquí
        }
    }

    /**
     * Obtiene el componente en una posición específica.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return Componente en la posición o null si no hay ninguno
     */
    public Component getComponent(int x, int y) {
        return grid[x][y];
    }

    /**
     * Obtiene el valor del intGrid en una posición específica.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return Valor entero en la posición
     */
    public int getIntGridValue(int x, int y) {
        return intGrid[x][y];
    }

    /**
     * Actualiza el grafo de conexiones.
     */
    public void updateGraph() {
        // Implementar lógica para mantener actualizado el grafo si es necesario
    }
    
    public boolean esValidoAtacar(Point punto) {
        int x = punto.x;
        int y = punto.y;

        // Verifica límites de la matriz
        if (x < 0 || x >= intGrid.length || y < 0 || y >= intGrid[0].length) {
            return false; // Punto fuera de los límites
        }

        // Una casilla es válida si no ha sido atacada antes (-1 o -2)
        return intGrid[x][y] == 0 || intGrid[x][y] == 1;
    }
    
    public boolean realizarAtaque(Point punto) {
        int x = punto.x;
        int y = punto.y;
        System.out.println("Atacando en" + x + " " + y);
        // Actualiza el estado de la casilla en el intGrid
        if (intGrid[x][y] == 1) { // Había un componente
            intGrid[x][y] = -2;   // Atacado y le atinó
            return true;
        } else {
            intGrid[x][y] = -1;   // Atacado pero no le atinó a nada
            return false;
        }
    }
    
    public boolean fueAtaqueExitoso(Point punto){
        int x = punto.x;
        int y = punto.y;
        System.out.println("Revisa en" + x + " " + y);
        // Actualiza el estado de la casilla en el intGrid
        if (intGrid[x][y] == -2) { // Había un componente
            return true;
        } else {
            intGrid[x][y] = -1;   // Atacado pero no le atinó a nada
            return false;
        }
    }
    
    public void actualizarVisibilidadComponente(Point punto) {
        int x = punto.x;
        int y = punto.y;

//        // Verifica límites de la matriz
//        if (x < 0 || x >= intGrid.length || y < 0 || y >= intGrid[0].length) {
//            throw new IllegalArgumentException("El punto está fuera de los límites del mapa.");
//        }

        // Verifica si la casilla tiene un componente y ha sido destruida
        if (intGrid[x][y] == -2) { // Casilla atacada y componente destruido
            Component componente = grid[x][y];
            if (componente != null) {
                componente.setVisibleToEnemies(true); // Actualiza visibilidad
            }
        }
    }
}
