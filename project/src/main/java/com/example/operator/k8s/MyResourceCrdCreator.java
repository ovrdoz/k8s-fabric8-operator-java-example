package com.example.operator.k8s;

import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.JSONSchemaProps;
import io.fabric8.kubernetes.api.model.apiextensions.v1.JSONSchemaPropsBuilder;

import java.util.HashMap;
import java.util.Map;

public class MyResourceCrdCreator {

    public static CustomResourceDefinition createMyResourceCRD() {
        JSONSchemaProps specProperties = new JSONSchemaPropsBuilder()
                .withType("object")
                .withProperties(createSpecProperties())
                .build();

        return new CustomResourceDefinitionBuilder()
                .withApiVersion("apiextensions.k8s.io/v1")
                .withNewMetadata()
                .withName("myresources.operator.example.com")
                .endMetadata()
                .withNewSpec()
                .withGroup("operator.example.com")
                .addNewVersion()
                .withName("v1")
                .withServed(true)
                .withStorage(true)
                .withNewSchema()
                .withNewOpenAPIV3Schema()
                .withType("object")
                .withProperties(Map.of("spec", specProperties))
                .endOpenAPIV3Schema()
                .endSchema()
                .endVersion()
                .withScope("Namespaced")
                .withNewNames()
                .withPlural("myresources")
                .withSingular("myresource")
                .withKind("MyResource")
                .addToShortNames("rm")
                .endNames()
                .endSpec()
                .build();
    }

    private static Map<String, JSONSchemaProps> createSpecProperties() {
        Map<String, JSONSchemaProps> properties = new HashMap<>();

        properties.put("namespace", new JSONSchemaPropsBuilder().withType("string").build());
        properties.put("myResourceName", new JSONSchemaPropsBuilder().withType("string").build());
        properties.put("myResourceImage", new JSONSchemaPropsBuilder().withType("string").build());

        return properties;
    }
}
