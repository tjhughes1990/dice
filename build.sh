#! /bin/bash

# Run full build.
mvn clean install
if [[ $? -ne 0 ]]; then
    echo "ERROR: Maven build failed. Skipping system tests. . ."
    exit 1
fi

# Initialise docker containers. Containers must be run as root.
sudo docker-compose up --build -d
if [[ $? -ne 0 ]]; then
	echo "ERROR: could not run docker as root. Skipping system tests. . ."
	exit 1
fi


# Wait for docker container initialisation.
COUNT=0
echo "Waiting for docker conatiner initialisation. . ."
until curl localhost:8080 > /dev/null 2>&1; do
    if [[ $COUNT -gt 60 ]]; then
        echo "Docker container startup timed out. Exiting. . ."
        sudo docker-compose down
        exit 1
    fi

    sleep 1
done

# Run tests.
mvn clean install -PsystemTests

# Shutdown docker containers.
sudo docker-compose down
