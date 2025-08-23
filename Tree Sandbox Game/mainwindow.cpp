#include "mainwindow.h"
#include "./ui_mainwindow.h"
#include "EmailSender.h"
#include "common.h"
#include "ArbolDeHumanos.h"
#include "archivismo.h"
#include "CrearHumano.h"

// Constructor
MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
    , ArbolHumanos(new ArbolDeHumanos())  // Inicializar el puntero ArbolHumanos
    , ArbolAngeles(new ArbolDeAngeles())
    , listaHumanos(*new ListaDobleHumano())
    , muertos(nullptr)
    , contadorPandemia(0)
{
    ui->setupUi(this);

    muertos = new Muerte(listaHumanos);

    scene = new QGraphicsScene(this);

    ArbolAngeles->inicializarArbol();


    // Configurar validadores para las QLineEdit que reciben números
    ui->crearHumanosLineEdit->setValidator(new QIntValidator(1, 99999, this));
    ui->matarPorIdLineEdit->setValidator(new QIntValidator(1, 99999, this));
    ui->matarPorHeadNivelesLineEdit->setValidator(new QIntValidator(1, 100, this)); // Ajusta el rango según necesidad
    ui->matarAleatoriamenteProbabilidadLineEdit->setValidator(new QIntValidator(1, 100, this)); // Ajusta el rango según necesidad
    ui->pecadoLineEdit->setValidator(new QIntValidator(1, 100, this)); // Ajusta el rango según necesidad
    ui->buscarPorIdLineEdit->setValidator(new QIntValidator(1, 99999, this));
    ui->reencarnacionesLineEdit->setValidator(new QIntValidator(1, 1000, this)); // Ajusta el rango según necesidad

    // Establecer valores iniciales5
    ui->crearHumanosLineEdit->setText("1");
    ui->matarPorIdLineEdit->setText("1");
    ui->matarPorHeadNivelesLineEdit->setText("1");
    ui->matarAleatoriamenteProbabilidadLineEdit->setText("1");
    ui->pecadoLineEdit->setText("1");
    ui->buscarPorIdLineEdit->setText("1");
    ui->reencarnacionesLineEdit->setText("1");

    // Conectar el botón "Crear humanos" a su slot
    connect(ui->crearHumanosBtn, &QPushButton::clicked, this, &MainWindow::enviarValorCrearHumanos);

    CargarData();
    resetearArchivoEspecifico(bitacoraMuerte);
}

MainWindow::~MainWindow()
{

    delete ui;
    delete ArbolHumanos;
}


void MainWindow::on_verMaxheapBtn_clicked()
{
    resetearArchivoEspecifico(verHeap);

    if (muertos->Raiz != nullptr){
        muertos->imprimirHeapMuerte(muertos->Raiz, 0, "Raiz");
        QUrl url = QUrl::fromLocalFile(verHeap);
        if (!QDesktopServices::openUrl(url)) {
            qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
        }

    } else {
        QMessageBox::warning(this, "Heap Inexistente", "Genere un heap antes de visualizarlo.");
    }
}

void MainWindow::actualizarLabelMuertos(){
    ui->humanosMuertosLabel->setText(QString::number(muertos->contadorMuertes));
    ui->humanosVivosLabel->setText(QString::number(listaHumanos.contarVivos()));

}


// Slot para el botón "Crear humanos"
void MainWindow::enviarValorCrearHumanos()
{
    if (ArbolHumanos->raiz != nullptr){
        ArbolHumanos->eliminarRecursivo(ArbolHumanos->raiz);
    }
    int valor = ui->crearHumanosLineEdit->text().toInt();
    crearHumanos(valor); // Llamar a la función para crear humanos

    // Construir el árbol con los humanos creados
    int totalHumanos = listaHumanos.largo();

    // Crear un array dinámico de punteros a puenteParaDirecciones
    puenteParaDirecciones** humanosParaArbol = new puenteParaDirecciones*[totalHumanos];

    NodoHumanoLista* actual = listaHumanos.primerNodo;
    int index = 0;

    while (actual != nullptr) {
        // Crear un nuevo puenteParaDirecciones dinámicamente
        puenteParaDirecciones* creado = new puenteParaDirecciones;
        creado->direccion = actual;
        creado->humano = actual->humano;

        // Almacenar el puntero en el array
        humanosParaArbol[index++] = creado;

        actual = actual->siguiente;
    }

    muertos->lista = listaHumanos;

    // Construir el árbol
    ArbolHumanos->construirArbol(humanosParaArbol, totalHumanos);
    ArbolHumanos->mostrarInformacion(totalHumanos);

    ui->nivelesArbolHumanosLabel->setText(QString::number(ArbolHumanos->obtenerAltura(ArbolHumanos->raiz)));
    ui->nodosArbolHumanosLabel->setText(QString::number(ArbolHumanos->obtenerNumeroElementos(ArbolHumanos->raiz)));
    ui->humanosVivosLabel->setText(QString::number(listaHumanos.contarVivos()));
}


// Función para crear humanos y agregarlos a la lista doble
void MainWindow::crearHumanos(int cantidad)
{
    if(cantidad <= 0)
    {
          cout << "Cantidad invalida para crear humanos.\n";
        return;
    }

    // Generar humanos
    for(int i = 0; i < cantidad; ++i)
    {
        int idUnico = generarIDUnico(listaHumanos.idsExistentes);
        humanoCompleto* humanoNuevo = new humanoCompleto(generarHumanoCompletamenteNuevo(idUnico));

        listaHumanos.insertarAlFinal(humanoNuevo);
        listaHumanos.encontrarAmigos(*humanoNuevo);
    }

      cout << cantidad << " humanos creados e insertados en la lista.\n";
}




// Función para generar un ID único
int MainWindow::generarIDUnico(unordered_set<int>& idsExistentes)
{
    int nuevoID;
    do
    {
        nuevoID = generarNumeroAleatorio(99999) + 1;  // Generar ID entre 1 y 99999
    } while (idsExistentes.find(nuevoID) != idsExistentes.end());

    idsExistentes.insert(nuevoID);  // Guardar el ID como existente
    return nuevoID;
}
/////////////////////////////////////////Muertes

void MainWindow::on_generarMaxheapSumaBtn_clicked()
{
    // Generar el heap basado en la suma de pecados
    muertos->heapMuerteSum();

    // Actualizar la interfaz de usuario según sea necesario
    ui->humanosVivosLabel->setText(QString::number(listaHumanos.contarVivos()));

    QMessageBox::information(this, "Heap Generado", "Se ha generado un Max Heap basado en la suma de pecados.");
}

void MainWindow::on_generarMaxheapPecadoBtn_clicked()
{
    // Obtener el índice del pecado desde el QLineEdit
    bool ok;
    int pecadoIndice = ui->pecadoLineEdit->text().toInt(&ok);

    // Ajustar el índice para que coincida con los índices de 0 a 6
    pecadoIndice -= 1;

    // Validar la conversión y el rango del índice
    if (!ok || pecadoIndice < 0 || pecadoIndice >= 7) // Asumiendo 7 pecados, índices 0-6
    {
        QMessageBox::warning(this, "Entrada Inválida", "Ingrese un índice de pecado válido (1-7).");
        return;
    }

    // Generar el heap basado en un pecado específico
    muertos->heapMuerteSpecific(pecadoIndice);

    // Actualizar la interfaz de usuario según sea necesario
    ui->humanosVivosLabel->setText(QString::number(listaHumanos.contarVivos()));

    QMessageBox::information(this, "Heap Generado", "Se ha generado un Max Heap basado en el pecado específico.");
}

// Función para matar humanos aleatoriamente
void MainWindow::on_matarAleatoriamente_clicked()
{
    // Obtener la probabilidad desde el QLineEdit
    cout << "Pandemia numero " << contadorPandemia++ << endl;
    bool ok;

    // Llamar a la función para matar aleatoriamente
    muertos->pandemiaAleatoria(ui->matarAleatoriamenteProbabilidadLineEdit->text().toInt());

    // Actualizar la interfaz de usuario según sea necesario
    actualizarLabelMuertos();
}


void MainWindow::on_matarPorHeadBtn_clicked()
{
    // Obtener el nivel desde el QLineEdit
    bool ok;
    int nivel = ui->matarPorHeadNivelesLineEdit->text().toInt(&ok);

    // Validar la conversión y que el nivel sea positivo
    if (!ok || nivel <= 0)
    {
        QMessageBox::warning(this, "Entrada Inválida", "Ingrese un nivel válido (entero positivo).");
        return;
    }

    // Llamar a la función para matar humanos en el nivel especificado
    muertos->matarPorNivel(nivel);

    // Actualizar la interfaz de usuario según sea necesario
    actualizarLabelMuertos();
}

// Función para matar un humano específicamente por ID
void MainWindow::on_matarPorIdbtn_clicked()
{
    // Obtener el ID desde el QLineEdit
    bool ok;
    int id = ui->matarPorIdLineEdit->text().toInt(&ok);

    // Validar la conversión
    if (!ok || id <= 0)
    {
        QMessageBox::warning(this, "Entrada Inválida", "Ingrese un ID válido (entero positivo).");
        return;
    }

    puenteParaDirecciones* humanpntr = ArbolHumanos->buscarHumanoCercano(id);
    if(humanpntr == nullptr)
    {
        humanpntr = new puenteParaDirecciones();
        humanpntr->direccion = listaHumanos.buscarPorId((id));
    }
    else
    {
        humanpntr->direccion = listaHumanos.buscarPorIdCheckpoint(humanpntr->direccion, id);
    }

    if(humanpntr->direccion == nullptr) {
        QMessageBox::warning(this, "ERROR", "La id ingresada no se encuentra registrada." );
        return;
    }

    // Corrected comparison using '=='
    if(humanpntr->direccion->humano->dataBasica.status == 0) {
        QMessageBox::warning(this, "ERROR", "Este humano ya está muerto.");
        return;
    }

    resetearArchivoEspecifico(muerte);

    // Set status to 0 (dead)
    humanpntr->direccion->humano->dataBasica.status = 0;

    QMessageBox::information(this, "Éxito", "Se ha castigado con muerte al humano " + QString::fromStdString(humanpntr->direccion->humano->dataBasica.nombre));

    QString mensaje = muertos->crearMensajeBitacora(3, humanpntr->direccion->humano); // 3 representa 'ESPECIFICO'
    std::string mensaje2 = mensaje.toStdString();

    // Escribir en bitacoraMuerte y muerteArchivo
    escribirEnBitacora(bitacoraMuerte, mensaje2);
    escribirEnBitacora(muerte, mensaje2);

    enviarCorreoConArchivo(muerte, "muerte");

    // Actualizar la interfaz de usuario según sea necesario
    actualizarLabelMuertos();
}

// Función para eliminar el heap de manera recursiva
void MainWindow::deleteHeap(HeapNode* root)
{
    if (root == nullptr)
        return;

    deleteHeap(root->left);
    deleteHeap(root->right);
    delete root;
}

void MainWindow::on_generarPecadosBtn_clicked()
{
    listaHumanos.pecarAll();
}

void MainWindow::on_verHumanosBtn_clicked()
{
    resetearArchivoEspecifico(arbolHumanidad);
    ArbolHumanos->imprimirArbolHumanidad(ArbolHumanos->raiz,0,"Raiz");
    QUrl url = QUrl::fromLocalFile(arbolHumanidad);
    if (!QDesktopServices::openUrl(url)) {
        qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
    }
}

void MainWindow::on_verHumanidadTxt_clicked(){
    resetearArchivoEspecifico(humanidad);
    NodoHumanoLista* tmp = listaHumanos.primerNodo;
    while (tmp != nullptr){
        string mensaje = (tmp->humano->escribirDetallesHumanidad()).toStdString();
        escribirEnBitacora(humanidad,mensaje);
        tmp = tmp->siguiente;
    }
    QUrl url = QUrl::fromLocalFile(humanidad);
    if (!QDesktopServices::openUrl(url)) {
        qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
    }
}

void MainWindow::on_buscarPorIdBtn_clicked()
{
    int valor = ui->buscarPorIdLineEdit->text().toInt();
    puenteParaDirecciones* humanpntr = ArbolHumanos->buscarHumanoCercano(valor);
    if(humanpntr == nullptr)
    {

        humanpntr = new puenteParaDirecciones();
        humanpntr->direccion = listaHumanos.buscarPorId((valor));
            }
    else
    {
        humanpntr->direccion = listaHumanos.buscarPorIdCheckpoint(humanpntr->direccion, valor);
            }
    if(humanpntr->direccion == nullptr) {QMessageBox::warning(this, "ERROR", "La id ingresada no se encuentra registrada." ); return;}
    string mensaje = (humanpntr->direccion->humano->escribirDetallesHumanidad()).toStdString();
    resetearArchivoEspecifico(buscarPorIdTxt);
    escribirEnBitacora(buscarPorIdTxt,mensaje);
    QUrl url = QUrl::fromLocalFile(buscarPorIdTxt);
    if (!QDesktopServices::openUrl(url)) {
        qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
    }

}

void MainWindow::on_verAngelesBtn_clicked()
{
    resetearArchivoEspecifico(verArbolAngeles);
    ArbolAngeles->imprimirArbolAngeles(ArbolAngeles->raiz,0,"DIOS");
    QUrl url = QUrl::fromLocalFile(verArbolAngeles);
    if (!QDesktopServices::openUrl(url)) {
        qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
    }
}

void MainWindow:: on_verUltimoNivelHumanidad_clicked()
{
    resetearArchivoEspecifico(ultimoNivel);
    string titulo = "Ultimo nivel del arbol de la humanidad\n";
    escribirEnBitacora(ultimoNivel,titulo);
    ArbolHumanos->encontrarNodosUltimoNivel(ArbolHumanos->raiz);
    QUrl url = QUrl::fromLocalFile(ultimoNivel);
    if (!QDesktopServices::openUrl(url)) {
        qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
    }


}

void MainWindow::on_nuevoNivelBtn_clicked()
{
    ArbolAngeles->agregarNivel();
    ui->nivelesAngelesLabel->setText(QString::number(ui->nivelesAngelesLabel->text().toInt() + 1));
    //Buscando humanos para salvar
    queue<NodoAngel*> cola = ArbolAngeles->nivelMasBajo;

    cout << "Angeles en nivel " << cola.size() << endl;

    resetearArchivoEspecifico(bitacoraAngel);

    while(!cola.empty()) {

        NodoAngel* angel = cola.front();
        cola.pop();
        QString minihumano = leerYBorrarUltimaLinea(bitacoraMuerte);
        cout << minihumano.toStdString() << endl;
        minihumano = extraerID(minihumano);
        if(minihumano.toStdString() == "0") {minihumano = leerYBorrarUltimaLinea(bitacoraMuerte); minihumano = extraerID(minihumano);}
        try {
            int id = minihumano.toInt();
            puenteParaDirecciones* humanpntr = ArbolHumanos->buscarHumanoCercano(id);
            cout << id<< endl;
                cout << "if\n";
                humanpntr = new puenteParaDirecciones();
                humanpntr->direccion = listaHumanos.buscarPorId((id));

            if(humanpntr->direccion == nullptr) {return;}
            int reencarnacionesCounter = humanpntr->direccion->humano->contadorReencarnaciones;
            list<humanoCompleto> reencarnaciones = humanpntr->direccion->humano->reencarnaciones;
            humanpntr->direccion->humano->partirPecadosMitad();
            array<int,7> pecados = humanpntr->direccion->humano->dataBasica.listaDePecados;
            humanoCompleto* Reencarnacion = generarHumano(humanpntr->direccion->humano->dataBasica.id, reencarnacionesCounter, reencarnaciones, pecados);
            humanoCompleto old = new humanoCompleto(humanpntr->direccion->humano);
            humanpntr->direccion->humano = Reencarnacion;
            humanpntr->direccion->humano->reencarnaciones.push_back(old);
            humanpntr->direccion->humano->dataBasica.id = old.dataBasica.id;
            angel->angel->asignarHumano(Reencarnacion);
            humanoCompleto* huma = &old;
            Humano* hum = &humanpntr->direccion->humano->dataBasica;
            string mensaje = "El humano " + huma->dataBasica.nombre + " " + huma->dataBasica.apellido + " ID:" + std::to_string(huma->dataBasica.id) +
                             " " + huma->dataBasica.pais + " " + huma->dataBasica.profesion + " " + huma-> dataBasica.creencia + "fue reincarnado por el angel " +
                             angel->angel->nombre + ", asi convirtiendose en " + hum->nombre + " " + hum->apellido + " ID: " + std::to_string(hum->id) +
                             " " + hum -> pais + " " + hum -> profesion + " " + hum -> creencia;
            escribirEnBitacora(bitacoraAngel, mensaje);
            cout << "Se ha reincarnado un humano " << endl;

        }
        catch(invalid_argument e) {
            cout << "Hubo un error de lectura de muertos (Podrian no quedar muertos disponibles) " << endl;
            return;
        }
    }

    ui->humanosMuertosLabel->setText(QString::number(listaHumanos.contarMuertos()));
    ui->humanosVivosLabel->setText(QString::number(listaHumanos.contarVivos()));
    enviarCorreoConArchivo(bitacoraAngel, "angel");
}

void MainWindow::on_humanosReencarnadosBtn_clicked()
{
    int valor = ui->reencarnacionesLineEdit->text().toInt();
    listaHumanos.escribirReencarnaciones(valor);
    QUrl url = QUrl::fromLocalFile(verReencarnaciones);
    if (!QDesktopServices::openUrl(url)) {
        qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
    }
}

void MainWindow::on_rankingPaisesBtn_clicked()
{
    resetearArchivoEspecifico(verRanking);
    listaHumanos.escribirRankingPaises();
    QUrl url = QUrl::fromLocalFile(verRanking);
    if (!QDesktopServices::openUrl(url)) {
        qWarning("No se pudo abrir el archivo en la aplicación predeterminada.");
    }
}
