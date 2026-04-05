package com.meraproject.jobmicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class JobsController {
    private JobService jobService;
    @Autowired
    public JobsController(JobService jobService) {
        this.jobService = jobService;
    }
    @GetMapping("/jobs/{id}")
    public Optional<Job> getJobById(String id) {
        return jobService.getJobById(id);
    }

     @GetMapping("/jobs/company/{companyId}")
    public java.util.List<Job> getJobsByCompanyId(String companyId) {
         return jobService.getJobsByCompanyId(companyId);
     }

     @PostMapping("/jobs")
        public Job createJob(@RequestBody Job job) {
            return jobService.createJob(job);
        }

    @GetMapping("/jobs/delete/{id}")
    public void deleteJob(String id) {
        jobService.deleteJob(id);
    }

}
