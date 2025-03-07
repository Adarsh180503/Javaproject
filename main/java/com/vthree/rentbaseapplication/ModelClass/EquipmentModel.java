package com.vthree.rentbaseapplication.ModelClass;

import java.io.Serializable;

public class EquipmentModel implements Serializable, Comparable<EquipmentModel>{
    String equipment_id;
    String equipment_name;
    String user_id;
    String description;
    String image;
    String address;
    String city;
    String taluka;
    String priseinhr;
    String deposite;
    String contact;
    double latitude;
    double longitude;
    double dist;

    public EquipmentModel() {
    }

/*
    public EquipmentModel(String equipment_id, String equipment_name, String description, String image, String address,
                          String priseinhr, String deposite) {
        this.equipment_id = equipment_id;
        this.equipment_name = equipment_name;
        this.description = description;
        this.image = image;
        this.address = address;
        this.priseinhr = priseinhr;
        this.deposite = deposite;

    }

    public EquipmentModel(String equipment_id, String equipment_name,String user_id, String description, String image,
                          String address, String city, String taluka, String priseinhr, String deposite, String contact) {
        this.equipment_id = equipment_id;
        this.equipment_name = equipment_name;
        this.user_id=user_id;
        this.description = description;
        this.image = image;
        this.address = address;
        this.city = city;
        this.taluka = taluka;
        this.priseinhr = priseinhr;
        this.deposite = deposite;
        this.contact = contact;

    }

    public EquipmentModel(String equipment_id, String equipment_name, String description, String address, String city,
                          String taluka, String priseinhr, String deposite, String contact) {
        this.equipment_id = equipment_id;
        this.equipment_name = equipment_name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.taluka = taluka;
        this.priseinhr = priseinhr;
        this.deposite = deposite;
        this.contact = contact;

    }
*/

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPriseinhr() {
        return priseinhr;
    }

    public void setPriseinhr(String priseinhr) {
        this.priseinhr = priseinhr;
    }

    public String getDeposite() {
        return deposite;
    }

    public void setDeposite(String deposite) {
        this.deposite = deposite;
    }

    public EquipmentModel(String equipment_id, String equipment_name, String user_id, String description, String image,
                          String address, String city, String taluka, String priseinhr, String deposite, String contact,
                          double latitude, double longitude, double dist) {
        this.equipment_id = equipment_id;
        this.equipment_name = equipment_name;
        this.user_id = user_id;
        this.description = description;
        this.image = image;
        this.address = address;
        this.city = city;
        this.taluka = taluka;
        this.priseinhr = priseinhr;
        this.deposite = deposite;
        this.contact = contact;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dist = dist;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }


    @Override
    public int compareTo(EquipmentModel comparestu) {
        double compareprice=((EquipmentModel)comparestu).getDist();
        /* For Ascending order*/

       // double val=this.dist-compareprice;
        return Double.compare(this.dist ,compareprice);

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

    @Override
    public String toString() {
        return "[ user_id=" + user_id + ", name=" + equipment_name + ", dist=" + dist + "]";
    }

}
