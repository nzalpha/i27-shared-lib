import com.i27academy.builds.Docker

def call(Map pipelineParams){
    Docker d = new Docker(this)


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
        stage ('Add') {
            // This step will take care of building the application
            steps {
                echo "--------Add Method-------"
                script{
                    println d.add(5,6)
                }
            }
        }

        // stage('Build'){
        //     steps{
        //         script{
        //         echo "-----------Build Method ------------"
        //         d.buildApp("${env.APPLICATION_NAME}")
        //         }
        //     }
        // }
    }
}
}
