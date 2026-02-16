"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import { useParams } from "next/navigation";
import Cookies from "js-cookie";
import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import Chip from "@mui/material/Chip";
import Grid from "@mui/material/Grid";
import Divider from "@mui/material/Divider";
import { JobApplicationDTO } from "../../types";
import StatusSelector, {
  getStatusColor,
  getStatusTextColor,
} from "../../../components/StatusSelector";

export default function ApplicationDetailsPage() {
  const router = useRouter();
  const params = useParams();
  const { id } = params;
  const [job, setJob] = React.useState<JobApplicationDTO | null>(null);

  React.useEffect(() => {
    const token = Cookies.get("nudge_token");
    if (!token) {
      router.push("/login");
      return;
    }

    const fetchJobDetails = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/v1/job-applications/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        );
        if (response.ok) {
          const data = await response.json();
          // Handle wrapped response { data: ... }
          if (data.data) {
            setJob(data.data);
          } else {
            setJob(data);
          }
        } else {
          console.error("Failed to fetch job details");
        }
      } catch (error) {
        console.error("Error fetching job details:", error);
      }
    };

    if (id) {
      fetchJobDetails();
    }
  }, [id, router]);

  const handleJobUpdate = (updatedJob: JobApplicationDTO) => {
    setJob(updatedJob);
  };

  if (!job) {
    return <Box sx={{ p: 4, textAlign: "center" }}>Loading...</Box>;
  }

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => router.back()}
        sx={{ mb: 2, color: getStatusTextColor(job.status) }}
      >
        Back to Dashboard
      </Button>

      <Paper
        elevation={3}
        sx={{
          p: 4,
          borderRadius: 2,
          backgroundColor: `${getStatusColor(job.status)}0D`, // 5% opacity tint
          borderLeft: `6px solid ${getStatusColor(job.status)}`, // Consistent with JobCard
        }}
      >
        <Grid container spacing={2} alignItems="center" sx={{ mb: 3 }}>
          <Grid size={{ xs: 12, sm: 8 }}>
            <Typography variant="h4" component="h1" fontWeight="bold">
              {job.title}
            </Typography>
            <Typography
              variant="h6"
              sx={{ color: getStatusTextColor(job.status) }}
            >
              {job.companyName}
            </Typography>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }} textAlign={{ sm: "right" }}>
            <StatusSelector
              currentStatus={job.status}
              possibleStatuses={job.possibleStatuses}
              applicationId={job.applicationId}
              onStatusUpdate={handleJobUpdate}
            />
          </Grid>
        </Grid>

        <Divider sx={{ mb: 3 }} />

        <Grid container spacing={3}>
          <Grid size={{ xs: 12, sm: 6 }}>
            <Typography variant="subtitle2" color="text.secondary">
              Application Type
            </Typography>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {job.applicationType}
            </Typography>

            <Typography variant="subtitle2" color="text.secondary">
              Applied Date
            </Typography>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {job.appliedDate}
            </Typography>

            {job.lastUpdated && (
              <>
                <Typography variant="subtitle2" color="text.secondary">
                  Last Updated
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {job.lastUpdated}
                </Typography>
              </>
            )}
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            {job.nextFollowUpDate && (
              <>
                <Typography variant="subtitle2" color="text.secondary">
                  Next Follow-up
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {job.nextFollowUpDate}
                </Typography>
              </>
            )}
            {job.summary && (
              <Grid size={{ xs: 12 }}>
                <Typography variant="subtitle2" color="text.secondary">
                  Summary
                </Typography>
                <Typography variant="body1" sx={{ whiteSpace: "pre-wrap" }}>
                  {job.summary}
                </Typography>
              </Grid>
            )}
          </Grid>
        </Grid>
      </Paper>
    </Container>
  );
}
