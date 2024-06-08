package com.example.demo.service;

import com.example.demo.db.entity.CartItemEntity;
import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.repository.MedicineRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.text.Collator;


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
    public MedicineEntity saveMedicine(MedicineEntity medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public void deleteMedicineById(Long id) {
        medicineRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void decreaseMedicineStockCount(List<CartItemEntity> cartItems) {
        for (CartItemEntity cartItem : cartItems) {
            MedicineEntity medicine = cartItem.getMedicine();
            int newStockCount = medicine.getStockCount() - cartItem.getQuantity();

            // Ensure stock count doesn't go below zero
            if (newStockCount < 0) {
                newStockCount = 0;
            }

            medicine.setStockCount(newStockCount);
            medicineRepository.save(medicine);
        }
    }

    private final Collator collator = Collator.getInstance(new Locale("bg", "BG"));

    @Override
    public List<MedicineEntity> sortMedicines(List<MedicineEntity> medicinesList, String sortBy) {
        Comparator<MedicineEntity> comparator = Comparator.comparing(MedicineEntity::getMedicineName, collator);
        switch (sortBy) {
            case "ascending":
                medicinesList.sort(comparator);
                break;
            case "descending":
                medicinesList.sort(comparator.reversed());
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
    public List<MedicineEntity> searchMedicines(List<MedicineEntity> medicinesList, String searchText) {
        List<MedicineEntity> filteredMedicines = new ArrayList<>();
        for (MedicineEntity medicine : medicinesList) {
            if (matchesIgnoreCaseAndPartial(medicine.getMedicineName(), searchText)) {
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
