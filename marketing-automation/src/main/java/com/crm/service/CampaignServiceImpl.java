package com.crm.service;

import com.crm.dto.CampaignDTO; 
import com.crm.dto.EmailDTO;
import com.crm.dto.SmsDTO;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository repository;
    
    @Autowired 
    private CampaignMapper mapper;

    
    @Autowired
    private JavaMailSender mailSender;


//    private CustomerDataManagement customerDataManagement;

    private static final Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

    /**
     * Add Method Info for documentation
     */
    @Override
    public List<CampaignDTO> retrieveAllCampaigns() {
        List<Campaign> allCampaigns = repository.findAll();
        List<CampaignDTO> resultList = new ArrayList<>();

        allCampaigns.forEach(e -> {
            CampaignDTO campaignDTO = mapper.mapToDTO(e); // Use the injected mapper
            resultList.add(campaignDTO);
        });

        return resultList;
    }

   @Override
    public List<CampaignDTO> createCampaigns(List<CampaignDTO> campaignDTOs) throws CampaignNotFoundException {
        List<CampaignDTO> createdCampaigns = new ArrayList<>();
        for (CampaignDTO dto : campaignDTOs) {
            createdCampaigns.add(createCampaign(dto));
        }
        return createdCampaigns;
    }

    @Override
    public CampaignDTO getCampaignById(Long campaignId) throws CampaignNotFoundException {
        Optional<Campaign> campaign = repository.findById(campaignId);
        if (campaign.isPresent()) {
            return mapper.mapToDTO(campaign.get());
        } else {
            throw new CampaignNotFoundException("Campaign Not Found with the ID");
        }
    }

    @Override
    public CampaignDTO createCampaign(CampaignDTO campaignDTO) throws CampaignNotFoundException {
        validateCampaignDates(campaignDTO.getStartDate(), campaignDTO.getEndDate());
        Campaign campaign = mapper.mapToCampaign(campaignDTO);
        Campaign savedCampaign = null;
        int customerInteractions = 0;
//        String trackingUrl = "http://localhost:3004/api/marketing" + campaign.getCampaignID() + "/track";

        if (campaign.getType() == Type.EMAIL || campaign.getType() == Type.SMS) {
        	
        	savedCampaign = repository.save(campaign);
        	String trackingUrl = "http://localhost:3004/api/marketing/" + campaign.getCampaignID() + "/track";
            if (campaign.getType() == Type.EMAIL) {
                EmailDTO emailDTO = new EmailDTO();
                emailDTO.setCampaignId(campaign.getCampaignID());
                emailDTO.setSubject(campaign.getName());
                emailDTO.setOpeningLine("Welcome to " + campaign.getName() + "!");
                emailDTO.setMsgBody("Don't miss out on our exclusive " + campaign.getName() + " offers");
                emailDTO.setClosing("Thanks for being with us");
                emailDTO.setConclusion("Click below link know more");
                emailDTO.setUrl(trackingUrl);
                emailDTO.setType(campaign.getType());
             

                // Send email directly
                sendEmail(emailDTO);
                campaignDTO = mapper.mapToDTO(savedCampaign); //map to DTO
                campaignDTO.setTrackingUrl(trackingUrl);

                // Perform other actions using Feign client (if needed)
                //otherServiceClient.updateCampaignStatus(campaign.getCampaignID(), "Email Sent");
            } else if (campaign.getType() == Type.SMS) {
                SmsDTO smsDTO = new SmsDTO();
                smsDTO.setMessage("Exclusive " + campaign.getName() + " sale Click here:" + trackingUrl);
                logger.info("SMS campaign created with URL:" + trackingUrl);
            } else {
                logger.info("The type of the notification is Invalid");
            }
            //campaign.setCustomerInteractions(customerInteractions);
            
        } else {
            throw new CampaignNotFoundException("Invalid Type of Campaign");
        }
        return campaignDTO;
    }

    public void sendEmail(EmailDTO emailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("krishnachitturi55@gmail.com");
            String trackingUrl = "http://localhost:3004/api/marketing/" + emailDTO.getCampaignId() + "/track";
            helper.setSubject(emailDTO.getSubject());
            String emailContent = "<h3>" + emailDTO.getOpeningLine() + "</h3>"
                    + "<p>" + emailDTO.getMsgBody() + "</p>"
                    + "<p>" + emailDTO.getClosing() + "</p>"
                    + "<p>" + emailDTO.getConclusion() + "</p>"
                    + "<a href='" + trackingUrl + "'>Click here</a>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Override 
    public String trackCampaignClick(Long campaignId) throws CampaignNotFoundException{
    	Optional<Campaign> campaignOpt=repository.findById(campaignId);
    	if(campaignOpt.isPresent()) {
    		Campaign campaign=campaignOpt.get();
    		campaign.setCustomerInteractions(campaign.getCustomerInteractions()+1);
    		repository.save(campaign);
    		String redirectedUrl="<h2>Thank you for your interest!</h2><p>Your response has been noted</p>"+campaignId;
    		return redirectedUrl;
    	}else {
    		throw new CampaignNotFoundException("Campaign not found with Id:"+campaignId);
    	}
    	
    }

    @Override
    public CampaignDTO updateCampaign(Long campaignId, CampaignDTO campaignDTO) throws CampaignNotFoundException {
        validateCampaignDates(campaignDTO.getStartDate(), campaignDTO.getEndDate());
        Optional<Campaign> existingCampaign = repository.findById(campaignId);
        if (existingCampaign.isPresent()) {
            Campaign campaign = existingCampaign.get();
            campaign.setName(campaignDTO.getName());
            campaign.setStartDate(campaignDTO.getStartDate());
            campaign.setEndDate(campaignDTO.getEndDate());
            campaign.setType(campaignDTO.getType());
            campaign.setCustomerInteractions(campaignDTO.getCustomerInteractions());
            repository.save(campaign);
            return mapper.mapToDTO(campaign);
        } else {
            throw new CampaignNotFoundException("Campaign Not Found with the ID");
        }
    }

    @Override
    public boolean deleteCampaign(Long campaignId) throws CampaignNotFoundException {
        Optional<Campaign> campaign = repository.findById(campaignId);
        if (campaign.isPresent()) {
            repository.deleteById(campaignId);
            return true;
        } else {
            throw new CampaignNotFoundException("Campaign Not Found with ID");
        }
    }

    @Override
    public List<CampaignDTO> getByType(Type type) {
        List<Campaign> campaign = repository.findByType(type);
        List<CampaignDTO> result = new ArrayList<>();
        for (Campaign c : campaign) {
            CampaignDTO campaignDTO = mapper.mapToDTO(c);
            result.add(campaignDTO);
        }
        return result;
    }

    @Override
    public boolean saveCampaigns(CampaignDTO campaignDTO) throws CampaignNotFoundException{
        validateCampaignDates(campaignDTO.getStartDate(), campaignDTO.getEndDate());
        Campaign campaign = mapper.mapToCampaign(campaignDTO);
        Campaign campaignSaved = repository.save(campaign);
        if (campaignDTO.getCampaignID() == campaignSaved.getCampaignID()) {
            return true;
        } else {
            throw new CampaignNotFoundException("Campaign is not found");
        }
    }

    // Validation Method
    private void validateCampaignDates(LocalDate startDate, LocalDate endDate) throws CampaignNotFoundException {
        if (startDate == null || endDate == null) return; //Dates are validated by the DTO already.

        if (endDate.isBefore(startDate)) {
            throw new CampaignNotFoundException("End date must be after or equal to start date.");
        }
    }
}