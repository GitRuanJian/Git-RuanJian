package org.edu.ptu.studentmanager.utils;

import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Created by Lin Chenxiao on 2021-05-28
 **/
public class GradeUtils {
    public static String getGradeText(int year, int month, int admissionYear) {
        int gap = year - admissionYear;
        if (gap <= 0 || gap == 1 && month < 7) return "大一";
        if (gap == 1 || gap == 2 && month < 7) return "大二";
        if (gap == 2 || gap == 3 && month < 7) return "大三";
        return "大四";
    }
    
    public static int getGrade(int admissionYear) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int gap = year - admissionYear;
        if (gap <= 0 || gap == 1 && month < 7) return 1;
        if (gap == 1 || gap == 2 && month < 7) return 2;
        if (gap == 2 || gap == 3 && month < 7) return 3;
        return 4;
    }

    public static int getAdmissionYear(int requiredGrade) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - requiredGrade;
        if (calendar.get(Calendar.MONTH) >= 7) year++;
        return year;
    }
}
