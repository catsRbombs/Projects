; SUPERGATO
; Proyecto #2

; IC-3101 Arquitectura de Computadoras

; Instituto Tecnológico de Costa Rica
; Campus Tecnológico Central Cartago
; Escuela de Ingeniería en Computación                              
; II Semestre 2024
; Prof. M.Sc. Esteban Arias Méndez

; Efraim Cuevas Aguilar        
; Carné: 2024109746                                                    
; Fabián Mata Salas
; Carné: 2024188730




%include "io.mac"



.DATA                 
    current_board       dd -1                          ; Va del 0-8, -1 es que puede poner en cualquier casilla

    ; Las X son 1
    ; Las O son 2
    current_turn        dd 2                           ; Turnos para guardar el dato en las casillas

    fake_turn dd 1          ;Variable para manipulacion de turno del algoritmo del bot

    ; Matrices para los tableros secundarios
    board_0x0           times 9 dd 0
    board_0x1           times 9 dd 0
    board_0x2           times 9 dd 0

    board_1x0           times 9 dd 0
    board_1x1           times 9 dd 0
    board_1x2           times 9 dd 0

    board_2x0           times 9 dd 0
    board_2x1           times 9 dd 0
    board_2x2           times 9 dd 0

    ; Matriz de punteros a los tableros
    board_matrix dd board_0x0, board_0x1, board_0x2
                dd board_1x0, board_1x1, board_1x2
                dd board_2x0, board_2x1, board_2x2

    ; Tablero principal con 9 casillas
    main_board          times 9 dd 0

section .text
global is_valid_move
global make_move
global next_turn
global restart_game
global check_board_victory
global check_game_victory
global check_board_tie
global check_empty_board
global bot_move
global get_current_board
;---------------------------------------------------------------- is_valid_move
; Esta función valida si el movimiento que va a hacer un jugador es valida
; Entrada: 3 números para buscar el número de la posición
; Salida: 1 si es una posición válida, 0 si no
;----------------------------------------------------------------
is_valid_move:
    enter 0,0

    ;Mover los valores a los registros
    mov EAX, [EBP+8]        ; Obtengo el numero del tablero

    mov ECX, [EBP+12]       ; Obtengo la posición en el tablero (y)
    mov EDX, [EBP+16]       ; Obtengo la posición en el tablero (x)
    
    ;Compruebo si el jugador hizo la jugada en un tablero válido
    cmp dword[current_board], -1 ; Verifica si tiene libertad para poner en cualquier tablero
    je validate_position    ; Si si lo tiene, salta a ver si no está haciendo una jugada en una casilla ya usada

    cmp EAX, [current_board]; Verifica si está haciendo la jugada en un tablero válido
    je validate_position    ; Si son iguales, salta a ver si no está haciendo una jugada en una casilla ya usada


invalid_cell:
    ;Si no entra en ninguno retorna false (0)
    xor EAX, EAX
    leave
    ret

validate_position:

    ;Conseguir la dirección del tablero que se está jugando
    imul EAX, 4                     ; Lo multiplica por el tamaño de cada dirección
    add EAX, board_matrix           ; Añado la dirección base del array de tableros
    
    mov EAX, [EAX]                  ; Obtengo la dirección del tablero de juego donde se hizo la jugada
    
    ;Calculo la posición dentro del tablero 3x3
    ;Le sumo los índices con lo tamaños a la matriz que tengo
    ;Dirección de la casilla = base_tablero + (x + y*3) * 4
    imul ECX, 3                     ; ECX = y * 3
    add ECX, EDX                    ; ECX = x + (y * 3)
    imul ECX, 4                     ; EXC = 4(x + (y * 3))
    
    add EAX, ECX                    ; Le sumo la dirección de la casilla a la dirección base del tablero
    
    cmp dword[EAX], 0               ; Verifica que sea una casilla vacía
    jne invalid_cell                ; Salta a retornar si no es valido

    ; Si no entra en ninguno retorna true (1)
    mov EAX, 1
    leave
    ret


;---------------------------------------------------------------- make_move
; Esta función Coloca un 1 (X) o un 2 (O) dependiendo de quién sea el turno
; Entrada: 3 números para buscar el número de la posición
; Salida: void
;----------------------------------------------------------------
make_move:
    enter 0,0

    ;Mover los valores a los registros
    mov EAX, [EBP+8]        ; Obtengo el numero del tablero

    mov ECX, [EBP+12]       ; Obtengo la posición en el tablero (y)
    mov EDX, [EBP+16]       ; Obtengo la posición en el tablero (x)

    ;Conseguir la dirección del tablero que se está jugando
    imul EAX, 4                     ; Lo multiplica por el tamaño de cada dirección
    add EAX, board_matrix           ; Añado la dirección base del array de tableros
    
    mov EAX, [EAX]                  ; Obtengo la dirección del tablero de juego donde se hizo la jugada
    
    ;Calculo la posición dentro del tablero 3x3
    ;Le sumo los índices con lo tamaños a la matriz que tengo
    ;Dirección de la casilla = base_tablero + (x + y*3) * 4
    imul ECX, 3                     ; ECX = y * 3
    add ECX, EDX                    ; ECX = x + (y * 3)

    ; IMPORTANTE: aquí se guarda la posición del siguiente tablero que se puede jugar
    push ECX                        ; Mete el número a la pila para llamar a la función
    call set_current_board          ; Llama a la función para colocar el current board del siguiente turno

    imul ECX, 4                     ; EXC = 4(x + (y * 3))
    
    add EAX, ECX                    ; Le sumo la dirección de la casilla a la dirección base del tablero

    mov EDX, [current_turn]         ; Muevo el 1 o 2 para meterlo a la matriz

    ; Mueve el número de La X o O a la casilla en el EAX
    mov [EAX], EDX

    leave 
    ret 12

;---------------------------------------------------------------- set_current_board
; Coloca el tablero actual para el siguiente turno, así que valida si tiene que ser -1 (free placement)
; Entrada: el número del tablero grande en la pila
; Salida: void pero el current_board cambia
;----------------------------------------------------------------
set_current_board: 
    enter 0,0

    mov EDX, [EBP+8]        ; Obtengo el numero de la pila

    imul EDX, 4             ; Lo multiplico para contar por el tamaño de cada espacio

    add EDX, main_board     ; Sumo la dirección base del tablero principal

    cmp dword[EDX], 0       ; Verifico si ese tablero ya está ganado
    jne board_already_won   ; Si no está vacío, entonces salta porque ya está ganado

    mov EDX, [EBP+8]        ; Obtengo el numero de la pila de nuevo
    mov dword[current_board], EDX; Setteo el número como el siguiente tablero donde se debe jugar


    leave
    ret 4

board_already_won:
    mov dword[current_board], -1 ; Setteo el tablero en -1 para poder colocar en cualquier tablero el siguiente turno

    leave
    ret 4

;---------------------------------------------------------------- next_turn
; Simplemente cambia el turno para O o X
; Entrada: void
; Salida: void 
;----------------------------------------------------------------
next_turn:
    cmp dword[current_turn], 2    ; Verifica si el turno actual era de O
    je Os_turn                    ; Si lo es, salta para cambiarlo al turno de X

    ; Si no, entonces es turno de X, así que hay que hacer lo opuesto
    mov dword[current_turn], 2
    ret

Os_turn:
    mov dword[current_turn], 1
    ret


;---------------------------------------------------------- restart_game
; Esta función reinicia el estado del juego y no tiene ni entradas ni salidas
; current_turn = 2
; current_board = -1
; Todos los valores en los tableros secundarios y el tablero principal = 0
;----------------------------------------------------------
;---------------------------------------------------------- restart_game
; Esta función reinicia el estado del juego y no tiene ni entradas ni salidas
; current_turn = 2
; current_board = -1
; Todos los valores en los tableros secundarios y el tablero principal = 0
;----------------------------------------------------------
restart_game:
    enter 0, 0             
    ; Guarda los datos de todos los registros para que no haga segfault a la hora de retornar a C
    pushad                            

    mov dword[current_turn], 2      ; Establece current_turn a 2    
    mov dword[current_board], -1    ; Establece current_board a -1

    ; Poner en 0 todos los valores de los tableros secundarios
    ; Iterar 9 tableros
    mov ECX, 9              ; Es el número de tableros secundarios (9 matrices)
    mov ESI, board_matrix   ; Consigue la dirección base de board_matrix

reset_boards:
    mov EDI, [ESI]          ; Obtener dirección del tablero actual
    add ESI, 4              ; Apuntar al siguiente puntero en board_matrix
    mov EAX, 0              ; Valor para llenar los tableros

    mov EDX, 9              ; Cada tablero tiene 9 casillas

reset_single_board:
    mov dword[EDI], EAX     ; Escribir 0 en la casilla actual
    add EDI, 4              ; Avanzar al siguiente elemento del tablero
    dec EDX                 ; Decrementar el contador de casillas
    jnz reset_single_board  ; Repetir hasta vaciar el tablero

    dec ECX                 ; Decrementar el contador de tableros
    jnz reset_boards        ; Repetir para todos los tableros

    ; Ahora se va a poner en 0 todas las casillas del tablero principal
    mov ECX, 9              ; Número de casillas en main_board
    mov EDI, main_board     ; Dirección base de main_board

reset_main_board:
    mov dword[EDI], 0       ; Escribir 0 en la casilla actual
    add EDI, 4              ; Avanzar al siguiente elemento
    dec ECX                 ; Decrementar el contador
    jnz reset_main_board    ; Repetir hasta vaciar el tablero principal

    popad
    leave
    ret

;---------------------------------------------------------------- check_board_victory
; Pasándole el número del tablero, chequéa si se hizo una línea de 3
; También completa una casilla en el tablero principa si hubo una vistoria
; Entrada: número del tablero en la pila
; Salida: 0 false, 1 true
;----------------------------------------------------------------
check_board_victory:
    enter 0,0

    mov EDX, [EBP+8]        ; Obtengo el numero de la pila
    imul EDX, 4             ; Lo multiplico para contar por el tamaño de cada espacio

    pushad                  ; Guarda todos los registros en la pila para el final

    mov ESI, board_matrix   ; Cargo la dirección base del board_matrix
    add ESI, EDX            ; Le sumo la cantidad del EDX para llegar al tablero correcto
    mov ESI, [ESI]          ; Cargo el tablero correcto para revisarlo en board_check

    call board_check        ; Revisa si en ese tablero hubo una victoria

    cmp EAX, 1              ; Compara si el jugador ganó
    jne no_board_victory          ; Si no hay victoria, salta

    ; Si pasó, entonces ese tablero está completo
    ; Ahora en el tablero principal hay que dejar un 1 (X) o un 2 (O)
    mov ECX, [current_turn] ; Carga el símbolo que está jugando (número)
    add EDX, main_board     ; Le sumo al EDX la dirección base del tablero principal
    mov [EDX], ECX          ; Guardo en ese tablero el valor

    ; Se establece colocamiento libre para el siguiente turno
    mov dword[current_board], -1

    popad                   ; Restauro todos los registros

    mov EAX, 1              ; Muevo un 1 al EAX para retornar true

    leave
    ret 4

no_board_victory:
    popad                   ; Restaura todos los registros
    xor EAX, EAX            ; Retorna 0
    leave
    ret


;---------------------------------------------------------------- check_game_victory
; Revisa si en el tablero principal el jugador hizo una línea de 3
; Entrada: void
; Salida: 0 false, 1 true 
;----------------------------------------------------------------
check_game_victory:
    pushad                  ; Guarda todos los registros en la pila para el final

    mov ESI, main_board     ; Mueve el tablero principal para llamar a board_check
    call board_check        

    cmp EAX, 1              ; Compara si el jugador ganó
    jne no_victory          ; Si no hay victoria, salta

    ; Si pasó, ganó el jugador del turno actual
    popad                   ; Restaura todos los registros
    mov EAX, 1              ; Retorna 1
    ret

no_victory:
    popad                   ; Restaura todos los registros
    xor EAX, EAX            ; Retorna 0
    ret


;---------------------------------------------------------------- board_check
; Revisa el tablero que le pasaron a ver si hay una línea horizontal, vertical o diagonal
; Entrada: la dirección del tablero en ESI
; Salida: 0 false, 1 true en EAX
;----------------------------------------------------------------
board_check:
    mov EDI, [current_turn] ; Carga el jugador al que se va a revisar si hizo una línea de 3

    ; Revisar las filas (horizontalmente)
    mov ECX, 3              ; Es la cantidad de veces de revisiones horizontales
    mov EBX, ESI            ; Copia la dirección del tablero porque este se va a manipular
horizontal_check:
    cmp dword [EBX], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    jne horizontal_jump     ; Salta a la siguiente fila si no son iguales

    cmp dword [EBX+4], EDI  ; Verifica si en la segunda casilla está el mismo símbolo
    jne horizontal_jump     ; Salta a la siguiente fila si no son iguales

    cmp dword [EBX+8], EDI  ; Verifica si en la tercera casilla está el mismo símbolo
    jne horizontal_jump     ; Salta a la siguiente fila si no son iguales

    ; Si llega acá, hay una línea horizontal de 3 Os o Xs
    mov EAX, 1
    ret

horizontal_jump:
    add EBX, 12             ; Salta a la siguiente fila a evaluar
    dec ECX                 ; Decrementa el contador
    jnz horizontal_check    ; Repetir hasta recorrer las 3 filas

    ; Revisar las columnas (verticalmente)
    mov ECX, 3              ; Es la cantidad de veces de revisiones verticales
    mov EBX, ESI            ; Copia la dirección del tablero porque este se va a manipular
vertical_check:
    cmp dword [EBX], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    jne vertical_jump       ; Salta a retornar false porque no hay ninguna línea de 3
    
    cmp dword [EBX+12], EDI ; Verifica si en la segunda casilla está el mismo símbolo
    jne vertical_jump       ; Salta a retornar false porque no hay ninguna línea de 3
    
    cmp dword [EBX+24], EDI ; Verifica si en la tercera casilla está el mismo símbolo
    jne vertical_jump       ; Salta a retornar false porque no hay ninguna línea de 3
    
    ; Si llega acá, hay una línea vertical de 3 Os o Xs
    mov EAX, 1
    ret
    
vertical_jump:
    add EBX, 4              ; Salta a la siguiente columna a evaluar
    dec ECX                 ; Decrementa el contador
    jnz vertical_check      ; Repetir hasta recorrer las 3 columnas

    ; Revisar en diagonal hacia abajo
down_diagonal_check:
    cmp dword [ESI], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    jne upper_diagonal_check; Salta a revisar la diagonal hacia arriba si no son iguales
    
    cmp dword [ESI+16], EDI ; Verifica si en la segunda casilla está el mismo símbolo
    jne upper_diagonal_check; Salta a revisar la diagonal hacia arriba si no son iguales
    
    cmp dword [ESI+32], EDI ; Verifica si en la tercera casilla está el mismo símbolo
    jne upper_diagonal_check; Salta a revisar la diagonal hacia arriba si no son iguales

    ; Si llega acá, hay una línea diagonal de 3 Os o Xs
    mov EAX, 1
    ret

upper_diagonal_check:
    cmp dword [ESI+24], EDI ; Verifica si en la primera casilla está el mismo símbolo
    jne no_three_in_a_row   ; Salta a la siguiente columna si no son iguales
    
    cmp dword [ESI+16], EDI ; Verifica si en la segunda casilla está el mismo símbolo
    jne no_three_in_a_row   ; Salta a la siguiente columna si no son iguales
    
    cmp dword [ESI+8], EDI  ; Verifica si en la tercera casilla está el mismo símbolo
    jne no_three_in_a_row   ; Salta a la siguiente columna si no son iguales

    ; Si llega acá, hay una línea diagonal de 3 Os o Xs
    mov EAX, 1
    ret

no_three_in_a_row:
    xor EAX, EAX            ; Retorna 0
    ret


;---------------------------------------------------------------- check_board_tie
; Pasándole el número del tablero, chequéa si se llenó el tablero y nadie lo ganó
; También completa una casilla en el tablero principa si hubo un empate con un 3
; Entrada: número del tablero en la pila
; Salida: void (Solo debe colocar un 3 en el tablero principal)
;----------------------------------------------------------------
check_board_tie:
    enter 0,0

    mov EDX, [EBP+8]        ; Obtengo el numero de la pila
    imul EDX, 4             ; Lo multiplico para contar por el tamaño de cada espacio

    mov EAX, board_matrix   ; Cargo la dirección base del board_matrix
    add EAX, EDX            ; Le sumo la cantidad del EDX para llegar al tablero correcto
    mov EAX, [EAX]          ; Cargo el tablero correcto para revisarlo en board_check

    mov ECX, 9              ; Son 9 espacios que chequear en un tablero

board_tie_loop:
    ; Si una casilla tiene un 0, entonces no hay empate
    cmp dword[EAX], 0
    je no_tie

    add EAX, 4              ; Sigo con la siguiente casilla
    dec ECX                 ; Reducir el contador
    jnz board_tie_loop      ; Itera hasta haber recorrido las 9 casillas

    ; Si llega acá, hay un empate y no se pueden colocar más casillas en este subtablero
    add EDX, main_board     ; Le sumo al EDX la dirección base del tablero principal
    mov dword[EDX], 3       ; Guardo en ese tablero el valor de empate

    ;Verifico que no se me vaya a bloquear el juego si hay un empate
    mov ECX, [EBP + 8]
    push ECX
    call set_current_board  ; Llamo a esta función para verificar que no se bloquee el juego

    leave 
    ret


check_empty_board:
    enter 0,0

    mov EAX, board_matrix   ; Cargo la dirección base del board_matrix
    mov ECX, 80         ;Cantidad de casillas -1 por base.
    loop empty_board_loop

    mov EAX, 1
    leave
    ret

empty_board_loop:
    add EAX, 4              ; Sigo con la siguiente casilla
    ; Si una casilla tiene un 0, entonces hay una casilla vacía
    cmp dword[EAX], 1
    je auto_return_not_empty
    cmp dword[EAX], 2
    je auto_return_not_empty

    dec ECX
    jnz empty_board_loop

auto_return_not_empty:
    mov EAX, 0
    leave
    ret

no_tie:
    leave
    ret


bot_move:
    enter 0,0
    pushad
    mov dword [fake_turn], 1 ;Establecer el rival.
    mov EDX, [current_board]        ; Obtengo el numero de la pila
    cmp EDX, -1
    je handle_freedom
keep_going:
    mov EDX, [current_board]
    imul EDX, 4             ; Lo multiplico para contar por el tamaño de cada espacio

    mov ESI, board_matrix   ; Cargo la dirección base del board_matrix
    add ESI, EDX            ; Le sumo la cantidad del EDX para llegar al tablero correcto
    mov ESI, [ESI]          ; Cargo el tablero correcto para buscar por una posible victoria para el bot

    jmp check_bot_victory        ; Revisa si en ese tablero podria haber una victoria

handle_freedom:
    mov eax, main_board
    cmp dword [eax], 0
    je case1
    cmp dword [eax+4], 0
    je case2
    cmp dword [eax+8], 0
    je case3
    cmp dword [eax+12], 0
    je case4
    cmp dword [eax+16], 0
    je case5
    cmp dword [eax+20], 0
    je case6
    cmp dword [eax+24], 0
    je case7
    cmp dword [eax+28], 0
    je case8
    cmp dword [eax+32], 0
    je case9

case1: 
    mov dword [current_board], 0
    jmp keep_going

case2: 
    mov dword [current_board], 1
    jmp keep_going

case3: 
    mov dword [current_board], 2
    jmp keep_going

case4: 
    mov dword [current_board], 3
    jmp keep_going

case5:
    mov dword [current_board], 4
    jmp keep_going

case6:
    mov dword [current_board], 5
    jmp keep_going

case7:
    mov dword [current_board], 6
    jmp keep_going

case8:
    mov dword [current_board], 7
    jmp keep_going

case9:
    mov dword [current_board], 8
    jmp keep_going

check_bot_victory:

    mov EDI, 2 ;Carga el turno 2

    ; Revisar las filas (horizontalmente)
    mov EAX, 3              ; Es la cantidad de veces de revisiones horizontales
    mov EBX, ESI            ; Copia la dirección del tablero porque este se va a manipular

    jmp horizontal_bot_check

    
horizontal_bot_check:
    MOV ECX, 0
    cmp dword [EBX], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    je increase_counter_hor    ; Salta a la siguiente fila si no son iguales

horizontal_bot_check1:
    mov EDX, [fake_turn]
    cmp dword [EBX], EDX
    je decrease_counter_hor
    jne aftercheck1hor

decrease_counter_hor:
    dec ECX
    jnz aftercheck1hor
    
aftercheck1hor:
    cmp dword [EBX+4], EDI  ; Verifica si en la segunda casilla está el mismo símbolo
    je increase_counter_hor1     ; Salta a la siguiente fila si no son iguales

horizontal_bot_check2:
    mov EDX, [fake_turn]
    cmp dword [EBX+4], EDX
    je decrease_counter_hor1
    jne aftercheck2hor

decrease_counter_hor1:
    dec ECX
    jnz aftercheck2hor
aftercheck2hor:
    cmp dword [EBX+8], EDI  ; Verifica si en la tercera casilla está el mismo símbolo
    je increase_counter_hor2   ; Salta a la siguiente fila si no son iguales

continue_hor:
    mov EDX, [fake_turn]
    cmp dword [EBX+8], EDX
    je decrease_counter_hor3
    jne continue_hor2

decrease_counter_hor3:
    dec ECX
    jmp continue_hor2

continue_hor2:
    cmp ECX, 2
    je locate_horizontal
    jne horizontal_jump_bot

locate_horizontal:
    cmp dword [EBX], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    jne slot1place    

    cmp dword [EBX+4], EDI  ; Verifica si en la segunda casilla está el mismo símbolo
    jne slot2place    

    cmp dword [EBX+8], EDI  ; Verifica si en la tercera casilla está el mismo símbolo
    jne slot3place   

slot1place: 
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 0
    mov [eax], ecx
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

slot2place:
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 0
    mov [eax], ecx
    mov ecx, 1
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

slot3place:
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 0
    mov [eax], ecx
    mov ecx, 2
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

horizontal_jump_bot:
    add EBX, 12             ; Salta a la siguiente fila a evaluar
    mov ECX, 0              ;Actualizar contador de posibilidad
    dec EAX                 ; Decrementa el contador
    jnz horizontal_bot_check   ; Repetir hasta recorrer las 3 filas

    ; Revisar las columnas (verticalmente)
    mov EAX, 3              ; Es la cantidad de veces de revisiones verticales
    mov EBX, ESI            ; Copia la dirección del tablero porque este se va a manipular

vertical_bot_check:
    cmp dword [EBX], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    je increase_counter_ver       ; Salta a retornar false porque no hay ninguna línea de 3

vertical_bot_check1:    
    mov EDX, [fake_turn]
    cmp dword [EBX], EDX
    je decrease_counter_ver1
    jne aftercheck1ver

decrease_counter_ver1:
    dec ECX
    je aftercheck1ver
aftercheck1ver:
    cmp dword [EBX+12], EDI ; Verifica si en la segunda casilla está el mismo símbolo
    je increase_counter_ver1       ; Salta a retornar false porque no hay ninguna línea de 3
vertical_bot_check2:    
    mov EDX, [fake_turn]
    cmp dword [EBX+12], EDX
    je decrease_counter_ver2

    jne aftercheck2ver

decrease_counter_ver2:
    dec ECX
    jnz aftercheck2ver
aftercheck2ver:
    cmp dword [EBX+24], EDI ; Verifica si en la tercera casilla está el mismo símbolo
    je increase_counter_ver2       ; Salta a retornar false porque no hay ninguna línea de 3
continue_ver:
    mov EDX, [fake_turn]
    cmp dword [EBX+24], EDX
    je decrease_counter_ver3
    jne continue_ver2

decrease_counter_ver3:
    dec ECX
    jmp continue_ver2


continue_ver2:
    cmp ECX, 2
    je locate_vertical
    jne vertical_jump_bot

locate_vertical:
    cmp dword [EBX], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    jne slot1place    

    cmp dword [EBX+12], EDI  ; Verifica si en la segunda casilla está el mismo símbolo
    jne slot4place    

    cmp dword [EBX+24], EDI  ; Verifica si en la tercera casilla está el mismo símbolo
    jne slot7place   

slot4place:
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 1
    mov [eax], ecx
    mov ecx, 0
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

slot7place:
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 2
    mov [eax], ecx
    mov ecx, 0
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

vertical_jump_bot:
    add EBX, 4              ; Salta a la siguiente columna a evaluar
    mov ECX, 0              ;Actualizar contador de posibilidad
    dec EAX                 ; Decrementa el contador
    jnz vertical_bot_check      ; Repetir hasta recorrer las 3 columnas

    jmp down_diagonal_check_bot; Revisar en diagonal hacia arriba


down_diagonal_check_bot:
    cmp dword [ESI], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    je increase_counter_diagbot; Salta a revisar la diagonal hacia arriba si no son iguales
down_diagonal_check_bot1:    
    mov EDX, [fake_turn]
    cmp dword [ESI], EDX ; Verifica si en la primera casilla está el mismo símbolo
    je decrease_counter_diagbot1

    jne aftercheck1diagbot

decrease_counter_diagbot1:
    dec ECX
    jnz aftercheck1diagbot
aftercheck1diagbot:
    cmp dword [ESI+16], EDI ; Verifica si en la segunda casilla está el mismo símbolo
    je increase_counter_diagbot1; Salta a revisar la diagonal hacia arriba si no son iguales

down_diagonal_check_bot2: 
    mov EDX, [fake_turn]  
    cmp dword [ESI+16], EDX ; Verifica si en la primera casilla está el mismo símbolo
    je decrease_counter_diagbot2

    jne aftercheck2diagbot

decrease_counter_diagbot2:
    dec ECX
    jmp aftercheck2diagbot

aftercheck2diagbot:
    cmp dword [ESI+32], EDI ; Verifica si en la tercera casilla está el mismo símbolo
    je increase_counter_diagbot2; Salta a revisar la diagonal hacia arriba si no son iguales

continue_diagbot:
    mov EDX, [fake_turn]
    cmp dword [ESI+32], EDX ; Verifica si en la primera casilla está el mismo símbolo
    je decrease_counter_diagbot3
    jne continue_diagbot2

decrease_counter_diagbot3:
    dec ECX
    jmp continue_diagbot2

continue_diagbot2:
    cmp ECX, 2
    je locate_down_diagonal
    mov ECX, 0 
    jne upper_diagonal_check_bot

locate_down_diagonal:
    cmp dword [EBX], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    jne slot1place    

    cmp dword [EBX+16], EDI  ; Verifica si en la segunda casilla está el mismo símbolo
    jne slot5place    

    cmp dword [EBX+32], EDI  ; Verifica si en la tercera casilla está el mismo símbolo
    jne slot9place   

slot5place:
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 1
    mov [eax], ecx
    mov ecx, 1
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

slot9place:
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 2
    mov [eax], ecx
    mov ecx, 2
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

upper_diagonal_check_bot:
    cmp dword [ESI+24], EDI ; Verifica si en la primera casilla está el mismo símbolo
    je increase_counter_diagtop  ; Salta a la siguiente columna si no son iguales
upper_diagonal_check_bot1:    
    mov EDX, fake_turn
    cmp dword [ESI+24], EDX ; Verifica si en la primera casilla está el mismo símbolo
    je decrease_counter_diagtop1
    jne aftercheck1diagtop

decrease_counter_diagtop1:
    dec ECX
    jnz aftercheck1diagtop
aftercheck1diagtop:
    cmp dword [ESI+16], EDI ; Verifica si en la segunda casilla está el mismo símbolo
    je increase_counter_diagtop1   ; Salta a la siguiente columna si no son iguales

upper_diagonal_check_bot2:   
    mov EDX, fake_turn     
    cmp dword [ESI+16], EDX ; Verifica si en la primera casilla está el mismo símbolo
    je decrease_counter_diagtop2

    jne aftercheck2diagtop
decrease_counter_diagtop2:
    dec ECX
    jmp aftercheck2diagtop

aftercheck2diagtop:
    cmp dword [ESI+8], EDI  ; Verifica si en la tercera casilla está el mismo símbolo
    je increase_counter_diagtop2   ; Salta a la siguiente columna si no son iguales
continue_diagtop:
    mov EDX, fake_turn
    cmp dword [ESI+8], EDX ; Verifica si en la primera casilla está el mismo símbolo
    je decrease_counter_diagtop3
    jne continue_diagtop2

decrease_counter_diagtop3:
    dec ECX
    jmp continue_diagtop2

continue_diagtop2:
    cmp ECX, 2
    je locate_top_diagonal

    cmp EDI, 2
    je stop_player
    jne random_placement

locate_top_diagonal:
    cmp dword [ESI+24], EDI    ; Verifica si en la primera casilla está el mismo símbolo
    jne slot7place
    cmp dword [ESI+16], EDI
    jne slot5place
    cmp dword [ESI+8], EDI   ; Verifica si en la tercera casilla está el mismo símbolo
    jne slot3place

increase_counter_diagtop:
    inc ECX
    jmp upper_diagonal_check_bot1
increase_counter_diagtop1:
    inc ECX
    jmp upper_diagonal_check_bot2
increase_counter_diagtop2:
    inc ECX
    jmp continue_diagtop
increase_counter_diagbot:
    inc ECX
    jmp down_diagonal_check_bot1
increase_counter_diagbot1:
    inc ECX
    jmp down_diagonal_check_bot2
increase_counter_diagbot2:
    inc ECX
    jmp continue_diagbot
increase_counter_hor:
    inc ECX
    jmp horizontal_bot_check1
increase_counter_hor1:
    inc ECX
    jmp horizontal_bot_check2
increase_counter_hor2:
    inc ECX
    jmp continue_hor

increase_counter_ver:
    inc ECX
    jmp vertical_bot_check1
increase_counter_ver1:
    inc ECX
    jmp vertical_bot_check2
increase_counter_ver2:
    inc ECX
    jmp continue_ver

stop_player:

    mov dword [fake_turn], 2 ;establecer bot
    mov EDX, [current_board]
    imul EDX, 4             ; Lo multiplico para contar por el tamaño de cada espacio

    mov ESI, board_matrix   ; Cargo la dirección base del board_matrix
    add ESI, EDX            ; Le sumo la cantidad del EDX para llegar al tablero correcto
    mov ESI, [ESI]          ; Cargo el tablero correcto para buscar por una posible victoria para el bot
    mov EDI, 1 ;Carga el turno del player

    mov EAX, 3              ; Es la cantidad de veces de revisiones horizontales
    mov EBX, ESI            ; Copia la dirección del tablero porque este se va a manipular

    jmp horizontal_bot_check

random_placement:
    cmp dword[ESI], 0
    je slot1place
    cmp dword[ESI+4], 0
    je slot2place
    cmp dword[ESI+8], 0
    je slot3place
    cmp dword[ESI+12], 0
    je slot4place
    cmp dword[ESI+16], 0
    je slot5place
    cmp dword[ESI+20], 0
    je slot6place
    cmp dword[ESI+24], 0
    je slot7place
    cmp dword[ESI+28], 0
    je slot8place
    cmp dword[ESI+32], 0
    je slot9place

    mov EAX, [current_board]
    inc EAX
    mov [current_board], EAX
    jmp bot_move

slot6place:
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 1
    mov [eax], ecx
    mov ecx, 2
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

slot8place: 
    popad
    mov EAX, [EBP+8]
    MOV EDX, [EBP+12]
    mov ECX, 2
    mov [eax], ecx
    mov ecx, 1
    mov [edx], ecx
    mov EAX, [EBP+16]
    mov ECX, [current_board]
    mov [eax], ecx
    leave
    ret 12

get_current_board:
    enter 0,0
    mov EDX, [EBP+8]
    mov EAX, [current_board]
    MOV [EDX], EAX
    leave
    ret 4