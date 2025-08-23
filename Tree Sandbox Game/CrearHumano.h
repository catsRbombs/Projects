#ifndef CREARHUMANO_H
#define CREARHUMANO_H

#include "humanocompleto.h"
#include "utilidad.h"

// Declaración anticipada de la clase Humano
class Humano;

// Declaración de arrays globales
extern array<string, 1000> nombresData;
extern array<string, 1000> apellidosData;
extern array<string, 100> paisesData;
extern array<string, 20> religionesData;
extern array<string, 100> profesionesData;

// Declaración de funciones
void CargarData();
humanoCompleto* generarHumano(int id, int ReencarnacionesCounter, list<humanoCompleto> Reencarnaciones, array<int,7> listaPecados);
humanoCompleto* generarHumanoCompletamenteNuevo(int id);
Humano iterarAmigos(string criterio1, string criterio2, string criterio3);


#endif // CREARHUMANO_H
