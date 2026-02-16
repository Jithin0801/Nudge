"use client";

import * as React from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Card from "@mui/material/Card";
import CardActionArea from "@mui/material/CardActionArea";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Chip from "@mui/material/Chip";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import { JobApplicationDTO } from "../dashboard/types";
import StatusSelector, { getStatusColor } from "./StatusSelector";

interface JobCardProps {
  job: JobApplicationDTO;
  onJobUpdate?: (job: JobApplicationDTO) => void;
}

export default function JobCard({ job, onJobUpdate }: JobCardProps) {
  const router = useRouter();
  const searchParams = useSearchParams();
  const currentTab = searchParams.get("tab") || "jobs";

  const handleClick = () => {
    router.push(
      `/dashboard/application/${job.applicationId}?tab=${currentTab}`,
    );
  };

  return (
    <Card
      sx={{
        mb: 2,
        borderRadius: 2,
        boxShadow: 1,
        borderLeft: `6px solid ${getStatusColor(job.status)}`,
        backgroundColor: `${getStatusColor(job.status)}0D`, // 5% opacity for very light tint
      }}
    >
      <CardActionArea onClick={handleClick}>
        <CardContent>
          <Grid container alignItems="center" spacing={2}>
            <Grid size={{ xs: 12, sm: 8 }}>
              <Typography
                variant="h6"
                component="div"
                sx={{ fontWeight: "bold" }}
              >
                {job.title}
              </Typography>
              <Typography sx={{ mb: 1.5 }} color="text.secondary">
                {job.companyName}
              </Typography>
            </Grid>
            <Grid
              size={{ xs: 12, sm: 4 }}
              container
              justifyContent="flex-end"
              alignItems="center"
            >
              <Box
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "flex-end",
                  gap: 1,
                }}
              >
                <StatusSelector
                  currentStatus={job.status}
                  possibleStatuses={job.possibleStatuses}
                  applicationId={job.applicationId}
                  onStatusUpdate={onJobUpdate}
                />
                <Typography variant="caption" color="text.secondary">
                  Applied: {job.appliedDate}
                </Typography>
                {job.lastUpdated && (
                  <Typography variant="caption" color="text.secondary">
                    Updated: {job.lastUpdated}
                  </Typography>
                )}
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
