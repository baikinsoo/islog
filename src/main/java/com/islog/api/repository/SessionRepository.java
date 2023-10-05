package com.islog.api.repository;

import com.islog.api.domain.Member;
import com.islog.api.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
