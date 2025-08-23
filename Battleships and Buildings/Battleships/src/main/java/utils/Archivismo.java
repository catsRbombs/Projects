
package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Archivismo {
	
	

    public List<String> leerArchivo(String folder, String fileName) throws IOException {
        File file = new File(folder, fileName);
        if (!file.exists()) {
            return null;
        }
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        return lines;
    }
    
    public static String loadFileToString(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        }

        return contentBuilder.toString();
    }
    
    public void createFile(String folder, String fileName, String content) throws IOException {
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs(); // Crear directorios si no existen
        }
        File file = new File(dir, fileName);
        // 'false' indica que no se debe agregar al archivo existente, sino sobrescribirlo
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }
    
    public boolean fileExistsInFolder(String folder, String fileName) {
        File file = new File(folder, fileName);
        return file.exists();
    }
    
    public void replaceFileContent(String message, String directory, String fileName) {
        // Construir la ruta completa del archivo
        File file = new File(directory, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            // Escribir el mensaje al archivo (sobrescribe el contenido)
            writer.write(message);
            System.out.println("El archivo ha sido actualizado exitosamente.");
        } catch (IOException e) {
            // Manejar errores
            System.err.println("Ocurrió un error al actualizar el archivo: " + e.getMessage());
        }
    }
	
	public static void appendToFile(String directory, String fileName, String message) {
		// Construir la ruta completa del archivo
		File file = new File(directory, fileName);
		System.out.println("Ruta completa del archivo: " + file.getAbsolutePath());

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) { // true para agregar en vez de sobrescribir
			// Agregar el mensaje al archivo
			writer.write(message);
			writer.newLine(); // Opcional: agregar una nueva línea después del mensaje
			System.out.println("El contenido ha sido agregado exitosamente al archivo.");
		} catch (IOException e) {
			// Manejar errores
			System.err.println("Ocurrió un error al agregar al archivo: " + e.getMessage());
		}
	}
	
	private String path = "skibidipiratas\\Config\\minaConfig.json";
	
	public int getMinimumDamage(){
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(path)));
            JSONObject json = new JSONObject(contenido);
            return json.getInt("lowerEnd");
        } catch (IOException e) {e.printStackTrace();} 
        catch (Exception e) {e.printStackTrace();        }
        return -1;
    }
    
    public int getMaximumDamage(){
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(path)));
            JSONObject json = new JSONObject(contenido);
            return json.getInt("higherEnd");
        } catch (IOException e) {e.printStackTrace();} 
        catch (Exception e) {e.printStackTrace();        }
        return -1;
    }
}
