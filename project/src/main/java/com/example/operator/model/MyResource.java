package com.example.operator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("operator.example.com")
@Version("v1")
public class MyResource extends CustomResource implements Namespaced {

    private MyResourceSpec spec;

    public MyResourceSpec getSpec() {
        return spec;
    }

    public void setSpec(MyResourceSpec spec) {
        this.spec = spec;
    }

    public static class MyResourceSpec {

        @JsonProperty("MyResourceName")
        private String myResourceName;

        @JsonProperty("namespace")
        private String namespace;

        @JsonProperty("MyResourceImage")
        private String myResourceImage;


        public String getMyResourceImage() {
            return myResourceImage;
        }

        public void setMyResourceImage(String myResourceImage) {
            this.myResourceImage = myResourceImage;
        }

        public String getMyResourceName() {
            return myResourceName;
        }
    
        public void setMyResourceName(String myResourceName) {
            this.myResourceName = myResourceName;
        }
    
        public String getNamespace() {
            return namespace;
        }
    
        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }
        
    }
}
