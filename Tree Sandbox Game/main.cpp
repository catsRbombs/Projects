#include "mainwindow.h"
#include "common.h"
#include "archivismo.h"
#include "EmailSender.h"
#include <QTimer>
#include "ArbolDeAngeles.h"
#include "crearHumano.h"


#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;

    // Mostrar la ventana de la aplicación Qt
    w.show();
    int ret = a.exec();

    return ret;
}
