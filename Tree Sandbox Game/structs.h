#ifndef STRUCTS_H
#define STRUCTS_H

#include "humanocompleto.h"



struct NodoHumanoLista {
    humanoCompleto* humano; // parte de datos
    NodoHumanoLista* siguiente;// puntero para enlazar NodoListas
    NodoHumanoLista* anterior;
    // constructor

    NodoHumanoLista(humanoCompleto* pntrHumano) {humano = pntrHumano; siguiente = anterior = NULL;}

    NodoHumanoLista(const int& id, const string& nombre, const string& apellido, const string& pais,
                    const string& creencia, const string& profesion, const string& fechaHoraNacimiento,
                    const array<int, 7>& listaDePecados, const bool& status, const list<humanoCompleto>& reencarnaciones, const int& contadorReencarnaciones)
    {
        humano = new humanoCompleto(id, nombre, apellido, pais, creencia, profesion, fechaHoraNacimiento, listaDePecados, status, reencarnaciones, contadorReencarnaciones);
        siguiente = anterior = NULL;
    }

    NodoHumanoLista() {anterior = siguiente = NULL; humano = new humanoCompleto();}


    // Elimina la copia y asignación para evitar duplicaciones no intencionadas
    NodoHumanoLista(const NodoHumanoLista&) = delete;
    NodoHumanoLista& operator=(const NodoHumanoLista&) = delete;

};

struct puenteParaDirecciones {
    // Voy a llorar
    humanoCompleto* humano;
    NodoHumanoLista* direccion;
};

struct NodoHumanoArbol {
    puenteParaDirecciones* humano; // Parte de datos
    NodoHumanoArbol* hijoIzquierdo; // Puntero al hijo izquierdo
    NodoHumanoArbol* hijoDerecho;   // Puntero al hijo derecho

    // Constructor completo
    NodoHumanoArbol(const int& id, const string& nombre, const string& apellido, const string& pais,
                    const string& creencia, const string& profesion, const string& fechaHoraNacimiento,
                    const array<int, 7>& listaDePecados, const bool& status, const list<humanoCompleto>& reencarnaciones,
                    const int& contadorReencarnaciones)
    {
        humano->humano = new humanoCompleto(id, nombre, apellido, pais, creencia, profesion,
                                    fechaHoraNacimiento, listaDePecados, status,
                                    reencarnaciones, contadorReencarnaciones);
        hijoIzquierdo = hijoDerecho = nullptr;
    }

    // Constructor por defecto
    NodoHumanoArbol() {
        humano = new  puenteParaDirecciones(); // Crear un humano vacío
        hijoIzquierdo = hijoDerecho = nullptr;
    }

    NodoHumanoArbol(humanoCompleto* humanoPtr)
        : hijoIzquierdo(nullptr), hijoDerecho(nullptr) {puenteParaDirecciones hpntr; hpntr.humano = humanoPtr; humano = &hpntr;}

    NodoHumanoArbol(puenteParaDirecciones* hmpntr)
        : hijoIzquierdo(nullptr), hijoDerecho(nullptr), humano(hmpntr) {}

    // Destructor para liberar memoria del humano
    ~NodoHumanoArbol() {
        delete humano;
    }

    // Evitar la copia y asignación no intencionadas
    NodoHumanoArbol(const NodoHumanoArbol&) = delete;
    NodoHumanoArbol& operator=(const NodoHumanoArbol&) = delete;
};


struct ListaDobleHumano {

    NodoHumanoLista* primerNodo;
    NodoHumanoLista* ultimoNodo;
    unordered_set<int> idsExistentes;
    ListaDobleHumano() {primerNodo = ultimoNodo = NULL;}

    NodoHumanoLista insertarAlInicio (humanoCompleto humanpntr);
    NodoHumanoLista* buscarPorId(int id);
    NodoHumanoLista* buscarPorIdCheckpoint(NodoHumanoLista* humanpntr, int id);
    NodoHumanoLista* insertarAlFinal(humanoCompleto* humanpntr);
    void generarHumanosEnLista(int cantidad);
    int contarVivos();
    int contarMuertos();
    int largo (void);
    bool vacia(void);
    int generarIdUnico();
    void pecarAll();
    void encontrarAmigos(humanoCompleto& humanoActual);
    void escribirReencarnaciones(int numeroReencarnaciones);
    void escribirRankingPaises();

};

#endif // STRUCTS_H
