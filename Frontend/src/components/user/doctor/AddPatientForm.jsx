import React, { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import ErrorPage from '../../shared/ErrorPage';
import InputField from '../../shared/InputField';
import Button from '@mui/material/Button';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Spinners from '../../shared/Spinner';
import { createNewPatient } from '../../../store/actions';

const AddPatientForm = ({setOpen}) => {
   

    const{
        register,
        handleSubmit,
        reset, 
        formState:{errors},
    }=useForm({
        mode:"onTouched"
    });

    

    const [ loader, setLoader ] = useState(false);
    const dispatch = useDispatch();
    const {user} = useSelector((state)=>state.auth);
    const {errorMessage} = useSelector((state)=>state.error);
    

    const savePatientHandler= (data)=>{
        
            const sendData = {
                ...data,
            }
            dispatch(createNewPatient(sendData, toast, reset, setLoader, setOpen));
        
    }
    if(errorMessage){
        <ErrorPage message={errorMessage} />
    }
  return (
    <div className='py-5 relative h-full'>
        <form onSubmit={handleSubmit(savePatientHandler)} className='space-y-4'>
            <div className='flex md:flex-row flex-col gap-4 w-full'>
                <InputField
                    label="Patient Name"
                    required
                    id="name"
                    type="text"
                    placeholder="Enter Patient Name"
                    message="Patient name is required"
                    register={register}
                    errors={errors}
                
                />

            </div>
            <div className='flex md:flex-row flex-col gap-4 w-full'>
                <InputField
                    label="Age"
                    required
                    id="age"
                    type="number"
                    placeholder="Enter Patient's age"
                    message="Patient's age is required"
                    register={register}
                    errors={errors}
                />
                <InputField
                    label="Gender"
                    required
                    id="gender"
                    type="text"
                    placeholder="Gender"
                    message="Patient gender is required"
                    register={register}
                    errors={errors}
                
                />

            </div>
            <div className='flex md:flex-row flex-col gap-4 w-full'>
                <InputField
                    label="Contact Number"
                    required
                    id="contactNumber"
                    type="text"
                    placeholder="Enter Contact Number"
                    message="Contact number is required"
                    register={register}
                    errors={errors}
                
                />

            </div>
            <div className='flex w-full justify-between items-center absolute bottom-14'>
                <Button disabled={loader} type='submit' variant='contained' color='primary' 
                 className='bg-custom-blue text-white py-2.5 px-4 text-sm font-medium'>
                    {loader ? (
                        <div className='flex gap-2 items-center' >
                            <Spinners /> Loading...
                        </div>
                    ):(
                        "Save"
                    )}


                </Button>

            </div>

        </form>
        
    </div>
  )
}

export default AddPatientForm