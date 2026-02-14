"use client";

import * as React from "react";
import { Suspense } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Cookies from "js-cookie";
import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import Fab from "@mui/material/Fab";
import AddIcon from "@mui/icons-material/Add";
// Navbar is now in layout.tsx
import JobCard from "../components/JobCard";
import { JobApplicationDTO } from "./types";

import CircularProgress from "@mui/material/CircularProgress";

function DashboardContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [jobs, setJobs] = React.useState<JobApplicationDTO[]>([]);
  const [loading, setLoading] = React.useState(true);

  const currentTab = searchParams.get("tab") === "archived" ? 1 : 0;

  React.useEffect(() => {
    // LEARN: Auth protection - check for cookie
    const token = Cookies.get("nudge_token");
    if (!token) {
      router.push("/login");
      return;
    }

    const fetchJobs = async () => {
      try {
        setLoading(true);
        const response = await fetch(
          "http://localhost:8080/api/v1/job-applications",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        );
        if (response.ok) {
          const data = await response.json();
          console.log("Fetched jobs:", data);
          // Handle wrapped response { data: [...] } or direct array [...]
          if (Array.isArray(data)) {
            setJobs(data);
          } else if (data.data && Array.isArray(data.data)) {
            setJobs(data.data);
          } else {
            console.error("Unexpected response format", data);
            setJobs([]);
          }
        } else {
          console.error("Failed to fetch jobs");
        }
      } catch (error) {
        console.error("Error fetching jobs:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchJobs();
  }, [router]);

  const handleAddJob = () => {
    router.push("/dashboard/add");
  };

  // Filter jobs based on tab (0: Active/Applied/Interview/Offer, 1: Archived/Rejected)
  const filteredJobs = jobs.filter((job) => {
    if (currentTab === 0) return job.status !== "REJECTED";
    return job.status === "REJECTED";
  });

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 8 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Container maxWidth="md" sx={{ mt: 4, pb: 10 }}>
        {filteredJobs.map((job) => (
          <JobCard key={job.applicationId} job={job} />
        ))}
        {filteredJobs.length === 0 && (
          <Box sx={{ textAlign: "center", mt: 4, color: "text.secondary" }}>
            No jobs found in this category.
          </Box>
        )}
      </Container>

      <Fab
        color="primary"
        aria-label="add"
        sx={{ position: "fixed", bottom: 32, right: 32 }}
        onClick={handleAddJob}
      >
        <AddIcon />
      </Fab>
    </Box>
  );
}

export default function DashboardPage() {
  return (
    <Suspense
      fallback={
        <Box sx={{ display: "flex", justifyContent: "center", mt: 8 }}>
          <CircularProgress />
        </Box>
      }
    >
      <DashboardContent />
    </Suspense>
  );
}
