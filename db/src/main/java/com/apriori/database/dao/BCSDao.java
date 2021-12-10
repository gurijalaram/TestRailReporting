package com.apriori.database.dao;

import com.apriori.database.dto.BCSBatchDTO;
import com.apriori.database.dto.BCSPartBenchmarkingDTO;
import com.apriori.database.entity.bcs.BatchEntity;
import com.apriori.database.entity.bcs.BatchPartSummaryEntity;
import com.apriori.database.entity.bcs.PartEntity;
import com.apriori.database.utils.HibernateUtil;

import org.hibernate.Session;

import java.util.Collection;

public class BCSDao {
    private static Session session = HibernateUtil.getSessionFactory().openSession();

    public static void insertCostingData(final BCSBatchDTO batchData, Collection<BCSPartBenchmarkingDTO> partsBenchData) {
        session.beginTransaction();

        try {
            partsBenchData.forEach(part -> {
                session.save(BatchPartSummaryEntity.builder()
                    .partName(part.getPartName())
                    .errorMessage(part.getErrorMessage())
                    .state(part.getState())
                    .costingDuration(part.getCostingDuration())
                    .annualVolume(part.getAnnualVolume())
                    .batchSize(part.getBatchSize())
                    .filename(part.getFilename())
                    .materialName(part.getMaterialName())
                    .processGroup(part.getProcessGroup())
                    .partEntity(getOrCreatePartByName(part.getPartName()))
                    .batchEntity(getOrCreateBatchById(batchData))
                    .build()
                );
            });

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }

        HibernateUtil.finalizeSession();
    }

    public static PartEntity getOrCreatePartByName(String partName) {
        PartEntity partEntity = session.createQuery("from PartEntity p where p.part = :partName", PartEntity.class)
            .setParameter("partName", partName).uniqueResult();

        if (partEntity == null) {
            partEntity = PartEntity.builder()
                .part(partName)
                .build();

            session.save(partEntity);
        }

        return partEntity;
    }

    private static BatchEntity getOrCreateBatchById(BCSBatchDTO bcsBatchDTO) {
        BatchEntity batchEntity = session.createQuery("from BatchEntity where batchId = :id", BatchEntity.class)
            .setParameter("id", bcsBatchDTO.getBatchId()).uniqueResult();

        if (batchEntity == null) {
            batchEntity = BatchEntity.builder()
                .batchId(bcsBatchDTO.getBatchId())
                .externalId(bcsBatchDTO.getExternalId())
                .customerIdentity(bcsBatchDTO.getCustomerIdentity())
                .rollupName(bcsBatchDTO.getRollupName())
                .rollupScenarioName(bcsBatchDTO.getRollupScenarioName())
                .build();

            session.save(batchEntity);
        }

        return batchEntity;
    }
}
