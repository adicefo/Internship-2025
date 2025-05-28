import MasterPage from "../../components/layout/MasterPage";
import { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { FaArrowLeft, FaSave, FaGasPump, FaCar, FaDollarSign, FaImage } from 'react-icons/fa';
import { vehicleService } from '../../api';
import './VehicleDetailsPage.css';
import ConfirmDialog from '../../utils/ConfirmDialog';
import toast from 'react-hot-toast';
import { getMimeTypeFromBase64 } from '../../utils/StringHelpers';

const VehicleDetailsPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const vehicle = location.state?.vehicle || null;
  const isAddMode = !vehicle;
  const [showDialog, setShowDialog] = useState(false);
  const [formData, setFormData] = useState({
      "available": true,
      "averageFuelConsumption": '0',
      "name": "",
      "price": '0',
      "image": ""
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [imagePreview, setImagePreview] = useState(null);
  const fileInputRef = useRef(null);

  useEffect(() => {
    if(vehicle){
        setFormData({
            available: vehicle.available,
            averageFuelConsumption: vehicle.averageFuelConsumption,
            name: vehicle.name,
            price: vehicle.price,
            image: vehicle.image
        });

        if (vehicle.image) {
            const mimeType = getMimeTypeFromBase64(vehicle.image);
  const base64Image = `data:${mimeType};base64,${vehicle.image}`;
  setImagePreview(base64Image);
        }
    }
  }, [vehicle]);


  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value
    });
    
    // Clear error when field is modified
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: null
      });
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    // Name validation
    if (!formData.name) {
        newErrors.name = "Name is required";
    }
    if(!formData.price){
        newErrors.price = "Price is required";
    }
    else if(formData.price < 40 || formData.price > 60){
        newErrors.price = "Price must be between 40 and 60";
    }
    if(!formData.averageFuelConsumption){
        newErrors.averageFuelConsumption = "Average Consumption is required";
    }
    else if(formData.averageFuelConsumption < 3 || formData.averageFuelConsumption > 18){
        newErrors.averageFuelConsumption = "Average Consumption must be between 3 and 18";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  }

  const confirmEdit=async()=>{
    setShowDialog(false);
    try{
        var request={
            "name": formData.name,
            "price": formData.price,
            "averageFuelConsumption": formData.averageFuelConsumption,
            "available": formData.available,
            "image": formData.image //should be base64
        };
      await vehicleService.update(vehicle.id, request);
      toast.success('Vehicle updated successfully');
      navigate('/vehicles');
    }catch(error){
      toast.error('Error updating vehicle');
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    if (isAddMode) {
      setLoading(true);
      try {
        // Create new vehicle
        var request = {
         "available": formData.available,
         "averageFuelConsumption": formData.averageFuelConsumption,
         "name": formData.name,
         "price": formData.price,
         "image": formData.image //should be base64
        };
        const response = await vehicleService.create(request);
        console.log('Vehicle created:', response);
        toast.success('Vehicle created successfully');
        navigate('/vehicles');
      } catch (error) {
        console.error('Error saving vehicle:', error);
        toast.error('Error saving vehicle');
      } finally {
        setLoading(false);
      }
    } else {
      // For edit mode, just show the dialog
      setShowDialog(true);
    }
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      
      
      const reader = new FileReader();
      reader.onloadend = () => {
        const base64String = reader.result.toString();
        const base64Data = base64String.split(',')[1];
        setFormData({
          ...formData,
          image: base64Data
        });
        setImagePreview(base64String);
      };
      reader.readAsDataURL(file);
    }
  };

  const triggerFileInput = () => {
    fileInputRef.current.click();
  };
    
  return (
    <MasterPage currentRoute={isAddMode ? "Add Vehicle" : "Edit Vehicle"}>
      <div className="form-container">
        <form onSubmit={handleSubmit}>
          <div className="form-section">
            <h3>Vehicle Information</h3>
            
            {/* Row 1: Available and Average Consumption */}
            <div className="form-row">
              <div className="form-group checkbox-group">
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    name="available"
                    checked={formData.available}
                    onChange={handleInputChange}
                  />
                  <span>Availability of car</span>
                </label>
              </div>
              
              <div className="form-group">
                <label htmlFor="averageFuelConsumption">Average Consumption (l/km)</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon"><FaGasPump /></i>
                  <input
                    type="number"
                    step="0.1"
                    id="averageFuelConsumption"
                    name="averageFuelConsumption"
                    value={formData.averageFuelConsumption}
                    onChange={handleInputChange}
                    className={errors.averageFuelConsumption ? 'input-error' : ''}
                  />
                </div>
                {errors.averageFuelConsumption && <div className="error-message">{errors.averageFuelConsumption}</div>}
              </div>
            </div>
            
            {/* Row 2: Vehicle Name and Price */}
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="name">Vehicle Name</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon"><FaCar /></i>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    className={errors.name ? 'input-error' : ''}
                  />
                </div>
                {errors.name && <div className="error-message">{errors.name}</div>}
              </div>
              
              <div className="form-group">
                <label htmlFor="price">Price</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon"><FaDollarSign /></i>
                  <input
                    type="number"
                    id="price"
                    name="price"
                    value={formData.price}
                    onChange={handleInputChange}
                    className={errors.price ? 'input-error' : ''}
                  />
                </div>
                {errors.price && <div className="error-message">{errors.price}</div>}
              </div>
            </div>
            
            {/* Image Upload Section */}
            <div className="form-section image-section">
              <h3>Vehicle Image</h3>
              <div className="image-upload-container">
                <div className="image-preview-container">
                  {imagePreview ? (
                    <img 
                      src={imagePreview} 
                      alt="Vehicle Preview" 
                      className="image-preview" 
                    />
                  ) : (
                    <div className="no-image">
                      <FaCar size={60} />
                      <p>No image selected</p>
                    </div>
                  )}
                </div>
                <div className="image-upload-info">
                  <h4>Upload a vehicle image</h4>
                  <p>Recommended size: less than 2MB</p>
                  <input
                    type="file"
                    ref={fileInputRef}
                    accept="image/*"
                    onChange={handleImageUpload}
                    style={{ display: 'none' }}
                  />
                  <button
                    type="button"
                    className="image-upload-btn"
                    onClick={triggerFileInput}
                  >
                    <FaImage /> Select Image
                  </button>
                </div>
              </div>
            </div>
          </div>
          
          <div className="form-actions">
            <button 
              type="button" 
              className="back-button"
              onClick={() => navigate('/vehicles')}
            >
              <FaArrowLeft /> Back
            </button>
            
            <button 
              type="submit" 
              className="save-button"
              disabled={loading}
            >
              <FaSave /> {loading ? 'Saving...' : 'Save'}
            </button>
            {showDialog && (
              <ConfirmDialog
                title="Edit Confirmation"
                message="Are you sure you want to edit this vehicle?"
                onConfirm={confirmEdit}
                onCancel={() => setShowDialog(false)}
              />
            )}
          </div>
        </form>
      </div>
    </MasterPage>
  );
}

export default VehicleDetailsPage;