import type {PageResultModel, ResultModel, StudentModel} from '@/api/model/model'; 
import request from '@/utils/request';

export const queryPageApi = (name: string, degree: string, courseId: string, page: number, pageSize: number)=>
    request.get<any, PageResultModel>(`/student?studentName=${name}&degree=${degree}&courseId=${courseId}&page=${page}&pageSize=${pageSize}`); 

export const deleteApi = (ids: string)=> request.delete<any, ResultModel>(`/student/${ids}`); 

export const addApi = (stu: StudentModel) => request.post<any, ResultModel>(`/student`, stu); 

export const queryInfoApi = (id: number) => request.get<any, ResultModel>(`/student/${id}`); 

export const updateApi = (stu: StudentModel) => request.put<any, ResultModel>(`/student`, stu); 

export const applyPenaltyApi = (id: number, score: number) => request.put<any, ResultModel>( `/student/violation/${id}/${score}`); 

