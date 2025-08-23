// mainwindow.h
#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include "ArbolDeHumanos.h"
#include "structs.h"
#include "humanocompleto.h"
#include "muerte.h"
#include "common.h"
#include "arboldeangeles.h"

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    // Slots para los botones
    void enviarValorCrearHumanos();
    void on_matarPorHeadBtn_clicked();
    void on_matarAleatoriamente_clicked();
    void on_matarPorIdbtn_clicked();
    void on_verMaxheapBtn_clicked();
    void on_generarMaxheapPecadoBtn_clicked();
    void on_generarMaxheapSumaBtn_clicked();
    void on_generarPecadosBtn_clicked();
    void on_verHumanosBtn_clicked();
    void on_buscarPorIdBtn_clicked();
    void on_verAngelesBtn_clicked();
    void on_verUltimoNivelHumanidad_clicked();
    void on_nuevoNivelBtn_clicked();
    void on_humanosReencarnadosBtn_clicked();
    void on_rankingPaisesBtn_clicked();
    void on_verHumanidadTxt_clicked();

private:
    Ui::MainWindow *ui;
    ArbolDeHumanos *ArbolHumanos; // Puntero al árbol de humanos
    ListaDobleHumano listaHumanos; // Lista doble de humanos
    ArbolDeAngeles* ArbolAngeles;
    Muerte* muertos;
    QGraphicsScene* scene;
    int contadorPandemia;

    // Función para crear humanos
    void crearHumanos(int cantidad);

    // Función para generar un ID único
    int generarIDUnico(unordered_set<int>& idsExistentes);

    void deleteHeap(HeapNode* root);

    void actualizarLabelMuertos();
};

#endif // MAINWINDOW_H
