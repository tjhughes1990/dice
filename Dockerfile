# Dockerfile for java environment.

FROM archlinux:latest AS diceroller_java

LABEL version="1.0.0"

# Update and install git.
RUN pacman -Syu --noconfirm

# Download dependencies to /tmp.
WORKDIR /tmp
ARG INSTALL_DIR=/usr/local/share
ARG JRE_URL=https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.6%2B10
ARG JRE_TAR=OpenJDK11U-jre_x64_linux_hotspot_11.0.6_10.tar.gz
RUN curl -L -o $JRE_TAR $JRE_URL/$JRE_TAR &&\
    tar -zxvf $JRE_TAR -C $INSTALL_DIR

ENV PATH=${INSTALL_DIR}/jdk-11.0.6+10-jre/bin:$PATH

# Add new user.
ARG USER=diceroller
RUN useradd --shell=/bin/bash --create-home $USER
USER $USER 
WORKDIR /home/$USER

################################################################################

# Dockerfile for web app.

FROM diceroller_java as web_app

EXPOSE 8080

# Copy and run jar.
COPY ./service/target/DiceRoller-1.0.0.jar .
ENTRYPOINT ["java", "-jar", "DiceRoller-1.0.0.jar"]

################################################################################

# Dockerfile for database REST.

FROM diceroller_java AS database

# Copy and run jar.
COPY ./database/target/database-1.0.0.jar .
ENTRYPOINT ["java", "-jar", "database-1.0.0.jar"]
