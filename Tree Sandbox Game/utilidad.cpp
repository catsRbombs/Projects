// utilidad.cpp
#include "utilidad.h"


// Implementación de generarNumeroAleatorio
int generarNumeroAleatorio(int max)
{
    static   default_random_engine generador(  random_device{}());
      uniform_int_distribution<int> distribucion(0, max - 1);
    return distribucion(generador);
}

// Implementación de obtenerFechaHoraActual
  string obtenerFechaHoraActual()
{
    auto ahora =   chrono::system_clock::now();
      time_t tiempo =   chrono::system_clock::to_time_t(ahora);
      tm* tm_ptr =   localtime(&tiempo);

      stringstream ss;
    ss <<   put_time(tm_ptr, "%Y-%m-%d %H:%M:%S");
    return ss.str();
}


std::string obtenerHastaTabulador(const std::string& texto) {
    size_t posicionTab = texto.find('\t');  // Buscar la posición del primer '\t'
    if (posicionTab != std::string::npos) {
        return texto.substr(0, posicionTab);  // Retornar la subcadena desde el inicio hasta el '\t'
    }
    return texto;  // Si no hay '\t', retornar el string completo
}

 QString extraerID(const QString& texto) {
    // Expresión regular para encontrar "ID:" seguido de dígitos
    QRegularExpression regex(R"(ID:\s*(\d+))");
    QRegularExpressionMatch match = regex.match(texto);

    // Si se encuentra una coincidencia, extraer el grupo de dígitos
    if (match.hasMatch()) {
        return match.captured(1);  // Captura solo los dígitos después de "ID:"
    } else {
        return "";  // Retorna una cadena vacía si no se encuentra "ID:"
    }
}
