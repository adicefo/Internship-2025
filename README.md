# Internship-2025
**A repository for my internship development.**

## Running the Application

**To run the application:**
- **Clone the repo**
- **Open your terminal or docker desktop app**
- **Navigate to the path of the `docker-compose.yaml` file in the cloned repo**
- **Run `docker-compose up --build `**

## Test data 
**In order to inject test data do following:**
- **Open your terminal**
- **Navigate to the `data.sql` script file, usually path is `path_to_cloned_repo/Internship_API/src/main/resources`**
- **Run the command: `cat data.sql | docker exec -i postgres psql -h localhost -U adi -d internship_db -f-`**

## Credentials

### Postgresql database:

- **Host name/address:** `localhost`
- **Port:** `5433`
- **Username:** `adi`
- **Password:** `@di123!!`
- **Database:** `internship_db`

### Keycloak:
- **Username:** `real_admin`
- **Password:** `rpuk123!`


