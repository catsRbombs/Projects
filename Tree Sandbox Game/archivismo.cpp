#include "archivismo.h"

QString nombres;
QString apellidos;       // Variable para apellidos
QString religiones;      // Variable para religiones
QString paises;          // Variable para países
QString profesiones;      // Variable para profesiones
QString muerte;
QString bitacoraMuerte;
QString buscarPorIdTxt;
QString humanidad;
QString ultimoNivel;
QString arbolHumanidad;
QString verHeap;
QString verReencarnaciones;
QString verArbolAngeles;
QString verRanking;
QString bitacoraAngel;
// El arreglo contendrá referencias a las variables
QString* bitacoras[] = {
    &nombres, &apellidos, &religiones, &paises, &profesiones, &muerte, &bitacoraMuerte, &buscarPorIdTxt, &humanidad, &ultimoNivel, &arbolHumanidad, &verHeap, &verReencarnaciones, &verArbolAngeles, &verRanking, &bitacoraAngel
};

void introducirPaths(const QString& carpetaPaths) {
    *bitacoras[0] = carpetaPaths + "/Nombres.txt";
    *bitacoras[1] = carpetaPaths + "/Apellidos.txt";
    *bitacoras[2] = carpetaPaths + "/Religiones.txt";
    *bitacoras[3] = carpetaPaths + "/Paises.txt";
    *bitacoras[4] = carpetaPaths + "/Profesiones.txt";
    *bitacoras[5] = carpetaPaths + "/Muerte.txt";
    *bitacoras[6] = carpetaPaths + "/BitacoraMuertes.txt";
    *bitacoras[7] = carpetaPaths + "/BuscarPorId.txt";
    *bitacoras[8] = carpetaPaths + "/Humanidad.txt";
    *bitacoras[9] = carpetaPaths + "/UltimoNivel.txt";
    *bitacoras[10] = carpetaPaths + "/ArbolHumanidad.txt";
    *bitacoras[11] = carpetaPaths + "/VerHeap.txt";
    *bitacoras[12] = carpetaPaths + "/VerReencarnaciones.txt";
    *bitacoras[13] = carpetaPaths + "/VerArbolAngeles.txt";
    *bitacoras[14] = carpetaPaths + "/VerRanking.txt";
    *bitacoras[15] = carpetaPaths + "/bitacoraAngel.txt";
}

void generarCaminoBitacora() {
    QString camino = QFileInfo(__FILE__).absolutePath();
    camino = QDir(camino).filePath("Data");

    // Crear la carpeta si no existe
    if (!QDir(camino).exists()) {
        if (QDir().mkpath(camino)) {
            qDebug() << "Carpeta creada exitosamente:" << camino;
        } else {
            qDebug() << "Error al crear la carpeta:" << camino;
            return;  // Si no se puede crear la carpeta, salir de la función
        }
    }

    // Ahora asignamos las rutas a las variables
    introducirPaths(camino);
}

void resetearArchivoEspecifico(QString qpath) {
      string path = qpath.toStdString();  // Convertir QString a   string

    // Abrir archivo en modo truncar para vaciarlo
      ofstream archivo(path,   ios::trunc);
    if (archivo.is_open()) {
        archivo << "\n";  // Escribir una nueva línea para inicializar el archivo vacío
        archivo.close();  // Cerrar el archivo
    } else {
          cerr << "Error al abrir el archivo: " << path <<   endl;
    }
}

void resetearArchivos() { //NO LLAMAR NUNCA O PERDEMOS LA DATA !!!!
    for (int i = 0; i < 8; ++i) {
        QString qpath = *bitacoras[i];  // Usar el valor referenciado
          string path = qpath.toStdString();  // Convertir QString a   string

        // Abrir archivo en modo truncar para vaciarlo
          ofstream archivo(path,   ios::trunc);
        if (archivo.is_open()) {
            archivo << "\n";  // Escribir una nueva línea para inicializar el archivo vacío
            archivo.close();  // Cerrar el archivo
        } else {
              cerr << "Error al abrir el archivo: " << path <<   endl;
        }
    }
}


void escribirEnBitacora(QString& qpath, string& mensaje) {
    // Convert QString to   string
      string path = qpath.toStdString(); // Use qpath, not path

      ofstream archivo(path,   ios::app); // Open the file in append mode
    if (archivo.is_open()) {
        archivo << mensaje <<   endl ; // Write the message to the file
        archivo.close(); // Close the file
    } else {
          cerr << "Error al abrir el archivo: " << path <<   endl; // Print error message
    }
}

void borrarUltimaLinea(QString& qpath) {
    QFile archivo(qpath);
    if (!archivo.open(QIODevice::ReadWrite | QIODevice::Text)) {
          cerr << "Error al abrir el archivo: " << qpath.toStdString() <<   endl;
        return;
    }

    // Leer todo el archivo y guardar las posiciones de las líneas
    QTextStream in(&archivo);
    QString contenido;
    qint64 ultimaPosicion = 0;  // Para guardar la posición de la última línea
    qint64 posicionActual = 0;

    while (!in.atEnd()) {
        posicionActual = archivo.pos();
        contenido = in.readLine();  // Leer línea por línea
        if (!contenido.isEmpty()) {
            ultimaPosicion = posicionActual;  // Guardar la posición de la última línea válida
        }
    }

    // Truncar el archivo justo antes de la última línea
    archivo.resize(ultimaPosicion);
    archivo.close();
}

  string buscarLinea(int linea, QString& qpath) {

      string path = qpath.toStdString();

    ifstream archivo(path); // Abrir archivo
    if (!archivo.is_open()) {
          cerr << "No se pudo abrir el archivo: " << path <<   endl;
        return "";
    }

    string resultado;
    int indiceActual = 0;
    while(  getline(archivo, resultado)){
        if(indiceActual==linea) {archivo.close(); return resultado;}
        indiceActual++;
    }

    cerr << "Linea no existe en el archivo " << path << endl;
}

  QString leerYBorrarUltimaLinea( QString& qpath) {
      QFile archivo(qpath);
      if (!archivo.open(QIODevice::ReadWrite | QIODevice::Text)) {
          std::cerr << "Error al abrir el archivo: " << qpath.toStdString() << std::endl;
          return "";
      }

      QTextStream in(&archivo);
      QStringList lineas;

      // Leer todas las líneas del archivo
      while (!in.atEnd()) {
          QString linea = in.readLine();
          lineas << linea;
      }

      if (lineas.isEmpty()) {
          archivo.close();
          return "";
      }

      // Obtener la última línea
      QString ultimaLinea = lineas.takeLast();

      // Truncar el archivo y reescribir todas las líneas excepto la última
      archivo.resize(0); // Truncar el archivo a 0 bytes
      QTextStream out(&archivo);
      for (const QString& linea : lineas) {
          out << linea << "\n";
      }

      archivo.close();

      return ultimaLinea;
  }
