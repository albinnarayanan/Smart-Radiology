import { authReducer } from "./authReducer";
import { errorReducer } from "./errorReducer";
import { configureStore } from "@reduxjs/toolkit";
import { patientReducer } from "./patientReducer";
import { radiologyReducer } from "./radiologyReducer";
import { techScheduleReducer } from "./techScheduleReducer";
import { technicianReducer } from "./technicianReducer";
import { doctorReducer } from "./doctorReducer";


const user = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    :null;

const initialState = {
    auth:{
        user:user,
    }
}

const store = configureStore({
    reducer:{
        auth:authReducer,
        error:errorReducer,
        patients:patientReducer,
        radiologyRequests: radiologyReducer,
        techScheduleReducer: techScheduleReducer,
        technicians: technicianReducer,
        doctors: doctorReducer
    },
    preloadedState: initialState
})
export default store;