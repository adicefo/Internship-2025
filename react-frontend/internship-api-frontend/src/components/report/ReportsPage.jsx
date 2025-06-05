import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  CircularProgress,
} from "@mui/material";
import { BarChart } from "@mui/x-charts/BarChart";
import { PieChart } from "@mui/x-charts/PieChart";
import { RadarChart } from "@mui/x-charts/RadarChart";
import MasterPage from "../../components/layout/MasterPage";
import {
  driverService,
  rentService,
  reviewService,
  routeService,
} from "../../api";
import "./ReportsPage.css";
import { jsPDF } from "jspdf";
import html2canvas from "html2canvas";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";

const monthNames = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

const mockDrivers = [
  { name: "John Doe", clients: 5, hours: 40 },
  { name: "Jane Smith", clients: 8, hours: 35 },
  { name: "Bob Brown", clients: 3, hours: 50 },
];

const ReportsPage = () => {
  const [filter, setFilter] = useState("clients");
  const [drivers, setDrivers] = useState([]);
  const [routeData2025, setRouteData2025] = useState(Array(12).fill(null));
  const [rentData2025, setRentData2025] = useState(Array(12).fill(null));
  const [rents, setRents] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [topClients, setTopClients] = useState([]);
  const [total2025, setTotal2025] = useState(0);
  const [total2025Rent, setTotal2025Rent] = useState(0);
  const [loadingRoute, setLoadingRoute] = useState(true);
  const [radarData, setRadarData] = useState([]);

  const fetchDrivers = async () => {
    try {
      const response = await driverService.getAll();
      const items = response.data.items || [];
      setDrivers(items);
    } catch (error) {
      console.log("Error while fetching drivers " + error);
    }
  };
  const fetchReviewAverage = async () => {
    try {
      const response = await reviewService.getAverageReview();
      const items = response.data || [];
      setRadarData(items);
    } catch (error) {
      console.log("Error while fetching radar data " + error);
    }
  };
  const fetchRents = async () => {
    try {
      const response = await rentService.getAll();
      const items = response.data.items || [];
      setRents(items);
    } catch (error) {
      console.log("Error while fetching rents " + error);
    }
  };

  const fetchRouteData2025 = async () => {
    setLoadingRoute(true);
    let data = [];
    let total = 0;
    for (let month = 1; month <= 12; month++) {
      try {
        const response = await routeService.getForReport({
          month: month,
          year: 2025,
        });
        const amount = response.data?.fullAmount ?? response.data ?? 0;
        data.push(amount);
        total += amount;
      } catch (e) {
        data.push(0);
      }
    }
    setRouteData2025(data);
    setTotal2025(total);
    setLoadingRoute(false);
  };
  const fetchRentData2025 = async () => {
    let data = [];
    let total = 0;
    for (let month = 1; month <= 12; month++) {
      try {
        const response = await rentService.getAmountForReport({
          month: month,
          year: 2025,
        });
        const amount = response.data?.fullAmount ?? response.data ?? 0;
        data.push(amount);
        total += amount;
      } catch (e) {
        data.push(0);
      }
    }
    setRentData2025(data);
    setTotal2025Rent(total);
  };

  useEffect(() => {
    fetchDrivers();
  }, [filter]);
  useEffect(() => {
    fetchRents();
  }, []);
  useEffect(() => {
    fetchReviewAverage();
  }, []);
  useEffect(() => {
    fetchRouteData2025();
  }, []);
  useEffect(() => {
    fetchRentData2025();
  }, []);
  useEffect(() => {
    const fetchTopClients = async () => {
      const res = await routeService.getTopClients();
      setTopClients(res.data);
    };
    fetchTopClients();
  }, []);

  // Prepare data for chart
  const labels = drivers.map(
    (d, index) => d.user.name ?? `Driver ${index + 1}`
  );
  const data =
    filter === "clients"
      ? drivers.map((d) => d.numberOfClientsAmount)
      : drivers.map((d) => d.numberOfHoursAmount);

  // After fetching rents
  const vehicleUsageMap = {};
  rents.forEach((r) => {
    const name = r.vehicle.name;
    vehicleUsageMap[name] = (vehicleUsageMap[name] || 0) + 1;
  });

  const pieData = Object.entries(vehicleUsageMap).map(([name, count]) => ({
    id: name,
    value: count,
    label: name,
  }));

  const transformedData = radarData.map((item) => ({
    client: item.client,
    averageReview: item.averageReview,
  }));

  // PDF Export Handler (multi-page, file dialog, improved heading/date)
  const handleExportPDF = async () => {
    const input = document.getElementById("report-content");
    if (!input) return;
    const pdf = new jsPDF("p", "mm", "a4");
    const pdfWidth = pdf.internal.pageSize.getWidth();
    const pdfHeight = pdf.internal.pageSize.getHeight();
    // Render the content to canvas
    const canvas = await html2canvas(input, { scale: 2, useCORS: true });
    const imgData = canvas.toDataURL("image/png");
    const imgProps = pdf.getImageProperties(imgData);
    const imgWidth = pdfWidth - 30;
    const imgHeight = (imgProps.height * imgWidth) / imgProps.width;
    const pageHeight = pdfHeight - 50;
    let remainingHeight = imgHeight;
    let position = 40;
    let pageNum = 0;
    let sY = 0;
    const now = new Date();
    const dateString = `Exported: ${now.toLocaleString()}`;
    while (remainingHeight > 0) {
      if (pageNum === 0) {
        // Date prominent, top right
        pdf.setFontSize(16);
        pdf.setTextColor("#d32f2f");
        pdf.setFont(undefined, "bold");
        pdf.text(dateString, pdfWidth - 15, 20, { align: "right" });
      } else {
        // Other pages: only date prominent at top right
        pdf.setFontSize(16);
        pdf.setTextColor("#d32f2f");
        pdf.setFont(undefined, "bold");
        pdf.text(dateString, pdfWidth - 15, 20, { align: "right" });
      }
      // Add the image slice for this page
      pdf.addImage(
        imgData,
        "PNG",
        15,
        40,
        imgWidth,
        Math.min(pageHeight, remainingHeight),
        undefined,
        "FAST",
        0,
        sY
      );
      remainingHeight -= pageHeight;
      sY += (pageHeight * imgProps.height) / imgHeight;
      if (remainingHeight > 0) {
        pdf.addPage();
        pageNum++;
      }
    }
    // File System Access API for save dialog
    if (window.showSaveFilePicker) {
      try {
        const opts = {
          suggestedName: "eCar_Report.pdf",
          types: [
            {
              description: "PDF file",
              accept: { "application/pdf": [".pdf"] },
            },
          ],
        };
        const handle = await window.showSaveFilePicker(opts);
        const writable = await handle.createWritable();
        await writable.write(pdf.output("arraybuffer"));
        await writable.close();
        return;
      } catch (e) {
        // fallback to download
      }
    }
    pdf.save("eCar_Report.pdf");
  };

  return (
    <MasterPage currentRoute="Reports">
      <Box sx={{ p: 4 }}>
        {/* PDF Export Button */}
        <Box
          sx={{
            display: "flex",
            justifyContent: "flex-end",
            alignItems: "center",
            mb: 2,
          }}
        >
          <button
            onClick={handleExportPDF}
            style={{
              background: "#d32f2f",
              color: "white",
              border: "none",
              borderRadius: 6,
              padding: "8px 18px",
              fontWeight: "bold",
              fontSize: 16,
              cursor: "pointer",
              display: "flex",
              alignItems: "center",
              gap: 8,
              boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
              transition: "background 0.2s",
            }}
            title="Export report to PDF"
          >
            <PictureAsPdfIcon style={{ color: "white" }} />
            Export PDF
          </button>
        </Box>
        {/* Main Report Content for PDF */}
        <div id="report-content">
          <Box sx={{ mb: 4 }}>
            <Typography
              variant="h3"
              sx={{ color: "#1976d2", fontWeight: 700 }}
              gutterBottom
              align="left"
            >
              eCar Report Management
            </Typography>
            <Typography
              variant="h5"
              sx={{ color: "#333", fontWeight: 500 }}
              gutterBottom
              align="left"
            >
              Performance and Summary Statistics of Business Sectors
            </Typography>
            <Typography
              variant="subtitle2"
              sx={{ color: "#d32f2f", fontWeight: 700, fontSize: 16 }}
              align="right"
            >
              Exported: {new Date().toLocaleString()}
            </Typography>
          </Box>
          <Typography variant="h4" gutterBottom>
            Driver Reports
          </Typography>
          <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 3 }}>
            <FormControl sx={{ minWidth: 200 }}>
              <InputLabel>Filter by</InputLabel>
              <Select
                value={filter}
                label="Filter by"
                onChange={(e) => setFilter(e.target.value)}
              >
                <MenuItem value="clients">Clients</MenuItem>
                <MenuItem value="hours">Hours</MenuItem>
              </Select>
            </FormControl>
          </Box>
          <Box>
            <Typography variant="h6" gutterBottom>
              {filter === "clients"
                ? "Number of clients per driver"
                : "Number of hours per driver"}
            </Typography>
            <BarChart
              xAxis={[
                {
                  id: "driver",
                  data: labels,
                  scaleType: "band",
                  label: "Driver",
                },
              ]}
              series={[
                { data, label: filter === "clients" ? "Clients" : "Hours" },
              ]}
              width={500}
              height={300}
            />
          </Box>

          <Box sx={{ mt: 6 }}>
            <Typography variant="h5" gutterBottom>
              Route Report (2025)
            </Typography>
            <Typography variant="subtitle1" sx={{ mb: 2 }}>
              Yearly Distance Data (KM)
            </Typography>
            {loadingRoute ? (
              <Box
                sx={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  height: 120,
                }}
              >
                <CircularProgress />
              </Box>
            ) : (
              <TableContainer
                component={Paper}
                sx={{
                  borderRadius: 2,
                  boxShadow: 2,
                  maxWidth: "100%",
                  overflowX: "auto",
                }}
              >
                <Table sx={{ minWidth: 900 }}>
                  <TableHead>
                    <TableRow sx={{ backgroundColor: "#f5f5f5" }}>
                      <TableCell sx={{ fontWeight: "bold" }}>Year</TableCell>
                      {monthNames.map((month) => (
                        <TableCell key={month} sx={{ fontWeight: "bold" }}>
                          {month}
                        </TableCell>
                      ))}
                      <TableCell sx={{ fontWeight: "bold" }}>Total</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    <TableRow>
                      <TableCell
                        sx={{
                          fontWeight: "bold",
                          backgroundColor: "#e8f5e9",
                          color: "#388e3c",
                        }}
                      >
                        2025
                      </TableCell>
                      {routeData2025.map((amount, idx) => (
                        <TableCell key={idx}>
                          {amount?.toFixed(2) ?? "0.00"} KM
                        </TableCell>
                      ))}
                      <TableCell
                        sx={{
                          fontWeight: "bold",
                          backgroundColor: "#e8f5e9",
                          color: "#388e3c",
                        }}
                      >
                        {total2025.toFixed(2)} KM
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </Box>

          <Typography
            variant="h5"
            sx={{ mt: 6 }}
            gutterBottom
            className="vehicleTypography"
          >
            Vehicle Usage (Rents)
          </Typography>
          <PieChart
            series={[
              {
                data: pieData,
              },
            ]}
            width={500}
            height={300}
          />
          <Typography variant="h5" sx={{ mt: 6 }} gutterBottom>
            Clients with most drives
          </Typography>
          <BarChart
            xAxis={[
              {
                data: topClients.map((c) => c.client),
                scaleType: "band",
              },
            ]}
            series={[
              {
                data: topClients.map((c) => c.count),
                label: "Routes",
              },
            ]}
            width={500}
            height={300}
          />
          <Box sx={{ mt: 6 }}>
            <Typography variant="h5" gutterBottom>
              Reviewing Clients
            </Typography>
            <Typography variant="subtitle1" gutterBottom>
              The average value of review of every client
            </Typography>
            {transformedData.length > 0 && (
              <RadarChart
                height={500}
                series={[
                  {
                    label: "Average Review",
                    data: transformedData.map((item) => item.averageReview),
                  },
                ]}
                radar={{
                  metrics: transformedData.map((item) => item.client),
                }}
                margin={{ top: 70, bottom: 70, left: 70, right: 70 }}
              />
            )}
          </Box>
          <Box sx={{ mt: 6 }}>
            <Typography variant="h5" gutterBottom>
              Rent Report (2025)
            </Typography>
            <Typography variant="subtitle1" sx={{ mb: 2 }}>
              Yearly Distance Data (KM)
            </Typography>
            {loadingRoute ? (
              <Box
                sx={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  height: 120,
                }}
              >
                <CircularProgress />
              </Box>
            ) : (
              <TableContainer
                component={Paper}
                sx={{
                  borderRadius: 2,
                  boxShadow: 2,
                  maxWidth: "100%",
                  overflowX: "auto",
                }}
              >
                <Table sx={{ minWidth: 900 }}>
                  <TableHead>
                    <TableRow sx={{ backgroundColor: "#f5f5f5" }}>
                      <TableCell sx={{ fontWeight: "bold" }}>Year</TableCell>
                      {monthNames.map((month) => (
                        <TableCell key={month} sx={{ fontWeight: "bold" }}>
                          {month}
                        </TableCell>
                      ))}
                      <TableCell sx={{ fontWeight: "bold" }}>Total</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    <TableRow>
                      <TableCell
                        sx={{
                          fontWeight: "bold",
                          backgroundColor: "#e8f5e9",
                          color: "#388e3c",
                        }}
                      >
                        2025
                      </TableCell>
                      {rentData2025.map((amount, idx) => (
                        <TableCell key={idx}>
                          {amount?.toFixed(2) ?? "0.00"} KM
                        </TableCell>
                      ))}
                      <TableCell
                        sx={{
                          fontWeight: "bold",
                          backgroundColor: "#e8f5e9",
                          color: "#388e3c",
                        }}
                      >
                        {total2025Rent.toFixed(2)} KM
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </Box>
        </div>
      </Box>
    </MasterPage>
  );
};

export default ReportsPage;
