import { createReducer, on } from '@ngrx/store';
import * as SalesActions from './sales.actions';
import { SalesOpportunity } from '../../models/SalesOpportunity';
export interface SalesState {
 sales: SalesOpportunity[];
}
const initialState: SalesState = {
 sales: [],
};
export const salesReducer = createReducer(
 initialState,
 on(SalesActions.loadSalesSuccess, (state, { sales }) => ({ ...state, sales })),
 on(SalesActions.addSale, (state, { sale }) => ({ ...state, sales: [...state.sales, sale] })),
 on(SalesActions.updateSale, (state, { sale }) => ({
   ...state,
   sales: state.sales.map(s => s.opportunityID === sale.opportunityID ? sale : s)
 })),
 on(SalesActions.deleteSale, (state, { opportunityId }) => ({
   ...state,
   sales: state.sales.filter(s => s.opportunityID !== opportunityId)
 }))
);