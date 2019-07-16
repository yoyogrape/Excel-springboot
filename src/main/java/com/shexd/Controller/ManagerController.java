package com.shexd.Controller;

import com.google.common.collect.Lists;
import com.shexd.Entity.Manager;
import com.shexd.Entity.User;
import com.shexd.util.DateUtils;
import com.shexd.util.ExportExcel;
import com.shexd.util.ImportExcel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ManagerController {

    private final Logger logger= LoggerFactory.getLogger(ManagerController.class);

    /**
     * 下载输入数据的模板
     *
     * @param response
     */
    @RequestMapping("import/template")
    public void importFileTemplate(HttpServletResponse response){
        try {
            //定义文件名称
            String fileName = "文档模版.xlsx";
            List<Manager> list = Lists.newArrayList();
            new ExportExcel("导出的文档模版", Manager.class, 1,2).setDataList(list).write(response, fileName).dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 导入已经填好数据的Excel
     * @param multipartFile
     */
    @RequestMapping(value = "import",method = RequestMethod.POST)
    public void importFile(MultipartFile multipartFile){
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(multipartFile, 1, 0);
            List<Manager> list = ei.getDataList(Manager.class);
            for (Manager user : list){
                try{
                    //to do: 保存处理数据
                    //userService.save(user);
                    logger.info(user.toString());
                    successNum++;
                }catch(ConstraintViolationException ex){
                    failureNum++;
                }catch (Exception ex) {
                    failureNum++;
                }
            }

            if (failureNum>0){
                failureMsg.insert(0, ", 失败: "+failureNum);
            }
            logger.info("已经操作 "+successNum+" 条数据；"+" "+"失败 "+failureNum);
        } catch (Exception e) {
            logger.error("导入失败",e);
        }
    }

    /**
     *
     * 导出Excel文件
     * @param response
     */
    @RequestMapping("export")
    public void export(HttpServletResponse response){
        try {
            String fileName = "User Data"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<Manager> users=new ArrayList<>();
            Manager user1=new Manager();
            user1.setIdCode("asdfad");
            user1.setAddress("中山");
            user1.setUserName("小明");
            user1.setNickName("猪小明");
            user1.setAge("20");
            user1.setBirth(DateUtils.parseDate("1992-10-10"));
            users.add(user1);
            Manager user2=new Manager();
            user2.setIdCode("asdfad2");
            user2.setAddress("中山2");
            user2.setUserName("小红");
            user2.setNickName("小小红");
            user2.setAge("18");
            user2.setBirth(DateUtils.parseDate("1998-11-09"));
            users.add(user2);
            new ExportExcel("导出的数据", Manager.class,2).setDataList(users).write(response, fileName).dispose();
        } catch (Exception e) {
        }
    }


    /**
     *
     * 导入已经填好数据的Excel，合并单元格的数据
     * @param multipartFile
     */
    @RequestMapping(value = "import2",method = RequestMethod.POST)
    public void importFile2(MultipartFile multipartFile){
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(multipartFile, 1, 0);
            List<Manager> list = ei.getDataList(Manager.class);
            String idcode = "";
            String address = "";
            for (Manager user : list){
                try{
                    //to do: 保存处理数据
                    if (user.getIdCode()!=null) {
                        idcode = user.getIdCode();
                    }
                    if (user.getIdCode()==null) {
                        user.setIdCode(idcode);
                    }
                    if (user.getAddress()!=null) {
                        address = user.getAddress();
                    }
                    if (user.getAddress()==null) {
                        user.setAddress(address);
                    }

                    //userService.save(user);
                    logger.info(user.toString());
                    successNum++;
                }catch(ConstraintViolationException ex){
                    failureNum++;
                }catch (Exception ex) {
                    failureNum++;
                }
            }

            if (failureNum>0){
                failureMsg.insert(0, ", 失败: "+failureNum);
            }
            logger.info("已经操作 "+successNum+" 条数据；"+" "+"失败 "+failureNum);
        } catch (Exception e) {
            logger.error("导入失败",e);
        }
    }
}
