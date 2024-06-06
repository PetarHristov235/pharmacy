package com.example.demo.service;

import com.example.demo.db.entity.MedicineEntity;
import com.example.demo.db.repository.MedicineRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<MedicineEntity> sortMedicines(List<MedicineEntity> medicinesList, String sortBy) {
        if (isValid(sortBy)) {
            switch (sortBy) {
                case "title":
                    medicinesList.sort(Comparator.comparing(MedicineEntity::getMedicineName));
                    break;
                case "isPrescriptionRequired":
                    medicinesList.sort(Comparator.comparing(MedicineEntity::getIsPrescriptionRequired));
                    break;
            }
        }
        return medicinesList;
    }

    @Override
    public void decreaseMedicineStockCount(MedicineEntity medicine) {
        medicine.setStockCount(medicine.getStockCount() - 1);
    }

    @Override
    public List<MedicineEntity> filterMedicines(List<MedicineEntity> medicinesList, String filterBy, String filterText) {
        if (isValid(filterBy) && isValid(filterText)) {
            switch (filterBy) {
                case "title" -> medicinesList = filterByTitle(medicinesList, filterText);
                case "isPrescriptionRequired" ->
                        medicinesList.sort(Comparator.comparing(MedicineEntity::getIsPrescriptionRequired));
            }
        }
        return medicinesList;
    }

    private List<MedicineEntity> filterByTitle(List<MedicineEntity> medicines, String title) {
        List<MedicineEntity> filteredMedicines = new ArrayList<>();
        for (MedicineEntity medicine : medicines) {
            if (matchesIgnoreCaseAndPartial(medicine.getMedicineName(), title)) {
                filteredMedicines.add(medicine);
            }
        }
        return filteredMedicines;
    }

    private boolean matchesIgnoreCaseAndPartial(String text, String keyword) {
        String lowercaseText = text.toLowerCase();
        String lowercaseKeyword = keyword.toLowerCase();

        return lowercaseText.contains(lowercaseKeyword);
    }

    private boolean isValid(String str) {
        return str != null && !str.isEmpty();
    }
}
