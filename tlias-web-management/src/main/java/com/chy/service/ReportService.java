package com.chy.service;

import com.chy.pojo.ClazzCountOption;
import com.chy.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    JobOption getEmpJobData();

    List<Map<String, Object>> getEmpGenderData();

    List<Map> getStudentDegreeData();

    ClazzCountOption getStudentCountData();
}
