package com.usoft.sschool_teacher.controllers.ClassManagerController.ClassHomeWork;

import com.usoft.smartschool.pojo.HlTeacher;
import com.usoft.smartschool.util.MyResult;
import com.usoft.smartschool.util.NumberKit;
import com.usoft.sschool_teacher.common.ResultData;
import com.usoft.sschool_teacher.common.SystemParam;
import com.usoft.sschool_teacher.enums.po.AppHomeWorkPo;
import com.usoft.sschool_teacher.enums.po.AppHomeWorkTitlePo;
import com.usoft.sschool_teacher.enums.po.HomeWorkManagePo;
import com.usoft.sschool_teacher.service.IHomeWorkService;
import com.usoft.sschool_teacher.util.UploadFileUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * @Author: 陈秋
 * @Date: 2019/5/5 13:29
 * @Version 1.0
 * 作业发布
 */
@CrossOrigin
@RestController
@RequestMapping("/teacher/homerWork")
public class HomerWorkController {

    @Autowired
    private IHomeWorkService homeWorkService;

    @Autowired
    private UploadFileUtil uploadFileUtil;

    /**
     * 发布作业
     * @param po
     * @return
     */
    @PostMapping("/insertHomework")
    public ResultData insertHomeWork(@Valid AppHomeWorkPo po){
        ResultData result = new ResultData();
        int thId = SystemParam.getUserId();

        //判断班级不能重复
        String[] classIds = po.getAcceptClass().split(",");
        List<String> classList = Arrays.asList(classIds);
        Set<String> classSet = new HashSet<>(classList);
        if(classList.size() == classSet.size()){
            result.setStatus(2);
            result.setMessage("不能选择项目的班级");
            result.setData(false);
            return result;
        }

        int i = homeWorkService.insertHomeWork(thId, po.getHwName(), po.getHwType(), po.getAcceptClass(),
                po.getSubject(), po.getExpireTime(), po.getHwContent(), po.getHwContentImg());
        if (i>0){
            result.setStatus(1);
            result.setMessage("success");
            result.setData(i);
            return result;
        }else if (i == -1){
            result.setStatus(2);
            result.setMessage("系统故障");
            return result;
        }else if(i == -2){
            result.setStatus(2);
            result.setMessage("教师ID不能为空");
            return result;
        }else if (i == -3){
            result.setStatus(2);
            result.setMessage("接收班级不能为空");
            return result;
        }else if (i == -6){
            result.setStatus(2);
            result.setMessage("时间格式不正确");
            return result;
        }else if(i == -10){
            result.setStatus(2);
            result.setMessage("班级不存在");
            return result;
        }else if (i == -8){
            result.setStatus(2);
            result.setMessage("班级上传ID格式不对");
            return result;
        }else if (i == -9){
            result.setStatus(2);
            result.setMessage("请选择班级");
            return result;
        }
        return null;
    }

    /**
     * 修改作业
     * @param hmwId
     * @param thId
     * @param hwName
     * @param hwType
     * @param acceptClass
     * @param subject
     * @param expireTime
     * @param hwContent
     * @param hwContentImg
     * @return
     */
    @PostMapping("/updateHomework")
    public ResultData updateHomework(String hmwId,String thId,String hwName,String hwType,String acceptClass,String subject,
                                     String expireTime,String hwContent,String hwContentImg){
        ResultData res = new ResultData();
        int homeworkId = 0;
        int teacherId = SystemParam.getUserId();
        int homework = 0;
        try {
            homeworkId = Integer.parseInt(hmwId.trim());
        }catch(Exception e){
            res.setMessage("上传的ID不对");
            res.setStatus(2);
            return res;
        }
        if (thId!=null && !"".equals(thId) && hwName!=null && !"".equals(hwType)){
            try {
                //teacherId = Integer.parseInt(thId.trim());
                homework = Integer.parseInt(hwType.trim());
            }catch (Exception e){
                res.setStatus(2);
                res.setMessage("上传教师或作业类型ID格式不对");
                return res;
            }
        }

        int i = homeWorkService.updateHomework(homeworkId,teacherId, hwName, homework, acceptClass, subject,
                                        expireTime, hwContent, hwContentImg);
        if (i>0){
            res.setStatus(1);
            res.setMessage("操作成功");
            return res;
        }else if (i == -6){
            res.setStatus(2);
            res.setMessage("时间格式不正确");
            return res;
        }else if (i == -1){
            res.setStatus(2);
            res.setMessage("系统故障");
            return res;
        }
        return res;
    }

    /**
     * 提交选择题
     * @param po
     * @return
     */
    @PostMapping("/insertHomeworkTitle")
    public ResultData insertHomeworkTitle(@Valid AppHomeWorkTitlePo po){
        ResultData res = new ResultData();
        int i = homeWorkService.insertHomeworkTitle(po.getHwid(),po.getTitle(),
                po.getAnswerA(),po.getAnswerB(), po.getAnswerC(),po.getAnswerD(),po.getRightAnswer());
        if (i>0){
            res.setStatus(1);
            res.setMessage("success");
            return res;
        }else if(i == -4){
            res.setStatus(2);
            res.setMessage("选择题题目不能为空");
            return res;
        }else if (i == -5) {
            res.setStatus(2);
            res.setMessage("学者题题目不能为空");
            return res;
        }else if (i == -7){
            res.setStatus(2);
            res.setMessage("作业ID不能为空");
            return res;
        }else if (i == -1){
            res.setStatus(2);
            res.setMessage("系统故障");
            return res;
        }
        return null;
    }

    /**
     * 上传图片
     * @param request
     * @return
     */
    @PostMapping("/uploadImg")
    public MyResult uploadImg(HttpServletRequest request, @Param("state")Integer state){

        return uploadFileUtil.uploadFiles(request);
    }

    /**
     * 选择科目
     * @param teacherId
     * @return
     */
    @GetMapping("/getSubject")
    public ResultData getSubject(String teacherId){
        ResultData res = new ResultData();
        int thId = SystemParam.getUserId();
        //HlTeacher teacher = homeWorkService.getSubject(teacherId);
        HlTeacher teacher = homeWorkService.getSubject(thId);
        if (teacher!=null){
            res.setStatus(1);
            res.setMessage("success");
            res.setData(teacher);
            return res;
        }
        res.setStatus(1);
        res.setMessage("老师你还没有选择你的科目");
        res.setData("");
        return res;
    }

    /**
     * 作业管理
     * @param po 作业管理实体类条件
     * @return
     */
    @GetMapping("/getHomeworkManager")
    public ResultData getHomeworkManager(@Valid HomeWorkManagePo po,HttpServletRequest request){
        ResultData result = new ResultData();
        int thId = 0;
        try {
            thId = SystemParam.getUserId();
        }catch (Exception e){
            result.setStatus(2);
            result.setMessage("没有登陆");
            result.setData("");
            return result;
        }
        HttpSession session = request.getSession();
        session.setAttribute("subject",po.getSubject());
        session.setAttribute("classId",po.getClassId());
        List<Map<String,Object>> data = homeWorkService.getHomeworkmanager(thId,po.getState(),po.getStuName(), po.getClassName(),
                    po.getHwmName(), po.getCurrentPage(),po.getPageSize(),po.getSubject(),po.getClassId(),0);
        int resNum = homeWorkService.getStuHomeworkEsCount(thId,po.getState(),po.getStuName(),
                po.getClassName(),po.getHwmName(),po.getSubject(),po.getClassId(),0);
        if (data == null || data.size()==0){
            result.setStatus(2);
            result.setMessage("暂无数据");
            result.setData("");
            return result;
        }
        result.setMessage("success");
        result.setStatus(1);
        result.setData(data);
        result.setTotalNumber((resNum+po.getPageSize())/po.getPageSize());
        return result;
    }

    /**
     * 作业详情
     * @param homeworkId
     * @return
     */
    @GetMapping("/getHomeworkInformation")
    public ResultData getHomeworkInformation(String homeworkId,String studentId){
        ResultData res = new ResultData();
        int hwId = 0;
        int stId = 0;
        if (homeworkId == null || "".equals(homeworkId)){
            res.setStatus(2);
            res.setMessage("没有传入作业id");
            return res;
        }
        if (studentId == null || "".equals(studentId)){
            res.setStatus(2);
            res.setMessage("没有传入学生id");
            return res;
        }
        try {
             hwId = Integer.parseInt(homeworkId.trim());
             stId = Integer.parseInt(studentId.trim());
            //homeWorkService.getHomeworkInformation(hwId,stId);
        }catch(Exception e){
            res.setMessage("传入的参数格式不对");
            res.setStatus(2);
            return res;
        }
       Map<String,Object> resData =  homeWorkService.getHomeworkInformation(hwId,stId);
        if (resData.containsKey("msg")){
            res.setStatus(2);
            res.setMessage(resData.get("msg").toString());
            return res;
        }
        res.setStatus(1);
        res.setMessage("success");
        res.setData(resData);
        return res;
    }

    /**
     * 教师评语
     * @param imgPath
     * @param comment
     * @return
     */
    @PostMapping("/insertComment")
    public ResultData insertComment(String imgPath,String comment,String teacherId,String stuId,String hwmId){
        ResultData res = new ResultData();
        int tId = SystemParam.getUserId();
        int sId = 0;
        int hId = 0;
        try {
            //tId = Integer.parseInt(teacherId.trim());
            sId = Integer.parseInt(stuId.trim());
            hId = Integer.parseInt(hwmId.trim());
        }catch (Exception e){
            res.setMessage("id上传的格式不对");
            res.setStatus(2);
            return  res;
        }

       int i =  homeWorkService.insertComment(imgPath,comment,tId,sId,hId);
        if (i == -2){
            res.setStatus(2);
            res.setMessage("没有找到相关学生作业");
            return res;
        }else if(i == -1){
            res.setStatus(2);
            res.setMessage("系统故障");
            return res;
        }else if (i>0){
            res.setStatus(1);
            res.setMessage("success");
            return res;
        }
        return res;
    }

    /**
     * 教师批量评语
     * @return
     */
    @PostMapping("/insertComments")
    public ResultData insertComments(String imgPath,String comment,String teacherId,String stuHmwIds,HttpServletRequest request){
        ResultData res = new ResultData();
        int tId = SystemParam.getUserId();
        HttpSession session = request.getSession();
        Map data = new HashMap();
        data.put("classId",session.getAttribute("classId"));
        data.put("subject",session.getAttribute("subject"));
       int i =  homeWorkService.insertComments(imgPath,comment,tId,stuHmwIds);
        if(i == -2){
            res.setStatus(2);
            res.setMessage("系统故障");
            return res;
        }else if (i>0){
            res.setStatus(1);
            res.setMessage("success");
            res.setData(data);
            return res;
        }
        return res;
    }

    /**
     * 发布作业选择班级
     */
    @GetMapping("/getClasses")
    public MyResult getClasses(String subject){
        List data = homeWorkService.getClasses(subject);
        if (data.size()==0){
            return new MyResult(2,"没有数据","");
        }
        return new MyResult(1,"success",data);
    }
}
