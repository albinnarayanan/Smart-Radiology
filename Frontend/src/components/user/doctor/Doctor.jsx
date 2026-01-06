import React, { useState } from 'react'
import { useSelector } from 'react-redux';
import { IoMdPersonAdd } from "react-icons/io";
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import { IoIosArrowDown } from "react-icons/io";
import { MdOutlineAddCircleOutline } from "react-icons/md";
import Modal from '../../shared/Modal';
import AddPatientForm from './AddPatientForm';
import Loader from '../../shared/Loader';
import { TbDeviceIpadCancel } from "react-icons/tb";
import { DataGrid } from '@mui/x-data-grid';
import { radiologyReqDetailColumnForDoctor } from '../../helper/TableColumn';
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
import AddRadReqForm from './AddRadReqForm';
import useRadiologyDocReqs from '../../hooks/useRadiologyDocReqs';
import ViewReport from './ViewReport';
import ViewImage from './ViewImage';
import useGetAllPatients from '../../hooks/useGetAllPatients';
import Skeleton from '@mui/material/Skeleton';




const Doctor = () => {
  const { radiology_requests, pagination } = useSelector((state=>state.radiologyRequests));
  const emptyRadReqs = !radiology_requests || radiology_requests.length === 0;
  const [openReport, setOpenReport] = useState(false); 

  const [ searchParams ] = useSearchParams();
  const params = new URLSearchParams(searchParams);
  const pathName = useLocation().pathname;
  const navigate = useNavigate();
   
  useRadiologyDocReqs();
  useGetAllPatients();
  


  const { isLoading, errorMessage } = useSelector((state)=>state.error)

  const tableRecords = radiology_requests?.map((req)=>{
    return{
      id: req.id,
      patientId: req.patient?.patientId,
      patientName: req.patient?.name,
      imagingType: req.imagingType,
      priority : req.priority?? "No",
      reportAvailable: !!req.report,
      fullReq: req
      
    }
  });
  const [currentPage, setCurrentPage] = useState(
    pagination?.pageNumber+1 || 1
  );

  const [ openAddPatientModal, setOpenAddPatientModal ] = useState(false);
  const [ openRequestTestModal, setOpenRequestTestModal ] = useState(false);
  const { user } = useSelector((state)=>state.auth);
  const [selectedRadReq, setSelectedRadReq] = useState('');
  const [openImage, setOpenImage] = useState(false);


  const handleViewReport = (radReq) =>{
    
    setSelectedRadReq(radReq);
    setOpenReport(true);
    
  }
  const handleViewImage = (radReq) =>{
    setOpenImage(true);
    setSelectedRadReq(radReq);

  }

  const handlePaginationChange = (paginationModel) =>{
    const page = paginationModel.page+1;
    setCurrentPage(page);
    params.set("page", page.toString());
    navigate(`${pathName}?${params}`)


  }

  return (
    <div>
      <div className='flex flex-col items-center justify-center pt-10 bg-slate-300 rounded-md shadow-md 
      pb-2 w-1/3 mx-auto' >
        <h1 className='font-bold text-5xl '>{`Dr. ${user?.name}`}</h1>
        <h1 className='font-semibold text-2xl pt-3'>{`ID : ${user?.id}`}</h1>
      </div>
      <div className=' pr-5 pt-4 pb-10 flex justify-end'>
        <div className="fixed top-24 w-52 text-right">
      <Menu>
        <MenuButton className="inline-flex items-center gap-2 rounded-md bg-gray-800 px-3 py-1.5 
        text-sm/6 font-semibold text-white shadow-inner shadow-white/10 focus:not-data-focus:outline-none 
        data-focus:outline data-focus:outline-white data-hover:bg-gray-700 data-open:bg-gray-700">
          Create
          <IoIosArrowDown className="size-4 fill-white/60" />
        </MenuButton>

        <MenuItems
          transition
          anchor="bottom end"
          className="absolute right-0 mt-2 w-60 origin-top-right rounded-md bg-gray-300 shadow-lg 
          focus:outline-none"
        >
          <MenuItem>
            <button className="group flex w-full items-center gap-2 rounded-lg px-3 py-1.5
             data-focus:bg-gray-400"
              onClick={()=>setOpenAddPatientModal(true)}>
              <IoMdPersonAdd className='text-xl'/>
              Add Patient
            </button>
          </MenuItem>
          <MenuItem>
            <button className="group flex w-full items-center gap-2 rounded-lg px-3 py-1.5 
            data-focus:bg-gray-400"
              onClick={()=>setOpenRequestTestModal(true)}>
              <MdOutlineAddCircleOutline className='text-xl'/>
              Request Radiology Test
            </button>
          </MenuItem>
        </MenuItems>
      </Menu>
      </div>
      </div>
      {!emptyRadReqs && (
        <h1 className='text-slate-800 text-3xl text-center font-bold pb-6 uppercase'>
          Radiology Requests
        </h1>
      )}
      {isLoading ? (
         <div className="py-4 flex flex-col items-center justify-center">
                        
                        <Skeleton variant="rectangular" width={1400} height={100} />
                        <Skeleton variant="rectangular" width={1400} height={100} />
                        <Skeleton variant="rectangular" width={1400} height={100} />

          </div>
      ):(
        <>
          {emptyRadReqs ? (
            <div className='flex flex-col items-center justify-center text-gray-600 py-10'>
                <TbDeviceIpadCancel size={50} className='mb-3' />
                <h2 className='text-2xl font-semibold'>
                  No Requests Yet!
                </h2>
            </div>
          ):(
            <div className='flex flex-col items-center justify-center overflow-x-hidden  '>
              <DataGrid
                className='w-7xl'
                rows={tableRecords}
                autoGenerateColumns={false}
                columns={radiologyReqDetailColumnForDoctor((row) => handleViewReport(row.fullReq),(row)=>handleViewImage(row.fullReq))}
                sx={{
                    '& .MuiDataGrid-cell': {
                      border: '1px solid #d1d5db',
                    },
                    '& .MuiDataGrid-columnHeaders': {
                      borderBottom: '2px solid #000',
                    },
                    '& .MuiDataGrid-columnHeader': {
                      borderRight: '1px solid #d1d5db',
                    },
                  }}
                paginationMode='server'
                rowCount={pagination?.totalElements || 0 }
                initialState={{
                  pagination:{
                    paginationModel:{
                      pageSize: pagination?.pageSize || 10,
                      page: currentPage-1,
                    },
                  },
                }}
                onPaginationModelChange= {handlePaginationChange}
                disableColumnResize
                pageSizeOptions={[pagination?.pageSize || 10]}
                pagination
                paginationOptions={{
                  showFirstButton: true,
                  showLastButton: true,
                  hideNextButton: currentPage === pagination?.totalPages,
                }}
                disableRowSelectionOnClick
              
              />

            </div>
          )}
        </>
      )}

      

      <Modal open={openAddPatientModal} setOpen={setOpenAddPatientModal} title={"Add New Patient"}>
        <AddPatientForm setOpen={setOpenAddPatientModal}/>
      </Modal>
      <Modal open={openRequestTestModal} setOpen={setOpenRequestTestModal} title={"Add Radiology Reqeust"}>
        <AddRadReqForm setOpen={setOpenRequestTestModal} radReq ={selectedRadReq}/>

      </Modal>

      <ViewReport open={openReport} setOpen={setOpenReport} radReq ={selectedRadReq}/>
      <ViewImage open={openImage} setOpen={setOpenImage} radReq={selectedRadReq} />

    </div>

  )
}

export default Doctor