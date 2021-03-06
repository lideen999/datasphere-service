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

package com.datasphere.engine.shaker.processor.metadata;

import java.util.Date;
import java.util.List;

import com.datasphere.engine.shaker.processor.prep.data.ColumnData;
import com.datasphere.engine.shaker.processor.prep.data.EvaluationData;
import com.datasphere.engine.shaker.processor.prep.data.OperateData;
import com.datasphere.engine.shaker.processor.prep.data.ProgramData;

public class ProcessMetadata {

    private String id;
    private String userId;
    private String panelId;
    private String dataKey;
    private String tableId;
    List<ColumnData> columns;
    List<OperateData> operates;
    List<ProgramData> programs;
    List<EvaluationData> evaluations;
    private Date updateTime;
    private Date createTime;

    public ProcessMetadata() {
    }

    public ProcessMetadata(String id, String userId, String panelId, String dataKey, String tableId, List<ColumnData> columns, List<OperateData> operates, List<ProgramData> programs, List<EvaluationData> evaluations, Date updateTime, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.panelId = panelId;
        this.dataKey = dataKey;
        this.tableId = tableId;
        this.columns = columns;
        this.operates = operates;
        this.programs = programs;
        this.evaluations = evaluations;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPanelId() {
        return panelId;
    }

    public void setPanelId(String panelId) {
        this.panelId = panelId;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<ColumnData> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnData> columns) {
        this.columns = columns;
    }

    public List<OperateData> getOperates() {
        return operates;
    }

    public void setOperates(List<OperateData> operates) {
        this.operates = operates;
    }

    public List<ProgramData> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ProgramData> programs) {
        this.programs = programs;
    }

    public List<EvaluationData> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationData> evaluations) {
        this.evaluations = evaluations;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DataProcessData{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", panelId='" + panelId + '\'' +
                ", tableId='" + tableId + '\'' +
                ", columns=" + columns +
                ", operates=" + operates +
                ", programs=" + programs +
                ", evaluations=" + evaluations +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
