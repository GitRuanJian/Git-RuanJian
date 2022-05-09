package org.edu.ptu.studentmanager.common.basic;

/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
public interface UniqueDao {
    Integer getId();
    String getKey();
    boolean hasIllegalField();
}
