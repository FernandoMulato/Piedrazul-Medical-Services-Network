#!/usr/bin/env bash
# Reset the users-service database — truncates all data and resets sequences.
# Usage: ./scripts/reset-db.sh
#
# Requires: psql client with access to the users_db database.

set -euo pipefail

DB_NAME="${DB_NAME:-users_db}"
DB_USER="${DB_USER:-medical_user}"

echo "🔄 Resetting database: $DB_NAME"
echo "⚠️  This will DELETE ALL DATA in the database."

psql -U "$DB_USER" -d "$DB_NAME" <<SQL
-- Disable triggers temporarily
SET session_replication_role = 'replica';

-- Truncate all tables (in dependency order)
TRUNCATE TABLE professionals RESTART IDENTITY CASCADE;
TRUNCATE TABLE patients RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Re-enable triggers
SET session_replication_role = 'origin';

SQL

echo "✅ Database reset complete. All tables are empty and sequences reset."
