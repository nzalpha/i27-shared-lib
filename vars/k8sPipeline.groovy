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
        GKE_DEV_CLUSTER_NAME = "cart-dev-ns"
        GKE_DEV_ZONE = "us-central1-c"
        GKE_DEV_PROJECT = "boutique-424803"
        K8S_DEV_FILE = "k8s_dev.yaml"
        DEV_NAMESPACE = "carting-dev-ns"
        DOCKER_HUB= "docker.io/nawaz004"
        DOCKER_CREDS= credentials('docker_cred')

    }

    stages{
        
        stage('Auth')
        {
            steps{
                echo "------------Auth Method---------"
                script{
                d.auth_login("${env.GKE_DEV_CLUSTER_NAME}", "${env.GKE_DEV_ZONE}", "${env.GKE_DEV_PROJECT}")
                }
            }
        }

        stage("Deploy to Dev")
        {
         
            steps{
                echo "------------Deploy to Prod Method---------"
                script{  
                def docker_image = "${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
                d.auth_login("${env.GKE_DEV_CLUSTER_NAME}", "${env.GKE_DEV_ZONE}", "${env.GKE_DEV_PROJECT}")
                d.k8sdeploy("${env.K8S_DEV_FILE}", "${env.DEV_NAMESPACE}",docker_image)
                }
            }
        }

    }
}
}
