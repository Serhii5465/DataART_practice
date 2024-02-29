FROM openjdk:11-bullseye

ARG USER_NAME=developer

RUN apt update && apt install -y git maven \
    && rm -rf /var/lib/apt/lists/* && apt-get clean \
    && useradd --shell /bin/bash --create-home --user-group ${USER_NAME}

USER ${USER_NAME}