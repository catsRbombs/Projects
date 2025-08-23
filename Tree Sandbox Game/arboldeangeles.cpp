// ArbolDeAngeles.cpp
#include "ArbolDeAngeles.h"
#include "utilidad.h"
#include "common.h"
#include "archivismo.h"

// Constructor
ArbolDeAngeles::ArbolDeAngeles()
{
    // Inicializar la lista de nombres
      string nombres[10] = {
        "Miguel", "Nuriel", "Aniel", "Rafael",
        "Gabriel", "Shamsiel", "Raguel",
        "Uriel", "Azrael", "Sariel"
    };

    for (int i = 0; i < 10; ++i)
    {
        listaNombres[i] = nombres[i];
    }

    raiz = nullptr;
    nivelesActuales = 0;
    inicializarArbol();
}

// Destructor
ArbolDeAngeles::~ArbolDeAngeles()
{
    eliminarRecursivo(raiz);
}

// Función para eliminar el árbol recursivamente
void ArbolDeAngeles::eliminarRecursivo(NodoAngel* nodo)
{
    if (nodo == nullptr)
        return;

    eliminarRecursivo(nodo->hijoIzquierdo);
    eliminarRecursivo(nodo->hijoMedio);
    eliminarRecursivo(nodo->hijoDerecho);

    delete nodo->angel;
    delete nodo;
}

// Función para crear un nuevo nodo ángel
NodoAngel* ArbolDeAngeles::crearNodoAngel(int nivelArbol)
{
    // Generar un nombre aleatorio
    int indice = generarNumeroAleatorio(10);
      string nombre = listaNombres[indice];

    // Incrementar el conteo de versiones para este nombre en el nivel dado
    int version = ++conteoNombresPorNivel[nivelArbol][nombre];

    // Crear y retornar el nuevo nodo
    return new NodoAngel(nombre, version, nivelArbol);
}

// Función para inicializar el árbol con niveles 0, 1 y 2
void ArbolDeAngeles::inicializarArbol()
{
    // Crear el nivel 0
    nivelesActuales = 0;
    raiz = crearNodoAngel(nivelesActuales);

    // Crear niveles 1 y 2
    queue<NodoAngel*> cola;
    cola.push(raiz);

    while (nivelesActuales < 2)
    {
        int nodosEnNivel = cola.size();
        for (int i = 0; i < nodosEnNivel; ++i)
        {
            NodoAngel* nodoPadre = cola.front();
            cola.pop();

            // Crear tres hijos para cada nodo
            nodoPadre->hijoIzquierdo = crearNodoAngel(nivelesActuales + 1);
            nodoPadre->hijoMedio = crearNodoAngel(nivelesActuales + 1);
            nodoPadre->hijoDerecho = crearNodoAngel(nivelesActuales + 1);

            // Encolar los nuevos nodos
            cola.push(nodoPadre->hijoIzquierdo);
            cola.push(nodoPadre->hijoMedio);
            cola.push(nodoPadre->hijoDerecho);
        }
        nivelesActuales++;
    }
}

// Función para agregar un nuevo nivel al árbol
void ArbolDeAngeles::agregarNivel()
{
    while(!nivelMasBajo.empty()){
        nivelMasBajo.pop();
    }

    nivelesActuales++;
    int nivelNuevo = nivelesActuales;

    // Obtener todas las hojas actuales
    queue<NodoAngel*> cola;
    cola.push(raiz);

    vector<NodoAngel*> hojas;

    while (!cola.empty())
    {
        NodoAngel* nodo = cola.front();
        cola.pop();

        if (nodo->hijoIzquierdo == nullptr && nodo->hijoMedio == nullptr && nodo->hijoDerecho == nullptr)
        {
            hojas.push_back(nodo);
        }
        else
        {
            if (nodo->hijoIzquierdo)
                cola.push(nodo->hijoIzquierdo);
            if (nodo->hijoMedio)
                cola.push(nodo->hijoMedio);
            if (nodo->hijoDerecho)
                cola.push(nodo->hijoDerecho);
        }
    }

    // Agregar tres hijos a cada hoja
    for (auto hoja : hojas)
    {
        NodoAngel* nuevo = crearNodoAngel(nivelNuevo);
        hoja->hijoIzquierdo = nuevo;
        nivelMasBajo.push(nuevo);
        nuevo = crearNodoAngel(nivelNuevo);
        hoja->hijoMedio = nuevo;
        nivelMasBajo.push(nuevo);
        nuevo = crearNodoAngel(nivelNuevo);
        hoja->hijoDerecho = nuevo;
        nivelMasBajo.push(nuevo);
    }
}

// Función para asignar un humano a un ángel de la última generación
void ArbolDeAngeles::asignarHumanoAAngel(humanoCompleto* humano)
{
    // Buscar un ángel en la última generación que no haya salvado a nadie
      queue<NodoAngel*> cola;
    cola.push(raiz);

    while (!cola.empty())
    {
        NodoAngel* nodo = cola.front();
        cola.pop();

        if (nodo->angel->nivelArbol == nivelesActuales)
        {
            if (nodo->angel->humanoSalvado == nullptr)
            {
                nodo->angel->asignarHumano(humano);
                  cout << "Angel " << nodo->angel->nombre << " (" << nodo->angel->version << ") G" << nodo->angel->nivelArbol
                          << " ha salvado al humano con ID " << humano->dataBasica.id << "\n";
                return;
            }
        }
        else
        {
            if (nodo->hijoIzquierdo)
                cola.push(nodo->hijoIzquierdo);
            if (nodo->hijoMedio)
                cola.push(nodo->hijoMedio);
            if (nodo->hijoDerecho)
                cola.push(nodo->hijoDerecho);
        }
    }

      cout << "No hay ángeles disponibles en la última generación para salvar al humano.\n";
}

// Función para imprimir el árbol por niveles
void ArbolDeAngeles::imprimirArbol()
{
      queue<NodoAngel*> cola;
    cola.push(raiz);
    int nivelActual = 0;

    while (!cola.empty())
    {
        int nodosEnNivel = cola.size();
          cout << "Nivel " << nivelActual << ":\n";

        for (int i = 0; i < nodosEnNivel; ++i)
        {
            NodoAngel* nodo = cola.front();
            cola.pop();

              cout << nodo->angel->nombre << " (" << nodo->angel->version << ") G" << nodo->angel->nivelArbol << " | ";

            if (nodo->hijoIzquierdo)
                cola.push(nodo->hijoIzquierdo);
            if (nodo->hijoMedio)
                cola.push(nodo->hijoMedio);
            if (nodo->hijoDerecho)
                cola.push(nodo->hijoDerecho);
        }

          cout << "\n";
        nivelActual++;
    }
}

std::queue<NodoAngel*> ArbolDeAngeles::queueNivel() {
    std::queue<NodoAngel*> cola, resultado;
    if (!raiz) return resultado;  // Retorna vacío si el árbol está vacío

    cola.push(raiz);

    while (!cola.empty()) {
        int tamanoNivel = cola.size();
        bool esUltimoNivel = true;

        // Revisar si todos los nodos en este nivel son hojas
        for (int i = 0; i < tamanoNivel; ++i) {
            NodoAngel* nodoActual = cola.front();
        int tamanoNivel = cola.size();
        bool esUltimoNivel = true;

        // Revisar si todos los nodos en este nivel son hojas
        for (int i = 0; i < tamanoNivel; ++i) {
            NodoAngel* nodoActual = cola.front();
            cola.pop();

            // Agregar hijos al siguiente nivel
            if (nodoActual->hijoIzquierdo) {
                cola.push(nodoActual->hijoIzquierdo);
                esUltimoNivel = false;  // Si tiene hijo, no es el último nivel
            }
            if (nodoActual->hijoMedio) {
                cola.push(nodoActual->hijoMedio);
                esUltimoNivel = false;
            }
            if (nodoActual->hijoDerecho) {
                cola.push(nodoActual->hijoDerecho);
                esUltimoNivel = false;
            }

            // Si es el último nivel, agrega el nodo al resultado
            if (esUltimoNivel) {
                resultado.push(nodoActual);
            }
        }

        // Si encontramos el último nivel, devolvemos los nodos
        if (esUltimoNivel) {
            return resultado;
        }
    }

    return resultado;
    }


}

void ArbolDeAngeles::imprimirArbolAngeles(NodoAngel* nodo, int nivel, const std::string& tipo) {
    if (nodo == nullptr)
        return;

    std::string mensaje;

    // Crear la indentación basada en el nivel
    mensaje += std::string(nivel * 5, ' ');
    mensaje += std::to_string(nivel) + " " + tipo;

    int id = 0;
    if (nodo->angel->humanoSalvado != nullptr){
        id = nodo->angel->humanoSalvado->dataBasica.id;
    }

    // Añadir información adicional si el nivel es mayor a 1
    if (nivel > 1) {
        mensaje += ": [Nombre: " + nodo->angel->nombre;
        mensaje += ", Versión: " + std::to_string(nodo->angel->version) + ", Id del humano Reencarnado: " + std::to_string(id)+ "]\n";
    } else {
        mensaje += "\n";
    }

    // Escribir el mensaje en el archivo de bitácora **antes** de recorrer los hijos
    escribirEnBitacora(verArbolAngeles, mensaje);

    // Recorrer recursivamente los hijos
    if (nivel == 0) {
        imprimirArbolAngeles(nodo->hijoIzquierdo, nivel + 1, "Sefafines");
        imprimirArbolAngeles(nodo->hijoMedio, nivel + 1, "Querubines");
        imprimirArbolAngeles(nodo->hijoDerecho, nivel + 1, "Trono");
    } else {
        imprimirArbolAngeles(nodo->hijoIzquierdo, nivel + 1, "Hijo Izquierdo");
        imprimirArbolAngeles(nodo->hijoMedio, nivel + 1, "Hijo Medio");
        imprimirArbolAngeles(nodo->hijoDerecho, nivel + 1, "Hijo Derecho");
    }
}
