package com.civicresolve.repository;
import com.civicresolve.model.Category; import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category,Long>{}