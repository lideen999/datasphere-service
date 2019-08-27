/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.datasource;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.datasphere.server.domain.user.UserProfile;

/**
 *
 */
public class DataSourceAliasProjections {

  @Projection(types = DataSourceAlias.class, name = "default")
  public interface DefaultProjection {

    String getId();

    String getDataSourceId();

    String getFieldName();

    String getNameAlias();

    @Value("#{target.valueAlias == null ? null : @objectMapper.readValue(target.valueAlias, T(java.lang.Object))}")
    Object getValueAlias();

  }

  @Projection(types = DataSourceAlias.class, name = "forDetailView")
  public interface ForDetailViewProjection {

    String getId();

    String getDataSourceId();

    String getDashBoardId();

    String getFieldName();

    String getNameAlias();

    @Value("#{target.valueAlias == null ? null : @objectMapper.readValue(target.valueAlias, T(java.lang.Object))}")
    Object getValueAlias();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getCreatedTime();

    DateTime getModifiedTime();

  }

}
