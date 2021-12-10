package com.apriori.database.entity.bcs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "parts")

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partId")
    private Integer partId;

    @Column(name = "part")
    private String part;

    @Column(name = "size_kb")
    private Float sizeKb;

    @OneToMany(mappedBy = "partEntity", cascade = CascadeType.ALL)
    private Set<BatchPartSummaryEntity> batchPartSummaries;
}
