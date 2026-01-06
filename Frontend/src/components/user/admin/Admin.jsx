import { useSelector } from 'react-redux';
import CreateButton from '../../shared/CreateButton';
import { doctorsDetailColumn, techniciansDetailColumn } from '../../helper/TableColumn';
import {useLocation, useNavigate, useSearchParams} from 'react-router-dom';
import { DataGrid } from "@mui/x-data-grid";
import { useState } from 'react';
import useGetAllTechnicians from '../../hooks/useGetAllTechnicians';
import useGetAllDoctors from '../../hooks/useGetAllDoctors';
import AddDoctorForm from './AddDoctorForm';
import AddTechnicianForm from './AddTechnicianForm';
const Admin = () => {
  const {doctors, docPagination} = useSelector((state)=>state.doctors);
  const {technicians, techPagination} = useSelector((state)=>state.technicians);
  
  useGetAllDoctors();
  useGetAllTechnicians();
  

  const doctorTableRecords = doctors?.map((doc)=>({
    id: doc.id,
    doctorName: doc.name,
    specialization: doc.specialization

  }));

  const technicianTableRecords = technicians?.map((tech)=>({
    id: tech.id,
    technicianName: tech.name,
    modality: tech.username

  }));


  const { user } = useSelector((state)=>state.auth);
  const [searchParams] = useSearchParams();
  const pathname = useLocation().pathname;
  const params = new URLSearchParams(searchParams);
  const navigate = useNavigate();
  const [techCurrentPage, setTechCurrentPage] = useState(
      techPagination?.pageNumber + 1 || 1
    );
    const [docCurrentPage, setDocCurrentPage] = useState(
      docPagination?.pageNumber + 1 || 1
    )




  const handlePaginationChangeForDoctor = (paginationModel) => {
    const page = paginationModel.page + 1; // Adjust to 1-based index
    setDocCurrentPage(page);

    params.set("page", page.toString());
    navigate(`${pathname}?${params}`);
  };
  const handlePaginationChangeForTechnician = (paginationModel) => {
    const page = paginationModel.page + 1; // Adjust to 1-based index
    setTechCurrentPage(page);

    params.set("page", page.toString());
    navigate(`${pathname}?${params}`);
  };

  const [openDoctorDialog, setOpenDoctorDialog] = useState(false);
  const [openTechnicianDialog, setOpenTechnicianDialog] = useState(false);


  const handleCreateDoctor = () =>{
    setOpenDoctorDialog(true);
  }
  const handleCreateTechnician = () =>{
    setOpenTechnicianDialog(true);

  }

  return (
    <div className='flex flex-col items-center justify-center pt-15' >
      <div className=' bg-slate-700 rounded-md shadow-md w-50 flex items-center justify-center '>
        <h1 className='font-bold text-5xl text-white' >ADMIN</h1>
      </div>
      <div className='flex flex-row pt-10 gap-5'>
        <CreateButton button='Add Doctor' onClick={handleCreateDoctor} />
        {openDoctorDialog && <AddDoctorForm open={openDoctorDialog} setOpen={setOpenDoctorDialog} />}
        <CreateButton button='Add Technician' onClick={handleCreateTechnician}/>  
        {openTechnicianDialog && <AddTechnicianForm open={openTechnicianDialog} setOpen={setOpenTechnicianDialog} />}
      
      </div>
      <div className="max-w-fit mx-auto flex gap-20 mt-10">
                    <DataGrid
                      className="w-full"
                      rows={doctorTableRecords}
                      columns={doctorsDetailColumn()}
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
                      paginationMode="server"
                      rowCount={docPagination?.totalElements || 0}
                      initialState={{
                        pagination: {
                          paginationModel: {
                            pageSize: docPagination?.pageSize || 10,
                            page: docCurrentPage - 1,
                          },
                        },
                      }}
                      onPaginationModelChange={handlePaginationChangeForDoctor}
                      disableRowSelectionOnClick
                      disableColumnResize
                      pageSizeOptions={[docPagination?.pageSize || 10]}
                      pagination
                      paginationOptions={{
                        showFirstButton: true,
                        showLastButton: true,
                        hideNextButton: docCurrentPage === docPagination?.totalPages,
                      }}
                    />
                    <DataGrid
                      className="w-full"
                      rows={technicianTableRecords}
                      columns={techniciansDetailColumn()}
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
                      paginationMode="server"
                      rowCount={techPagination?.totalElements || 0}
                      initialState={{
                        pagination: {
                          paginationModel: {
                            pageSize: techPagination?.pageSize || 10,
                            page: techCurrentPage - 1,
                          },
                        },
                      }}
                      onPaginationModelChange={handlePaginationChangeForTechnician}
                      disableRowSelectionOnClick
                      disableColumnResize
                      pageSizeOptions={[techPagination?.pageSize || 10]}
                      pagination
                      paginationOptions={{
                        showFirstButton: true,
                        showLastButton: true,
                        hideNextButton: techCurrentPage === techPagination?.totalPages,
                      }}
                    />
                  </div>

    </div>
  )
}

export default Admin