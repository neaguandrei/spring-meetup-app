package com.cegeka.academy.service.dto;

import javax.validation.constraints.Size;

public class RoleDTO {

    private Long id;

    @Size(min = 1, max = 45)
    private String name;

    public RoleDTO(){

    }

    public RoleDTO(Long id, @Size(min = 1, max = 45) String name) {
        this.id = id;
        this.name = name;
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


}
