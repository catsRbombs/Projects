package model;

import java.io.Serializable;

public enum Tipo implements Serializable {
	
    FUEGO,
    AIRE,
    AGUA,
    MAGIA_BLANCA,
    MAGIA_OSCURA,
    ELECTRICIDAD,
    HIELO,
    ACIDO,
    ESPIRITUALIDAD,
    HIERRO;

	public String getImagen() {
		String basePath = System.getProperty("user.dir"); // Ruta base del proyecto
		String path =  basePath + "\\Data\\" + this.toString() + ".jpg";
		//System.out.println("Ruta generada: " + path); // Depuración
		return path;
	}

    @Override
    public String toString() {
        switch (this) {
            case FUEGO:
                return "Fuego";
            case AIRE:
                return "Aire";
            case AGUA:
                return "Agua";
            case MAGIA_BLANCA:
                return "Magia_Blanca";
            case MAGIA_OSCURA:
                return "Magia_Oscura";
            case ELECTRICIDAD:
                return "Electricidad";
            case HIELO:
                return "Hielo";
            case ACIDO:
                return "Acido";
            case ESPIRITUALIDAD:
                return "Espiritualidad";
            case HIERRO:
                return "Hierro";
        }
		return null;
    }

}


