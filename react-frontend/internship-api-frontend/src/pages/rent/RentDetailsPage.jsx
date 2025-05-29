import { useLocation } from "react-router-dom";
import MasterPage from "../../components/layout/MasterPage";

const RentDetailsPage=()=>{
    const location=useLocation();
    const rent=location.state?.rent || null;
    const isAddMode=!rent;
    return(
        <MasterPage currentRoute={isAddMode?"Add Rent":"Edit Rent"}>

        </MasterPage>
    );
}
export default RentDetailsPage;