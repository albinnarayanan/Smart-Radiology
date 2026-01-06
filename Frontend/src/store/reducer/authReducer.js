const initialState ={
    user:null,
    isAuthenticated:false,
    isLoading:false,
}

export const authReducer = (state = initialState, action) =>{
    switch(action.type){
        case "LOGIN_USER":
            return {
                ...state,
                user: action.payload,
                isAuthenticated: true,
                isLoading: false,
            }
        case "LOGOUT_USER":
            return {
                user:null,
                isAuthenticated:false,
                isLoading:false,
            }
        default:
            return state;
    }
}