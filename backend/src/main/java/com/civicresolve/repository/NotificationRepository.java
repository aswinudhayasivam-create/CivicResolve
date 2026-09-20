package com.civicresolve.repository;
import com.civicresolve.model.AppNotification; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface NotificationRepository extends JpaRepository<AppNotification,Long>{
 List<AppNotification> findByUserIdOrderByCreatedAtDesc(Long userId); long countByUserIdAndReadFalse(Long userId);
}
