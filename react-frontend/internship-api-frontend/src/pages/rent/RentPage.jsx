import { useNavigate } from "react-router-dom";
import MasterPage from "../../components/layout/MasterPage";
import { useState } from "react";
import { FaSearch, FaSearchPlus } from "react-icons/fa";

const RentPage = () => {
    const navigate=useNavigate();
    const [statusFilter,setStatusFilter]=useState("");

    const handleAddRent=()=>{
        navigate("/rent/add");
    }

    const handleFilter=()=>{
        console.log("Filtering with status:",statusFilter);
    }

    const handleStatusFilter=(e)=>{
        setStatusFilter(e.target.value);
    }

  return (
    <MasterPage currentRoute="Rent">
      <div className="routes-section">
        <div className="section-header">
          <h3>Rent Management</h3>
          <button className="add-button" onClick={handleAddRent}>
            Add New Rent
          </button>
        </div>
         {/*Filter controls*/}
         <div className="filter-container">
          <div className="filter-row">
            <div className="filter-group">
              <div className="form-group">
                <label htmlFor="statusFilter">Status</label>
                <div className="input-icon-wrapper">
                  <i className="input-icon">
                    <FaSearchPlus />
                  </i>
                  <select
                    id="statusFilter"
                    name="stautsFilter"
                    style={{ fontWeight: "bold" }}
                    value={statusFilter}
                    onChange={handleStatusFilter}
                  >
                    <option value="" style={{ fontWeight: "bold" }}>
                      All statuses
                    </option>
                    <option value="wait" style={{ fontWeight: "bold" }}>
                      Wait
                    </option>
                    <option value="active" style={{ fontWeight: "bold" }}>
                      Active
                    </option>
                    <option value="finished" style={{ fontWeight: "bold" }}>
                      Finished
                    </option>
                  </select>
                </div>
              </div>
            </div>

            <button className="filter-button" onClick={handleFilter}>
              <FaSearch className="filter-icon" /> Filter
            </button>
          </div>
        </div>
      </div>
    </MasterPage>
  );
};
export default RentPage;
