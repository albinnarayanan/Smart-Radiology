const initialState = {
    imageSchedules : [],
    pagination:{},
}

export const techScheduleReducer = (state = initialState,action) =>{
    switch(action.type){
        case "GET_ALL_MRI_REQS":
            return{
                ...state,
                imageSchedules: action.payload,
                pagination:{
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalPages: action.totalPages,
                    totalElements: action.totalElements,
                    lastPage: action.lastPage,
                }
            }


        default : 
            return state;
    }   
}