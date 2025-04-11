import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SalesOpportunity } from '../../../../models/SalesOpportunity';
import { environment } from '../../../../../environments/environment.development';
@Injectable({
  providedIn: 'root'
})
export class SalesService {

  constructor(private readonly httpClient: HttpClient) { }

  getSales(){
    return this.httpClient.get<SalesOpportunity[]>(environment.apiUrl+'/sales-opportunity')
  }
}
