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

package com.datasphere.server.user;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import com.datasphere.server.common.Mailer;
import com.datasphere.server.domain.images.Image;
import com.datasphere.server.domain.images.ImageRepository;
import com.datasphere.server.domain.user.group.GroupService;
import com.datasphere.server.domain.workspace.WorkspaceService;
import com.datasphere.server.util.AuthUtils;

/**
 * Created by aladin on 2019. 5. 14..
 */
@RepositoryEventHandler(User.class)
public class UserEventHandler {

  @Autowired
  GroupService groupService;

  @Autowired
  WorkspaceService workspaceService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ImageRepository imageRepository;

  @Autowired
  Mailer mailer;

  @HandleBeforeCreate
  public void checkCreateAuthorityAndImage(User user) {

    if(userRepository.countByUsername(user.getUsername()) > 0) {
      throw new UserException("duplicate user");
    }

    // Executed as request state when first registering users
    user.setStatus(User.Status.REQUESTED);
    // Image processing
    if(StringUtils.isNotEmpty(user.getImageUrl())) {
      updateImages(user.getUsername());
    }

  }

  @HandleBeforeSave
  @PreAuthorize("(authentication.name == #user.id) " +
          "or hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  public void checkUpdateAuthorityAndImage(User user) {
    // Image processing
    if(StringUtils.isNotEmpty(user.getImageUrl())) {
      updateImages(user.getId());
    }
  }

  @HandleAfterSave
  @PreAuthorize("(authentication.name == #user.id) ")
  public void checkUpdateImage(User user) {
    //Update user information when modifying user image
    AuthUtils.refreshAuth(user);
  }

  @HandleBeforeDelete
  @PreAuthorize("hasAuthority('PERM_SYSTEM_MANAGE_USER')")
  public void checkDeleteAuthority(User user) {
  }

  private void updateImages(String id) {
    List<Image> targetImages = imageRepository.findByDomainAndItemIdOrderByModifiedTimeDesc("user", id);
    if(CollectionUtils.isEmpty(targetImages)) {
      return;
    }

    if(targetImages.size() == 1) {
      targetImages.get(0).setEnabled(true);
      imageRepository.save(targetImages.get(0));
    } else {
      // If you uploaded a photo multiple times
      for (int i = 0; i < targetImages.size(); i++) {
        if (i == 0) {
          targetImages.get(i).setEnabled(true);
          imageRepository.save(targetImages.get(i));
        } else {
          imageRepository.delete(targetImages.get(i));
        }
      }
    }
  }

}
