#Fabian Mata proyecto 2 Grupo 4 Taller de la programacion, JUEGOS DE BOLA V1.0.0
############################################################
#Modulos
############################################################
import os
import itertools
import random
import copy
import pickle
import smtplib
from email.mime.text import MIMEText
############################################################
#Funciones Generales
############################################################
def titu(): #Funcion para poner el titulo del documento. No tiene entradas ni salidas
    """
    Limpia la pantalla de la consola y muestra el título del documento.

    Esta función utiliza el comando `os.system` para limpiar la pantalla de la consola y luego imprime
    un título formateado para el documento. Se utiliza para proporcionar un encabezado consistente
    para la interfaz de usuario.
    """
    os.system("cls")
    print("\n-------------------------------------------------------------------------------")
    print("JUEGOS DE BOLA".center(50),"\n")
############################################################

###########################################################
#Variables
nombre_torneo = ""
cantidad_de_equipos = ""
cantidad_de_clasificaciones_directas = ""
puntos_por_victoria = ""
puntos_por_empate = ""
equipos = {}
juegos = []
resultados = []
goleadores = []
advertencia = 0

############################################################
#CAJA NEGRA
############################################################

###########################################################
#Despliegue principal
############################################################
def despliegue_principal():
    """
    Despliega el menú principal del programa y maneja la navegación entre las diferentes opciones.

    El menú principal permite al usuario seleccionar entre varias opciones para configurar el torneo,
    registrar equipos, crear y consultar el calendario de juegos, registrar resultados, ver tablas de posiciones
    y goleadores, obtener ayuda, ver información acerca del programa, o finalizar el programa.

    No tiene parámetros de entrada ni valores de retorno.
    """
    while True:
        titu()
        print("1. Configuracion del torneo\n"
            "2. Registrar equipos\n"
            "3. Crear calendario de juegos\n"
            "4. Consultar calendario de juegos\n"
            "5. Registrar los resultados\n"
            "6. Tabla de posiciones\n"
            "7. Tabla de goleadores\n"
            "8. Ayuda\n"
            "9. Acerca de\n"
            "0. Fin\n"
              )
        global nombre_torneo, cantidad_de_equipos, cantidad_de_clasificaciones_directas, puntos_por_victoria, puntos_por_empate, equipos, juegos, resultados, goleadores
        entrada = input("OPCION ")
        match entrada:
            case "0":
                with open("configuracion.txt","w") as file:
                    file.write(nombre_torneo)
                    file.write("\n")
                    file.write(cantidad_de_equipos)
                    file.write("\n")
                    file.write(cantidad_de_clasificaciones_directas)
                    file.write("\n")
                    file.write(puntos_por_victoria)
                    file.write("\n")
                    file.write(puntos_por_empate)
                    with open("equipos.dat","wb") as file:
                        pickle.dump(equipos, file)
                    with open("juegos.dat", "wb") as file:
                        pickle.dump((juegos, resultados, goleadores),file)
                    return
            case "1": 
                nombre_torneo, cantidad_de_equipos, cantidad_de_clasificaciones_directas, puntos_por_victoria, puntos_por_empate = configuracion_del_torneo(nombre_torneo, cantidad_de_equipos, cantidad_de_clasificaciones_directas, puntos_por_victoria, puntos_por_empate)
            case "2":
                despliegue_registrar_equipos()
            case "3":
                equipos, cantidad_de_equipos, juegos, resultados, goleadores = crear_calendario_juegos(equipos,cantidad_de_equipos,juegos, resultados, goleadores)
            case "4":
                consultar_calendario(juegos,equipos,nombre_torneo)
            case "5":
                despliegue_registrar_resultados()
            case "6":
                imprimir_tabla_de_posiciones(nombre_torneo, cantidad_de_clasificaciones_directas, juegos, resultados, equipos, puntos_por_victoria, puntos_por_empate)
            case "7":
                tabla_de_goleadores(goleadores,juegos,nombre_torneo, equipos)
            case "8":
                ayuda()
            case "9":
                acerca_de()
            case "10":
                input(nombre_torneo)
                input(cantidad_de_equipos)
                input(cantidad_de_clasificaciones_directas)
                input(puntos_por_victoria)
                input(puntos_por_empate)
                input()
            case _:
                input("OPCION NO ES PERMITIDA <ACEPTAR> ")

############################################################
#Configuracion del torneo
############################################################
def configuracion_del_torneo(nombre_torneo, cantidad_equipos, cantidad_clasificantes, puntos_victoria, puntos_empate):

    """
    Configura los parámetros del torneo solicitando al usuario la información necesaria.

    Entradas: 
    nombre_torneo : Nombre del torneo.
    cantidad_equipos : Cantidad de equipos participantes.
    cantidad_clasificantes : Cantidad de equipos que clasifican directamente.
    puntos_victoria : Puntos ganados por cada partido ganado.
    puntos_empate : Puntos ganados por cada partido empatado.

    Salidas:
    tuple: Una variable que contiene el nombre del torneo, cantidad de equipos, cantidad de equipos que clasifican directamente,
           puntos por victoria y puntos por empate, todos como strings.
    """
    global equipos

    if equipos != {}:
        input("NO SE PUEDEN MODIFICAR LAS CONFIGURACIONES (YA HAY EQUIPOS REGISTRADOS AL TORNEO) <ACEPTAR> ")
        return nombre_torneo, cantidad_equipos, cantidad_clasificantes, puntos_victoria, puntos_empate
    
    while True:
        titu()
        print("CONFIGURACION DEL TORNEO\n".center(50))

        # Solicita el nombre del torneo
        while True:
            nombre_nuevo = input("Nombre del torneo: ")
            if nombre_nuevo == "C":
                return nombre_torneo, cantidad_equipos, cantidad_clasificantes, puntos_victoria, puntos_empate
            if 5 <= len(nombre_nuevo) <= 40:
                break
            input("Nombre del torneo invalido (5-40 caracteres) <ACEPTAR>".upper())

        # Solicita la cantidad de equipos participantes
        while True:
            cantequipos = input("Cantidad de equipos participantes: ")
            try:
                cantequipos = int(cantequipos)
            except:
                input("Cantidad debe ser un numero <ACEPTAR> ".upper())
                continue
            if (cantequipos) % 2 == 0 and (cantequipos) >= 2:
                break
            else:
                if (cantequipos) % 2 != 0:
                    input("Cantidad de equipos participantes debe ser par <ACEPTAR>".upper())
                    continue
                elif (cantequipos) < 2:
                    input("Cantidad de equipos participantes debe ser mayor o igual a 2 <ACEPTAR> ".upper())

        # Solicita la cantidad de equipos que clasifican directamente
        while True:
            clasificantes = input("Cantidad de equipos que clasifican directamente: ")
            try:
                clasificantes = int(clasificantes)
            except:
                input("Cantidad debe ser un numero <ACEPTAR> ".upper())
                continue
            if (clasificantes) >= 1 and (clasificantes) < (cantequipos):
                break
            else:
                if (clasificantes) > (cantequipos):
                    input("Cantidad de equipos que clasifican directamente debe ser menor a los participantes <ACEPTAR> ".upper())
                    continue
                elif (clasificantes) < 1:
                    input("Debe haber minimo un equipo clasificante de forma directa <ACEPTAR> ".upper())

        # Solicita los puntos ganados por cada partido ganado
        while True:
            victoria = input("Puntos ganados por cada partido ganado: ")
            try:
                victoria = int(victoria)
            except:
                input("Cantidad debe ser un numero <ACEPTAR> ".upper())
            if victoria >= 1:
                break
            input("Cantidad de puntos ganados por cada partido ganado debe ser mayor o igual a 1 <ACEPTAR> ".upper())

        # Solicita los puntos ganados por cada partido empatado
        while True:
            empate = input("Puntos ganados por cada partido empatado: ")
            try:
                empate = int(empate)
            except:
                input("Cantidad debe ser un numero <ACEPTAR> ".upper())
                continue
            if (empate) >= 1 and (empate) < (victoria):
                break
            else:
                if (empate) > (victoria):
                    input("Cantidad de puntos por empate debe ser menor a los ganados por victoria <ACEPTAR> ".upper())
                    continue
                elif (empate) < 1:
                    input("Cantidad ganados por empate debe ser mayor o igual a 1 <ACEPTAR> ".upper())
        
        confirmar = input("OPCION <A> ACEPTAR <C> CANCELAR ")
        match confirmar.lower():
            case "a":
                return nombre_nuevo, str(cantequipos), str(clasificantes), str(victoria), str(empate)
            case "c":
                return nombre_torneo, cantidad_equipos, cantidad_clasificantes, puntos_victoria, puntos_empate
            case _:
                input("OPCION NO ES PERMITIDA <ACEPTAR> ")
############################################################
#Registrar equipos
############################################################
def despliegue_registrar_equipos():
    while True:
        titu()
        print("REGISTRAR EQUIPOS\n".center(50))
        
        global nombre_torneo
        global equipos

        if nombre_torneo == "":
            input("Debe configurar los datos del torneo antes de continuar <ACEPTAR> ".upper())
            return 
        
        print("1. Agregar equipos\n"
        "2. Consultar equipos\n"
        "3. Modificar equipos\n"
        "4. Eliminar equipos\n"
        "0. Fin\n")

        opcion = input("OPCION ")

        match opcion:
            case "0":
                return
            case "1":
                equipos = agregar_equipos(equipos)
            case "2":
                equipos = consultar_equipos(equipos)
            case "3":
                equipos = modificar_equipos(equipos)
            case "4":
                equipos = eliminar_equipos(equipos)
            case "5":
                input(f"{equipos}")
            case _:
                input("OPCION NO ES PERMITIDA <ACEPTAR> ")

def agregar_equipos(equipos):
    """
    Función para agregar nuevos equipos al diccionario 'equipos'.
    Se solicita al usuario que ingrese el código del equipo, el nombre del equipo y la posición.
    Si el equipo ya existe, se muestra un mensaje apropiado.

    Entradas:
    equipos: Diccionario de equipos donde las claves son códigos de equipo y los valores son tuplas que contienen los nombres y posiciones de los equipos.

    Saliads:
    Diccionario actualizado con el nuevo equipo agregado 
    """
    
    titu() 
    print("REGISTRAR EQUIPOS: AGREGAR EQUIPOS\n".center(50))

    global cantidad_de_equipos

    if len(equipos) == int(cantidad_de_equipos): # Revisar si la cantidad de equipos registrados es igual a la establecida en configuracion
        input("TODOS LOS EQUIPOS HAN SIDO REGISTRADOS <ACEPTAR> ")
        return equipos

    while True:
        code = input("Código del equipo: ")
        if code.upper() == "C":  # Permite cancelar la operación si el usuario ingresa "C"
            return equipos
        if len(code) != 3 or not code.isalpha() or not code.isupper():
            input("CODIGO DE EQUIPO INVALIDO <ACEPTAR> ")
            continue
        if code in equipos:  # Verifica si el código del equipo ya está registrado
            input("EQUIPO YA FUE REGISTRADO PREVIAMENTE <ACEPTAR> ")
            continue
        break
    
    while True:
        parar = False
        nombre = input("Nombre del equipo: ")
        if not 3 <= len(nombre) <= 40:
            input("NOMBRE DE EQUIPO INVALIDO (3-40 CARACTERES) <ACEPTAR> ")
            continue 
        posicion = input("Posición en el escalón: ")
        try:
            if int(posicion) < 1:
                raise ValueError
        except:
            input("POSICIÓN DE EQUIPO INVALIDA <ACEPTAR> ")
            continue
        for tupla in equipos.values():
            if nombre == tupla[0] or posicion == tupla[1]:  # Verifica si el nombre y la posición ya están registrados
                input("ALGUNA CARACTERISTICA DEL EQUIPO YA FUE REGISTRADA PREVIAMENTE <ACEPTAR> ")
                parar = True
                break
        if parar:
            continue
        break
    
    while True:
        opcion = input("OPCIÓN <A>ACEPTAR <C>CANCELAR: ")
        match opcion.lower():
            case "a":  
                equipos[code] = (nombre, posicion)
                agregar_equipos(equipos)
                return equipos
            case "c": 
                agregar_equipos(equipos)
                return equipos
            case _:  
                input("OPCIÓN NO ES PERMITIDA <ACEPTAR> ")

def consultar_equipos(equipos):
    """
    Esta función permite al usuario consultar los equipos registrados.

    Entradas:
    equipos: Un diccionario que contiene a los equipos registrados, donde las claves son los códigos de los equipos y los valores son tuplas que contienen el nombre del equipo y su posición.

    Salidas:
    dict: La función devuelve el mismo diccionario de equipos registrados.

    Uso:
    consultar_equipos(equipos)
    """
    titu()
    print("REGISTRAR EQUIPOS: CONSULTAR EQUIPOS\n".center(50))  

    if len(equipos) == 0:
        input("REGISTRAR EQUIPOS PARA CONTINUAR <ACEPTAR> ")
        return equipos

    consulta = input("Equipo a consultar (Ingresar codigo, nombre, o posicion en elde escalón): ")  
    print()

    if consulta.upper() == "C":  # Si el usuario ingresa "C", la función devuelve el mismo diccionario de equipos registrados
        return equipos

    for i in range(len(equipos)):  # Recorre los equipos registrados
        llaves = tuple(equipos.keys()) #Crea variables para las llaves y las tuplas
        tuplas = tuple(equipos.values())
        if consulta == llaves[i] or consulta.lower() == tuplas[i][0].lower() or consulta == tuplas[i][1]:  # Verifica si el código, nombre o posición del equipo coinciden con la consulta del usuario
            print(f"Código: {llaves[i]}\nNombre: {tuplas[i][0]}\nPosición: {tuplas[i][1]}\n")
            input("<ACEPTAR> ")  # Solicita al usuario que confirme la consulta
            return consultar_equipos(equipos)  # Vuelve a llamar a la función consultar_equipos si la consulta coincide con algún equipo registrado

    input("EQUIPO NO ESTA REGISTRADO <ACEPTAR> ")  # Si la consulta no coincide con ningún equipo registrado, se muestra este mensaje
    return consultar_equipos(equipos)  # Vuelve a llamar a la función consultar_equipos si la consulta no coincide con ningún equipo registrado
        
def modificar_equipos(equipos):
    """
    Modifica los detalles de un equipo existente en el diccionario 'equipos'.

    Entradas:
    equipos: Un diccionario que contiene a los equipos registrados, donde las claves son códigos de equipo y los valores son tuplas que contienen el nombre del equipo y su posición.

    Salidas:
    El diccionario actualizado con los detalles del equipo modificado.
    """

    titu()
    print("REGISTRAR EQUIPOS: MODIFICAR EQUIPOS\n".center(50))  

    if len(equipos) == 0:
        input("REGISTRAR EQUIPOS PARA CONTINUAR <ACEPTAR> ")
        return equipos    

    consulta = input("Equipo a modificar (Ingresar código, nombre o posición en el escalón): ")  

    if consulta.upper() == "C":  # Si el usuario ingresa "C", la función devuelve el mismo diccionario de equipos registrados
        return equipos

    for i in range(len(equipos)):  # Recorre los equipos registrados
        llaves = tuple(equipos.keys()) #Crea variables para las llaves y las tuplas
        tuplas = tuple(equipos.values())
        if consulta == llaves[i] or consulta.lower() == tuplas[i][0].lower() or consulta == tuplas[i][1]:  # Verifica si el código, nombre o posición del equipo coinciden con la consulta del usuario
            
            while True: 
                titu()
                print("REGISTRAR EQUIPOS: MODIFICAR EQUIPOS\n".center(50))  
                print(f"EQUIPO A MODIFICAR: {llaves[i]}, {tuplas[i][0]}, # {tuplas[i][1]}, \n")   

                parar = False

                while True:

                    if parar: 
                        break

                    nombre = input("Nombre nuevo del equipo: ")
                    for tupla in tuplas:
                        if tupla[0] == tuplas[i][0]:
                            continue
                        if nombre.lower() == tupla[0].lower():
                            input("NOMBRE YA HA SIDO REGISTRADO <ACEPTAR> ")
                            parar = True
                            break
                    if parar: 
                        continue
                    if nombre == "":
                        nombre = tuplas[i][0]
                    if not 3 <= len(nombre) <= 40:
                        input("NOMBRE DE EQUIPO INVALIDO (3-40 CARACTERES) <ACEPTAR> ")
                        continue

                    posicion = input("Posición nueva en el escalón: ")
                    for tupla in tuplas:
                        if tupla[1] == tuplas[i][1]:
                            continue
                        if posicion == tupla[1]:
                            input("POSICION YA HA SIDO REGISTRADA <ACEPTAR> ")
                            parar = True
                            break
                    if parar:
                        continue
                    if posicion == "":
                        posicion = tuplas[i][1]
                    try:
                        if int(posicion) < 1:
                            raise ValueError
                    except:
                        input("POSICIÓN DE EQUIPO INVALIDA <ACEPTAR> ")
                        continue

                    for tupla in tuplas:
                        if tupla == (tuplas[i][0], tuplas[i][1]):
                            continue
                        if nombre in tupla or posicion in tupla:  # Verifica si el nombre y la posición ya están registrados
                            input("EQUIPO YA FUE REGISTRADO PREVIAMENTE <ACEPTAR> ")
                            continue

                    break
                
                if parar:
                    continue

                while True:

                    opcion = input("OPCIÓN <A>ACEPTAR <C>CANCELAR: ")
                    match opcion.lower():
                        case "a":  
                            equipos[llaves[i]] = (nombre, posicion)
                            modificar_equipos(equipos)
                            return equipos
                        case "c": 
                            modificar_equipos(equipos)
                            return equipos
                        case _:  
                            input("OPCIÓN NO ES PERMITIDA <ACEPTAR> ")


def eliminar_equipos(equipos):
    """
    Esta función permite eliminar un equipo registrado del diccionario 'equipos'.

    Entradas:
    equipos: Un diccionario que contiene a los equipos registrados, donde las claves son los códigos de equipo y los valores son tuplas que contienen el nombre del equipo y su posición.

    Salidas:
    Un diccionario actualizado con el equipo eliminado.
    """
    while True:

        listo = False

        titu()
        print("REGISTRAR EQUIPOS: ELIMINAR EQUIPOS\n".center(50)) 

        global juegos #Verificar que no hayan calendarios registrados
        if juegos != []:
            return equipos

        if len(equipos) == 0: # Si no hay equipos no se puede avanzar
            input("REGISTRAR EQUIPOS PARA CONTINUAR <ACEPTAR> ")
            return equipos

        consulta = input("Equipo a eliminar (Ingresar código, nombre o posición en el escalón): ")

        if consulta.upper() == "C":  # Si el usuario ingresa "C", la función devuelve el mismo diccionario de equipos registrados
            return equipos

        for i in range(len(equipos)):  # Recorre los equipos registrados
            llaves = tuple(equipos.keys())  # Crea variables para las llaves y las tuplas
            tuplas = tuple(equipos.values())
            if consulta == llaves[i] or consulta.lower() == tuplas[i][0].lower() or consulta == tuplas[i][1]:  # Verifica si el código, nombre o posición del equipo coinciden con la consulta del usuario
                print(f"EQUIPO A ELIMINAR: {llaves[i]}\n")
                while True:
                    confirmar = input("OPCION <A>ACEPTAR <C>CANCELAR ")
                    match confirmar.lower():
                        case "a":
                            confirmar_2 = input(f"De verdad quieres borrar el equipo {llaves[i]}? [SI/NO] ")
                            match confirmar_2.lower():
                                case "si":
                                    del equipos[llaves[i]]
                                    listo = True
                                    break
                                case "no":
                                    listo = True
                                    break
                                case _:
                                    input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                break

        if listo == False:
            input("EQUIPO NO ESTA REGISTRADO <ACEPTAR> ")

################################################################
#CREAR CALENDARIO DE JUEGOS
################################################################
def crear_calendario_juegos(equipos, cantidad_de_equipos, juegos, resultados, goleadores):
    """
    Función para crear un calendario de juegos para un conjunto dado de equipos.

    Entradas:
    - equipos: Un diccionario que contiene los nombres de los equipos como claves y datos del equipo como valores.
    - cantidad_de_equipos: El número total de equipos.
    - juegos: La lista de juegos, en caso de que tenga informacion previa, se reemplazaria el dato anterior.

    Retorna:
    - tuple: Una tupla que contiene información sobre los equipos, el número total de equipos y el calendario de juegos.
             El calendario de juegos se representa como una lista de tuplas, donde cada tupla contiene pares de juegos programados para un día.

    Nota:
    - La función baraja el orden de los pares de juegos para crear aleatoriedad en el calendario.
    - La función asegura que cada equipo juegue solo un juego por día y que cada par de juegos ocurra solo una vez en el calendario.
    - Si la función no puede crear un calendario válido después de un gran número de intentos (contador >= 999), vuelve a intentarlo para que sirva.
    - La función usa la función itertools.combinations para generar todos los posibles pares de juegos.
    - La función emplea una llamada a random.shuffle para introducir aleatoriedad.
    """

    titu()
    print("CREAR CALENDARIO DE JUEGOS\n".center(50))

    if cantidad_de_equipos == 0 or len(equipos) != int(cantidad_de_equipos): 
        input("AUN FALTAN EQUIPOS POR REGISTRAR <ACEPTAR> ")
        return (equipos, cantidad_de_equipos, juegos, resultados, goleadores)
    
    llaves = list(equipos.keys())
    salio_bien = False

    while True:

        if salio_bien:
            break

        partidos = []
        partidos = list(itertools.combinations(llaves, 2))
        dias = []
        contador = 0

        while partidos[:]:

            if contador >= 9999:
                break

            dia = []
            equipos_dia = set()
            random.shuffle(partidos) # Este método es terrible pero sirve

            for partida in partidos:
                equipo1, equipo2 = partida
                if equipo1 not in equipos_dia and equipo2 not in equipos_dia:
                        dia.append(partida)
                        equipos_dia.add(equipo1)
                        equipos_dia.add(equipo2)

            if len(dia) != int(cantidad_de_equipos) // 2:
                contador += 1
                continue
            
            for partido in dia:
                partidos.remove(tuple(partido))        

            dias.append(tuple(dia))

            if partidos == []:
                salio_bien = True 
    
    for dia in dias[:]:
         
        dia_inverso = []

        for partido in dia:
             dia_inverso.append((partido[1], partido[0]))
        
        dias.append(tuple(dia_inverso))

    resultados = []

    for i in range(len(dias)): #Creo una lista con 0s para usar al futuro con puntaje y goleadores
        puntaje_dia = []
        for partido in dias[i]:
            puntaje_dia.append(0)
        resultados.append((puntaje_dia))

    goleadores = copy.deepcopy(resultados) #Esto crea una copia sin problemas que tiene python con matrices, como que las listas de adentro sean referencias a otras.
    
    input("Calendario creado <GRACIAS> ")

    return (equipos, cantidad_de_equipos, dias,resultados,goleadores)

############################################################
#Consultar calendario de juegos
############################################################
def consultar_calendario(juegos, equipos, nombre_torneo):
    """
    Esta función se utiliza para mostrar el calendario de juegos registrados.

    Entradas::
    juegos: Una lista de tuplas que contiene los juegos programados. Cada tupla representa un día de juegos y contiene pares de equipos que jugarán entre sí.
    equipos: Un diccionario que contiene los equipos registrados. Las claves son los códigos de los equipos y los valores son tuplas con el nombre del equipo y su posición.
    nombre_torneo: El nombre del torneo.

    Salidas:
    tuple: Una tupla que contiene la lista de juegos, el diccionario de equipos y el nombre del torneo.
    """

    titu()
    print("CONSULTAR CALENDARIO DE JUEGOS\n".center(50))

    if juegos == []:
        input("NO HAY CALENDARIO REGISTRADO <ACEPTAR> ")
        return juegos
    
    print()
    print(nombre_torneo)
    contador = 1

    for dia in juegos:
        print(f"Fecha {contador}")
        for partida in dia:
            equipo1, equipo2 = partida
            print(f"{equipo1} VS {equipo2}")
        contador += 1
        print()

    input("<ACEPTAR> ")
    return juegos, equipos, nombre_torneo

############################################################
#Agregar resultados de partidas
############################################################
def despliegue_registrar_resultados():

    while True:
        titu()
        print("REGISTRAR LOS RESULTADOS\n".center(50))

        global juegos, resultados, goleadores, advertencia
        if juegos == []:
            input("NO HAY CALENDARIO REGISTRADO <ACEPTAR> ")
            return 


        opcion = input("1. Agregar resultados\n"
        "2. Consultar resultados\n"
        "3. Modificar resultados\n"
        "4. Eliminar resultados\n"
        "0. Fin\n\n"
        "OPCION ")

        match opcion:
            case "1":
                juegos, resultados, goleadores = agregar_resultados(juegos, resultados, goleadores)
            case "2":
                consultar_resultados(juegos,resultados,goleadores)
            case "3":
                juegos, resultados, goleadores, advertencia = modificar_resultados(juegos,resultados,goleadores, advertencia)
            case "4":
                juegos,resultados,goleadores = eliminar_resultados(juegos,resultados,goleadores)
            case "0":
                return None
            case _:
                input("OPCION NO ES PERMITIDA <ACEPTAR> ")
    
def agregar_resultados(juegos, resultados, goleadores):

    while True:
        titu()
        print("REGISTRAR LOS RESULTADOS: AGREGAR\n".center(50))

        goleadores_partida = []

        casa = input("Codigo del equipo casa: ")

        if casa.lower() == "c":
            return juegos, resultados, goleadores
        
        visita = input("Codigo del equipo visita: ")
        esta = False

        for q in range(len(juegos)):
            dia = juegos[q]
            for i in range(len(dia)):
                tupla = dia[i]
                if (casa,visita) == tupla:
                    juego = q
                    partida_especifica = i
                    esta = True
                    break
    
        if esta == 0:
            input("PARTIDA NO ESTA EN EL CALENDARIO <ACEPTAR> ")
            continue

        if resultados[juego][partida_especifica] != 0:
            input("LA PARTIDA YA FUE REGISTRADA <ACEPTAR> ")
            continue   

        while True:
            try:
                while True:

                    puntos_casa = input("Goles del equipo casa: ")
                    puntos_casa = int(puntos_casa)
                    if puntos_casa < 0 :
                        input("GOLES NEGATIVOS NO SON PERMITIDOS <ACEPTAR> ")
                        continue
                    break
                
                while True:

                    goleadores_temporal = []

                    for i in range(puntos_casa):

                        while True:
                            nombre = input("Nombre del goleador: ")
                            if len(nombre)<2 or len(nombre) > 40:
                                input("NOMBRE INVALIDO (DEBE SER 2-40 CARACTERES) <ACEPTAR> ")
                                continue
                            break

                        while True:
                            try:
                                minuto = (input("Minuto: "))
                                if "+" in minuto:
                                    minuto = minuto.replace(" ","") #Eliminar espacios del minuto
                                    minuto = minuto.split("+")
                                    if minuto[0] != "90":
                                        raise ValueError
                                    sobrante = abs(int(minuto[1]))
                                    total = int(minuto[0]) + (sobrante) 
                                    if -1 > total or total > 120 or sobrante == 0 or total == 0:
                                        raise ValueError
                                    total = 90
                                    break
                                else:
                                    minuto = int(minuto)
                                    total = int(minuto)
                                    sobrante = 0
                                    if minuto < 1 or minuto > 90:
                                        input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                        continue
                                    break
                            except:
                                input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                continue
                        
                        goleadores_temporal.append((nombre,total,sobrante))
                        
                    break

                break

            except:
                input("CANTIDAD DE GOLES INVALIDOS <ACEPTAR> ")
                continue

        goleadores_partida.append(tuple(goleadores_temporal))
        
        while True:
            try:
                while True:

                    puntos_visita = input("Goles del equipo visita: ")
                    puntos_visita = int(puntos_visita)
                    if puntos_visita < 0 :
                        input("GOLES NEGATIVOS NO SON PERMITIDOS <ACEPTAR> ")
                        continue
                    break
                
                while True:

                    goleadores_temporal = []

                    for i in range(puntos_visita):

                        while True:
                            nombre = input("Nombre del goleador: ")
                            if len(nombre)<2 or len(nombre) > 40:
                                input("NOMBRE INVALIDO (DEBE SER 2-40 CARACTERES)<ACEPTAR> ")
                                continue
                            break

                        while True:
                            try:
                                minuto = (input("Minuto: "))
                                if "+" in minuto:
                                    minuto = minuto.replace(" ","") #Eliminar espacios del minuto
                                    minuto = minuto.split("+")
                                    if minuto[0] != "90":
                                        raise ValueError
                                    sobrante = abs(int(minuto[1]))
                                    total = int(minuto[0]) + (sobrante) 
                                    if -1 > total or total > 120 or sobrante == 0 or total == 0:
                                        raise ValueError
                                    total = 90
                                    break
                                else:
                                    minuto = int(minuto)
                                    sobrante = 0
                                    if minuto < 1 or minuto > 90:
                                        input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                        continue
                                    total = int(minuto)
                                    break
                            except:
                                input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                continue
                        
                        goleadores_temporal.append((nombre,total,sobrante))
                        
                    break

                break

            except:
                input("CANTIDAD DE GOLES INVALIDOS <ACEPTAR> ")
                continue

        goleadores_partida.append(tuple(goleadores_temporal))
        
        while True:
            confirmar = input("OPCION <C>CANCELAR <A>ACEPTAR ")
            match confirmar.lower():
                case "c":
                    break
                case "a":
                    goleadores[juego][partida_especifica] = tuple(goleadores_partida)
                    resultados[juego][partida_especifica] = ((puntos_casa, puntos_visita))
                    break
                case _:
                    input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                    continue

def consultar_resultados(juegos, resultados, goleadores):
    
    while True:
        titu()
        print("REGISTRAR LOS RESULTADOS: CONSULTAR\n".center(50))

        casa = input("Codigo del equipo casa: ")

        if casa.lower() == "c":
            return juegos, resultados, goleadores
        
        visita = input("Codigo del equipo visita: ")
        esta = False

        for q in range(len(juegos)):
            dia = juegos[q]
            for i in range(len(dia)):
                tupla = dia[i]
                if (casa,visita) == tupla:
                    juego = q
                    partida_especifica = i
                    esta = True
                    break
    
        if esta == 0:
            input("PARTIDA NO ESTA EN EL CALENDARIO <ACEPTAR> ")
            continue

        if resultados[juego][partida_especifica]  == 0:
            input("LA PARTIDA AUN NO FUE REGISTRADA <ACEPTAR> ")
            continue

        print(f"RESULTADOS DE LA PARTIDA {juegos[juego][partida_especifica][0]} VS {juegos[juego][partida_especifica][1]}")
        print(f"GOLEADORES DE {juegos[juego][partida_especifica][0]}:", end=" ")
        for i in range(len(goleadores[juego][partida_especifica][0])):
            print(f"{goleadores[juego][partida_especifica][0][i][0]}, minuto {goleadores[juego][partida_especifica][0][i][1]} + {goleadores[juego][partida_especifica][0][i][2]}", end=" ")
            if i!= len(goleadores[juego][partida_especifica][0])-1:
                print(", ", end="")
            else:
                print()
        if resultados[juego][partida_especifica][0] == 0:
            print()
        print(f"GOLEADORES DE {juegos[juego][partida_especifica][1]}:", end=" ")
        for i in range(len(goleadores[juego][partida_especifica][1])):
            print(f"{goleadores[juego][partida_especifica][1][i][0]}, minuto {goleadores[juego][partida_especifica][1][i][1]} + {goleadores[juego][partida_especifica][1][i][2]}", end=" ")
            if i!= len(goleadores[juego][partida_especifica][1])-1:
                print(", ", end="")
            else:
                print()
        if resultados[juego][partida_especifica][1] == 0:
            print()
        print(f"PUNTAJE: {resultados[juego][partida_especifica][0]} - {resultados[juego][partida_especifica][1]}")
        input("<ACEPTAR> ")
        continue
    

def modificar_resultados(juegos,resultados,goleadores, advertencia):
    
    while True:
        titu()
        print("REGISTRAR LOS RESULTADOS: MODIFICAR RESULTADOS\n".center(50))

        if advertencia == 0:
            okay = input("AVISO: MODIFICAR LOS RESULTADOS DE UN PARTIDO BORRARA DATOS ANTERIORES [<A>ACEPTAR <C>CANCELAR <N>NO VOLVER A MOSTRAR] ")
            match okay.lower():
                case "a":
                    pass
                case "c":
                    return juegos, resultados, goleadores, advertencia
                case "n":
                    advertencia = 1
                case _:
                    input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                    continue

    
        casa = input("Codigo del equipo casa: ")

        if casa.lower() == "c":
            return juegos, resultados, goleadores, advertencia
        
        visita = input("Codigo del equipo visita: ")
        esta = False

        for q in range(len(juegos)):
            dia = juegos[q]
            for i in range(len(dia)):
                tupla = dia[i]
                if (casa,visita) == tupla:
                    juego = q
                    partida_especifica = i
                    esta = True
                    break

        if esta == 0:
            input("PARTIDA NO ESTA EN EL CALENDARIO <ACEPTAR> ")
            continue

        if resultados[juego][partida_especifica]  == 0:
            input("LA PARTIDA AUN NO FUE REGISTRADA <ACEPTAR> ")
            continue
        
        goleadores_partida = []

        while True:
            try:
                while True:

                    puntos_casa = input("Goles del equipo casa: ")
                    puntos_casa = int(puntos_casa)
                    if puntos_casa < 0 :
                        input("GOLES NEGATIVOS NO SON PERMITIDOS <ACEPTAR> ")
                        continue
                    break
                
                while True:

                    goleadores_temporal = []

                    for i in range(puntos_casa):

                        while True:
                            nombre = input("Nombre del goleador: ")
                            if len(nombre)<2 or len(nombre) > 40:
                                input("NOMBRE INVALIDO (DEBE SER 2-40 CARACTERES) <ACEPTAR> ")
                                continue
                            break

                        while True:
                            try:
                                minuto = (input("Minuto: "))
                                if "+" in minuto:
                                    minuto = minuto.replace(" ","") #Eliminar espacios del minuto
                                    minuto = minuto.split("+")
                                    if minuto[0] != "90":
                                        raise ValueError
                                    sobrante = abs(int(minuto[1]))
                                    total = int(minuto[0]) + (sobrante) 
                                    if -1 > total or total > 120 or sobrante == 0 or total == 0:
                                        raise ValueError
                                    total = 90
                                    break
                                else:
                                    minuto = int(minuto)
                                    total = int(minuto)
                                    sobrante = 0
                                    if minuto < 1 or minuto > 90:
                                        input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                        continue
                                    break
                            except:
                                input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                continue
                        
                        goleadores_temporal.append((nombre,total,sobrante))
                        
                    break

                break

            except:
                input("CANTIDAD DE GOLES INVALIDOS <ACEPTAR> ")
                continue

        goleadores_partida.append(tuple(goleadores_temporal))
        
        while True:
            try:
                while True:

                    puntos_visita = input("Goles del equipo visita: ")
                    puntos_visita = int(puntos_visita)
                    if puntos_visita < 0 :
                        input("GOLES NEGATIVOS NO SON PERMITIDOS <ACEPTAR> ")
                        continue
                    break
                
                while True:

                    goleadores_temporal = []

                    for i in range(puntos_visita):

                        while True:
                            nombre = input("Nombre del goleador: ")
                            if len(nombre)<2 or len(nombre) > 40:
                                input("NOMBRE INVALIDO (DEBE SER 2-40 CARACTERES)<ACEPTAR> ")
                                continue
                            break

                        while True:
                            try:
                                minuto = (input("Minuto: "))
                                if "+" in minuto:
                                    minuto = minuto.replace(" ","") #Eliminar espacios del minuto
                                    minuto = minuto.split("+")
                                    if minuto[0] != "90":
                                        raise ValueError
                                    sobrante = abs(int(minuto[1]))
                                    total = int(minuto[0]) + (sobrante) 
                                    if -1 > total or total > 120 or sobrante == 0 or total == 0:
                                        raise ValueError
                                    total = 90
                                    break
                                else:
                                    minuto = int(minuto)
                                    sobrante = 0
                                    if minuto < 1 or minuto > 90:
                                        input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                        continue
                                    total = int(minuto)
                                    break
                            except:
                                input("MINUTO INVALIDO (DEBE SER 1-90 O 90+TIEMPO EXTRA) <ACEPTAR> ")
                                continue
                        
                        goleadores_temporal.append((nombre,total,sobrante))
                        
                    break

                break

            except:
                input("CANTIDAD DE GOLES INVALIDOS <ACEPTAR> ")
                continue

        goleadores_partida.append(tuple(goleadores_temporal))
        
        while True:
            confirmar = input("OPCION <C>CANCELAR <A>ACEPTAR ")
            match confirmar.lower():
                case "c":
                    break
                case "a":
                    goleadores[juego][partida_especifica] = tuple(goleadores_partida)
                    resultados[juego][partida_especifica] = ((puntos_casa, puntos_visita))
                    break
                case _:
                    input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                    continue


def eliminar_resultados(juegos,resultados,goleadores):
    
    while True:
        titu()
        print("REGISTRAR LOS RESULTADOS: ELIMINAR RESULTADOS\n".center(50))
        casa = input("Codigo del equipo casa: ")

        if casa.lower() == "c":
            return juegos, resultados, goleadores
        
        visita = input("Codigo del equipo visita: ")

        esta = False

        for q in range(len(juegos)):
            dia = juegos[q]
            for i in range(len(dia)):
                tupla = dia[i]
                if (casa,visita) == tupla:
                    juego = q
                    partida_especifica = i
                    esta = True
                    break

        if esta == 0:
            input("PARTIDA NO ESTA EN EL CALENDARIO <ACEPTAR> ")
            continue
        
        if resultados[juego][partida_especifica]  == 0:
            input("LA PARTIDA AUN NO FUE REGISTRADA")
            continue

        print(f"PARTIDA EN CUESTION: {juegos[juego][partida_especifica][0]} VS {juegos[juego][partida_especifica][1]}")
        print(f"GOLEADORES DE {juegos[juego][partida_especifica][0]}:", end=" ")
        for i in range(len(goleadores[juego][partida_especifica][0])):
            print(f"{goleadores[juego][partida_especifica][0][i][0]}, minuto {goleadores[juego][partida_especifica][0][i][1]} + {goleadores[juego][partida_especifica][0][i][2]}", end=" ")
            if i!= len(goleadores[juego][partida_especifica][0])-1:
                print(", ", end="")
            else:
                print()
        if resultados[juego][partida_especifica][0] == 0:
            print()
        print(f"GOLEADORES DE {juegos[juego][partida_especifica][1]}:", end=" ")
        for i in range(len(goleadores[juego][partida_especifica][1])):
            print(f"{goleadores[juego][partida_especifica][1][i][0]}, minuto {goleadores[juego][partida_especifica][1][i][1]} + {goleadores[juego][partida_especifica][1][i][2]}", end=" ")
            if i!= len(goleadores[juego][partida_especifica][1])-1:
                print(", ", end="")
            else:
                print()
        if resultados[juego][partida_especifica][1] == 0:
            print()
        print(f"PUNTAJE: {resultados[juego][partida_especifica][0]} - {resultados[juego][partida_especifica][1]}")

        while True:
            confirmar = input(f"\nOPCION <A>ACEPTAR <C>CANCELAR ")
            match confirmar.lower():
                case "c":
                    break
                case "a":
                    confirmar_2 = input(f"DE VERDAD DESEAS ELIMINAR LOS DATOS DEL PARTIDO {juegos[juego][partida_especifica][0]} VS {juegos[juego][partida_especifica][1]}? [SI/NO] ")
                    match confirmar_2.lower():
                        case "si":
                            resultados[juego][partida_especifica] = 0
                            goleadores[juego][partida_especifica] = 0
                            break
                        case "no":
                            break
                        case _:
                            input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                            continue
                case _:
                    input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                    continue

############################################################
#TABLA DE POSICIONES
############################################################
def imprimir_tabla_de_posiciones(nombre_torneo, clasificantes, juegos,resultados,equipos,puntos_por_victoria,puntos_por_empate):
    
    titu()
    print("TABLA DE POSICIONES\n".center(50))

    h = " "
    h *= 40

    x = (f"{nombre_torneo}\n"
         f"Equipos que clasfician: {clasificantes}\n"
         "\n"
         f"EQUIPO{h[:20]}JJ JG  JE JP  GF GC  GD PUNTOS"
         "\n"
         "-----------------------------------------------------------------------------")
    
    datos_por_equipo = []
    marca = 0
    
    for equipo in equipos:

        nombre_equipo = equipos[equipo][0]
        puesto_equipo = int(equipos[equipo][1])
        codigo_equipo = equipo
        contador = 0

        for q in range(len(juegos)):
            juego = juegos[q]
            for i in range(len(juego)):
                partida = juego[i]
                resultado = resultados[q][i]
                if codigo_equipo == partida[0] or codigo_equipo == partida[1]:
                    if resultado != 0:
                        contador += 1

        juegos_jugados = contador

        juegos_ganados = 0
        juegos_perdidos = 0
        juegos_empatados = 0
        goles_a_favor = 0
        goles_en_contra = 0

        for juego in range(len(resultados)):
            juego_caracter = resultados[juego]
            for partida in range(len(juego_caracter)):
                if resultados[juego][partida] == 0:
                    continue
                partida_nombres = juegos[juego][partida]
                if codigo_equipo == partida_nombres[0] or codigo_equipo == partida_nombres[1]:
                    if codigo_equipo == partida_nombres[0]:
                        slot_equipo = 0
                        equipo_rival = 1
                    elif codigo_equipo == partida_nombres[1]:
                        slot_equipo = 1
                        equipo_rival = 0
                    if resultados[juego][partida]!= 0:
                        if resultados[juego][partida][slot_equipo] > resultados[juego][partida][equipo_rival]:
                            juegos_ganados += 1
                        elif resultados[juego][partida][slot_equipo] < resultados[juego][partida][equipo_rival]:
                            juegos_perdidos += 1
                        elif resultados[juego][partida][slot_equipo] == resultados[juego][partida][equipo_rival]:
                            juegos_empatados += 1
                        goles_a_favor += resultados[juego][partida][slot_equipo]
                        goles_en_contra += resultados[juego][partida][equipo_rival]
        
        goles_diferencia = ""

        if goles_a_favor > goles_en_contra:
            goles_diferencia += "+"
            goles_diferencia += str(goles_a_favor - goles_en_contra)
            goles_diferencia_interno = goles_a_favor - goles_en_contra
        elif goles_a_favor < goles_en_contra:
            goles_diferencia += "-"
            goles_diferencia += str(goles_en_contra - goles_a_favor)
            goles_diferencia_interno = goles_a_favor - goles_en_contra
        elif goles_a_favor == goles_en_contra:
            goles_diferencia += "0"
            goles_diferencia_interno = 0

        puntos = (juegos_ganados * puntos_por_victoria) + (juegos_empatados * puntos_por_empate)
        if puntos == "":
            puntos = 0
        puntos = int(puntos)
        marca += 1

        datos_por_equipo.append([[nombre_equipo,juegos_jugados,juegos_ganados,juegos_empatados,juegos_perdidos,goles_a_favor,goles_en_contra,goles_diferencia,puntos,codigo_equipo,goles_diferencia_interno,puesto_equipo],marca])
     
    puntajes_clasificados = []

    for i in range(len(datos_por_equipo)):
        puntaje = datos_por_equipo[i][0][8]
        marca = datos_por_equipo[i][1]
        puntajes_clasificados.append([puntaje,marca])
    
    #Ordenar puntaje_clasificados de mayor a menor en base a puntaje
    puntajes_clasificados.sort(reverse=True)

    equipos_ordenados = []

    for i in range(len(puntajes_clasificados)):

        esta = False

        puntaje_equipo_actual = puntajes_clasificados[i][0] 
        marca_equipo_actual = puntajes_clasificados[i][1] 

        for dato in equipos_ordenados:

            if puntaje_equipo_actual == dato[0]:
                esta = True
                break

        if esta == False:
            equipos_ordenados.append([puntaje_equipo_actual, marca_equipo_actual])
            continue

        else:
            copia = copy.copy(equipos_ordenados)
            equipos_ordenados = []
            listo = False
            for i in range(len(copia)+1):

                if i == (len(copia)):
                    if listo:
                        equipos_ordenados.append([copia[i-1][0],copia[i-1][1]]) 
                        break
                    else:
                        equipos_ordenados.append([puntaje_equipo_actual, marca_equipo_actual])
                        break

                puntaje_iteracion = copia[i][0]
                marca_iteracion = copia[i][1]

                if copia[i][0] == puntaje_equipo_actual and listo == False:

                    #Agarrar datos importantes

                    for dato in datos_por_equipo:
                        if marca_equipo_actual == dato[1]:
                            goles_diferencia_actual = dato[0][10]
                            goles_a_favor_actual = dato[0][5]
                            puesto_equipo_actual = dato[0][11]
                        elif marca_iteracion == dato[1]:
                            goles_diferencia_iteracion = dato[0][10]
                            goles_a_favor_iteracion = dato[0][5]
                            puesto_equipo_iteracion = dato[0][11]

                    #Primera regla, gana el equipo con mayor diferencia de goles

                    if goles_diferencia_actual > goles_diferencia_iteracion:
                        equipos_ordenados.append([puntaje_equipo_actual, marca_equipo_actual])
                        listo = True
                        continue
                    if goles_diferencia_actual < goles_diferencia_iteracion:
                        equipos_ordenados.append([puntaje_iteracion, marca_iteracion])
                        continue
                    if goles_diferencia_actual == goles_diferencia_iteracion:

                        #Segunda regla, gana el equipo con mayor goles a favor

                        if goles_a_favor_actual > goles_a_favor_iteracion:
                            equipos_ordenados.append([puntaje_equipo_actual, marca_equipo_actual])
                            listo = True
                            continue
                        if goles_a_favor_actual < goles_a_favor_iteracion:
                            equipos_ordenados.append([puntaje_iteracion, marca_iteracion])
                            continue
                        if goles_a_favor_actual == goles_a_favor_iteracion:

                            #Tercera regla, gana el equipo con menor puesto

                            if puesto_equipo_actual < puesto_equipo_iteracion:
                                equipos_ordenados.append([puntaje_equipo_actual, marca_equipo_actual])
                                listo = True
                                continue
                            if puesto_equipo_actual > puesto_equipo_iteracion:
                                equipos_ordenados.append([puntaje_iteracion, marca_iteracion])
                                continue
                if listo:
                    equipos_ordenados.append([copia[i-1][0],copia[i-1][1]])
                if puntaje_iteracion != puntaje_equipo_actual:
                        equipos_ordenados.append([copia[i][0], copia[i][1]])
                        continue

    for i in equipos_ordenados:
        for dato in datos_por_equipo:
            if i[1] == dato[1]:
                datos = dato[0][:9]
                x += ("\n")
                for q in range(len(datos)):
                    dato_en_cuestion = datos[q] 
                    if q == 0:
                        espacio = h[:-len(dato_en_cuestion)-14]
                        x += (dato_en_cuestion)
                        x += espacio
                    else:
                        x += str(dato_en_cuestion)
                        x += "    "[:-len(str(dato_en_cuestion))]
                break

    print(x)

    while True:
        confirmar = input("Desea enviar la tabla a algun correo electronico? [SI/NO] ")         
        match confirmar.lower():
            case "no":
                return
            case "si":
                sujeto = f"Tabla de posiciones del torneo {nombre_torneo}"
                enviador = "torneosdebolafmata@gmail.com"
                contrasena = "ktic kvtv ized tdjf"
                recibidor = input("Escribir el correo al que se le desea enviar la informacion: ")  
                msg = MIMEText(x)
                msg['Subject'] = sujeto
                msg['From'] = enviador
                msg['To'] = recibidor
                smtp_server = "smtp.gmail.com"
                smtp_port = 587
                try: 
                    with smtplib.SMTP(smtp_server, smtp_port) as server:
                        server.starttls()  # Iniciar servidor
                        server.login(enviador, contrasena)  # Log in al email
                        server.sendmail(enviador, recibidor, msg.as_string())  # Enviar el email
                        input("Email enviado! <ACEPTAR> ")    
                        return      
                except Exception as e:
                    input(f"Error: {e}")   
            case _:
                input("Opcion no es permitida <ACEPTAR> ")   



############################################################################################
#Tabla de posiciones
############################################################################################
def tabla_de_goleadores(goleadores, partidos,nombre_torneo, nombres_equipo):
    
    titu()
    print("TABLA DE GOLEADORES\n".center(50))

    print(nombre_torneo)
    print("Tabla de goleadores: \n")
    
    lista_jugadores = []
    for a in range(len(goleadores)):
        dia = goleadores[a]
        for b in range(len(dia)):
            partida = dia[b]
            if partida == 0:
                continue
            for c in range(len(partida)):
                equipo = partida[c]
                for d in range(len(equipo)):
                    jugador = equipo[d]
                    esta = False
                    for lista_2 in lista_jugadores:
                        if jugador[0] in lista_2:
                            esta = True
                    codigo_equipo = partidos[a][b][c]
                    nombre_equipo = nombres_equipo[codigo_equipo][0]
                    if esta == False:
                        lista_jugadores.append([jugador[0], nombre_equipo, 1])
                    elif esta:
                        for lista in lista_jugadores:
                            if lista[0] == jugador[0]:
                                lista[2] = lista[2] + 1
                                
    h = " "
    h *= 40
    
    print(f"Jugador {h[:35]} Equipo{h[:35]} Goles")
    print("---------------------------------------------------------------------------------------------------------------------")
    for jugador in lista_jugadores:
        y = h[:-len(jugador[1])]
        z = h[:-len(jugador[0])]
        print(f"{jugador[0]} {z} {jugador[1]} {y} {jugador[2]}")
        print(h)

    input("<ACEPTAR> ")
    

################################################################################
#Ayuda
################################################################################
def open_pdf(pdf_filename): 
    #Conseguir el path de este script
    script_dir = os.path.dirname(os.path.abspath(__file__))
    
    #Conseguir el path de este pdf
    pdf_path = os.path.join(script_dir, pdf_filename)
    
    #Abrir el pdf
    os.startfile(pdf_path)

def ayuda():
    open_pdf("Manual de usuario torneos de bola.pdf")
        
#############################################################################################
#Acerca de
#############################################################################################
def acerca_de():
    print(" Programa: Torneos de bola\n"
          " Version: 1.0\n"
          " Fecha de creacion: \n"
          " Autor del proyecto: Fabian Mata Salas")
    input("<ACEPTAR>")

############################################################################################## 
#Leer archivos
##############################################################################################
try:
    with open('configuracion.txt', 'r') as file:
        i = 0
        for q in file:
            if q == "\n":
                continue
            linea = q
            if i == 0:
                nombre_torneo = linea 
                i += 1
                continue
            if i == 1:
                cantidad_de_equipos = linea
                i += 1
                continue
            if i == 2:
                cantidad_de_clasificaciones_directas = linea
                i += 1
                continue
            if i == 3:
                puntos_por_victoria = linea
                i += 1
                continue
            if i == 4:
                puntos_por_empate = linea
                i += 1
                continue
except FileNotFoundError:
    nombre_equipo = "configuracion.txt"
    with open(nombre_equipo, "w") as file:
              pass

try:
    equipos_data = open("equipos.dat","rb")
except: 
    with open("equipos.dat","wb") as file:
        pass

while True:
    try:
        equipos = pickle.load(equipos_data) 
    except:
        break

equipos_data.close()
try:
    juegos_data = open("juegos.dat","rb")
except: 
    with open("juegos.dat","wb") as file:
        pass

while True:
        contador = 0
        try:
            listas_cargadas = pickle.load(juegos_data)
            juegos, resultados, goleadores = listas_cargadas
        except:
            break

equipos_data.close()







despliegue_principal()