package com.apriori.database.entity.bcs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "batch_part_summary")

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchPartSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "state")
    private String state;

    @Column(name = "costing_duration")
    private Long costingDuration;

    @Column(name = "process_group")
    private String processGroup;

    @Column(name = "material_name")
    private String materialName;

    @Column(name = "filename")
    private String filename;

    @Column(name = "batch_size")
    private Integer batchSize;

    @Column(name = "annual_volume")
    private Integer annualVolume;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private BatchEntity batchEntity;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private PartEntity partEntity;
}
