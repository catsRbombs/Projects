#ifndef ARBOLDEHUMANOS_H
#define ARBOLDEHUMANOS_H

#include "structs.h"
#include "common.h"

class ArbolDeHumanos
{
public:
    NodoHumanoArbol* raiz;

    // Constructor y destructor
    ArbolDeHumanos();
    ~ArbolDeHumanos();

    // Funciones para construir el árbol
    void construirArbol(puenteParaDirecciones** humanos, int totalHumanos);
    NodoHumanoArbol* construirArbolCompleto(puenteParaDirecciones** humanos, int inicio, int fin);

    // Funciones auxiliares
    void eliminarRecursivo(NodoHumanoArbol* nodo);
    int obtenerNumeroElementos(NodoHumanoArbol* nodo);
    int obtenerAltura(NodoHumanoArbol* nodo);

    // Funciones para mostrar información
    void mostrarInformacion(int totalHumanos);

    // Funciones para búsqueda
    humanoCompleto* buscarHumano(int id);
    puenteParaDirecciones* buscarHumanoCercanoRecursivo(NodoHumanoArbol* nodo, int id, int rango);
    puenteParaDirecciones* buscarHumanoCercano(int id);
    void encontrarNodosUltimoNivel(NodoHumanoArbol* nodo);
    void imprimirArbolHumanidad(NodoHumanoArbol* nodo, int nivel, const std::string& tipo);
};

#endif // ARBOLDEHUMANOS_H
