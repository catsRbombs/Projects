// EmailSender.cpp
#include "EmailSender.h"
#include "common.h"

QString generarCaminoSendEmail() {
    QString camino = QFileInfo(__FILE__).absolutePath();
    camino = QDir(camino).filePath("sendemail");

    // Crear la carpeta si no existe
    if (!QDir(camino).exists()) {
        if (QDir().mkpath(camino)) {
            qDebug() << "Carpeta creada exitosamente:" << camino;
        } else {
            qDebug() << "Error al crear la carpeta:" << camino;
            return "";  // Devolver cadena vacía si no se puede crear la carpeta
        }
    }
    return camino;  // Devolver la ruta si la carpeta ya existe o se creó con éxito
}

void enviarCorreoConArchivo( QString& pathArchivo,  string preset)
{
    QString rutaJar = QDir::toNativeSeparators(generarCaminoSendEmail() + "/SendEmailApp.jar");

    QString destinatario;
    QString asunto;
    QString mensaje;
    QString remitente;
    QString contrasena;
    QString servidorSMTP;
    QString puertoSMTP;

    if (preset == "muerte")
    {
        destinatario = "muerteproyecto2FG@gmail.com";
        asunto = "Archivo de Muerte";
        mensaje = "Adjunto el archivo de muerte.";
        remitente = "muerteproyecto2FG@gmail.com"; //muerteproyecto2FG@gmail.com
        contrasena = "oxwxnztuetamxrzl";
        servidorSMTP = "smtp.gmail.com";
        puertoSMTP = "465";
    }
    else if (preset == "angel")
    {
        destinatario = "angelesproyecto2FG@gmail.com";
        asunto = "Archivo de Reencarnacion";
        mensaje = "Adjunto el archivo de Reencarnacion.";
        remitente = "angelesproyecto2FG@gmail.com";
        contrasena = "nsxuosbiucdgagpa";
        servidorSMTP = "smtp.gmail.com";
        puertoSMTP = "465";
    }
    else
    {
        qDebug() << "Preset no reconocido:" << preset;
        return;
    }

    // Asegúrate de que `rutaJar` y `pathArchivo` sean válidos
    if (rutaJar.isEmpty() || !QFile::exists(rutaJar)) {
        qDebug() << "Ruta al archivo .jar no válida o archivo .jar no encontrado:" << rutaJar;
        return;
    }

    if (pathArchivo.isEmpty() || !QFile::exists(pathArchivo)) {
        qDebug() << "Ruta al archivo adjunto no válida o archivo no encontrado:" << pathArchivo;
        return;
    }

    // Llenar los argumentos usando las variables del preset adecuado
    QStringList argumentos;
    argumentos << "-jar" << rutaJar
               << servidorSMTP << puertoSMTP
               << remitente << contrasena
               << destinatario << asunto
               << mensaje << QDir::toNativeSeparators(pathArchivo);


    // Crear el proceso y ejecutarlo
    QProcess proceso;
    proceso.start("java", argumentos);
    proceso.waitForFinished();

    // Verificar si hubo errores
    QString salida = proceso.readAllStandardOutput();
    QString error = proceso.readAllStandardError();

    if (!error.isEmpty())
    {
        qDebug() << "Error al enviar el correo:" << error;
    }
    else
    {
        qDebug() << "Correo enviado exitosamente.";
    }
}



