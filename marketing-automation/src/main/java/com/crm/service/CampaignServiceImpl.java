package com.crm.service;
import com.crm.dto.CampaignDTO;
import com.crm.dto.EmailDTO;
import com.crm.dto.SmsDTO;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service class for Business Logic for Creating Campaigns
 * Able to track the customer actions
 */
@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository repository;
    private final CampaignMapper mapper;
    public CampaignServiceImpl(CampaignRepository repository, CampaignMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    /**
     * Retrieves all campaigns from the database.
     *
     * @return A list of CampaignDTO objects representing all campaigns.
     */
    @Override
    public List<CampaignDTO> retrieveAllCampaigns() {
        List<Campaign> allCampaigns = repository.findAll();
        List<CampaignDTO> resultList = new ArrayList<>();
        allCampaigns.forEach(e -> resultList.add(mapper.mapToDTO(e)));
        return resultList;
    }
    /**
     * Creates a new campaign.
     *
     * @param campaignDTO The CampaignDTO object containing campaign details.
     * @return The created CampaignDTO object.
     * @throws CampaignNotFoundException If the start date is after the end date.
     */
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
            emailDTO.setSubject(savedCampaign.getName());
            emailDTO.setOpeningLine("Welcome to " + savedCampaign.getName() + "!");
            emailDTO.setMsgBody("Don't miss out on our exclusive " + savedCampaign.getName() + " offers");
            emailDTO.setClosing("Thanks for being with us");
            emailDTO.setConclusion("Click below link to know more");
            emailDTO.setTrackingUrl(trackingUrl);
            emailDTO.setType(savedCampaign.getType());
            //sendEmailNotification(emailDTO.toString());
            campaign.setTrackingUrl(trackingUrl);
        }else if (savedCampaign.getType() == Type.SMS) {
            SmsDTO smsDTO = new SmsDTO();
            smsDTO.setMessage("Exclusive " + savedCampaign.getName() + " offers! Click: " + trackingUrl);
            smsDTO.setTrackingUrl(trackingUrl);
            //sendSMSNotification(smsDTO.toString());
        }
        return mapper.mapToDTO(savedCampaign);
    }   
    /**
     * Tracks a campaign click by incrementing the customer interactions count.
     *
     * @param campaignId The ID of the campaign to track.
     * @return A URL indicating the success of the tracking.
     * @throws CampaignNotFoundException If the campaign with the given ID is not found.
     */
    @Override
    public String trackCampaignClick(Long campaignId) throws CampaignNotFoundException {
        Optional<Campaign> campaignOpt = repository.findById(campaignId);
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
            campaign.setCustomerInteractions(campaign.getCustomerInteractions()+1);
            repository.save(campaign);
             return "http://localhost:3004/success.html";
        } else {
            throw new CampaignNotFoundException("Campaign not found with ID: " + campaignId);
        }
    }
    /**
     * Updates an existing campaign.
     *
     * @param campaignId  The ID of the campaign to update.
     * @param campaignDTO The CampaignDTO object containing updated campaign details.
     * @return The updated CampaignDTO object.
     * @throws CampaignNotFoundException If the campaign with the given ID is not found or the date validation fails.
     */
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
    /**
     * Deletes a campaign by its ID.
     *
     * @param campaignId The ID of the campaign to delete.
     * @return True if the campaign was deleted successfully, false otherwise.
     * @throws CampaignNotFoundException If the campaign with the given ID is not found.
     */
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
    /**
     * Retrieves campaigns by their type.
     *
     * @param type The type of campaigns to retrieve.
     * @return A list of CampaignDTO objects representing campaigns of the specified type.
     */
    @Override
    public List<CampaignDTO> getByType(Type type) {
        List<Campaign> campaigns = repository.findByType(type);
        List<CampaignDTO> result = new ArrayList<>();
        for (Campaign c : campaigns) {
            result.add(mapper.mapToDTO(c));
        }
        return result;
    }
    /**
     * Saves a campaign.
     *
     * @param campaignDTO The CampaignDTO object to save.
     * @return True if the campaign was saved successfully, false otherwise.
     * @throws CampaignNotFoundException If the date validation fails.
     */
    @Override
    public boolean saveCampaigns(CampaignDTO campaignDTO) throws CampaignNotFoundException {
        validateCampaignDates(campaignDTO.getStartDate(), campaignDTO.getEndDate());
        Campaign campaign = mapper.mapToCampaign(campaignDTO);
        Campaign campaignSaved = repository.save(campaign);
        return campaignDTO.getCampaignID().equals(campaignSaved.getCampaignID());
        }
    /**
     * Validates that the end date is after or equal to the start date.
     *
     * @param startDate The start date of the campaign.
     * @param endDate   The end date of the campaign.
     * @throws CampaignNotFoundException If the end date is before the start date.
     */
    private void validateCampaignDates(LocalDate startDate, LocalDate endDate) throws CampaignNotFoundException {
        if (startDate == null || endDate == null)
            return;
        if (endDate.isBefore(startDate)) {
            throw new CampaignNotFoundException("End date must be after or equal to start date.");
        }
    }
    /**
     * Retrieves a campaign by its ID.
     *
     * @param campaignId The ID of the campaign to retrieve.
     * @return The CampaignDTO object representing the retrieved campaign.
     * @throws CampaignNotFoundException If the campaign with the given ID is not found.
     */
	@Override
	public CampaignDTO getCampaignById(Long campaignId) throws CampaignNotFoundException {
		Optional<Campaign> campaign = repository.findById(campaignId);
		if (campaign.isPresent()) {
			return CampaignMapper.MAPPER.mapToDTO(campaign.get());
		} else {
			throw new CampaignNotFoundException("Campaign Not Found with the ID");
		}
	}
	/**
     * Creates multiple campaigns from a list of CampaignDTO objects.
     *
     * @param campaignDTOs A list of CampaignDTO objects to create.
     * @return A list of CampaignDTO objects representing the created campaigns.
     * @throws CampaignNotFoundException If any of the campaign creations fail.
     */
	@Override
	public List<CampaignDTO> createCampaigns(List<CampaignDTO> campaignDTOs) throws CampaignNotFoundException {
		List<CampaignDTO> createdCampaigns = new ArrayList<>();
		for (CampaignDTO dto : campaignDTOs) {
			createdCampaigns.add(createCampaign(dto));
		}
		return createdCampaigns;
	}
	 /**
     * Analyzes campaign reach by campaign type, calculating average interactions and identifying
     * the highest and lowest reach campaigns within each type.
     *
     * @return A map containing campaign type as keys and analysis results as values.
     * The analysis results include average interactions, highest reach campaign details,
     * and lowest reach campaign details.
     */
	@Override
    public Map<Type, Map<String, Object>> getCampaignReachAnalysisByType() {
        List<Campaign> allCampaigns = repository.findAll();

        // Group campaigns by campaign type
        Map<Type, List<Campaign>> campaignsByType = allCampaigns.stream()
                .collect(Collectors.groupingBy(Campaign::getType));

        Map<Type, Map<String, Object>> analysisResults = new HashMap<>();

        // Analyze each campaign type group
        campaignsByType.forEach((type, campaigns) -> {
            Map<String, Object> results = new HashMap<>();

            // Calculate average interactions
            double averageInteractions = campaigns.stream()
                    .mapToInt(Campaign::getCustomerInteractions)
                    .average()
                    .orElse(0.0);
            results.put("averageInteractions", averageInteractions);

            // Find highest reach campaign
            Campaign highestReachCampaign = campaigns.stream()
                    .max((c1, c2) -> Integer.compare(c1.getCustomerInteractions(), c2.getCustomerInteractions()))
                    .orElse(null);
            if (highestReachCampaign != null) {
                results.put("highestReachCampaignName", highestReachCampaign.getName());
                results.put("highestReachInteractions", highestReachCampaign.getCustomerInteractions());
            }

            // Find lowest reach campaign
            Campaign lowestReachCampaign = campaigns.stream()
                    .min((c1, c2) -> Integer.compare(c1.getCustomerInteractions(), c2.getCustomerInteractions()))
                    .orElse(null);
            if (lowestReachCampaign != null) {
                results.put("lowestReachCampaignName", lowestReachCampaign.getName());
                results.put("lowestReachInteractions", lowestReachCampaign.getCustomerInteractions());
            }

            analysisResults.put(type, results);
        });

        return analysisResults;
    }
}