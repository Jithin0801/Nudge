"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import Card from "@mui/material/Card";
import CardActionArea from "@mui/material/CardActionArea";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Chip from "@mui/material/Chip";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import { JobApplicationDTO } from "../dashboard/types";

interface JobCardProps {
  job: JobApplicationDTO;
}

const getStatusColor = (status: string) => {
  switch (status) {
    case "APPLIED":
      return "info";
    case "INTERVIEW":
      return "warning";
    case "OFFER":
      return "success";
    case "REJECTED":
      return "error";
    default:
      return "default";
  }
};

export default function JobCard({ job }: JobCardProps) {
  const router = useRouter();

  const handleClick = () => {
    router.push(`/dashboard/application/${job.applicationId}`);
  };

  return (
    <Card sx={{ mb: 2, borderRadius: 2, boxShadow: 1 }}>
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
                <Chip
                  label={job.status}
                  color={getStatusColor(job.status) as any}
                  size="small"
                  variant="outlined"
                />
                <Typography variant="caption" color="text.secondary">
                  Applied: {job.appliedDate}
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
