package com.consortium.admin.dto;

import com.consortium.admin.entity.Person;

public class PersonResponse {

    private Long id;
    private String name;
    private String email;
    private String avatar;

    public PersonResponse() {
    }

    public PersonResponse(Long id, String name, String email, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }

    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getName(),
                person.getEmail(),
                person.getAvatar()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
