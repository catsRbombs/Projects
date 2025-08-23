#FABIAN MATA SALAS, GRUPO 4, TALLER DE PROGRAMACION, PROYECTO 3

########################################################################
#Bibliotecas
#########################################################################
import os
from tkinter import *
from tkinter import messagebox
from datetime import datetime
import pickle
#########################################################################
#Variables
#########################################################################

espaciosf = ""
preciof = ""
pagominf = ""
redondearf = ""
minsmaxf = ""
mon1 = ""
mon2 = ""
mon3 = ""
billete1f = ""
billete2f = ""
billete3f = ""
billete4f = ""
billete5f = ""
parqueo = []
historial = []
valoresmons = [[[0, 0],[0, 0],[0, 0]], [[0, 0],[0, 0],[0, 0]], [[0, 0],[0, 0],[0, 0]]] #Se guardan los datos dos veces porque el diccionario me estaba dando problemas cuando se cambiaba la configuracion de las monedas
valoresbilletes = [[[0, 0],[0, 0],[0, 0]], [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]], [[0, 0],[0, 0],[0, 0]],[[0, 0],[0, 0],[0, 0]]]
totmonedas = [0,0,0,0,0,0]
totbilletes = [0,0,0,0,0,0]
valores = {"monedas" : 
           {mon1 : {"entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]}, mon2 : { "entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]}, mon3 : { "entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]}  },
           "billetes" : {
               billete1f : {"entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]},
               billete2f : {"entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]},
               billete3f : {"entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]},
               billete4f : {"entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]},
               billete5f : {"entradas": [0, 0], "salidas" : [0, 0], "saldo" : [0, 0]} },
            "total_monedas" : [0,0,0,0,0,0], "total_billetes" : [0,0,0,0,0,0]   }

#########################################################################
#Funciones Generales
#########################################################################

def window_center(window, width, height): #Centra la ventana en la pantalla
    width_window = window.winfo_screenwidth()
    height_window = window.winfo_screenheight()
    x = (width_window // 2) - (width // 2)
    y = (height_window // 2) - (height // 2)
    window.geometry(f'{width}x{height}+{x}+{y}')

def cerrar_y_abrir_menu(ventana):
    ventana.destroy()
    abrirmenu()


#########################################################################
#Menu Principal - Ventana
#########################################################################
def abrirmenu():
    global valores
    valores = {"monedas": {
    "mon1": {
        "entradas": [valoresmons[0][0][0], valoresmons[0][0][1]],
        "salidas": [valoresmons[0][1][0], valoresmons[0][1][1]],
        "saldo": [valoresmons[0][2][0], valoresmons[0][2][1]]
    },
    "mon2": {
        "entradas": [valoresmons[1][0][0], valoresmons[1][0][1]],
        "salidas": [valoresmons[1][1][0], valoresmons[1][1][1]],
        "saldo": [valoresmons[1][2][0], valoresmons[1][2][1]]
    },
    "mon3": {
        "entradas": [valoresmons[2][0][0], valoresmons[2][0][1]],
        "salidas": [valoresmons[2][1][0], valoresmons[2][1][1]],
        "saldo": [valoresmons[2][2][0], valoresmons[2][2][1]]
    } }, 
    "billetes" : {
        "billete1f": {
            "entradas": [valoresbilletes[0][0][0], valoresbilletes[0][0][1]],
            "salidas": [valoresbilletes[0][1][0], valoresbilletes[0][1][1]],
            "saldo": [valoresbilletes[0][2][0], valoresbilletes[0][2][1]]
        },
        "billete2f": {
            "entradas": [valoresbilletes[1][0][0], valoresbilletes[1][0][1]],
            "salidas": [valoresbilletes[1][1][0], valoresbilletes[1][1][1]],
            "saldo": [valoresbilletes[1][2][0], valoresbilletes[1][2][1]]
        },
        "billete3f": {
            "entradas": [valoresbilletes[2][0][0], valoresbilletes[2][0][1]],
            "salidas": [valoresbilletes[2][1][0], valoresbilletes[2][1][1]],
            "saldo": [valoresbilletes[2][2][0], valoresbilletes[2][2][1]]
        },
        "billete4f": {
            "entradas": [valoresbilletes[3][0][0], valoresbilletes[3][0][1]],
            "salidas": [valoresbilletes[3][1][0], valoresbilletes[3][1][1]],
            "saldo": [valoresbilletes[3][2][0], valoresbilletes[3][2][1]]
        },
        "billete5f": {
            "entradas": [valoresbilletes[4][0][0], valoresbilletes[4][0][1]],
            "salidas": [valoresbilletes[4][1][0], valoresbilletes[4][1][1]],
            "saldo": [valoresbilletes[4][2][0], valoresbilletes[4][2][1]]
        }}, 
            "total_monedas" : totmonedas, "total_billetes" : totbilletes   }
    global menu
    menu = Tk()
    #Configuraciones
    window_center(menu,900,420)
    menu.geometry("900x420")
    menu.title('Menu') 
    canva = Canvas(menu)
    canva.pack(expand=True,fill=BOTH)
    label0=Label(
        canva,
        text = "Estacionamiento de Vehiculos",
        font = ("Helvetica", 20),
        fg = "black",
        justify = "center"
    )
    label0.place(relx = 0.5, y = 20, anchor = "center") #Centra el label en la parte de arriba del canvas

    #crear los botones
    buttonconfig = Button(menu, text="Configuracion", width=30, command=ventanaconfig)
    buttonconfig.place(relx=0.5, y=70, anchor = "center")  

    buttondinero = Button(menu, text="Dinero del cajero", width=30, command = lambda: (menu.destroy(), dinero_del_cajero()))
    buttondinero.place(relx=0.5, y=110, anchor = "center")

    buttonentrada = Button(menu, text="Entrada de vehículo", width=30, command = entrada_de_vehiculo)
    buttonentrada.place(relx=0.5, y=150, anchor = "center")

    buttoncajero = Button(menu, text="Cajero", width=30, command = cajero)
    buttoncajero.place(relx=0.5, y=190, anchor = "center")

    buttonsalida = Button(menu, text="Salida de vehiculo", width=30, command = salida_de_vehiculo)
    buttonsalida.place(relx=0.5, y=230, anchor = "center")

    buttonreporte = Button(menu, text="Reporte de ingresos de cajero", width=30)
    buttonreporte.place(relx=0.5, y=270, anchor = "center")

    buttonayuda = Button(menu, text="Ayuda", width=30, command = ayuda)
    buttonayuda.place(relx=0.5, y=310, anchor = "center")

    buttoninfo = Button(menu, text="Acerca de", width=30, command = acerca_de)
    buttoninfo.place(relx=0.5, y=350, anchor = "center")

    buttonexit = Button(menu, text="Salir", width=30, command = lambda : (guardar_cajero(valores), guardar_configuracion, guardar_historial_parqueo(historial), guardar_parqueo(parqueo), menu.destroy()))
    buttonexit.place(relx=0.5, y=390, anchor = "center")

    menu.mainloop() 
#########################################################################
#Configuracion
#########################################################################

def ventanaconfig():

    if parqueo:
        messagebox.showinfo("Error", "No se puede cambiar la configuracion mientras hayan vehiculos parqueados")
        return

    menu.destroy()

    global config_window
    config_window = Tk()

    #Variables
    espacios = StringVar() #StringVar es una clase de tkinter que facilita la manipulacion de informacion con entry
    precio = StringVar()
    pagominimo = StringVar()
    redondear = StringVar()
    minutosmax = StringVar()
    moneda1 = StringVar()
    moneda2 = StringVar()
    moneda3 = StringVar()
    billete1 = StringVar()
    billete2 = StringVar()
    billete3 = StringVar()
    billete4 = StringVar()
    billete5 = StringVar()

    config_window.title("Configuracion del programa")
    window_center(config_window,600,650)
    config_window.geometry("600x650")

    Label(config_window, text="Cantidad de espacios en el parque (entero >=1)").place(x=20, y=20)
    Entry(config_window, textvariable=espacios).place(x=400, y=20)

    Label(config_window, text="Precio por hora (flotante con máximo 2 decimales >=0)").place(x=20, y=60)
    Entry(config_window, textvariable=precio ).place(x=400, y=60)

    Label(config_window, text="Pago mínimo (entero >=0)").place(x=20, y=100)
    Entry(config_window, textvariable = pagominimo).place(x=400, y=100)

    Label(config_window, text="Redondear el tiempo cobrado al próximo minuto (entero entre 0 y 60)").place(x=20, y=140)
    Entry(config_window, textvariable = redondear).place(x=400, y=140)

    Label(config_window, text="Minutos máximos para salir después del pago (entero >=0)").place(x=20, y=180)
    Entry(config_window, textvariable = minutosmax).place(x=400, y=180)

    Label(config_window, text="Tipos de moneda (máximo 3 tipos, enteros >=0)").place(x=20, y=220)
    Label(config_window, text="Moneda 1, la de menor denominación (ejemplo 50)").place(x=40, y=260)
    Entry(config_window, textvariable = moneda1).place(x=400, y=260)
    Label(config_window, text="Moneda 2, denominación siguiente a la anterior (ejemplo 100)").place(x=40, y=300)
    Entry(config_window, textvariable = moneda2).place(x=400, y=300)
    Label(config_window, text="Moneda 3, denominación siguiente a la anterior (ejemplo 500)").place(x=40, y=340)
    Entry(config_window, textvariable = moneda3).place(x=400, y=340)

    Label(config_window, text="Tipos de billetes (máximo 5 tipos, enteros >=0)").place(x=20, y=380)
    Label(config_window, text="Billete 1, el de menor denominación (ejemplo 1000)").place(x=40, y=420)
    Entry(config_window, textvariable = billete1).place(x=400, y=420)
    Label(config_window, text="Billete 2, denominación siguiente a la anterior (ejemplo 2000)").place(x=40, y=460)
    Entry(config_window, textvariable = billete2).place(x=400, y=460)
    Label(config_window, text="Billete 3, denominación siguiente a la anterior (ejemplo 5000)").place(x=40, y=500)
    Entry(config_window, textvariable = billete3).place(x=400, y=500)
    Label(config_window, text="Billete 4, denominación siguiente a la anterior (ejemplo 10000)").place(x=40, y=540)
    Entry(config_window, textvariable = billete4).place(x=400, y=540)
    Label(config_window, text="Billete 5, denominación siguiente a la anterior (ejemplo 20000)").place(x=40, y=580)
    Entry(config_window, textvariable = billete5).place(x=400, y=580)

    #Validaciones
    def validar_entero(valor, valorminimo=1):
        if valor.isdigit() and int(valor) >= valorminimo:
            return True
        else:
            return False
    
    def validar_flotante(valor):
        try:
            valor = float(valor)
            return valor >= 0 and (valor*100).is_integer()
        except ValueError:
            return False 
        

    def se_puede_hacer_cambio(monto, monedas):
        """Check if we can make the given monto using the provided denominations."""
        dp = [False] * (monto + 1)  # Crear una lista de falsos con la cantidad del monto + 1
        dp[0] = True  # El 0 es True porque se puede lograr sin monedas

        for moneda in monedas:
            for x in range(moneda, monto + 1):
                if dp[x - moneda]:  # Si se puede lograr el monto x - moneda, entonces se marca como verdadero
                    dp[x] = True 

        return dp[monto] 
    
    def validar_y_guardar():
        
        listo = True
        errores = []

        if not validar_entero(espacios.get()):
            errores.append("La cantidad de espacios debe ser un entero mayor o igual a 1")
        
        if not validar_flotante(precio.get()):
            errores.append("El precio debe ser un flotante mayor o igual a 0 con dos decimales")
        
        if not validar_entero(pagominimo.get(),0):
            errores.append("El pago mínimo debe ser un entero mayor o igual a 0")
        
        if not validar_entero(redondear.get(),0) or int(redondear.get()) > 60 or int(redondear.get()) < 0: 
            errores.append("El tiempo a redondear debe ser un entero entre 0 y 60")
        
        if not validar_entero(minutosmax.get(),0):
            errores.append("Los minutos máximos para salir después del pago debe ser un entero mayor o igual a 0")
        
        try:
             listo = True
             monedas = [int(moneda1.get()), int(moneda2.get()), int(moneda3.get())]
             monedas.sort()
             if int(monedas[0]) == 0:
                 if int(monedas[1]) == 0:
                     if int(monedas[2]) == 0:
                         pass
                     else:
                         if listo:
                             errores.append("Cuando una moneda sea 0, las siguientes deben ser 0")
                             listo = False
                 else:
                     errores.append("Cuando una moneda sea 0, las siguientes deben ser 0")
                     listo = False
             if int(moneda1.get()) != monedas[0] or int(moneda2.get()) != monedas[1] or int(moneda3.get()) != monedas[2]:
                 if listo:
                     errores.append("Las denominaciones de las monedas deben ser mayores a las anteriores")
        except:
            if listo:
                errores.append("Las monedas deben ser enteros")

        try:
            listo = True
            billetes = [int(billete1.get()), int(billete2.get()), int(billete3.get()), int(billete4.get()), int(billete5.get())]
            billetes.sort()
            if int(billetes[0]) == 0:
                if int(billetes[1]) == 0:
                    if int(billetes[2]) == 0:
                        if int(billetes[3]) == 0:
                            pass
                        else:
                            if listo:
                                errores.append("Cuando un billete sea 0, las siguientes deben ser 0")
                                listo = False
                    else:
                        errores.append("Cuando un billete sea 0, las siguientes deben ser 0")
                        listo = False
            if int(billete1.get()) != billetes[0] or int(billete2.get()) != billetes[1] or int(billete3.get()) != billetes[2] or int(billete4.get()) != billetes[3] or int(billete5.get()) != billetes[4]:
                if listo:
                    errores.append("Las denominaciones de los billetes deben ser mayores a las anteriores")
        except: 
            errores.append("Billetes deben ser enteros")

        if se_puede_hacer_cambio(int(pagominimo.get()), monedas) == False and se_puede_hacer_cambio(int(pagominimo.get()), billetes) == False:
            errores.append("El pago minimo debe ser capaz de ser logrado con alguna combinacion de monedas y billetes")
        
        try:
            if se_puede_hacer_cambio(int(precio.get())+int(pagominimo.get()), monedas) == False and se_puede_hacer_cambio(int(precio.get())+int(pagominimo.get()), billetes) == False:
               errores.append("El precio por hora, iniciando desde el precio minimo,  debe ser capaz de ser logrado con alguna combinacion de monedas y billetes")
        except:
            errores.append("El precio por hora siempre debe ser capaz de ser logrado con alguna combinacion de monedas y billetes")
      
        if errores:
            messagebox.showerror("Errores de validacion:", "\n".join(errores)) #Imprime cada error en una linea distinta
        else:
            global espaciosf
            global preciof
            global pagominf
            global redondearf
            global minsmaxf
            global mon1
            global mon2
            global mon3
            global billete1f
            global billete2f
            global billete3f
            global billete4f
            global billete5f
            espaciosf = espacios.get()
            preciof = precio.get()
            pagominf = pagominimo.get()
            redondearf = redondear.get()
            minsmaxf = minutosmax.get()
            mon1 = moneda1.get()
            mon2 = moneda2.get()
            mon3 = moneda3.get()
            billete1f = billete1.get()
            billete2f = billete2.get()
            billete3f = billete3.get()
            billete4f = billete4.get()
            billete5f = billete5.get()

            parqueo = []

            for i in range(int(espaciosf)):
                parqueo.append([])
            
            cerrar_y_abrir_menu(config_window)

    # Boton de ok y cancelar 
    Button(config_window, text="Ok", command=validar_y_guardar).place(x=200, y=620)
    Button(config_window, text="Cancelar", command = lambda: (config_window.destroy(), abrirmenu())).place(x=300, y=620) #Se usa lambda para que se cierre la ventana y luego se abra la otra secuencialmente
    config_window.protocol("WM_DELETE_WINDOW", lambda: (config_window.destroy(), abrirmenu()))

####################################################################################
#Dinero del cajero
####################################################################################
def dinero_del_cajero():
    global valores #Actualizar valores en vivo
    valores = {"monedas": {
    mon1: {
        "entradas": [valoresmons[0][0][0], valoresmons[0][0][1]],
        "salidas": [valoresmons[0][1][0], valoresmons[0][1][1]],
        "saldo": [valoresmons[0][2][0], valoresmons[0][2][1]]
    },
    mon2: {
        "entradas": [valoresmons[1][0][0], valoresmons[1][0][1]],
        "salidas": [valoresmons[1][1][0], valoresmons[1][1][1]],
        "saldo": [valoresmons[1][2][0], valoresmons[1][2][1]]
    },
    mon3: {
        "entradas": [valoresmons[2][0][0], valoresmons[2][0][1]],
        "salidas": [valoresmons[2][1][0], valoresmons[2][1][1]],
        "saldo": [valoresmons[2][2][0], valoresmons[2][2][1]]
    } }, 
    "billetes" : {
        billete1f: {
            "entradas": [valoresbilletes[0][0][0], valoresbilletes[0][0][1]],
            "salidas": [valoresbilletes[0][1][0], valoresbilletes[0][1][1]],
            "saldo": [valoresbilletes[0][2][0], valoresbilletes[0][2][1]]
        },
        billete2f: {
            "entradas": [valoresbilletes[1][0][0], valoresbilletes[1][0][1]],
            "salidas": [valoresbilletes[1][1][0], valoresbilletes[1][1][1]],
            "saldo": [valoresbilletes[1][2][0], valoresbilletes[1][2][1]]
        },
        billete3f: {
            "entradas": [valoresbilletes[2][0][0], valoresbilletes[2][0][1]],
            "salidas": [valoresbilletes[2][1][0], valoresbilletes[2][1][1]],
            "saldo": [valoresbilletes[2][2][0], valoresbilletes[2][2][1]]
        },
        billete4f: {
            "entradas": [valoresbilletes[3][0][0], valoresbilletes[3][0][1]],
            "salidas": [valoresbilletes[3][1][0], valoresbilletes[3][1][1]],
            "saldo": [valoresbilletes[3][2][0], valoresbilletes[3][2][1]]
        },
        billete5f: {
            "entradas": [valoresbilletes[4][0][0], valoresbilletes[4][0][1]],
            "salidas": [valoresbilletes[4][1][0], valoresbilletes[4][1][1]],
            "saldo": [valoresbilletes[4][2][0], valoresbilletes[4][2][1]]
        }}, 
            "total_monedas" : [0,0,0,0,0,0], "total_billetes" : [0,0,0,0,0,0]   }    
    global submenu
    submenu = Tk()
    #Configuraciones
    window_center(submenu,400,200)
    submenu.geometry("400x200")
    submenu.title('Dinero del cajero') 

    #crear los botones
    buttonsaldo = Button(submenu, text="Saldo del cajero", width=30, command = lambda: (submenu.destroy(), saldo_del_cajero()))
    buttonsaldo.place(relx=0.5, y=70, anchor = "center")  

    buttoncargar = Button(submenu, text="Cargar el cajero", command = cargar_el_cajero)
    buttoncargar.place(relx=0.5, y=110, anchor = "center")

    buttoncargar = Button(submenu, text="Cancelar", command = lambda: (submenu.destroy(), abrirmenu()))
    buttoncargar.place(relx=0.5, y=150, anchor = "center")   
    
    submenu.protocol("WM_DELETE_WINDOW", lambda: (submenu.destroy(),abrirmenu()))

def saldo_del_cajero():
    global saldowindow
    saldowindow = Tk()
    window_center(saldowindow, 900, 500)
    saldowindow.title("Estacionamiento - Saldo del Cajero")
    saldowindow.geometry("900x500")

    def reiniciar_valor():
        if reset_var.get() == 1:
            for categoria in ["monedas", "billetes"]:
                for denominacion in valores[categoria]:
                    valores[categoria][denominacion]["entradas"][0] = 0
                    valores[categoria][denominacion]["entradas"][1] = 0
                    valores[categoria][denominacion]["salidas"][0] = 0
                    valores[categoria][denominacion]["salidas"][1] = 0
                    valores[categoria][denominacion]["saldo"][0] = 0
                    valores[categoria][denominacion]["saldo"][1] = 0
            valores["total_billetes"][0] = 0
            valores["total_billetes"][1] = 0
            valores["total_billetes"][2] = 0
            valores["total_billetes"][3] = 0     
            valores["total_billetes"][4] = 0     
            valores["total_billetes"][5] = 0            
            valores["total_monedas"][0] = 0
            valores["total_monedas"][1] = 0
            valores["total_monedas"][2] = 0
            valores["total_monedas"][3] = 0
            valores["total_monedas"][4] = 0
            valores["total_monedas"][5] = 0

    def en_ok():
        reiniciar_valor()
        saldowindow.destroy()
        dinero_del_cajero()

    title_label = Label(saldowindow, text="ESTACIONAMIENTO - SALDO DEL CAJERO", font=("Helvetica", 12))
    title_label.place(x=300, y=10)    

    titulos = ["", "ENTRADAS", "", "SALIDAS", "", "SALDO"]
    subtitulos = ["DENOMINACION", "CANTIDAD", "TOTAL", "CANTIDAD", "TOTAL", "CANTIDAD", "TOTAL"]
    for i, header in enumerate(titulos):
        Label(saldowindow, text=header, font=("Helvetica", 10)).place(x=20 + i * 120, y=50)
    for i, sub_header in enumerate(subtitulos):
        Label(saldowindow, text=sub_header, font=("Helvetica", 10)).place(x=20 + i * 120, y=70)

    posicion = 90
    for categoria in ["monedas", "billetes"]:
        for denominacion in valores[categoria]:
            Label(saldowindow, text=f"{categoria.capitalize()} de {denominacion}").place(x=20, y=posicion)
            Label(saldowindow, text=valores[categoria][denominacion]["entradas"][0]).place(x=140, y=posicion)
            Label(saldowindow, text=valores[categoria][denominacion]["entradas"][1]).place(x=260, y=posicion)
            Label(saldowindow, text=valores[categoria][denominacion]["salidas"][0]).place(x=380, y=posicion)
            Label(saldowindow, text=valores[categoria][denominacion]["salidas"][1]).place(x=500, y=posicion)
            Label(saldowindow, text=valores[categoria][denominacion]["saldo"][0]).place(x=620, y=posicion)
            Label(saldowindow, text=valores[categoria][denominacion]["saldo"][1]).place(x=740, y=posicion)
            posicion += 30
    Label(saldowindow, text="TOTAL DE MONEDAS").place(x=20, y=posicion)
    for i, value in enumerate(valores["total_monedas"]):
        Label(saldowindow, text=value).place(x=140 + i * 120, y=posicion)
    posicion += 30
    Label(saldowindow, text="TOTAL DE BILLETES").place(x=20, y=posicion)
    for i, value in enumerate(valores["total_billetes"]):
        Label(saldowindow, text=value).place(x=140 + i * 120, y=posicion)

    reset_var = IntVar()
    reset_checkbox = Checkbutton(saldowindow, text="Vaciar cajero", variable=reset_var)
    reset_checkbox.place(x=20, y=posicion + 50)

    ok_button = Button(saldowindow, text="Ok", command=en_ok)
    ok_button.place(x=300, y=posicion + 100)
    cancel_button = Button(saldowindow, text="Cancelar", command=lambda: (saldowindow.destroy(), dinero_del_cajero()))
    cancel_button.place(x=400, y=posicion + 100)

def cargar_el_cajero():

    #Me acabo de enterar que existe la opcion .withdraw 
    submenu.withdraw() 

    def cerrar_opcion():
        submenu.deiconify()
        cargar_cajero_window.destroy()
    
    cargar_cajero_window = Toplevel(submenu)
    cargar_cajero_window.title("Estacionamiento – Cargar Cajero")
    cargar_cajero_window.geometry("750x450")
    cargar_cajero_window.protocol("WM_DELETE_WINDOW", cerrar_opcion)

    def actualizar_valores():

        monedas_totales = []
        billetes_totales = []

        for i in range(3):
            cantidad = int(monedas_entries[i].get() or 0)
            total = denominaciones_monedas[i] * cantidad
            monedas_totales.append(total)
            valores["monedas"][f"mon{i+1}"]["entradas"][0] = cantidad
            valores["monedas"][f"mon{i+1}"]["saldo"][0] += cantidad
            valores["monedas"][f"mon{i+1}"]["saldo"][1] = total
            monedas_totales_entries[i].config(state="normal")
            monedas_totales_entries[i].delete(0, END)
            monedas_totales_entries[i].insert(0, total)
            monedas_totales_entries[i].config(state="readonly")

        for i in range(5):
            cantidad = int(billetes_entries[i].get() or 0)
            total = denominaciones_billetes[i] * cantidad
            billetes_totales.append(total)
            # Update the dictionary
            valores["billetes"][f"billete{i+1}f"]["entradas"][0] = cantidad
            valores["billetes"][f"billete{i+1}f"]["saldo"][0] += cantidad
            valores["billetes"][f"billete{i+1}f"]["saldo"][1] = total
            billetes_totales_entries[i].config(state="normal")
            billetes_totales_entries[i].delete(0, END)
            billetes_totales_entries[i].insert(0, total)
            billetes_totales_entries[i].config(state="readonly")
        
        total_monedas.set(sum(monedas_totales))
        total_billetes.set(sum(billetes_totales))
        total_cajero.set(total_monedas.get() + total_billetes.get())

    denominaciones_monedas = [50, 100, 500]
    monedas_labels = ["Monedas de 50", "Monedas de 100", "Monedas de 500"]
    monedas_entries = []
    monedas_totales_entries = []

    for i in range(3):
        Label(cargar_cajero_window, text=monedas_labels[i]).place(x=10, y=30 + i*30)
        entrada = Entry(cargar_cajero_window)
        entrada.place(x=150, y=30 + i*30)
        monedas_entries.append(entrada)
        total_entrada = Entry(cargar_cajero_window, state="readonly")
        total_entrada.place(x=300, y=30 + i*30)
        monedas_totales_entries.append(total_entrada)

    denominaciones_billetes = [1000, 2000, 5000, 10000, 20000]
    billetes_labels = ["Billetes de 1000", "Billetes de 2000", "Billetes de 5000", "Billetes de 10000", "Billetes de 20000"]
    billetes_entries = []
    billetes_totales_entries = []
    for i in range(5):
        Label(cargar_cajero_window, text=billetes_labels[i]).place(x=10, y=150 + i*30)
        entrada = Entry(cargar_cajero_window)
        entrada.place(x=150, y=150 + i*30)
        billetes_entries.append(entrada)
        total_entrada = Entry(cargar_cajero_window, state="readonly")
        total_entrada.place(x=300, y=150 + i*30)
        billetes_totales_entries.append(total_entrada)
    
    total_monedas = IntVar()
    total_billetes = IntVar()
    total_cajero = IntVar()

    Label(cargar_cajero_window, text="TOTAL DE MONEDAS").place(x=10, y=120)
    Entry(cargar_cajero_window, textvariable=total_monedas, state="readonly").place(x=300, y=120)
    Label(cargar_cajero_window, text="TOTAL DE BILLETES").place(x=10, y=300)
    Entry(cargar_cajero_window, textvariable=total_billetes, state="readonly").place(x=300, y=300)
    Label(cargar_cajero_window, text="TOTAL DEL CAJERO").place(x=10, y=350)
    Entry(cargar_cajero_window, textvariable=total_cajero, state="readonly").place(x=300, y=350)
    Button(cargar_cajero_window, text="Ok", command=lambda: [actualizar_valores(), cerrar_opcion()]).place(x=200, y=400)
    Button(cargar_cajero_window, text="Cancelar", command=cerrar_opcion).place(x=300, y=400)

#######################################################################################
#Entrada de vehiculo
#######################################################################################
def entrada_de_vehiculo():

    global espaciosf, parqueo

    espacios_disponibles = int(espaciosf)
    ya = True
    espacio_asignado = 0

    for i in range(len(parqueo)):
        if parqueo[i] != []:
            espacios_disponibles -= 1
        if parqueo[i] == []:
            if ya:
                espacio_asignado = i+1
                ya = False
    
    if espacios_disponibles == 0:
        messagebox.showerror("ERROR", "No quedan espacios disponibles en el estacionamiento.")
        return

    menu.withdraw()
    entrada_window = Toplevel(menu)
    entrada_window.title("ESTACIONAMIENTO - ENTRADA DE VEHÍCULO")
    entrada_window.geometry("500x400")
    window_center(entrada_window,500,400)

    Label(entrada_window, text = f"Espacios disponibles: {espacios_disponibles}").place(x=20, y=20)

    Label(entrada_window, text = "SU PLACA").place(x=20, y=60)
    placa = StringVar()
    Entry(entrada_window, textvariable=placa, width = 20).place(x=150, y=60)

    Label(entrada_window, text = f"Espacio asignado: {espacio_asignado}").place(x=20, y=100)
    hora_actual = datetime.now().strftime("%H:%M")
    dia_actual = datetime.now().strftime("%d/%m/%Y")
    Label(entrada_window, text = f"Hora de entrada: {hora_actual}, {dia_actual}").place(x=20, y=140)

    Label(entrada_window, text = f"Precio por hora: {preciof}").place(x=20,y=220)

    Label(entrada_window, text = f"Pago minimo: {pagominf}").place(x=20, y=260)

    #validaciones
    def validar():
        errores = []
        placav = placa.get()
        if len(placav) > 8 or len(placav) <= 0:
            errores.append("La placa debe tener entre 1 y 8 caracteres")
        for datos in parqueo:
            if placav in datos:
                errores.append("La placa ya esta registrada")
                break
        if errores:
            messagebox.showerror("ERROR", "\n".join(errores))
        else:
            for i in range(int(espaciosf)):
                if parqueo[i] == []:
                    parqueo[i] = ([placav, dia_actual, hora_actual, 0, 0, 0])
                    entrada_window.destroy()
                    menu.deiconify()
                    break
    
    def cerrar_menu():
        entrada_window.destroy()
        menu.deiconify()

    Button(entrada_window, text="Ok", command=validar).place(x=150, y=300)
    Button(entrada_window, text="Cancelar", command=cerrar_menu).place(x=250, y=300)
    entrada_window.protocol("WM_DELETE_WINDOW", cerrar_menu)

def cajero():
    global menu
    menu.withdraw()

    cajero_window = Toplevel(menu)
    cajero_window.title("INGRESE SU PLACA")
    cajero_window.geometry("80x100")
    window_center(cajero_window,80,100)

    placa = StringVar()
    
    def verificar_placa():
        try:
            placa1 = placa.get()
            for i in range(len(parqueo)):
                if placa1 == parqueo[i][0]:
                    cajero_window.destroy()
                    cajero_avanzar(parqueo[i])
                    break
                else:
                    if i == len(parqueo) - 1:
                        messagebox.showerror("ERROR", "La placa ingresada no esta registrada")
        except: 
            messagebox.showerror("ERROR", "La placa ingresada no es valida")


    Label(cajero_window, text="INGRESE SU PLACA").place(x=00,y=10)

    Entry(cajero_window, textvariable=placa, width=20).place(x=0,y=30)

    Button(cajero_window, text="Ok", command = verificar_placa).place(x=0,y=70)

    Button(cajero_window, text="Cancelar", command = lambda: (cajero_window.destroy(), menu.deiconify())).place(x=40,y=70)

    cajero_window.protocol("WM_DELETE_WINDOW", lambda: (cajero_window.destroy(), menu.deiconify()))

def cajero_avanzar(info):
    
    global preciof, pagominf, mon1, mon2, mon3, billete1f, billete2f, billete3f, billete4f, billete5f

    listo = False

    paga_acumulada = 0
    def mastotal(entrada):
        nonlocal paga_acumulada, listo
        if paga_acumulada >= int(tot_pagar):
            listo = True
            Label(cajero_window, text=f"{paga_acumulada}", bg="green", fg="white", font=("Helvetica", 16)).place(x=400, y=140, width=150, height=50)  
            Label(cajero_window, text=f"0", bg="red", fg="white", font=("Helvetica", 16)).place(x=400, y=50, width=150, height=50)                  
            pass
        else:
            if (int(tot_pagar)-paga_acumulada) < 0:
                paga_acumulada += int(entrada)            
                Label(cajero_window, text=f"{0}", bg="red", fg="white", font=("Helvetica", 16)).place(x=400, y=50, width=150, height=50)
                Label(cajero_window, text=f"{paga_acumulada}", bg="green", fg="white", font=("Helvetica", 16)).place(x=400, y=140, width=150, height=50)                          
            else:
                paga_acumulada += int(entrada)            
                Label(cajero_window, text=f"{int(tot_pagar)-paga_acumulada}", bg="red", fg="white", font=("Helvetica", 16)).place(x=400, y=50, width=150, height=50)
                Label(cajero_window, text=f"{paga_acumulada}", bg="green", fg="white", font=("Helvetica", 16)).place(x=400, y=140, width=150, height=50)          

    
    placa = info[0]
    hora_entrada = info[2]
    dia_entrada = info[1]
    
    cajero_window = Toplevel(menu)
    cajero_window.title("ESTACIONAMIENTO - CAJERO")
    cajero_window.geometry("600x600")
    window_center(cajero_window,600,600)

    Label(cajero_window, text=f"Paso 1: SU PLACA: {placa}").place(x=20, y=20)

    Label(cajero_window, text=f"HORA DE ENTRADA {hora_entrada, dia_entrada}").place(x=20, y=60)

    hora_salida = datetime.now().strftime("%H:%M")
    dia_salida = datetime.now().strftime("%d/%m/%Y")

    #Determinar diferencia de horas al calcular la cantidad de diferencia de dias
    dif_yyyy = int(dia_salida[6:]) - int(dia_entrada[6:])
    dif_mm = int(dia_salida[3:5]) - int(dia_entrada[3:5])
    dif_dd = int(dia_salida[0:2]) - int(dia_entrada[0:2])
    dif_hh = abs(int(hora_salida[0:2]) - int(hora_entrada[0:2]))

    tot_horas = dif_hh + dif_dd * 24 + dif_mm * 730 + dif_yyyy * 8760
    tot_pagar = tot_horas * preciof
    if tot_pagar < pagominf:
        tot_pagar = pagominf

    Label(cajero_window, text=f"HORA DE SALIDA {hora_salida, dia_salida}").place(x=20, y=100)

    Label(cajero_window, text="TIEMPO COBRADO").place(x=20, y=140)
    Label(cajero_window, text=f"{dif_hh} h {dif_dd} d {dif_mm} m {dif_yyyy} y").place(x=150, y=140)

    Label(cajero_window, text="A PAGAR").place(x=400, y=20)
    Label(cajero_window, text=f"{tot_pagar}", bg="red", fg="white", font=("Helvetica", 16)).place(x=400, y=50, width=150, height=50)

    Label(cajero_window, text="Pago").place(x=400, y=110)
    Label(cajero_window, text="XXXXXX", bg="green", fg="white", font=("Helvetica", 16)).place(x=400, y=140, width=150, height=50)

    Label(cajero_window, text="Cambio").place(x=400, y=200)

    # Step 2: Payment in Coins and Bills
    Label(cajero_window, text="Paso 2: SU PAGO EN: MONEDAS").place(x=20, y=200)
    
    coin_denominations = [mon1, mon2, mon3]
    bill_denominations = [billete1f, billete2f, billete3f, billete4f, billete5f]
    
    coin_y_offset = 230
    for coin in coin_denominations:
        if int(coin) == 0:
            continue
        Button(cajero_window, text=str(coin),command = lambda coin = coin:  (mastotal(int(coin)))).place(x=20, y=coin_y_offset, width=50, height=30)
        coin_y_offset += 40

    Label(cajero_window, text="BILLETES").place(x=150, y=200)

    bill_y_offset = 230
    for bill in bill_denominations:
        if int(bill) == 0:
            continue
        Button(cajero_window, text=str(bill), command = lambda bill = bill: (mastotal(int(bill)))).place(x=150, y=bill_y_offset, width=60, height=30)
        bill_y_offset += 40

    tarjeta_de_credito = StringVar()

    Label(cajero_window, text="TARJETA DE CRÉDITO").place(x=300, y=200)
    Entry(cajero_window, textvariable = tarjeta_de_credito).place(x=300, y=230, width=200)

    if tarjeta_de_credito.get():
        mastotal(int(tot_pagar)-paga_acumulada)
    
    Label(cajero_window, text="Paso 3: SU CAMBIO EN:").place(x=20, y=350)
    
    Label(cajero_window, text="MONEDAS").place(x=20, y=380)
    Label(cajero_window, text=f"XX DE {mon1}").place(x=20, y=410)
    Label(cajero_window, text=f"XX DE {mon2}").place(x=20, y=440)
    Label(cajero_window, text=f"XX DE {mon3}").place(x=20, y=470)

    Label(cajero_window, text="BILLETES").place(x=200, y=380)
    Label(cajero_window, text=f"XX DE {billete1f}").place(x=200, y=410)
    Label(cajero_window, text=f"XX DE {billete2f}").place(x=200, y=440)
    Label(cajero_window, text=f"XX DE {billete3f}").place(x=200, y=470)
    Label(cajero_window, text=f"XX DE {billete4f}").place(x=200, y=500)
    Label(cajero_window, text=f"XX DE {billete5f}").place(x=200, y=530)
    
    def anular_pago():
        if not listo:
            cajero_window.destroy()
            cajero_avanzar(info)
    
    def terminar_pago():
        if listo:
            for i in range(len(parqueo)):
                if parqueo[i] == info:
                    parqueo[i] == [info[0], info[1],info[2], dia_salida, hora_salida, tot_pagar]
            cajero_window.destroy()
            menu.deiconify()
        

    Button(cajero_window, text="Anular el pago", command= anular_pago).place(x=300, y=450, width=100, height=30)
    Button(cajero_window, text="Ok", command=terminar_pago ).place(x=350, y=450, width=100, height=30)
    cajero_window.protocol("WM_DELETE_WINDOW", lambda : (cajero_window.destroy(), menu.deiconify))

##############################################################################
#Salir vehiculo
##############################################################################
def salida_de_vehiculo():
    global menu, minsmaxf
    menu.withdraw()

    salida_window = Toplevel(menu)
    salida_window.title("INGRESE SU PLACA")
    salida_window.geometry("80x100")
    window_center(salida_window,80,100)

    placa = StringVar()
    hora_actual = datetime.now().strftime("%H:%M")
    dia_actual = datetime.now().strftime("%d/%m/%Y")    
    def verificar_placa():
        try:
            placa1 = placa.get()
            for i in range(len(parqueo)):
                if parqueo[i] == []:
                    continue
                if placa1 == parqueo[i][0]:
                    if parqueo[i][3] == 0:
                        messagebox.showerror("Error", "Esta placa aun no ha pagado")
                        salida_window.destroy()
                        menu.deiconify()
                        break
                    if parqueo[i][3][2:] - hora_actual[2:] > minsmaxf:
                        messagebox.showerror("ERROR", f"El tiempo de parqueo ha expirado. \n Tiempos maximo {minsmaxf} \n Tiempo que usted tardo {parqueo[i][0][2:] - hora_actual[2:]}")
                        historial.append(parqueo[i])
                        parqueo[i] = [parqueo[i][0], hora_actual, dia_actual, 0, 0, 0]
                        salida_window.destroy()
                        menu.deiconify()
                        break
                    else:
                        if parqueo[i][5]:
                            break
                        else:
                            messagebox.showerror("Error", "Esta placa aun no ha pagado")
                            salida_window.destroy()
                            menu.deiconify()
                            break
                else:
                    if i == len(parqueo) - 1:
                        messagebox.showerror("ERROR", "La placa ingresada no esta registrada")
        except ZeroDivisionError: 
            messagebox.showerror("ERROR", "La placa ingresada no es valida")


    Label(salida_window, text="INGRESE SU PLACA").place(x=00,y=10)

    Entry(salida_window, textvariable=placa, width=20).place(x=0,y=30)

    Button(salida_window, text="Ok", command = verificar_placa).place(x=0,y=70)

    Button(salida_window, text="Cancelar", command = lambda: (salida_window.destroy(), menu.deiconify())).place(x=40,y=70)

    salida_window.protocol("WM_DELETE_WINDOW", lambda: (salida_window.destroy(), menu.deiconify()))

def leer_configuracion():
    global espaciosf, preciof, pagominf, redondearf, minsmaxf, mon1, mon2, mon3, billete1f, billete2f, billete3f, billete4f, billete5f
    try:
        with open('configuracion.dat', 'r') as file:
            for linea in file:
                espaciosf, preciof, pagominf, redondearf, minsmaxf, mon1, mon2, mon3, billete1f, billete2f, billete3f, billete4f, billete5f = linea.strip().split('\n')
    except FileNotFoundError:
        return 

def guardar_configuracion():
    global espaciosf, preciof, pagominf, redondearf, minsmaxf, mon1, mon2, mon3, billete1f, billete2f, billete3f, billete4f, billete5f
    with open('configuracion.dat', 'w') as file:
        for i in [espaciosf, preciof, pagominf, redondearf, minsmaxf, mon1, mon2, mon3, billete1f, billete2f, billete3f, billete4f, billete5f]:
            file.write(f"{i}\n")

def leer_parqueo():
    global parqueo
    try:
        with open('parqueo.dat', 'rb') as file:
            parqueo = pickle.load(file)
    except: pass
    return

def guardar_parqueo(parqueo):
    with open('parqueo.dat', 'wb') as file:
        pickle.dump(parqueo, file)

def leer_historial_parqueo():
    global historial_parqueo
    try:
        with open('historial_parqueo.dat', 'rb') as file:
            historial_parqueo = pickle.load(file)
    except: pass


def guardar_historial_parqueo(historial_parqueo):
    with open('historial_parqueo.dat', 'wb') as file:
        pickle.dump(historial_parqueo, file)

def leer_cajero():
    global valores
    try:
        with open('cajero.dat', 'rb') as file:
            valores = pickle.load(file)
    except: pass

def guardar_cajero(cajero):
    with open('cajero.dat', 'wb') as file:
        pickle.dump(cajero, file)
#########################################################
#Ayuda
########################################################
def open_pdf(pdf_filename): 
    #Conseguir el path de este script
    script_dir = os.path.dirname(os.path.abspath(__file__))
    
    #Conseguir el path de este pdf
    pdf_path = os.path.join(script_dir, pdf_filename)
    
    #Abrir el pdf
    os.startfile(pdf_path)

def ayuda():
    open_pdf("Manual de usuario estacionamiento de vehiculos.pdf")

############################################################
#Acerca de
############################################################
def acerca_de():
    messagebox.showinfo("Acerca de", "Estacionamiento de vehiculos \n Version 0.8 \n 2024 \n Fabian Mata Salas \n Taller de programacion")

leer_configuracion()
leer_cajero()
leer_parqueo()
leer_historial_parqueo()
abrirmenu()