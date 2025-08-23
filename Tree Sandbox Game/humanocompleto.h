// humanoCompleto.h
#ifndef HUMANOCOMPLETO_H
#define HUMANOCOMPLETO_H

#include "humano.h"


struct humanoCompleto
{
public:
    Humano dataBasica;
    array<Humano, 10> listaAmigos;
    list<humanoCompleto> reencarnaciones;
    int contadorReencarnaciones = 0;
    int amigosCounter = 0;
    bool status = true; // true = vivo, false = muerto


    // Constructores
    humanoCompleto();
    humanoCompleto(const int& id, const   string& nombre, const   string& apellido, const   string& pais,
                   const   string& creencia, const   string& profesion, const   string& fechaHoraNacimiento,
                   const   array<int, 7>& listaDePecados, const bool& status,
                   const   list<humanoCompleto>& reencarnaciones, const int& contadorReencarnaciones);
    humanoCompleto(const   list<humanoCompleto>& reenarnaciones, const int& contadorReencarnaciones);

    humanoCompleto( humanoCompleto* humano );
    // Métodos para manejar la lista de amigos
    void setListaAmigos(Humano& Amigo);

    // Métodos para configurar dataBasica
    void setdataBasica(const int& id, const   string& nombre, const   string& apellido, const   string& pais,
                       const   string& creencia, const   string& profesion, const   string& fechaHoraNacimiento,
                       const   array<int, 7>& listaDePecados, const bool& status);
    void setdataBasicaHumano(Humano& previo);

    // Método para validar amistad
    bool validarAmistad(Humano& amigo);

    // Métodos para manejar el estado
    void setEstado(bool nuevoEstado);
    bool getEstado() const;
    void generarPecado();
    void partirPecadosMitad();
    QString escribirDetallesHumanidad();


};

#endif // HUMANOCOMPLETO_H
