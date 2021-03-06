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

package com.datasphere.server.domain.scheduling.engine;

import org.apache.commons.collections4.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.datasphere.engine.datasource.DataSource;
import com.datasphere.engine.datasource.DataSourceRepository;
import com.datasphere.engine.datasource.ingestion.IngestionHistory;
import com.datasphere.engine.datasource.ingestion.IngestionHistoryRepository;
import com.datasphere.engine.datasource.ingestion.jdbc.BatchIngestionInfo;
import com.datasphere.server.domain.engine.DruidEngineMetaRepository;
import com.datasphere.server.domain.engine.EngineIngestionService;

import static com.datasphere.engine.datasource.ingestion.IngestionHistory.IngestionStatus.FAILED;
import static com.datasphere.engine.datasource.ingestion.IngestionHistory.IngestionStatus.RUNNING;
import static com.datasphere.engine.datasource.ingestion.IngestionHistory.IngestionStatus.SUCCESS;

/**
 * Created by aladin on 2019. 12. 5..
 */
@Component
@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
@DisallowConcurrentExecution
public class DataSourceIngestionCheckJob extends QuartzJobBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceIngestionCheckJob.class);

  @Autowired
  EngineIngestionService ingestionService;

  @Autowired
  IngestionHistoryRepository ingestionHistoryRepository;

  @Autowired
  DruidEngineMetaRepository engineMetaRepository;

  @Autowired
  DataSourceRepository dataSourceRepository;

  public DataSourceIngestionCheckJob() {
  }

  @Override
  public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    LOGGER.info("## Start batch job for checking ingestion job.");

    List<IngestionHistory> histories = ingestionHistoryRepository.findByStatus(IngestionHistory.IngestionStatus.RUNNING);
    if (CollectionUtils.isNotEmpty(histories)) {
      for (IngestionHistory history : histories) {
        if (history.getIngestionMethod() != IngestionHistory.IngestionMethod.SUPERVISOR) {
          continue;
        }
        LOGGER.debug("Check '{} : {}' Job...", history.getDataSourceId(), history.getIngestionId());
        try {
          setIngestionResult(history);
        } catch (Exception e) {
          LOGGER.warn("Fail to set ingestion({}) results : {}", history.getIngestionId(), e.getMessage());
        }
      }
    } else {
      LOGGER.info("No running ingestion job to check.");
    }

    LOGGER.info("## End batch job for checking ingestion job.");
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
  public void setIngestionResult(IngestionHistory history) {

    Optional<Map> result;
    try {
      result = engineMetaRepository.getSupervisorIngestionStatus(history.getIngestionId());
    } catch (Exception e) {
      LOGGER.error("Fail to check supervisor({}) : {}", history.getIngestionId(), e.getCause());
      doFailedProcess(history, 0L, e.getMessage());
      return;
    }

    if (!result.isPresent()) {
      doFailedProcess(history, 0L, null);
      return;
    }

    // 일단 RealTime만 적용해야하기 때문에 상태처리는 이정도로 보류
    Map resultMap = result.get();
    if (resultMap.containsKey("id")) {
      history.setStatus(RUNNING);
      history.setDuration(0L);
      ingestionHistoryRepository.save(history);
      LOGGER.info("Saved ingestion({}) status : {}", history.getIngestionId(), RUNNING);
      return;
    } else {
      doFailedProcess(history, 0L, "Supervisor not found.");
      return;
    }

  }

  @Transactional
  public void doSuccessProcess(IngestionHistory history, Long duration) {
    history.setStatus(SUCCESS);
    history.setDuration(duration);
    ingestionHistoryRepository.save(history);
    LOGGER.info("Saved ingestion({}) status : {}", history.getIngestionId(), SUCCESS);

    // 성공후 바로 Enable 처리
    DataSource dataSource = dataSourceRepository.findById(history.getDataSourceId()).get();
    dataSource.setStatus(DataSource.Status.ENABLED);
    dataSourceRepository.save(dataSource);
    LOGGER.info("Marked \"ENABLED\" status to datasource({})", history.getDataSourceId());
  }

  @Transactional
  public void doFailedProcess(IngestionHistory history, Long duration, String causeMessage) {

    if (history == null) {
      return;
    }

    history.setStatus(FAILED);
    history.setDuration(duration);
    history.setCause(causeMessage);
    ingestionHistoryRepository.save(history);

    LOGGER.info("Saved ingestion({}) status : {}", history.getIngestionId(), FAILED);

    DataSource dataSource = dataSourceRepository.findById(history.getDataSourceId()).get();
    // 배치 타입의 적재인 경우,
    // 그리고 추가 적재인 경우를 제외하고 적재 결과가 실패인 경우 데이터 소스 상태 FAILED 로 변경
    if (dataSource != null &&
        !(dataSource.getIngestionInfo() instanceof BatchIngestionInfo) &&
        dataSource.getStatus() != DataSource.Status.ENABLED) {
      dataSource.setStatus(DataSource.Status.FAILED);
      dataSourceRepository.save(dataSource);
      LOGGER.info("Marked \"FAILED\" status to datasource({})", history.getDataSourceId());
    }
  }

}
