package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.progress.model.ProgressVo;
import com.enation.app.javashop.core.system.progress.model.TaskProgress;
import com.enation.app.javashop.core.system.progress.model.TaskProgressConstant;
import com.enation.app.javashop.core.system.progress.service.ProgressManager;
import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProgressControllerTest
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-05-11 下午12:04
 */
public class ProgressControllerTest extends BaseTest {

    //    /progress
    @Autowired
    private ProgressManager progressManager;


    @Test
    public void hasTask() throws Exception {

        //任务存在判定
        TaskProgress taskProgress = new TaskProgress(2);
        progressManager.putProgress(TaskProgressConstant.PAGE_CREATE, taskProgress);
        mockMvc.perform(get("/admin/task/"+TaskProgressConstant.PAGE_CREATE)
                .header("Authorization", superAdmin))
                .andExpect(status().isOk());

        //任务不存在判定
        ErrorMessage error = new ErrorMessage("003", "进度不存在");
        progressManager.remove(TaskProgressConstant.PAGE_CREATE);
        mockMvc.perform(get("/admin/task/has-task")
                .header("Authorization", superAdmin))
                .andExpect(objectEquals(error));

    }


    @Test
    public void viewProgress() throws Exception {

        //正常随机任务查看
        ProgressVo progressVo = new ProgressVo(100, "SUCCESS", "", "");
        mockMvc.perform(get("/admin/task/task_test/progress")
                .header("Authorization", superAdmin))
                .andExpect(objectEquals(progressVo));

        //更新进度测试
        TaskProgress taskProgress = new TaskProgress(2);
        taskProgress.setStepPer(99.8);
        progressManager.putProgress(TaskProgressConstant.PAGE_CREATE, taskProgress);
        progressVo = new ProgressVo(0, "DOING", "", "");
        mockMvc.perform(get("/admin/task/"+TaskProgressConstant.PAGE_CREATE+"/progress")
                .header("Authorization", superAdmin))
                .andExpect(objectEquals(progressVo));

    }

}
