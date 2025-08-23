// humanoCompleto.cpp
#include "utilidad.h"
#include "humanocompleto.h"

// Constructor por defecto
humanoCompleto::humanoCompleto()
    : contadorReencarnaciones(0), status(true) // Inicializado como vivo
{
    // Inicialización por defecto de dataBasica y listaAmigos
}

// Constructor completo
humanoCompleto::humanoCompleto(const int& id, const   string& nombre, const   string& apellido, const   string& pais,
                               const   string& creencia, const   string& profesion, const   string& fechaHoraNacimiento,
                               const   array<int, 7>& listaDePecados, const bool& status_,
                               const   list<humanoCompleto>& reencarnaciones_, const int& contadorReencarnaciones_)
    : dataBasica(id, nombre, apellido, pais, creencia, profesion, fechaHoraNacimiento, listaDePecados, status_),
    reencarnaciones(reencarnaciones_), contadorReencarnaciones(contadorReencarnaciones_), status(status_)
{
    // Inicialización de listaAmigos si es necesario
}

humanoCompleto::humanoCompleto(humanoCompleto* humano) : dataBasica(humano->dataBasica.id, humano->dataBasica.nombre, humano->dataBasica.apellido, humano->dataBasica.pais, humano->dataBasica.creencia, humano->dataBasica.profesion, humano->dataBasica.fechaHoraNacimiento, humano->dataBasica.listaDePecados, humano->dataBasica.status),
    reencarnaciones(humano->reencarnaciones), contadorReencarnaciones(humano->contadorReencarnaciones) {}

// Constructor para reencarnaciones
humanoCompleto::humanoCompleto(const   list<humanoCompleto>& reenarnaciones_, const int& contadorReencarnaciones_)
    : reencarnaciones(reenarnaciones_), contadorReencarnaciones(contadorReencarnaciones_), status(true)
{
    // Inicialización de dataBasica y listaAmigos si es necesario
}



// Métodos para configurar dataBasica
void humanoCompleto::setdataBasica(const int& id, const   string& nombre, const   string& apellido, const   string& pais,
                                   const   string& creencia, const   string& profesion, const   string& fechaHoraNacimiento,
                                   const   array<int, 7>& listaDePecados, const bool& status_)
{
    dataBasica.id = id;
    dataBasica.nombre = nombre;
    dataBasica.apellido = apellido;
    dataBasica.pais = pais;
    dataBasica.creencia = creencia;
    dataBasica.profesion = profesion;
    dataBasica.fechaHoraNacimiento = fechaHoraNacimiento;
    dataBasica.listaDePecados = listaDePecados;
    status = status_;
}

// Método para copiar dataBasica de otro humano
void humanoCompleto::setdataBasicaHumano(Humano& previo)
{
    dataBasica = previo;
}

// Métodos para manejar el estado
void humanoCompleto::setEstado(bool nuevoEstado)
{
    status = nuevoEstado;
}

bool humanoCompleto::getEstado() const
{
    return status;
}

void humanoCompleto::generarPecado(){
    dataBasica.listaDePecados[0] += generarNumeroAleatorio(100);
    dataBasica.listaDePecados[1] += generarNumeroAleatorio(100);
    dataBasica.listaDePecados[2] += generarNumeroAleatorio(100);
    dataBasica.listaDePecados[3] += generarNumeroAleatorio(100);
    dataBasica.listaDePecados[4] += generarNumeroAleatorio(100);
    dataBasica.listaDePecados[5] += generarNumeroAleatorio(100);
    dataBasica.listaDePecados[6] += generarNumeroAleatorio(100);
}




QString humanoCompleto::escribirDetallesHumanidad() {
    QString mensaje;

    // Validar que el id es válido (mayor o igual a 0 en este caso)
    if (dataBasica.id < 0) {
        qWarning("ID inválido");
        return "ID inválido";
    }

    // Validación del estado (Vivo o Muerto)
    QString temporal = dataBasica.status ? "Vivo" : "Muerto";

    // Validar y construir la parte principal del mensaje
    mensaje = QString::fromStdString(std::to_string(dataBasica.id)) + "\t" +
              QString::fromStdString(dataBasica.nombre.empty() ? "Sin nombre" : dataBasica.nombre) + "\t" +
              QString::fromStdString(dataBasica.apellido.empty() ? "Sin apellido" : dataBasica.apellido) + "\t" +
              QString::fromStdString(dataBasica.pais.empty() ? "Desconocido" : dataBasica.pais) + "\t" +
              temporal + "\t" +
              QString::fromStdString(dataBasica.profesion.empty() ? "Sin profesión" : dataBasica.profesion) + "\t" +
              QString::fromStdString(dataBasica.creencia.empty() ? "Sin creencia" : dataBasica.creencia) + "\t" +
              QString::fromStdString(dataBasica.fechaHoraNacimiento.empty() ? "Fecha desconocida" : dataBasica.fechaHoraNacimiento) + "\t";

    // Validar la lista de pecados antes de iterar
    if (dataBasica.listaDePecados.empty()) {
        qWarning("Lista de pecados vacía");
        mensaje += "Sin pecados\t";
    } else {
        for (int p : dataBasica.listaDePecados) {
            mensaje += QString::number(p) + "\t";
        }
    }

    // Agregar contador de amigos
    mensaje += QString::number(amigosCounter) + "\t";

    // Validar la lista de amigos antes de iterar

    for (const Humano& amigo : listaAmigos) {
        if (amigo.id < 0 || amigo.id>99999) {
            mensaje += "0\t";
        } else {
            mensaje += QString::number(amigo.id) + "\t";
        }
    }


    // Agregar el contador de reencarnaciones
    mensaje += QString::number(contadorReencarnaciones);

    return mensaje;
}



void humanoCompleto::partirPecadosMitad() {
    dataBasica.listaDePecados[0] = dataBasica.listaDePecados[0]/2;
    dataBasica.listaDePecados[1] = dataBasica.listaDePecados[1]/2;
    dataBasica.listaDePecados[2] = dataBasica.listaDePecados[2]/2;
    dataBasica.listaDePecados[3] = dataBasica.listaDePecados[3]/2;
    dataBasica.listaDePecados[4] = dataBasica.listaDePecados[4]/2;
    dataBasica.listaDePecados[5] = dataBasica.listaDePecados[5]/2;
    dataBasica.listaDePecados[6] = dataBasica.listaDePecados[6]/2;
}




