package com.i27academy.k8s

class K8s{
    def jenkins
    K8s(jenkins){
        this.jenkins=jenkins
    }

    // Method to authenticate to cluster

      def auth_login(appName){
        jenkins.sh """
         echo "Entering into K8 authentication"
         
         gcloud compute instances list
         """
    }
}