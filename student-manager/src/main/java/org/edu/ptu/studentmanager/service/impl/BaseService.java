package org.edu.ptu.studentmanager.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.edu.ptu.studentmanager.common.basic.BaseMapper;
import org.edu.ptu.studentmanager.common.basic.UniqueDao;
import org.edu.ptu.studentmanager.common.request.Condition;
import org.edu.ptu.studentmanager.common.response.ListResult;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.common.request.IdCollection;
import org.edu.ptu.studentmanager.common.request.MapEditor;
import org.edu.ptu.studentmanager.mapper.CommonMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.edu.ptu.studentmanager.common.enums.Errors.*;

/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
public class BaseService<T extends UniqueDao> {

    protected String table;
    protected String keyColumn;
    protected CommonMapper mapper;
    protected BaseMapper<T> baseMapper;
    protected List<String> columns;

    protected void afterSelect(List<? extends T> list) {}

    protected Result beforeAdd(T data) {
        return Result.OK;
    }

    protected Result beforeEdit(T data, T record) {
        return Result.OK;
    }

    protected Result beforeDelete(String ids) {
        return Result.OK;
    }

    protected Result beforeChooseCourse(MapEditor mapEditor) {
        return Result.OK;
    }

    public Result list(Condition condition) {
        if (condition == null || condition.getOffset() == null || condition.getLimit() == null)
            return Result.fail(INVALID_PARAMS);
        boolean flag = true;
        long total = 0;
        if (StringUtils.hasLength(condition.getColumn())) {
            for (String col : columns) {
                if (condition.getColumn().equals(col)) {
                    flag = false;
                    if (condition.getValue() == null) condition.setValue("");
                    total = mapper.countColumn(table, condition.getColumn(), condition.getValue());
                    condition.setColumn("trim(a.`" + condition.getColumn() + "`)");
                    break;
                }
            }
        }
        if (flag) {
            condition.setColumn("1");
            condition.setValue("1");
            total = mapper.count(table);
        }

        List<? extends T> list = baseMapper.select(condition);
        if (!CollectionUtils.isEmpty(list)) afterSelect(list);
        return ListResult.ok(list, total);
    }

    public Result add(T data) {
        if (data == null || data.hasIllegalField()) return Result.fail(INVALID_PARAMS);
        if (mapper.countColumn(table, keyColumn, data.getKey()) > 0)
            return Result.fail(REPEAT_DATA, "数据重复");
        Result result = beforeAdd(data);
        if (!result.isSuccess()) return result;
        baseMapper.add(data);
        return Result.ok(data.getId());
    }

    public Result edit(T data) {
        if (data == null || data.getId() == null || data.hasIllegalField())
            return Result.fail(INVALID_PARAMS);
        T record = baseMapper.selectById(data.getId());
        if (record == null) return Result.fail(RECORD_NOT_EXISTS);
        if (!data.getKey().equals(record.getKey()) &&
                mapper.countColumn(table, keyColumn, data.getKey()) > 0)
            return Result.fail(REPEAT_DATA, "记录已存在");
        Result result = beforeEdit(data, record);
        if (!result.isSuccess()) return result;
        if (baseMapper.update(data) > 0) return Result.OK;
        return Result.fail(RECORD_NOT_EXISTS);
    }

    public Result delete(IdCollection ids) {
        if (ids == null || CollectionUtils.isEmpty(ids.getIds())) return Result.fail(INVALID_PARAMS);
        String joinIds = Strings.join(ids.getIds(), ',');
        Result result = beforeDelete(joinIds);
        if (result.isSuccess()) return Result.ok(mapper.deleteByIds(table, joinIds));
        return result;
    }

    protected Result chooseCourse(MapEditor mapEditor, String tableMap, String colInMap, String colForInsert) {
        if (mapEditor == null || mapEditor.getReceptorId() == null ||
                CollectionUtils.isEmpty(mapEditor.getIds())) return Result.fail(INVALID_PARAMS);
        if (mapper.exists(table, mapEditor.getReceptorId()) == 0) return Result.fail(RECORD_NOT_EXISTS);
        Result result = beforeChooseCourse(mapEditor);
        if (!result.isSuccess()) return result;
        mapper.deleteByColumnId(tableMap, colInMap, mapEditor.getReceptorId());
        List<String> list = mapEditor.getIds().stream().distinct().map(v -> v != null ?
                String.format("(%d,%d)", mapEditor.getReceptorId(), v) : null).collect(Collectors.toList());
        mapper.insertValues(tableMap, colForInsert, Strings.join(list, ','));
        return Result.OK;
    }

    protected List<String> getFields(Class<T> tClass) {
        if (tClass == null) return new LinkedList<>();
        List<String> list = new LinkedList<>();
        for (Field field : tClass.getDeclaredFields()) {
            list.add(field.getName().replaceAll("[A-Z]", "_$0").toLowerCase());
        }
        return list;
    }
}
