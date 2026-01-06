import { FaEdit } from "react-icons/fa";
import { FaEye } from "react-icons/fa";


export const radiologyReqDetailColumnForDoctor = (handleViewReport, handleViewImage) =>[
    {
        //column for radiology request ID
        sortable: false,
        disableColumnMenu:true,
        field: "id",
        headerName: "Radiology Request ID",
        minWidth: 190,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Radiology Request ID</span>
    },
    {
        //column for patient priority
        sortable: false,
        disableColumnMenu:true,
        field: "patientId",
        headerName: "Patient ID",
        minWidth: 188,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Patient ID</span>
    },
    {
        //column for patient name
        sortable: false,
        disableColumnMenu:true,
        field: "patientName",
        headerName: "Patient Name",
        minWidth: 200,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Patient Name</span>
    },
    {
        //column for imaging type
        sortable: false,
        disableColumnMenu:true,
        field: "imagingType",
        headerName: "Imaging Type",
        minWidth: 200,
        headerAlign:"center",
        editable: false,
       
        renderHeader: (params) => <span className="text-center">Imaging Type</span>
    },
    {
        //column for patient priority
        sortable: false,
        disableColumnMenu:true,
        field: "priority",
        headerName: "Priority",
        minWidth: 200,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Priority</span>
    },
    {
    // Custom action column with an "Edit" button.
    field: "action",
    headerName: "Action",
    headerAlign: "center",
    editable: false,
    headerClassName: "text-black font-semibold text-center",
    cellClassName: "text-slate-700 font-normal",
    sortable: false,
    minWidth: 300,
    renderHeader: (params) => <span>Action</span>,
    renderCell: (params) =>{
        const isReportAvailable = params.row.reportAvailable;
        return(
            <div className='flex justify-center items-center space-x-2 h-full pt-2'>
                <button
                disabled={!isReportAvailable}
                onClick={() => handleViewReport(params.row)}
                className={`flex items-center px-4 h-9 rounded-md 
                    ${isReportAvailable
                    ?"bg-slate-800 text-white cursor-pointer"
                    :"bg-gray-400 text-gray-200 cursor-not-allowed"
                    }`}
              >
                <FaEye size={20} className="mr-2" />
                View Report
              </button>
              <button
                disabled={!isReportAvailable}
                onClick={() => handleViewImage(params.row)}
                className={`flex items-center px-4 h-9 rounded-md 
                    ${isReportAvailable
                    ?"bg-slate-800 text-white cursor-pointer"
                    :"bg-gray-400 text-gray-200 cursor-not-allowed"
                    }`}
              >
            <FaEye size={20} className="mr-2" />
            View Image
          </button>
            </div>
        )
    }
  }
   
    
    
]

export const radiologyReqDetailColumnForTechnician = (handleReport) =>[
    {
        //column for radiology request ID
        sortable: false,
        disableColumnMenu:true,
        field: "id",
        headerName: "Imaging Schedule Id",
        minWidth: 180,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Imaging Schdule ID</span>
    },
    {
        //column for patient priority
        sortable: false,
        disableColumnMenu:true,
        field: "patientId",
        headerName: "Patient ID",
        minWidth: 180,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Patient ID</span>
    },
    {
        //column for patient name
        sortable: false,
        disableColumnMenu:true,
        field: "patientName",
        headerName: "Patient Name",
        minWidth: 180,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Patient Name</span>
    },
    {
        //column for imaging type
        sortable: true,
        disableColumnMenu:true,
        field: "patientAge",
        headerName: "Patient Age",
        minWidth: 180,
        headerAlign:"center",
        editable: false,
       
        renderHeader: (params) => <span className="text-center">Age</span>
    },
    {
        //column for patient priority
        sortable: false,
        disableColumnMenu:true,
        field: "priority",
        headerName: "Priority",
        minWidth: 180,
        headerAlign:"center",
        editable: false,

        renderHeader: (params) => <span className="text-center">Priority</span>
    },
    {
    // Custom action column with an "Edit Report" button.
    field: "action",
    headerName: "Action",
    headerAlign: "center",
    editable: false,
    headerClassName: "text-black font-semibold text-center",
    cellClassName: "text-slate-700 font-normal",
    sortable: false,
    width: 250,
    renderHeader: (params) => <span>Action</span>,
    renderCell: (params) =>{
        return(
            <div className='flex justify-center items-center space-x-2 h-full pt-2'>
                <button onClick={() => handleReport(params.row)} className='flex items-center bg-blue-500
                 text-white px-4 h-9 rounded-md cursor-pointer'>
                    <FaEdit className='mr-2' />
                    Write Report
                </button>
            </div>
        )
    }
  }
]

export const doctorsDetailColumn = () =>[
    {
       
        sortable: false,
        disableColumnMenu:true,
        field: "id",
        headerName: "Doctor Id",
        minWidth: 200,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">ID</span>
    },
    {
        
        sortable: false,
        disableColumnMenu:true,
        field: "doctorName",
        headerName: "Doctor Name",
        minWidth: 220,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Doctor Name</span>
    },
    {
        
        sortable: false,
        disableColumnMenu:true,
        field: "specialization",
        headerName: "Specialization",
        minWidth: 210,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Specialization</span>
    },

]

export const techniciansDetailColumn = () =>[
    {
        
        sortable: false,
        disableColumnMenu:true,
        field: "id",
        headerName: "Technician Id",
        minWidth: 200,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">ID</span>
    },
    {
        
        sortable: false,
        disableColumnMenu:true,
        field: "technicianName",
        headerName: "Technician Name",
        minWidth: 220,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Technician Name</span>
    },
    {
        
        sortable: false,
        disableColumnMenu:true,
        field: "modality",
        headerName: "Modality",
        minWidth: 210,
        headerAlign:"center",
        editable: false,
        
        renderHeader: (params) => <span className="text-center">Modality</span>
    },

]