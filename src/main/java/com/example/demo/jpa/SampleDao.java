package com.example.demo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SampleDao extends
        JpaRepository<SampleEntity, Long>, JpaSpecificationExecutor<SampleEntity> {

    List<SampleEntity> findByName(String name);

    @Query("from SampleEntity where name=?1")
        // JPQL ~= HQL
    List<SampleEntity> findByName2(String name);

    @Query("select " +
            "name as name, " +
            "type as type " +
            "from SampleEntity")
    List<SampleNameTypeVO> findByProjection1();

    @Query("select new com.example.demo.jpa.SampleNameTypeDTO("
            + "name, "
            + "type "
            + ") from SampleEntity")
    List<SampleNameTypeDTO> findByProjection2();

}

