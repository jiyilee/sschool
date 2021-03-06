package com.usoft.sschool_student.mapper;

import com.usoft.smartschool.pojo.XnIntegralRule;
import com.usoft.smartschool.pojo.XnIntegralRuleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XnIntegralRuleMapper {
    int countByExample(XnIntegralRuleExample example);

    int deleteByExample(XnIntegralRuleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XnIntegralRule record);

    int insertSelective(XnIntegralRule record);

    List<XnIntegralRule> selectByExample(XnIntegralRuleExample example);

    XnIntegralRule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XnIntegralRule record, @Param("example") XnIntegralRuleExample example);

    int updateByExample(@Param("record") XnIntegralRule record, @Param("example") XnIntegralRuleExample example);

    int updateByPrimaryKeySelective(XnIntegralRule record);

    int updateByPrimaryKey(XnIntegralRule record);
}