package com.civicresolve.repository;
import com.civicresolve.model.ComplaintHistory; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ComplaintHistoryRepository extends JpaRepository<ComplaintHistory,Long>{
 List<ComplaintHistory> findByComplaintIdOrderByCreatedAtAsc(Long id);
}