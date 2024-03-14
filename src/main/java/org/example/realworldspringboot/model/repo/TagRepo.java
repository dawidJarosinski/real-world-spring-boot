package org.example.realworldspringboot.model.repo;

import org.example.realworldspringboot.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Integer> {
}
