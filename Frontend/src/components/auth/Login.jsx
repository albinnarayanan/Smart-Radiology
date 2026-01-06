import React from 'react'
import { RiLoginCircleLine } from "react-icons/ri";
import { useState } from 'react';
import InputField from '../shared/InputField';
import Spinner from '../shared/Spinner'; 
import { useForm } from 'react-hook-form';
import { useDispatch } from 'react-redux';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';
import { authenticateLoginUser } from '../../store/actions/index.js';
import InputButton from '../shared/InputButton.jsx';
const Login = () => {
  
  const [loader, setLoader] = useState(false);
  const dispatch = useDispatch()
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    reset,
    formState: {errors},
  }=useForm({
    mode: "onTouched",
  })

  const loginHandler = async(data)=>{
    dispatch(authenticateLoginUser(data, toast, reset, navigate, setLoader));
  }

  
  return (
    <div className='min-h-[calc(100vh-64px)] flex justify-center items-center'>
      <form onSubmit={handleSubmit(loginHandler)} className='w-[450px] shadow-custom py-8 px-4 rounded-md'>
        <div className='flex flex-col items-center justify-center space-y-4'>
          <RiLoginCircleLine className='text-slate-800 text-5xl'/>
            <h1 className='text-slate-800 text-center font-san text-3xl font-bold'>
              Login Here               
            </h1>
        </div>
        <hr className='mt-2 mb-5 text-black' />
        <div className='flex flex-col gap-3'>
          <InputField 
            label="UserName" required id="username" type="text" 
            register={register} errors={errors} message="UserName is required"
            placeholder="Enter your username" />

          <InputField 
            label="Password" required id="password" type="password"
            register={register} errors={errors} message="Password is required"
            placeholder="Enter your password"
            />
        </div>
        <button disabled={loader} type="submit" className='bg-button-gradient flex gap-2 items-center justify-center
          font-semibold text-white w-full py-2 hover:text-slate-300 transition-colors duration-100
          rounded-sm my-3'>
              {loader ? (<><Spinner /> Loading...</>):(<>Login</>)}
        </button>

      </form>
      
    </div>
  )
}

export default Login