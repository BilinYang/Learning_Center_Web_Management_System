import request from "@/utils/request";
import type { CourseModel, SearchCourseModel } from "./model/model";
import {type AxiosResponse} from 'axios'; 
import type { ResultModel, PageResultModel } from "./model/model";

export const queryPageApi = (courseName: string, begin: string, end: string, page: number, pageSize: number) => 
    request.get<any, PageResultModel>(`/course?courseName=${courseName}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
export const queryAllApi = () => request.get<any, ResultModel>('/course/list'); 

export const deleteApi = (id: number)=> request.delete<any, ResultModel>(`/course/${id}`); 

export const addApi = (course: CourseModel) => request.post<any, ResultModel>(`/course`, course); 

export const getCourseApi = (id: number) => request.get<any, ResultModel>(`/course/${id}`); 

export const updateApi = (course: CourseModel) => request.put<any, ResultModel>(`/course`, course); 



