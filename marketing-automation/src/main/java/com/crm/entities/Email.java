package com.crm.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Email {
	private String subject;
	private  String openingLine;
	private String msgbody;
	private String closing;
	private String conclusion;
	private String trackingUrl;
	private Long campaignId;

    @Override
    public String toString() {
        return subject + "\n\n" +
                openingLine + "\n\n" +
                msgbody + "\n\n" +
                conclusion + "\n\n" ;
    }
}

 