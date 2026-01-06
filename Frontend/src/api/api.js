import axios from 'axios';

const api = axios.create({
    baseURL: `${import.meta.env.VITE_SMART_RADIOLOGY_BACKEND_URL}/api`,
    withCredentials: true,

});

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error) =>{
    failedQueue.forEach((prom)=>{
        if(error){
            prom.reject(error);

        }
        else{
            prom.resolve();
        }
    });
    failedQueue = [];
};

api.interceptors.response.use(
    (response)=> response,
    async (error)=>{
        const originalRequest = error.config;
        if (
        originalRequest.url?.includes('/auth/refresh') ||
        originalRequest.url?.includes('/auth/logout')
    ) {
      return Promise.reject(error);
    }
        
        if(error.response?.status == 401 && !originalRequest._retry){
            if(isRefreshing){
                return new Promise((resolve,reject)=>{
                    failedQueue.push({resolve,reject});
                })
                .then(()=> api(originalRequest))
                .catch((err)=> Promise.reject(err));
            }

            originalRequest._retry = true;
            isRefreshing = true;

            try {
                await api.post('/auth/refresh');
                processQueue(null);
                return api(originalRequest);
      } catch (refreshError) {
        // ! VERY IMPORTANT !
        processQueue(refreshError);
        console.warn('Refresh token failed → forcing logout');
        
        try {
          await api.post('/auth/logout', {}, { withCredentials: true });
        } catch (e) {
          console.warn("Logout failed during refresh error", e);
        }

        // Clear both cookies (access + refresh)
        // Use the same path and attributes as when set
        const clearCookie = (name) =>
          (document.cookie = `${name}=; Max-Age=0; path=/api; SameSite=Strict`);

        clearCookie('refreshToken');
        clearCookie('jwt'); 

        // Optional but recommended: redirect to login
        // Prevents stuck UI on protected routes
        if (!window.location.pathname.includes('/login')) {
          window.location.replace('/login');
        }

        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }
        return Promise.reject(error);
    }

);


export default api;