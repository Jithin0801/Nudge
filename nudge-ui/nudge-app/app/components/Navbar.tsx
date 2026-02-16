"use client";

import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import Avatar from "@mui/material/Avatar";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
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
  fullName?: string;
  // Add other fields if needed
}

export default function Navbar() {
  const theme = useTheme();
  const router = useRouter();
  const searchParams = useSearchParams();
  const pathname = usePathname();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [username, setUsername] = React.useState<string>("User");

  React.useEffect(() => {
    const token = Cookies.get("nudge_token");
    if (token) {
      try {
        const decoded = jwtDecode<DecodedToken>(token);
        // Assuming 'sub' contains the username/email.
        // If your JWT has a specific 'name' field, use that.
        // For now, using sub as per previous logic but displaying it fully.
        setUsername(decoded.fullName || decoded.sub || "User");
      } catch (error) {
        console.error("Failed to decode token", error);
      }
    }
  }, []);

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

          <Box sx={{ flexGrow: 1 }} />

          <Box sx={{ flexGrow: 0 }}>
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                cursor: "pointer",
                p: 1,
                borderRadius: 2,
                "&:hover": {
                  bgcolor: "action.hover",
                },
              }}
              onClick={handleMenu}
            >
              <Typography
                variant="subtitle1"
                sx={{ mr: 1, fontWeight: "medium", color: "text.primary" }}
              >
                Hi, {username}
              </Typography>
              <ArrowDropDownIcon color="action" />
            </Box>
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: "bottom",
                horizontal: "right",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
              sx={{ mt: 1 }}
            >
              <MenuItem onClick={handleLogout}>Logout</MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
