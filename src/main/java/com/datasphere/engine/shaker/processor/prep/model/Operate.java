/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.shaker.processor.prep.model;

//import javax.persistence.Column;
//import javax.persistence.Id;
//import javax.persistence.Table;
import java.util.Date;

//@Table(name = "T_OPERATES")
public class Operate implements Comparable<Operate> {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_OPERATES.ID
     *
     * @mbg.generated
     */
//    @Id
//    //@Column(name = "ID")
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_OPERATES.STATUS
     *
     * @mbg.generated
     */
//    //@Column(name = "STATUS")
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_OPERATES.OPERATE_CODE
     *
     * @mbg.generated
     */
    //@Column(name = "OPERATE_CODE")
    private String operateCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_OPERATES.PARAMETERS
     *
     * @mbg.generated
     */
    //@Column(name = "PARAMETERS")
    private String parameters;

    //@Column(name = "PARAMSVO")
    private String paramsvo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_OPERATES.PROCESS_ID
     *
     * @mbg.generated
     */
    //@Column(name = "PROCESS_ID")
    private String processId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_OPERATES.UPDATE_TIME
     *
     * @mbg.generated
     */
    //@Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_OPERATES.CREATE_TIME
     *
     * @mbg.generated
     */
    //@Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_OPERATES
     *
     * @mbg.generated
     */
    public Operate(Integer id, String status, String operateCode, String parameters, String paramsvo, String processId, Date updateTime, Date createTime) {
        this.id = id;
        this.status = status;
        this.operateCode = operateCode;
        this.parameters = parameters;
        this.paramsvo = paramsvo;
        this.processId = processId;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_OPERATES
     *
     * @mbg.generated
     */
    public Operate() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_OPERATES.ID
     *
     * @return the value of T_OPERATES.ID
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_OPERATES.ID
     *
     * @param id the value for T_OPERATES.ID
     *
     * @mbg.generated
     */
    public Operate setId(Integer id) {
        this.id = id;
        return this;
    }


    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_OPERATES.STATUS
     *
     * @return the value of T_OPERATES.STATUS
     *
     * @mbg.generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_OPERATES.STATUS
     *
     * @param status the value for T_OPERATES.STATUS
     *
     * @mbg.generated
     */
    public Operate setStatus(String status) {
        this.status = status == null ? null : status.trim();
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_OPERATES.OPERATE_CODE
     *
     * @return the value of T_OPERATES.OPERATE_CODE
     *
     * @mbg.generated
     */
    public String getOperateCode() {
        return operateCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_OPERATES.OPERATE_CODE
     *
     * @param operateCode the value for T_OPERATES.OPERATE_CODE
     *
     * @mbg.generated
     */
    public Operate setOperateCode(String operateCode) {
        this.operateCode = operateCode == null ? null : operateCode.trim();
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_OPERATES.PARAMETERS
     *
     * @return the value of T_OPERATES.PARAMETERS
     *
     * @mbg.generated
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_OPERATES.PARAMETERS
     *
     * @param parameters the value for T_OPERATES.PARAMETERS
     *
     * @mbg.generated
     */
    public Operate setParameters(String parameters) {
        this.parameters = parameters == null ? null : parameters.trim();
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_OPERATES.PROCESS_ID
     *
     * @return the value of T_OPERATES.PROCESS_ID
     *
     * @mbg.generated
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_OPERATES.PROCESS_ID
     *
     * @param processId the value for T_OPERATES.PROCESS_ID
     *
     * @mbg.generated
     */
    public Operate setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_OPERATES.UPDATE_TIME
     *
     * @return the value of T_OPERATES.UPDATE_TIME
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_OPERATES.UPDATE_TIME
     *
     * @param updateTime the value for T_OPERATES.UPDATE_TIME
     *
     * @mbg.generated
     */
    public Operate setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_OPERATES.CREATE_TIME
     *
     * @return the value of T_OPERATES.CREATE_TIME
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_OPERATES.CREATE_TIME
     *
     * @param createTime the value for T_OPERATES.CREATE_TIME
     *
     * @mbg.generated
     */
    public Operate setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getParamsvo() {
        return paramsvo;
    }

    public Operate setParamsvo(String paramsvo) {
        this.paramsvo = paramsvo;
        return this;
    }

    @Override
    public int compareTo(Operate o) {
        return this.createTime.compareTo(o.getCreateTime());
    }
}