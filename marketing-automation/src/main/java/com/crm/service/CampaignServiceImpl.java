package com.crm.service;
import com.crm.dto.CampaignDTO;   
import com.crm.dto.EmailDTO;
import com.crm.dto.SmsDTO;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository repository;
    private final CampaignMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

    public CampaignServiceImpl(CampaignRepository repository, CampaignMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @Override
    public List<CampaignDTO> retrieveAllCampaigns() {
        List<Campaign> allCampaigns = repository.findAll();
        List<CampaignDTO> resultList = new ArrayList<>();
        allCampaigns.forEach(e -> resultList.add(mapper.mapToDTO(e)));
        return resultList;
    }
    @Override
    public CampaignDTO createCampaign(CampaignDTO campaignDTO) throws CampaignNotFoundException {
        validateCampaignDates(campaignDTO.getStartDate(), campaignDTO.getEndDate());
        Campaign campaign = mapper.mapToCampaign(campaignDTO);
        Campaign savedCampaign = repository.save(campaign);
        String trackingUrl = "http://localhost:3004/api/marketing/" + savedCampaign.getCampaignID() + "/track";
        savedCampaign.setTrackingUrl(trackingUrl);
        savedCampaign = repository.save(savedCampaign); // Save with tracking URL
        if (savedCampaign.getType() == Type.EMAIL) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setCampaignId(savedCampaign.getCampaignID());
            emailDTO.setSubject(savedCampaign.getName());
            emailDTO.setOpeningLine("Welcome to " + savedCampaign.getName() + "!");
            emailDTO.setMsgBody("Don't miss out on our exclusive " + savedCampaign.getName() + " offers");
            emailDTO.setClosing("Thanks for being with us");
            emailDTO.setConclusion("Click below link to know more");
            emailDTO.setTrackingUrl(trackingUrl);
            emailDTO.setType(savedCampaign.getType());
            campaign.setTrackingUrl(trackingUrl);
        }else if (savedCampaign.getType() == Type.SMS) {
            SmsDTO smsDTO = new SmsDTO();
            smsDTO.setMessage("Exclusive " + savedCampaign.getName() + " offers! Click: " + trackingUrl);
            smsDTO.setTrackingUrl(trackingUrl);
        }
        return mapper.mapToDTO(savedCampaign);
    }   
    @Override
    public String trackCampaignClick(Long campaignId) throws CampaignNotFoundException {
        logger.info("Tracking click for campaignId: {}", campaignId);
        Optional<Campaign> campaignOpt = repository.findById(campaignId);
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
            campaign.setCustomerInteractions(campaign.getCustomerInteractions()+1);
            repository.save(campaign);
            logger.info("Customer interactions updated for campaignId: {}", campaignId);
             return "http://localhost:3004/success.html";
        } else {
            logger.warn("Campaign not found with ID: {}", campaignId);
            throw new CampaignNotFoundException("Campaign not found with ID: " + campaignId);
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
            return mapper.mapToDTO(repository.save(campaign));
        } else {
            throw new CampaignNotFoundException("Campaign Not Found with ID: " + campaignId);
        }
    }
    @Override
    public boolean deleteCampaign(Long campaignId) throws CampaignNotFoundException {
        Optional<Campaign> campaign = repository.findById(campaignId);

        if (campaign.isPresent()) {
            repository.deleteById(campaignId);
            return true;
        } else {
            throw new CampaignNotFoundException("Campaign Not Found with ID: " + campaignId);
        }
    }
    @Override
    public List<CampaignDTO> getByType(Type type) {
        List<Campaign> campaigns = repository.findByType(type);
        List<CampaignDTO> result = new ArrayList<>();
        for (Campaign c : campaigns) {
            result.add(mapper.mapToDTO(c));
        }
        return result;
    }
    @Override
    public boolean saveCampaigns(CampaignDTO campaignDTO) throws CampaignNotFoundException {
        validateCampaignDates(campaignDTO.getStartDate(), campaignDTO.getEndDate());
        Campaign campaign = mapper.mapToCampaign(campaignDTO);
        Campaign campaignSaved = repository.save(campaign);
        return campaignDTO.getCampaignID().equals(campaignSaved.getCampaignID());
    }
    private void validateCampaignDates(LocalDate startDate, LocalDate endDate) throws CampaignNotFoundException {
        if (startDate == null || endDate == null)
            return;
        if (endDate.isBefore(startDate)) {
            throw new CampaignNotFoundException("End date must be after or equal to start date.");
        }
    }

	@Override
	public CampaignDTO getCampaignById(Long campaignId) throws CampaignNotFoundException {
		Optional<Campaign> campaign = repository.findById(campaignId);
		if (campaign.isPresent()) {
			return CampaignMapper.MAPPER.mapToDTO(campaign.get());
		} else {
			throw new CampaignNotFoundException("Campaign Not Found with the ID");
		}
	}

	@Override
	public List<CampaignDTO> createCampaigns(List<CampaignDTO> campaignDTOs) throws CampaignNotFoundException {
		List<CampaignDTO> createdCampaigns = new ArrayList<>();
		for (CampaignDTO dto : campaignDTOs) {
			createdCampaigns.add(createCampaign(dto));
		}
		return createdCampaigns;
	}
}