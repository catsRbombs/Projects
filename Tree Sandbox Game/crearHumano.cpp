#include "humanocompleto.h"
#include "archivismo.h"
#include "utilidad.h"

array<string,1000> nombresData;
array<string,1000> apellidosData;
array<string,100> paisesData;
array<string,20> religionesData;
array<string,100> profesionesData;

void CargarData() {
    generarCaminoBitacora();

    for(int i = 0; i < 1000; i++){
        nombresData[i] = buscarLinea(i, nombres);
        apellidosData[i] = buscarLinea(i, apellidos);

        if(i < 100){
            paisesData[i] = buscarLinea(i, paises);
            profesionesData[i] = buscarLinea(i, profesiones);
        }

        if(i < 20) religionesData[i] = buscarLinea(i, religiones);
    }

}


humanoCompleto* generarHumano(int id, int ReencarnacionesCounter, list<humanoCompleto> Reencarnaciones, array<int,7> listaPecados) {

    ReencarnacionesCounter++;
    int index = (id + (id + ReencarnacionesCounter) / 2 ) % 1000;

    string nombre = nombresData[index];
    string apellido = apellidosData[index];

    index = (id + (id + ReencarnacionesCounter) / 2 ) % 100;

    string pais = paisesData[index];
    string profesion = profesionesData[index];

    string creencia = religionesData[(id + (id + ReencarnacionesCounter) / 2 ) % 20];
    bool status = true;
    string FechaYHora = obtenerFechaHoraActual();
    humanoCompleto* humanoResultante = new humanoCompleto(id, nombre, apellido, pais, creencia, profesion, FechaYHora, listaPecados, status, Reencarnaciones, ReencarnacionesCounter);
    return humanoResultante;

}

humanoCompleto* generarHumanoCompletamenteNuevo(int id) {

    int index = (id + id / 2) % 1000; // Índice para nombres y apellidos

    string nombre = nombresData[index];
    string apellido = apellidosData[index];

    index = (id + id / 2) % 100; // Índice para países y profesiones

    string pais = paisesData[index];
    string profesion = profesionesData[index];

    int indexCreencia = (id + id / 2) % 20; // Índice para religiones
    string creencia = religionesData[indexCreencia];

    bool status = true;
    string FechaYHora = obtenerFechaHoraActual();

    array<int,7> listaPecados = {}; // Inicializar si es necesario
    list<humanoCompleto> Reencarnaciones;
    int ReencarnacionesCounter = 0;

    humanoCompleto* pene = new humanoCompleto(id, nombre, apellido, pais, creencia, profesion, FechaYHora, listaPecados, &status, Reencarnaciones, ReencarnacionesCounter);
    return pene;
}


Humano iterarAmigos(string criterio1, string criterio2, string criterio3){
    //TODO ITERAR ARBOL y buscar cantidad de amigos apropiada
    return Humano();
}
