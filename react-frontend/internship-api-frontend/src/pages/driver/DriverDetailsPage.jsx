import { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { FaArrowLeft, FaSave, FaTimes } from 'react-icons/fa';
import { driverService,userService } from '../../api';
import MasterPage from '../../components/layout/MasterPage';
import './DriverDetailsPage.css';
import ConfirmDialog from '../../utils/ConfirmDialog';
import toast from 'react-hot-toast';

const DriverDetailsPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const driver = location.state?.driver || null;
  const isAddMode = !driver;
  const [showDialog, setShowDialog] = useState(false);
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [passwordData, setPasswordData] = useState({
    password: '',
    passwordConfirm: ''
  });
  const [passwordErrors, setPasswordErrors] = useState({});
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    username: '',
    email: '',
    password: '',
    passwordConfirm: '',
    telephoneNumber: '',
    gender: 'Male',
    registrationDate: new Date().toISOString().substring(0, 10),
    numberOfClients: '0',
    numberOfHours: '0'
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // If editing existing driver, populate form
    if (driver) {
      setFormData({
        name: driver.user.name || '',
        surname: driver.user.surname || '',
        username: driver.user.username || '',
        email: driver.user.email || '',
        telephoneNumber: driver.user.telephoneNumber || '',
        gender: driver.user.gender || 'Male',
        registrationDate: driver.user.registrationDate?.substring(0, 10) || new Date().toISOString().substring(0, 10),
        numberOfClients: driver.numberOfClientsAmount?.toString() || '0',
        numberOfHours: driver.numberOfHoursAmount?.toString() || '0',
        password: '',
        passwordConfirm: ''
      });
    }
  }, [driver]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
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
    } else if (formData.name.length < 3) {
      newErrors.name = "Name must contain at least 3 characters";
    } 
    
    // Surname validation
    if (!formData.surname) {
      newErrors.surname = "Surname is required";
    } else if (formData.surname.length < 3) {
      newErrors.surname = "Surname must contain at least 3 characters";
    } 
    
    // Username validation
    if (!formData.username) {
      newErrors.username = "Username is required";
    } else if (formData.username.length < 5) {
      newErrors.username = "Username must contain at least 5 characters";
    }
    
    // Email validation
    if (!formData.email) {
      newErrors.email = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = "Please enter a valid email address";
    }
    
    // Password validation (only in add mode)
    if (isAddMode) {
      if (!formData.password) {
        newErrors.password = "Password is required";
      }
      
      if (!formData.passwordConfirm) {
        newErrors.passwordConfirm = "Password confirmation is required";
      } else if (formData.password !== formData.passwordConfirm) {
        newErrors.passwordConfirm = "Passwords do not match";
        newErrors.password = "Passwords do not match";
      }
    }
    
    // Phone validation (optional)
    if (formData.telephoneNumber && !/^06\d-\d{3}-\d{3,4}$/.test(formData.telephoneNumber)) {
      newErrors.telephoneNumber = "Number format: 06x-xxx-xxx(x)";
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const confirmEdit=async()=>{
    setShowDialog(false);
    try{
        var request={
            "name":formData.name,
            "surname":formData.surname,
            "username":formData.username,
            "email":formData.email,
            "telephoneNumber":formData.telephoneNumber,
        };
      await userService.update(driver.user.id, request);
      toast.success('Driver updated successfully');
      navigate('/drivers');
    }catch(error){
      toast.error('Error updating driver');
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
        // Create new driver
        var request = {
          "name": formData.name,
          "surname": formData.surname,
          "username": formData.username,
          "email": formData.email,
          "telephoneNumber": formData.telephoneNumber,
          "gender": formData.gender,
          "password": formData.password,
          "passwordConfirm": formData.passwordConfirm
        };
        const response = await driverService.create(request);
        console.log('Driver created:', response);
        toast.success('Driver created successfully');
        navigate('/drivers');
      } catch (error) {
        console.error('Error saving driver:', error);
        toast.error('Error saving driver');
      } finally {
        setLoading(false);
      }
    } else {
      // For edit mode, just show the dialog
      setShowDialog(true);
    }
  };

  const handlePasswordInputChange = (e) => {
    const { name, value } = e.target;
    setPasswordData({
      ...passwordData,
      [name]: value
    });
    
    // Clear error when field is modified
    if (passwordErrors[name]) {
      setPasswordErrors({
        ...passwordErrors,
        [name]: null
      });
    }
  };

  const validatePasswordForm = () => {
    const newErrors = {};
    
    if (!passwordData.password) {
      newErrors.password = "Password is required";
    }
    
    if (!passwordData.passwordConfirm) {
      newErrors.passwordConfirm = "Password confirmation is required";
    } else if (passwordData.password !== passwordData.passwordConfirm) {
      newErrors.passwordConfirm = "Passwords do not match";
      newErrors.password = "Passwords do not match";
    }
    
    setPasswordErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handlePasswordUpdate = async () => {
    if (!validatePasswordForm()) {
      return;
    }
    
    try{
      setShowPasswordModal(false);
      await userService.updatePassword(driver.user.id, passwordData);
      toast.success('Password updated successfully');
    }catch(error){
      toast.error('Error updating password');
    }
    
    // Reset password fields
    setPasswordData({
      password: '',
      passwordConfirm: ''
    });
  };

  const handleShowPasswordUpdateDialog = () => {
    setShowPasswordModal(true);
  };

  return (
    <MasterPage currentRoute={isAddMode ? "Add Driver" : "Edit Driver"}>
      <div className="form-container">
        <form onSubmit={handleSubmit}>
          <div className="form-section">
            <h3>Personal Information</h3>
            
            {/* Row 1: Name and Surname */}
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="name">Name</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">👤</i>
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
                <label htmlFor="surname">Surname</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">👤</i>
                  <input
                    type="text"
                    id="surname"
                    name="surname"
                    value={formData.surname}
                    onChange={handleInputChange}
                    className={errors.surname ? 'input-error' : ''}
                  />
                </div>
                {errors.surname && <div className="error-message">{errors.surname}</div>}
              </div>
            </div>
            
            {/* Row 2: Username and Email */}
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="username">Username</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">@</i>
                  <input
                    type="text"
                    id="username"
                    name="username"
                    value={formData.username}
                    onChange={handleInputChange}
                    className={errors.username ? 'input-error' : ''}
                  />
                </div>
                {errors.username && <div className="error-message">{errors.username}</div>}
              </div>
              
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">✉️</i>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    className={errors.email ? 'input-error' : ''}
                  />
                </div>
                {errors.email && <div className="error-message">{errors.email}</div>}
              </div>
            </div>
            
            {/* Row 3: Password fields (only in add mode) */}
            {isAddMode && (
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="password">Password</label>
                  <div className="input-icon-wrapper">
                    <i className="input-icon">🔒</i>
                    <input
                      type="password"
                      id="password"
                      name="password"
                      value={formData.password}
                      onChange={handleInputChange}
                      className={errors.password ? 'input-error' : ''}
                    />
                  </div>
                  {errors.password && <div className="error-message">{errors.password}</div>}
                </div>
                
                <div className="form-group">
                  <label htmlFor="passwordConfirm">Confirm Password</label>
                  <div className="input-icon-wrapper">
                    <i className="input-icon">🔒</i>
                    <input
                      type="password"
                      id="passwordConfirm"
                      name="passwordConfirm"
                      value={formData.passwordConfirm}
                      onChange={handleInputChange}
                      className={errors.passwordConfirm ? 'input-error' : ''}
                    />
                  </div>
                  {errors.passwordConfirm && <div className="error-message">{errors.passwordConfirm}</div>}
                </div>
              </div>
            )}
            
            {/* Row 4: Phone and Gender */}
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="telephoneNumber">Telephone Number</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">📞</i>
                  <input
                    type="text"
                    id="telephoneNumber"
                    name="telephoneNumber"
                    value={formData.telephoneNumber}
                    onChange={handleInputChange}
                    className={errors.telephoneNumber ? 'input-error' : ''}
                    placeholder="06x-xxx-xxxx"
                  />
                </div>
                {errors.telephoneNumber && <div className="error-message">{errors.telephoneNumber}</div>}
              </div>
              
              <div className="form-group">
                <label htmlFor="gender">Gender</label>
                <div className="input-icon-wrapper">
                  <select
                    id="gender"
                    name="gender"
                    value={formData.gender}
                    onChange={handleInputChange}
                  >
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                  </select>
                </div>
              </div>
            </div>
            
            {/* Additional fields for edit mode */}
            {!isAddMode && (
              <>
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="registrationDate">Registration Date</label>
                    <div className="input-icon-wrapper">
                      <i className="input-icon">📅</i>
                      <input
                        type="text"
                        id="registrationDate"
                        name="registrationDate"
                        value={formData.registrationDate}
                        disabled
                      />
                    </div>
                  </div>
                  
                  <div className="form-group">
                    <label htmlFor="numberOfClients">Number of Clients</label>
                    <div className="input-icon-wrapper">
                      <i className="input-icon">👥</i>
                      <input
                        type="text"
                        id="numberOfClients"
                        name="numberOfClients"
                        value={formData.numberOfClients}
                        disabled
                      />
                    </div>
                  </div>
                </div>
                
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="numberOfHours">Number of Hours</label>
                    <div className="input-icon-wrapper">
                      <i className="input-icon">⏱️</i>
                      <input
                        type="text"
                        id="numberOfHours"
                        name="numberOfHours"
                        value={formData.numberOfHours}
                        disabled
                      />
                    </div>
                  </div>
                  
                  <div className="form-group">
                    <button 
                      type="button" 
                      className="password-update-btn"
                      onClick={handleShowPasswordUpdateDialog}
                    >
                      🔄 Update Password
                    </button>
                  </div>
                </div>
              </>
            )}
          </div>
          
          <div className="form-actions">
            <button 
              type="button" 
              className="back-button"
              onClick={() => navigate('/drivers')}
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
          </div> 
        </form>
      </div>
      
      {/* Password Update Modal */}
      {showPasswordModal && (
        <div className="modal-overlay">
          <div className="password-modal">
            <div className="modal-header">
              <h3>Update Password</h3>
              <button 
                type="button" 
                className="close-button"
                onClick={() => setShowPasswordModal(false)}
              >
                <FaTimes />
              </button>
            </div>
            <div className="modal-body">
              <div className="form-group">
                <label htmlFor="modal-password">New Password</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">🔒</i>
                  <input
                    type="password"
                    id="modal-password"
                    name="password"
                    value={passwordData.password}
                    onChange={handlePasswordInputChange}
                    className={passwordErrors.password ? 'input-error' : ''}
                  />
                </div>
                {passwordErrors.password && <div className="error-message">{passwordErrors.password}</div>}
              </div>
              
              <div className="form-group">
                <label htmlFor="modal-passwordConfirm">Confirm New Password</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">🔒</i>
                  <input
                    type="password"
                    id="modal-passwordConfirm"
                    name="passwordConfirm"
                    value={passwordData.passwordConfirm}
                    onChange={handlePasswordInputChange}
                    className={passwordErrors.passwordConfirm ? 'input-error' : ''}
                  />
                </div>
                {passwordErrors.passwordConfirm && <div className="error-message">{passwordErrors.passwordConfirm}</div>}
              </div>
            </div>
            <div className="modal-footer">
              <button 
                type="button" 
                className="cancel-button"
                onClick={() => setShowPasswordModal(false)}
              >
                Cancel
              </button>
              <button 
                type="button" 
                className="save-button"
                onClick={handlePasswordUpdate}
              >
                <FaSave /> Save
              </button>
            </div>
          </div>
        </div>
      )}
      
      {showDialog && (
        <ConfirmDialog
          title="Edit Confirmation"
          message="Are you sure you want to edit this item?"
          onConfirm={confirmEdit}
          onCancel={() => setShowDialog(false)}
        />
      )}
    </MasterPage>
  );
};

export default DriverDetailsPage; 