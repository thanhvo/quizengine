package com.vvt.quizengine.repository;

import com.vvt.quizengine.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {
}
