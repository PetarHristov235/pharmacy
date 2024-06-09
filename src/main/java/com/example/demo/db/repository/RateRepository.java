package com.example.demo.db.repository;

import com.example.demo.db.entity.RateEntity;
import com.example.demo.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RateRepository extends JpaRepository<RateEntity,Long> {

}