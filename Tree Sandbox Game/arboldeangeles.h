// ArbolDeAngeles.h
#ifndef ARBOLDEANGELES_H
#define ARBOLDEANGELES_H

#include "Angel.h"


class ArbolDeAngeles
{
public:
    NodoAngel* raiz;
    int nivelesActuales;
      string listaNombres[10];
      unordered_map<int,   unordered_map<  string, int>> conteoNombresPorNivel;

      queue<NodoAngel*> nivelMasBajo;
    // Constructor
    ArbolDeAngeles();

    // Destructor
    ~ArbolDeAngeles();

    // Métodos
    void inicializarArbol();
    void agregarNivel();
    void asignarHumanoAAngel(humanoCompleto* humano);
    void imprimirArbol();
    queue<NodoAngel*> queueNivel();
    void imprimirArbolAngeles(NodoAngel* nodo, int nivel, const std::string& tipo);

private:
    void eliminarRecursivo(NodoAngel* nodo);
    NodoAngel* crearNodoAngel(int nivelArbol);
};


#endif // ARBOLDEANGELES_H
