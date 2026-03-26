# =====================================================
#  Stage 1 — BUILD: compile the jar using Maven
# =====================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first (cache dependencies layer separately)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# =====================================================
#  Stage 2 — RUN: lightweight runtime image
# =====================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the built jar from Stage 1
COPY --from=builder /app/target/whatsapp-chatbot-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Health check — Docker will monitor this
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
