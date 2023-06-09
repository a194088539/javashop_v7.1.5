package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.framework.logs.Debugger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 调试控制器
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-17
 */
@Controller
@RequestMapping("/debugger")
@ConditionalOnProperty(value = "javashop.debugger", havingValue = "true")
public class DebuggerController {

    @Autowired
    private Debugger debugger;


    /**
     * 获取debugger界面
     *
     * @param model
     * @param request
     * @return
     */
    @GetMapping()
    public String ui(Model model, HttpServletRequest request) {
        if (!request.getServletPath().endsWith("/")) {
            return "debugger-301";
        }
        model.addAttribute("jquery_path", "../jquery.min.js");

        return "debugger";
    }


    /**
     * 获取日志
     *
     * @return
     */
    @GetMapping(value = "/log")
    @ResponseBody
    public String log() {
        return debugger.getLog();
    }


    /**
     * 清空日志
     */
    @DeleteMapping(value = "/log")
    @ResponseBody
    public void delLog() {
        debugger.clear();
    }
}
