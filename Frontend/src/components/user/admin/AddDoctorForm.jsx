import Dialog from '@mui/material/Dialog'
import { useForm } from 'react-hook-form';
import InputField from '../../shared/InputField';
import CloseButton from '../../shared/CloseButton';
import Spinners from '../../shared/Spinner';
import Button from '@mui/material/Button';
import { useState } from 'react';
import { createNewDoctor } from '../../../store/actions';
import toast from 'react-hot-toast';
import { useDispatch } from 'react-redux';


const AddDoctorForm = ({open, setOpen}) => {
    const dispatch = useDispatch();
    
    const [ loader, setLoader ] = useState(false);
    const{
        register,
        handleSubmit,
        reset,
        formState:{errors},
    }=useForm({
        mode: 'onTouched'
    })


    const handleClose = () =>{
        setOpen(false);
    }
    const handleCloseDoctorForm = () =>{
        setOpen(false);
    }

    const saveDoctorHandler = (data) =>{
        const sendData = {
                ...data,
            }
            
            dispatch(createNewDoctor(sendData, toast, reset, setLoader, setOpen));
            

    }
  return (
    <div>
        <Dialog onClose={handleClose} open={open}
        PaperProps={{
        sx: {
          width: "60%",
          maxWidth: "none",
          height: "85vh",
          maxHeight: "none",
          padding: "20px"
        }
      }}>
        <form onSubmit={handleSubmit(saveDoctorHandler)} className='space-y-4'>
            <div className='flex items-center justify-center gap-4 '>
                <h2 className='flex w-full text-xl items-center justify-center font-semibold border'>
                    Fill the Doctor details below
                </h2>
                <CloseButton onClick={handleCloseDoctorForm} />
            </div>

            <div className='flex md:flex-row flex-col gap-4 w-2/4 pt-10'>
                <InputField
                    label="Name"
                    required
                    id="name"
                    type="text"
                    placeholder="Enter Doctor's name"
                    message="Doctors name is required"
                    register={register}
                    errors={errors}
                />
            </div>
             <div className='flex md:flex-row flex-col gap-4 w-2/4 pt-5'>
                <InputField
                    label="Specialization"
                    required
                    id="specialization"
                    type="text"
                    placeholder="Enter specialization"
                    message="Specialization is required"
                    register={register}
                    errors={errors}
                />
            </div>
             <div className='flex md:flex-row flex-col gap-4 w-2/4 pt-5'>
                <InputField
                    label="Email"
                    required
                    id="email"
                    type="email"
                    placeholder="Enter email"
                    message="Email is required"
                    register={register}
                    errors={errors}
                />
            </div>
            <div className='flex md:flex-row flex-col gap-4 w-2/4 pt-5'>
                <InputField
                    label="Username"
                    required
                    id="username"
                    type="text"
                    placeholder="Set username"
                    message="Username is required"
                    register={register}
                    errors={errors}
                />
            </div>
            <div className='flex md:flex-row flex-col gap-4 w-2/4 pt-5'>
                <InputField
                    label="Password"
                    required
                    id="password"
                    type="password"
                    placeholder="Set password"
                    message="Password is required"
                    register={register}
                    errors={errors}
                />
            </div>
            <div className='flex w-full justify-between items-center  bottom-14'>
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
        </Dialog>
    </div>
  )
}

export default AddDoctorForm