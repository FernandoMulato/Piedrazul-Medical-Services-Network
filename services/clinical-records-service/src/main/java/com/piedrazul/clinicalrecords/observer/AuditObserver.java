package com.piedrazul.clinicalrecords.observer;

import com.piedrazul.clinicalrecords.entity.ClinicalRecord;

import org.springframework.stereotype.Component;

@Component
public class AuditObserver
        implements ClinicalRecordObserver {

    @Override
    public void update(
            ClinicalRecord clinicalRecord) {

        System.out.println(
                "[AUDIT] Clinical record created: "
                        + clinicalRecord.getId());
    }
}