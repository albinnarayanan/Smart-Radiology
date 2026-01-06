import api from "../../api/api";


export const authenticateLoginUser = (sendData, toast, reset, navigate, setLoader) => async (dispatch)=>{
    try {
        setLoader(true);
        const { data } = await api.post("/auth/login",sendData);
        dispatch({
            type: "LOGIN_USER",
            payload: data,
        });
        reset();
        toast.success("Login Successful");
        localStorage.setItem("user",JSON.stringify(data));
        const entityType = data?.entityType?.toLowerCase();
        console.log("User entity type:", entityType);
        if (entityType === "doctor") {
            navigate("/doctor/dashboard");
        } else if (entityType === "technician") {
            navigate("/technician/dashboard");
        } else if (entityType === "admin") {
            navigate("/admin/dashboard");
        } else {
            toast.error("Unknown user role");
            navigate("/login");
        }
        
        
    } catch (error) {
        console.log(error);
        toast.error( "Login failed. Please check username & password.");
        
    }
    finally{
        setLoader(false);
    }

}

export const logoutUser = (navigate,toast) => async(dispatch) =>{
    try {
        
        await api.post("/auth/logout");
        toast.success("Logged out successfully");
        
    } catch (error) {
        console.error("Logout error");
        toast.error("Logout failed, but session cleared locally");

        // Even if backend fails, we clear everything locally for security
    } finally {
        dispatch({ type: "LOGOUT_USER" });
        localStorage.clear(); 
        navigate("/login");
        
    }
    
   
}

export const initializeAuth = () => async (dispatch) => {
  try {
    // Try to refresh token (cookies are sent automatically)
    await api.post('/auth/refresh');
    
    // If refresh succeeds, fetch current user info
    // (You can create a /auth/me endpoint that returns user info based on access token)
    const { data } = await api.get('/auth/me');  // ← recommended new endpoint
    
    dispatch({
      type: 'LOGIN_USER',
      payload: data,
    });
    
    console.log('Auth restored after refresh');
  } catch (error) {
    console.warn('Auth initialization failed → logging out');
    dispatch({ type: 'LOGOUT_USER' });
    throw error;
    
  }
};

export const createNewPatient = (sendData, toast, reset, setLoader, setOpen) =>async (dispatch)=>{
    try {
        setLoader(true);
        const { data } = await api.post("/doctor/createPatient",sendData);
        toast.success("Patient created successfully");
        reset();
        setOpen(false);

        
    } catch (error) {
        toast.error(error?.response?.data?.message || "Internal Server Error");
    }
    finally{
        setLoader(false);
    }
}

export const createRadiologyRequest = (sendData, toast, reset, setLoader, setOpen) => async (dispatch)=>{
    try {
        setLoader(true);
        const {patientId, imagingType} = sendData;

        await api.post(`/doctor/createRadiologyRequest/${patientId}/imagingType/${imagingType}`,sendData);
        toast.success("Radiology request successfully created !");
        reset();
        setOpen(false);
    } catch (error) {
        console.log(error);
        toast.error(error?.response?.data?.message || "Internal Server Error");
    }
    finally{
        setLoader(false);
    }
}
export const getAllRadiologyReqsForDoc = (queryString) => async(dispatch)=>{
    try {
        dispatch({
            type:"IS_FETCHING"
        });
        const { data } = await api.get(`/doctor/getAllRadiologyReq?${queryString}`);
        dispatch({
            type:"FETCH_RADIOLOGY_REQUESTS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalPages: data.totalPages,
            totalElements: data.totalElements,
            lastPage: data.lastPage

        });
        
        localStorage.setItem("docsRadReqs",JSON.stringify(data.content));
        console.log("Saved to localStorage:", data.content);
        dispatch({
            type: "IS_SUCCESS"
        });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch"
        });
        
    }

}

export const getAllRadiologyReqsForTech = (queryString) => async(dispatch)=> {
    try {
        dispatch({
            type: "IS_FETCHING",
        });
        const { data } = await api.get(`/technician/getAllRequests?${queryString}`);
        dispatch({
            type: "GET_ALL_MRI_REQS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalPages: data.totalPages,
            totalElements: data.totalElements,
            lastPage: data.lastPage
        });
        localStorage.setItem("techRadSchedules", JSON.stringify(data.content));
        dispatch({
            type:"IS_SUCCESS",
        })
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch"
        });
        
    }
}

export const submitPatientReport = (toast,sendData,reset,setLoader,setOpen) => async(dispatch) =>{
    try {
        setLoader(true);
        const { findings, imagingScheduleId, imageFile } = sendData;
        const formData = new FormData(); 
        const reportDTO = { findings }; 
        formData.append( "report", new Blob([JSON.stringify(reportDTO)], { type: "application/json" }) ); 
        formData.append("image", imageFile[0]);
        
        await api.post(`technician/report/${imagingScheduleId}/image`,formData);
        toast.success("Report submitted successfully");
        reset();
        setOpen(false);
        
    } catch (error) {
        console.log(error);
        toast.error(error?.response?.data?.message || "Internal Server Error")
    }
    finally{
        setLoader(false);
    }


}

export const getAllDoctors = (queryString) => async dispatch =>{
    try{
        dispatch({
            type: "IS_FETCHING"
        })
        const { data } = await api.get(`/admin/getDoctors?${queryString}`);
        dispatch({
            type: "GET_ALL_DOCTORS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalPages: data.totalPages,
            totalElements: data.totalElements,
            lastPage: data.lastPage
        })
        dispatch({
            type: "IS_SUCCESS"
        })

    }
    catch(error){
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch"
        });
        
    }

}
export const getAllTechnicians = (queryString) => async dispatch =>{
    try {
        dispatch({
        type: "IS_FETCHING"
    })
    const { data } = await api.get(`/admin/getTechnicians?${queryString}`);
    dispatch({
        type: "GET_ALL_TECHNICIANS",
        payload: data.content,
        pageNumber: data.pageNumber,
        pageSize: data.pageSize,
        totalPages: data.totalPages,
        totalElements: data.totalElements,
        lastPage: data.lastPage

    })
    dispatch({
        type: "IS_SUCCESS"
    });
        
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch"
        });
        
    }
    
}
export const createNewDoctor = (sendData, toast, reset, setLoader, setOpen) =>async (dispatch)=>{
    try {
        setLoader(true);
        await api.post("/admin/createDoctor",sendData);
        toast.success("Doctor created successfully");
        reset();
        setOpen(false);

        
    } catch (error) {
        toast.error(error?.response?.data?.message || "Internal Server Error");
    }
    finally{
        setLoader(false);
    }

}
export const createNewTechnician = (sendData, toast, reset, setLoader, setOpen) =>async (dispatch)=>{
    try {
        setLoader(true);
        await api.post("/admin/createTechnician",sendData);
        toast.success("Technician created successfully");
        reset();
        setOpen(false);

        
    } catch (error) {
        toast.error(error?.response?.data?.message || "Internal Server Error");
    }
    finally{
        setLoader(false);
    }

}

export const getAllPatients = (queryString) => async dispatch =>{
    try {
        dispatch({
        type: "IS_FETCHING"
    })
    const { data } = await api.get(`/doctor/getAllPatients?${queryString}`);
    dispatch({
        type: "GET_ALL_PATIENTS",
        payload: data.content,
        pageNumber: data.pageNumber,
        pageSize: data.pageSize,
        totalPages: data.totalPages,
        totalElements: data.totalElements,
        lastPage: data.lastPage

    })

    dispatch({
        type: "IS_SUCCESS"
    });
        
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch"
        });
        
    }
    
}