package com.piedrazul.clinicalrecords.observer;

import com.piedrazul.clinicalrecords.entity.ClinicalRecord;

import org.springframework.stereotype.Component;

@Component
public class NotificationObserver
        implements ClinicalRecordObserver {

    @Override
    public void update(
            ClinicalRecord clinicalRecord) {

        System.out.println(
                "[NOTIFICATION] Patient notified for clinical record: "
                        + clinicalRecord.getId());
    }
}