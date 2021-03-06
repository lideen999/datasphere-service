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

import com.datasphere.server.common.MetatronProperties;
import com.datasphere.server.user.User;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * Created by aladin on 2019. 1. 26..
 */
@Component
public class Mailer {

  private final Logger LOGGER = LoggerFactory.getLogger(Mailer.class);

  @Autowired
  private MetatronProperties metatronProperties;

  @Autowired(required = false)
  private JavaMailSenderImpl javaMailSender;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  TemplateEngine templateEngine;

  @Value("${polaris.title}")
  private String title;

  @Async
  public void sendEmail(List<String> to, String subject, String content, boolean isMultipart, boolean isHtml) {
    LOGGER.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
        isMultipart, isHtml, to, subject, content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
      message.setTo(to.toArray(new String[0]));
      message.setFrom(metatronProperties.getMail().getFrom());
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);
      LOGGER.debug("Sent e-mail to User '{}'", to);
    } catch (Exception e) {
      LOGGER.warn("E-mail could not be sent to user '{}'", to, e);
    }
  }

  @Async
  public void sendPasswordResetMail(User user, String temporaryPassword, boolean isAdmin) {
    LOGGER.debug("Sending password reset e-mail to '{}'", user.getEmail());

    Context context = new Context();
    context.setVariable("user", user);
    context.setVariable("temporaryPassword", temporaryPassword);
    context.setVariable("title", this.getUpperCaseTitle());
    context.setVariable("baseUrl", metatronProperties.getMail().getBaseUrl());

    String templateName;
    if(isAdmin) {
      templateName = "email/user_reset_password_by_admin";
    } else {
      templateName = "email/user_reset_password";
    }

    String content = templateEngine.process(templateName, context);
    String subject = this.getUpperCaseTitle() + " temporary password information";
    sendEmail(Lists.newArrayList(user.getEmail()), subject, content, false, true);
  }

  @Async
  public void sendSignUpRequestMail(User user, boolean isAdmin) {
    LOGGER.debug("Sending sign up request e-mail to '{}'", user.getEmail());
    Context context = new Context();
    context.setVariable("user", user);
    context.setVariable("title", this.getUpperCaseTitle());
    context.setVariable("baseUrl", metatronProperties.getMail().getBaseUrl());

    String content = templateEngine.process("email/user_signup_request", context);
    String subject = this.getUpperCaseTitle() + " User registration request";


    List<String> toList = Lists.newArrayList();
    toList.add(metatronProperties.getMail().getAdmin());

    // TODO: 관리자 목록 가져올 것!
    sendEmail(toList, subject, content, false, true);
  }

  @Async
  public void sendSignUpApprovedMail(User user, boolean isAdmin, String password) {
    LOGGER.debug("Sending sign up approved e-mail to '{}'", user.getEmail());
    Context context = new Context();
    context.setVariable("user", user);
    context.setVariable("title", this.getUpperCaseTitle());
    context.setVariable("baseUrl", metatronProperties.getMail().getBaseUrl());

    String templateName, subject;
    if(isAdmin) {
      templateName = "email/user_signup_approved_by_admin";
      subject = this.getUpperCaseTitle() + " added your account";
      context.setVariable("temporaryPassword", password);
    } else {
      templateName = "email/user_signup_approved";
      subject = this.getUpperCaseTitle() + " approved your request";
    }

    String content = templateEngine.process(templateName, context);
    sendEmail(Lists.newArrayList(user.getEmail()), subject, content, false, true);
  }

  @Async
  public void sendSignUpDeniedMail(User user) {
    LOGGER.debug("Sending sign up denied e-mail to '{}'", user.getEmail());
    Context context = new Context();
    context.setVariable("user", user);
    context.setVariable("title", this.getUpperCaseTitle());
    context.setVariable("baseUrl", metatronProperties.getMail().getBaseUrl());

    String content = templateEngine.process("email/user_signup_denied", context);
    String subject = this.getUpperCaseTitle() + " rejected your request";

    // TODO: 관리자 목록 가져올 것!
    sendEmail(Lists.newArrayList(user.getEmail()), subject, content, false, true);
  }

  private String getUpperCaseTitle() {
    return this.title.toUpperCase();
  }

}
