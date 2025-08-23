#include "humano.h"

Humano::Humano(const int& id, const string& nombre, const string& apellido, const string& pais,
               const string& creencia, const string& profesion, const string& fechaHoraNacimiento,
               const array<int, 7>& listaDePecados, const bool& status)
    : id(id), nombre(nombre), apellido(apellido), pais(pais), creencia(creencia),
    profesion(profesion), fechaHoraNacimiento(fechaHoraNacimiento),
    listaDePecados(listaDePecados), status(status) {}

Humano::Humano() {
    nombre = "Pending";
    apellido = "Pending";
    pais = "Pending";
    creencia = "Pending";
    profesion = "Pending";
    fechaHoraNacimiento = "Pending";
    status = false;
}

void Humano::imprimirInfo() const {
    cout << "ID: " << id << "\n";
    cout << "Nombre: " << nombre << " " << apellido << "\n";
    cout << "Pais: " << pais << "\n";
    cout << "Creencia: " << creencia << "\n";
    cout << "Profesion: " << profesion << "\n";
    cout << "Fecha y hora de Nacimiento: " << fechaHoraNacimiento << "\n";
    cout << "Lista de Pecados:\n";
    for (const auto& pecado : listaDePecados) {
        cout << " - " << pecado << "\n";
    }
    cout << "Status: " << status << "\n";
}

