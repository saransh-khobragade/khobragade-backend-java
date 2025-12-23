
cat init_db.sql | docker exec -i postgres psql -U khobragade_db_user -d khobragade_db

cat > .env << EOF
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=khobragade_db
DATABASE_USER=khobragade_db_user
DATABASE_PASSWORD=khobragade_db_password
EOF

export $(cat .env | xargs)

echo $DATABASE_NAME

./gradlew bootRun

render login
render services

