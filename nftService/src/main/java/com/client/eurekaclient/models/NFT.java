package com.client.eurekaclient.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "nfts")
public class NFT implements Comparable<NFT> {
    @Id
    private String id;
    private String address = null;
    @NotBlank
    @Size(max = 30)
    public String name;
    @NotBlank
    public String imageLink = "null";
    @NotBlank
    @Size(max = 30)
    public String origin;
    @NotBlank
    @Size(max = 30)
    public String status;
    public double age = 70;
    public double breed = 0;
    @Indexed(unique=true)
    public long index = 0;
    public Map<String, String> skills;
    public String tribe = "null";
    public long activeTill;
    public String chain = "null";

    public NFT(String name, String imageLink, String origin, String status, double age, double breed, long index, Map<String, String> skills, String tribe, long activeTill, String chain) {
        this.name = name;
        this.imageLink = imageLink;
        this.origin = origin;
        this.status = status;
        this.age = age;
        this.breed = breed;
        this.index = index;
        this.skills = skills;
        this.tribe = tribe;
        this.activeTill = activeTill;
        this.chain = chain;
    }

    public long getActiveTill() {
        return activeTill;
    }

    public void setActiveTill(long activeTill) {
        this.activeTill = activeTill;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getBreed() {
        return breed;
    }

    public void setBreed(double breed) {
        this.breed = breed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addValueToMap(String key, String value) {
        this.skills.put(key, value);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Map<String, String> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, String> skills) {
        this.skills = skills;
    }

    public String getTribe() {
        return tribe;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    @Override
    public int compareTo(NFT otherNft) {
        return Long.valueOf(this.getIndex()).compareTo(otherNft.getIndex());
    }

    @Override
    public String toString() {
        return String.format("NFT{name=%s,origin=%s,status=%s,age=%s,breed=%s,skills=%s}", this.name, this.origin, this.status, this.age, this.breed, this.skills);
    }
}
