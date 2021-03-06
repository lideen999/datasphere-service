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

package com.datasphere.server.common.geospatial.geojson;

import com.google.common.collect.Maps;

import java.util.Map;

public class Feature implements GeoJson {

  String id;

  GeoJsonGeometry geometry;

  Map<String, Object> properties;

  public Feature() {
  }

  public void addProperties(String key, Object value) {
    if (properties == null) {
      properties = Maps.newLinkedHashMap();
    }

    properties.put(key, value);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GeoJsonGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(GeoJsonGeometry geometry) {
    this.geometry = geometry;
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

}
