package com.projectalpha.model.user.diner;

import com.projectalpha.model.user.UserProfileDiner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "custom_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "name")
    private String name;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "like_counter")
    private BigDecimal likeCounter;

    @Column(name = "user_profile_diner_id")
    private UUID dinerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_diner_id", insertable = false, updatable = false)
    private UserProfileDiner diner;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomListItem> items = new ArrayList<>();
}