import { createAction, props } from '@ngrx/store';
import { SalesOpportunity } from '../../models/SalesOpportunity';
export const loadSales = createAction('[Sales] Load Sales');
export const loadSalesSuccess = createAction('[Sales] Load Sales Success', props<{ sales: SalesOpportunity[] }>());
export const addSale = createAction('[Sales] Add Sale', props<{ sale: SalesOpportunity }>());
export const updateSale = createAction('[Sales] Update Sale', props<{ sale: SalesOpportunity }>());
export const deleteSale = createAction('[Sales] Delete Sale', props<{ opportunityId: number }>());