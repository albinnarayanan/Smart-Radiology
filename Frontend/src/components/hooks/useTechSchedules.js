import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux';
import { useSearchParams } from 'react-router-dom'
import { getAllRadiologyReqsForTech } from '../../store/actions';

const useTechSchedules = () => {
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
        params.set("sortBy","imagingScheduleId");
        
        const queryString = params.toString();
        console.log("QUERY_STRING",queryString);
        
        dispatch(getAllRadiologyReqsForTech(queryString));

    },[dispatch, searchParams])
}

export default useTechSchedules;