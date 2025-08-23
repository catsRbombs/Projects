// Angel.cpp
#include "Angel.h"
#include "common.h"

// Implementación de la clase Angel

// Constructor por defecto
Angel::Angel()
    : nombre(""), version(0), humanoSalvado(nullptr), nivelArbol(0)
{
}

// Constructor con parámetros
Angel::Angel(const   string& nombre_, int version_, int nivelArbol_)
    : nombre(nombre_), version(version_), humanoSalvado(nullptr), nivelArbol(nivelArbol_)
{
}

// Método para asignar un humano al ángel
void Angel::asignarHumano(humanoCompleto* humano)
{
    humanoSalvado = humano;
    //humano->angelAsignado = this; // Establecer la relación inversa
}

// Implementación de la estructura NodoAngel

// Constructor completo
NodoAngel::NodoAngel(const   string& nombre, int version, int nivelArbol)
{
    angel = new Angel(nombre, version, nivelArbol);
    hijoIzquierdo = hijoDerecho = hijoMedio = nullptr;
}

// Constructor por defecto
NodoAngel::NodoAngel()
{
    angel = new Angel(); // Crear un ángel vacío
    hijoIzquierdo = hijoDerecho = hijoMedio = nullptr;
}

// Método para generar un nombre aleatorio para el ángel
  string NodoAngel::generarNombreAngel()
{
      string nombres[10] = {
        "Miguel", "Nuriel", "Aniel", "Rafael",
        "Gabriel", "Shamsiel", "Raguel",
        "Uriel", "Azrael", "Sariel"
    };

    // Genera un número aleatorio entre 0 y 9
    int indice = generarNumeroAleatorio(10);

    // Retorna el nombre correspondiente
    return nombres[indice];
}

// Método para establecer un nombre aleatorio al ángel
void NodoAngel::establecerNombreAleatorio()
{
    angel->nombre = generarNombreAngel();
}
