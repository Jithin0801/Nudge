"use client";

import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Link from "next/link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import AppRegistrationIcon from "@mui/icons-material/AppRegistration";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { Paper } from "@mui/material";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { API_BASE_URL } from "../../config";

export default function RegisterPage() {
  const router = useRouter();
  // LEARN: Managing form state with an object for cleaner code when there are many fields.
  const [formData, setFormData] = React.useState({
    firstName: "",
    lastName: "",
    emailId: "",
    password: "",
    dateOfBirth: "",
    phoneNumber: "",
  });

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      // LEARN: Sending JSON data to the backend.
      const response = await fetch(`${API_BASE_URL}/api/v1/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Registration successful:", data);
        // alert('Registration Successful! Please login.');
        router.push("/dashboard"); // LEARN: Redirecting to dashboard as requested.
      } else {
        const errorData = await response.text();
        console.error("Registration failed:", errorData);
        alert("Registration failed: " + (errorData || "Unknown error"));
      }
    } catch (err) {
      console.error("Registration error:", err);
      alert("An error occurred during registration.");
    }
  };

  // LEARN: Helper to get today's date for the max attribute of Date picker
  const today = new Date().toISOString().split("T")[0];

  return (
    <Container component="main" maxWidth="lg">
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          marginBottom: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Paper
          elevation={6}
          sx={{
            p: 4,
            display: "flex",
            flexDirection: { xs: "column", md: "row" },
            alignItems: "center", // Center vertically
            gap: 6,
            borderRadius: 2,
            width: "100%",
          }}
        >
          {/* Illustration Section - Placed first (left) on desktop */}
          <Box sx={{ flex: 1, display: "flex", justifyContent: "center" }}>
            <Image
              src="/assets/register_illustration_1770503137112.png"
              alt="Join Community Illustration"
              width={400} // Slightly larger for the wider container
              height={400}
              style={{ objectFit: "contain" }}
              priority
            />
          </Box>

          {/* Form Section */}
          <Box
            sx={{
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              width: "100%",
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: "primary.main" }}>
              <AppRegistrationIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign up
            </Typography>
            <Box
              component="form"
              noValidate
              onSubmit={handleSubmit}
              sx={{ mt: 3, width: "100%" }}
            >
              <Grid container spacing={2}>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <TextField
                    autoComplete="given-name"
                    name="firstName"
                    required
                    fullWidth
                    id="firstName"
                    label="First Name"
                    autoFocus
                    value={formData.firstName}
                    onChange={handleChange}
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <TextField
                    required
                    fullWidth
                    id="lastName"
                    label="Last Name"
                    name="lastName"
                    autoComplete="family-name"
                    value={formData.lastName}
                    onChange={handleChange}
                  />
                </Grid>
                <Grid size={{ xs: 12 }}>
                  <TextField
                    required
                    fullWidth
                    id="emailId"
                    label="Email Address"
                    name="emailId"
                    autoComplete="email"
                    value={formData.emailId}
                    onChange={handleChange}
                  />
                </Grid>
                <Grid size={{ xs: 12 }}>
                  <TextField
                    required
                    fullWidth
                    name="password"
                    label="Password"
                    type="password"
                    id="password"
                    autoComplete="new-password"
                    helperText="Must be at least 8 characters long."
                    value={formData.password}
                    onChange={handleChange}
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <TextField
                    required
                    fullWidth
                    id="dateOfBirth"
                    label="Date of Birth"
                    name="dateOfBirth"
                    type="date"
                    InputLabelProps={{ shrink: true }} // LEARN: Keeps the label up when type is date.
                    inputProps={{ max: today }} // LEARN: Restrict dates to the past.
                    value={formData.dateOfBirth}
                    onChange={handleChange}
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <TextField
                    required
                    fullWidth
                    id="phoneNumber"
                    label="Phone Number"
                    name="phoneNumber"
                    autoComplete="tel"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                  />
                </Grid>
              </Grid>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                Sign Up
              </Button>
              <Grid container justifyContent="flex-end">
                <Grid>
                  <Link href="/login">
                    <Typography
                      variant="body2"
                      color="primary"
                      sx={{ cursor: "pointer", textDecoration: "underline" }}
                    >
                      Already have an account? Sign in
                    </Typography>
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}
