package com.i27academy.builds

class Docker{
    def jenkins
    Docker(jenkins){
        this.jenkins=jenkins
    }

    def buildApp(appName){
        jenkins.sh """
       
         echo "Building the  Application $appName for shared library"
         mvn clean package -DskipTests=true
         """
    }

    // def imageValidation(){
    
    //     println ("Pulling the docker image")
    //     try{
    //         sh "docker pull ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
    //     }
    //     catch(Exception e){
    //         println ("Docker image with this tag doesnt exist, so creating the image")
    //         buildApp().call()
    //         dockerBuildAndPush().call()
    //     }
    // }

        def dockerBuildAndPush(workSpace,appName,pomVersion,pomPackaging,dockerHub,dockerUsr,dockerPsw,gitCommit){
        jenkins.sh """
        echo "--------------Inisde Docker Groovy buildandpush---------"
        echo "workspace is $workSpace"
        echo " appname is $appName"
        echo "pomVersion is $pomVersion"
        echo "packaging is $pomPackaging"
        echo "docker hub is $dockerHub"
        echo "docker usr is $dockerUsr"
        echo " psw is $dockerPsw"
        echo " commit is $gitCommit"
        cp $workSpace/target/i27-$appName-$pomVersion.$pomPackaging ./.cicd/
        echo "------------Building Docker Image--------"
        docker build --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT} ./.cicd/"
        echo "Build Done"
        echo "-----------Docker Login--------------"
        docker login -u ${env.DOCKER_CREDS_USR} -p ${env.DOCKER_CREDS_PSW}"
        echo "-------------Docker Push-----------"
        docker push ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
        """
    }


//     def dockerDeploy(envDeploy,hostPort, containerPort){
//     return{
//          echo ("-----------Deploying to ${envDeploy} Env---------")
//                 withCredentials([usernamePassword(credentialsId: 'ali_docker_vm_cred', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
//                     script{
//                          sh "sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no ${USERNAME}@${docker_server_ip} docker pull ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
                    
//                     try{
//                         //Stop the container
//                       sh "sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no ${USERNAME}@${docker_server_ip} docker stop  ${env.APPLICATION_NAME}-${envDeploy}"
//                         // Remove the container
//                       sh "sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no ${USERNAME}@${docker_server_ip} docker rm  ${env.APPLICATION_NAME}-${envDeploy}"   
//                     }  catch(err)
//                     {
//                         echo "Error Caught: $err"
                       
//                     }
//                      echo " Creating Container"
//                        sh "sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no ${USERNAME}@${docker_server_ip} docker run -d -p ${hostPort}:${containerPort} --name ${env.APPLICATION_NAME}-${envDeploy} ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
//                     }
//                 }
//     }
// }

}

    //Docker Build


    // Docker Login

    // Docker push