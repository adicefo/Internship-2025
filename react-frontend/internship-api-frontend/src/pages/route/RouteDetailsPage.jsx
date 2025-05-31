import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect, useCallback, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { routeService, clientService, driverService } from "../../api";
import toast from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
import { FaArrowLeft, FaSave, FaPlay, FaCheckCircle } from "react-icons/fa";
import "./RouteDetailsPage.css";


// Google Maps API key
const GOOGLE_API_KEY = import.meta.env.VITE_API_KEY;

const RouteDetailsPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const route = location.state?.route || null;
  const isAddMode = !route;
  const [showDialog, setShowDialog] = useState(false);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const sourceInputRef = useRef(null);
  const destInputRef = useRef(null);
  const [sourcePredictions, setSourcePredictions] = useState([]);
  const [destPredictions, setDestPredictions] = useState([]);
  const [showSourcePredictions, setShowSourcePredictions] = useState(false);
  const [showDestPredictions, setShowDestPredictions] = useState(false);
  const [googleMapsLoaded, setGoogleMapsLoaded] = useState(false);
  const autocompleteServiceRef = useRef(null);
  const geocoderRef = useRef(null);
  const placesServiceRef = useRef(null);
  const mapRef = useRef(null);
  const [formData, setFormData] = useState({
    sourcePointLat: 0.0,
    sourcePointLon: 0.0,
    destinationPointLat: 0.0,
    destinationPointLon: 0.0,
    client_id: 0,
    driver_id: 0,
    sourceAddress: "",
    destinationAddress: "",
    status: route?.status || "wait",
  });

  const [clients, setClients] = useState([]);
  const [drivers, setDrivers] = useState([]);

  // Load Google Maps API script
  useEffect(() => {
    if (!window.google) {
      const script = document.createElement("script");
      script.src = `https://maps.googleapis.com/maps/api/js?key=${GOOGLE_API_KEY}&libraries=places`;
      script.async = true;
      script.defer = true;
      script.onload = () => {
        setGoogleMapsLoaded(true);
      };
      document.head.appendChild(script);
    } else {
      setGoogleMapsLoaded(true);
    }

    return () => {
      // Clean up script if component unmounts before loading completes
      const script = document.querySelector(`script[src*="${GOOGLE_API_KEY}"]`);
      if (script) {
        document.head.removeChild(script);
      }
    };
  }, []);

  // Initialize Google Maps services when the script is loaded
  useEffect(() => {
    if (googleMapsLoaded && window.google) {
      // Create a hidden map element for PlacesService
      const mapDiv = document.createElement("div");
      mapDiv.style.display = "none";
      document.body.appendChild(mapDiv);

      const map = new window.google.maps.Map(mapDiv, {
        center: { lat: 0, lng: 0 },
        zoom: 2,
      });

      mapRef.current = map;
      autocompleteServiceRef.current =
        new window.google.maps.places.AutocompleteService();
      geocoderRef.current = new window.google.maps.Geocoder();
      placesServiceRef.current = new window.google.maps.places.PlacesService(
        map
      );

      // Clean up when component unmounts
      return () => {
        if (mapDiv.parentNode) {
          document.body.removeChild(mapDiv);
        }
      };
    }
  }, [googleMapsLoaded]);

  const getAddressFromLatLng = async (latitude, longitude) => {
    if (!geocoderRef.current) return "Loading...";

    try {
      return new Promise((resolve, reject) => {
        geocoderRef.current.geocode(
          {
            location: { lat: parseFloat(latitude), lng: parseFloat(longitude) },
          },
          (results, status) => {
            if (status === "OK" && results && results.length > 0) {
              resolve(results[0].formatted_address);
            } else {
              reject("Address not found");
            }
          }
        );
      });
    } catch (e) {
      console.error("Error getting address:", e);
      return "Unknown location";
    }
  };

  const searchPlaces = (query, isSource) => {
    if (!autocompleteServiceRef.current || query.length < 3) {
      return;
    }

    autocompleteServiceRef.current.getPlacePredictions(
      { input: query },
      (predictions, status) => {
        if (
          status === window.google.maps.places.PlacesServiceStatus.OK &&
          predictions
        ) {
          const places = predictions.map((prediction) => ({
            placeId: prediction.place_id,
            description: prediction.description,
          }));

          if (isSource) {
            setSourcePredictions(places);
          } else {
            setDestPredictions(places);
          }
        } else {
          if (isSource) {
            setSourcePredictions([]);
          } else {
            setDestPredictions([]);
          }
        }
      }
    );
  };

  const getPlaceDetails = (placeId) => {
    if (!placesServiceRef.current) {
      return Promise.resolve({ latitude: 0.0, longitude: 0.0 });
    }

    return new Promise((resolve, reject) => {
      placesServiceRef.current.getDetails(
        { placeId: placeId, fields: ["geometry"] },
        (place, status) => {
          if (
            status === window.google.maps.places.PlacesServiceStatus.OK &&
            place &&
            place.geometry
          ) {
            resolve({
              latitude: place.geometry.location.lat(),
              longitude: place.geometry.location.lng(),
            });
          } else {
            reject("Place details not found");
          }
        }
      );
    });
  };

  useEffect(() => {
    const fetchDropdown = async () => {
      try {
        setLoading(true);
        const clientResponse = await clientService.getAll();
        const driverResponse = await driverService.getAll();
        setClients(clientResponse.data.items || []);
        setDrivers(driverResponse.data.items || []);
        setErrors(null);
      } catch (err) {
        console.error("Error fetching dropdown data:", err);
        setErrors("Failed to load dropdown data. Please try again.");
      } finally {
        setLoading(false);
      }
    };
    fetchDropdown();
  }, []);

  useEffect(() => {
    if (route && geocoderRef.current) {
      const fetchAddresses = async () => {
        try {
          const sourceAddress = await getAddressFromLatLng(
            route.sourcePointLat,
            route.sourcePointLon
          );
          const destinationAddress = await getAddressFromLatLng(
            route.destinationPointLat,
            route.destinationPointLon
          );
          console.log(route);
          setFormData({
            sourcePointLat: route.sourcePointLat,
            sourcePointLon: route.sourcePointLon,
            destinationPointLat: route.destinationPointLat,
            destinationPointLon: route.destinationPointLon,
            client_id: route.client.id,
            driver_id: route.driver.id,
            sourceAddress: sourceAddress,
            destinationAddress: destinationAddress,
            status: route.status || "wait",
          });
        } catch (err) {
          console.error("Error fetching addresses:", err);
        }
      };

      fetchAddresses();
    }
  }, [route, googleMapsLoaded]);

  const handleSourceSearch = useCallback(
    (e) => {
      const query = e.target.value;
      setFormData((prev) => ({ ...prev, sourceAddress: query }));

      if (!isAddMode) return;

      searchPlaces(query, true);
      setShowSourcePredictions(true);
    },
    [isAddMode]
  );

  const handleDestSearch = useCallback(
    (e) => {
      const query = e.target.value;
      setFormData((prev) => ({ ...prev, destinationAddress: query }));

      if (!isAddMode) return;

      searchPlaces(query, false);
      setShowDestPredictions(true);
    },
    [isAddMode]
  );

  const handlePlaceSelect = async (place, isSource) => {
    try {
      const coords = await getPlaceDetails(place.placeId);

      if (isSource) {
        setFormData((prev) => ({
          ...prev,
          sourceAddress: place.description,
          sourcePointLat: coords.latitude,
          sourcePointLon: coords.longitude,
        }));
        setShowSourcePredictions(false);
      } else {
        setFormData((prev) => ({
          ...prev,
          destinationAddress: place.description,
          destinationPointLat: coords.latitude,
          destinationPointLon: coords.longitude,
        }));
        setShowDestPredictions(false);
      }
    } catch (error) {
      console.error("Error selecting place:", error);
      toast.error("Failed to get location details");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const validateForm = () => {
    const newErrors = {};

    if (formData.client_id === 0) {
      newErrors.client_id = "Client is required";
    }

    if (formData.driver_id === 0) {
      newErrors.driver_id = "Driver is required";
    }

    if (formData.sourcePointLat === 0 || formData.sourcePointLon === 0) {
      newErrors.source = "Source location is required";
    }

    if (
      formData.destinationPointLat === 0 ||
      formData.destinationPointLon === 0
    ) {
      newErrors.destination = "Destination location is required";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSave = async () => {
    if (!validateForm()) {
      toast.error("Please fix the errors before submitting");
      return;
    }

    if (isAddMode) {
        setLoading(true);
        try {
            setLoading(true);

            const routeData = {
              sourcePointLat: formData.sourcePointLat,
              sourcePointLon: formData.sourcePointLon,
              destinationPointLat: formData.destinationPointLat,
              destinationPointLon: formData.destinationPointLon,
              client_id: formData.client_id,
              driver_id: formData.driver_id,
            };
      
            if (isAddMode) {
              await routeService.create(routeData);
              toast.success("Route created successfully");
            }
            navigate("/routes");
        } catch (error) {
          console.error('Error saving route:', error);
          toast.error('Error saving route');
        } finally {
          setLoading(false);
        }
      } else {
        // For edit mode, just show the dialog
        setShowDialog(true);
      }
   
  };

  const handleActivate = async() => {
    try {
      await routeService.update(route.id, {});
      toast.success("Route status changed to active");
      navigate("/routes");
    } catch (error) {
      console.error("Error updating route status:", error);
      toast.error("Failed to update route status");
    }
  };

  const handleFinish = async() => {
   try {
    await routeService.updateFinish(route.id);
    toast.success("Route marked as finished");
    navigate("/routes");
   } catch (error) {
    console.error("Error updating route status:", error);
    toast.error("Failed to update route status");
   }
    
  
  };

  const handleGoBack = () => {
    navigate("/routes");
  };

  
  if (!googleMapsLoaded) {
    return (
      <MasterPage currentRoute={isAddMode ? "Add Route" : "Edit Route"}>
        <div
          className="d-flex justify-content-center align-items-center"
          style={{ height: "300px" }}
        >
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      </MasterPage>
    );
  }

  return (
    <MasterPage currentRoute={isAddMode ? "Add Route" : "Edit Route"}>
      <div className="card">
        <div className="card-header">
          <h5 className="card-title">
            {isAddMode ? "Add New Route" : "Route Details"}
          </h5>
        </div>
        <div className="card-body">
          <div className="row">
            {/* Source Location */}
            <div className="col-md-6 mb-3">
              <div className="position-relative">
                <div className="form-group">
                  <label htmlFor="sourceAddress">Source Address</label>
                  <input
                    type="text"
                    className="form-control"
                    id="sourceAddress"
                    name="sourceAddress"
                    value={formData.sourceAddress}
                    onChange={handleSourceSearch}
                    ref={sourceInputRef}
                    readOnly={!isAddMode}
                    placeholder="Start typing to search for a location..."
                  />
                </div>
                {isAddMode &&
                  showSourcePredictions &&
                  sourcePredictions.length > 0 && (
                    <div
                      className="position-absolute w-100 bg-white border rounded shadow-sm"
                      style={{
                        zIndex: 1000,
                        maxHeight: "200px",
                        overflowY: "auto",
                      }}
                    >
                      {sourcePredictions.map((place) => (
                        <div
                          key={place.placeId}
                          className="p-2 border-bottom cursor-pointer"
                          onClick={() => handlePlaceSelect(place, true)}
                          style={{ cursor: "pointer" }}
                        >
                          {place.description}
                        </div>
                      ))}
                    </div>
                  )}
              </div>
              <div className="row mt-2">
                <div className="col-md-6">
                  <div className="form-group">
                    <label htmlFor="sourcePointLat">Latitude</label>
                    <input
                      type="text"
                      className="form-control"
                      id="sourcePointLat"
                      name="sourcePointLat"
                      value={formData.sourcePointLat}
                      readOnly
                    />
                  </div>
                </div>
                <div className="col-md-6">
                  <div className="form-group">
                    <label htmlFor="sourcePointLon">Longitude</label>
                    <input
                      type="text"
                      className="form-control"
                      id="sourcePointLon"
                      name="sourcePointLon"
                      value={formData.sourcePointLon}
                      readOnly
                    />
                  </div>
                </div>
              </div>
            </div>

            {/* Destination Location */}
            <div className="col-md-6 mb-3">
              <div className="position-relative">
                <div className="form-group">
                  <label htmlFor="destinationAddress">
                    Destination Address
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="destinationAddress"
                    name="destinationAddress"
                    value={formData.destinationAddress}
                    onChange={handleDestSearch}
                    ref={destInputRef}
                    readOnly={!isAddMode}
                    placeholder="Start typing to search for a location..."
                  />
                </div>
                {isAddMode &&
                  showDestPredictions &&
                  destPredictions.length > 0 && (
                    <div
                      className="position-absolute w-100 bg-white border rounded shadow-sm"
                      style={{
                        zIndex: 1000,
                        maxHeight: "200px",
                        overflowY: "auto",
                      }}
                    >
                      {destPredictions.map((place) => (
                        <div
                          key={place.placeId}
                          className="p-2 border-bottom cursor-pointer"
                          onClick={() => handlePlaceSelect(place, false)}
                          style={{ cursor: "pointer" }}
                        >
                          {place.description}
                        </div>
                      ))}
                    </div>
                  )}
              </div>
              <div className="row mt-2">
                <div className="col-md-6">
                  <div className="form-group">
                    <label htmlFor="destinationPointLat">Latitude</label>
                    <input
                      type="text"
                      className="form-control"
                      id="destinationPointLat"
                      name="destinationPointLat"
                      value={formData.destinationPointLat}
                      readOnly
                    />
                  </div>
                </div>
                <div className="col-md-6">
                  <div className="form-group">
                    <label htmlFor="destinationPointLon">Longitude</label>
                    <input
                      type="text"
                      className="form-control"
                      id="destinationPointLon"
                      name="destinationPointLon"
                      value={formData.destinationPointLon}
                      readOnly
                    />
                  </div>
                </div>
              </div>
            </div>

            {/* Client and Driver Selection */}
            <div className="col-md-6 mb-3">
              <div className="form-group">
                <label htmlFor="client_id">Client</label>
                {isAddMode ? (
                  <>
                    <select
                      className="form-control"
                      id="client_id"
                      name="client_id"
                      value={formData.client_id}
                      onChange={handleChange}
                    >
                      <option value={0}>Select Client</option>
                      {clients.map((client) => (
                        <option key={client.id} value={client.id}>
                          {client.user.name} {client.user.surname}
                        </option>
                      ))}
                    </select>
                  </>
                ) : (
                  <input
                    type="text"
                    className="form-control"
                    value={
                      clients.find((c) => c.id === formData.client_id)?.user
                        .name + " " + clients.find((c) => c.id === formData.client_id)?.user
                        .surname || ""
                    }
                    readOnly
                  />
                )}
              </div>
            </div>

            <div className="col-md-6 mb-3">
              <div className="form-group">
                <label htmlFor="driver_id">Driver</label>
                {isAddMode ? (
                  <>
                    <select
                      className="form-control"
                      id="driver_id"
                      name="driver_id"
                      value={formData.driver_id}
                      onChange={handleChange}
                    >
                      <option value={0}>Select Driver</option>
                      {drivers.map((driver) => (
                        <option key={driver.id} value={driver.id}>
                          {driver.user?.name} {driver.user?.surname}
                        </option>
                      ))}
                    </select>
                  </>
                ) : (
                  <input
                    type="text"
                    className="form-control"
                    value={
                      drivers.find((d) => d.id === formData.driver_id)?.user
                        ?.name + " " + drivers.find((d) => d.id === formData.driver_id)?.user
                        ?.surname || ""
                    }
                    readOnly
                  />
                )}
              </div>
            </div>
          </div>

          <div className="d-flex justify-content-between mt-4">
  {/* Left Side */}
  <div>
    <button
      type="button"
      className="btn btn-secondary me-2"
      onClick={handleGoBack}
    >
      <FaArrowLeft className="me-1" /> Go Back
    </button>
  </div>

  {/* Right Side */}
  <div>
    {isAddMode && (
      <button
        type="submit"
        className="save-button" // or use "save-button" if you prefer custom
        onClick={handleSave}
        disabled={loading}
      >
        <FaSave className="me-1" /> Save
      </button>
    )}

    {!isAddMode && (
      <>
        {formData.status === "wait" && (
          <button
            type="button"
            className="btn btn-primary"
            onClick={handleActivate}
            style={{ backgroundColor: "blue" }}
          >
            <FaPlay className="me-1" /> Activate
          </button>
        )}

        {formData.status === "active" && (
          <button
            type="button"
            className="btn btn-success"
            onClick={handleFinish}
          >
            <FaCheckCircle className="me-1" /> Finish
          </button>
        )}
      </>
    )}
  </div>
</div>

        </div>
      </div>
    </MasterPage>
  );
};

export default RouteDetailsPage;
