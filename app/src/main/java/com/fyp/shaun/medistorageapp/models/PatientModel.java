package com.fyp.shaun.medistorageapp.models;

import java.io.Serializable;

/**
 * This is a PatientModel POJO
 */
public class PatientModel implements Serializable{

    private String _id;
    private String name;
    private Address address;
    private String illness;
    private Links links;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return  _id + "\n" + name + "\nAddress :\n" + address.getAddressLine1() + "\n"
                + address.getAddressLine2() + "\n" + address.getCity() + "\n"
                + address.getCounty() +"\n" + address.getCountry() + "\n" + address.getPostCode() + "\n" + illness;
    }
}