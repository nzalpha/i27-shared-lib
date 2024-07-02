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

    def imageValidation(workSpace,appName,pomVersion,pomPackaging,dockerHub,dockerUsr,dockerPsw,gitCommit){
            
        try{
            sh "docker pull $dockerHub/$appName:$gitCommit"
        }
        catch(Exception e){
            println ("Docker image with this tag doesnt exist, so creating the image")
            buildApp(appName)
            dockerBuildAndPush(workSpace,appName,pomVersion,pomPackaging,dockerHub,dockerUsr,dockerPsw,gitCommit)
        }
    }
            
            
       
    

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
        docker build --build-arg JAR_SOURCE=i27-$appName-$pomVersion.$pomPackaging -t $dockerHub/$appName:$gitCommit ./.cicd/
        echo "Build Done"
        echo "-----------Docker Login--------------"
        docker login -u $dockerUsr -p $dockerPsw
        echo "-------------Docker Push-----------"
        docker push $dockerHub/$appName:$gitCommit
        """
    }


    def dockerDeploy(dockerServerIp,appName,DevHostPort,DevContPort,dockerHub,gitCommit,VMCredUsr,VMCredPsw,envDeploy){

        jenkins.sh """
        echo "--------------Inisde Docker Groovy DockerDeploy---------"
        echo "dockerServerIp is $dockerServerIp"
        echo " DevHostPort is $DevHostPort"
        echo "DevContPort is $DevContPort"
        echo "dockerHub is $dockerHub"
        echo "VMCredUsr is $VMCredUsr"
        echo "VMCredPsw usr is $VMCredPsw"
        sshpass -p $VMCredPsw ssh -o StrictHostKeyChecking=no $VMCredUsr@$dockerServerIp docker pull $dockerHub/$appName:$gitCommit
        sshpass -p $VMCredPsw ssh -o StrictHostKeyChecking=no $VMCredUsr@$dockerServerIp docker run -d -p $DevHostPort:$DevContPort --name $appName-$envDeploy $dockerHub/$appName:$gitCommit
        """
    
}

}

    //Docker Build


    // Docker Login

    // Docker push