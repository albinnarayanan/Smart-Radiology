import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Navigate, Outlet, useLocation } from 'react-router-dom';

const PrivateRoute = ({ adminOnly =false, doctorOnly = false, technicianOnly = false}) => {
    const { user, isAuthenticated, isLoading } = useSelector(state => state.auth);
    const isAdmin = user && user?.entityType==="admin";
    const isDoctor = user && user?.entityType==="doctor";
    const isTechnician = user && user?.entityType==="technician";
    const location = useLocation();
    const dispatch = useDispatch();

    if(isLoading){
        return <div>Loading authentication...</div>;
    }

    useEffect(() => {
    // Only start if user is logged in
    if (!user) return;

    const REFRESH_INTERVAL_MS = 12 * 60 * 1000; 
    let failureCount = 0;
    const MAX_FAILURES = 3;

    const interval = setInterval(async() => {
      try {
      await api.post('/auth/refresh');
      failureCount = 0;
    } catch (err) {
      failureCount++;
      console.warn('Refresh failed', err);
      if (failureCount >= MAX_FAILURES) {
        dispatch({ type: 'LOGOUT_USER' });
      }
    }
    }, REFRESH_INTERVAL_MS);

    // Cleanup: stops when user logs out or component unmounts
    return () => clearInterval(interval);
  }, [user, dispatch]); // ← dependency: restart/clean when user changes (login/logout)
    
    if(!isAuthenticated){  
    return <Navigate to="/login" state={{ from: location }} replace />;
    }
    
    if(adminOnly && !isAdmin){
        return <Navigate to="/" />
    }

    if(doctorOnly && !isDoctor){
        return <Navigate to="/" />
    }

    if(technicianOnly && !isTechnician){
        return <Navigate to="/" />
    }

    return <Outlet />   



}

export default PrivateRoute