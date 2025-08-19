// Encapsulated TypeScript code - primarily used for backend business requests

// import request object
import request from '@/utils/request'
import { type AxiosResponse } from 'axios'
import type { ResultModel } from '@/api/model/model'
import type {DeptModel, DeptModelArray} from '@/api/model/model'

// search departments 
export const queryAllApi = ()=>request.get<any, ResultModel>('/dept');

// add department 
export const addApi = (dept: DeptModel)=> request.post<any, ResultModel>('/dept', dept);

// get department id 
export const getDeptApi = (id: number) => request.get<any, ResultModel>(`/dept/${id}`);

// edit department information 
export const updateApi = (dept: DeptModel) => request.put<any, ResultModel>(`/dept`, dept);

// delete department 
export const deleteApi = (id: number) => request.delete<any, ResultModel>(`/dept?id=${id}`);

 