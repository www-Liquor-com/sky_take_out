package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 添加员工
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);

        // 添加之前看看数据库是否含有账号
        if (employeeService.addEmployee(employeeDTO).equals(StatusConstant.ENABLE)){
            return Result.success();
        }
        return Result.error("账号已存在，员工添加失败！");
    }

    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("改变员工状态")
    public Result<String> updateStates(@PathVariable Integer status, Long id) {

        log.info("员工的状态发生变化：ID为：{}", id);
        if (employeeService.updateStates(id).equals(StatusConstant.ENABLE)){
            return Result.success();
        }else{
            return Result.error("员工状态编辑错误！");
        }
    }

    @PutMapping("/editPassword")
    @ApiOperation("更改登录密码")
    public Result<String> updatePassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("登录密码更改：{}", passwordEditDTO);

        if (employeeService.updatePassword(passwordEditDTO).equals(StatusConstant.ENABLE)){
            return Result.success();
        } else if (employeeService.updatePassword(passwordEditDTO).equals(StatusConstant.DISABLE)) {
            return Result.error(MessageConstant.UPDATE_PASSWORD_ERROR);
        } else {
            return Result.error(MessageConstant.OLD_PASSWORD_ERROR);
        }
    }

}
