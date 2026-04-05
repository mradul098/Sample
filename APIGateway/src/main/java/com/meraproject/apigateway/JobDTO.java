package com.meraproject.apigateway;

import java.util.*;

public class JobDTO{
    String id;
    String title;
    String description;
    String companyId;

    public JobDTO() {}

    public JobDTO(String id, String title, String description, String companyId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.companyId = companyId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
}