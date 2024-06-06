package com.example.demo.service;

import com.example.demo.db.entity.RateEntity;

import java.util.List;

public interface RateService {
    void saveRating(RateEntity rateEntity);
    List<RateEntity> findRatesByMedicineId(Long id);
}
