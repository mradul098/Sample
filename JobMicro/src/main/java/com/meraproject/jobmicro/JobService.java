package com.meraproject.jobmicro;

import java.util.Optional;

public interface JobService {
    Job createJob(Job job);
    Optional<Job> getJobById(String id);
    java.util.List<Job> getAllJobs();

    void deleteJob(String id);
    java.util.List<Job> getJobsByCompanyId(String companyId);

}
