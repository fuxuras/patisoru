package com.fuxuras.patisoru.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fuxuras.patisoru.entities.abstracts.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseEntity {
    private String email;
    private String password;
    private String fullName;
    private String role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;
}
