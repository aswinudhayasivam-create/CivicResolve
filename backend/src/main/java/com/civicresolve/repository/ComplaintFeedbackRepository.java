package com.civicresolve.repository;
import com.civicresolve.model.ComplaintFeedback; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ComplaintFeedbackRepository extends JpaRepository<ComplaintFeedback,Long>{ Optional<ComplaintFeedback> findByComplaintId(Long complaintId); }
