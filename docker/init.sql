-- Optional database initialization script
-- This script creates test data for manual testing

-- Test users will be created via the API during registration
-- This file serves as a reference for the database schema

-- The application uses JPA auto-ddl which will create the schema automatically
-- Schema includes:
-- - users (id, username, email, password)
-- - projects (id, owner_id, name, status, deleted)
-- - tasks (id, project_id, title, completed, deleted)

-- No initialization data is needed as users can register via API
