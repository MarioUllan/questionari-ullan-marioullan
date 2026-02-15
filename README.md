# Gestione Questionari

Aplicación Web desarrollada con Spring Boot para la gestión y compilación de cuestionarios.

La aplicación permite:
- Crear preguntas (abiertas o de elección múltiple)
- Crear cuestionarios
- Asignar preguntas a cuestionarios
- Compilar cuestionarios
- Guardar respuestas como borrador
- Enviar cuestionarios como definitivos
- Acceder a cuestionarios compilados mediante un código único (usuarios no registrados)

---

# 1. Instalación

## Requisitos

- Java 17 o superior
- Maven

Para comprobar la versión de Java:

```bash
java -version
```

Para comprobar la versión de Maven:

```bash
mvn -version
```

## Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd gestione-questionari
```

---

# 2. Ejecución
1. Abrir el proyecto en IntelliJ.
2. Localizar la clase principal `GestioneQuestionariApplication`.
3. Hacer clic derecho sobre la clase.
4. Seleccionar **Run 'GestioneQuestionariApplication'**.
5. Acceder en el navegador a:

```
http://localhost:8080
```

---

# 3. Uso de la aplicación

## 3.1 Crear preguntas

Acceder a:

```
http://localhost:8080/questions
```

Se pueden crear preguntas de tipo:

- `OPEN`
- `MULTIPLE_CHOICE`

---

## 3.2 Crear cuestionarios

Acceder a:

```
http://localhost:8080/questionnaires
```

Pasos:

1. Crear un nuevo cuestionario.
2. Asignar preguntas previamente creadas mediante la opción "Asignar preguntas".

---

## 3.3 Compilar un cuestionario

1. Desde la lista de cuestionarios, iniciar la compilación.
2. Introducir un email.
3. El sistema genera automáticamente un código único.
4. El cuestionario queda en estado `DRAFT`.

---

## 3.4 Guardar borrador

Permite guardar las respuestas sin finalizar el cuestionario.

El cuestionario permanece en estado `DRAFT` y puede seguir editándose.

---

## 3.5 Enviar definitivo

Al pulsar "Enviar definitivo":

- El estado pasa a `FINAL`.
- El cuestionario queda en modo solo lectura.
- El código generado debe conservarse para acceder posteriormente.

---

## 3.6 Acceder mediante código

Desde la página principal se puede introducir el código único generado para:

- Visualizar el cuestionario compilado.
- Editarlo si está en estado `DRAFT`.
- Eliminarlo.

Si el código no existe (por ejemplo, si fue eliminado), se muestra un mensaje informativo.

---

# 4. Base de datos

La aplicación utiliza una base de datos H2 en memoria.

Consola H2 disponible en:

```
http://localhost:8080/h2-console
```

Configuración:

JDBC URL:

```
jdbc:h2:mem:testdb
```
---

# 5. Tests

El proyecto incluye:

- Test de validación (Bean Validation).
- Test MVC del controlador.

---

# 6. Entrega

Tag de entrega:

```
finalDelivery
```