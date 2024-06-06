package com.example.demo.service;

import com.example.demo.db.entity.RateEntity;
import com.example.demo.db.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final RateRepository rateRepository;

    @Override
    public void saveRating(RateEntity rateEntity) {
        rateRepository.save(rateEntity);
    }


    @Override
    public List<RateEntity> findRatesByMedicineId(Long medicineId) {
        return rateRepository.findRatesByMedicineId(medicineId);
    }
}
