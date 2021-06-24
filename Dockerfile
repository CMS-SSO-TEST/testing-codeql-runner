FROM adoptopenjdk/openjdk11:latest

ENV CODEQL_HOME=/opt

RUN apt update -y && apt upgrade -y
RUN apt install -y wget git

RUN wget https://github.com/github/codeql-action/releases/latest/download/codeql-bundle-linux64.tar.gz -O /tmp/codeql-bundle-linux64.tar.gz && \
    tar -xvzf /tmp/codeql-bundle-linux64.tar.gz -C ${CODEQL_HOME} && \
    rm /tmp/codeql-bundle-linux64.tar.gz

ENV PATH="${CODEQL_HOME}/codeql/codeql:${PATH}"

RUN ln -s "${CODEQL_HOME}/codeql/codeql" /usr/bin/codeql

ENTRYPOINT "/bin/bash"
