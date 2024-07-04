// this is for k8 deployment

import com.i27academy.k8s.K8s
import com.i27academy.builds.Docker
def call(Map pipelineParams){
    K8s d = new K8s(this)


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
                script{
                d.auth_login()
                }
            }
        }

    }
}
}
