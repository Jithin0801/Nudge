"use client";

// LEARN: "use client" is required here because we use interactive components like TextField (which tracks state) and Button (which handles clicks).

import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Link from "next/link"; // LEARN: Using Next.js Link for client-side navigation without full page reload.
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { Paper } from "@mui/material";
import Image from "next/image"; // LEARN: Next.js Image component optimizes images automatically.
import { useRouter } from "next/navigation"; // LEARN: useRouter for programmatic navigation in Client Components.
import Cookies from "js-cookie"; // LEARN: Library for easy cookie management.
import { API_BASE_URL } from "../../config";

export default function LoginPage() {
  const router = useRouter();
  // LEARN: React useState hook to manage form state.
  const [loginId, setLoginId] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [error, setError] = React.useState("");

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!loginId || !password) {
      setError("Please fill in all fields");
      return;
    }
    // LEARN: Basic Auth involves encoding credentials in base64.
    const credentials = btoa(`${loginId}:${password}`);

    try {
      const response = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
        method: "POST",
        headers: {
          Authorization: `Basic ${credentials}`,
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Login Successful:", data.message);

        // LEARN: Storing the JWT token in a cookie for subsequent requests/auth checks.
        if (data.data) {
          Cookies.set("nudge_token", data.data, { expires: 7 });
        }

        router.push("/dashboard");
      } else {
        const errorData = await response.text(); // or .json() depending on backend
        setError("Login failed: " + errorData || "Invalid credentials");
      }
    } catch (err) {
      console.error("Login error:", err);
      setError("An error occurred. Please try again.");
    }
  };

  return (
    <Container component="main" maxWidth="md">
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
            flexDirection: { xs: "column", md: "row" }, // LEARN: Responsive layout: column on mobile, row on desktop.
            alignItems: "center",
            gap: 4,
            borderRadius: 2,
          }}
        >
          {/* Illustration Section */}
          <Box sx={{ flex: 1, display: "flex", justifyContent: "center" }}>
            <Image
              src="/assets/login_illustration_1770503123132.png"
              alt="Login Security Illustration"
              width={300}
              height={300}
              style={{ objectFit: "contain" }}
              priority // LEARN: Priority loads the image early as it's above the fold.
            />
          </Box>

          {/* Form Section */}
          <Box
            sx={{
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign in
            </Typography>
            <Box
              component="form"
              onSubmit={handleSubmit}
              noValidate
              sx={{ mt: 1, width: "100%" }}
            >
              <TextField
                margin="normal"
                required
                fullWidth
                id="loginId"
                label="Login ID"
                name="loginId"
                autoComplete="username"
                autoFocus
                value={loginId}
                onChange={(e) => setLoginId(e.target.value)}
                error={!!error}
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                error={!!error}
                helperText={error}
              />
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                Sign In
              </Button>
              <Button
                fullWidth
                variant="outlined"
                startIcon={
                  <img
                    src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg"
                    alt="Google"
                    width={20}
                  />
                } // Placeholder Google Icon
                sx={{ mb: 2 }}
                onClick={() => alert("Google Sign In is redundant for now")}
              >
                Sign in with Google
              </Button>
              <Grid container justifyContent="flex-end">
                <Grid>
                  {/* LEARN: Using Next.js Link component wraps the Material UI Link for styling + performance. */}
                  <Link href="/register">
                    <Typography
                      variant="body2"
                      color="primary"
                      sx={{ cursor: "pointer", textDecoration: "underline" }}
                    >
                      {"Don't have an account? Sign Up"}
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
