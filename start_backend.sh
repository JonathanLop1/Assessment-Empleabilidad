#!/bin/bash

# Colores para los mensajes
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Iniciando el sistema paso a paso...${NC}"

# 1. Base de Datos
echo -e "\n${GREEN}1. Configurando Base de Datos (PostgreSQL)...${NC}"
if [ "$(docker ps -aq -f name=projectdb)" ]; then
    echo "   Eliminando contenedor 'projectdb' anterior..."
    docker rm -f projectdb
fi

echo "   Arrancando contenedor de base de datos..."
docker run -d --name projectdb \
  -e POSTGRES_DB=projectdb \
  -e POSTGRES_USER=projectuser \
  -e POSTGRES_PASSWORD=projectpass \
  -p 5432:5432 \
  postgres:15-alpine

# Esperar a que la BD esté lista
echo "   Esperando a que la base de datos esté lista..."
sleep 5

# 2. Backend
echo -e "\n${GREEN}2. Arrancando el Backend...${NC}"
cd backend
export DB_PORT=5432
export DB_HOST=localhost
export DB_NAME=projectdb
export DB_USER=projectuser
export DB_PASSWORD=projectpass
export JWT_SECRET=MyS3cr3tK3yF0rJWT-Pr0j3ctT4skM4n4g3m3ntSyst3m-Ch4ng3InPr0duct10n

# Verificar si wrapper existe, si no usar mvn
if [ -f "./mvnw" ]; then
    ./mvnw spring-boot:run
else
    mvn spring-boot:run
fi
