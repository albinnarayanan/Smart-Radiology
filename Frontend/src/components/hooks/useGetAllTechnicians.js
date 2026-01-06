import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux';
import { useSearchParams } from 'react-router-dom'
import { getAllDoctors, getAllRadiologyReqsForDoc, getAllTechnicians } from '../../store/actions';

const useGetAllTechnicians = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    useEffect(()=>{
        const params = new URLSearchParams();
        const currentPage = searchParams.get("page")
                            ? Number(searchParams.get("page"))
                            : 1;
        params.set("pageNumber", currentPage - 1 );

        const sortOrder = searchParams.get("sortBy")||"asc";
        params.set("sortOrder",sortOrder);
        params.set("sortBy","technicianId");
        
        const queryString = params.toString();
        console.log("QUERY_STRING",queryString);
        
        dispatch(getAllTechnicians(queryString));

    },[dispatch, searchParams])
}

export default useGetAllTechnicians;