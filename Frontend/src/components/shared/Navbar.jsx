import { FaHospitalUser } from "react-icons/fa";
import {useSelector} from "react-redux";
import UserMenu from "../user/UserMenu";
import { Link } from "react-router-dom";
import { FaSignInAlt } from "react-icons/fa";
import Home from "../home/Home";

const Navbar = () =>{
    const { user } = useSelector((state)=>state.auth);
    return( 
    <div className="w-full h-[70px] bg-gray-600 text-white z-50 flex items-center sticky top-0">
        <div className="px-8 w-full flex justify-between">
            
            <h1 className="font-bold text-lg gap-2 flex items-center">
                <Link to='/' className="flex items-center gap-3">
                   
                        <FaHospitalUser size={30} color="white"/>
                        SMART RADIOLOGY WORKFLOW MANAGER
                    
                </Link>
            </h1>
            {(user && user.id)? (
                <div className="font-medium transition-all duration-150">
                    <UserMenu />
                </div>

            ):(
                <div className="font-medium transition-all duration-150">
                    <Link className="flex items-center space-x-2 px-4 py-1.5 bg-linear-to-r
                        from-purple-600 to-red-500 text-white font-semibold
                        rounded-md shadow-lg hover:from-purple-500 hover:to-red-400
                        transition duration-300 ease-in-out transform" to="/login">
                           <FaSignInAlt />
                           <span>Login</span> 
                    </Link>
                </div>
                
            )}

            
        </div>
    </div>
    );
}
export default Navbar;