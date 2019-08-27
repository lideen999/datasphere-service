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

package com.datasphere.server.domain.user.group;

import com.querydsl.jpa.JPQLQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * Created by kyungtaak on 2017. 1. 23..
 */
public class GroupRepositoryImpl extends QuerydslRepositorySupport implements GroupRepositoryExtends {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepositoryImpl.class);

  @Autowired
  private EntityManager entityManager;

  public GroupRepositoryImpl() {
    super(Group.class);
  }

  @Override
  public List<String> findGroupIdsByMemberId(String memberId) {
    QGroup qGroup = QGroup.group;
    JPQLQuery query = from(qGroup).select(qGroup.id);

    query.where(qGroup.members.any().memberId.eq(memberId));

    return query.fetch();
  }

}
