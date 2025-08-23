// SUPERGATO
// Proyecto #2

// IC-3101 Arquitectura de Computadoras

// Instituto Tecnológico de Costa Rica
// Campus Tecnológico Central Cartago
// Escuela de Ingeniería en Computación
// II Semestre 2024
// Prof. M.Sc. Esteban Arias Méndez

// Efraim Cuevas Aguilar
// Carné: 2024109746
// Fabián Mata Salas
// Carné: 2024188730

#include <gtk/gtk.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
// #include <gdk-pixbuf/gdk-pixbuf.h>
// #include <gdk-pixbuf/gdk-pixbuf-loader.h>
// #include <gdk-pixbuf/gdk-pixbuf-utils.h>

#include "estructuras.h"

#define O_BIG "resources/O_grande.png"
#define O_SMALL "resources/O_small.png"
#define X_BIG "resources/X_grande.png"
#define X_SMALL "resources/X_small.png"

// Compilar con estos comandos
//  nasm -f elf -g -F dwarf ultimateGato.asm -o ultimateGato.o
// gcc ultimateGato.c ultimateGato.o -m32 -o ultimateGato $(pkg-config --cflags --libs gtk+-3.0) -rdynamic
//  ./ultimateGato

// gcc ultimateGato.c -o ultimateGato $(pkg-config --cflags --libs gtk+-3.0) -rdynamic

// gcc ultimateGato.c -o ultimateGato `pkg-config --cflags --libs gtk+-3.0`
// gcc -o ultimateGato ultimateGato.c `pkg-config --cflags --libs gtk+-3.0 gdk-pixbuf-2.0`
// gcc ultimateGato.c -o ultimateGato $(pkg-config --cflags --libs gtk+-3.0 gdk-pixbuf-2.0) -rdynamic

// Variables globales para manejar la interfaz gráfica
GtkBuilder *builder;
GtkWidget *window;
GtkWidget *fixed;

GtkWidget *reiniciarBtn;
GtkWidget *gamemodeSwitch;
GtkWidget *turnoDeLbl;
GtkWidget *turnoJugadorImg;
GtkWidget *cambiarTokenBtn;

GtkWidget *grid[9][3][3];  // Grid de botones
GtkWidget *megaGrid[3][3]; // Grid de imageWidgets para colocar los elementos grandes

// Variable del turno para saber cual label poner
//  O = 0            X = 1
// Siempre va a iniciar O
int turno = 0;
// Variable que verifica el modo de juego.
int botPlay = 0;
// Variables de puntaje
int pointP1 = 0;
int pointP2 = 0;
// FUNCIONES PARA LA INTERFAZ
void botClicked();
void init();
void createButtonGrid();
void createImageGrid();
void cellClicked(GtkWidget *, gpointer);
void changeCellIcon(GtkWidget *);
void highlightCell(struct Coordenada *);
void changeBigCellIcon(int);
void switchTurno();
void switchGamemode();
void reiniciarPartida();
void turnoBot();
void victory();
int xHandler(int);
int yHandler(int);

// FUNCIONES DE ENSAMBLADOR
extern void get_current_board(int*);
extern int check_empty_board();
extern int is_valid_move(int, int, int);
extern void make_move(int, int, int);
extern void next_turn();
extern void restart_game();
extern int check_board_victory(int);
extern int check_game_victory();
extern int check_board_tie(int);
extern void bot_move(int*, int*, int*);

// COLORES DEL CSS PARA LOS COLORES DEL BOTÓN
const char *css_default = "button { background-color: white; color: black; }";
const char *css_highlight = "button { background-color: #d3d3d3; color: black; }";

int main(int argc, char *argv[])
{

    gtk_init(&argc, &argv);

    init();

    // Iniciar el bucle principal de GTK
    gtk_main();
}

void init()
{
    builder = gtk_builder_new_from_file("ultimate_gato_screen.glade");
    window = GTK_WIDGET(gtk_builder_get_object(builder, "window"));
    fixed = GTK_WIDGET(gtk_builder_get_object(builder, "grid"));

    reiniciarBtn = GTK_WIDGET(gtk_builder_get_object(builder, "reiniciarBtn"));
    gamemodeSwitch = GTK_WIDGET(gtk_builder_get_object(builder, "gamemodeSwitch"));
    turnoDeLbl = GTK_WIDGET(gtk_builder_get_object(builder, "turnoDeLbl"));
    turnoJugadorImg = GTK_WIDGET(gtk_builder_get_object(builder, "turnoJugadorImg"));
    cambiarTokenBtn = GTK_WIDGET(gtk_builder_get_object(builder, "cambiarTokenBtn"));

    createButtonGrid();
    createImageGrid();

    // Conectar las señales definidas en el archivo .glade a las funciones correspondientes
    gtk_builder_connect_signals(builder, NULL);

    // Configuraciones de  juego
    // Mostrar al inicio que es el turno de O
    gtk_image_set_from_file(GTK_IMAGE(turnoJugadorImg), O_BIG);
    // Conectar el comando de reiniciar con el botón
    g_signal_connect(reiniciarBtn, "clicked", G_CALLBACK(reiniciarPartida), NULL);
    g_signal_connect(gamemodeSwitch, "notify::active", G_CALLBACK(switchGamemode), NULL);
    // Conectar el comando de cambiar el cambiarToken con el boton
    g_signal_connect(cambiarTokenBtn, "clicked", G_CALLBACK(switchTurno), NULL);

    // Mostrar la ventana
    gtk_widget_show_all(window);

    // Conectar la señal de cierre de la ventana al programa principal
    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
}

void createButtonGrid()
{
    GtkWidget *boton;
    struct Coordenada *coord;

    // Posiciones donde se van a ir poniendo los botónes
    int xInicial = 8;
    int yInicial = 3;
    int x = xInicial;
    int y = yInicial;

    for (int i = 0; i < 9; i++)
    {

        // Cuando cambian de fila hay que cambiar el Y
        if (i == 3)
        {
            xInicial = x = 8;
            yInicial = y += 212;
        }
        else if (i == 6)
        {
            xInicial = x = 8;
            yInicial = y += 208;
        }

        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 3; k++)
            {

                boton = gtk_button_new_with_label("");
                gtk_widget_set_size_request(boton, 50, 50);
                // TODO: Poner comandos del juego

                // Creo la coordenada para asignarle al boton en el comando
                coord = (struct Coordenada *)malloc(sizeof(struct Coordenada));
                coord->x = i;
                coord->y = j;
                coord->z = k;

                grid[i][j][k] = boton;
                g_signal_connect(grid[i][j][k], "clicked", G_CALLBACK(cellClicked), coord);
                gtk_fixed_put(GTK_FIXED(fixed), grid[i][j][k], x, y);

                x += 60;
            }
            x = xInicial;
            y += 60;
        }
        y = yInicial;
        xInicial = x += 207;
    }
}

void createImageGrid()
{
    GtkWidget *imagen;
    int x, y;
    for (int i = 0; i < 3; i++)
    {
        if (i == 0)
            y = 10;
        else if (i == 1)
            y = 223;
        else if (i == 2)
            y = 431;

        for (size_t j = 0; j < 3; j++)
        {
            if (j == 0)
                x = 11;
            else if (j == 1)
                x = 217;
            else if (j == 2)
                x = 423;

            // Crea una nueva imagen
            imagen = gtk_image_new();
            gtk_widget_set_size_request(imagen, 166, 160);
            megaGrid[i][j] = imagen;
            gtk_fixed_put(GTK_FIXED(fixed), megaGrid[i][j], x, y);
        }
    }
}

void cellClicked(GtkWidget *widget, gpointer data)
{   
    
    gtk_widget_set_sensitive(GTK_WIDGET(cambiarTokenBtn), FALSE);
    struct Coordenada *coord = (struct Coordenada *)data;

    // TODO: Implementar lógica del gato si es válido poner la casilla ahí
    if (is_valid_move(coord->x, coord->y, coord->z))
    {

        GtkWidget *boton = grid[coord->x][coord->y][coord->z];

        printf("Botón en la casilla (%d, %d, %d) presionado\n", coord->x, coord->y, coord->z);

        // Cambia el ícono del botón a la O o la X dependiendo del turno del jugador
        changeCellIcon(boton);
        // Le cambia el color al botón a uno gris
        highlightCell(coord);

        // Coloca la X o la O en la matriz (datos)
        make_move(coord->x, coord->y, coord->z);

        // Verificar si se hizo una línea de 3
        if (check_board_victory(coord->x))
        {
            changeBigCellIcon(coord->x);
        }
        else
        {
            // Si no hay una línea de 3, verifica si ese subtablero se llenó
            check_board_tie(coord->x);
        }

        // Verifica si el jugador ganó el juego
        if (check_game_victory())
        {
            victory();
            return;
        }

        switchTurno();


        if (botPlay)
        {
            turnoBot();
        }
    }
    else{
        printf("Botón en la casilla (%d, %d, %d) inavlida!\n", coord->x, coord->y, coord->z);
    }
}

void botClicked (GtkWidget *widget, gpointer data) {
    struct Coordenada *coord = (struct Coordenada *)data;

    if (is_valid_move(coord->x, coord->y, coord->z))
    {
        GtkWidget *boton = grid[coord->x][coord->y][coord->z];

        printf("Botón en la casilla (%d, %d, %d) presionado\n", coord->x, coord->y, coord->z);

        // Cambia el ícono del botón a la O o la X dependiendo del turno del jugador
        changeCellIcon(boton);
        // Le cambia el color al botón a uno gris
        highlightCell(coord);

        // Coloca la X o la O en la matriz (datos)
        make_move(coord->x, coord->y, coord->z);

        // Verificar si se hizo una línea de 3
        if (check_board_victory(coord->x))
        {
            changeBigCellIcon(coord->x);
        }
        else
        {
            // Si no hay una línea de 3, verifica si ese subtablero se llenó
            check_board_tie(coord->x);
        }

        // Verifica si el jugador ganó el juego
        if (check_game_victory())
        {
            victory();
            return;
        }

        switchTurno();
    }
    else{
        printf("Botón en la casilla (%d, %d, %d) inavlida!\n", coord->x, coord->y, coord->z);
    }
}
void changeCellIcon(GtkWidget *boton)
{
    GtkWidget *imagen;

    if (turno == 0)
    { // Si es turno de O
        imagen = gtk_image_new_from_file(O_SMALL);
    }
    else
    { // Si es turno de X
        imagen = gtk_image_new_from_file(X_SMALL);
    }

    if (imagen == NULL)
    {
        printf("Error al cargar la imagen.\n");
    }

    gtk_button_set_image(GTK_BUTTON(boton), imagen);
    gtk_widget_queue_draw(GTK_WIDGET(boton));
    gtk_widget_show(imagen); // Asegurarse de que la imagen sea visible
}

void changeButtonColor(GtkWidget *boton, const char *css)
{
    GtkCssProvider *provider = gtk_css_provider_new();
    GtkStyleContext *context = gtk_widget_get_style_context(boton);

    gtk_css_provider_load_from_data(provider, css, -1, NULL);
    gtk_style_context_add_provider(context, GTK_STYLE_PROVIDER(provider), GTK_STYLE_PROVIDER_PRIORITY_USER);

    g_object_unref(provider);
}

void highlightCell(struct Coordenada *coord)
{
    int x = coord->x;
    int y = coord->y;
    int z = coord->z;

    for (int i = 0; i < 9; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 3; k++)
            {
                GtkWidget *boton = grid[i][j][k];
                if (i == x && j == y && k == z)
                {
                    // Le cambia el color del botón a uno gris
                    changeButtonColor(boton, css_highlight);
                }
                else
                {
                    // Restablece el color predeterminado del boton
                    changeButtonColor(boton, css_default);
                }
            }
        }
    }
}

void switchTurno()
{

    // Cambia el turno X = 1 / O = 0
    turno = 1 - turno;
    // Cambia el turno en la lógica de ensamblador
    next_turn();

    if (turno == 0)
    {
        gtk_image_set_from_file(GTK_IMAGE(turnoJugadorImg), O_BIG);
    }
    else
    {
        gtk_image_set_from_file(GTK_IMAGE(turnoJugadorImg), X_BIG);
    }
}

// FUncion para cambiar el icono del jugador que inicia.
void switchStarter()
{
    if (check_empty_board())
    {
        next_turn();

        if (turno == 0)
        {
            gtk_image_set_from_file(GTK_IMAGE(turnoJugadorImg), O_BIG);
        }
        else
        {
            gtk_image_set_from_file(GTK_IMAGE(turnoJugadorImg), X_BIG);
        }
    }
    else
    {
        GtkWidget *dialog = gtk_dialog_new_with_buttons(
            "",
            GTK_WINDOW(window),
            GTK_DIALOG_MODAL | GTK_DIALOG_DESTROY_WITH_PARENT,
            "_OK",
            GTK_RESPONSE_OK,
            NULL);

        GtkWidget *content_area = gtk_dialog_get_content_area(GTK_DIALOG(dialog));
        GtkWidget *vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 10);
        gtk_box_set_homogeneous(GTK_BOX(vbox), FALSE);
        gtk_container_add(GTK_CONTAINER(content_area), vbox);

        // Creo el label con el mensaje"
        GtkWidget *label_title = gtk_label_new("NO SE PUEDE CAMBIAR ICONO");
        gtk_box_pack_start(GTK_BOX(vbox), label_title, TRUE, TRUE, 10);

        gtk_widget_show_all(dialog);

        // Esperar la confirmación del usuario y cerrar el dialog
        gtk_dialog_run(GTK_DIALOG(dialog));
        gtk_widget_destroy(dialog);
    }
}

void changeBigCellIcon(int gridSecundario)
{
    int i = gridSecundario % 3;
    int j = gridSecundario / 3;

    GtkWidget *imagen = megaGrid[j][i];

    if (turno == 0)
    {
        gtk_image_set_from_file(GTK_IMAGE(imagen), O_BIG);
    }
    else
    {
        gtk_image_set_from_file(GTK_IMAGE(imagen), X_BIG);
    }
}

void switchGamemode(GtkSwitch *switch_widget, GParamSpec *pspec, gpointer user_data)
{
gboolean state = gtk_switch_get_active(switch_widget);
    if (state) {
        g_print("Modo Singleplayer activado\n");
        botPlay = 1;
        reiniciarPartida();
    } else {
        g_print("Modo Multijugador activado\n");
        botPlay = 0;
        reiniciarPartida();
    }
}
void reiniciarPartida(){
    //Settea el turno inicial a O
    turno = 0;
    gtk_image_set_from_file(GTK_IMAGE(turnoJugadorImg), O_BIG);

    //Limpia el tablero de matrices en el ensamblador
    restart_game();

    //Borra las imágenes y rehabilita los botones
    for(int i=0; i<9; i++){
        for(int j=0; j<3; j++){
            for(int k=0; k<3; k++){
                gtk_button_set_image(GTK_BUTTON(grid[i][j][k]), NULL);
                gtk_widget_set_sensitive(GTK_WIDGET(grid[i][j][k]), TRUE); 
                changeButtonColor(grid[i][j][k], css_default);
            }
        }
    }

    
    for(int j=0; j<3; j++){
        for(int k=0; k<3; k++){
            gtk_image_set_from_file(GTK_IMAGE(megaGrid[j][k]), NULL);
        }
    }

    gtk_widget_set_sensitive(GTK_WIDGET(cambiarTokenBtn), TRUE);

    switchTurno();
}

void turnoBot()
{
    int x, y, z;
    GtkWidget *boton;
    struct Coordenada *coord;
    coord = (struct Coordenada *)malloc(sizeof(struct Coordenada));

    bot_move(&y,&z, &x);

    coord->x = x;
    coord->y = y;
    coord->z = z;

    boton = grid[coord->x][coord->y][coord->z];

    botClicked(boton, coord);
}

void victory()
{
// Crear un diálogo para mostrar quién ganó
    GtkWidget *dialog = gtk_dialog_new_with_buttons(
        "",
        GTK_WINDOW(window),
        GTK_DIALOG_MODAL | GTK_DIALOG_DESTROY_WITH_PARENT,
        "_OK",
        GTK_RESPONSE_OK,
        NULL);

    // Crear un contenedor para los widgets
    GtkWidget *content_area = gtk_dialog_get_content_area(GTK_DIALOG(dialog));
    GtkWidget *vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 10);
    gtk_box_set_homogeneous(GTK_BOX(vbox), FALSE);
    gtk_container_add(GTK_CONTAINER(content_area), vbox);

    // Incrementar el puntaje y establecer imagen del ganador
    GtkWidget *imagen = gtk_image_new();
    if (turno == 0)
    {   
        pointP1++;
        gtk_image_set_from_file(GTK_IMAGE(imagen), O_BIG);
    }
    else
    {
        pointP2++;
        gtk_image_set_from_file(GTK_IMAGE(imagen), X_BIG);
    }

    // Crear el label del ganador
    GtkWidget *label_title = gtk_label_new("¡GANADOR!");
    gtk_box_pack_start(GTK_BOX(vbox), label_title, TRUE, TRUE, 10);

    // Crear el label con las estadísticas de puntos
    char stats[50];
    snprintf(stats, sizeof(stats), "P1 Wins: %d | P2 Wins: %d", pointP1, pointP2);
    GtkWidget *label_stats = gtk_label_new(stats);
    gtk_box_pack_start(GTK_BOX(vbox), label_stats, TRUE, TRUE, 10);

    // Centrar la imagen del ganador
    gtk_widget_set_halign(imagen, GTK_ALIGN_CENTER);
    gtk_box_pack_start(GTK_BOX(vbox), imagen, TRUE, TRUE, 10);

    // Mostrar el dialog
    gtk_widget_show_all(dialog);

    // Esperar la confirmación del usuario y cerrar el dialog
    gtk_dialog_run(GTK_DIALOG(dialog));
    gtk_widget_destroy(dialog);

    // Deshabilitar todos los botones para que no puedan seguir jugando hasta que reinicien
    for (int i = 0; i < 9; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 3; k++)
            {
                gtk_widget_set_sensitive(GTK_WIDGET(grid[i][j][k]), FALSE);
            }
        }
    }
}