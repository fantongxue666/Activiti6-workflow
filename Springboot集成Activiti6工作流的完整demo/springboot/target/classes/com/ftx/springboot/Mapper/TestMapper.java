package com.ftx.springboot.Mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestMapper.java
 * @Description TODO
 * @createTime 2020年05月07日 15:33:00
 */
@Mapper
public interface TestMapper {

    @Insert(value = "insert into holiday(id,name,time,reason,status) values(#{id},#{name},#{time},#{reason},#{status});")
    int insertHoliday(Map map);

    @Update(value = "update holiday set status=#{status} where id=#{id}")
    int updateHolidayStatus(@Param("status") String status,@Param("id") String id);

    @Select(value = "select * from holiday where id=#{id};")
    Map selectHolidayById(String id);

    @Select(value = "select a.* from holiday a,act_ru_execution b where a.id=b.BUSINESS_KEY_ and b.ID_=#{instanceId};")
    Map selectHolidayByInstanceId(String instanceId);

    @Select(value = "select * from user where account=#{username} and password=#{password}")
    Map selectUserByAccountAndPassword(@Param("username") String username,@Param("password") String password);

    @Select(value = "select * from user where deptid=#{deptId}")
    List<Map> selectUserByDeptId(Integer deptId);
}
