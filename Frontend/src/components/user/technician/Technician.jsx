import { DataGrid } from '@mui/x-data-grid';
import React, { useState } from 'react'
import { TbDeviceIpadCancel } from 'react-icons/tb';
import { useSelector } from 'react-redux';
import { radiologyReqDetailColumnForTechnician } from '../../helper/TableColumn';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';


import Report from './Report';
import useTechSchedules from '../../hooks/useTechSchedules';
import Skeleton from '@mui/material/Skeleton';

const Technician = () => {
    const { user } = useSelector((state)=>state.auth);
    const { isLoading, errorMessage } = useSelector((state)=> state.error);
    const { imageSchedules, pagination } = useSelector((state)=> state.techScheduleReducer);
    const emptyRadReqs = !imageSchedules ||imageSchedules.length === 0;

    useTechSchedules();

    const tableRecords = imageSchedules?.map((req)=>{
      return {
        id: req.id,
        patientId: req?.radiologyRequest?.patient?.patientId,
        patientName: req?.radiologyRequest?.patient?.name,
        patientAge: req?.radiologyRequest?.patient?.age,
        priority: req?.radiologyRequest?.priority?? "No"
      }
    })

    const [currentPage, setCurrentPage] = useState(pagination?.pageNumber+1 || 1);

    const [ searchParams ] = useSearchParams();
    const params = new URLSearchParams(searchParams);
    const pathName = useLocation().pathname;
    const navigate = useNavigate() ;

    const handlePaginationChange = (paginationModel) =>{
      const page = paginationModel.page+1;
      setCurrentPage(page);
      params.set("page",page.toString());
      navigate(`${pathName}?${params}`);
    }


    const [selectedRow, setSelectedRow] = useState(null);
    const [ openReport, setOpenReport] = useState(false);
    const handleReport = (row) =>{
      setSelectedRow(row);
      setOpenReport(true);

    }

  return (
    <React.Fragment>
    <div className='flex flex-col items-center justify-center pt-10 bg-slate-300 rounded-md shadow-md pb-2 w-1/3 mx-auto' >
      <h1 className='font-bold text-5xl'>{`Tech. ${user?.name}`}</h1>
      <h1 className='font-semibold text-2xl pt-3'>{`ID : ${user?.id}`}</h1>
    </div>
    <div>
      {!emptyRadReqs && (
              <h1 className='text-slate-800 text-3xl text-center font-bold pb-6 uppercase pt-5'>
                Imaging Schedules
              </h1>
            )}
            {isLoading ? (
              <div className="py-4 flex flex-col items-center justify-center">
                        
                        <Skeleton variant="rectangular" width={1150} height={100} />
                        <Skeleton variant="rectangular" width={1150} height={100} />
                        <Skeleton variant="rectangular" width={1150} height={100} />

          </div>
            ):(
              <>
        {emptyRadReqs ? (
          <div className='flex flex-col items-center justify-center text-gray-600 py-10'>
            <TbDeviceIpadCancel size={50} className='mb-3' />
            <h2 className='font-semibold text-2xl'>
            No Requests Yet !
            </h2>
          </div>
        ):(
          <div className='flex flex-col items-center justify-center'>
            <DataGrid 
              className='w-6xl'
              rows={tableRecords}
              columns={radiologyReqDetailColumnForTechnician(handleReport)}
              sx={{
                '& .MuiDataGrid-cell':{
                  border:'1px solid #d1d5db',
                },
                '& .MuiDataGrid-columnHeaders':{
                  borderBottom: '2px solid #000',
                },
                '& .MuiDataGrid-columnHeader':{
                  borderRight: '1px solid #d1d5db',
                },
              }}
              paginationMode='server'
              rowCount={pagination?.totalElements || 0}
              initialState={{
                pagination:{
                  paginationModel:{
                    pageSize: pagination?.pageSize || 10,
                    page: currentPage-1,
                  },
                },
              }}
              onPaginationModelChange={handlePaginationChange}
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
    </div>
    <Report open={openReport} setOpen={setOpenReport} selectedRow={selectedRow}  />

    </React.Fragment>
  
  )
}

export default Technician