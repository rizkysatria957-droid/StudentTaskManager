
# STAGE 1: BUILD APPLICATION
FROM gradle:8.14-jdk17 AS builder

WORKDIR /app

# Copy konfigurasi Gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# Copy source code
COPY src ./src

# Build aplikasi
RUN gradle clean installDist --no-daemon


# STAGE 2: RUN APPLICATION
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy hasil build dari stage pertama
COPY --from=builder /app/build/install/StudentTaskManager/ ./

# Menjalankan aplikasi
ENTRYPOINT ["./bin/StudentTaskManager"]

