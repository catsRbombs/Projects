#include "ArbolDeHumanos.h"
#include <iostream>
#include "archivismo.h"

using namespace std;

// Constructor
ArbolDeHumanos::ArbolDeHumanos()
{
    raiz = nullptr;
}

// Destructor
ArbolDeHumanos::~ArbolDeHumanos()
{
    eliminarRecursivo(raiz);
}

// Función para eliminar el árbol recursivamente
void ArbolDeHumanos::eliminarRecursivo(NodoHumanoArbol* nodo)
{
    if (nodo == nullptr)
        return;

    eliminarRecursivo(nodo->hijoIzquierdo);
    eliminarRecursivo(nodo->hijoDerecho);
    delete nodo;
}

// Función para construir el árbol
void ArbolDeHumanos::construirArbol(puenteParaDirecciones** humanos, int totalHumanos)
{
    // Calcular el 1% de los humanos
    int numNodos = (totalHumanos * 1) / 100;
    if (numNodos < 1)
        numNodos = 1;

    // Ajustar numNodos para que el árbol sea completo
    int niveles = 0;
    int temp = numNodos + 1;
    while (temp > 1)
    {
        temp = temp / 2;
        niveles++;
    }
    int nodosNecesarios = (1 << niveles) - 1; // 2^niveles - 1
    if (nodosNecesarios > totalHumanos)
        numNodos = totalHumanos;
    else
        numNodos = nodosNecesarios;

    // Copiar los humanos a un nuevo array para ordenarlos
    puenteParaDirecciones** humanosOrdenados = new puenteParaDirecciones*[totalHumanos];
    for (int i = 0; i < totalHumanos; i++)
    {
        humanosOrdenados[i] = humanos[i];
    }

    // Ordenar los humanos por ID usando Bubble Sort
    for (int i = 0; i < totalHumanos - 1; i++)
    {
        for (int j = 0; j < totalHumanos - i - 1; j++)
        {
            if (humanosOrdenados[j]->humano->dataBasica.id > humanosOrdenados[j + 1]->humano->dataBasica.id)
            {
                puenteParaDirecciones* tempHumano = humanosOrdenados[j];
                humanosOrdenados[j] = humanosOrdenados[j + 1];
                humanosOrdenados[j + 1] = tempHumano;
            }
        }
    }

    // Seleccionar humanos para el árbol
    puenteParaDirecciones** humanosSeleccionados = new puenteParaDirecciones*[numNodos];
    int intervalo = totalHumanos / numNodos;
    for (int i = 0; i < numNodos; ++i)
    {
        int index = i * intervalo;
        if (index >= totalHumanos)
            index = totalHumanos - 1;
        humanosSeleccionados[i] = humanosOrdenados[index];
    }

    // Construir el árbol completo
    raiz = construirArbolCompleto(humanosSeleccionados, 0, numNodos - 1);

    // Liberar memoria
    delete[] humanosOrdenados;
    delete[] humanosSeleccionados;
}

// Función recursiva para construir un árbol completo a partir de una lista de humanos
NodoHumanoArbol* ArbolDeHumanos::construirArbolCompleto(puenteParaDirecciones** humanos, int inicio, int fin)
{
    if (inicio > fin)
        return nullptr;

    int medio = (inicio + fin) / 2;
    puenteParaDirecciones* humanoMedio = humanos[medio];

    // Crear un nuevo nodo apuntando al humano correspondiente
    NodoHumanoArbol* nodo = new NodoHumanoArbol(humanoMedio);

    // Construir recursivamente los hijos
    nodo->hijoIzquierdo = construirArbolCompleto(humanos, inicio, medio - 1);
    nodo->hijoDerecho = construirArbolCompleto(humanos, medio + 1, fin);

    return nodo;
}

// Función para obtener el número de elementos en el árbol
int ArbolDeHumanos::obtenerNumeroElementos(NodoHumanoArbol* nodo)
{
    if (nodo == nullptr)
        return 0;
    return 1 + obtenerNumeroElementos(nodo->hijoIzquierdo) + obtenerNumeroElementos(nodo->hijoDerecho);
}

// Función para obtener la altura del árbol
int ArbolDeHumanos::obtenerAltura(NodoHumanoArbol* nodo)
{
    if (nodo == nullptr)
        return 0;
    int alturaIzquierda = obtenerAltura(nodo->hijoIzquierdo);
    int alturaDerecha = obtenerAltura(nodo->hijoDerecho);
    return (alturaIzquierda > alturaDerecha ? alturaIzquierda : alturaDerecha) + 1;
}

// Función para mostrar la información requerida
void ArbolDeHumanos::mostrarInformacion(int totalHumanos)
{
    int niveles = obtenerAltura(raiz);
    int numNodos = obtenerNumeroElementos(raiz);

    cout << "Cantidad de niveles del arbol: " << niveles << endl;
    cout << "Cantidad de nodos del arbol: " << numNodos << endl;
    cout << "Cantidad total de humanos: " << totalHumanos << endl;
}



puenteParaDirecciones* ArbolDeHumanos::buscarHumanoCercano(int id)
{
    return buscarHumanoCercanoRecursivo(raiz, id, 100); // Rango inicial de 100 (ajustable según la distribución)
}

puenteParaDirecciones* ArbolDeHumanos::buscarHumanoCercanoRecursivo(NodoHumanoArbol* nodo, int id, int rango)
{
    if (nodo == nullptr)
        return nullptr;

    int diferencia = abs(id - nodo->humano->humano->dataBasica.id);

    // Si el id del nodo está dentro del rango, retornamos este humano
    if (diferencia <= rango)
        return nodo->humano;

    // Ajustamos el rango a medida que avanzamos en el árbol
    int nuevoRango = (rango > 100) ? rango / 2 : 100;  // Reducimos el rango en cada nivel hasta un mínimo

    // Continuamos la búsqueda según el valor de id
    if (id < nodo->humano->humano->dataBasica.id)
        return buscarHumanoCercanoRecursivo(nodo->hijoIzquierdo, id, nuevoRango);
    else
        return buscarHumanoCercanoRecursivo(nodo->hijoDerecho, id, nuevoRango);
}

void ArbolDeHumanos::imprimirArbolHumanidad(NodoHumanoArbol* nodo, int nivel, const std::string& tipo){

    if (nodo == nullptr)
        return;
    string mensaje;


    // Imprimir la indentación según el nivel
    mensaje += std::string(nivel * 5, ' ');
    mensaje += std::to_string(nivel) + " "  + tipo + ": [ID: " + std::to_string(nodo->humano->humano->dataBasica.id);
    mensaje += ", Nombre: " + nodo->humano->humano->dataBasica.nombre;
    mensaje += " " + nodo->humano->humano->dataBasica.apellido + "]\n";
    escribirEnBitacora(arbolHumanidad, mensaje);

    imprimirArbolHumanidad(nodo->hijoIzquierdo, nivel + 1, "Hijo Izquierdo");
    imprimirArbolHumanidad(nodo->hijoDerecho, nivel + 1, "Hijo Derecho");
}

void ArbolDeHumanos::encontrarNodosUltimoNivel(NodoHumanoArbol* nodo)
{
    if (nodo == nullptr)
        return;

    // Si el nodo es una hoja (no tiene hijos), es un nodo del último nivel
    if (nodo->hijoIzquierdo == nullptr && nodo->hijoDerecho == nullptr)
    {
        string text = (nodo->humano->humano->escribirDetallesHumanidad()).toStdString();
        escribirEnBitacora(ultimoNivel,text);
    }
    else
    {
        // Continuamos recorriendo los subárboles izquierdo y derecho
        encontrarNodosUltimoNivel(nodo->hijoIzquierdo);
        encontrarNodosUltimoNivel(nodo->hijoDerecho);
    }
}
