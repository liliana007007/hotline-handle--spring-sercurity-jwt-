package com.capinfo.hotline.handle.test.web;

import com.capinfo.hotline.springmvc.web.BaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author longliying
 * @title: TaskController
 * @date 2019/7/1712:52
 */
@RestController
@RequestMapping("/tasks")
public class TaskController extends BaseController {
    @GetMapping("/listTasks")
    public String listTasks(){
        return "任务列表";
    }

    @PostMapping("/newTasks")
    @PreAuthorize("hasRole('ADMIN')")
    public String newTasks(){
        return "创建了一个新的任务";
    }

    @PutMapping("/updateTasks/{taskId}")
    public String updateTasks(@PathVariable("taskId")Integer id){
        return "更新了一下id为:"+id+"的任务";
    }

    @DeleteMapping("/deleteTasks/{taskId}")
    public String deleteTasks(@PathVariable("taskId")Integer id){
        return "删除了id为:"+id+"的任务";
    }
}
