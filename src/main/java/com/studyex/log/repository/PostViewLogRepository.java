package com.studyex.log.repository;

import com.studyex.log.entity.PostViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewLogRepository extends JpaRepository<PostViewLog, Long> {
}
