package com.crm.dto;


import com.crm.enums.Type;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailDTO {

	@NotNull(message="Subject cannot be null")
	private String subject;
	@NotNull(message="Opening line cannot be null")
    private String openingLine;
	@NotNull(message="Body of the message cannot be null and the ")
	@Size(min=5,max=100,message="The message body should atleast contain minimum 10 characters and the maximum of the 100 characters")
	private String msgBody;
	@NotNull
	private String closing;
	@Nullable
    private String conclusion;
	@NotNull(message="Type of the Notification should not be null")
	private Type type;
	private String trackingUrl;
	private Long campaignId;
	@Override
	public String toString() {
		return "EmailDTO [subject=" + subject + ", openingLine=" + openingLine + ", msgBody=" + msgBody + ", closing="
				+ closing + ", conclusion=" + conclusion + ", type=" + type + ", trackingUrl=" + trackingUrl
				+ ", campaignId=" + campaignId + "]";
	}

}
