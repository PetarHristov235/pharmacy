package com.example.demo.service;

import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.repository.MedicineRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;

    @Override
    public List<MedicineEntity> findAllMedicines() {
        return medicineRepository.findAll();
    }

    @Override
    public MedicineEntity getRandomMedicine() {
        List<MedicineEntity> medicinesList = medicineRepository.findAll();
        if (medicinesList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(medicinesList.size());
        MedicineEntity randomMedicine = medicinesList.get(randomIndex);

        if (randomMedicine.getStockCount() == 0) {
            getRandomMedicine();
        }
        return randomMedicine;
    }

    @Override
    public MedicineEntity getMedicineById(Long id) {
        Optional<MedicineEntity> optMedicineEntity = medicineRepository.findById(id);
        return optMedicineEntity.orElse(null);
    }

    @Override
    public MedicineEntity saveMedicine(MedicineEntity Medicine) {
        return medicineRepository.save(Medicine);
    }

    @Override
    public void deleteMedicineById(Long id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public void decreaseMedicineStockCount(MedicineEntity medicine) {
        medicine.setStockCount(medicine.getStockCount() - 1);
        medicineRepository.save(medicine);
    }

    @Override
    public List<MedicineEntity> sortMedicines(List<MedicineEntity> medicinesList, String sortBy) {
            switch (sortBy) {
                case "ascending":
                    medicinesList.sort(Comparator.comparing(MedicineEntity::getMedicineName));
                    break;
                case "descending":
                    medicinesList.sort(Comparator.comparing(MedicineEntity::getMedicineName).reversed());
                    break;
            }
        return medicinesList;
    }

    @Override
    public List<MedicineEntity> filterMedicines(List<MedicineEntity> medicinesList, String filterBy) {
            switch (filterBy)
            {
                case "withPrescription" -> medicinesList = filterMedicinesWithPrescription(medicinesList);
                case "withoutPrescription" -> medicinesList = filterMedicinesWithoutPrescription(medicinesList);
            }
        return medicinesList;
    }

    @Override
    public List<MedicineEntity> searchMedicines(List<MedicineEntity> medicinesList, String filterText) {
        List<MedicineEntity> filteredMedicines = new ArrayList<>();
        for (MedicineEntity medicine : medicinesList) {
            if (matchesIgnoreCaseAndPartial(medicine.getMedicineName(), filterText)) {
                filteredMedicines.add(medicine);
            }
        }
        return filteredMedicines;
    }

    private List<MedicineEntity> filterByName(List<MedicineEntity> medicines, String title) {
        List<MedicineEntity> filteredMedicines = new ArrayList<>();
        for (MedicineEntity medicine : medicines) {
            if (matchesIgnoreCaseAndPartial(medicine.getMedicineName(), title)) {
                filteredMedicines.add(medicine);
            }
        }
        return filteredMedicines;
    }

    private List<MedicineEntity> filterMedicinesWithPrescription(List<MedicineEntity> medicines) {
        return medicines.stream()
                .filter(MedicineEntity::getIsPrescriptionRequired)
                .collect(Collectors.toList());
    }

    private List<MedicineEntity> filterMedicinesWithoutPrescription(List<MedicineEntity> medicines) {
        return medicines.stream()
                .filter(medicine -> !medicine.getIsPrescriptionRequired())
                .collect(Collectors.toList());
    }

    private boolean matchesIgnoreCaseAndPartial(String text, String keyword) {
        String lowercaseText = text.toLowerCase();
        String lowercaseKeyword = keyword.toLowerCase();

        return lowercaseText.contains(lowercaseKeyword);
    }

}
