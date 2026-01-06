import { RiLoginBoxLine } from "react-icons/ri";
import { useSelector } from "react-redux";
import {Link} from "react-router-dom";
import { Box, FormControlLabel, Grow } from "@mui/material";
import { useState } from "react";
import Drop from "./Drop";

const Home = () =>{
    const { user } = useSelector((state)=>state.auth);
    const [ checked, setChecked ] = useState(true);

    return(
        <Box sx={{display:"flex"}}>
            <Grow in={checked} >
            
        <div className="flex items-center justify-center h-screen w-full bg-white">
            <video
                className="w-200 object-contain"
                src="src/assets/smartRadiologyGif.mp4"
                autoPlay
                loop
                muted
            />
            <Grow
          in={checked}
          style={{ transformOrigin: '0 0 0' }}
          {...(checked ? { timeout: 2000 } : {})}
        >
        
            <div className="flex flex-col justify-center">
                <h1 className="text-6xl font-extrabold text-gray-900 ">Welcome to Smart Radiology Workflow Manager</h1>
                <Drop/>
                {!user ?
                    <div>
                        <Link className="pt-10 text-2xl font-bold flex gap-2 text-blue-800" to="/login"><RiLoginBoxLine size={30}/>Login</Link>
                    </div>
                     :null}
                
                
            </div>
            </Grow>
            
            
        </div>
        </Grow>

        </Box>
    );

}
export default Home;