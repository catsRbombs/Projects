// Humano.h

#ifndef HUMANO_H
#define HUMANO_H

#include "common.h"

class Humano {

    public:
    // Atributos
    int id = 0;
    string nombre;
    string apellido;
    string pais;
    string creencia;
    string profesion;
    string fechaHoraNacimiento;
    array<int, 7> listaDePecados;
    bool status;

    // Constructor
    Humano(const int& id, const string& nombre, const string& apellido, const string& pais,
           const string& creencia, const string& profesion, const string& fechaHoraNacimiento,
           const array<int, 7>& listaDePecados, const bool& status);

    //Blank constructor
    Humano();

    // Método para imprimir información del humano
    void imprimirInfo() const;
};

#endif // HUMANO_H
