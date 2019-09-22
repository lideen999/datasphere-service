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

package com.datasphere.server.common.datasource;

import org.apache.commons.lang3.StringUtils;

import java.sql.Types;

import com.datasphere.datasource.Field;
import com.datasphere.server.domain.dataprep.teddy.ColumnType;

/**
 * Created by aladin on 2019. 6. 11..
 */
public enum DataType {
  STRING,
  TEXT,         // Deprecated
  NUMBER,
  INTEGER,
  LONG,
  FLOAT,
  DOUBLE,
  DECIMAL,
  TIMESTAMP,
  BOOLEAN,
  ARRAY,
  STRUCT,
  WKT,
  MAP,
  UNKNOWN;

  public String toSqlDataType() {
    switch (this) {
      case TEXT:
      case STRING:
        return "VARCHAR(500)";
      case LONG:
        return "INT";
      default:
        return this.name();
    }
  }

  public LogicalType toLogicalType() {
    switch (this) {
      case TEXT:
      case STRING:
        return LogicalType.STRING;
      case NUMBER:
        return LogicalType.NUMBER;
      case INTEGER:
      case LONG:
        return LogicalType.INTEGER;
      case FLOAT:
      case DOUBLE:
        return LogicalType.DOUBLE;
      case TIMESTAMP:
        return LogicalType.TIMESTAMP;
      case BOOLEAN:
        return LogicalType.BOOLEAN;
      case MAP:
      case STRUCT:
        return LogicalType.STRUCT;
      case ARRAY:
        return LogicalType.ARRAY;
      default:
        return LogicalType.STRING;
    }
  }

  public Field.FieldRole toRole() {
    switch (this) {
      case TEXT:
      case STRING:
      case TIMESTAMP:
      case BOOLEAN:
      case MAP:
      case STRUCT:
      case ARRAY:
        return Field.FieldRole.DIMENSION;
      case NUMBER:
      case INTEGER:
      case LONG:
      case FLOAT:
      case DOUBLE:
        return Field.FieldRole.MEASURE;
    }

    return Field.FieldRole.DIMENSION;
  }

  /**
   * JDBC type to Metatron DataType
   */
  public static DataType jdbcToFieldType(int type) {

    switch (type) {
      case Types.VARCHAR:
      case Types.CHAR:
      case Types.NCHAR:
      case Types.NVARCHAR:
      case Types.LONGNVARCHAR:
        return DataType.STRING;
      case Types.BOOLEAN:
        return DataType.BOOLEAN;
      case Types.INTEGER:
        return DataType.INTEGER;
      case Types.BIGINT:
        return DataType.LONG;
      case Types.FLOAT:
        return DataType.FLOAT;
      case Types.NUMERIC:
      case Types.DOUBLE:
      case Types.DECIMAL:
        return DataType.DOUBLE;
      case Types.ARRAY:
        return DataType.ARRAY;
      case Types.JAVA_OBJECT:
        return DataType.STRUCT;
      case Types.DATE:
      case Types.TIME:
      case Types.TIME_WITH_TIMEZONE:
      case Types.TIMESTAMP:
      case Types.TIMESTAMP_WITH_TIMEZONE:
        return DataType.TIMESTAMP;
      default:
        return DataType.STRING;
    }
  }

  public static DataType jdbcToFieldType(String type) {

    switch (type) {
      case "varchar":
      case "char":
      case "string":
        return DataType.STRING;

      case "boolean":
        return DataType.BOOLEAN;

      case "numeric":
      case "tinyint":
      case "smallint":
      case "int":
      case "integer":
        return DataType.INTEGER;

      case "bigint":
        return DataType.LONG;

      case "float":
        return DataType.FLOAT;

      case "double":
        return DataType.DOUBLE;

      case "arrays":
        return DataType.ARRAY;

      case "structs":
        return DataType.STRUCT;

      case "date":
      case "datetime":
      case "timestamp":
        return DataType.TIMESTAMP;
      //
      //      default:
      //        return DataType.STRING;
    }

    if (StringUtils.contains(type, "decimal")) {
      return DataType.DOUBLE;
    }

    if (StringUtils.contains(type, "varchar")) {
      return DataType.STRING;
    }

    return DataType.STRING;
  }

  public static DataType engineToFieldDataType(String type) {
    switch (type) {
      case "dimension.STRING":
      case "STRING":
      case "string":
        return STRING;
      case "DOUBLE":
      case "double":
        return DOUBLE;
      case "FLOAT":
      case "float":
        return FLOAT;
      case "LONG":
      case "long":
        return LONG;
      default:
        return UNKNOWN;
    }
  }

  // To be deleted soon - when row-base grid serialization comes.
  public static DataType teddyTypeToFieldType(ColumnType type) {
    switch (type) {
      case BOOLEAN:
        return BOOLEAN;
      case LONG:
        return LONG;
      case DOUBLE:
        return DOUBLE;
      case ARRAY:
        return ARRAY;
      case MAP:
        return DataType.MAP;
      case STRING:
        return STRING;
      case UNKNOWN:
      default:
        return UNKNOWN;
    }
  }
}
