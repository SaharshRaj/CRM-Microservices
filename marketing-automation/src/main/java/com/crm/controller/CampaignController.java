package com.crm.controller;
import com.crm.dto.CampaignDTO; 
import com.crm.exception.CampaignNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@RequestMapping("api/marketing")
public interface CampaignController {
	@GetMapping("GetAllCampaigns")
	public ResponseEntity<List<CampaignDTO>> getAllCampaigns();
	@PostMapping("campaign")
	public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO);
	@PostMapping("multipleCampaigns")
	ResponseEntity<List<CampaignDTO>> createCampaigns(@RequestBody List<CampaignDTO> campaignDTOs);
	@GetMapping("campaign/{campaignId}")
	public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable("campaignId") Long campaignId);
	@PutMapping("campaign/{campaignId}")
	public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable("campaignId") Long campaignId,@Valid @RequestBody CampaignDTO campaignDTO);
	@DeleteMapping("campaign/{campaignId}")
	public ResponseEntity<CampaignDTO> deleteCampaign(@PathVariable("campaignId") Long campaignId);
	@GetMapping("/{campaignId}/track")
	public ResponseEntity<Void> trackCampaignClick(@PathVariable("campaignId") Long campaignId) throws CampaignNotFoundException;
}
