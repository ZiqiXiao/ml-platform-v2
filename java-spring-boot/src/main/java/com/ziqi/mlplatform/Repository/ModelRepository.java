package com.ziqi.mlplatform.Repository;

import com.ziqi.mlplatform.Model.Model;
import com.ziqi.mlplatform.dto.ModelResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByModelName(String modelName);
}
