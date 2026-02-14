"use client";

import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import Avatar from "@mui/material/Avatar";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import { useTheme } from "@mui/material/styles";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";
import { useRouter, useSearchParams, usePathname } from "next/navigation";

interface DecodedToken {
  sub: string;
  // Add other fields if needed
}

export default function Navbar() {
  const theme = useTheme();
  const router = useRouter();
  const searchParams = useSearchParams();
  const pathname = usePathname();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [userInitials, setUserInitials] = React.useState<string>("");

  const currentTab = searchParams.get("tab") === "archived" ? 1 : 0;

  React.useEffect(() => {
    const token = Cookies.get("nudge_token");
    if (token) {
      try {
        const decoded = jwtDecode<DecodedToken>(token);
        // Assuming 'sub' contains the username/email
        const username = decoded.sub || "User";
        // Extract initials: First 2 chars of username
        setUserInitials(username.substring(0, 2).toUpperCase());
      } catch (error) {
        console.error("Failed to decode token", error);
      }
    }
  }, []);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    const tabName = newValue === 1 ? "archived" : "jobs";
    router.push(`/dashboard?tab=${tabName}`);
  };

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    Cookies.remove("nudge_token");
    router.push("/login"); // Redirect to login page
    handleClose();
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar
        position="static"
        color="default"
        elevation={1}
        sx={{ bgcolor: "background.paper" }}
      >
        <Toolbar>
          <Typography
            variant="h6"
            component="div"
            sx={{
              flexGrow: 0,
              mr: 4,
              fontWeight: "bold",
              color: "primary.main",
              cursor: "pointer",
            }}
            onClick={() => router.push("/dashboard")}
          >
            Nudge
          </Typography>

          <Tabs
            value={currentTab}
            onChange={handleTabChange}
            textColor="primary"
            indicatorColor="primary"
            sx={{ flexGrow: 1 }}
          >
            <Tab label="Jobs" />
            <Tab label="Archived" />
          </Tabs>

          <Box sx={{ flexGrow: 0 }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
              sx={{ p: 0 }}
            >
              <Avatar sx={{ bgcolor: "secondary.main" }}>
                {userInitials || "U"}
              </Avatar>
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem onClick={handleLogout}>Logout</MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
