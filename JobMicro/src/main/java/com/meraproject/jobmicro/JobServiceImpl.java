package com.meraproject.jobmicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    private JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(Job job) {
        if (job.getId() == null || job.getId().isEmpty()) {
            job.setId(java.util.UUID.randomUUID().toString());
        }
        return jobRepository.save(job);
    }

    @Override
    public Optional<Job> getJobById(String id) {
        return jobRepository.findById(id);
    }

    @Override
    public void deleteJob(String id) {
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> getJobsByCompanyId(String companyId) {
        return jobRepository.findByCompanyId(companyId);
    }
}
