import type { EmpModel, ResultModel, PageResultModel } from '@/api/model/model'
import request from '@/utils/request'

export const queryPageApi = (begin: string, end: string, gender: string, name: string, page: number, pageSize: number) => 
  request.get<any, PageResultModel>(`/emp?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)

export const addApi = (emp: EmpModel) => request.post<any, ResultModel>('/emp', emp)

export const queryInfoApi = (id: number) => request.get<any, ResultModel>(`/emp/${id}`)

export const updateApi = (emp: EmpModel) => request.put<any, ResultModel>(`/emp`, emp)

export const deleteApi = (ids: string) => request.delete<any, ResultModel>(`/emp?ids=${ids}`)

export const queryAllApi = () => request.get<any, PageResultModel>('/emp/list') 
