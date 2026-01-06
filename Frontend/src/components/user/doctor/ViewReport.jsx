import Dialog from '@mui/material/Dialog'
import FormatFindings from '../../helper/FormatFindings';
import { useRef } from 'react';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import DownloadButton from '../../shared/DownloadButton';

const ViewReport = ({ open, setOpen, radReq }) => {
  const handleClose = () => {
    setOpen(false);
  };

  const requestedDate = new Date(radReq?.requestedAt);
  const requestedDateFormatted = requestedDate.toLocaleString();

  const generatedDate = new Date(radReq?.report?.generatedAt);
  const generatedDateFormatted = generatedDate.toLocaleString();

  const printRef = useRef(null);

  function normalizeColors(element) { 
    const all = element.querySelectorAll('*'); 
    all.forEach(el => { const style = window.getComputedStyle(el); 
      if (style.color.includes('oklch')) el.style.color = '#000'; 
      if (style.backgroundColor.includes('oklch')) el.style.backgroundColor = '#fff'; }); 
    }

  const handleDownloadPDF = async() =>{
    const element = printRef.current;
    if(!element){
      return;
    }
    normalizeColors(element);
    
    const canvas = await html2canvas(element,{
      scale:2
    });
    const data = canvas.toDataURL('image/png');

    const pdf = new jsPDF({
      orientation: "portrait",
      unit: "px",
      format: 'a4'
});
  const imgProperties = pdf.getImageProperties(data);
  const pdfWidth = pdf.internal.pageSize.getWidth();
  const pdfHeight = (imgProperties.height * pdfWidth / imgProperties.width);

  pdf.addImage(data, 'PNG', 0, 0, pdfWidth, pdfHeight);
  pdf.save(`RadiologyReport_${radReq.patient.name}.pdf`);
    

  }

  return (
    <Dialog 
      onClose={handleClose}
      open={open}
      PaperProps={{
        sx: {
          width: "60%",
          maxWidth: "none",
          height: "70vh",
          maxHeight: "none",
          padding: "20px"
        }
      }}
    >
      <div ref={printRef} className="p-6">
        {/* Report Heading */}
        <h2 className="text-center font-bold underline text-[20px] mb-6">
          Smart Radiology Workflow Manager
        </h2>

        {/* Patient & Doctor Details */}
        <div className="mb-6 space-y-2">
          <div className="flex justify-between"> 
            <p><span className="font-semibold">Patient Name:</span> {radReq?.patient?.name}</p> 
            <p><span className="font-semibold">Requested at:</span> {requestedDateFormatted}</p> 
          </div> 
          <div className="flex justify-between"> 
            <p><span className="font-semibold">Patient ID:</span> {radReq?.patient?.patientId}</p> 
            <p><span className="font-semibold">Generated at:</span> {generatedDateFormatted}</p> 
          </div>
          <p><span className="font-semibold">Imaging Type:</span> {radReq?.imagingType}</p> 
          <p><span className="font-semibold">Doctor Name:</span> {radReq?.doctor?.name}</p> 
          <p><span className="font-semibold">Doctor ID:</span> {radReq?.doctor?.doctorId}</p>
        </div>

        {/* Findings */}
        {<FormatFindings findingsText={radReq?.report?.findings} />}
      </div>
      <div className='mx-auto mt-10' >
          <DownloadButton onClick={handleDownloadPDF} />
        </div>
    </Dialog>
  );
};

export default ViewReport;
