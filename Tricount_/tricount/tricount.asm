;                           TRICOUNT.ASM
;       Integrantes: Efraim Cuevas Aguilar 2024109746
;                    Fabián Mata Salas     2024188730
;
;
;

%include "io.mac"

.DATA
;//////////LINEAS DE MENUS//////////
;//////////INTERFAZ PRINCIPAL//////////
    title               db '------------------------------------TRICOUNT------------------------------------',0
    interface_info_1    db 'Autores: Fabian Mata Salas - Efraim Cuevas Aguilar -   Tecnologico de Costa Rica',0
    interface_info_2    db "Semestre II 2024, IC-3101 Arquitectura de computadores, Campus de Cartago",0 
    transactions_msg    db 0x09, 0x09, 0x09, "Transacciones realizadas: ",0
    friends_msg         db 0x09, 0x09, 0x09, "Cantidad de integrantes:  ",0
    total_expense_msg   db 0x09, 0x09, 0x09, "Gastos totales:  ",0
    main_menu_opt1      db 0x09, 0x09, "1. Lista de amigos", 0
    main_menu_opt2      db 0x09, 0x09, "2. Información de gastos", 0
    main_menu_opt3      db 0x09, 0x09, "3. Conciliar gasto", 0
    main_menu_opt4      db 0x09, 0x09, "4. Conciliar todos los gastos", 0
    main_menu_opt5      db 0x09, 0x09, "5. Añadir gasto", 0
    main_menu_opt6      db 0x09, 0x09, "6. Salir", 0
    wrong_option        db 0x09, "Elija una opción válida"
    ask_choice          db 0x09, 0x09, 0x09, "Ingresar una opcion: ",0 

;//////////INTERFAZ DE INFORMACIÓN DE AMIGOS //////////
    friends_title       db 0x09, 0x09, 0x09, "LISTA DE AMIGOS",0
    friends_menu_opt1   db 0x09, 0x09, "1. Añadir integrante al grupo", 0
    friends_menu_opt2   db 0x09, 0x09, "2. Eliminar integrante del grupo", 0
    friends_menu_opt3   db 0x09, 0x09, "3. Regresar", 0
    friends_limit_msg   db 0x09, "     Ya has alcanzado el límite de personas del grupo (20/20)", 0

    add_friend_prompt   db 0x09, "Inserte el nombre del nuevo integrante (Max. 20 caracteres)",0
    add_friend_error_msg db 0x09, 0x09, "ERROR! No puede dejar el nombre en blanco.",0
    friend_exists_msg   db 0x09, "ERROR! Esa persona ya está en el grupo",0
    added_friend_msg_1  db 0x09, "La persona ",0
    added_friend_msg_2  db 0x09, " ahora forma parte del grupo.",0
;//////////INTERFAZ DE INFORMACIÓN DE GASTOS //////////
    transaction_title   db 0x09, 0x09, 0x09, "INFORMACIÓN DE GASTOS",0
    return_prompt       db 0x09, "Presione ENTER para regresar al menú principal",0
    excluded_msg        db "Excluidos: ",0

;//////////INTERFAZ DE CONCILIACION (individual/total) //////////

;//////////INTERFAZ DE AÑADIR GASTO (individual/total) //////////
    amount_prompt       db 0x09, 0x09, 0x09, "Ingrese el monto del gasto a añadir: ",0
    invalid_amount_msg  db 0x09, "ERROR! Ingrese un monto válido (números enteros positivos)",0
    overflow_msk        db 0x09, "El monto se excede de la cantidad máxima de dinero en gastos (overflow)",0

    ask_who_pays_msg    db 0x09, 0x09, 0x09, "Ingrese el nombre de la persona que recibe el gasto: "
    who_pays_error_msg  db 0x09, "ERROR! Ese nombre no existe",0

    ask_excluded_msg    db 0x09, "Desea excluir a alguien del pago? (Y/N)",0
    excl_amount_msg     db 0x09, 0x09, 0x09, "Inserte la cantidad de personas a excluir (participantes-2): ",0

    expense_name_ask    db 0x09, "Asigne un nombre al gasto: ",0
    confirm_prompt      db 0x09, "Confirmar el gasto (Y/N): ",0

    transaction_limit_msg db 0x09, "Se ha llegado al límite de transacciones que puede hacer (30/30)",0

;//////////INICIALIZANDO DATOS//////////

    MAX_FRIEND_COUNT        EQU 20           
    friend_count            dd 0            ; Es la cantidad actual de amigos
                                            ; en el grupo

    NAME_LENGTH             EQU 21          ; 20 chars max, 1 nulo
    names_array             times 20 * 21 db 0   
    
    transaction_index       db 0            ; índice para trabajar la transacción actual
    person_index            db 0            ; índice para indicar la persona actual
    
    MAX_TRANSACTIONS        EQU 30 
    transaction_count       db 0

    total_expenses          dd 0            ; El total de gastos en la cuenta

    TWENTY_PERSON_ARRAY     MAX_FRIEND_COUNT * 4
    ;---------------------------------------------------------------------------
    ; Struct transacción
    ; Contiene:
    ; El nombre de la transacción       (tamaño 20 bytes + 1 nulo)
    ; El nombre de la persona que pagó  (tamaño 20 bytes + 1 nulo)
    ; El monto del gasto                (tamaño 4 bytes)
    ; El índice de la persona que pagó  (tamaño 4 bytes)
    ; Un array de 20 numeros, que guarde los índices de las personas excluidas en el pago (80 bytes 20x4)
    ;Offsets para los índices de las transacciones
    TRANSACTION_SIZE        EQU NAME_LENGTH + NAME_LENGTH + 4 + 4 + TWENTY_PERSON_ARRAY
    WHO_PAID_OFFSET         EQU NAME_LENGTH 
    EXPENSE_OFFSET          EQU NAME_LENGTH + NAME_LENGTH
    OWER_INDEX_OFFSET       EQU NAME_LENGTH + NAME_LENGTH + 4 
    EXCLUDED_INDEXES_OFFSET EQU NAME_LENGTH + NAME_LENGTH + 4 + 4  ; (Ya los indexes aquí se buscan los que están vacios y se van llenando)
    ;---------------------------------------------------------------------------

    ; Array de 30 transacciones
    transactions_array        times TRANSACTION_SIZE * MAX_TRANSACTIONS db 0
    ; Acceso:
    ; Nombre de transaccion transaccions_array[i * TRANSACTION_SIZE]
    ; Nomre de quién pagó   transaccions_array[i * TRANSACTION_SIZE + WHO_PAID_OFFSET]
    ; Monto gastado         transaccions_array[i * TRANSACTION_SIZE + EXPENSE_OFFSET]
    ; Indice de quién pagó  transaccions_array[i * TRANSACTION_SIZE + OWER_INDEX_OFFSET]
    ; Array de 20 indices   transaccions_array[i * TRANSACTION_SIZE + EXCLUDED_INDEXES_OFFSET]
    ;---------------------------------------------------------------------------

    ;---------------------------------------------------------------------------
    ; Struct deudas_persona
    ; Contiene:
    ; Un array de 20 numeros de cuánto le debe cada persona a esta 80 bytes

    ; Array de 20 personas x 20 cuentas
    person_registry         times TWENTY_PERSON_ARRAY * MAX_FRIEND_COUNT db 0
    ; Acceso:
    ; Registro de deudas por persona               person_registry[i * TWENTY_PERSON_ARRAY]
    ;---------------------------------------------------------------------------

    ;String para debugging
    ayuda db "AYUDA",0

.CODE
    .STARTUP
    ; TODO: Crear función que abra archivos y rellene las bases de datos
    jmp main_menu

main_menu:
    ; TODO: Hacer funcion que guarde la base de datos en un archivo cada vez que se llame al menú principal
    call header                         ; Imprime el titular que siempre sale arriba en el menú

    ; Imprimir el numero de gastos activos actuales
    PutStr transactions_msg
    PutLInt [transaction_count]
    nwln

    ; Imprimir el número de amigos en el grupo
    PutStr friends_msg
    PutLInt [friend_count]
    nwln

    ; Imprimir el monto total (la suma de todos los gastos)
    PutStr total_expense_msg
    ; PutLInt 
    nwln

    nwln 
    nwln
    nwln
    nwln
    
    ; Imprimir las opciones para navegar el menú
    PutStr main_menu_opt1
    nwln
    nwln
    PutStr main_menu_opt2
    nwln
    nwln
    PutStr main_menu_opt3
    nwln
    nwln
    PutStr main_menu_opt4
    nwln
    nwln
    PutStr main_menu_opt5
    nwln 
    nwln
    PutStr main_menu_opt6

    nwln
    nwln

    PutStr ask_choice                       ; Muestra el mensaje de selección de opción
    GetLInt EAX 
    cmp EAX, 1                              
    je friends_opt                          ; Si es Información de amigos, salta al friends_opt
    cmp EAX, 2
    je expenses_opt                         ; Si es Información de gastos, salta al expenses_opt
    cmp EAX, 3
    je single_conciliation                  ; Salta si solo se va a pagar una conciliación a single_conciliation
    cmp EAX, 4
    je all_conciliation                     ; Salta si se van a pagar todos los gastos
    cmp EAX, 5
    je add_expense
    cmp EAX, 6                              ; Si es la última opción, salta al final del programa
    je end_program
    PutStr wrong_option                     ; A este punto no se seleccionó una opción válida
    nwln
    jmp main_menu                           ; Tira el menú de nuevo

friends_opt:
    call header                             ; Imprime el titular que siempre sale arriba en el menú
    PutStr friends_title                    ; Imprime el nombre de la sección del menú
    nwln
    nwln
    nwln
    nwln
    nwln
    call print_friend_list                  ; Imprime la lista de amigos que hay en el grupo
    nwln
    PutStr friends_menu_opt1
    nwln
    PutStr friends_menu_opt2
    nwln
    PutStr friends_menu_opt3
    nwln
    PutStr ask_choice
    GetLInt EAX
    cmp EAX, 1
    je add_friend 
    cmp EAX, 2
    ;je remove_friend                      ; EL REMOVE FRIEND SOLO SE HACE SI HAY TIEMPO, ES UN EXTRA
    cmp EAX, 3
    je main_menu                         
    jmp friends_opt                        ; Vuelve a llamar este menú si se elige una opción fuera de las que hay

add_friend:
    call get_empty_name_index         ; Busca si aún hay espacio para añadir a otro amigo
    cmp EBX, 21                       ; Determina si ya se llegó al límite
    je friends_limit_reached          ; Si llegó al límite imprime un mensaje y vuelve al menú principal

    PutStr add_friend_prompt          ; Pide al usuario el nombre de la persona a añadir
    nwln
    GetStr EAX, 21                    ; Obtiene el nombre del amigo del usuario
    call trim_string                  ; Le recorta los espacios extra al inicio y el final del string

    cmp byte [EAX], 0                 ; Verifica si el nombre ingresado es vacío (presiono solo enter)
    je add_friend_error               ; Salta a imprimir un mensaje de error

    ; TODO: Verificar que el nombre no exista en la lista de amigos

    mov EDX, EBX                      ; Carga el índice actual en EDX
    imul EDX, NAME_LENGTH             ; Multiplica el índice por el tamaño de cada nombre
    add EDX, names_array              ; Suma la dirección base del array de nombres
    
    mov ESI, EAX                      ; Guarda la dirección del string en el ESI
    mov EDI, EDX         ; Guarda la dirección de donde se va a guardar el nomnbre en el struct
    call copy_string_memory           ; Guarda el string en memoria
    inc byte [friend_count]           ; Incrementa la cantidad de personas que tiene el array
    jmp main_menu                     ; Vuelve al menú principal

add_friend_error:
    mov EBX, add_friend_error_msg       ; Mueve el mensaje de error a EBX
    call print_notification             ; Llama la función para imprimir el mensaje

friends_limit_reached:
    mov EBX, friends_limit_msg          ; Mueve el mensaje del límire a EBX
    call print_notification             ; LLama la función para imprimir el mensaje


; -----------------------------------------------------------------------------------------------------------
;TODO Comentar código de la etiqueta
expenses_opt:
    call header
    PutStr transaction_title
    nwln
    ; TODO: Imprimir todos los gastos sin conciliar (monto, quien pagó, excluidos)
    nwln
    PutStr return_prompt
    GetCh AL
    jmp main_menu

single_conciliation:
    call header

all_conciliation:
    call header

add_expense:
    cmp [transaction_count], 30         ; Verifica si ya se llegó a las 30 transacciones
    je transaction_limit_reached        ; Si llegó a las 30 transacciones, imprime un mensaje y vuelve al menú principal

    call header
    nwln
    
    PutStr amount_prompt                ; Pregunta por el monto del gasto a añadir
    GetLInt EAX                         ; Guarda el número en el EAX
    push EAX                            ; Guarda el EAX en la pila para no perderlo

    ; Comprobar si hay overflow con la suma final
    mov EDX, [total_expenses]           ; Carga el monto total actual
    add EAX, EDX                        ; Suma este monto con el total de los gastos
    jo overflow_error                   ; Si hay overflow, salta a dar error
    push EAX                            ; Guarda el monto total para actualizarlo al final de la función

    ; Pedir el nombre de la persona que va a pagar 

ask_who_pays: 
    call header
    nwln

    call print_friend_list              ; Imprime la lista de integrantes actuales para que el usuario pueda elegir más fácilmente

    PutStr ask_who_pays_msg             ; Le pide al usuario el nombre de la persona a pagar
    nwln
    GetStr EAX, NAME_LENGTH             ; Guarda el nombre de la persona en EDI
    call trim_string                    ; Le recorta los espacios extra al inicio y el final del string
    cmp byte [EAX], 0                   ; Se asegura que no se haya metido un nombre vacío
    je payer_error:                     ;

    overflow_error:
        mov EBX, overflow_msk
        call print_notification        ; Imprime el mensaje de overflow
        
    transaction_limit_reached:
        mov EBX, transaction_limit_msg
        call print_notification        ; Imprime el mensaje de que se alcanzó el límite de transacciones a hacer
        

header:
    PutStr title
    nwln
    PutStr interface_info_1
    nwln
    PutStr interface_info_2
    nwln
    nwln
    ret

;---------------------------------------------------- get_empty_name_index
;Función que busca un campo libre (index) del array de nombres
;Entrada: nada
;Salida:  el índice del espacio libre en el registro EBX, o un 21 (que se validará que está fuera de límite)
;----------------------------------------------------
get_empty_name_index:
    mov EAX, [friend_count]          ; Carga el número de amigos actuales en EAX
    cmp EAX, MAX_FRIEND_COUNT        ; Compara el número de amigos actuales con el máximo permitido
    jne  search_empty_name           ; Si hay menos de 20 personas en el grupo, salta a buscar el índice libre
    mov EBX, 21                      ; El 21 en EBX es indicador de que está fuera de límite
    jmp found_empty_name             ; Salta al return, con un 21 en EBX

search_empty_name:
    mov ECX, MAX_FRIEND_COUNT        ; Carga el número máximo de amigos permitidos en ECX
    xor EBX, EBX                     ; Inicializa EBX (índice) en 0

name_index_loop:
    mov EAX, EBX                  ; Carga el índice actual en EAX
    imul EAX, NAME_LENGTH         ; Multiplica el índice por el tamaño de cada nombre
    add EAX, names_array          ; Suma la dirección base del array de nombres
    ; Verifica si el primer byte del nombre en la posición EBX es nulo (espacio vacío)
    cmp byte [EAX], 0  ; Compara el primer byte con 0 (vacío)
    
    je found_empty_name              ; Si está vacío, salta a found_empty_name
    
    inc EBX                          ; Incrementa el índice de búsqueda
    cmp EBX, ECX                     ; Compara si el índice ha alcanzado el límite
    jl name_index_loop               ; Si no ha alcanzado el límite, sigue buscando

    ; Si se recorre todo el array y no se encuentra un espacio vacío, retorna 21:
    mov EBX, 21                      ; Asigna 21 a EBX para indicar que no hay espacio
    ret

found_empty_name:
    ret                              ; EBX contiene el índice del espacio vacío encontrado


;---------------------------------------------------- compare_strings
;Función que compara a ver si 2 strings son iguales
;Entrada: ESI y EDI conteniendo las direcciones de ambos strings
;Salida:  En EAX, 0 si los strings son iguales, 1 si son diferentes
;----------------------------------------------------
compare_strings:
    mov ECX, 21                       ; Guarda la cantidad de veces a repetir el loop

compare_strings_loop:
    mov AL, [ESI]                     ; Lee el caracter del primer string     
    mov BL, [EDI]                     ; Lee el caracter del segundo string

    cmp AL, 0                         ; Compara el byte con el carácter nulo (fin del string)
    je end_comparison                 ; Si se llegó al final del primer string, termina

    cmp AL, 0                         ; Compara el byte con el carácter nulo (fin del string)
    je end_comparison                 ; Si se llegó al final del segundo string, termina

    cmp AL, BL                        ; Compara los caracteres
    jne not_equal                     ; Si los caracteres no son iguales, salta a not_equal

    ; Incrementar los punteros para leer el siguiente carácter
    inc ESI 
    inc EDI                        
    jmp compare_strings_loop       ; Vuelve al loop para comparar los caracteres

    end_comparison:
    mov EAX, 0                        ; Si los caracteres son iguales, EAX = 0
    ret    

    not_equal:
    mov EAX, 1                        ; Si los caracteres son diferentes, EAX = 1
    ret    

;---------------------------------------------------- get_name_index
;Función que busca el índice del nombre a buscar en la base de datos
;Entrada: El EDI contiene la dirección del string a buscar
;Salida:  En EAX, retorna el índice (0-19) o 21 si no lo encuentra
;----------------------------------------------------
get_name_index:
    mov EBX, 0                    ; Inicia el índice en 0
    mov ECX, MAX_FRIEND_COUNT     ; Carga el número máximo de amigos permitidos en ECX

search_name_index_loop:
    mov ESI, EBX                  ; Carga el índice actual en EAX
    imul ESI, NAME_LENGTH         ; Multiplica el índice por el tamaño de cada nombre
    add ESI, names_array          ; Suma la dirección base del array de nombres

    call compare_strings          ; Compara si los strings son iguales
    cmp EAX, 0                    ; Verifica si son iguales EAX = 0
    je   found_name_index         ; Si son iguales, salta 

    inc EBX                       ; Incrementa el índice de búsqueda
    cmp EBX, ECX                  ; Compara si el índice ha alcanzado el límite
    jl search_name_index_loop     ; Si no ha alcanzado el límite, sigue buscando

    mov EAX, 21                   ; Si llegó acá, el nombre no se encontró
    ret
    
found_name_index:
    mov EAX, EBX                  ; Si lo encuentra, EAX contiene el índice
    ret

;---------------------------------------------------- copy_string_memory
;Función que copia un string de un registro a memoria
;Entrada: la dirección del string en el ESI 
;         la dirección destino en EDI
;Salida:  nada
;----------------------------------------------------
copy_string_memory:
    cld                          ; Limpia el flag de dirección 
    
copy_loop:
    lodsb                        ; Carga un byte desde [ESI] en AL e incrementa el ESI
    stosb                        ; Almacena el byte de AL en [EDI] e incrementa EDI
    cmp AL, 0                    ; Compara el byte con 0 (fin del string)
    jne copy_loop                ; Si no es el carácter nulo, sigue copiando
    ret                          ; Si llega al final del string, termina la función

;---------------------------------------------------- copy_string_register
; Función que copia un string de la memoria a un registro (EBX)
;Entrada: la dirección del string en el ESI 
;         la dirección destino en EDI
;Salida:  nada
;----------------------------------------------------
copy_string_register:
    xor ECX, ECX                 ; Limpia ECX (usaremos ECX para almacenar el string)
    
copy_loop_register:
    lodsb                        ; Carga un byte desde [ESI] en AL y avanza ESI
    mov [EBX + ECX], AL          ; Copia el byte a la posición del registro EBX (como un array)
    inc ECX                      ; Incrementa el índice ECX para moverse en EBX
    cmp AL, 0                    ; Compara con el byte nulo (fin del string)
    jne copy_loop_register       ; Si no es el byte nulo, sigue copiando
    ret                          ; Si es el byte nulo, termina la función

;---------------------------------------------------- print_friend_list
; Función que imprime la lista de Amigos en el grupo
; imprime 2 amigos por línea
;Entrada: nada
;Salida:  nada
;----------------------------------------------------
print_friend_list:
    mov ECX, [friend_count]       ; Carga la cantidad de amigos en el grupo
    cmp ECX, 0                  ; Verifica si está vacío
    je return                   ; Si no hay nadie, no imprime nada y termina la función
    mov EBX, 0                  ; Inicializa el índice del array en 0

    ; Contar cuantas líneas en blanco hay que imprimir luego
    mov EAX, 20                 ; Carga la cantidad de amigos total que puede haber
    sub EAX, ECX                ; Le resta cantidad de amigos que hay actualmente
    shr EAX, 1                  ; Lo divide entre 2 (se imprimen 2 personas por nombre)

    xor EDX, EDX                  ; Inicializa el EDX (contador de cuantos nombres se han imprimido)

    PutCh 0x09                    ; Escribe un tabulador 
    PutCh 0x09                    ; Escribe un tabulador 
print_friend_list_loop:
    mov ESI, EBX                  ; Carga el índice actual en ESI
    imul ESI, NAME_LENGTH         ; Multiplica el índice por el tamaño de cada nombre
    add ESI, names_array          ; Suma la dirección base del array de nombres

    cmp EBX, 20                   ; Verifica si ya se recorrió todo el array
    je end_of_friend_list         ; Salta si ya se recorrió el array

    cmp byte [ESI], 0             ; Verifica si hay un nombre guardado en ese índice del array de nombres
    je inc_friend_index           ; Salta para incrementar el índice del array si es nulo sin incrementar la cantidad de prints

    cmp ECX, 0                    ; Verifica si ya se imprimieron todos los integrantes que había, en caso de que sean muy pocos
    je end_of_friend_list         ; Salta si ya se imprimieron todos los amigos
    PutStr ESI                    ; Imprime el nombre de la persona en esa posición del array
    PutCh 0x09                    ; Escribe un tabulador 
    PutCh 0x09                    ; Escribe un tabulador 


    inc EDX                       ; incrementa EDX, significando que o ya se imprimio 1 o 2 nombres en la línea
    cmp EDX, 2                    ; Verifica si ya se imprimieron 2 nombres
    je two_printed                ; Salta a hacer un salto de línea
    
is_end_of_friends_list:
    inc EBX                       ; Incrementa el índice del array
    cmp EBX, 20                   ; Verifica si ya se recorrió todo el array
    je end_of_friend_list         ; Salta si ya se recorrió el array
    cmp ECX, 0                    ; Verifica si ya se imprimieron todos los integrantes que había, en caso de que sean muy pocos
    je end_of_friend_list         ; Salta si ya se imprimieron todos los amigos
    jmp print_friend_list_loop    ; Sigue hasta que se recorra todo el array

inc_friend_index:
    inc EBX                      ; Incrementa el índice del array
    jmp print_friend_list_loop   ;

two_printed:
    xor EDX, EDX                 ; Reinicia el contador a 0
    nwln
    PutCh 0x09                    ; Escribe un tabulador  
    PutCh 0x09                    ; Escribe un tabulador 
    jmp is_end_of_friends_list  ; Salta a verificar si ya se llegó al final 


end_of_friend_list:
    mov ECX, EAX                     ; Carga el número de líneas vacías en ECX
print_empty_lines:
    cmp ECX, 0                       ; Verifica si ya imprimiste todas las líneas vacías
    je return                 ; Si no quedan líneas por imprimir, termina
    nwln                             ; Imprime una nueva línea vacía
    dec ECX                          ; Decrementa el contador de líneas vacías
    jmp print_empty_lines            ; Repite hasta imprimir todas las líneas vacías
    
;---------------------------------------------------- print_notification
; Función que imprime una notificación de menú para ciertos tipos de mensaje
; que luego te devuelve al menú principal
;Entrada: la dirección del string en el EBX 
;Salida:  nada
;----------------------------------------------------
print_notification:
    call header         ; Imprime el titular que siempre debe salir en la interfaz
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    PutStr EBX          ; Imprime mensaje respectivo que uno quiere mostrar
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    PutStr return_prompt              ; Indica que para volver al menú principal hay que presionar enter
    GetCh AL                              
    jmp main_menu                     ; Vuelve al menú principal

;---------------------------------------------------- confirm_menu_format
; Función que imprime una pantalla para confirmar una decisión
; que luego se compara si se escribió Y (fuera de la función)
;Entrada: la dirección del string prompt en el EBX 
;         la direccion del string de confirmación en el EDX
;Salida:  carácter 'Y' o cualquier otro que no sea una confirmación en el AL
;----------------------------------------------------
confirm_menu_format:
    call header         ; Imprime el titular que siempre debe salir en la interfaz
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    PutStr EBX                        ; Imprime mensaje respectivo que uno quiere preguntar
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    nwln
    PutStr EDX                        ; Pregunta si uno quiere confirmar o no
    GetCh AL                              
    ret

;---------------------------------------------------- trim_string
; Función que recorta un string, removiendo espacios del inicio y final del string
;Entrada: El String guardado en el EAX
;Salida:  Un String recortado o vacío "" en EAX
;----------------------------------------------------
trim_string:
    mov ESI, EAX                      ; Mueve el string al ESI, que es el registro donde se va a manipular el string
    call trim_start                   ; Remueve los espacios al inicio del string (mueve el puntero)

    cmp byte [ESI], ' '               ; Verifica si el carácter actual es un espacio
    je return_trim                    ; Guarda el string en EAX antes de devolverse

    call trim_end                     ; Remueve los espacios al final del string
    mov EAX, ESI                      ; Una vez hecho los cambios, copia el string editado en EAX
    ret                               ; Retorna el puntero al principio del string recortado

return_trim:
    mov EAX, ESI                      ; Copia el string editado en EAX
    ret                               ; Retorna el puntero al principio del string recortado

;---------------------------------------------------- trim_start
; Función que mueve el puntero del inicio de un string hasta donde haya algo diferente de un espacio
; Ejemplo: " Nombre" -> "Nombre" o "  " -> "" (vacio)
;Entrada: El String guardado en el ESI
;Salida:  El String modificado en ESI
;----------------------------------------------------
trim_start:
    cmp byte [ESI], ' '               ; Verifica si el carácter actual es un espacio
    je skip_char                      ; Si es espacio, incrementa el puntero al siguiente carácter
    ret                               ; Termina si encuentra algo que no es espacio

skip_char:
    inc ESI                          ; Si no es un espacio, incrementa el puntero al siguiente carácter

;---------------------------------------------------- trim_end
; Función que remueve espacios al final del string
;Entrada: El String guardado en el EAX
;Salida:  Un String recortado o vacío "" en EAX
;----------------------------------------------------
trim_end:
    ; Primero se busca el final del string o el carácter nulo
    mov EDI, ESI                    ; Copia el puntero del inicio del string en EDI

find_end:
    cmp byte [EDI], 0               ; Compara al caracter actual con nulo
    je check_end_spaces            ; Si es el final del string, pasa a revisar los espacios antes del final
    inc EDI                         ; Pasa al siguiente carácter
    jmp find_end                    ; Se repite hasta que se encuente el final

check_end_spaces:
    dec EDI                         ; Decrementa el puntero para volver al carácter anterior
    cmp byte [EDI], ' '             ; Verifica si el carácter actual es un espacio
    je remove_end_spaces            ; Si es un espacio, lo convierte en nulo y continúa
    ret                             ; Si no es un espacio, ya el string fue modificado
remove_end_spaces:
    mov byte [EDI], 0               ; Elimina el espacio (equivale a cambiarlo por un carácter nulo)
    jmp check_end_spaces            ; Se repite hasta que encuentre otro espacio o termine
return:
    ret
end_program:
    .EXIT