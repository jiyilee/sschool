package com.usoft.sschool_teacher.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 陈秋
 * @Date: 2019/5/20 17:13
 * @Version 1.0
 */
public interface IGradeManageService {
    /**
     * 成绩管理
     */
    Map getGrade(int classId, int teacherId,String term,
                 String testName,String subject,int start,int page);
    Integer getCont(int classId, int teacherId,String term,
                    String testName,String subject,int start,int page);

    /**
     * 获取学期
     */
    List getSemester(int classId);

    /**
     * 获取考试类型
     */
    List getExamType(int classId);

}
