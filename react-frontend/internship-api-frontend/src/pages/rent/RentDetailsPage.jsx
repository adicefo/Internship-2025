import MasterPage from "../../components/layout/MasterPage";
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { FaArrowLeft, FaSave, FaCheck } from "react-icons/fa";
import { toast } from "react-hot-toast";
import ConfirmDialog from "../../utils/ConfirmDialog";
import { rentService, vehicleService, clientService } from "../../api";
import "./RentDetailsPage.css";

const RentDetailsPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const rent = location.state?.rent || null;
  const isAddMode = !rent;

  // Format date as full ISO string for API
  const formatDateForApi = (dateString) => {
    const date = new Date(dateString);
    return date.toISOString();
  };

  // Format date for display in form (date only)
  const formatDateForDisplay = (dateString) => {
    return dateString ? dateString.split("T")[0] : "";
  };

  // Get today's date and tomorrow's date in ISO format
  const today = new Date();
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);

  // Form data state with proper date formatting
  const [formData, setFormData] = useState({
    rentDate: today.toISOString(),
    endDate: tomorrow.toISOString(),
    vehicle_id: "",
    client_id: ""
  });

  // Lists for dropdowns
  const [vehicles, setVehicles] = useState([]);
  const [clients, setClients] = useState([]);

  // UI state
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [showDialog, setShowDialog] = useState(false);
  const [showFinishDialog, setShowFinishDialog] = useState(false);

  // Fetch vehicles and clients on component mount
  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        // Fetch vehicles
        const vehiclesResponse = await vehicleService.getAll();
        setVehicles(vehiclesResponse.data.items || []);

        // Fetch clients
        const clientsResponse = await clientService.getAll();
        setClients(clientsResponse.data.items || []);

        // If editing, populate form with rent data
        if (rent) {
          setFormData({
            rentDate: rent.rentDate || today.toISOString(),
            endDate: rent.endDate || tomorrow.toISOString(),
            vehicle_id: rent.vehicle?.id || "",
            client_id: rent.client?.id || "",
          });
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        toast.error("Failed to load necessary data");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [rent]);

  // Form validation
  const validateForm = () => {
    const newErrors = {};

    if (!formData.rentDate) {
      newErrors.rentDate = "Rent date is required";
    }

    if (!formData.endDate) {
      newErrors.endDate = "End date is required";
    }

    if (!formData.vehicle_id) {
      newErrors.vehicle_id = "Vehicle selection is required";
    }

    if (!formData.client_id && isAddMode) {
      newErrors.client_id = "Client selection is required";
    }

    // Check if end date is after rent date
    if (formData.rentDate && formData.endDate) {
      const rentDate = new Date(formData.rentDate);
      const endDate = new Date(formData.endDate);

      if (endDate < rentDate) {
        newErrors.endDate = "End date must be after rent date";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;

   
    if (name === "rentDate" || name === "endDate") {
     
      const date = new Date(value);
      const fullDate = new Date();
      fullDate.setFullYear(date.getFullYear(), date.getMonth(), date.getDate());

      setFormData({ ...formData, [name]: fullDate.toISOString() });
    } else {
      setFormData({ ...formData, [name]: value });
    }

    // Clear error for this field if it exists
    if (errors[name]) {
      setErrors({ ...errors, [name]: null });
    }
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    if (isAddMode) {
      setLoading(true);
      try {
        // Create new rent
        const request = {
          rentDate: formData.rentDate,
          endDate: formData.endDate,
          vehicle_id: formData.vehicle_id,
          client_id: formData.client_id,
        };
        const response = await rentService.create(request);
        console.log("Rent created:", response);
        toast.success("Rent created successfully");
        navigate("/rents");
      } catch (error) {
        console.error("Error saving rent:", error);
        toast.error("Error saving rent");
      } finally {
        setLoading(false);
      }
    } else {
      // For edit mode, just show the dialog
      setShowDialog(true);
    }
  };

  // Save the rent (create or update)
  const confirmEdit = async () => {
    setSaving(true);
    setShowDialog(false);

    try {
      const request = {
        rentDate: formData.rentDate,
        endDate: formData.endDate,
        vehicle_id: formData.vehicle_id,
      };

      await rentService.update(rent.id, request);
      toast.success("Rent updated successfully");
      navigate("/rents");
    } catch (error) {
      toast.error("Error updating rent");
    }
  };

 const confirmFinish= async()=>{
    setShowFinishDialog(false);
    try{
        await rentService.updateFinish(rent.id);
        toast.success("Rent updated to  finish");
        navigate("/rents");
      }catch(error){
        toast.error("Error finishing rent");
      }
          
 }

  const handleFinishRent = async() => {
setShowFinishDialog(true);
  }


  const handleGoBack = () => {
    navigate("/rents");
  };

  return (
    <MasterPage currentRoute={isAddMode ? "Add Rent" : "Edit Rent"}>
      <div className="rent-details-container">
        <div className="section-header">
          <h3 style={{color:"black"}}>{isAddMode ? "Add New Rent" : "Edit Rent"} </h3>
        </div>

        {loading ? (
          <div className="loading-indicator">Loading data...</div>
        ) : (
          <form onSubmit={handleSubmit} className="rent-form">
            <div className="form-section">
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="rentDate">Rent Date</label>
                  <input
                    type="date"
                    id="rentDate"
                    name="rentDate"
                    min={today.toISOString().split("T")[0]}
                    value={formatDateForDisplay(formData.rentDate)}
                    onChange={handleInputChange}
                    className={errors.rentDate ? "error" : ""}
                  />
                  {errors.rentDate && (
                    <div className="error-message">{errors.rentDate}</div>
                  )}
                </div>

                <div className="form-group">
                  <label htmlFor="endDate">End Date</label>
                  <input
                    type="date"
                    id="endDate"
                    name="endDate"
                    min={formData.rentDate}
                    value={formatDateForDisplay(formData.endDate)}
                    onChange={handleInputChange}
                    className={errors.endDate ? "error" : ""}
                  />
                  {errors.endDate && (
                    <div className="error-message">{errors.endDate}</div>
                  )}
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="vehicle_id">Vehicle</label>
                  <select
                    id="vehicle_id"
                    name="vehicle_id"
                    value={formData.vehicle_id}
                    onChange={handleInputChange}
                    className={errors.vehicle_id ? "error" : ""}
                  >
                    <option value="">Select a vehicle</option>
                    {vehicles.map((vehicle) => (
                      <option key={vehicle.id} value={vehicle.id}>
                        {vehicle.name}
                      </option>
                    ))}
                  </select>
                  {errors.vehicle_id && (
                    <div className="error-message">{errors.vehicle_id}</div>
                  )}
                </div>

                <div className="form-group">
                  <label htmlFor="client_id">Client</label>
                  <select
                    id="client_id"
                    name="client_id"
                    value={formData.client_id}
                    onChange={handleInputChange}
                    className={errors.client_id ? "error" : ""}
                    disabled={!isAddMode} 
                  >
                    <option value="">Select a client</option>
                    {clients.map((client) => (
                      <option key={client.id} value={client.id}>
                        {client.user?.name} {client.user?.surname}
                      </option>
                    ))}
                  </select>
                  {errors.client_id && (
                    <div className="error-message">{errors.client_id}</div>
                  )}
                </div>
              </div>
            </div>

            <div className="form-actions">
              <button
                type="button"
                className="back-button"
                onClick={handleGoBack}
                disabled={saving}
              >
                <FaArrowLeft /> Go Back
              </button>

              <div className="right-buttons">
                {rent && rent.status === "active" && (
                  <button
                    type="button"
                    className="finish-button"
                    onClick={handleFinishRent}
                    disabled={saving}
                  >
                    <FaCheck /> Finish
                  </button>
                )}
                {showFinishDialog && (
                  <ConfirmDialog
                    title="Finish Confirmation"
                    message="Are you sure you want to finish this rent?"
                    onConfirm={confirmFinish}
                    onCancel={() => setShowFinishDialog(false)}
                  />
                )}


                <button type="submit" className="save-button" disabled={saving}>
                  <FaSave /> Save
                </button>
              </div>
            </div>
          </form>
        )}

        {showDialog && (
          <ConfirmDialog
            title="Edit Confirmation"
            message="Are you sure you want to edit this rent?"
            onConfirm={confirmEdit}
            onCancel={() => setShowDialog(false)}
          />
        )}
      </div>
    </MasterPage>
  );
};

export default RentDetailsPage;
