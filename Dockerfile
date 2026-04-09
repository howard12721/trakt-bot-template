FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

RUN chmod +x ./gradlew

COPY src ./src

RUN set -eux; \
    ./gradlew --no-daemon installDist; \
    install_dir="$(find build/install -mindepth 1 -maxdepth 1 -type d | head -n 1)"; \
    test -n "$install_dir"; \
    mv "$install_dir" /opt/app; \
    printf '%s\n' \
      '#!/bin/sh' \
      'set -eu' \
      '' \
      'launcher="$(find "$(dirname "$0")/bin" -mindepth 1 -maxdepth 1 -type f ! -name '\''*.bat'\'' | head -n 1)"' \
      'test -n "$launcher"' \
      '' \
      'exec "$launcher" "$@"' \
      > /opt/app/run; \
    chmod +x /opt/app/run

FROM eclipse-temurin:25-jre

RUN useradd --system --create-home --home-dir /home/appuser appuser

WORKDIR /app

COPY --from=builder --chown=appuser:appuser /opt/app ./

ENV JAVA_OPTS=""

USER appuser

ENTRYPOINT ["./run"]
