import Dialog from '@mui/material/Dialog'
import DialogTitle from '@mui/material/DialogTitle'
import CloseButton from '../../shared/CloseButton'
import { useRef, useState } from 'react';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import DownloadButton from '../../shared/DownloadButton';

const ViewImage = ({open, setOpen, radReq}) => {
  const image  = radReq?.reportImageUrl;

  const [ imageLoaded, setImageLoaded ] = useState(false);

    const handleClose = () =>{
        setOpen(false)
    }

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
      scale:2,
      useCORS: true,
      allowTaint: false

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
  pdf.save(`RadiologyImage_${radReq.patient.name}.pdf`);

  }
    

  return (
    <div>
        <Dialog open={open}
        onClose={handleClose}
        PaperProps={{
            sx: {
            width: "90%",
            maxWidth: "none",
            height: "80vh",
            maxHeight: "none",
            padding: "20px"
            }
      }}> 
      <div className='pb-4'>
          <DialogTitle className='text-center font-bold font-serif'>RADIOLOGY IMAGE</DialogTitle>
        </div>
        <div onClick={handleClose} className='absolute right-4 top-6'>
            <CloseButton/>
        </div>
        {image && 
          <div ref={printRef} className='flex justify-center aspect-3/2'>
            <img crossOrigin='anonymous' className="w-full h-full cursor-pointer transition-transform 
                            duration-300 transform hover:scale-105" 
              src={image} alt="No image Available" 
              onLoad={()=>setImageLoaded(true)}
              onError={()=>setImageLoaded(false)}
            />
          </div>
        }
        <div className='mx-auto mt-10' >
          <DownloadButton onClick={handleDownloadPDF} disabled={!imageLoaded} />
        </div>
        </Dialog>
        
    </div>

  )
}

export default ViewImage;