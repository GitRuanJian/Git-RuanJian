package org.edu.ptu.studentmanager.common.request;

import lombok.Data;

import java.util.List;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Data
public class MapEditor {
    private Integer receptorId;
    private List<Integer> ids;
}
