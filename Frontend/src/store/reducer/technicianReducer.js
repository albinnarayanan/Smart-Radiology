const initialState = {
    technicians : [],
    techPagination: {}
}

export const technicianReducer = (state=initialState, action)=>{
    switch(action.type){
        case "GET_ALL_TECHNICIANS":
            return{
                ...state,
                technicians: action.payload,
                techPagination:{
                    ...state.techPagination,
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