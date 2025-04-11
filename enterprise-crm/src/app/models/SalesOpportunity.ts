export type SalesOpportunity = {
  opportunityID: number;

  customerID: number;

  estimatedValue: number;

  closingDate: string;

  followUpReminder: string | null;

  salesStage: keyof SalesStage;
};

export interface SalesStage {
  PROSPECTING: string;
  QUALIFICATION: string;
  CLOSED_WON: string;
  CLOSED_LOST: string;
};