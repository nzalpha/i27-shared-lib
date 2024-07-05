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
         gcloud container clusters get-credentials cart-dev-ns --zone us-central1-c --project boutique-424803
         gcloud compute instances list
         echo "-----------------Get nodes in the cluster-------------------"
         kubectl get nodes
         """
    }
}