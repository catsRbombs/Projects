// Muerte.h
#ifndef MUERTE_H
#define MUERTE_H

#include "structs.h"
#include "common.h"



// Declaraciones adelantadas
class ListaDobleHumano;
class humanoCompleto;

// Estructura para representar un nodo en el heap
struct HeapNode {
    humanoCompleto* humano;  // Puntero al humano asociado
    int key;                 // Clave del heap (suma de pecados o valor específico)
    HeapNode* left;          // Hijo izquierdo
    HeapNode* right;         // Hijo derecho
    HeapNode* parent;        // Puntero al padre

    // Constructor
    HeapNode(humanoCompleto* h, int k)
        : humano(h), key(k), left(nullptr), right(nullptr), parent(nullptr) {}
};

// Clase Muerte
class Muerte {
public:
    Muerte(ListaDobleHumano& listaInicial);
    ~Muerte();

    // Miembros públicos
    ListaDobleHumano& lista;

    // Métodos públicos
    HeapNode* heapMuerteSum();
    HeapNode* heapMuerteSpecific(int pecadoIndice);
    void matarPorNivel(int nivel);
    void pandemiaAleatoria(int probabilidad);
    void muerteEspecifica(int id);
    void imprimirHeapMuerte(HeapNode* nodo, int nivel, std::string tipo);
    bool muererng(int porcentaje);

    QString crearMensajeBitacora(int tipoAlgoritmo, humanoCompleto* humano);
    int contarNodosHeap(HeapNode* nodo);
    std::string obtenerRuta(int posicion);
    void insertarNodo(HeapNode*& raiz, HeapNode* nuevoNodo, const std::string& ruta);
    void heapifyUp(HeapNode* nodo);

    // Miembros privados
    HeapNode* Raiz; // Puntero al heap actual
    int contadorPandemias;
    int contadorMuertes;
    std::default_random_engine generador;

    // Métodos privados
    void eliminarHeap(HeapNode* nodo);
};

#endif // MUERTE_H
