package com.example.operator;

import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;

import com.example.operator.k8s.DeploymentCreator;
import com.example.operator.k8s.MyResourceCrdCreator;
import com.example.operator.model.MyResource;

public class MyResourceOperator {

    private final KubernetesClient kubernetesClient;

    public MyResourceOperator(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public void createOrUpdateCRD() {
        try {
            CustomResourceDefinition crd = MyResourceCrdCreator.createMyResourceCRD();
    
            String crdName = crd.getMetadata().getName();
            CustomResourceDefinition existingCRD = kubernetesClient.apiextensions().v1().customResourceDefinitions().withName(crdName).get();
    
            if (existingCRD == null) {
                kubernetesClient.apiextensions().v1().customResourceDefinitions().create(crd);
                System.out.println("Created CRD: " + crdName);
            } else {
                kubernetesClient.apiextensions().v1().customResourceDefinitions().withName(crdName).replace(crd);
                System.out.println("Updated CRD: " + crdName);
            }
    
        } catch (KubernetesClientException e) {
            System.err.println("Error creating or updating CRD: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public void watchForMyResourceChanges() {
        kubernetesClient.resources(MyResource.class)
                .inAnyNamespace()
                .watch(new Watcher<MyResource>() {
                    @Override
                    public void eventReceived(Action action, MyResource myResource) {
                        System.out.println("Received " + action + " for " + myResource.getMetadata().getName());
                        String namespace = myResource.getMetadata().getNamespace();
                        String myResourceName = myResource.getSpec().getMyResourceName();
                        System.out.println("Event: " + action + ", MyResource: " + myResourceName+ ", Namespace: " + namespace);

                        switch (action) {
                            case ADDED:
                                handleMyResourceModification(myResource);
                                break;
                            case MODIFIED:
                                handleMyResourceModification(myResource);
                                break;
                            case DELETED:
                                handleMyResourceDeletion(myResource);
                                break;
                            case ERROR:
                                System.err.println("Error watching MyResource: " + myResourceName);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                public void onClose(WatcherException cause) {
                    if (cause != null) {
                        System.err.println("Watcher onClose() with exception: " + cause.getMessage());
                    } else {
                        System.out.println("Watcher onClose() without exception.");
                    }
                }
                });
    
}



    private void handleMyResourceModification(MyResource myResource) {
        System.out.println("handleMyResourceModification: " + myResource.getMetadata().getName());
    
        // Create or update the Deployment resources
        DeploymentCreator deploymentCreator = new DeploymentCreator(kubernetesClient);
        deploymentCreator.createMyResourceeDeployment(myResource);
    }
    

    private void handleMyResourceDeletion(MyResource myResource) {
        System.out.println("handleMyResourceModification: " + myResource.getMetadata().getName());
    }

    public static void main(String[] args) {
        Config config = new ConfigBuilder().build();
        KubernetesClient kubernetesClient = new DefaultKubernetesClient(config);

        MyResourceOperator myResourceOperator = new MyResourceOperator(kubernetesClient);
        myResourceOperator.createOrUpdateCRD();
    }

}


