package com.civicresolve.repository;
import com.civicresolve.model.Complaint; import org.springframework.data.jpa.repository.*; import java.util.*;
public interface ComplaintRepository extends JpaRepository<Complaint,Long>{
 List<Complaint> findByCitizenIdOrderByCreatedAtDesc(Long citizenId);
 Optional<Complaint> findByTrackingNumber(String tracking);
 long countByStatus(String status);
}