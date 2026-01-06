import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import ErrorPage from '../../shared/ErrorPage';
import InputField from '../../shared/InputField';
import Button from '@mui/material/Button';
import Spinners from '../../shared/Spinner';
import toast from 'react-hot-toast';
import { createRadiologyRequest } from '../../../store/actions';
import { useForm } from 'react-hook-form';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';


const AddRadReqForm = ({setOpen, radReq, update=false}) => {

    const { patients } = useSelector((state)=>state.patients);
    const [selectedPatient, setSelectedPatient] = useState(null);
    const [patientOptions, setPatientOptions] = useState([]);


    useEffect(() => {
         if (patients && Array.isArray(patients)) { 
            const formatted = patients.map(p => ({ value: p.id, label: `${p.name} - ${p.contactNumber}` })); 
            setPatientOptions(formatted); } }, [patients]);

    const {
        register,
        handleSubmit, 
        setValue,
        reset,
        formState: {errors},
    }=useForm({
        mode:"onTouched"
    });

    useEffect(()=>{
        if(update && radReq){
            setValue("patientName",radReq?.patient?.name);
            setValue("imagingType",radReq?.imagingType);
            setValue("priority",radReq?.priority);
        }
    },[radReq,update])

    const [loader, setLoader] = useState(false);
    const dispatch = useDispatch();

    const {errorMessage} = useSelector((state)=>state.error);

    if(errorMessage){
        return <ErrorPage message={errorMessage} />
    }

    const saveRadReqHandler = (data) =>{
        if (!selectedPatient || !data.imagingType) {
             toast.error("Please fill all fields"); 
             return; 
        } 
        dispatch( createRadiologyRequest( { patientId: selectedPatient.value, imagingType: data.imagingType }, 
            toast, reset, setLoader, setOpen ) ); };
  return (

    <div className='py-5 relative h-full'>
        <form onSubmit={handleSubmit(saveRadReqHandler)} className='space-y-4'>
            <div className='flex  flex-col gap-4 w-full'>
                <Autocomplete options={patientOptions} getOptionLabel={(option) => option.label}
                    onChange={(e, value) => setSelectedPatient(value)}
                    renderInput={(params) => (
                    <TextField {...params} label="Select Patient" variant="outlined" />
                    )}
                    isOptionEqualToValue={(option, value) => option.value === value.value}
                />
                <InputField 
                    label="Imaging Type"
                    required
                    id="imagingType"
                    type="text"
                    placeholder="Enter the imaging type"
                    message = "This field is required"
                    register={register}
                    errors={errors}
                
                />
            </div>
            <div className='flex w-full justify-between items-center absolute bottom-14'>
                <Button disabled={loader} onClick={()=>setOpen(false)} variant='outlined' 
                className='text-white py-2.5 px-4 text-sm font-medium'>
                    Cancel
                </Button>
                <Button disabled={loader} type='submit' variant='contained' color='primary'
                    className='bg-custom-blue text-white py-2.5 px-4 text-sm font-medium'>
                        {loader ? (
                            <div className='flex gap-2 items-center'>
                                <Spinners />Loading...
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

export default AddRadReqForm