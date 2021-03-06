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

package com.datasphere.engine.manager.resource.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.datasphere.engine.manager.resource.common.DuplicateNameException;
import com.datasphere.engine.manager.resource.common.InvalidNameException;
import com.datasphere.engine.manager.resource.common.ResourceProviderException;

public abstract class ResourceProvider {

    /*
     * Provider
     */
    public abstract String getId();

    public abstract String getType();

    public abstract int getStatus();

    /*
     * 创建资源、更新资源、删除资源、检查资源
     */
    public abstract Resource createResource(String scopeId, String userId, String name,
            Map<String, Serializable> properties)
            throws ResourceProviderException, InvalidNameException, DuplicateNameException;

    public abstract void updateResource(Resource resource) throws ResourceProviderException;

    public abstract void deleteResource(Resource resource) throws ResourceProviderException;

    public abstract void checkResource(Resource resource) throws ResourceProviderException;

    /*
     * Properties
     */
    public abstract Set<String> listProperties();

}
