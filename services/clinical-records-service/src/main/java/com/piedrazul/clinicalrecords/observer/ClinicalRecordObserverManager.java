package com.piedrazul.clinicalrecords.observer;

import com.piedrazul.clinicalrecords.entity.ClinicalRecord;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClinicalRecordObserverManager {

    private final List<ClinicalRecordObserver> observers;

    public void notifyObservers(
            ClinicalRecord clinicalRecord) {

        observers.forEach(observer ->
                observer.update(clinicalRecord));
    }
}