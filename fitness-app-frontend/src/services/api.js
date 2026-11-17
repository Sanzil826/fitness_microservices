import axios from "axios";

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_URL
});

api.interceptors.request.use((config) => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    console.log("Token exists:", !!token);
    console.log("Request URL:", config.url);

    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }

    if (userId) {
        config.headers["X-User-ID"] = userId;
    }

    console.log("Request headers:", config.headers);

    return config;
});

export const getActivities= ()=> api.get(`/activities`);
export const addActivities= (activity)=> api.post(`/activities`,activity);
export const getActivityDetail = (id)=>{return api.get(`recommendations/activity/${id}`)};