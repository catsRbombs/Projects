section .data
    
    filename db 'archivo.txt', 0           ; Nombre del archivo
    new_line db 'Nueva linea de texto', 0xA ; Nueva línea a agregar (con salto de línea)
    new_line_len equ $ - new_line           ; Longitud de la nueva línea
    buffer_len equ 128                      ; Tamaño del buffer para leer
    error_msg db 'Error al abrir el archivo', 0xA
    error_msg_len equ $ - error_msg

section .bss
    file_descriptor resd 1                  ; Descriptor del archivo
    buffer resb buffer_len                  ; Buffer para leer datos

section .text
    global _start

_start:
    ; Abrir el archivo (sys_open)
    mov eax, 5                              ; sys_open
    mov ebx, filename                       ; Nombre del archivo
    mov ecx, 66                             ; O_RDWR | O_CREAT (lectura/escritura, crear si no existe)
    mov edx, 0644                           ; Permisos rw-r--r--
    int 0x80                                ; Llamada al sistema
    cmp eax, 0
    js error                                ; Si hay un error, saltar a 'error'
    mov [file_descriptor], eax              ; Guardar el descriptor del archivo

    ; Leer desde el archivo (sys_read)
    mov eax, 3                              ; sys_read
    mov ebx, [file_descriptor]              ; Descriptor del archivo
    mov ecx, buffer                         ; Buffer donde se almacenarán los datos
    mov edx, buffer_len                     ; Cantidad de bytes a leer
    int 0x80                                ; Llamada al sistema

    ; Posicionar el puntero de archivo al final (sys_lseek)
    mov eax, 19                             ; sys_lseek
    mov ebx, [file_descriptor]              ; Descriptor del archivo
    xor ecx, ecx                            ; Desplazamiento: 0 (final del archivo)
    mov edx, 2                              ; SEEK_END (posicionar al final)
    int 0x80                                ; Llamada al sistema

    ; Escribir al archivo (sys_write)
    mov eax, 4                              ; sys_write
    mov ebx, [file_descriptor]              ; Descriptor del archivo
    mov ecx, new_line                       ; Nueva línea de texto a agregar
    mov edx, new_line_len                   ; Longitud de la nueva línea
    int 0x80                                ; Llamada al sistema

    ; Cerrar el archivo (sys_close)
    mov eax, 6                              ; sys_close
    mov ebx, [file_descriptor]              ; Descriptor del archivo
    int 0x80                                ; Llamada al sistema

    ; Salir del programa (sys_exit)
    mov eax, 1                              ; sys_exit
    xor ebx, ebx                            ; Código de salida 0
    int 0x80                                ; Llamada al sistema

error:
    ; Mostrar mensaje de error si no se puede abrir el archivo
    mov eax, 4                              ; sys_write
    mov ebx, 1                              ; Salida estándar (stdout)
    mov ecx, error_msg                      ; Mensaje de error
    mov edx, error_msg_len                  ; Longitud del mensaje
    int 0x80                                ; Llamada al sistema

    ; Salir con código de error
    mov eax, 1                              ; sys_exit
    mov ebx, 1                              ; Código de salida 1 (error)
    int 0x80                                ; Llamada al sistema
