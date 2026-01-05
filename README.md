# 🚀 Gestor de Proyectos y Tareas

¡Hola! Bienvenido a tu propio sistema para organizar proyectos. Aquí te explico cómo ponerlo a funcionar paso a paso, ¡es muy fácil!

## 🛠️ ¿Qué necesitas tener instalado?

Antes de empezar, asegúrate de tener estos programas en tu computadora:

1.  **Java 17+**: Para que funcione el "cerebro" (backend) de la aplicación.
2.  **Node.js y npm**: Para que funcione la "cara" (frontend) de la aplicación.
3.  **Docker Desktop**: Para la base de datos (donde se guardan tus tareas).
4.  **Maven**: Para construir el proyecto Java.

---

## 🏃‍♂️ Opción 1: La forma más rápida (Docker)

Si tienes Docker instalado, puedes arrancar todo con un solo comando.

1.  Abre una terminal (pantalla negra de comandos).
2.  Ve a la carpeta del proyecto.
3.  Escribe esto y dale ENTER:

```bash
docker-compose up --build
```

¡Espera unos minutos y listo!
- Ve a **http://localhost:4200** en tu navegador para ver la aplicación.

---

## 🐢 Opción 2: Paso a paso (Manual)

Si prefieres hacerlo parte por parte, sigue estos pasos:

### 1. Encender la Base de Datos 🗄️

Necesitamos un lugar para guardar los datos. Usaremos Docker para esto.

1.  Abre una terminal en la carpeta del proyecto.
2.  Escribe:

```bash
docker-compose up -d postgres
```

### 2. Encender el Backend (El Cerebro) 🧠

1.  Abre **otra** terminal.
2.  Entra a la carpeta `backend`:
    ```bash
    cd backend
    ```
3.  Dile a la computadora dónde está la base de datos y arranca el programa:
    ```bash
    export DB_PORT=5432
    mvn spring-boot:run
    ```
    *(Nota: Si usas Windows, el comando `export` es `set`)*

### 3. Encender el Frontend (La Cara) 🎨

1.  Abre una **tercera** terminal.
2.  Entra a la carpeta `frontend`:
    ```bash
    cd frontend
    ```
3.  Instala las librerías necesarias (solo la primera vez):
    ```bash
    npm install
    ```
4.  Arranca la aplicación:
    ```bash
    ng serve
    ```

### 4. ¡A jugar! 🎮

Abre tu navegador favorito (Chrome, Firefox, Edge) y entra a:

👉 **http://localhost:4200**

---

## 🧪 ¿Cómo probarlo?

1.  **Regístrate**: Crea una cuenta nueva con tu email y contraseña.
2.  **Crea un Proyecto**: Dale un nombre genial (ej. "Construir un Cohete").
3.  **Agrega Tareas**: Ponle cosas por hacer (ej. "Comprar combustible", "Buscar astronautas").
    - ¡No olvides ponerle fecha y descripción!
4.  **Completa Tareas**: Cuando termines algo, dale al botón "Complete". ¡Verás cómo cambia!
5.  **Borra Tareas**: Si te equivocaste, usa el botón rojo "Delete" para borrarla.

---

## 🆘 Problemas Comunes

- **"El puerto 8080 está ocupado"**: Asegúrate de no tener otro programa usando ese puerto.
- **"No conecta a la base de datos"**: Revisa que Docker esté corriendo y que hayas hecho el paso 1 de la opción manual.
- **Pantalla en blanco**: Asegúrate de haber esperado a que `ng serve` termine de cargar.

¡Diviértete organizando tus proyectos! 🚀
