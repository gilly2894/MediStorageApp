package com.fyp.shaun.medistorageapp.models;

import java.io.Serializable;

/**
 * This is a Links POJO
 */
public class Links implements Serializable {

    private String link;
    private String rel;

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }
}
