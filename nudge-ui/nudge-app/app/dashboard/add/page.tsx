"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import Cookies from "js-cookie";
import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import MenuItem from "@mui/material/MenuItem";
import Grid from "@mui/material/Grid";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

const STATUS_OPTIONS = [
  { value: "APPLIED", label: "Applied" },
  { value: "INTERVIEW", label: "Interview" },
  { value: "OFFER", label: "Offer" },
  { value: "REJECTED", label: "Rejected" },
];

export default function AddJobPage() {
  const router = useRouter();
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState("");

  const [formData, setFormData] = React.useState({
    title: "",
    companyName: "",
    summary: "",
    appliedDate: new Date().toISOString().split("T")[0], // Default to today
    applicationType: "",
    status: "APPLIED",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    setError("");

    const token = Cookies.get("nudge_token");
    if (!token) {
      router.push("/login");
      return;
    }

    try {
      // Prepare payload - remove empty optional fields if necessary,
      // but backend should handle empty strings or nulls.
      // Date fields should be YYYY-MM-DD
      const payload = {
        ...formData,
        // Ensure dates are string YYYY-MM-DD
      };

      const response = await fetch(
        "http://localhost:8080/api/v1/job-applications",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(payload),
        },
      );

      if (response.ok) {
        console.log("Job created successfully");
        router.push("/dashboard");
      } else {
        const errorText = await response.text();
        setError(`Failed to create job: ${errorText}`);
      }
    } catch (err) {
      console.error("Error creating job:", err);
      setError("An error occurred while creating the job.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => router.back()}
        sx={{ mb: 2 }}
      >
        Back to Dashboard
      </Button>

      <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
        <Typography
          variant="h5"
          component="h1"
          fontWeight="bold"
          sx={{ mb: 3 }}
        >
          Add New Job Application
        </Typography>

        <Box component="form" onSubmit={handleSubmit} noValidate>
          <Grid container spacing={3}>
            <Grid size={{ xs: 12, sm: 6 }}>
              <TextField
                required
                fullWidth
                id="title"
                name="title"
                label="Job Title"
                value={formData.title}
                onChange={handleChange}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <TextField
                required
                fullWidth
                id="companyName"
                name="companyName"
                label="Company Name"
                value={formData.companyName}
                onChange={handleChange}
              />
            </Grid>

            <Grid size={{ xs: 12, sm: 6 }}>
              <TextField
                select
                fullWidth
                id="status"
                name="status"
                label="Status"
                value={formData.status}
                onChange={handleChange}
              >
                {STATUS_OPTIONS.map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <TextField
                fullWidth
                id="applicationType"
                name="applicationType"
                label="Application Type (e.g. Full-time)"
                value={formData.applicationType}
                onChange={handleChange}
              />
            </Grid>

            <Grid size={{ xs: 12, sm: 6 }}>
              <TextField
                required
                fullWidth
                type="date"
                id="appliedDate"
                name="appliedDate"
                label="Applied Date"
                InputLabelProps={{ shrink: true }}
                value={formData.appliedDate}
                onChange={handleChange}
              />
            </Grid>

            <Grid size={{ xs: 12 }}>
              <TextField
                fullWidth
                multiline
                rows={4}
                id="summary"
                name="summary"
                label="Summary / Notes"
                value={formData.summary}
                onChange={handleChange}
              />
            </Grid>

            {error && (
              <Grid size={{ xs: 12 }}>
                <Typography color="error">{error}</Typography>
              </Grid>
            )}

            <Grid size={{ xs: 12 }}>
              <Button
                type="submit"
                variant="contained"
                size="large"
                disabled={loading}
                sx={{ minWidth: 150 }}
              >
                {loading ? "Saving..." : "Save Application"}
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Paper>
    </Container>
  );
}
