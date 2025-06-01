import { useKeycloak } from "@react-keycloak/web";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaCar,
  FaUsers,
  FaRoute,
  FaCarAlt,
  FaBell,
  FaStar,
  FaShoppingCart,
  FaBars,
  FaSignOutAlt,
  FaCog,
  FaChartBar,
  FaMoneyBillWave,
  FaCarSide,
  FaHome,
} from "react-icons/fa";
import "./MasterPage.css";

const MasterPage = ({ children, currentRoute }) => {
  const { keycloak } = useKeycloak();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    keycloak.logout({
      redirectUri: window.location.origin,
    });
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  const navItems = [
    { title: "Drivers", icon: <FaCar />, route: "Drivers", path: "/drivers" },
    { title: "Clients", icon: <FaUsers />, route: "Clients", path: "/clients" },
    { title: "Routes", icon: <FaRoute />, route: "Route", path: "/routes" },
    {
      title: "Vehicles",
      icon: <FaCarAlt />,
      route: "Vehicles",
      path: "/vehicles",
    },
    {
      title: "Notifications",
      icon: <FaBell />,
      route: "Notification",
      path: "/notifications",
    },
    { title: "Reviews", icon: <FaStar />, route: "Review", path: "/reviews" },
    { title: "Rents", icon: <FaShoppingCart />, route: "Rent", path: "/rents" },
  ];

  const additionalItems = [
    {
      title: "Statistics",
      icon: <FaChartBar />,
      route: "Statistics",
      path: "/statistics",
    },
    {
      title: "Company Prices",
      icon: <FaMoneyBillWave />,
      route: "Company Price",
      path: "/prices",
    },
  ];

  const handleNavigation = (route, path) => {
    if (path) {
      navigate(path);
    } else {
      navigate("/dashboard");
    }
  };

  const handleDashboard = () => {
    navigate("/dashboard");
  };

  return (
    <div className="dashboard">
      {/* Header */}
      <header className="dashboard-header">
        <button className="menu-toggle" onClick={toggleSidebar}>
          <FaBars />
        </button>
        <h1>eCar Management System</h1>
        <button onClick={handleLogout} className="logout-button">
          Logout
        </button>
      </header>

      {/* Sidebar Navigation */}
      <div className={`sidebar ${sidebarOpen ? "open" : ""}`}>
        <div className="sidebar-header">
          <div className="sidebar-logo">
            <span>@eCar</span>
          </div>
          <div className="user-avatar">
            <div className="avatar-circle">
              <FaCarAlt />
            </div>
          </div>
          <div className="user-name">
            {keycloak.tokenParsed?.preferred_username ||
              keycloak.tokenParsed?.name ||
              "User"}
          </div>
        </div>

        <div className="nav-items">
          {navItems.map((item) => (
            <div
              key={item.route}
              className={`nav-item ${
                currentRoute === item.route ? "active" : ""
              }`}
              onClick={() => handleNavigation(item.route, item.path)}
            >
              <div className="nav-icon">{item.icon}</div>
              <div className="nav-title">{item.title}</div>
            </div>
          ))}

          <div className="nav-divider"></div>

          <div className="additional-menu">
            <div className="additional-header">
              <div className="additional-icon">
                <FaCog />
              </div>
              <div className="additional-title">Additional</div>
            </div>

            <div className="additional-items">
              {additionalItems.map((item) => (
                <div
                  key={item.route}
                  className={`nav-item ${
                    currentRoute === item.route ? "active" : ""
                  }`}
                  onClick={() => handleNavigation(item.route, item.path)}
                >
                  <div className="nav-icon">{item.icon}</div>
                  <div className="nav-title">{item.title}</div>
                </div>
              ))}
            </div>
          </div>

          <div className="nav-items dashboard-items">
            <div
              className={`nav-item ${
                currentRoute === "Dashboard" ? "active" : ""
              }`}
              onClick={handleDashboard}
            >
              <div className="nav-icon">
                <FaHome />
              </div>
              <div className="nav-title">Dashboard</div>
            </div>
            <div className="nav-item logout" onClick={handleLogout}>
              <div className="nav-icon">
                <FaSignOutAlt />
              </div>
              <div className="nav-title">Log out</div>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className={`main-content ${sidebarOpen ? "shifted" : ""}`}>
        <div className="dashboard-content">
          <div className="welcome-banner">
            <div className="welcome-text">
              <h2>
                Welcome,{" "}
                {keycloak.tokenParsed?.preferred_username ||
                  keycloak.tokenParsed?.name ||
                  "User"}
              </h2>
              <p>
                Current section:{" "}
                <span className="highlight">{currentRoute}</span>
              </p>
            </div>
            <div className="date-display">
              {new Date().toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            </div>
          </div>

          {/* Page-specific content */}
          {children}
        </div>
      </div>

      {/* Overlay for mobile */}
      {sidebarOpen && (
        <div className="sidebar-overlay" onClick={toggleSidebar}></div>
      )}
    </div>
  );
};

export default MasterPage;
