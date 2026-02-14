"use client";

// LEARN: "use client" marks this as a Client Component.
// ThemeRegistry involves React Context (ThemeProvider) which is a client-side feature.

import * as React from "react";
import { AppRouterCacheProvider } from "@mui/material-nextjs/v14-appRouter";
import { ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import { createTheme } from "@mui/material/styles";
import { Roboto } from "next/font/google";

// LEARN: Optimizing fonts with next/font/google.
const roboto = Roboto({
  weight: ["300", "400", "500", "700"],
  subsets: ["latin"],
  display: "swap",
});

// LEARN: Creating a custom Material UI theme.
// This allows us to define the color palette, typography, and component overrides globally.
const theme = createTheme({
  palette: {
    mode: "light", // Starting with light mode as requested by "calm" and "vibrant" descriptors
    primary: {
      main: "#00796b", // Teal shade - vibrant but professional
    },
    secondary: {
      main: "#ff7043", // Deep Orange - complementary and energetic
    },
    background: {
      default: "#f4f6f8", // Soft gray background
      paper: "#ffffff",
    },
  },
  typography: {
    fontFamily: roboto.style.fontFamily,
    h1: { marginBottom: "1rem" },
    h2: { marginBottom: "1rem" },
  },
  components: {
    // LEARN: Overriding component styles globally to ensure consistency.
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8, // Rounded corners for a modern feel
          textTransform: "none", // Remove uppercase default for a softer look
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          marginBottom: "1rem",
        },
      },
    },
  },
});

export default function ThemeRegistry({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    // LEARN: AppRouterCacheProvider enables MUI's emotion styling engine to work with Next.js App Router's server-side streaming.
    <AppRouterCacheProvider>
      <ThemeProvider theme={theme}>
        {/* LEARN: CssBaseline kickstarts an elegant, consistent, and simple baseline to build upon. */}
        <CssBaseline />
        {children}
      </ThemeProvider>
    </AppRouterCacheProvider>
  );
}
