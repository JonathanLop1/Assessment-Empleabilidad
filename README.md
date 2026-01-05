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

## 🆘 Solución de Problemas (Troubleshooting)

Si algo no funciona, ¡no te preocupes! Aquí tienes una guía paso a paso para los problemas más comunes.

### 1. Error: "El puerto 5432 está ocupado" (Bind for 0.0.0.0:5432 failed)
Este error significa que ya tienes otra base de datos Postgres corriendo en tu computadora. Tienes dos opciones:

**Opción A: Detener el servicio que ocupa el puerto (Recomendado)**
1.  Busca qué proceso usa el puerto:
    -   **Linux/Mac**: `sudo lsof -i :5432`
    -   **Windows**: `netstat -ano | findstr :5432`
2.  Detén ese servicio:
    -   **Linux/Mac**: Toma el número "PID" que salió en el paso anterior y ejecuta:
        ```bash
        sudo kill -9 <PID>
        ```
        *(Ejemplo: `sudo kill -9 1234`)*
    -   **Windows**: Toma el número "PID" (está al final de la línea) y ejecuta:
        ```cmd
        taskkill /PID <PID> /F
        ```
        *(Ejemplo: `taskkill /PID 1234 /F`)*

**Opción B: Usar otro puerto (ej. 5433)**
Si no quieres detener el otro servicio, configuremos este proyecto en el puerto **5433**:

1.  **Base de Datos (Docker)**:
    En lugar de usar `docker-compose`, ejecuta este comando manual para levantar la base de datos en el puerto 5433:
    ```bash
    docker run -d --name projectdb \
      -e POSTGRES_DB=projectdb \
      -e POSTGRES_USER=projectuser \
      -e POSTGRES_PASSWORD=projectpass \
      -p 5433:5432 \
      postgres:15-alpine
    ```

2.  **Backend (Spring Boot)**:
    Debemos decirle al backend que se conecte al nuevo puerto. Abre una terminal en la carpeta `backend` y ejecuta:
    
    *Linux/Mac:*
    ```bash
    export DB_PORT=5433
    export DB_HOST=localhost
    export DB_NAME=projectdb
    export DB_USER=projectuser
    export DB_PASSWORD=projectpass
    # Clave secreta para JWT (puedes cambiarla)
    export JWT_SECRET=MyS3cr3tK3yF0rJWT-Pr0j3ctT4skM4n4g3m3ntSyst3m-Ch4ng3InPr0duct10n
    
    mvn spring-boot:run
    ```

    *Windows (CMD):*
    ```cmd
    set DB_PORT=5433
    set DB_HOST=localhost
    set DB_NAME=projectdb
    set DB_USER=projectuser
    set DB_PASSWORD=projectpass
    set JWT_SECRET=MyS3cr3tK3yF0rJWT-Pr0j3ctT4skM4n4g3m3ntSyst3m-Ch4ng3InPr0duct10n
    
    mvn spring-boot:run
    ```

3.  **Frontend**:
    El frontend sigue funcionando igual:
    ```bash
    cd frontend
    npm install
    ng serve
    ```

### 2. Error de Docker: "iptables failed" o "Network creation failed"
Si `docker-compose up` falla con errores de red extraños, intenta reiniciar el servicio de Docker o usa la **Opción Manual** descrita arriba (levantar cada servicio por separado).

Para reiniciar Docker y limpiar redes (¡Cuidado! Esto borra redes no usadas):
```bash
docker network prune
sudo systemctl restart docker
```

---

¡Diviértete organizando tus proyectos! 🚀
