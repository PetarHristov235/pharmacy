package com.example.demo.service;

import com.example.demo.db.entity.CartItemEntity;
import com.example.demo.db.entity.MedicineEntity;

import java.util.List;

public interface MedicineService {
    List<MedicineEntity> findAllMedicines();
    MedicineEntity getMedicineById(Long id);
    MedicineEntity saveMedicine(MedicineEntity Medicine);
    void deleteMedicineById(Long id);
    List<MedicineEntity> filterMedicines(List<MedicineEntity> MedicinesList, String filterBy);
    List<MedicineEntity> sortMedicines(List<MedicineEntity> MedicinesList, String sortBy);
    List<MedicineEntity> searchMedicines(List<MedicineEntity> MedicinesList, String filterText);
}