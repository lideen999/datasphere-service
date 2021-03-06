package com.datasphere.engine.manager.resource.provider.db.controller;

import com.datasphere.engine.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.hbase.model.HBaseConnectionInfo;
import com.datasphere.engine.manager.resource.provider.hbase.model.HBaseDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfo;
import com.datasphere.engine.manager.resource.provider.service.UDSMService;

import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
public class HBaseDataSourceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HBaseDataSourceController.class);

    public static final String BASE_PATH = "/datasource/hbase";

    @Autowired
    UDSMService uDSMService;

    /**
     * test connection  hbase
     * @param hBaseConnectionInfo
     * @return
     */
	@RequestMapping(value = BASE_PATH + "/testHBase", method = RequestMethod.POST) 
    public Single<Map<String,Object>> testHBase(@RequestBody HBaseConnectionInfo hBaseConnectionInfo) {
        return Single.fromCallable(() -> {
            int result = uDSMService.testHBase(hbaseConnectionInfo);
            if(result == 0){
                return JsonWrapper.failureWrapper("测试失败");
            }
            return JsonWrapper.successWrapper("测试成功");
        });
    }

    /**
     * select hbase list table
     * @param hBaseConnectionInfo
     * @hbaseConnectionInfo
     */
	@RequestMapping(value = BASE_PATH + "/hBaseListTable", method = RequestMethod.POST) 
    public Object hBaseListTable(@RequestBody HBaseConnectionInfo hBaseConnectionInfo){
        return Single.fromCallable(() -> {
            List<DBTableInfodmp> dbTableInfodmps = uDSMService.hBaseListTable(hbaseConnectionInfo);
            if(dbTableInfodmps == null){
                return JsonWrapper.failureWrapper("获取失败");
            }
            return JsonWrapper.successWrapper(dbTableInfodmps);
        });
    }

    /**
     * select hbase table data
     * @param hBaseConnectionInfo
     * @hbaseConnectionInfo
     */
	@RequestMapping(value = BASE_PATH + "/queryHBaseTableData", method = RequestMethod.POST) 
    public Object queryHBaseTableData(@RequestBody HBaseConnectionInfo hBaseConnectionInfo){
        return Single.fromCallable(() -> {
            return JsonWrapper.successWrapper(uDSMService.queryHBaseTableData(hbaseConnectionInfo));
        });
    }

    /**
     * create hbase datasource big data
     * @param hBaseDataSourceInfo
     * @return
     */
	@RequestMapping(value = BASE_PATH + "/createHBase", method = RequestMethod.POST) 
    public Single<Map<String,Object>> createHBase(@RequestBody HBaseDataSourceInfo hBaseDataSourceInfo, HttpServletRequest request){
        return Single.fromCallable(() -> {
            if(hbaseDataSourceInfo.getBusinessType() == null){
                return JsonWrapper.failureWrapper("业务类型不能为空");
            }
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int result = uDSMService.createHBase(hbaseDataSourceInfo,token);
            if (result == 0){
                return JsonWrapper.failureWrapper("插入失败");
            }
            return JsonWrapper.successWrapper("插入成功");
        });
    }

    /**
     * update hbase datasource info
     * @param hBaseDataSourceInfo
     * @return
     */
	@RequestMapping(value = BASE_PATH + "/updateHBase", method = RequestMethod.POST) 
    public Single<Map<String,Object>> updateHBaseById(@RequestBody HBaseDataSourceInfo hBaseDataSourceInfo, HttpServletRequest request){
        return Single.fromCallable(() -> {
            if(StringUtils.isBlank(hbaseDataSourceInfo.getId())){
                return JsonWrapper.failureWrapper("id不能为空！");
            }
            //TODO 原有数据库是否被引用 -- 是否 可更新
            //TODO 查询数据库中有无数据源  验证名称是否重复
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int rsult = uDSMService.updateHBaseById(hbaseDataSourceInfo,token);
            if(rsult == 0){
                return JsonWrapper.failureWrapper("更新失败！");
            }
            return JsonWrapper.successWrapper();
        });
    }
}
