package com.whaleal.mars.util;

import com.whaleal.mars.bean.Gender;
import com.whaleal.mars.bean.Student;

public class StudentGenerator {

    public static Student getInstance( Integer stuNo ) {
        Integer classNo = stuNo / 1000;
        Integer sNo = stuNo - 1000;
        Student student = new Student();
        student.setStuNo(String.valueOf(stuNo));
        student.setStuHeight(172.35);
        student.setStuSex(stuNo % 2 == 0 ? Gender.male : Gender.female);
        student.setStuName(String.valueOf((stuNo - 1000)));
        student.setStuAge(18);
        student.setClassNo(String.valueOf(classNo));
        Double cScore = classNo * sNo * 0.1;
        student.setChineseScore(cScore);
        Double mScore = (10 - classNo) * sNo * 0.1;
        student.setMathScore(mScore);
        student.setEnglishScore(cScore + mScore);
        return student;
    }
}
