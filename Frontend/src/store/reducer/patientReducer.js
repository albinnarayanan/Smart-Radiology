const initialState = {
    patients:[],
    pagination:{},
}

export const patientReducer = (state=initialState, action)=>{
    switch(action.type){
        case "GET_ALL_PATIENTS":
            return{
                ...state,
                patients: action.payload,
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