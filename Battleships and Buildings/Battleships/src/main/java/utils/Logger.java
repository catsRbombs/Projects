// utils/Logger.java

package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * La clase Logger registra eventos importantes y errores.
 * Implementa el patrón Singleton.
 */
public class Logger {

    private static Logger instance = null;
    private BufferedWriter writer;
    private static final String LOG_FILE = "application.log";


    /**
     * Constructor privado para el patrón Singleton.
     */
    public Logger() {
        try {
            // Abre el archivo de log en modo append
            writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            System.err.println("No se pudo inicializar el Logger: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única de Logger.
     *
     * @return Instancia de Logger
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }


    /**
     * Registra un mensaje en el log con un nivel específico.
     *
     * @param level   Nivel del mensaje
     * @param message Mensaje a registrar
     */
	public synchronized void log(String message) {
		String timestamp = LocalDateTime.now().toString();
		String logMessage = String.format("[%s] %s", timestamp, message); // Corregido
		System.out.println(logMessage); // También imprime en consola

		try {
			writer.write(logMessage);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
		}
}


    /**
     * Cierra el Logger y libera recursos.
     */
    public synchronized void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el Logger: " + e.getMessage());
        }
    }
}
