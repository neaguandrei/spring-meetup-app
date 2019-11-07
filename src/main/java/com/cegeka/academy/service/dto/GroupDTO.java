package com.cegeka.academy.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GroupDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 45)
    private String name;

    @Size(max = 45)
    private String description;

    public GroupDTO(){
    }

    public GroupDTO(Long id, @NotNull @Size(min = 1, max = 45) String name, @Size(max = 45) String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                "description='" + description + '\'' +
                "}";
    }
}
