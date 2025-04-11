import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SalesAutomationComponent } from '../../../pages/crm-services/sales-automation/sales-automation.component';
import { SalesService } from '../../../pages/crm-services/sales-automation/service/sales.service';



@NgModule({
  declarations: [
    SalesAutomationComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    SalesService
  ]
})
export class SalesAutomationModule { }
