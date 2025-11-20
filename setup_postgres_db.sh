#!/bin/bash

# Script to set up PostgreSQL database for Student Tutoring System

DB_NAME="tutoring_system_db"
DB_USER="tutoring_user"
DB_PASS="secure_password123"
DB_PORT=5432

echo "Setting up PostgreSQL database for Student Tutoring System..."

# Check if Docker is installed
if ! [ -x "$(command -v docker)" ]; then
    echo "Error: Docker is not installed." >&2
    exit 1
fi

# Check if docker-compose is installed
if ! [ -x "$(command -v docker-compose)" ]; then
    echo "Checking for docker compose (v2)..."
    if ! [ -x "$(docker compose version)" ]; then
        echo "Error: Neither docker-compose nor docker compose (v2) is installed." >&2
        exit 1
    fi
fi

# Create docker-compose.yml file
cat >docker-compose.yml <<EOF
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: tutoring_system_postgres
    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASS}
    ports:
      - "${DB_PORT}:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./postgres_schema.sql:/docker-entrypoint-initdb.d/01-schema.sql
      - ./postgres_dummy_data.sql:/docker-entrypoint-initdb.d/02-data.sql
    restart: unless-stopped

volumes:
  postgres_data:
EOF

echo "Starting PostgreSQL container..."
docker-compose up -d

echo "Waiting for PostgreSQL to be ready..."
sleep 10

echo "Database setup complete!"
echo ""
echo "Connection details:"
echo "Host: localhost"
echo "Port: ${DB_PORT}"
echo "Database: ${DB_NAME}"
echo "Username: ${DB_USER}"
echo "Password: ${DB_PASS}"
echo ""
echo "To connect manually: psql -h localhost -p ${DB_PORT} -U ${DB_USER} -d ${DB_NAME}"
echo ""
echo "To stop the database: docker-compose down"
echo "To stop and remove volumes: docker-compose down -v"

# Wait a bit more to ensure database is completely ready
sleep 5

# Verify tables have been created and populated
echo ""
echo "Verifying tables have been created and populated..."
sleep 10

# Test connection and count records in a few tables to ensure data was loaded
docker-compose exec -T postgres psql -U ${DB_USER} -d ${DB_NAME} -c "
SELECT 'users' as table_name, COUNT(*) as count FROM users
UNION ALL
SELECT 'students' as table_name, COUNT(*) as count FROM students
UNION ALL
SELECT 'subjects' as table_name, COUNT(*) as count FROM subjects
UNION ALL
SELECT 'tutors' as table_name, COUNT(*) as count FROM tutors;
"

