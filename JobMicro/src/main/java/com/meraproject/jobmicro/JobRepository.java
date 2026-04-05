package com.meraproject.jobmicro;
import java.util.*;

public interface JobRepository extends org.springframework.data.jpa.repository.JpaRepository<Job, String> {
    List<Job> findByCompanyId(String companyId);
}
