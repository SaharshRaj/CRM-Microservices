import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerSupportComponent } from '../../../pages/crm-services/customer-support/customer-support.component';
import { CustomerSupportService } from '../../../pages/crm-services/customer-support/service/customer-support.service';



@NgModule({
  declarations: [
    CustomerSupportComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    CustomerSupportService
  ]
})
export class CustomerSupportModule { }
