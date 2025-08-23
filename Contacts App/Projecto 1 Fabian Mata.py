#Fabian Mata Salas, Grupo 4. Proyecto #1 de programacion. Tecnologico de Costa Rica

#######################################################################################
#MODULOS
#######################################################################################
import os #para hacer clear de la pantalla
import datetime #para verificar que una fecha sea valida
import PyPDF2 #Herramienta que permite trabajar con pdfs
import re 
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
import sys #Para cerar el programa bien
#######################################################################################
#FUNCIONES GENERALES
#######################################################################################

#A la hora de desplegar manuales, siempre se pone el titulo de lista digital de contactos. Voy a asignarle una variable con el titulo y una funcion para centrarlo.
#Entradas: No aplica
#Salidas: Titulo centrado y un espacio por orden
def titu():
   print("LISTA DIGITAL DE CONTACTOS".center(50), "\n")  #Esta funcion centra el texto en 50 caractertes
#Por simplicidad, esto hace que a la hora de dejar una entrada en blanco, se toma otra entrada que generalmente es de un registro de datos. Esto lo uso para pasar datos en blanco en la seccion de modificar contactos
def pasar(entrada,valor_viejo):      
   if entrada == "":
      return valor_viejo 
   else:
      return entrada
#Verificar que una entrada no se haya dejado en blanco
#Entrada: dato  
#Salida: True si es un espacio vacio o False en otro caso
def no_hay_dato(entrada):
   x = ""
   if x == entrada:
      return True
   return False
#Para mantener una memoria, aqui hay variables con las diferentes listas usadas
areas_mem = []
contactos = [[],[],[],[],[],[],[],[],[]]
num_areas = []
nom_areas = []
tipos_telefono = ("M", "Móvil", "C", "Casa", "T", "Trabajo", "O", "Otro")
area_por_omision = "vacio"
telefono_por_omision = "vacio"
grupos = []
contactos_por_grupo = []
#######################################################################################
#MENU PRINCIPAL
#######################################################################################

#Despliegue de opciones
#Entradas: Comando del usuario
#Salidas: Funcion pedida
def despliegue_principal():
    terminar = False
    opciones = [0,1,2,3,4,5,6,7]
    while True:
        os.system("cls")
        print("\n-------------------------------------------------------------------------------")
        titu()
        print("1. Registrar áreas \n2. Configuración de lista de contactos \n3. Registrar contactos \n4. Administrar grupos de contactos \n5. Lista de contactos \n6. Ayuda \n7. Acerca de \n0. Fin \n") 
        while True:
         try:
            command =  int(input("OPCION: "))
            opciones.index(command)
            match command:
               case 1:
                  despliegue_areas()
                  return
               case 2:
                  despliegue_config_contactos()
                  return
               case 3:
                  despliegue_registro_datos()
                  return
               case 4:
                  despliegue_grupos()
                  return
               case 5:
                  lista_de_contactos()
                  return
               case 6:
                  ayuda()
                  return
               case 7:
                  acerca_de()
                  return
               case 0:
                  input("Gracias por usar la lista digital de contactos, adios! <ACEPTAR> ")
                  os._exit(0)
            break
         except: 
            input("OPCION NO ES PERMITIDA <ACEPTAR> ")
            break

#######################################################################################
#AREAS
#######################################################################################
#Revisar si un area esta registrada en un contacto
#Entradas: area
#Salidas: True o False
def area_en_contacto(variable):
   if variable in contactos[1]:
      return True
   return False

#Agregar area
#Entradas: Area a agregar, Nombre del area, Opcion de aceptar o cancelar
#Salidas: internamente, se agrega a la lista el area. Se devuelve el usuario al menu anterior
def agregar_area():   
   while True:       
       os.system("cls") 
       print("\n-------------------------------------------------------------------------------") 
       titu()
       print("REGISTRAR AREAS: AGREGAR".center(50),"\n")  
       while True: 
         try:     
            num = (input("Area: "))
            if num == "C" or num == "c":
               despliegue_areas()
               return
            num = int(num)
            if num in num_areas:
               print("Area ya fue registrada previamente, favor ingresar otra")
               raise KeyError
            if 0 < num <= 999:
               break
            raise ValueError
         except KeyError:
            pass
         except: 
            print("Favor ingresar un area valida entre 1 y 999")
       while True:
          try:
             name = input("Nombre de area: ")
             if no_hay_dato(name):
                raise ValueError
             if not 1 <= len(name) <= 40:
                raise ValueError
             break
          except:
             print("Favor ingresar un area de entrada valida entre 1 y 40 caracteres")
       while True:
          conf = input ("OPCION   <A> ACEPTAR    <C> CANCELAR ")
          conf = conf.lower()
          if conf == "a":
                area = (num, name)
                areas_mem.append(area)
                num_areas.append(num)
                nom_areas.append(name)
                agregar_area()
                return
          if conf == "c":
             agregar_area()
             return
          input("OPCION NO ES PERMITIDA <ACEPTAR> ")

#Consultar datos sobre un area
#Entradas: area a consultar
#Salidas: nombre del area
def consultar_area():
   while True:
    os.system("cls")
    print("\n-------------------------------------------------------------------------------")
    titu()
    print("REGISTRAR AREAS: CONSULTAR".center(50),"\n")
    while True:
      try:
         area = input("Area a consultar: ")     
         if area == "C" or area == "c":
            despliegue_areas()   
            return  
         area = int(area) 
         digito_lista = num_areas.index(area)
         print("\nNombre del area:", nom_areas[digito_lista])
         break
      except:
         print("EL AREA NO ESTA REGISTRADA: NO SE PUEDE CONSULTAR")
    while True:
         command = input("\nOpcion   <A> ACEPTAR ")
         command = command.lower()
         if command == "a":
            consultar_area()
            break
         else:
            input("OPCION NO ES PERMITIDA <ACEPTAR> ")

#Modificar datos de las areas
#Entradas: Area a modificar del usuario, nuevo nombre por el usuario
#Salidas: Internamente, modifica el nombre asignado a el area
def modificar_area():
   while True:
    os.system("cls")
    print("\n-------------------------------------------------------------------------------")
    titu()
    print("REGISTRAR AREAS: MODIFICAR".center(50),"\n")
    while True:
      try:
         area = input("Area a modificar: ")     
         if area == "C" or area == "c":
            despliegue_areas()   
            return  
         area = int(area) 
         digito_lista = num_areas.index(area)
         print("\nNombre actual del area:", nom_areas[digito_lista], end="       ")
         nuevo_nombre = input("Nuevo nombre: ")
         if no_hay_dato(nuevo_nombre):
            nuevo_nombre = nom_areas[digito_lista]
         if 0 < len(nuevo_nombre) < 41:
            break
         else:
            print("AREA INVALIDA, INGRESAR AREA DE 1 A 40 CARACTERES")
            continue
      except:
         print("Esta area no esta registrada, no se puede modificar")
    while True:
         try:
            command = input("OPCION   <A> ACEPTAR    <C> CANCELAR ")
            command = command.lower()
            if command == "a":
               nom_areas[digito_lista] = nuevo_nombre
               areas_mem[digito_lista] = (area, nuevo_nombre)
               modificar_area()
               return
            if command == "a":
               modificar_area()
               return
            raise ValueError
         except:
            input("OPCION NO ES PERMITIDA <ACEPTAR> ")

#Eliminar areas
#Entradas: area a eliminar
#Salidas: area eliminada internamente
def eliminar_area():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")
      titu()
      print("REGISTRAR AREAS: ELIMINAR".center(50),"\n")
      while True:
         try:
            target = input("Area a eliminar: ")
            if target == "C" or target == "c":
               despliegue_areas()
               return
            target = int(target)
            if target not in num_areas:
               input("Area no ha sido registrada, no se puede eliminar")
               raise ImportError
            digito_lista = num_areas.index(target)
            print("Nombre del area: ", nom_areas[digito_lista])
            try: 
               confirm = input("OPCION   <A> ACEPTAR    <C> CANCELAR " )
               confirm = confirm.lower()
               if confirm == "a":
                 if area_en_contacto(target):
                   print("Este area tiene contactos registrados, no se puede eliminar <ACEPTAR> ")
                   raise KeyError
                 while True:
                     print("¿De verdad deseas borrar el area", areas_mem[digito_lista], "? ", end="")
                     confirm_seguro = input("SI/NO ")
                     confirm_seguro = confirm_seguro.lower()
                     if confirm_seguro == "si":
                        global area_por_omision #En caso de que el area siendo borrada sea la guardada como area por omision, hay que asegurarse de borrarla, que en caso de mi codigo, es que sea equivalente a "vacio"
                        if area_por_omision == num_areas[digito_lista]:
                           area_por_omision = "vacio"
                        del num_areas[digito_lista]
                        del nom_areas[digito_lista]
                        del areas_mem[digito_lista]
                        eliminar_area()
                        return
                     if confirm_seguro == "no":
                        eliminar_area()
                        return
                     print("Favor confirmar si realmente deseas borrar el area")
               if confirm == "c":
                  eliminar_area()
                  return
            except KeyError:
                  pass
            except: 
               input("OPCION NO ES PERMITIDA <ACEPTAR> ")
         except ImportError:
            pass
         except:
            input("INGRESAR AREA VALIDA <ACEPTAR> ")

#Despliegue de opciones para areas
#Entradas: Comando de usuario
#Salidas: Comando recibido
def despliegue_areas():
    while True:
     os.system("cls")     
     print("\n-------------------------------------------------------------------------------")
     titu()
     print("REGISTRAR AREAS".center(50))
     print("\n1. Agregar areas \n2. Consultar areas \n3. Modificar areas \n4. Eliminar areas \n0. Fin")
     while True:
      opciones = [0,1,2,3,4]
      try:
         command=(int(input("OPCION: ")))
         opciones.index(command)
         match command:
            case 0:
               despliegue_principal()
            case 1: 
               agregar_area()
            case 2:
               consultar_area()
            case 3:
               modificar_area() 
            case 4:
               eliminar_area()
         break
      except:
         input("OPCION NO ES PERMITIDA <ACEPTAR> ")
         break
              
#######################################################################################
#Configuracion de lista de contactos
#######################################################################################
#Entradas: Las areas por omision y telefono por omision que el usuario desee
#Salidas: Internamente, se guarda la memoria de estos datos elegidos
#Notas: No venia en las instrucciones dadas, pero añadi la opcion de en cualquier momento salir de la opcion al ingresar C, para evitar problemas si no hubieran areas registradas
def despliegue_config_contactos():
   area_fue_resgistrada = True
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")
      titu()
      print("CONFIGURACION DE LA LISTA DE CONTACTOS".center(50))
      print()
      if area_fue_resgistrada == False:
         print("Area por omision registrada:", num_areas[indice], "-",nom_areas[indice])
         print()      
      if area_fue_resgistrada:
         entrada_area = input("Area por omision: ")   
         if entrada_area == "C" or  entrada_area == "c":
             break
         try:
             entrada_area = int(entrada_area)
         except:
             input("ESTA AREA NO ESTA REGISTRADA, NO SE PUEDE SELECCIONAR <ACEPTAR>")
             continue         
         if entrada_area in num_areas:
            for i in range(len(num_areas)):
               if entrada_area == num_areas[i]:
                  indice = i
            print("                 ", nom_areas[indice])
            area_fue_resgistrada = False
            print()
         else:
            input("ESTA AREA NO ESTA REGISTRADA, NO SE PUEDE SELECCIONAR <ACEPTAR> ")
            continue
      entrada_telefono = input("Tipo de telefono por omision (M: Movil,C: Casa,T: Trabajo,O: Otros): ")
      if entrada_telefono == ("C") or entrada_telefono == "c":
         break
      entrada_telefono = entrada_telefono.upper()
      if entrada_telefono in ("M", "C", "T", "O"):
         pass
      else:
         input("ESTE TIPO DE TELEFONO NO EXISTE, NO SE PUEDE SELECCIONAR <ACEPTAR> ")
         continue
      print()
      while True:
         confirm = input("OPCION   <A> ACEPTAR    <C> CANCELAR " )
         confirm = confirm.lower()
         if confirm == "a":
            global area_por_omision 
            area_por_omision = int(entrada_area)
            global telefono_por_omision 
            telefono_por_omision = entrada_telefono
            break
         if confirm == "c":
            break
         input("OPCION NO ES PERMITIDA <ACEPTAR> ")
      break
   despliegue_principal()

#######################################################################################
#Registrar Contactos
#######################################################################################
#Revisar si un contacto esta en un grupo
#Entradas: Contacto
#Salidas: True o False
def contacto_en_grupo(area,grupo,tupla_grupos):
   for h in range(len(tupla_grupos)):
      if tupla_grupos == []:
         return False
      if area == tupla_grupos[h][0] and grupo == tupla_grupos[h][1]:
         return True
   return False

#Despliegue principal del registro de datos
#Entradas: Opcion a elegir
#Salidas: comando dado
def despliegue_registro_datos():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")      
      titu()
      print("REGISTRAR CONTACTOS".center(50))
      print()
      try: 
       print(
            "1. Agregar contactos\n"
            "2. Consultar contactos\n"
            "3. Modificar contactos\n"
            "4. Eliminar contactos\n"
            "0. Fin\n"
            ''
       )
       entrada = int(input("OPCION: "))
       if entrada in [0,1,2,3,4]:
         match entrada:
            case 0:
               despliegue_principal()
               return
            case 1:
               agregar_contactos()
               return
            case 2:
               consultar_contactos()
               return
            case 3:
               modificar_contactos()
               return
            case 4:
               eliminar_contactos()
       raise ValueError
      except:
         input("OPCION NO ES PERMITIDA <ACEPTAR> ")

#Agregar contactos
#Entradas: Contacto a agregar, y todos sus datos respectivos
#Salidas: Se crea este contacto en la lista contactos
def agregar_contactos():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------") 
      titu()
      print("\nREGISTRAR CONTACTOS: AGREGAR".center(50))
      print()
      while True:
       try: 
          telefono = input("Telefono: ")
          if telefono == "C" or telefono == "c":
             despliegue_registro_datos()
             return
          if telefono == "":
             if telefono_por_omision == "vacio":
                raise KeyError
             telefono = telefono_por_omision
          if not 4 < len(telefono) < 13:
             input("INGRESAR UN NUMERO DE TELEFONO VALIDO <ACEPTAR> ")
             continue
          telefono = int(telefono)
       except:
          input("INGRESAR UN NUMERO DE TELEFONO VALIDO <ACEPTAR> ")
          continue
       break
      while True:
         try:
            area = (input("Area: ",))
            if area == "":
               if area_por_omision == "vacio":
                  raise ValueError
               area = area_por_omision
               print(area, end=" - ")
            area = int(area)
            if areas_mem == []: #Verificar que haya algo en las areas para evitar problemas
               input("NO SE PUEDE CONTINUAR SIN REGISTRAR AL MENOS 1 AREA <ACEPTAR> ")
               despliegue_registro_datos()
               return
            if area not in num_areas:
               input("AREA INVALIDA, NO HA SIDO REGISTRADA <ACEPTAR> ")
               continue
            indice = num_areas.index(area)
            print("     ", nom_areas[indice])
            break
         except:
            input("INGRESAR UN AREA VALIDA <ACEPTAR> ")
            continue
      for i in range(len(contactos[0])):
         if telefono in contactos[0] and area in contactos[1]:
            input("ESTE TELEFONO YA ESTA REGISTRADO, NO SE PUEDE AGREGAR <ACEPTAR> ")
            agregar_contactos()
            return
      while True:
         tipo_telefono = input("Tipo de telefono (M,C,T,O): ")
         if tipo_telefono == "":
            if telefono_por_omision == "vacio":
               input("TIPO DE TELEFONO INVALIDO <ACEPTAR> ")
               continue
            tipo_telefono = telefono_por_omision
         tipo_telefono = tipo_telefono.upper()
         if tipo_telefono not in ("M", "C", "T", "O"):
            input("TIPO DE TELEFONO INVALIDO <ACEPTAR> ")
            continue
         for i in range(len(tipos_telefono)):
            if tipo_telefono == tipos_telefono[i]:
               print("                           ", tipos_telefono[i+1])
         break
      while True:
         nombre_contacto = input("Nombre de contacto: ")
         if 0 < len(nombre_contacto) < 51:
            break
         input ("NOMBRE DE CONTACTO INVALIDO, INGRESAR NOMBRE DE 1 A 50 CARACTERES <ACEPTAR> ")
      while True:
         valido = True
         correo = input("Correo Electronico: ")
         arrobas = correo.count("@")
         if arrobas == 1:
            valido = False
         if " " in correo or valido:
            input("INGRESAR UN CORREO VALIDO <ACEPTAR> ")
            continue
         break
      while True:
         direccion = input("Direccion fisica: ")
         if 0 < len(direccion) < 81:
            break
         input ("DIRECCION INVALIDA, INGRESAR DIRECCION DE 1 A 80 CARACTERES <ACEPTAR> ")
      while True:
         try:
            fecha = input("Fecha de nacimiento (DD/MM/AAAA) ")
            try: 
               if int(fecha) == 0:
                  break
            except:
               pass
            if fecha[2] != "/" or fecha[5] != "/":
               confirmar = input ("Fecha incompleta, desea que la fecha asignada sea 0? (SI/NO) ") #Si la fecha no se da en el formato correcto, se asume que no se tienen los datos
               confirmar = confirmar.lower()
               try:
                  match confirmar:
                     case "si":
                        fecha = 0
                        break
                     case "no":
                        continue
                     case _:
                        input("Opcion no es permitida <ACEPTAR> ")
                        raise  ValueError
               except:
                  continue
            try: #Verificar si la fecha existe
               if fecha[-4:] == "0000":
                  datetime.date(2000, int(fecha[3:5]), int(fecha[:2]))         
               else:
                  datetime.date(int(fecha[6:]),int(fecha[3:5]),int(fecha[:2])) 
            except: 
               input("INGRESAR UNA FECHA VALIDA <ACEPTAR> ")
               continue
            break
         except: input("INGRESAR UNA FECHA VALIDA <ACEPTAR> ")
      while True:
         pasatiempos = input("Pasatiempos: ")
         if 0 <= len(pasatiempos) < 61:
            break
         input ("INGRESAR UN DATO EN EL LIMITE DE CARACTERES (0-60) <ACEPTAR> ")
      while True:
         notas = input("Notas: ")
         if 0<= len(notas) < 61:
            break
         input("INGRESAR UN DATO EN EL LIMITE DE CARACTERES (0,60) <ACEPTAR> ")
      print()
      while True:
         confirmacion = input("OPCION: <A>ACEPTAR <C>CANCELAR ")
         confirmacion = confirmacion.lower()
         match confirmacion:
            case "a":
               contactos[0].append(telefono)
               contactos[1].append(area)
               contactos[2].append(tipo_telefono)
               contactos[3].append(nombre_contacto)
               contactos[4].append(correo)
               contactos[5].append(direccion)
               contactos[6].append(fecha)
               contactos[7].append(pasatiempos)
               contactos[8].append(notas)
               break
            case "c":
               break
            case _:
               input("INGRESAR UNA OPCION VALIDA <ACEPTAR> ")
               
#Consultar contactos
#Entradas: contacto a consultar
#Salidas: Muestra el contacto
def consultar_contactos():
      while True:
         os.system("cls")
         print("\n-------------------------------------------------------------------------------") 
         titu()
         print("\nREGISTRAR CONTACTOS: CONSULTAR".center(50))
         print()
         if contactos[0] == []:
            input("NO HAY CONTACTOS REGISTRADOS <ACEPTAR> ")
            despliegue_registro_datos()
            return
         print("Datos del telefono a consultar:\n")
         try:
            telefono = input("Numero de telefono: ")            
            if telefono == "C" or telefono == "c":
               despliegue_registro_datos()
               return
            area = (input("Area: "))
            if area == "":
               if area_por_omision == "vacio":
                  raise ValueError
               area = area_por_omision
            telefono = int(telefono)
            area = int(area)
            for i in range(len(contactos[1])):
               listo = False
               if area == contactos[1][i] and  telefono == contactos[0][i]:
                  for g in range(len(num_areas)):
                     if area == num_areas[g]:
                        print("     ", nom_areas[g])
                  print("Tipo de telefono:", end=" ")
                  for q in range(len(tipos_telefono)):
                     if contactos[2][i] == tipos_telefono[q]:
                        print(tipos_telefono[q+1])
                  print("Nombre de contacto:", contactos[3][i])
                  print("Correo electronico:", contactos[4][i])
                  print("Direccion fisica:", contactos[5][i])
                  print("Fecha de nacimiento:", contactos[6][i])
                  print("Pasatiempos", contactos[7][i])
                  print("Notas:", contactos[8][i])
                  listo = True
                  input("<ACEPTAR> ")
               if listo:
                  break
         except:
            input("ESTE CONTACTO NO ESTA REGISTRADO, NO SE PUEDE CONSULTAR <ACEPTAR> ")
            continue
         if listo:
            continue
         input("ESTE CONTACTO NO ESTA REGISTRADO, NO SE PUEDE CONSULTAR <ACEPTAR> ")

#Modificar contactos
#Entradas: dato a modificar
#Salidas: cambios hechos al contacto
def modificar_contactos():
      while True:
         os.system("cls")
         print("\n-------------------------------------------------------------------------------") 
         titu()
         print("\nREGISTRAR CONTACTOS: MODIFICAR".center(50))
         print()
         if contactos[0] == []:
            input("NO HAY CONTACTOS REGISTRADOS <ACEPTAR> ")
            despliegue_registro_datos()
            return
         print("Datos del telefono a modificar:\n")
         try:
            telefono = input("Numero de telefono: ")            
            if telefono == "C" or telefono == "c":
               despliegue_registro_datos()
               return
            area = (input("Area: "))
            if area == "":
               if area_por_omision == "vacio":
                  raise ValueError
               else:
                  area = area_por_omision
            else:
               area = int(area)
            telefono = int(telefono)
            if telefono not in contactos[0] or area not in contactos[1]:
               input("TELEFONO NO ESTA REGISTRADO, NO SE PUEDE ELIMINAR <ACEPTAR> ")
               continue
            for i in range(len(contactos[1])):
               if area == contactos[1][i] and  telefono == contactos[0][i]:  
                  no_existe = False
                  for g in range(len(num_areas)):
                     if area == num_areas[g]:
                        print("     ", nom_areas[g])
                  while True:
                     print("Tipo de telefono:", end=" ")
                     for q in range(len(tipos_telefono)):
                        if contactos[2][i] == tipos_telefono[q]:
                           print(tipos_telefono[q+1], end="")                     
                     print(" - Nuevo valor(M,C,T,O): ", end=" ")
                     tipo_telefono = input()
                     tipo_telefono = pasar(tipo_telefono,contactos[2][i])
                     tipo_telefono = tipo_telefono.upper()
                     if tipo_telefono not in ("M", "C", "T", "O"):
                        input("TIPO DE TELEFONO INVALIDO <ACEPTAR> ")
                        continue
                     for z in range(len(tipos_telefono)):
                        if tipo_telefono == tipos_telefono[z]:
                           print("                                                ", tipos_telefono[z+1])   
                     break               
                  while True:
                      print("Nombre de contacto:", contactos[3][i], end=" ")
                      print(" - Nuevo valor: ", end=" ")                      
                      nombre_contacto = input()
                      nombre_contacto = pasar(nombre_contacto,contactos[3][i])
                      if 0 < len(nombre_contacto) < 51:
                         break
                      input ("NOMBRE DE CONTACTO INVALIDO, INGRESAR NOMBRE DE 1 A 50 CARACTERES <ACEPTAR> ")      
                  while True:
                     print("Correo electronico:", contactos[4][i], end=" ")
                     print(" - Nuevo valor: ", end=" ")                     
                     valido = True
                     correo = input()
                     correo = pasar(correo,contactos[4][i])
                     arrobas = correo.count("@")
                     if arrobas == 1:
                        valido = False
                     if " " in correo or valido:
                        input("INGRESAR UN CORREO VALIDO <ACEPTAR> ")
                        continue
                     break                  
                  while True:
                           print("Direccion fisica:", contactos[5][i], end=" ")
                           print(" - Nuevo valor: ", end=" ")
                           direccion = input()
                           direccion = pasar(direccion,contactos[5][i])
                           if 0 < len(direccion) < 81:
                              break
                           input ("DIRECCION INVALIDA, INGRESAR DIRECCION DE 1 A 80 CARACTERES <ACEPTAR> ")                  
                  while True:
                     try:
                        print("Fecha de nacimiento:", contactos[6][i], end=" ")
                        print(" - Nuevo valor(DD/MM/AAAA): ", end=" ")                        
                        fecha = input()
                        fecha = pasar(fecha,contactos[6][i])
                        try: 
                           if int(fecha) == 0:
                              break
                        except:
                           pass
                        if fecha[2] != "/" or fecha[5] != "/":
                           confirmar = input ("Fecha incompleta, desea que la fecha asignada sea 0? (SI/NO) ") #Si la fecha no se da en el formato correcto, se asume que no se tienen todos los datos
                           confirmar = confirmar.lower()
                           try:
                              match confirmar:
                                 case "si":
                                    fecha = 0
                                    break
                                 case "no":
                                    continue
                                 case _:
                                    input("Opcion no es permitida <ACEPTAR> ")
                                    raise  ValueError
                           except:
                              continue
                        try: #Verificar si la fecha existe
                           if fecha[-4:] == "0000":
                              datetime.date(2000, int(fecha[3:5]), int(fecha[:2]))         
                           else:
                              datetime.date(int(fecha[6:]),int(fecha[3:5]),int(fecha[:2])) 
                        except: 
                           input("INGRESAR UNA FECHA VALIDA <ACEPTAR> ")
                           continue
                        break
                     except: input("INGRESAR UNA FECHA VALIDA <ACEPTAR> ")
                  while True:
                     print("Pasatiempos", contactos[7][i], end=" ")
                     print(" - Nuevo valor: ", end=" ")                     
                     pasatiempos = input()
                     pasatiempos = pasar(pasatiempos,contactos[7][i])
                     if 0 <= len(pasatiempos) < 61:
                        break
                     input ("INGRESAR UN DATO EN EL LIMITE DE CARACTERES (0-60) <ACEPTAR> ")
                  while True:
                     print("Notas:", contactos[8][i], end=" ")
                     print(" - Nuevo valor: ", end=" ")                     
                     notas = input()
                     notas = pasar(notas,contactos[8][i])
                     if 0<= len(notas) < 61:
                        break
                     input("INGRESAR UN DATO EN EL LIMITE DE CARACTERES (0,60) <ACEPTAR> ")
                  print()
                  while True:
                     confirmacion = input("OPCION: <A>ACEPTAR <C>CANCELAR ")
                     confirmacion = confirmacion.lower()
                     match confirmacion:
                        case "a":
                           contactos[2][i] = (tipo_telefono)
                           contactos[3][i] = (nombre_contacto)
                           contactos[4][i] = (correo)
                           contactos[5][i] = (direccion)
                           contactos[6][i] = (fecha)
                           contactos[7][i] = (pasatiempos)
                           contactos[8][i] = (notas)
                           break
                        case "c":
                           break
                        case _:
                           input("INGRESAR UNA OPCION VALIDA <ACEPTAR> ")         
         except:        
            input("ESTE CONTACTO NO ESTA REGISTRADO, NO SE PUEDE ELIMINAR <ACEPTAR> ")  
#Eliminar contactos
#Entradas: Dato a eliminar
#Salidas: El dato sera eliminado de la memoria
def eliminar_contactos():
      while True:
         os.system("cls")
         print("\n-------------------------------------------------------------------------------") 
         titu()
         print("\nREGISTRAR CONTACTOS: ELIMINAR".center(50))
         print()
         if contactos[0] == []:
            input("NO HAY CONTACTOS REGISTRADOS <ACEPTAR> ")
            despliegue_registro_datos()
            return
         print("Datos del telefono a eliminar:\n")
         try:
            telefono = input("Numero de telefono: ")            
            if telefono == "C" or telefono == "c":
               despliegue_registro_datos()
               return
            area = (input("Area: "))
            if area == "":
               if area_por_omision == "vacio":
                  raise ValueError
               else:
                  area = area_por_omision
            area = int(area)
            telefono = int(telefono)
            if telefono not in contactos[0] or area not in contactos[1]:
               input("TELEFONO NO ESTA REGISTRADO, NO SE PUEDE ELIMINAR <ACEPTAR> ")
               continue
            for i in range(len(contactos[1])):
               if area == contactos[1][i] and  telefono == contactos[0][i]:
                  if contacto_en_grupo(contactos[0][i], contactos[1][i], contactos_por_grupo):
                     eliminar_contactos()
                     return  
                  for g in range(len(num_areas)):
                     if area == num_areas[g]:
                        print("     ", nom_areas[g])   
                  while True:
                     confirmar = input("OPCION <A>ACEPTAR <C>CANCELAR ")
                     confirmar = confirmar.lower()
                     match confirmar:
                        case "a":
                           print("De verdad quieres borrar el contacto'", contactos[3][i], "',",  contactos[0][i], "[SI/NO]", end=" ")
                           confirmar_2 = input()
                           confirmar_2 = confirmar_2.lower()
                           match confirmar_2:
                              case "si":
                                 for q in range(9):
                                    del contactos[q][i]
                                 break
                              case "no":
                                 break
                              case _:
                                 input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                        case "c":
                           break
                        case _:
                           input("OPCION NO ES PERMITIDA <ACEPTAR> ")
         except:
            input("ESTE CONTACTO NO ESTA REGISTRADO, NO SE PUEDE ELIMINAR <ACEPTAR> ")
   
#######################################################################################
#ADMINISTRAR GRUPOS DE CONTACTOS
#######################################################################################
#Despliegue principal de opciones sobre la administracion de grupos
#Entradas: comando
#Salidas: ejecucion del comando
def despliegue_grupos():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")
      titu()
      print("ADMINISTRAR GRUPOS DE CONTACTOS".center(50))
      print()
      try: 
       print(
            "1. Agregar grupos\n"
            "2. Agregar contactos a los grupos\n"
            "3. Modificar grupos\n"
            "4. Eliminar grupos\n"
            "5. Eliminar contactos de los grupos\n"
            "0. Fin\n"
            ''
       )
       entrada = int(input("OPCION: "))
       match entrada:
         case 0:
            despliegue_principal()
            return
         case 1:
             agregar_grupo()
             return
         case 2:
             agregar_contacto_a_grupo()
             return
         case 3:
             modificar_grupo()
             return
         case 4:
             eliminar_grupo()
             return
         case 5:
             eliminar_contacto_de_grupo()
             return
      except:
         input("OPCION NO ES PERMITIDA <ACEPTAR> ")

#Agregar Grupos
#Entradas: Grupo a agregar
#Salidas: El grupo es agregado internamente
def agregar_grupo():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")
      titu()
      print("ADMINISTRAR GRUPOS DE CONTACTOS: AGREGAR GRUPO".center(50))
      print()
      nombre = input("Nombre del grupo (sensible a mayúsculas y minúsculas): ")
      if nombre in ["C","c"]:
         despliegue_grupos()
         return 
      if nombre in grupos:
         input("ESTE GRUPO YA ESTA REGISTRADO, NO SE PUEDE AGREGAR <ACEPTAR> ")
         continue
      if not 0<len(nombre)<41 or nombre == "":
         input("NOMBRE INVALIDO, INGRESAR NOMBRE DE 1 A 40 CARACTERES")
      while True:
         confirm = input("OPCION: <A>ACEPTAR <C>CANCELAR ")
         confirm = confirm.lower()
         match confirm:
            case "a":
               grupos.append(nombre)
               contactos_por_grupo.append([])
               break
            case "c":
               break
            case _:
               input("OPCION NO ES PERMITIDA <ACEPTAR> ")

#Agregar contactos a grupos
#Entradas: Contacto a añadir
#Salidas: se le guardan los contactos al grupo respectivo
def agregar_contacto_a_grupo():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")
      titu()
      print("ADMINISTRAR GRUPOS DE CONTACTOS: AGENDAR CONTACTOS A GRUPOS".center(50))
      print()
      if grupos == []:
         input("AGREGAR ALGUN GRUPO PARA CONTINUAR <ACEPTAR> ")
         despliegue_grupos()
         return
      nombre = input("Nombre del grupo (sensible a mayúsculas y minúsculas): ")
      if nombre in ["C","c"]:
         despliegue_grupos()
         return       
      if nombre not in grupos:
         input("ESTE GRUPO NO EXISTE, NO PUEDE AGENDARLE CONTACTOS <ACEPTAR> ")
         continue
      print()  
      while True:
         try:
            os.system("cls")
            print("\n-------------------------------------------------------------------------------")
            titu()
            print("ADMINISTRAR GRUPOS DE CONTACTOS: AGENDAR CONTACTOS A GRUPOS".center(50))
            print()
            print("[<C>REGRESAR] GRUPO EN CUESTION:", nombre)
            if contactos[0] == []:
               input("AGREGAR ALGUN CONTACTO PARA CONTINUAR <ACEPTAR> ")
               despliegue_grupos()
               return
            telefono = input("Numero de telefono: ")      
            if telefono in ["C","c"]:
               break
            area = (input("Area: "))
            if area == "":
               if area_por_omision == "vacio":
                  raise ValueError
               area = area_por_omision
            telefono = int(telefono)
            area = int(area)
            if not telefono in contactos[0] or not area in contactos[1]:
               input("CONTACTO NO ESTA REGISTRADO, NO SE PUEDE AGREGAR <ACEPTAR> ")
            for i in range(len(contactos[1])):
               if area == contactos[1][i] and telefono == contactos[0][i]:
                  ya_se_registro = False
                  for g in range(len(num_areas)):
                     if area == num_areas[g]:
                        print("     ", nom_areas[g])                  
                  print("Nombre del contacto:", contactos[3][i])
                  print()
                  for a in range(len(grupos)):
                     if nombre == grupos[a]:
                        for q in range(len(contactos_por_grupo[a])):
                           if contactos_por_grupo[a] != []:
                              if telefono == contactos_por_grupo[a][q][0] and area == contactos_por_grupo[a][q][1]:
                                 input("CONTACTO YA ESTA EN ESTE GRUPO, NO SE PUEDE VOLVER A REGISTRAR <ACEPTAR> ")
                                 ya_se_registro = True
                        if ya_se_registro:
                           break
                  if ya_se_registro:
                     break
                  while True:
                     confirmar = input("OPCION <A>ACEPTAR <C>CANCELAR ")
                     confirmar = confirmar.lower()
                     match confirmar:
                        case "a":
                           for i in range(len(grupos)):
                              if nombre == grupos[i]:
                                 contactos_por_grupo[i].append((telefono,area))
                                 break
                           break     
                        case "c":
                           break
                        case _:
                           input("OPCION NO ES PERMITIDA <ACEPTAR> ")
         except: 
            input("CONTACTO NO ESTA REGISTRADO, NO SE PUEDE AGREGAR <ACEPTAR> ")
                        
#Modificar grupos de contactos
#Entrada: Grupo a modificar, Cambio al grupo
#Salidas: Grupo se ve modificado
#Notas: En caso de que el nombre nuevo del grupo ya exista, se le presenta la opcion al usuario de combinar los grupos.
def modificar_grupo():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")
      titu()
      print("ADMINISTRAR GRUPOS DE CONTACTOS: MODIFICAR GRUPOS".center(50))
      print()
      if grupos == []:
         input("AGREGAR ALGUN GRUPO PARA CONTINUAR <ACEPTAR> ")
         despliegue_grupos()
         return
      nombre = input("Nombre del grupo (sensible a mayúsculas y minúsculas): ")
      if nombre in ["C","c"]:
         despliegue_grupos()
         return       
      if nombre not in grupos:
         input("ESTE GRUPO NO EXISTE, NO SE PUEDE MODIFICAR <ACEPTAR> ")
         continue
      print()  
      while True:
         grupo_repetido = False
         os.system("cls")
         print("\n-------------------------------------------------------------------------------")
         titu()
         print("ADMINISTRAR GRUPOS DE CONTACTOS: MODIFICAR GRUPOS".center(50))
         print()
         print("GRUPO EN CUESTION:", nombre)
         print()
         nuevo_nombre = input("Nuevo nombre: ")
         if nuevo_nombre in grupos:
            input("GRUPO YA EXISTE, EN CASO DE ACEPTAR LA OPERACION LOS CONTACTOS SE LE AGREGARAN <ACEPTAR> ")
            grupo_repetido = True
         if not 0 < len(nuevo_nombre) < 41:
            input("INGRESAR NOMBRE VALIDO PARA EL GRUPO (1-40 CARACTERES) <ACEPTAR> ")
            continue
         while True:
           confirmar = input("OPCION <A>ACEPTAR <C>CANCELAR ")
           confirmar = confirmar.lower()
           match confirmar:
              case "a":
                 if grupo_repetido:
                    listo = False
                    for i in range(len(grupos)):
                     if listo:
                           break                       
                     if nuevo_nombre == grupos[i]:
                          for q in range(len(grupos)):
                             if listo:
                                break                             
                             if nombre == grupos[q]:
                                 for contacto in contactos_por_grupo[q]:
                                    if contacto in contactos_por_grupo[i]:
                                       pass
                                    else:
                                       contactos_por_grupo[i].append(contacto)
                                 del grupos[q]
                                 del contactos_por_grupo[q]
                                 listo = True
                                 break
                    break
                 else:
                    for z in range(len(grupos)):
                       if nombre == grupos[z]:
                          grupos[z] = nuevo_nombre
                    break      
              case "c":
                 break
              case _:
                 input("OPCION NO ES PERMITIDA <ACEPTAR> ")
         break
                                      
#Eliminar grupos de contactos
#Entradas: Grupo a eliminar
#Salidas: El grupo es eliminado
def eliminar_grupo():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")
      titu()
      print("ADMINISTRAR GRUPOS DE CONTACTOS: ELIMINAR GRUPOS".center(50))
      print()
      if grupos == []:
         input("AGREGAR ALGUN GRUPO PARA CONTINUAR <ACEPTAR> ")
         despliegue_grupos()
         return
      nombre = input("Nombre del grupo (sensible a mayúsculas y minúsculas): ")
      if nombre in ["C","c"]:
         despliegue_grupos()
         return       
      if nombre not in grupos:
         input("ESTE GRUPO NO EXISTE, NO SE PUEDE ELIMINAR <ACEPTAR> ")
         continue
      print()    
      while True:
         confirmar = input("OPCION <A>ACEPTAR <C>CANCELAR ")
         confirmar = confirmar.lower()
         match confirmar:
            case "a":
               print("De verdad quieres borrar el grupo", nombre,"? [SI/NO]", end=" ")
               confirmar_2 = input()
               confirmar_2 = confirmar_2.lower()
               match confirmar_2:
                  case "si":
                     for i in range(len(grupos)):
                      if nombre == grupos[i]:
                         del grupos[i]
                         del contactos_por_grupo[i]
                         break
                     break
                  case "no":
                     break
                  case _:
                     input("OPCION NO ES PERMITIDA <ACEPTAR> ")
            case "c":
               break
            case _:
               input("OPCION NO ES PERMITIDA <ACEPTAR> ")

#Eliminar contactos de un grupo
#Entradas: Grupo en cuestion, contacto a eliminar
#Salidas: el contacto es eliminado del grupo
def eliminar_contacto_de_grupo():
   while True:
      try:
         listo = False
         os.system("cls")
         print("\n-------------------------------------------------------------------------------")
         titu()
         print("ADMINISTRAR GRUPOS DE CONTACTOS: ELIMINAR CONTACTOS DE GRUPOS".center(50))
         print()
         if grupos == []:
            input("AGREGAR ALGUN GRUPO PARA CONTINUAR <ACEPTAR> ")
            despliegue_grupos()
            return            
         nombre = input("Nombre del grupo (sensible a mayúsculas y minúsculas): ")
         if nombre in ["C","c"]:
            despliegue_grupos()
            return       
         if nombre not in grupos:
            input("ESTE GRUPO NO EXISTE, NO SE PUEDEN ELIMINARLE CONTACTOS <ACEPTAR> ")
            continue
         for i in range(len(grupos)):
            if nombre == grupos[i]:
               if contactos_por_grupo[i] == []:
                  input("AGREGAR ALGUN CONTACTO AL GRUPO PARA CONTINUAR <ACEPTAR> ")
                  despliegue_grupos()
                  return
         print()  
         while True:
            if listo:
               break
            listo = False
            while True:
               if listo:
                  break
               os.system("cls")
               print("\n-------------------------------------------------------------------------------")
               titu()
               print("ADMINISTRAR GRUPOS DE CONTACTOS: ELIMINAR CONTACTOS DE GRUPOS".center(50))
               print()
               print("GRUPO EN CUESTION:", nombre)
               print()       
               for i in range(len(grupos)):
                  if grupos[i] == nombre:    
                     if contactos_por_grupo[i] == []:
                        input("ESTE GRUPO NO TIENE CONTACTOS REGISTRADOS <ACEPTAR> ")
                        listo = True
                        break                      
               if listo:
                  break                
               telefono = input("Numero de telefono: ")      
               if telefono in ["C","c"]:
                  listo = True
                  break
               area = (input("Area: "))
               if area == "":
                  if area_por_omision == "vacio":
                        raise ValueError
                  area = area_por_omision
               telefono = int(telefono)
               area = int(area)
               esta = True
               for i in range(len(grupos)):
                  if grupos[i] != nombre:
                     continue  
                  for g in range(len(contactos[1])):
                     if area == contactos[1][g] and telefono == contactos[0][g]:
                        for f in range(len(num_areas)):
                           if area == num_areas[f]:
                              print("     ", nom_areas[f])     
                              nombre_del_area = nom_areas[f]             
                        print("Nombre del contacto:", contactos[3][g])
                        print()  
                        for q in range(len(contactos_por_grupo[i])):
                           if contactos_por_grupo[i] != []:
                              if telefono == contactos_por_grupo[i][q][0] and area == contactos_por_grupo[i][q][1]:
                                 esta = False
                        if esta:
                           break
                  if esta:
                     input("CONTACTO NO ESTA EN ESTE GRUPO, NO SE PUEDE ELIMINAR <ACEPTAR> ")
                     break
                  while True:
                     opcion = input("OPCION <A>ACEPTAR <C>CANCELAR ")
                     opcion = opcion.lower()
                     match opcion:
                        case "a":
                           print("De verdad deseas borrar el contacto", nombre_del_area, "del grupo", nombre,"? [SI/NO]", end=" ")
                           confirmar = input()
                           confirmar = confirmar.lower()
                           match confirmar:
                              case "si":
                                 for z in range(len(contactos_por_grupo[i])):
                                    if contactos_por_grupo[i][z] == (telefono,area):
                                       del contactos_por_grupo[i][z]
                                       break
                                 break
                              case "no":
                                 break
                              case _:
                                 input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                        case "c":
                           break
                        case _:
                           input("OPCION NO ES PERMITIDA <ACEPTAR> ")
                  break
            if listo:
               break
      except:
         input("INGRESAR UN CONTACTO VALIDO <ACEPTAR> ")

#######################################################################################
#LISTA DE CONTACTOS
#######################################################################################
#Proceso de filtracion usando re
#Entradas: filtro empleado, lista con instancias de las que se quiere filtrar
#Salidas: lista filtarada
#Notas: Los diferentes metodos y filtros estan explicados en el manual de usuario. En caso de que el filtro no cumpla con ningun requerimiento, se asume que no hay filtro, asi que se devuelve la lista dada.
#Advertencia: Esta funcion emplea metodos y cosas con las que soy muy poco familiar, entonces puede ser infeciciente, aunque no le he encontrado ningun error de funcionamiento, pero cada parte del metodo esta explicada en amplitud por lo mismo
def filtracion(filtro,instancias):
   
   filtro = str(filtro)

   for i in range(len(instancias)): 
        instancias[i] = str(instancias[i]) #Convierto el filtro y lista a strings para evitar problemas con integros

   resultado_filtrado = []

   if filtro.startswith("%") and filtro.endswith("%"): 
      if "%" in filtro[1:-1]: #Revisa si estamos trabajando con el formato %entrada %entrada2% (En realidad tambien deberia servir con %entrada%entrada2%, pero no lo vi como un problema)
         filtro = filtro[1:-1]
         input1, input2 = filtro.split("%")
         patron = re.escape(input1) + ".*" + re.escape(input2)  #Simbologia y uso explicados mas abajo
         for text in instancias: #la logica usada es iterar por cada caso en instancias para comparar
            if re.search(patron,text,re.IGNORECASE):
               resultado_filtrado.append(text)
      else: #En caso de estar trabajando en formato %entrada%
         patron = re.escape(filtro[1:-1]) #re.escape hace que cualquier simbolo especial no cause errores en el proceso de filtracion, ya que varios simboloz tienen significados relevantes para re
         for text in instancias:
            if re.search(patron,text,re.IGNORECASE):  #re.search es un boleano que revisa si el patron se cumple en el texto, la flag re.IGNORECASE ignora mayusculas o minusculas
               resultado_filtrado.append(text)
   elif filtro.startswith("%"):
      patron = re.escape(filtro[1:]) + "$" #con re, el $ simboliza el fin de un string
      for text in instancias:
        if re.search(patron,text,re.IGNORECASE):
           resultado_filtrado.append(text)
   elif filtro.endswith("%"):
      patron = "^" + re.escape(filtro[:-1])  #con re, el ^ simboliza que trabajamos el inicio de un string
      for text in instancias:
         if re.search(patron,text,re.IGNORECASE):
            resultado_filtrado.append(text)
   elif "%" in filtro:
      input1, input2 = filtro.split("%")
      patron = "^" + re.escape(input1) + ".*" + re.escape(input2) + "$" #El . en re intenta comprar entre dos caracteres, y el * intenta comparar por zero o mas casos de cualquier caracter. Combinarlos como .* permite revisar por zero o mas ocurrencias de cualquier caracter, funcionalmente permitiendo ignorar todo por medio de dos cosas para mi uso en este caso
      for text in instancias:
         if re.search(patron,text,re.IGNORECASE):
            resultado_filtrado.append(text)
   else:
      return instancias
     
   return resultado_filtrado

#Crear un pdf
#Entradas: string con pdf a crear
#Salidas: pdf creado
def crear_pdf (texto):
    c = canvas.Canvas("Lista de contactos", pagesize=letter)
    c.setFont("Helvetica", 10)
    y = 750 
    lineas = texto.split("\n")
    for linea in lineas:
        if y < 50:
           c.showpage()
           y = 750
        c.drawString(100, y, linea) 
        y -= 20  
    c.save()

#Crea un pdf con una lista de contactos, utiliza filtros para buscar los tipos de datos especificos
#Entradas: Filtros para revisar cada parte del contacto 
#Salidas: Se crea un pdf con los datos registrados
def lista_de_contactos():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")      
      titu()
      print("LISTA DE CONTACTOS".center(50))
      print()
      if contactos[0] == []:
         input("REGISTRAR ALGUN CONTACTO PARA CONTINUAR <ACEPTAR> ")
         despliegue_principal()
         break
      #Variables usadas:
      nombre_contacto = []
      numero_telefono = []
      area = []
      tipo_telefono = []
      correo = []
      direccion = []
      fecha_nacimiento = []
      pasatiempo = []
      notas = []
      #Filtros
      print("AVISO: FILTROS EN FORMATO INVALIDO NO SURGIRAN EFECTO\n")
      filtro_nombre_contacto = input("Nombre de telefono: ")

      if filtro_nombre_contacto in ["c","C"]:
         despliegue_principal()
         return
      if filtro_nombre_contacto.lower() == "todos":
         pass

      filtro_telefono = input("Telefono: ")
      filtro_area = input("Area: ")
      filtro_fecha_nacimiento = input("Fecha de nacimiento: ")
      filtro_pasatiempos = input("Pasatiempos: ")
      filtro_grupo = input("Grupo: ")

      nombre_contacto_f = filtracion(filtro_nombre_contacto,contactos[3]) #f de filtrado
      numero_telefono_f = filtracion(filtro_telefono,contactos[0])
      area_f = filtracion(filtro_area, contactos[1])
      fecha_nacimiento_f = filtracion(filtro_fecha_nacimiento, contactos[6])
      pasatiempo_f = filtracion(filtro_pasatiempos, contactos[7])
      grupos_f = filtracion(filtro_grupo,grupos)
      
      

      for i in range(len(contactos[0])):
         if str(contactos[3][i]) in nombre_contacto_f and str(contactos[0][i]) in numero_telefono_f and str(contactos[1][i]) in area_f and str(contactos[6][i]) in fecha_nacimiento_f and str(contactos[7][i]) in pasatiempo_f:
            nombre_contacto.append(str(contactos[3][i]))
            numero_telefono.append(str(contactos[0][i]))
            area.append(str(contactos[1][i]))
            tipo_telefono.append(str(contactos[2][i]))
            correo.append(str(contactos[4][i]))
            direccion.append(str(contactos[5][i]))
            fecha_nacimiento.append(str(contactos[6][i]))
            pasatiempo.append(str(contactos[7][i]))
            notas.append(str(contactos[8][i]))
      
      #Las siguientes lineas ya son la combinacion de texto que sera convertida
      espacio = len("Filtros empleados: ")
      x = " " * espacio

      texto_final = (f"Filtros empleados: Nombre de telefono: {filtro_nombre_contacto}\n"
                         f"{x}      Telefono: {filtro_telefono}\n"
                         f"{x}      Area: {filtro_area}\n"
                         f"{x}      Fecha de nacimiento: {filtro_fecha_nacimiento}\n"
                         f"{x}      Pasatiempos: {filtro_pasatiempos}\n"
                         f"{x}      Grupo: {filtro_grupo}\n")
      
      for i in range(len(nombre_contacto)):
         texto_final += ("\n"
                         f"Nombre de contacto: {nombre_contacto[i]}\n"
                         f"Numero de telefono: {numero_telefono[i]}\n"
                         f"Area: {area[i]}\n"
                         f"Tipo_telefono: {tipo_telefono[i]}\n"
                         f"Correo: {correo[i]}\n"
                         f"Direccion: {direccion[i]}\n"
                         f"Fecha de nacimiento: {fecha_nacimiento[i]}\n"
                         f"Pasatiempos: {pasatiempo[i]}\n"
                         f"Notas: {notas[i]}\n"

         )
      
      while True:
         confirmar = input("OPCION <A>ACEPTAR <C>CANCELAR <P>PREVISTA DEL DOCUMENTO ")
         match confirmar.lower():
            case "a":
               crear_pdf(texto_final)
               break
            case "c":
               break
            case "p":
               print(texto_final)
               input("<ACEPTAR>")


#######################################################################################
#AYUDA
#######################################################################################
def open_pdf(pdf_filename): 
    #Conseguir el path de este script
    script_dir = os.path.dirname(os.path.abspath(__file__))
    
    #Conseguir el path de este pdf
    pdf_path = os.path.join(script_dir, pdf_filename)
    
    #Abrir el pdf
    os.startfile(pdf_path)

def ayuda():
    open_pdf("Manual_de_usuario_lista_digital_de_contactos.pdf")   

#######################################################################################
#ACERCA DE
#######################################################################################
def acerca_de():
   while True:
      os.system("cls")
      print("\n-------------------------------------------------------------------------------")      
      titu()
      print("ACERCA DE".center(50))
      print()   
      print("Autor: Fabian Mata Salas\n"
            "Fecha de publicacion: 5/5/2024\n"
            "Nombre de programa: Lista digital de contactos version 1.0.0")
      input("<ACEPTAR>")
      despliegue_principal()
      return
   
despliegue_principal()