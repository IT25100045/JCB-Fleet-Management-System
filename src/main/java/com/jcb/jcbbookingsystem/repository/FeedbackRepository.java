package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByIncidentId(Long incidentId);

    List<Feedback> findByOperatorUsername(String operatorUsername);
}
