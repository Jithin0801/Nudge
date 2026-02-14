import type { Metadata } from "next";
// LEARN: ThemeRegistry is our client-side wrapper for MUI.
import ThemeRegistry from "./ThemeRegistry";

export const metadata: Metadata = {
  title: "Nudge App",
  description: "Nudge application built with Next.js and Material UI",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body id="__next">
        {/* LEARN: Wrapping the app with ThemeRegistry applies the theme and handles SSR styling. */}
        <ThemeRegistry>{children}</ThemeRegistry>
      </body>
    </html>
  );
}
