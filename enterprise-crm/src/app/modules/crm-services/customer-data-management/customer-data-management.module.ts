import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerDataManagementComponent } from '../../../pages/crm-services/customer-data-management/customer-data-management.component';
import { CustomersService } from '../../../pages/crm-services/customer-data-management/service/customers.service';



@NgModule({
  declarations: [
    CustomerDataManagementComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    CustomersService
  ]
})
export class CustomerDataManagementModule { }
