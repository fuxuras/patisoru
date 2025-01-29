package com.fuxuras.patisoru.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fuxuras.patisoru.entities.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity{
    private String title;
    private String text;
    private Long likeCount;

    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    List<Like> likes;

    @PrePersist
    public void prePersist(){
        if(likeCount == null){
            likeCount = 0L;
        }
    }

}
