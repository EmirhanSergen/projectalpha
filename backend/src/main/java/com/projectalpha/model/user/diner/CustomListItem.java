package com.projectalpha.model.user.diner;

import com.projectalpha.model.business.Business;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import com.projectalpha.model.user.UserProfileDiner;


import java.util.UUID;

@Entity
@Table(name = "custom_list_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "list_id")
    private UUID listId;

    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "diner_id")
    private UUID dinerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", insertable = false, updatable = false)
    private CustomList list;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", insertable = false, updatable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diner_id", insertable = false, updatable = false)
    private UserProfileDiner diner;
}