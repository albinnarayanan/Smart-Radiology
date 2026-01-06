import React, { useEffect, useState } from 'react'
import Dialog from '@mui/material/Dialog'
import DialogTitle from '@mui/material/DialogTitle'
import { IoCloseSharp } from "react-icons/io5";
import { useForm } from 'react-hook-form';
import InputField from '../../shared/InputField';
import Button from '@mui/material/Button';
import Spinners from '../../shared/Spinner';
import { useDispatch } from 'react-redux';
import toast from 'react-hot-toast';
import { submitPatientReport } from '../../../store/actions';

const Report = ({open, setOpen, selectedRow }) => {
    const {
        register,
        handleSubmit,
        reset,
        setValue,
        formState: {errors},
    } = useForm({
        mode: "onTouched",
    })
     const [loader, setLoader] = useState(false);

     const dispatch = useDispatch();

     useEffect(()=>{
        if(selectedRow){
            setValue("imagingScheduleId",selectedRow?.id);
        }
     },[selectedRow, setValue])

    const handleClose = () =>{
        setOpen(false)
    }

    const handleReportSubmission = (data) =>{
        dispatch(submitPatientReport(toast,data,reset,setLoader,setOpen));
        



    }
    const handleCancelClick = () =>{
        setOpen(false);
    }

  return (
    <div >
        <Dialog onClose={handleClose} open={open} PaperProps={{
    sx: {
      width: "50%",
      maxWidth: "none",
      height: "50vh",       // 80% of viewport height
      maxHeight: "none"     // disable default maxHeight
    }
  }}>
            <DialogTitle className='relative text-center'>
                <div className='text-2xl font-semibold'>
                    Detailed Report
                </div>
                <IoCloseSharp size={20} onClick={handleCancelClick} className='absolute right-3 top-3 cursor-pointer text-slate-600 hover:text-red-600' />
            </DialogTitle>
            <form onSubmit={handleSubmit(handleReportSubmission)} className='space-y-4'>
        <div>
            <input type="hidden" {...register("imagingScheduleId")} />
            <InputField 
                required
                id="findings"
                type="textarea"
                placeholder="Type in the report"
                register= {register}
                errors={errors}
                className="w-2/3 mx-auto"
            />


        </div>
        <div className='pl-10' >
            <label className="block font-medium text-lg text-gray-700 mb-1">
                Upload Report Image
            </label>
                <input type="file" accept="image/*" {...register("imageFile", { required: true })} 
                className="block w-2/3 text-sm text-gray-900 border border-gray-300 rounded-lg
                    shadow-md hover:shadow-gray-400 cursor-pointer focus:outline-none " />
                {errors.imageFile && (
                    <span className="text-red-500 text-xs">Image is required</span>
                )}
        </div>
        <div className='flex w-full justify-center items-center '>
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

      </Dialog>
      
    </div>
  )
}

export default Report