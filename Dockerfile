#use java 8 as a base image
FROM openjdk:8

#create a new app directory for application files
RUN mkdir /app

#Copy the app files from host machine to image filesystem
#(first argument - local machine, seconde - remote)
COPY . /app

#Set the directory for executing future commands
WORKDIR /app

# setup gradle
RUN curl -L https://services.gradle.org/distributions/gradle-6.5.1-bin.zip -o gradle-6.5.1-bin.zip
RUN unzip gradle-6.5.1-bin.zip
ENV GRADLE_HOME=/app/gradle-6.5.1
ENV PATH=$PATH:$GRADLE_HOME/bin
RUN gradle --version

#Running command to run test
CMD gradle test