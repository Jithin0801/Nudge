import { Suspense } from "react";
import Box from "@mui/material/Box";
import Navbar from "../components/Navbar";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "background.default" }}>
      <Suspense
        fallback={<Box sx={{ height: 64, bgcolor: "background.paper" }} />}
      >
        <Navbar />
      </Suspense>
      {children}
    </Box>
  );
}
