package com.i27academy.k8s

class K8s{
    def jenkins
    K8s(jenkins){
        this.jenkins=jenkins
    }

    // Method to authenticate to cluster

      def auth_login(gke_cluster_name,gke_zone,gke_project){
        jenkins.sh """
         echo "Entering into K8 authentication"
         gcloud container clusters get-credentials $gke_cluster_name --zone $gke_zone --project $gke_project
         gcloud compute instances list
         echo "-----------------Get nodes in the cluster-------------------"
         kubectl get nodes
         """
    }


      def k8sdeploy(fileName,namespace){
         jenkins.sh """
         echo "Entering into K8 deploy"
         kubectl apply -f ./.cicd/$fileName -n $namespace
         """
      }
}