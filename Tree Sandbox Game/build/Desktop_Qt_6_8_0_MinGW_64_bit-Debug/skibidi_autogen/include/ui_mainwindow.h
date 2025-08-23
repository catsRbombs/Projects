/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 6.8.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenu>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralwidget;
    QPushButton *crearHumanosBtn;
    QLineEdit *crearHumanosLineEdit;
    QPushButton *matarPorHeadBtn;
    QPushButton *matarAleatoriamente;
    QPushButton *matarPorIdbtn;
    QLineEdit *matarPorIdLineEdit;
    QLineEdit *matarPorHeadNivelesLineEdit;
    QLineEdit *matarAleatoriamenteProbabilidadLineEdit;
    QLabel *label;
    QLabel *label_2;
    QLabel *label_3;
    QPushButton *verMaxheapBtn;
    QPushButton *generarMaxheapPecadoBtn;
    QPushButton *generarMaxheapSumaBtn;
    QLineEdit *pecadoLineEdit;
    QLabel *label_4;
    QPushButton *generarPecadosBtn;
    QLabel *label_5;
    QLabel *label_6;
    QLabel *label_7;
    QLabel *label_8;
    QLabel *humanosVivosLabel;
    QLabel *humanosMuertosLabel;
    QLabel *nivelesArbolHumanosLabel;
    QLabel *nodosArbolHumanosLabel;
    QLabel *label_13;
    QLabel *label_14;
    QPushButton *verHumanosBtn;
    QPushButton *buscarPorIdBtn;
    QLineEdit *buscarPorIdLineEdit;
    QPushButton *verAngelesBtn;
    QPushButton *verUltimoNivelHumanidad;
    QPushButton *nuevoNivelBtn;
    QLabel *label_15;
    QLabel *label_16;
    QLabel *nivelesAngelesLabel;
    QPushButton *humanosReencarnadosBtn;
    QLineEdit *reencarnacionesLineEdit;
    QLabel *label_18;
    QPushButton *rankingPaisesBtn;
    QPushButton *verHumanidadTxt;
    QStatusBar *statusbar;
    QMenuBar *menubar;
    QMenu *menuBien_vs_Mal;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName("MainWindow");
        MainWindow->resize(904, 646);
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName("centralwidget");
        crearHumanosBtn = new QPushButton(centralwidget);
        crearHumanosBtn->setObjectName("crearHumanosBtn");
        crearHumanosBtn->setGeometry(QRect(50, 30, 101, 21));
        crearHumanosLineEdit = new QLineEdit(centralwidget);
        crearHumanosLineEdit->setObjectName("crearHumanosLineEdit");
        crearHumanosLineEdit->setGeometry(QRect(170, 30, 71, 24));
        matarPorHeadBtn = new QPushButton(centralwidget);
        matarPorHeadBtn->setObjectName("matarPorHeadBtn");
        matarPorHeadBtn->setGeometry(QRect(50, 520, 191, 24));
        matarAleatoriamente = new QPushButton(centralwidget);
        matarAleatoriamente->setObjectName("matarAleatoriamente");
        matarAleatoriamente->setGeometry(QRect(50, 550, 191, 24));
        matarPorIdbtn = new QPushButton(centralwidget);
        matarPorIdbtn->setObjectName("matarPorIdbtn");
        matarPorIdbtn->setGeometry(QRect(50, 490, 191, 24));
        matarPorIdLineEdit = new QLineEdit(centralwidget);
        matarPorIdLineEdit->setObjectName("matarPorIdLineEdit");
        matarPorIdLineEdit->setGeometry(QRect(340, 490, 71, 24));
        matarPorHeadNivelesLineEdit = new QLineEdit(centralwidget);
        matarPorHeadNivelesLineEdit->setObjectName("matarPorHeadNivelesLineEdit");
        matarPorHeadNivelesLineEdit->setGeometry(QRect(340, 520, 71, 24));
        matarAleatoriamenteProbabilidadLineEdit = new QLineEdit(centralwidget);
        matarAleatoriamenteProbabilidadLineEdit->setObjectName("matarAleatoriamenteProbabilidadLineEdit");
        matarAleatoriamenteProbabilidadLineEdit->setGeometry(QRect(340, 550, 71, 24));
        matarAleatoriamenteProbabilidadLineEdit->setMaxLength(100);
        label = new QLabel(centralwidget);
        label->setObjectName("label");
        label->setGeometry(QRect(310, 490, 31, 21));
        label_2 = new QLabel(centralwidget);
        label_2->setObjectName("label_2");
        label_2->setGeometry(QRect(260, 550, 71, 21));
        label_3 = new QLabel(centralwidget);
        label_3->setObjectName("label_3");
        label_3->setGeometry(QRect(290, 520, 41, 21));
        verMaxheapBtn = new QPushButton(centralwidget);
        verMaxheapBtn->setObjectName("verMaxheapBtn");
        verMaxheapBtn->setGeometry(QRect(50, 440, 191, 24));
        generarMaxheapPecadoBtn = new QPushButton(centralwidget);
        generarMaxheapPecadoBtn->setObjectName("generarMaxheapPecadoBtn");
        generarMaxheapPecadoBtn->setGeometry(QRect(50, 380, 191, 24));
        generarMaxheapSumaBtn = new QPushButton(centralwidget);
        generarMaxheapSumaBtn->setObjectName("generarMaxheapSumaBtn");
        generarMaxheapSumaBtn->setGeometry(QRect(50, 410, 191, 24));
        pecadoLineEdit = new QLineEdit(centralwidget);
        pecadoLineEdit->setObjectName("pecadoLineEdit");
        pecadoLineEdit->setGeometry(QRect(340, 380, 71, 24));
        label_4 = new QLabel(centralwidget);
        label_4->setObjectName("label_4");
        label_4->setGeometry(QRect(280, 380, 61, 21));
        generarPecadosBtn = new QPushButton(centralwidget);
        generarPecadosBtn->setObjectName("generarPecadosBtn");
        generarPecadosBtn->setGeometry(QRect(50, 70, 191, 24));
        label_5 = new QLabel(centralwidget);
        label_5->setObjectName("label_5");
        label_5->setGeometry(QRect(50, 110, 71, 21));
        QFont font;
        font.setPointSize(11);
        label_5->setFont(font);
        label_6 = new QLabel(centralwidget);
        label_6->setObjectName("label_6");
        label_6->setGeometry(QRect(50, 210, 131, 21));
        label_6->setFont(font);
        label_7 = new QLabel(centralwidget);
        label_7->setObjectName("label_7");
        label_7->setGeometry(QRect(50, 140, 91, 21));
        label_8 = new QLabel(centralwidget);
        label_8->setObjectName("label_8");
        label_8->setGeometry(QRect(50, 170, 111, 21));
        humanosVivosLabel = new QLabel(centralwidget);
        humanosVivosLabel->setObjectName("humanosVivosLabel");
        humanosVivosLabel->setGeometry(QRect(190, 140, 81, 21));
        humanosMuertosLabel = new QLabel(centralwidget);
        humanosMuertosLabel->setObjectName("humanosMuertosLabel");
        humanosMuertosLabel->setGeometry(QRect(190, 170, 81, 21));
        nivelesArbolHumanosLabel = new QLabel(centralwidget);
        nivelesArbolHumanosLabel->setObjectName("nivelesArbolHumanosLabel");
        nivelesArbolHumanosLabel->setGeometry(QRect(190, 250, 91, 21));
        nodosArbolHumanosLabel = new QLabel(centralwidget);
        nodosArbolHumanosLabel->setObjectName("nodosArbolHumanosLabel");
        nodosArbolHumanosLabel->setGeometry(QRect(190, 280, 81, 21));
        label_13 = new QLabel(centralwidget);
        label_13->setObjectName("label_13");
        label_13->setGeometry(QRect(50, 250, 91, 21));
        label_14 = new QLabel(centralwidget);
        label_14->setObjectName("label_14");
        label_14->setGeometry(QRect(50, 280, 111, 21));
        verHumanosBtn = new QPushButton(centralwidget);
        verHumanosBtn->setObjectName("verHumanosBtn");
        verHumanosBtn->setGeometry(QRect(490, 30, 121, 24));
        buscarPorIdBtn = new QPushButton(centralwidget);
        buscarPorIdBtn->setObjectName("buscarPorIdBtn");
        buscarPorIdBtn->setGeometry(QRect(490, 60, 121, 24));
        buscarPorIdLineEdit = new QLineEdit(centralwidget);
        buscarPorIdLineEdit->setObjectName("buscarPorIdLineEdit");
        buscarPorIdLineEdit->setGeometry(QRect(620, 60, 71, 24));
        buscarPorIdLineEdit->setMinimumSize(QSize(71, 24));
        verAngelesBtn = new QPushButton(centralwidget);
        verAngelesBtn->setObjectName("verAngelesBtn");
        verAngelesBtn->setGeometry(QRect(490, 90, 121, 24));
        verUltimoNivelHumanidad = new QPushButton(centralwidget);
        verUltimoNivelHumanidad->setObjectName("verUltimoNivelHumanidad");
        verUltimoNivelHumanidad->setGeometry(QRect(620, 90, 121, 24));
        nuevoNivelBtn = new QPushButton(centralwidget);
        nuevoNivelBtn->setObjectName("nuevoNivelBtn");
        nuevoNivelBtn->setGeometry(QRect(490, 250, 121, 24));
        label_15 = new QLabel(centralwidget);
        label_15->setObjectName("label_15");
        label_15->setGeometry(QRect(490, 190, 141, 21));
        label_15->setFont(font);
        label_16 = new QLabel(centralwidget);
        label_16->setObjectName("label_16");
        label_16->setGeometry(QRect(490, 220, 61, 21));
        nivelesAngelesLabel = new QLabel(centralwidget);
        nivelesAngelesLabel->setObjectName("nivelesAngelesLabel");
        nivelesAngelesLabel->setGeometry(QRect(590, 220, 21, 21));
        humanosReencarnadosBtn = new QPushButton(centralwidget);
        humanosReencarnadosBtn->setObjectName("humanosReencarnadosBtn");
        humanosReencarnadosBtn->setGeometry(QRect(490, 320, 141, 24));
        reencarnacionesLineEdit = new QLineEdit(centralwidget);
        reencarnacionesLineEdit->setObjectName("reencarnacionesLineEdit");
        reencarnacionesLineEdit->setGeometry(QRect(760, 320, 71, 24));
        label_18 = new QLabel(centralwidget);
        label_18->setObjectName("label_18");
        label_18->setGeometry(QRect(650, 320, 101, 21));
        rankingPaisesBtn = new QPushButton(centralwidget);
        rankingPaisesBtn->setObjectName("rankingPaisesBtn");
        rankingPaisesBtn->setGeometry(QRect(490, 140, 121, 24));
        verHumanidadTxt = new QPushButton(centralwidget);
        verHumanidadTxt->setObjectName("verHumanidadTxt");
        verHumanidadTxt->setGeometry(QRect(620, 30, 121, 24));
        MainWindow->setCentralWidget(centralwidget);
        statusbar = new QStatusBar(MainWindow);
        statusbar->setObjectName("statusbar");
        MainWindow->setStatusBar(statusbar);
        menubar = new QMenuBar(MainWindow);
        menubar->setObjectName("menubar");
        menubar->setGeometry(QRect(0, 0, 904, 21));
        menuBien_vs_Mal = new QMenu(menubar);
        menuBien_vs_Mal->setObjectName("menuBien_vs_Mal");
        MainWindow->setMenuBar(menubar);

        menubar->addAction(menuBien_vs_Mal->menuAction());

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QCoreApplication::translate("MainWindow", "MainWindow", nullptr));
        crearHumanosBtn->setText(QCoreApplication::translate("MainWindow", "Crear humanos", nullptr));
        crearHumanosLineEdit->setText(QCoreApplication::translate("MainWindow", "1", nullptr));
        matarPorHeadBtn->setText(QCoreApplication::translate("MainWindow", "Matar por head", nullptr));
        matarAleatoriamente->setText(QCoreApplication::translate("MainWindow", "Matar aleatoriamente", nullptr));
        matarPorIdbtn->setText(QCoreApplication::translate("MainWindow", "Matar especificamente", nullptr));
        matarPorIdLineEdit->setText(QCoreApplication::translate("MainWindow", "1", nullptr));
        matarPorHeadNivelesLineEdit->setText(QCoreApplication::translate("MainWindow", "1", nullptr));
        matarAleatoriamenteProbabilidadLineEdit->setText(QCoreApplication::translate("MainWindow", "0", nullptr));
        label->setText(QCoreApplication::translate("MainWindow", "ID:", nullptr));
        label_2->setText(QCoreApplication::translate("MainWindow", "Probabilidad:", nullptr));
        label_3->setText(QCoreApplication::translate("MainWindow", "Niveles:", nullptr));
        verMaxheapBtn->setText(QCoreApplication::translate("MainWindow", "Ver Maxheap", nullptr));
        generarMaxheapPecadoBtn->setText(QCoreApplication::translate("MainWindow", "Generar Maxheap por pecado", nullptr));
        generarMaxheapSumaBtn->setText(QCoreApplication::translate("MainWindow", "Generar Maxheap por suma", nullptr));
        pecadoLineEdit->setText(QCoreApplication::translate("MainWindow", "1", nullptr));
        label_4->setText(QCoreApplication::translate("MainWindow", "Pecado: ", nullptr));
        generarPecadosBtn->setText(QCoreApplication::translate("MainWindow", "Generar pecados", nullptr));
        label_5->setText(QCoreApplication::translate("MainWindow", "Humanos", nullptr));
        label_6->setText(QCoreApplication::translate("MainWindow", "Arbol de humanos", nullptr));
        label_7->setText(QCoreApplication::translate("MainWindow", "Humanos vivos:", nullptr));
        label_8->setText(QCoreApplication::translate("MainWindow", "Humanos muertos:", nullptr));
        humanosVivosLabel->setText(QCoreApplication::translate("MainWindow", "0", nullptr));
        humanosMuertosLabel->setText(QCoreApplication::translate("MainWindow", "0", nullptr));
        nivelesArbolHumanosLabel->setText(QCoreApplication::translate("MainWindow", "0", nullptr));
        nodosArbolHumanosLabel->setText(QCoreApplication::translate("MainWindow", "0", nullptr));
        label_13->setText(QCoreApplication::translate("MainWindow", "Niveles:", nullptr));
        label_14->setText(QCoreApplication::translate("MainWindow", "Nodos:", nullptr));
        verHumanosBtn->setText(QCoreApplication::translate("MainWindow", "Humanidad (Arbol)", nullptr));
        buscarPorIdBtn->setText(QCoreApplication::translate("MainWindow", "Buscar por ID", nullptr));
        buscarPorIdLineEdit->setText(QCoreApplication::translate("MainWindow", "1", nullptr));
        verAngelesBtn->setText(QCoreApplication::translate("MainWindow", "Angeles (Arbol)", nullptr));
        verUltimoNivelHumanidad->setText(QCoreApplication::translate("MainWindow", "Ultimo Nivel Arbol", nullptr));
        nuevoNivelBtn->setText(QCoreApplication::translate("MainWindow", "Nuevo nivel", nullptr));
        label_15->setText(QCoreApplication::translate("MainWindow", "Arbol de angeles", nullptr));
        label_16->setText(QCoreApplication::translate("MainWindow", "Niveles:", nullptr));
        nivelesAngelesLabel->setText(QCoreApplication::translate("MainWindow", "2", nullptr));
        humanosReencarnadosBtn->setText(QCoreApplication::translate("MainWindow", "Humanos reencarnados", nullptr));
        reencarnacionesLineEdit->setText(QCoreApplication::translate("MainWindow", "1", nullptr));
        label_18->setText(QCoreApplication::translate("MainWindow", "Reencarnaciones:", nullptr));
        rankingPaisesBtn->setText(QCoreApplication::translate("MainWindow", "Ranking por paises", nullptr));
        verHumanidadTxt->setText(QCoreApplication::translate("MainWindow", "Humanidad (Txt)", nullptr));
        menuBien_vs_Mal->setTitle(QCoreApplication::translate("MainWindow", "Bien vs Mal", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
