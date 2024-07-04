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
         # gcloud auth activate-service-account jenkins@boutique-424803.iam.gserviceaccount.com --key-file=${gke_sa_key}
         gcloud compute instances list
         """
    }
}