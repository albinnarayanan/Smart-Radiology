import './App.css'
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import Home from './components/home/home';
import React, { useEffect, useState } from 'react';
import Navbar from './components/shared/Navbar';
import Login from './components/auth/login';
import Doctor from './components/user/doctor/Doctor';
import Technician from './components/user/technician/Technician';
import Admin from './components/user/admin/Admin';
import { Toaster } from 'react-hot-toast';
import PrivateRoute from './components/PrivateRoute';
import { initializeAuth } from './store/actions';
import { useDispatch, useSelector } from 'react-redux';



function App() {
  const dispatch = useDispatch();
  const { isLoading } = useSelector(state => state.auth);

  const [initialCheckDone, setInitialCheckDone] = useState(false);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        await dispatch(initializeAuth());     // ← await here
        // Optional: add small delay if needed in very slow networks
        // await new Promise(r => setTimeout(r, 300));
      } catch (err) {
        console.error("Initial auth check failed", err);
      } finally {
        setInitialCheckDone(true);           // ← only now hide loading
      }
    };

    checkAuth();
  }, [dispatch]);

  if (!initialCheckDone) {
    return (
      <div style={{
        height: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '1.5rem',
        backgroundColor: '#f8f9fa'
      }}>
        Authenticating... Please wait
      </div>
    );
  }
  
  return (
    <React.Fragment>
      <Router>
        <Navbar/>
        <Routes>
          <Route path='/' element={<Home/>} />
          <Route path='/login' element={<Login/>} />
          <Route path='/' element={<PrivateRoute doctorOnly />}>
            <Route path='/doctor/dashboard' element={<Doctor/>} />
          </Route>
          <Route path='/' element={<PrivateRoute technicianOnly />}>
            <Route path='/technician/dashboard' element={<Technician/>} />
          </Route>
          <Route path='/' element={<PrivateRoute adminOnly />}>
            <Route path='/admin/dashboard' element={<Admin/>} />
          </Route>
        </Routes>

      </Router>
      <Toaster position='top-center'/>
    </React.Fragment>
  )
}

export default App
