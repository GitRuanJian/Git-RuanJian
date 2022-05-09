package org.edu.ptu.studentmanager.common.dao;

import lombok.Data;
import org.edu.ptu.studentmanager.common.basic.UniqueDao;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Data
public class Major implements UniqueDao {
    private static final BigDecimal ZERO = new BigDecimal(0);

    private Integer id;
    private String name;
    private Integer capacity;
    private BigDecimal requiredCredits;

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public boolean hasIllegalField() {
        return !StringUtils.hasLength(name) || capacity == null || requiredCredits == null ||
                capacity <= 0 || requiredCredits.compareTo(ZERO) <= 0;
    }
}
