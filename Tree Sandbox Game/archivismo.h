#ifndef ARCHIVISMO_H
#define ARCHIVISMO_H

#include "common.h"

// Declaración de las variables de ruta para bitácoras
extern QString nombres;
extern QString apellidos;       // Variable para apellidos
extern QString religiones;      // Variable para religiones
extern QString paises;          // Variable para países
extern QString profesiones;      // Variable para profesiones
extern QString muerte;
extern QString bitacoraMuerte;
extern QString buscarPorIdTxt;
extern QString humanidad;
extern QString ultimoNivel;
extern QString arbolHumanidad;
extern QString verHeap;
extern QString verReencarnaciones;
extern QString verArbolAngeles;
extern QString verRanking;
extern QString bitacoraAngel;
//Todo bitacorismo

//
string buscarLinea(int, QString&);
// Función para generar el camino de las bitácoras basado en la ubicación del archivo fuente
void introducirPaths(const QString& carpetaPaths);

void generarCaminoBitacora();

// Función para introducir los paths de las bitácoras a partir de una carpeta

void resetearArchivoEspecifico(QString qpath);

// Función para resetear todos los archivos de bitácoras (vaciar su contenido)
void resetearArchivos();

// Función para escribir un mensaje en una bitácora
void escribirEnBitacora( QString& qpath, string& mensaje);

void borrarUltimaLinea( QString& qpath);

QString leerYBorrarUltimaLinea ( QString& qpath);

#endif // ARCHIVISMO_H
