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
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging()
        DOCKER_HUB= "docker.io/nawaz004"
        DOCKER_CREDS= credentials('docker_cred')
        VM_CREDS = credentials('ali_docker_vm_cred')
        ENV_DEPLOY="Dev"
        Dev_HOST_PORT=5761
        Dev_CONT_PORT=8761
        Test_ENV_DEPLOY="Test"
        Test_HOST_PORT=6761
        Test_CONT_PORT=8761
        Prod_ENV_DEPLOY="Prod"
        Prod_HOST_PORT=8761
        Prod_CONT_PORT=8761
    }

parameters {
        choice (name: 'buildOnly', choices: 'no\nyes', description: "Only Build")
        choice (name: 'ScandOnly', choices: 'no\nyes', description: "Only Scan")
        choice (name: 'DockerPushOnly', choices: 'no\nyes', description: "Only Push to registry")
        choice (name: 'DeploytoDev', choices: 'no\nyes', description: "Only deploy to dev")
        choice (name: 'DeploytoTest', choices: 'no\nyes', description: "Only deploy to test")
        choice (name: 'DeploytoStage', choices: 'no\nyes', description: "Only deploy to stage")
        choice (name: 'DeploytoProd', choices: 'no\nyes', description: "Only deploy to prod")
    }

    stages{

        stage('Build'){
            when{
                anyOf{
                    expression{
                        params.buildOnly == 'yes'
                    }
                }
            }
            steps{
                script{
                echo "-----------Build Method ------------"
                echo "${WORKSPACE}"
                d.buildApp("${env.APPLICATION_NAME}")

                }
            }
        }

        stage("Docker build and Push"){
            when{
                anyOf{
                    expression{
                        params.DockerPushOnly == 'yes'
                    }
                }
            }
            steps{
                script{
                    echo "-----------Docker build and push Method ------------"
                    d.dockerBuildAndPush("${WORKSPACE}","${env.APPLICATION_NAME}","${env.POM_VERSION}","${env.POM_PACKAGING}","${env.DOCKER_HUB}","${env.DOCKER_CREDS_USR}","${env.DOCKER_CREDS_PSW}","${GIT_COMMIT}")
                }
                
            }
        }

        stage("Deploy to Dev"){
            when{
                anyOf{
                    expression{
                        params.DeploytoDev == 'yes'
                    }
                }
            }
            steps{
                script{
                    echo "-----------Docker Deploy ------------"
               d.imageValidation("${WORKSPACE}","${env.APPLICATION_NAME}","${env.POM_VERSION}","${env.POM_PACKAGING}","${env.DOCKER_HUB}","${env.DOCKER_CREDS_USR}","${env.DOCKER_CREDS_PSW}","${GIT_COMMIT}")  
                d.dockerDeploy("${docker_server_ip}","${env.APPLICATION_NAME}","${env.Dev_HOST_PORT}","${env.Dev_CONT_PORT}","${env.DOCKER_HUB}","${GIT_COMMIT}","${env.VM_CREDS_USR}","${env.VM_CREDS_PSW}","${ENV_DEPLOY}")
                }
            }
        }


        stage("Deploy to Test"){
            when{
                anyOf{
                    expression{
                        params.DeploytoDev == 'yes'
                    }
                }
            }
            steps{
                script{
                    echo "-----------Docker Deploy ------------"
               d.imageValidation("${WORKSPACE}","${env.APPLICATION_NAME}","${env.POM_VERSION}","${env.POM_PACKAGING}","${env.DOCKER_HUB}","${env.DOCKER_CREDS_USR}","${env.DOCKER_CREDS_PSW}","${GIT_COMMIT}")  
                d.dockerDeploy("${docker_server_ip}","${env.APPLICATION_NAME}","${env.Test_HOST_PORT}","${env.Test_CONT_PORT}","${env.DOCKER_HUB}","${GIT_COMMIT}","${env.VM_CREDS_USR}","${env.VM_CREDS_PSW}","${env.Test_ENV_DEPLOY}")
                }
            }
        }

}
}
}

