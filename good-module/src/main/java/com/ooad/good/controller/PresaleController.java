package com.ooad.good.controller;

import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.ooad.good.model.bo.Presale;
import com.ooad.good.model.po.PresaleActivityPo;
import com.ooad.good.model.vo.PresaleVo;
import com.ooad.good.service.PresaleService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Api(value="预售服务",tags="presale")
@RestController
@RequestMapping(value = "/presale", produces = "application/json;charset=UTF-8")
public class PresaleController {

    private  static  final Logger logger = LoggerFactory.getLogger(PresaleController.class);

    @Autowired
    private PresaleService presaleService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    /**
     * 管理员新增sku预售活动
     * @param vo
     * @param bindingResult
     * @return
     */
    @PostMapping("/presales")
    public Object insertPresale(@Validated @RequestBody PresaleVo vo, BindingResult bindingResult) {

        logger.debug("insert presale ");
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }

        Presale presale = vo.createPresale();
        presale.setGmtCreate(LocalDateTime.now());
        ReturnObject retObject = presaleService.insertPresale(presale);
        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }

    }

    /**
     * 管理员修改sku预售活动
     * @param id
     * @param vo
     * @param bindingResult
     * @return
     */
    @PutMapping("presales/{id}")
    public Object updatePresale(@PathVariable("id")Long id, @Validated @RequestBody PresaleVo vo,BindingResult bindingResult){

        logger.debug("update Presale: id ="+id);

        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }

        Presale presale=vo.createPresale();
        presale.setId(id);
        presale.setGmtModified(LocalDateTime.now());

        ReturnObject retObject=presaleService.updatePresale(presale);
        if (retObject.getData() != null) {
            return Common.getRetObject(retObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }


}