#include "structs.h"
#include "CrearHumano.h"
#include "archivismo.h"

// Función que inserta un nodo nuevo al inicio de la lista
// Entradas: el dato a guardar
// Salidas: Puntero al nodo insertado

// Función que inserta un nodo al final de la lista
NodoHumanoLista* ListaDobleHumano::insertarAlFinal(humanoCompleto* humanpntr) {
    NodoHumanoLista* nuevo = new NodoHumanoLista(humanpntr);

    nuevo->anterior = ultimoNodo;
    if (ultimoNodo != nullptr) {
        ultimoNodo->siguiente = nuevo;
    }
    ultimoNodo = nuevo;
    if (primerNodo == nullptr) {
        primerNodo = nuevo;
    }

    return nuevo;
}

// Función que busca un nodo con un dato específico
NodoHumanoLista* ListaDobleHumano::buscarPorId(int dato) {
    NodoHumanoLista* tmp = primerNodo;
    while (tmp != NULL) {
        if(tmp->humano->dataBasica.id == dato){
            cout << "Humano encontrado exitosamente " << endl;
            return tmp;
        }
        //cout << tmp->humano->dataBasica.id << tmp->humano->dataBasica.nombre << endl;
        tmp = tmp->siguiente;
    }
    cout << "Fallo la busqueda de humano " << endl;
    return nullptr;
}

NodoHumanoLista* ListaDobleHumano::buscarPorIdCheckpoint(NodoHumanoLista* humanpntr, int dato) {
    NodoHumanoLista* tmp = humanpntr;
    while (tmp != NULL) {
        if(tmp->humano->dataBasica.id==dato) return tmp;
        tmp = tmp->siguiente;
    }
    return nullptr;
}

// Función que calcula el largo de la lista
int ListaDobleHumano::largo() {
    NodoHumanoLista* tmp = primerNodo;
    int largo = 0;
    while (tmp != NULL) {
        largo++;
        tmp = tmp->siguiente;
    }
    return largo;
}

// Función que verifica si la lista está vacía
bool ListaDobleHumano::vacia() {
    return primerNodo == NULL;
}

int ListaDobleHumano::contarVivos()
{
    int contador = 0;
    NodoHumanoLista* actual = primerNodo;
    while (actual != nullptr) {
        if (actual->humano != nullptr && actual->humano->dataBasica.status) {
            contador++;
        }
        actual = actual->siguiente;
    }
    return contador;
}

int ListaDobleHumano::contarMuertos()
{
    int contador = 0;
    NodoHumanoLista* actual = primerNodo;
    while (actual != nullptr) {
        if (actual->humano != nullptr && !actual->humano->dataBasica.status) {
            contador++;
        }
        actual = actual->siguiente;
    }
    return contador;
}

void ListaDobleHumano::pecarAll(){

    NodoHumanoLista* actual = primerNodo;
    while (actual != nullptr) {
        actual->humano->generarPecado();
        actual = actual->siguiente;
    }
}

// Cambia la firma para pasar `humanoActual` por referencia
void ListaDobleHumano::encontrarAmigos(humanoCompleto& humanoActual) {
    NodoHumanoLista* tmp = primerNodo;

    // Bucle para encontrar amigos basándose en los criterios dados
    while(humanoActual.amigosCounter < 10 && tmp != nullptr) {
        // Evitar añadir el propio humano como amigo
        if (humanoActual.dataBasica.id != tmp->humano->dataBasica.id) {
            // Verificar criterios de amistad
            if (humanoActual.dataBasica.pais == tmp->humano->dataBasica.pais ||
                humanoActual.dataBasica.apellido == tmp->humano->dataBasica.apellido ||
                humanoActual.dataBasica.creencia == tmp->humano->dataBasica.creencia) {

                // Añadir amigo y actualizar el contador de amigos
                humanoActual.listaAmigos[humanoActual.amigosCounter] = tmp->humano->dataBasica;
                humanoActual.amigosCounter++;
            }
        }
        tmp = tmp->siguiente;
    }
}

void ListaDobleHumano::escribirReencarnaciones(int numeroReencarnaciones){
    resetearArchivoEspecifico(verReencarnaciones);
    NodoHumanoLista* actual = primerNodo;

    // Recorrer toda la lista
    while (actual != nullptr) {
        if (actual->humano != nullptr) {
                // Comparar el número de reencarnaciones
                if (actual->humano->contadorReencarnaciones == numeroReencarnaciones) {
                    // Crear el mensaje con la información del humano
                    string mensaje= (actual->humano->escribirDetallesHumanidad()).toStdString();

                    // Escribir el mensaje en el archivo 'verReencarnaciones.txt'
                    escribirEnBitacora(verReencarnaciones,mensaje);

                }

        }
        actual = actual->siguiente;
    }

}

void ListaDobleHumano::escribirRankingPaises(){


    // Reiniciar (limpiar) el archivo antes de escribir
    resetearArchivoEspecifico(verRanking);

    // Estructura para acumular la suma de pecados por país
    std::unordered_map<std::string, int> sumaPecadosPorPais;

    // Puntero al nodo actual para recorrer la lista
    NodoHumanoLista* actual = primerNodo;

    // Recorrer toda la lista de humanos
    while (actual != nullptr) {
        if (actual->humano != nullptr) {
            // Obtener el país del humano
            std::string pais = actual->humano->dataBasica.pais; // Asegúrate de que 'pais' es un miembro de dataBasica

            // Calcular la suma de pecados del humano
            int sumaPecados = 0;
            for(auto pecado : actual->humano->dataBasica.listaDePecados){
                sumaPecados += pecado;
            }

            // Acumular la suma de pecados por país
            sumaPecadosPorPais[pais] += sumaPecados;
        }
        actual = actual->siguiente;
    }

    // Convertir el unordered_map a un vector de pares para poder ordenar
    std::vector<std::pair<std::string, int>> ranking;
    for(const auto& par : sumaPecadosPorPais){
        ranking.emplace_back(par);
    }

    // Ordenar el vector en orden descendente basado en la suma de pecados
    std::sort(ranking.begin(), ranking.end(),
              [](const std::pair<std::string, int>& a, const std::pair<std::string, int>& b) -> bool {
                  return a.second > b.second;
              }
              );

    // Crear el mensaje de ranking
    std::string mensaje = "Ranking de Países por Suma de Pecados:\n\n";

    for(size_t i = 0; i < ranking.size(); ++i){
        mensaje += std::to_string(i + 1) + ". " + ranking[i].first + " - " + std::to_string(ranking[i].second) + " pecados\n";
    }

    // Escribir el mensaje en el archivo de bitácora
    escribirEnBitacora(verRanking, mensaje);

    // Opcional: Mostrar el ranking en la consola
    std::cout << mensaje;
}

