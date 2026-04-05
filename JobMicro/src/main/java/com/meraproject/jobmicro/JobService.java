package com.meraproject.jobmicro;

import java.util.Optional;

public interface JobService {
    Job createJob(Job job);
    Optional<Job> getJobById(String id);

    void deleteJob(String id);
    java.util.List<Job> getJobsByCompanyId(String companyId);

}
