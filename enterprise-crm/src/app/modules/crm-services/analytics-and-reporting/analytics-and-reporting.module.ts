import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsAndReportingComponent } from '../../../pages/crm-services/analytics-and-reporting/analytics-and-reporting.component';
import { AnalyticsService } from '../../../pages/crm-services/analytics-and-reporting/service/analytics.service';



@NgModule({
  declarations: [
    AnalyticsAndReportingComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    AnalyticsService
  ]
})
export class AnalyticsAndReportingModule { }
