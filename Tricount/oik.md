1. Ingreso de Amigos

Objetivo: Permitir el ingreso de al menos 20 amigos. Cada amigo tendrá un nombre asociado y su correspondiente estructura en memoria.
Tareas:

Crear un arreglo en memoria para almacenar los nombres de los amigos (con capacidad para al menos 20 amigos).
Implementar una función que lea un nombre desde la entrada del usuario y lo almacene en el arreglo.
Verificar que no se supere el límite de 20 amigos, mostrando un mensaje de error si es necesario.
Permitir al usuario agregar amigos en cualquier momento hasta el límite.

Sugerencia para empezar:

Define un espacio en memoria para los nombres de los amigos. Puedes usar db para reservar una cantidad fija de bytes para cada nombre.
Usa sys_read para capturar el nombre ingresado por el usuario y almacenar el valor en memoria.

2. Ingreso de Cuentas de Gastos

Objetivo: Permitir registrar al menos 30 cuentas de gastos, donde cada cuenta puede involucrar a varias personas con diferentes montos.

Tareas:
Crear un arreglo en memoria para almacenar las cuentas de gastos.
Cada cuenta debe tener: (1) un monto total, (2) una lista de personas involucradas, y (3) los montos que cada persona debe pagar.
Implementar una función que permita ingresar una cuenta y seleccionar qué personas participan en la cuenta (algunas pueden tener gasto 0).
Validar que no se exceda el límite de 30 cuentas.

Sugerencia para empezar:

Define una estructura en memoria para cada cuenta, incluyendo el monto total y los pagos de cada persona. Usa sys_read para ingresar el monto y los participantes.
3. Guardar y Cargar Datos desde Archivos

Objetivo: Permitir que los datos de amigos y cuentas de gastos se guarden en un archivo para que puedan ser recuperados al reiniciar el programa.

Tareas:

Usar sys_open, sys_read y sys_write para leer y escribir los datos en un archivo de texto.
Implementar funciones para guardar la lista de amigos y las cuentas de gastos.
Implementar funciones para cargar la lista de amigos y cuentas de un archivo cuando el programa se inicia.
Asegurarse de que se pueda agregar nueva información sin perder la ya existente.

Sugerencia para empezar:

Guarda los datos en un formato sencillo (como texto plano) para que sea fácil de leer y escribir. Usa las llamadas al sistema para manipular archivos en Linux.

4. Registro de Gastos Individuales por Persona

Objetivo: Registrar cuánto gasta cada persona en cada cuenta.

Tareas:

Al registrar una cuenta, solicitar al usuario que elija qué amigos están involucrados en la cuenta.
Permitir que algunos amigos tengan un gasto de 0, mientras otros tienen diferentes montos.
Almacenar estos datos en las estructuras de memoria para que puedan ser consultados después.
Sugerencia para empezar:
Usa bucles para recorrer la lista de amigos y solicitar el monto que cada uno debe pagar en una cuenta.

5. Validación de Errores

Objetivo: Validar que las entradas del usuario sean correctas y que no ocurran errores como desbordamientos aritméticos.

Tareas:

Si se espera un número y el usuario ingresa texto, mostrar un mensaje de error y pedir que se ingrese de nuevo.
Controlar los desbordamientos en las operaciones aritméticas (por ejemplo, si el total de gastos es muy grande).
Mostrar mensajes claros de error cuando ocurran problemas y permitir que el usuario intente nuevamente sin que el programa falle.

Sugerencia para empezar:

Implementa verificaciones simples para validar que la entrada del usuario sea un número antes de convertirlo a un valor numérico y almacenarlo.
6. Conciliación de Cuentas

Objetivo: Minimizar el número de pagos entre personas para saldar las cuentas. Esto significa que el programa debe calcular quién debe pagar a quién y cuánto.

Tareas:

Sumar los gastos de cada persona y calcular un saldo (positivo o negativo) según si la persona debe dinero o le deben dinero.
Ordenar a las personas de acuerdo con sus saldos.
Hacer que las personas con saldo negativo paguen a las que tienen saldo positivo, minimizando el número de transacciones.
Implementar dos modos: (1) modo consulta (solo muestra los datos actuales) y (2) modo conciliación (realiza los pagos y pone las cuentas en 0).
Sugerencia para empezar:
Puedes usar un algoritmo simple de "matching" donde las personas que deben pagan a las personas a las que se les debe. Esto se puede hacer recorriendo la lista de saldos y haciendo ajustes hasta que todos los saldos estén en 0.
7. Reportes y Opciones Extra (Opcional)

Objetivo: Generar reportes y permitir eliminar personas o corregir cuentas.

Tareas:

Implementar una opción para generar un archivo de texto con todos los datos de las personas y las cuentas.
Permitir eliminar personas, asegurándote de que no tengan pagos pendientes.
Permitir corregir los montos de las cuentas existentes.
Resumen de Pasos:
Ingreso de amigos: Crear estructura para almacenar los nombres de los amigos (al menos 20).
Ingreso de cuentas de gastos: Crear estructura para registrar las cuentas (al menos 30).
Guardar y cargar datos: Implementar lectura y escritura de datos desde/para un archivo.
Registro de gastos individuales: Permitir que cada persona registre un monto diferente en cada cuenta.
Validación de errores: Implementar validación de entradas y control de desbordamientos.
Conciliación de cuentas: Crear algoritmo para minimizar el número de pagos entre personas.
Opciones adicionales (opcional): Generar reportes, eliminar amigos, corregir cuentas.
Cada uno de estos pasos puede implementarse por separado, y una vez completado todo, se integrarán para formar la aplicación completa.