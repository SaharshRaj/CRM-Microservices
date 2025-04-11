import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MarketingAutomationComponent } from '../../../pages/crm-services/marketing-automation/marketing-automation.component';
import { MarketingAutomationService } from '../../../pages/crm-services/marketing-automation/service/marketing-automation.service';



@NgModule({
  declarations: [
    MarketingAutomationComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    MarketingAutomationService
  ]
})
export class MarketingAutomationModule { }
