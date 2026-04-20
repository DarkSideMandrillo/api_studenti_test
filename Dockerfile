# --- STAGE 1: Compilazione ---
# Usiamo Maven con JDK 23 per compilare il progetto
FROM maven:3.9.9-eclipse-temurin-23 AS build

# Impostiamo la cartella di lavoro dentro il container
WORKDIR /app

# Copiamo il pom.xml e i sorgenti
COPY . .

# Eseguiamo il build del progetto saltando i test per velocizzare
# Questo genererà il file target/api_studenti-1.0-SNAPSHOT.jar
RUN mvn clean package -DskipTests

# --- STAGE 2: Esecuzione ---
# Usiamo una JRE (Java Runtime Environment) leggera per far girare l'app
FROM eclipse-temurin:23-jre

WORKDIR /app

# Copiamo il "Fat JAR" creato nello stage precedente
# Il nome deve corrispondere a <artifactId>-<version>.jar del tuo POM
COPY --from=build /app/target/api_studenti-1.0-SNAPSHOT.jar app.jar

# Specifichiamo a Render che l'app userà una porta dinamica
# Nota: assicurati di aver modificato il codice Java per leggere System.getenv("PORT")
EXPOSE 7070

# Comando per avviare l'API
CMD ["java", "-jar", "app.jar"]