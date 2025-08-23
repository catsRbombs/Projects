#include "Muerte.h"
#include "archivismo.h"
#include "EmailSender.h"
#include "utilidad.h"

// Constructor
Muerte::Muerte(ListaDobleHumano& listaInicial)
    : lista(listaInicial), contadorPandemias(0), contadorMuertes(0), generador(std::random_device{}()), Raiz(nullptr)
{
}

// Destructor para limpiar el heap
Muerte::~Muerte()
{
    eliminarHeap(Raiz);
}

// Función recursiva para eliminar el heap
void Muerte::eliminarHeap(HeapNode* nodo)
{
    if (nodo == nullptr)
        return;
    eliminarHeap(nodo->left);
    eliminarHeap(nodo->right);
    delete nodo;
}

// Función para obtener la ruta para insertar un nodo basado en su posición
std::string Muerte::obtenerRuta(int posicion)
{
    std::string ruta = "";
    // Ignorar el bit más significativo (raíz)
    while (posicion > 1)
    {
        if (posicion % 2 == 0)
            ruta = "L" + ruta; // Hijo izquierdo
        else
            ruta = "R" + ruta; // Hijo derecho
        posicion /= 2;
    }
    return ruta;
}

// Función para insertar un nodo en el heap siguiendo la ruta
void Muerte::insertarNodo(HeapNode*& raiz, HeapNode* nuevoNodo, const std::string& ruta)
{
    if (raiz == nullptr)
    {
        raiz = nuevoNodo;
        return;
    }

    HeapNode* actual = raiz;
    for (size_t i = 0; i < ruta.length() - 1; ++i)
    {
        if (ruta[i] == 'L')
        {
            if (actual->left == nullptr)
                actual->left = new HeapNode(nullptr, 0);
            actual = actual->left;
        }
        else
        {
            if (actual->right == nullptr)
                actual->right = new HeapNode(nullptr, 0);
            actual = actual->right;
        }
    }

    if (ruta.back() == 'L')
    {
        actual->left = nuevoNodo;
        nuevoNodo->parent = actual;
    }
    else
    {
        actual->right = nuevoNodo;
        nuevoNodo->parent = actual;
    }

    // Heapify hacia arriba para mantener la propiedad de Max Heap
    heapifyUp(nuevoNodo);
}

// Función para heapify hacia arriba
void Muerte::heapifyUp(HeapNode* nodo)
{
    if (nodo == nullptr || nodo->parent == nullptr)
        return;

    HeapNode* padre = nodo->parent;

    if (padre->key < nodo->key)
    {
        // Intercambiar claves y humanos
        std::swap(padre->key, nodo->key);
        std::swap(padre->humano, nodo->humano);

        // Continuar heapify hacia arriba
        heapifyUp(padre);
    }
}

QString Muerte::crearMensajeBitacora(int tipoAlgoritmo, humanoCompleto* humano)
{
    // Obtener la hora y fecha actual
    QDateTime ahora = QDateTime::currentDateTime();
    QString fechaHora = ahora.toString("yyyy-MM-dd HH:mm:ss");

    // Determinar el tipo de algoritmo
    QString tipo;
    if (tipoAlgoritmo == 1)
        tipo = "HEAP";
    else if (tipoAlgoritmo == 2)
        tipo = "ALEATORIO";
    else if (tipoAlgoritmo == 3)
        tipo = "ESPECIFICO";
    else
        tipo = "UNKNOWN";

    // Crear el mensaje con toda la información del humano
    QString mensaje = QString::number(contadorMuertes) + " - " + fechaHora + " - " + tipo + " - ";
    mensaje += "ID: " + QString::number(humano->dataBasica.id) + ", ";
    mensaje += "Nombre: " + QString::fromStdString(humano->dataBasica.nombre) + " " + QString::fromStdString(humano->dataBasica.apellido) + ", ";
    mensaje += "Pais: " + QString::fromStdString(humano->dataBasica.pais) + ", ";
    mensaje += "Creencia: " + QString::fromStdString(humano->dataBasica.creencia) + ", ";
    mensaje += "Profesion: " + QString::fromStdString(humano->dataBasica.profesion) + ", ";
    mensaje += "Fecha de Nacimiento: " + QString::fromStdString(humano->dataBasica.fechaHoraNacimiento) + ", ";
    mensaje += "Lista de Pecados: [";

    for (size_t i = 0; i < humano->dataBasica.listaDePecados.size(); ++i)
    {
        mensaje += QString::number(humano->dataBasica.listaDePecados[i]);
        if (i != humano->dataBasica.listaDePecados.size() - 1)
            mensaje += ", ";
    }
    mensaje += "], ";
    mensaje += "Estado: " + QString(!humano->getEstado() ? "Vivo" : "Muerto");

    return mensaje;
}

// Función para contar los nodos en el heap
int Muerte::contarNodosHeap(HeapNode* nodo)
{
    if (nodo == nullptr)
        return 0;
    return 1 + contarNodosHeap(nodo->left) + contarNodosHeap(nodo->right);
}

// Función para generar un Max Heap basado en la suma de pecados
HeapNode* Muerte::heapMuerteSum()
{
    resetearArchivoEspecifico("muerte");
    eliminarHeap(Raiz); // Reiniciar el heap
    Raiz = nullptr;

    // Contador para la posición de inserción
    int contador = 1;

    NodoHumanoLista* actual = lista.primerNodo;
    int totalVivos = 0; // Contador de humanos vivos

    while (actual != nullptr)
    {
        if (actual->humano != nullptr && actual->humano->dataBasica.status == 1)
        {
            totalVivos++; // Incrementar el contador de vivos

            // Sumar todos los pecados
            int sumaPecados = 0;
            for (int pecadoValor : actual->humano->dataBasica.listaDePecados)
            {
                sumaPecados += pecadoValor;
            }

            // Crear un nuevo HeapNode
            HeapNode* nuevoNodo = new HeapNode(actual->humano, sumaPecados);

            // Obtener la ruta para la inserción
            std::string ruta = obtenerRuta(contador++);

            // Insertar el nodo siguiendo la ruta
            insertarNodo(Raiz, nuevoNodo, ruta);
        }
        actual = actual->siguiente;
    }

    // Verificar la construcción del heap
    int totalNodosHeap = contarNodosHeap(Raiz);
    std::cout << "Total de humanos vivos: " << totalVivos << std::endl;
    std::cout << "Total de nodos en el heap: " << totalNodosHeap << std::endl;

    return Raiz;
}

// Función para generar un Max Heap basado en un pecado específico
HeapNode* Muerte::heapMuerteSpecific(int pecadoIndice)
{
    resetearArchivoEspecifico("muerte");
    eliminarHeap(Raiz); // Reiniciar el heap
    Raiz = nullptr;

    // Validar el índice del pecado
    if (pecadoIndice < 0 || pecadoIndice >= 7) // Asumiendo 7 pecados, índices 0-6
    {
        std::cerr << "Índice de pecado inválido: " << pecadoIndice << ". Debe estar entre 0 y 6." << std::endl;
        return nullptr;
    }

    // Contador para la posición de inserción
    int contador = 1;

    NodoHumanoLista* actual = lista.primerNodo;
    int totalVivos = 0; // Contador de humanos vivos

    while (actual != nullptr)
    {
        if (actual->humano != nullptr && actual->humano->dataBasica.status == 1)
        {
            totalVivos++; // Incrementar el contador de vivos

            // Obtener el valor del pecado específico
            int valorPecado = actual->humano->dataBasica.listaDePecados[pecadoIndice];

            // Crear un nuevo HeapNode
            HeapNode* nuevoNodo = new HeapNode(actual->humano, valorPecado);

            // Obtener la ruta para la inserción
            std::string ruta = obtenerRuta(contador++);

            // Insertar el nodo siguiendo la ruta
            insertarNodo(Raiz, nuevoNodo, ruta);
        }
        actual = actual->siguiente;
    }

    // Verificar la construcción del heap
    int totalNodosHeap = contarNodosHeap(Raiz);
    std::cout << "Total de humanos vivos: " << totalVivos << std::endl;
    std::cout << "Total de nodos en el heap: " << totalNodosHeap << std::endl;

    return Raiz;
}void Muerte::matarPorNivel(int nivel)
{
    resetearArchivoEspecifico("muerte");
    if (Raiz == nullptr)
    {
        std::cerr << "Heap vacío. No hay humanos para matar." << std::endl;
        return;
    }

    if (nivel <= 0)
    {
        std::cerr << "Nivel inválido. Debe ser un entero positivo." << std::endl;
        return;
    }

    // Utilizar una cola para realizar un recorrido por niveles (BFS)
    struct NodoQueue {
        HeapNode* nodo;
        int nivelActual;
        NodoQueue(HeapNode* n, int lvl) : nodo(n), nivelActual(lvl) {}
    };

    // Cola de nodos para el recorrido en anchura
    std::queue<NodoQueue> cola;
    cola.push(NodoQueue(Raiz, 1));

    while (!cola.empty())
    {
        NodoQueue actual = cola.front();
        cola.pop();

        // Detener el recorrido si hemos superado el nivel indicado
        if (actual.nivelActual > nivel)
            break;

        // Verificar que 'humano' no es nullptr antes de acceder
        if (actual.nodo->humano != nullptr && actual.nodo->humano->dataBasica.status == 1)
        {
            actual.nodo->humano->dataBasica.status = 0; // Usar 0 para muerto

            // Incrementar el contador de muertes
            contadorMuertes++;

            // Crear el mensaje de la bitácora
            QString mensaje = crearMensajeBitacora(1, actual.nodo->humano); // 1 representa 'HEAP'
            std::string mensaje2 = mensaje.toStdString();

            // Escribir en bitacoraMuerte y muerteArchivo
            escribirEnBitacora(bitacoraMuerte, mensaje2);
            escribirEnBitacora(muerte, mensaje2);

            std::cout << "Se ha castigado a la persona " << actual.nodo->humano->dataBasica.nombre << " con la muerte." << std::endl;
        }

        // Agregar los hijos a la cola, si existen
        if (actual.nodo->left != nullptr)
            cola.push(NodoQueue(actual.nodo->left, actual.nivelActual + 1));

        if (actual.nodo->right != nullptr)
            cola.push(NodoQueue(actual.nodo->right, actual.nivelActual + 1));
    }

    // Enviar el correo después de procesar los nodos
    enviarCorreoConArchivo(muerte, "muerte");
}



void Muerte::pandemiaAleatoria(int probabilidad)
{
    // Resetear el archivo de muerte
    resetearArchivoEspecifico(muerte);

    if (probabilidad == 0)
    {
        std::cout << "La probabilidad es muy baja. No se matará a ningún humano." << std::endl;
        return;
    }

    NodoHumanoLista* actual = lista.primerNodo;

    while (actual != nullptr)
    {
        if (actual->humano != nullptr && actual->humano->dataBasica.status == 1)
        {
            if(muererng(probabilidad)) {
                actual->humano->dataBasica.status = 0; // Usar 0 para muerto

                // Incrementar el contador de muertes
                contadorMuertes++;

                // Crear el mensaje de la bitácora
                QString mensaje = crearMensajeBitacora(2, actual->humano); // 2 representa 'ALEATORIO'
                std::string mensaje2 = mensaje.toStdString();

                // Escribir en bitacoraMuerte y muerteArchivo
                escribirEnBitacora(bitacoraMuerte, mensaje2);
                escribirEnBitacora(muerte, mensaje2);

                std::cout << "Humano con ID " << actual->humano->dataBasica.id << " ha sido muerto por Pandemia " << (contadorPandemias + 1) << "." << std::endl;
            }
        }
        actual = actual->siguiente;
    }

    // Incrementar el contador de pandemias
    contadorPandemias++;
    enviarCorreoConArchivo(muerte, "muerte");

}




void Muerte::muerteEspecifica(int id)
{
    // Resetear el archivo de muerte
    resetearArchivoEspecifico(muerte);

    // Buscar el humano en la lista
    NodoHumanoLista* actual = lista.primerNodo;
    bool encontrado = false;

    while (actual != nullptr)
    {
        if (actual->humano != nullptr && actual->humano->dataBasica.id == id)
        {
            if (actual->humano->getEstado())
            {
                actual->humano->setEstado(false);
                contadorMuertes++; // Incrementar el contador de muertes

                // Crear el mensaje de la bitácora
                QString mensaje = crearMensajeBitacora(3, actual->humano); // 3 representa 'ESPECIFICO'
                std::string mensaje2 = mensaje.toStdString();

                // Escribir en bitacoraMuerte y muerteArchivo
                escribirEnBitacora(bitacoraMuerte, mensaje2);
                escribirEnBitacora(muerte, mensaje2);

                std::cout << "Humano con ID " << id << " ha sido muerto por Muerte Específico." << std::endl;
            }
            else
            {
                std::cout << "Humano con ID " << id << " ya está muerto." << std::endl;
            }
            encontrado = true;
            break;
        }
        actual = actual->siguiente;
    }

    enviarCorreoConArchivo(muerte, "muerte");

    if (!encontrado)
    {
        std::cout << "Humano con ID " << id << " no encontrado en la lista." << std::endl;
    }
}


void Muerte::imprimirHeapMuerte(HeapNode* nodo, int nivel, std::string tipo)
{
    if (nodo == nullptr)
        return;

    std::string mensaje;
    mensaje += std::string(nivel * 5, ' '); // Añadir espacios para representar el nivel

    if (nodo->humano != nullptr)
    {
        mensaje += std::to_string(nivel) + " " + tipo + ": [ID: " + std::to_string(nodo->humano->dataBasica.id);
        mensaje += ", Nombre: " + nodo->humano->dataBasica.nombre;
        mensaje += " " + nodo->humano->dataBasica.apellido + ", Pecados: " + std::to_string(nodo->key) + "]\n";
    }
    else
    {
        mensaje += std::to_string(nivel) + " " + tipo + ": [Nodo vacío]\n";
    }

    escribirEnBitacora(verHeap, mensaje);

    // Llamadas recursivas para imprimir los nodos hijos izquierdo y derecho
    imprimirHeapMuerte(nodo->left, nivel + 1, "Hijo Izquierdo");
    imprimirHeapMuerte(nodo->right, nivel + 1, "Hijo Derecho");
}



bool Muerte::muererng(int porcentaje) {
    if (porcentaje < 1 || porcentaje > 100) {
        throw std::invalid_argument("El porcentaje debe estar entre 1 y 100");
    }

    // Inicializar la semilla de aleatoriedad solo una vez al inicio del programa
    static bool semillaIniciada = false;
    if (!semillaIniciada) {
        std::srand(static_cast<unsigned>(std::time(nullptr)));
        semillaIniciada = true;
    }

    // Generar un número aleatorio entre 1 y 100
    int numeroAleatorio = std::rand() % 100 + 1;

    return numeroAleatorio <= porcentaje;
}


