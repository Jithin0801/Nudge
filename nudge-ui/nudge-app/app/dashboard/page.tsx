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

  // Sort jobs: Active first, then Terminal (REJECTED, WITHDRAWN)
  // Secondary sort: Applied Date descending (newest first)
  const sortedJobs = React.useMemo(() => {
    return [...jobs].sort((a, b) => {
      const isTerminalA = a.status === "REJECTED" || a.status === "WITHDRAWN";
      const isTerminalB = b.status === "REJECTED" || b.status === "WITHDRAWN";

      if (isTerminalA && !isTerminalB) return 1; // A is terminal, push to bottom
      if (!isTerminalA && isTerminalB) return -1; // B is terminal, push to bottom

      // If both are same category, sort by date descending
      return (
        new Date(b.appliedDate).getTime() - new Date(a.appliedDate).getTime()
      );
    });
  }, [jobs]);

  const handleJobUpdate = (updatedJob: JobApplicationDTO) => {
    setJobs((prevJobs) =>
      prevJobs.map((job) =>
        job.applicationId === updatedJob.applicationId ? updatedJob : job,
      ),
    );
  };

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
        {sortedJobs.map((job) => (
          <JobCard
            key={job.applicationId}
            job={job}
            onJobUpdate={handleJobUpdate}
          />
        ))}
        {sortedJobs.length === 0 && (
          <Box sx={{ textAlign: "center", mt: 4, color: "text.secondary" }}>
            No jobs found.
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
