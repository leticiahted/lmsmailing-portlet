/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.tls.liferaylms.mail.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the AuditSendMails service. Represents a row in the &quot;lmsmail_AuditSendMails&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.tls.liferaylms.mail.model.impl.AuditSendMailsModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.tls.liferaylms.mail.model.impl.AuditSendMailsImpl}.
 * </p>
 *
 * @author je03042
 * @see AuditSendMails
 * @see com.tls.liferaylms.mail.model.impl.AuditSendMailsImpl
 * @see com.tls.liferaylms.mail.model.impl.AuditSendMailsModelImpl
 * @generated
 */
public interface AuditSendMailsModel extends BaseModel<AuditSendMails> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a audit send mails model instance should use the {@link AuditSendMails} interface instead.
	 */

	/**
	 * Returns the primary key of this audit send mails.
	 *
	 * @return the primary key of this audit send mails
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this audit send mails.
	 *
	 * @param primaryKey the primary key of this audit send mails
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this audit send mails.
	 *
	 * @return the uuid of this audit send mails
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this audit send mails.
	 *
	 * @param uuid the uuid of this audit send mails
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the audit send mails ID of this audit send mails.
	 *
	 * @return the audit send mails ID of this audit send mails
	 */
	public long getAuditSendMailsId();

	/**
	 * Sets the audit send mails ID of this audit send mails.
	 *
	 * @param auditSendMailsId the audit send mails ID of this audit send mails
	 */
	public void setAuditSendMailsId(long auditSendMailsId);

	/**
	 * Returns the user ID of this audit send mails.
	 *
	 * @return the user ID of this audit send mails
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this audit send mails.
	 *
	 * @param userId the user ID of this audit send mails
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this audit send mails.
	 *
	 * @return the user uuid of this audit send mails
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this audit send mails.
	 *
	 * @param userUuid the user uuid of this audit send mails
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the template ID of this audit send mails.
	 *
	 * @return the template ID of this audit send mails
	 */
	public long getTemplateId();

	/**
	 * Sets the template ID of this audit send mails.
	 *
	 * @param templateId the template ID of this audit send mails
	 */
	public void setTemplateId(long templateId);

	/**
	 * Returns the group ID of this audit send mails.
	 *
	 * @return the group ID of this audit send mails
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this audit send mails.
	 *
	 * @param groupId the group ID of this audit send mails
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the send date of this audit send mails.
	 *
	 * @return the send date of this audit send mails
	 */
	public Date getSendDate();

	/**
	 * Sets the send date of this audit send mails.
	 *
	 * @param sendDate the send date of this audit send mails
	 */
	public void setSendDate(Date sendDate);

	/**
	 * Returns the number of post of this audit send mails.
	 *
	 * @return the number of post of this audit send mails
	 */
	public long getNumberOfPost();

	/**
	 * Sets the number of post of this audit send mails.
	 *
	 * @param numberOfPost the number of post of this audit send mails
	 */
	public void setNumberOfPost(long numberOfPost);

	/**
	 * Returns the company ID of this audit send mails.
	 *
	 * @return the company ID of this audit send mails
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this audit send mails.
	 *
	 * @param companyId the company ID of this audit send mails
	 */
	public void setCompanyId(long companyId);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(AuditSendMails auditSendMails);

	public int hashCode();

	public CacheModel<AuditSendMails> toCacheModel();

	public AuditSendMails toEscapedModel();

	public String toString();

	public String toXmlString();
}