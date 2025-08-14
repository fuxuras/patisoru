package com.fuxuras.patisoru.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "user_id")
    private UUID userId;
}
