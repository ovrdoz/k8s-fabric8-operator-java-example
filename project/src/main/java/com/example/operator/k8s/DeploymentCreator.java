package com.example.operator.k8s;

import com.example.operator.model.MyResource;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class DeploymentCreator {

    private final KubernetesClient kubernetesClient;

    public DeploymentCreator(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public void createMyResourceeDeployment(MyResource myResource) {
            final Deployment deployment = new DeploymentBuilder()
                    .withNewMetadata()
                    .withName(myResource.getSpec().getMyResourceName())
                    .withNamespace(myResource.getSpec().getNamespace())
                    .endMetadata()
                    .withNewSpec()
                    .withReplicas(1)
                    .withNewSelector()
                    .addToMatchLabels("app", myResource.getSpec().getMyResourceName())
                    .endSelector()
                    .withNewTemplate()
                    .withNewMetadata()
                    .addToLabels("app", myResource.getSpec().getMyResourceName())
                    .addToLabels("managed-by", "my-operator")
                    .endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName(myResource.getSpec().getMyResourceName())
                    .withImage(myResource.getSpec().getMyResourceImage())
                    .withCommand("sh", "-c", "while true; do echo 'Hello, I am a test-mode!'; sleep 10; done")
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();

            kubernetesClient.apps().deployments().inNamespace(myResource.getSpec().getNamespace()).createOrReplace(deployment);

    }
    
   

    public static void deleteDeploymentIfExists(KubernetesClient kubernetesClient, String appName, String namespace) {
        System.out.println("DELETING DEPLOY APP "+ appName +" - "+namespace);
        kubernetesClient.apps().deployments()
            .inNamespace(namespace)
            .withLabel("app", appName)
            .delete();
    }

}
