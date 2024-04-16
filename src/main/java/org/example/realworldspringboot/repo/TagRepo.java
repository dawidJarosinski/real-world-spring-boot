package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Integer> {

    Tag findTagByValue(String value);

    Boolean existsTagByValue(String value);
}
