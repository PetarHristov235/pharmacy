package com.example.demo.db.repository;

import com.example.demo.db.entity.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, Long> {

    @Query("""
            SELECT b FROM MedicineEntity b
            WHERE b.medicineName = :medicineName""")
    MedicineEntity findBookByBookName(@Param("medicineName") String medicineName);

    @Query("""
            DELETE FROM MedicineEntity b
            WHERE b.medicineName = :medicineName""")
    void deleteMedicineByMedicineName(@Param("medicineName") String medicineName);

    @Query("""
            SELECT b FROM MedicineEntity b
            WHERE b.id = :id""")
    Optional<MedicineEntity> findById(Long id);
}
