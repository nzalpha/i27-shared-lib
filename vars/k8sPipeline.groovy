// this is for k8 deployment

import com.i27academy.k8s.k8s
import com.i27academy.builds.Docker
def call(Map pipelineParams){
    k8s d = new k8s(this)


pipeline {
   agent {
        label 'K8-slave'
    }
    tools {
        maven 'mvn 3.8'
        jdk 'jdk-17'
    }

environment {
        APPLICATION_NAME = "${pipelineParams.appName}"
    }

    stages{
        
        stage('Auth')
        {
            steps{
                echo "------------Auth Method---------"
                d.auth_login()
            }
        }

        stage ('Add') {
            // This step will take care of building the application
            steps {
                echo "--------Add Method-------"
                script{
                    println d.add(5,6)
                }
            }
        }

        stage('Build'){
            steps{
                script{
                echo "-----------Build Method ------------"
                d.buildApp("${env.APPLICATION_NAME}")
                }
            }
        }
    }
}
}
