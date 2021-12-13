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
@Table(name = "batches")

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchEntity {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Id
    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "customer_identity")
    private String customerIdentity;

    @Column(name = "rollup_name")
    private String rollupName;

    @Column(name = "rollup_scenario_name")
    private String rollupScenarioName;

    @OneToMany(mappedBy = "batchEntity", cascade = CascadeType.ALL)
    private Set<BatchPartSummaryEntity> batchPartSummaries;
}
