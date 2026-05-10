# ── Estágio 1: build ──────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache do Docker
# (dependências só são re-baixadas se o pom.xml mudar)
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Estágio 2: runtime ────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
