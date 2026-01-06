const initialState = {
    radiology_requests:[],
    pagination:{},

}

export const radiologyReducer = (state=initialState,action)=>{
    switch(action.type){
        case "FETCH_RADIOLOGY_REQUESTS":
            return{
                ...state,
                radiology_requests: action.payload,
                pagination:{
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalPages: action.totalPages,
                    totalElements: action.totalElements,
                    lastPage: action.lastPage,
                }

            }
        default:
            return state;    
    }

}