package com.example.demo.service;

import com.example.demo.db.entity.MedicineEntity;

import java.util.List;

public interface MedicineService {
    List<MedicineEntity> findAllMedicines();
    MedicineEntity getRandomMedicine();
    MedicineEntity getMedicineById(Long id);
    MedicineEntity saveMedicine(MedicineEntity Medicine);
    void deleteMedicineById(Long id);
    List<MedicineEntity> filterMedicines(List<MedicineEntity> MedicinesList, String filterBy, String filterText);
    List<MedicineEntity> sortMedicines(List<MedicineEntity> MedicinesList, String sortBy);
    void decreaseMedicineStockCount(MedicineEntity Medicine);
}