package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Archivismo;

public class Arma implements Serializable {
	private static final long serialVersionUID = 1L;
    public String nombre;
    public Map<Tipo, Integer> dmgPorTipo;
    public boolean usada;
	public int menor;
	public int mayor;
	public Archivismo archivismo;

    public Arma(String nombre) {
		this.archivismo = new Archivismo();
		leerArchivoConfig();
        this.nombre = nombre;
        this.dmgPorTipo = generarDanioPorTipo();
        this.usada = false;
    }
	
	public Arma(String nombre, Map<Tipo,Integer> danios){
		this.nombre = nombre;
		this.dmgPorTipo = danios;
		this.usada = false;
	}

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public Map<Tipo, Integer> getDanioPorTipo() {
        return dmgPorTipo;
    }

    public boolean isUsada() {
        return usada;
    }
	
	public void leerArchivoConfig() {
		if (archivismo.fileExistsInFolder("Config", "PorcentajeArmas.txt")) {
			List<String> contenido = null;
			try {
				// Leer archivo y asignar su contenido
				contenido = archivismo.leerArchivo("Config", "PorcentajeArmas.txt");
			} catch (IOException ex) {
				// Loguear el error si ocurre al leer el archivo
				Logger.getLogger(Arma.class.getName()).log(Level.SEVERE, null, ex);
			}

			if (contenido != null && contenido.size() >= 2) { // Validar que hay al menos dos líneas
				try {
					// Convertir las líneas a enteros
					menor = Integer.parseInt(contenido.get(0).trim()); 
					mayor = Integer.parseInt(contenido.get(1).trim()); 
				} catch (NumberFormatException e) {
					// Manejar el caso de valores no numéricos
					System.out.println("Error: uno de los valores no es un número entero válido.");
					e.printStackTrace();
				}
			} else {
				// Manejar el caso donde el archivo no contiene suficientes líneas
				System.out.println("Error: el archivo no tiene suficientes líneas.");
			}
		} else {
			// Manejar el caso donde el archivo no existe
			System.out.println("Error: el archivo PorcentajeArmas.txt no existe en la carpeta Config.");
		}
	}


	public Map<Tipo, Integer> generarDanioPorTipo() {
		Map<Tipo, Integer> danios = new EnumMap<>(Tipo.class);
		Random random = new Random();
		for (Tipo tipo : Tipo.values()) {
			// Generar un número entre 'menor' y 'mayor' (ambos inclusivos)
			int porcentajeDanio = menor + random.nextInt((mayor - menor) + 1);
			danios.put(tipo, porcentajeDanio); // Guardar el daño como Integer
		}
		return danios;
	}


	public void setUtilizada(boolean usada) {
		this.usada = usada;
	}
	
    public double usarArma(Tipo tipoObjetivo) {
        if (!usada) {
            usada = true;
            return dmgPorTipo.get(tipoObjetivo);
        } else {
            return 0.0; // El arma ya fue utilizada
        }
    }
	
	@Override
	public String toString() {
		return "Arma{" +
				"nombre='" + nombre + '\'' +
				", danioPorTipo=" + dmgPorTipo +
				", usada=" + usada +
				'}';
}
}
