const initialState = {
    doctors : [],
    docPagination: {}
}

export const doctorReducer = (state = initialState, action)=>{
    switch(action.type){
        case "GET_ALL_DOCTORS":
            return{
                ...state,
                doctors: action.payload,
                docPagination:{
                    ...state.docPagination,
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