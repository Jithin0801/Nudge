"use client";

import * as React from "react";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import Cookies from "js-cookie";
import { Chip, CircularProgress } from "@mui/material";

import { JobApplicationDTO } from "../dashboard/types";
import { API_BASE_URL } from "../config";

interface StatusSelectorProps {
  currentStatus: string;
  possibleStatuses?: string[];
  applicationId: string;
  onStatusUpdate?: (updatedJob: JobApplicationDTO) => void;
  readOnly?: boolean;
}

export const getStatusColor = (status: string) => {
  switch (status) {
    case "APPLIED":
    case "SUBMITTED":
      return "#4285F4"; // Google Blue
    case "UNDER_REVIEW":
    case "INTERVIEW":
    case "INTERVIEWING":
    case "SCREENING":
    case "ASSESSMENT":
      return "#FBBC05"; // Google Yellow
    case "OFFER":
    case "OFFER_RECEIVED":
    case "ACCEPTED":
      return "#34A853"; // Google Green
    case "REJECTED":
    case "WITHDRAWN":
      return "#EA4335"; // Google Red
    default:
      return "#9E9E9E"; // Grey
  }
};

export const getStatusTextColor = (status: string) => {
  switch (status) {
    case "APPLIED":
    case "SUBMITTED":
      return "#1967D2"; // Darker Blue
    case "UNDER_REVIEW":
    case "INTERVIEW":
    case "INTERVIEWING":
    case "SCREENING":
    case "ASSESSMENT":
      return "#B06000"; // Darker Orange/Yellow for readability
    case "OFFER":
    case "OFFER_RECEIVED":
    case "ACCEPTED":
      return "#137333"; // Darker Green
    case "REJECTED":
    case "WITHDRAWN":
      return "#C5221F"; // Darker Red
    default:
      return "#5f6368"; // Dark Grey
  }
};

export default function StatusSelector({
  currentStatus,
  possibleStatuses = [],
  applicationId,
  onStatusUpdate,
  readOnly = false,
}: StatusSelectorProps) {
  const [status, setStatus] = React.useState(currentStatus);
  const [loading, setLoading] = React.useState(false);

  // If there are no possible statuses or readOnly is true, just show a chip
  if (readOnly || !possibleStatuses || possibleStatuses.length === 0) {
    return (
      <Chip
        label={status}
        sx={{
          backgroundColor: `${getStatusColor(status)}33`,
          color: getStatusTextColor(status),
          fontWeight: "bold",
          border: "none",
        }}
        size="small"
        variant="filled"
      />
    );
  }

  const handleChange = async (event: SelectChangeEvent) => {
    const newStatus = event.target.value;
    // Don't do anything if clicking the same status
    if (newStatus === status) return;

    setStatus(newStatus);
    setLoading(true);

    try {
      const token = Cookies.get("nudge_token");
      if (!token) {
        console.error("No token found");
        return;
      }

      const response = await fetch(
        `${API_BASE_URL}/api/v1/job-applications/${applicationId}/status?status=${newStatus}`,
        {
          method: "PATCH", // Updating status workflow
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      if (response.ok) {
        // Try to get the updated job from response or re-fetch
        let updatedJob: JobApplicationDTO | null = null;
        try {
          const data = await response.json();
          // Check if data is the JobApplicationDTO (has applicationId)
          if (data && data.applicationId) {
            updatedJob = data;
          } else if (data && data.data && data.data.applicationId) {
            updatedJob = data.data;
          }
        } catch (e) {
          // Response might be empty or string, ignore
        }

        // If we didn't get the job from PATCH, fetch it
        if (!updatedJob) {
          const getResponse = await fetch(
            `${API_BASE_URL}/api/v1/job-applications/${applicationId}`,
            {
              headers: { Authorization: `Bearer ${token}` },
            },
          );
          if (getResponse.ok) {
            const getData = await getResponse.json();
            updatedJob = getData.data || getData;
          }
        }

        if (updatedJob && onStatusUpdate) {
          onStatusUpdate(updatedJob);
        } else {
          // Fallback if we fail to get updated object but PATCH worked
          // We can at least update local status state, but we really want the full object
          setStatus(newStatus);
        }
      } else {
        console.error("Failed to update status");
        // Revert on failure
        setStatus(currentStatus);
      }
    } catch (error) {
      console.error("Error updating status:", error);
      setStatus(currentStatus);
    } finally {
      setLoading(false);
    }
  };

  const handleClick = (event: React.MouseEvent) => {
    event.stopPropagation(); // Prevent parent click (e.g. Card navigation)
  };

  return (
    <FormControl size="small" onClick={handleClick} variant="standard">
      {loading ?
        <CircularProgress size={20} />
      : <Select
          value={status}
          onChange={handleChange}
          variant="standard"
          disableUnderline
          sx={{
            fontSize: "0.8125rem",
            "& .MuiSelect-select": {
              padding: "4px 8px",
              borderRadius: "16px",
              backgroundColor: (theme) => {
                const color = getStatusColor(status);
                return `${color}33`; // 20% opacity
              },
              color: (theme) => {
                const color = getStatusTextColor(status);
                return color;
              },
            },
          }}
          renderValue={(selected) => (
            <Chip
              label={selected}
              sx={{
                backgroundColor: `${getStatusColor(selected)}33`,
                color: getStatusTextColor(selected),
                fontWeight: "bold",
                border: "none",
              }}
              size="small"
              variant="filled"
              clickable={false} // Ensure the chip itself doesn't capture clicks weirdly inside Select
            />
          )}
        >
          {possibleStatuses.map((s) => (
            <MenuItem key={s} value={s}>
              {s}
            </MenuItem>
          ))}
          {/* Always include current status if not in possible, to avoid selection errors */}
          {!possibleStatuses.includes(status) && (
            <MenuItem value={status}>{status}</MenuItem>
          )}
        </Select>
      }
    </FormControl>
  );
}
