package com.piedrazul.clinicalrecords.observer;

import com.piedrazul.clinicalrecords.entity.ClinicalRecord;

public interface ClinicalRecordObserver {

    void update(ClinicalRecord clinicalRecord);
}