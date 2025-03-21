package org.asansocketserver.domain.ward.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "ward")
@Entity
public class Ward {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String imageUrl;
    @OneToMany(mappedBy = "ward" , orphanRemoval = true)
    private List<Sector> sectors = new ArrayList<>();

}


