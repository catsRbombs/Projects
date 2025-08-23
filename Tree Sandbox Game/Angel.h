#ifndef ANGEL_H
#define ANGEL_H

#include "humanocompleto.h"
#include "utilidad.h"


class Angel
{
public:
      string nombre;
    int version;
    humanoCompleto* humanoSalvado;
    int nivelArbol;

    // Constructores
    Angel();
    Angel(const   string& nombre, int version, int nivelArbol);

    // Métodos
    void asignarHumano(humanoCompleto* humano);
};

struct NodoAngel
{
    Angel* angel; // Parte de datos
    NodoAngel* hijoIzquierdo; // Puntero al hijo izquierdo
    NodoAngel* hijoDerecho;   // Puntero al hijo derecho
    NodoAngel* hijoMedio;

    NodoAngel(const   string& nombre, int version, int nivelArbol);
    NodoAngel();

    // Métodos
    string generarNombreAngel();
    void establecerNombreAleatorio();
};

#endif // ANGELISMO_H
