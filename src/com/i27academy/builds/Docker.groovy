package com.i27academy.builds

class Docker{
    def jenkins
    Docker(jenkins){
        this.jenkins=jenkins
    }

    def add(fnum,snum){
        return fnum+snum
    }

    //Application Build

    def buildApp(appName){
        jenkins.sh """
         echo "Building the  Application $appName for shared library"
         sh 'mvn clean package -DskipTests=true'
         archiveArtifacts artifacts: 'target/*.jar'
         """
    }
}

    //Docker Build


    // Docker Login

    // Docker push