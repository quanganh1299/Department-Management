package com.vti.repository;

import com.vti.entity.Department;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IDepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
    boolean existsByName(String name);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Department AS d SET d.total_members = (SELECT COUNT(a.department_id) FROM `Account` AS a WHERE a.department_id = d.id)",nativeQuery = true)
    void updateTotalMembers();
}
