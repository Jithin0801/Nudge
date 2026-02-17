export interface JobApplicationDTO {
  applicationId: string;
  title: string;
  summary?: string;
  companyName: string;
  appliedDate: string; // LocalDate yyyy-MM-dd
  nextFollowUpDate?: string; // LocalDate yyyy-MM-dd
  applicationType?: string;
  status:
    | "APPLIED"
    | "SUBMITTED"
    | "UNDER_REVIEW"
    | "INTERVIEW"
    | "INTERVIEWING"
    | "SCREENING"
    | "ASSESSMENT"
    | "OFFER"
    | "OFFER_RECEIVED"
    | "ACCEPTED"
    | "REJECTED"
    | "WITHDRAWN";
  resume?: string; // Base64 encoded for upload
  resumeFilename?: string;
  resumeId?: string;
  possibleStatuses?: string[];
  lastUpdated?: string; // LocalDateTime or LocalDate
}

export const MOCK_JOBS: JobApplicationDTO[] = [
  {
    applicationId: "1",
    title: "Senior Frontend Engineer",
    companyName: "Google",
    status: "APPLIED",
    appliedDate: "2026-02-12",
    applicationType: "Full-time",
  },
  {
    applicationId: "2",
    title: "Full Stack Developer",
    companyName: "Netflix",
    status: "INTERVIEW",
    appliedDate: "2026-02-10",
    applicationType: "Contract",
  },
  {
    applicationId: "3",
    title: "Software Engineer",
    companyName: "Amazon",
    status: "REJECTED",
    appliedDate: "2026-02-01",
    applicationType: "Full-time",
  },
];
